package sango.bucapps.api.Models.Entity;

import lombok.Getter;
import lombok.Setter;

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

    @JoinColumn(name = "carrito_id")
    @OneToOne
    private Carrito carrito;

    @Column
    private Date fechaCreado;
}
