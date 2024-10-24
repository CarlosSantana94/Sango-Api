package sango.bucapps.api.v2.Models.Entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuario_v2")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioV2 {

    @Id
    private String id;

    @Column(nullable = false)
    private String email;

    private String img;
    private String nombre;
    private String password;
    private Long tel;
    private String token;

    @Column(name = "puede_pagar_concc", nullable = false)
    private boolean puedePagarConCC;

    @OneToMany(mappedBy = "usuario")
    private List<CarritoV2> carritos;

    // Getters y Setters
}