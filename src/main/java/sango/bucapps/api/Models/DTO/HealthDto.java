package sango.bucapps.api.Models.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class HealthDto {
    private Date requestTime;
    private Boolean isUp;
}
