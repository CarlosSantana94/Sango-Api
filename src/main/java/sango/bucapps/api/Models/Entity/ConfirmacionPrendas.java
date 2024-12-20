package sango.bucapps.api.Models.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "confirmacion_prendas")
public class ConfirmacionPrendas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reg;


    private Long cantidad;

    @ManyToOne()
    @JoinColumn(name = "id_prenda")
    private SubOpcionesPrenda subOpcionesPrenda;

    private String img;
    private String nombre;
    private Double precio;
    private Double precioTotal;
    private String servicio;
    private Boolean revisada;
    private Long idCarrito;
    private String commentario;
}
