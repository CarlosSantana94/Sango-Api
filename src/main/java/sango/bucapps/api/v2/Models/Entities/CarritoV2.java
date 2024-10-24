package sango.bucapps.api.v2.Models.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import sango.bucapps.api.Models.Entity.SubOpcionesPrenda;
import sango.bucapps.api.v2.Models.Enums.EstadoCarrito;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "carrito_v2")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarritoV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioV2 usuario;

    @Enumerated(EnumType.STRING)
    private EstadoCarrito estado;

    private Date fechaCreacion;

    private Date fechaRecoleccion;

    private Date fechaEntrega;

    // Relación con CarritoItemV2 (ítems del carrito)
    @JsonManagedReference
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoItemV2> items;

}

