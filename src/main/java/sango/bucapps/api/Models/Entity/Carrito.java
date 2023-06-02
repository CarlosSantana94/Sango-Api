package sango.bucapps.api.Models.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direccion_id")
    private Direccion direccion;

    @Column
    private Double total;

    @Column
    private String estado;

    @Column
    private String formaDePago;

    @Column
    private String cuandoOToken;

    @Column
    private String ordenConekta;

    @Column
    private Timestamp creado;


    @ManyToMany(fetch = FetchType.LAZY)
    private List<SubOpcionesPrenda> subOpcionesPrendas;

    @Column
    private String motivoCancelacion;


}
