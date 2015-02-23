package inject.log4j.redis.rest.config;

import inject.log4j.redis.rest.service.Initialisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


@Configuration
@PropertySource({"classpath:inject.properties"})
@ComponentScan(basePackages = "inject.log4j.redis")
public class InjectConfig {
    private Logger LOGGER = LoggerFactory.getLogger(InjectConfig.class);

    @Bean(initMethod = "init")
    protected Initialisation initialisation() {
        LOGGER.debug("Initialisation du WatcherService");
        return new Initialisation();
    }

}
