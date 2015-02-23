package inject.log4j.redis.rest.service;


import inject.log4j.redis.rest.utils.SystemTimeProvider;
import inject.log4j.redis.rest.utils.TimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
public class SendMessagesCallback extends WatcherCallback {

    @Value("${watcher.waiting.maximum.iteration:30}")
    private Integer maxIteration; // Nombre d'iterations maximales.

    @Value("${watcher.waiting.time.interval:2}")
    private Integer waitingTimeInterval; // En secondes.

    private Logger sender = LoggerFactory.getLogger("sendMessages");
    private Logger logger = LoggerFactory.getLogger(SendMessagesCallback.class);

    private TimeProvider timeProvider = new SystemTimeProvider();

     @Override
    public void launch(Path input) {
         logger.info("Lecture du fichier : {}", input);
         try {
             FileInputStream inputFile = new FileInputStream(new File(input.toUri()));
             CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
             decoder.onMalformedInput(CodingErrorAction.IGNORE);
             InputStreamReader reader = new InputStreamReader(inputFile, decoder);
             BufferedReader bufferedReader = new BufferedReader(reader);
             String line = bufferedReader.readLine();
             // Indicateur de performance
             int compteur = 0;
             long startDate = timeProvider.currentTimeMillis();
             while (line != null) {
                 line = bufferedReader.readLine();
                 sender.info(line);
                 compteur++;
             }
             long endDate = timeProvider.currentTimeMillis();
             logger.info("NB de Ligne envoyé : {} msgs", compteur);
             long timeElapse = endDate - startDate;
             logger.info("Temps d'exécution : {} ms",timeElapse);
             logger.info("Nb de message /s : {} msgs/s ou {} msgs/ms",compteur/(timeElapse / 1000),compteur/timeElapse);
             bufferedReader.close();
         } catch (FileNotFoundException e) {
             logger.error("erreur lors de la lecture du fichier : {}", input, e);
         } catch (IOException e) {
             logger.error("erreur lors de la lecture du fichier : {}",input,e);
         }
     }

    @Override
    protected int getWaitingTimeInterval() {
        return waitingTimeInterval;
    }

    @Override
    protected int getMaxIteration() {
        return maxIteration;
    }


//    private void closeQuietly(Closeable c) {
//        if (c != null) {
//            try {
//                c.close();
//            } catch (IOException ignored) {
//            }
//        }
//    }
//

}
