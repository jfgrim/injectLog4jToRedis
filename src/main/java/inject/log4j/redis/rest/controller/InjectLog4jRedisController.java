package inject.log4j.redis.rest.controller;

import inject.log4j.redis.rest.domain.MonitoredSystemData;
import inject.log4j.redis.rest.domain.StatutEnum;
import inject.log4j.redis.rest.dto.ApiDto;
import inject.log4j.redis.rest.service.IoMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

@Controller
public class InjectLog4jRedisController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InjectLog4jRedisController.class);

    @Inject
    private ApiDto apiDto;

    @Value("${jobcre.thresHold.space.free:30}")
    private int thresholdFreeSpacePermit;

    @RequestMapping(method = RequestMethod.GET, value = "/api/version")
    @ResponseBody
    public ApiDto getVersion() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(apiDto.toString());
        }
        return apiDto;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/monitor/inject")
    @ResponseBody
    public MonitoredSystemData monitor() {
        MonitoredSystemData monitoredSystemData = null;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Test de l'espace disque avec un seuil de {}% d'espace libre", thresholdFreeSpacePermit);
        }
        if (IoMonitor.isValidDataSpace(thresholdFreeSpacePermit)) {
            monitoredSystemData = MonitoredSystemData.onInfo("inject", StatutEnum.OK);
        } else {
            monitoredSystemData = MonitoredSystemData.onError("inject", "Space Free : " + IoMonitor.getValidDataSpace() + "%");
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(monitoredSystemData.toString());
        }
        return monitoredSystemData;
    }
}
