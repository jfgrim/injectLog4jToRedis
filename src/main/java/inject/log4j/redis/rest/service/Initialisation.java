package inject.log4j.redis.rest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.io.IOException;

@Service
public class Initialisation {
    private Logger LOGGER = LoggerFactory.getLogger(Initialisation.class);
    private Logger loggerSupervision = LoggerFactory.getLogger("supervision");

    @Inject
    private WatcherService watcherService;
    @Inject
    private SendMessagesCallback sendMessagesCallback;

    @Value("${watcher.librairie.path}")
    private String logPath;

    @Value("${watcher.check.at.start}")
    private boolean checkAtStart;

    @PostConstruct
    public void init() {
        try {

            LOGGER.info("Starting Injection with Watcher path: {}", logPath);
            watcherService.watch(logPath, sendMessagesCallback, checkAtStart);
            LOGGER.info("Injector Watcher started.");

        } catch (IOException e) {
            LOGGER.error("Erreur sur l'ouverture du Watcher, on stoppe l'application", e);
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void destruct() {
        watcherService.stopCallbacks();
        watcherService.stopWatching();
    }

}
