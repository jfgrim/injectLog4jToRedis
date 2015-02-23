# injectLog4jToRedis

Injecteur utilisant l'API redis4Log4jAppender.


```xml
## Configuration

# Path racine de travail
watcher.librairie.path=/data/tmp/

# Complement du répertoire watcher.librairie.path ==> ce répertoire est scruter par le Watcher.
# Déposer un fichier dans ce répertoire et ce fichier sera ouvert et envoyé via redis4Log4jAppender
inject.input.directory=processing/

# Complement du répertoire watcher.librairie.path ==> ce répertoire est le répertoire de dépot de fin de travail.
# Le fichier sera déplacé dans ce répertoire une fois traité
inject.output.directory=processed/

# activation de la Verification dès le démarrage de la présence des fichiers log par le Watcher
watcher.check.at.start=true

# Nombre d'iterations maximales (attente de fin de fichier)
watcher.waiting.maximum.iteration=30

# Durée entre deux polling du répertoire (en ms). Ceci afin d'éviter de consommer tout le CPU dans la boucle de polling.
watcher.check.timer=1000

# Temps d'attente entre 2 itérations (attente de fin de fichier)
watcher.waiting.time.interval=2
```

### Example with JSONEventLayout
## POM.XML
```xml
       <dependency>
            <groupId>com.github.jfgrim</groupId>
            <artifactId>redis4AppenderLog4j</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>net.logstash.log4j</groupId>
            <artifactId>jsonevent-layout</artifactId>
            <version>1.7</version>
        </dependency>
```
##LOG4J.XML
```xml
    <appender name="redis" class="client.redis.log4jAppender.RedisAppender">
        <param name="key" value="keylog" />
        <param name="hosts" value="localhost:6379,10.11.11.171:7000" />
        <param name="mode" value="channel" />
        <param name="firstDelay" value="250" />
        <param name="batchSize" value="100" />
        <param name="attemptDelay" value="2000" />
        <param name="numberRetryToRedis" value="3" />
        <param name="period" value="100" />
        <param name="alwaysBatch" value="true" />
        <layout class="net.logstash.log4j.JSONEventLayoutV1" />
    </appender>
```

### License

Copyright (c) 2014 JeF Grimault

Published under Apache Software License 2.0, see LICENSE

[![Rochester Made](http://rochestermade.com/media/images/rochester-made-dark-on-light.png)](http://rochestermade.com)
