package sango.bucapps.api.Controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sango.bucapps.api.Models.DTO.HealthDto;

import java.util.Date;


@RestController
@CrossOrigin(maxAge = 3600)
public class HealthController {

    @GetMapping(value = "/")
    public HealthDto getIndexConnection() {

        HealthDto healthDto = new HealthDto();
        healthDto.setRequestTime(new Date());
        healthDto.setIsUp(true);
        healthDto.setAppName("SANGO-API");
        healthDto.setVersion(4);

        return healthDto;
    }

    @GetMapping(value = "/health")
    public HealthDto getHealth() {

        HealthDto healthDto = new HealthDto();
        healthDto.setRequestTime(new Date());
        healthDto.setIsUp(true);
        healthDto.setAppName("SANGO-API");
        healthDto.setVersion(4);

        return healthDto;
    }
}
