package inject.log4j.redis.rest.service;

import com.google.common.base.Function;
import inject.log4j.redis.rest.domain.MaxIterationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

/**
 * Classe abstraite à étendre pour créer un callback pour {@link WatcherService}.
 */
public abstract class WatcherCallback implements Function<Path, Void> {
    private Logger logger = LoggerFactory.getLogger(WatcherCallback.class);


    protected abstract int getWaitingTimeInterval();

    protected abstract int getMaxIteration();

    @Override
    public Void apply(final Path input) {
        try {
            FileTime prevFileTime;
            FileTime newFileTime = Files.getLastModifiedTime(input);
            logger.debug("File[{}] create time : {} .", input.toFile().getName(), newFileTime.toMillis());
            int count = 0;
            do {
                if (count++ >= getMaxIteration()) {
                    throw new MaxIterationException(count, getMaxIteration());
                }

                prevFileTime = newFileTime;
                logger.debug("File[{}] Iter[{}] sleep for {} sec.", input.toFile().getName(), count, getWaitingTimeInterval());
                Thread.sleep(getWaitingTimeInterval() * 1000);
                newFileTime = Files.getLastModifiedTime(input);
                logger.debug("File[{}] Iter[{}] last modified time : {} .", input.toFile().getName(), count, newFileTime.toMillis());

            } while (prevFileTime.toMillis() != newFileTime.toMillis());

            this.launch(input);

        } catch (Exception e) {
            logger.error("Error dans la fonction de callback", e);
        }

        return null;
    }

    /**
     * Lancement du callback.
     *
     * @param input Le chemin vers le fichier nouvellement créer.
     * @throws Exception
     */
    public abstract void launch(Path input) throws Exception;
}
