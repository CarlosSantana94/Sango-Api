package sango.bucapps.api.Models.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "direccion")
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long cp;

    @Column
    private Long tel;

    @Column
    private String indicacion;

    @Column
    private String alias;

    @Column
    private String direccion;

    @Column
    private Double lat;

    @Column
    private Double lng;

    @Column
    private String nombre;

    @Column
    private String interior;

    @JoinColumn(name = "usuario_id")
    @ManyToOne
    private Usuario usuario;

}
