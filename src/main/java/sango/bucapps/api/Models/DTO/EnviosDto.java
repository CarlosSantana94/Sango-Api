package sango.bucapps.api.Models.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnviosDto {
    private Date fecha;
    private Long disponibles;

    private String motivo;
}
