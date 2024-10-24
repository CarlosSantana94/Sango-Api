package sango.bucapps.api.Models.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "opciones_prenda")
public class OpcionesPrenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @Column
    private String img;

    @JoinColumn(name = "servicio_id")
    @ManyToOne
    private Servicio servicio;
}
