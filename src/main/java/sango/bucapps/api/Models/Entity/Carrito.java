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

    @Column
    private Double total;

    @Column
    private String estado;

    @Column
    private String formaDePago;

    @Column
    private Timestamp creado;

    @ManyToMany
    private List<SubOpcionesPrenda> subOpcionesPrendas;

}
