package inject.log4j.redis.rest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Service Watcher pour surveiller la création de nouveau fichier dans un repertoire.
 */
@Service
public class WatcherService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WatcherService.class);
    private static final ExecutorService watcherExecutor = Executors.newCachedThreadPool();
    private static final ExecutorService callbackExecutor = Executors.newSingleThreadExecutor();

    @Value("${inject.input.directory:processing/}")
    private String inputDirectory;

    @Value("${inject.output.directory:processed/}")
    private String outputDirectory;

    @Value("${watcher.check.timer:1000}")
    private Integer checkTimer;

    /**
     * Lance le watcher sur un répertoire et lance le callback lors de la création d'un fichier.
     *
     * @param dirPath            Le repertoire à surveiller.
     * @param callback           La fonction de callback qui sera appelée quand un nouveau fichier sera créer.
     * @param checkAlreadyExists Exécute le callback sur les fichiers déjà présents dans le répertoire.
     * @throws java.io.IOException
     */
    public void watch(final String dirPath, final WatcherCallback callback, final boolean checkAlreadyExists) throws IOException {
        LOGGER.info("WatcherService Watch Starting ...");
        try {
            FileSystem fs = FileSystems.getDefault();
            WatchService ws = fs.newWatchService();
            final Path inputPath = fs.getPath(dirPath).resolve(inputDirectory);
            inputPath.toFile().mkdirs();
            final Path outputPath = fs.getPath(dirPath).resolve(outputDirectory);
            outputPath.toFile().mkdirs();

            final WatchKey key = inputPath.register(ws, StandardWatchEventKinds.ENTRY_CREATE);

            if (checkAlreadyExists) {
                for (File file : inputPath.toFile().listFiles()) {
                    launchCallback(inputPath, file.toPath(), callback);
                }
            }

            // On lance le Watcher dans un nouveau Thread pour ne pas bloquer l'appelant.
            watcherExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    while (!callbackExecutor.isShutdown() && !watcherExecutor.isShutdown()) { // Boucle globale
                        try {
                            for (WatchEvent<?> event : key.pollEvents()) { // Boucle sur les évènements
                                final Object c = event.context();
                                if (c instanceof Path && !callbackExecutor.isShutdown()) {
                                    // Lors d'une création de fichier, on lance le callback dans un nouveau Thread.
                                    launchCallback(inputPath, (Path) c, callback);
                                }
                                moveFileAfterWork(inputPath,outputPath,(Path) c);
                            }
                        } catch (Exception e) {
                            LOGGER.error("Erreur dans la boucle du watcher", e);
                        }

                        if (checkTimer > 0) {
                            try {
                                Thread.sleep(checkTimer);
                            } catch (InterruptedException e) {
                                LOGGER.error(e.getMessage(), e);
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            LOGGER.error("Impossible de lancer le watcher", e);
            throw new RuntimeException(e);
        }
        LOGGER.info("WatcherService Watch Started.");
    }

    /**
     * Lance le callback dans un nouveau Thread via le callbackExecutor.
     *
     * @param path     Le chemin du répertoire watché.
     * @param file     Le fichier nouvellement créé.
     * @param callback
     */
    private void launchCallback(final Path path, final Path file, final WatcherCallback callback) {
        callbackExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.apply(path.resolve(file));
                } catch (Exception e) {
                    LOGGER.error("Erreur dans le callback path[{}], file[{}]", path, file, e);
                }
            }
        });
    }

    private void moveFileAfterWork(final Path inputPath, final Path outputPath, final Path file){
        callbackExecutor.execute(new Runnable() {
            @Override
            public void run() {
                File source = inputPath.resolve(file).toFile();
                File destination = outputPath.resolve(file).toFile();
                source.renameTo(destination);
            }
        });
    }
    /**
     * Arrête tous les watchers en cours d'exécution.
     */
    public void stopWatching() {
        if (!watcherExecutor.isShutdown()) {
            watcherExecutor.shutdownNow();
        }
    }

    /**
     * Arrête tous les callbacks en cours d'exécution (lancés par le watcher).
     */
    public void stopCallbacks() {
        if (!callbackExecutor.isShutdown()) {
            callbackExecutor.shutdown();
        }
    }

    /**
     * Attend la fin de l'exécution de tous les jobs.
     *
     * @return true si les jobs sont terminés, false si timeout.
     * @throws InterruptedException Si le thread a été interrompu pendant l'attente.
     */
    public boolean waitCallbacks() throws InterruptedException {
        return callbackExecutor.awaitTermination(10, TimeUnit.MINUTES);
    }

    public void setInputDirectory(String inputDirectory) {
        this.inputDirectory = inputDirectory;
    }

    public void setCheckTimer(Integer checkTimer) {
        this.checkTimer = checkTimer;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }
}
