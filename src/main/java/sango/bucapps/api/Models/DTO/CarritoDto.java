package sango.bucapps.api.Models.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarritoDto {

    private Long id;
    private Double total;
    private String estado;
    private String formaDePago;
    private Timestamp creado;
    private Long cantidad;
    private DireccionDto direccion;
    private Date recoleccion;
    private Date entrega;
    private List<SubOpcionesPrendaDto> subOpcionesPrendas;

}
