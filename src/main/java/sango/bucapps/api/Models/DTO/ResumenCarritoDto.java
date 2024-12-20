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
public class ResumenCarritoDto {
    private Date recoleccion;
    private Date entrega;
    private String direccion;
    private Long cp;
    private String nombre;
    private Long tel;
    private Integer cantidadPrendas;
    private List<SubOpcionesPrendaDto> prendasList;
    private Double total;
    private String estado;
    private Long id;
    private Timestamp creado;
    private String formaDePago;
    private String cuandoEfectivo;
    private String usuario;
    private Double lat;
    private Double lng;
    private String nombreRepartidor;
    private Long idRepartidor;
}
