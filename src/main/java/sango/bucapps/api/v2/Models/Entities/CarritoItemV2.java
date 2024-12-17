package sango.bucapps.api.v2.Models.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import sango.bucapps.api.Models.Entity.SubOpcionesPrenda;
import sango.bucapps.api.v2.Models.Enums.EstadoPrenda;

import javax.persistence.*;

@Entity
@Table(name = "carrito_item")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarritoItemV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sub_opcion_prenda_id", nullable = false)
    private SubOpcionesPrenda prenda;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "carrito_id", nullable = false)
    private CarritoV2 carrito;

    private int cantidad;

    @Enumerated(EnumType.STRING)
    private EstadoPrenda estado = EstadoPrenda.PENDIENTE;

    private String commentario;


}
