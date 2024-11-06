package sango.bucapps.api.v2.Models.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import sango.bucapps.api.Models.Entity.Direccion;
import sango.bucapps.api.Models.Entity.Envios;
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

    @ManyToOne
    @JoinColumn(name = "envios_id")
    private Envios envios;

    @Enumerated(EnumType.STRING)
    private EstadoCarrito estado;

    private Date fechaCreacion;

    private Date fechaRecoleccion;

    private Date fechaEntrega;

    // Relación con CarritoItemV2 (ítems del carrito)
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoItemV2> items;

    @ManyToOne
    @JoinColumn(name = "direccion_id")
    private Direccion direccion;

    private String cuandoOToken;

    private String formaDePago;

    private String ordenConekta;

    @Column(name = "imprimir", columnDefinition = "boolean default false")
    private boolean imprimir = false;
}

