package inject.log4j.redis.rest.service;

/*
    Monitoring de l'espace disque afin d'avoir suffisamment pour la dépose des fichiers intermédiaires
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class IoMonitor {

    private static Logger logger = LoggerFactory.getLogger(IoMonitor.class);
    private static Logger loggerSupervision = LoggerFactory.getLogger("supervision");

    public static boolean isValidDataSpace(int thresholdFreeSpacePermit) {
        File file = new File("/");
        long totalSpace = file.getTotalSpace(); //total disk space in bytes.
        long freeSpace = file.getFreeSpace(); //unallocated / free disk space in bytes.
        int freePercent = (int) (((double) freeSpace / totalSpace) * 100);
        if (freePercent < thresholdFreeSpacePermit) {
            logger.warn("La limite de {}% de l'espace disponible a été dépassée : Espace disponible : [{} mb] - Espace total : [{} mb] => Espace disponible en % : [{}%]"
                    , thresholdFreeSpacePermit, freeSpace / 1024 / 1024, totalSpace / 1024 / 1024, freePercent);
            loggerSupervision.error("La limite de {}% de l'espace disponible a été dépassée : Espace disponible : [{} mb] - Espace total : [{} mb] => Espace disponible en % : [{}%]"
                    , thresholdFreeSpacePermit, freeSpace / 1024 / 1024, totalSpace / 1024 / 1024, freePercent);
            return false;
        }
        loggerSupervision.info("Le disponibilté de l'espace disque est : Espace disponible :  Espace disponible : [{} mb] - Espace total : [{} mb] => Espace disponible en % : [{}%]"
                , freeSpace / 1024 / 1024, totalSpace / 1024 / 1024, freePercent);
        return true;
    }

    public static int getValidDataSpace() {
        File file = new File("/");
        long totalSpace = file.getTotalSpace(); //total disk space in bytes.
        long freeSpace = file.getFreeSpace(); //unallocated / free disk space in bytes.
        return (int) (((double) freeSpace / totalSpace) * 100);
    }
}
