package inject.log4j.redis.rest.launcher;

//import inject.log4j.redis.rest.service.IoMonitor;
//import org.apache.catalina.Host;
//import org.apache.catalina.LifecycleException;
//import org.apache.catalina.core.StandardHost;
//import org.apache.catalina.startup.Tomcat;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.ServletException;
//import java.io.File;

public class Main {
//    private static final Logger logger = LoggerFactory.getLogger(Main.class);
//    private static final Logger loggerSupervision = LoggerFactory.getLogger("supervision");
//
//    /**
//     * @param args
//     * @throws ServletException
//     * @throws LifecycleException
//     */
//    public static void main(String[] args) throws ServletException, LifecycleException {
//
//        // Verification de l'état de l'espace disque avant de commencer.
//        int thresholdFreeSpacePermit = 30; //le defaut
//        String freeSpace = System.getenv("SPACE_FREE");
//        String webPort = System.getenv("PORT");
//        String envHosts = System.getenv("HOSTNAME");
//        System.out.println("paramètre d'environnement HOST : " + envHosts + ", PORT : " + webPort + ", SPACE_FREE : " + freeSpace);
//
//        thresholdFreeSpacePermit = Integer.parseInt(freeSpace);
//        if (!IoMonitor.isValidDataSpace(thresholdFreeSpacePermit)) {
//            System.out.println("[ERROR] Erreur, l'espace disque n'est pas suffisant pour que l'application puisse fonctionner de façon optimale, Seuil dépassé : " + thresholdFreeSpacePermit + "%");
//            System.exit(1);
//        }
//        String webappDirLocation = "src/main/webapp";
//        Tomcat tomcat = new Tomcat();
//
//        //The port that we should run on can be set into an environment variable
//        //Look for that variable and default to 8080 if it isn't there.
//        if (webPort == null || webPort.isEmpty()) {
//            webPort = "8080";
//        }
//        if (envHosts != null && envHosts.contains(",")) {
//            String[] hostnames = StringUtils.split(",", envHosts);
//            for (String hostname : hostnames) {
//                if (hostname != null || !hostname.isEmpty()) {
//                    System.out.println("Hostname : " + hostname);
//                    Host host = new StandardHost();
//                    host.setName(hostname);
//                    tomcat.setHost(host);
//                    tomcat.getEngine().addChild(host);
//                } else {
//                    logger.info("Config host par defaut");
//                }
//            }
//        } else {
//            String hostname = null;
//            if (envHosts != null) {
//                hostname = envHosts;
//            } else {
//                hostname = "localhost";
//            }
//            logger.info("Hostname : " + hostname);
//            Host host = new StandardHost();
//            host.setName(hostname);
//            tomcat.setHost(host);
//            tomcat.getEngine().addChild(host);
//        }
//        tomcat.setHostname("localhost");
//        tomcat.setPort(Integer.valueOf(webPort));
//        tomcat.getServer().setShutdown("STOPSERVER");
//
//        tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
//
//        logger.info("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());
//
//        tomcat.start();
//        tomcat.getServer().await();
//
//    }

}
