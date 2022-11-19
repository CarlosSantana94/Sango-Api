package sango.bucapps.api.Models.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RepartidorDto {

    private Long id;

    private Long idRepartidor;

    private Date fecha;

    private Double lat;

    private Double lng;

    private Double speed;

    private Double accuracy;
}
