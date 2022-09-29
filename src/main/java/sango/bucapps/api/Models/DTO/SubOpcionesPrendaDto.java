package sango.bucapps.api.Models.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubOpcionesPrendaDto {
    private Long id;
    private String nombre;
    private Double precio;
    private String servicio;
    private Long opcionId;
    private Double precioTotal;
    private String descripcion;
    private String img;
    private Long cantidad;
}
