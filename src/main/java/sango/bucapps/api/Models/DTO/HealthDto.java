package sango.bucapps.api.Models.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class HealthDto {
    private String appName;
    private Date requestTime;
    private Date buildTime;
    private Boolean isUp;
    private Integer version;
}
