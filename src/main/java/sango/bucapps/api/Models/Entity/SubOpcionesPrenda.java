package sango.bucapps.api.Models.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "sub_opciones_prenda")
public class SubOpcionesPrenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @Column
    private Double precio;

    @Column
    private String descripcion;

    @Column
    private String img;

    @JoinColumn(name = "opcion_id")
    @ManyToOne
    private OpcionesPrenda opcionesPrenda;

    @Column
    private Boolean porMetro;
}
