package sango.bucapps.api.Models.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import sango.bucapps.api.v2.Models.Entities.CarritoV2;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Getter
@Setter
@Table(name = "envios")
public class Envios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Date fechaRecoleccion;

    @Column
    private Date fechaEntrega;

    @JoinColumn(name = "carrito_id_v2", nullable = false)
    @OneToOne
    @JsonIgnore
    private CarritoV2 carritoV2;

    @Column
    private Date fechaCreado;

    @Column
    private Date fechaModificado;

    @Column
    private Date fechaOriginalRecoleccion;

    @Column
    private Date FechaOriginalEntrega;

    @Column
    private String motivoModificacion;

}
