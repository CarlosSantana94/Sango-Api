package sango.bucapps.api.v2.Models.Entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecoleccionEntregaV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    private CarritoV2 carrito;

    private LocalDate fechaRecoleccionOriginal;
    private LocalDate fechaRecoleccionNueva;
    private LocalDate fechaEntregaOriginal;
    private LocalDate fechaEntregaNueva;

    @Column(length = 500)
    private String comentario;

    private boolean esReagendado;
}
