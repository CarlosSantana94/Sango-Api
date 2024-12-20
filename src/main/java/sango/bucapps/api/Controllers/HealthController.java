package sango.bucapps.api.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sango.bucapps.api.Models.DTO.HealthDto;

import java.util.Date;


@RestController
@CrossOrigin(maxAge = 3600)
public class HealthController {

    @Autowired
    BuildProperties buildProperties;

    @GetMapping(value = "/")
    public HealthDto getIndexConnection() {

        return getHealthDto();
    }

    private HealthDto getHealthDto() {
        HealthDto healthDto = new HealthDto();
        healthDto.setRequestTime(new Date());
        healthDto.setBuildTime(Date.from(buildProperties.getTime()));
        healthDto.setIsUp(true);
        healthDto.setAppName("SANGO-API2.22-PROD");
        healthDto.setVersion(24);

        return healthDto;
    }

    @GetMapping(value = "/health")
    public HealthDto getHealth() {

        return getHealthDto();
    }
}
