package sango.bucapps.api.Models.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "repartidor")
public class Repartidor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idRepartidor;

    private Timestamp fecha;

    private Double lat;

    private Double lng;

    private Double speed;

    private Double accuracy;
}
