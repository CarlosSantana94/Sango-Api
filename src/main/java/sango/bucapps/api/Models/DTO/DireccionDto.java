package sango.bucapps.api.Models.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DireccionDto {

    private Long id;
    private Long cp;
    private Long tel;
    private String indicacion;
    private String alias;
    private String direccion;
    private Double lat;
    private Double lng;
    private String nombre;
    private String interior;
}
