package sango.bucapps.api.Models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListaDePrendasDTO {
    private Long cantidad;
    private Long id;
    private Long reg;
    private String img;
    private String nombre;
    private Double precio;
    private Double precioTotal;
    private String servicio;
    private Boolean revisada;
    private String comentario;
    private String padre;
}
