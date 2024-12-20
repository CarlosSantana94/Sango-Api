package sango.bucapps.api.v2.Models.Dtos;

import lombok.Data;
import sango.bucapps.api.Models.Entity.Direccion;
import sango.bucapps.api.Models.Entity.Envios;
import sango.bucapps.api.v2.Models.Enums.EstadoCarrito;

import java.util.List;

@Data
public class ResumenCarrito {

    private List<DetalleCarrito> detalles;
    private double total;
    private Long id;
    private int totalPrendas;
    private Envios envios;
    private Direccion direccion;
    private EstadoCarrito estadoCarrito;


    public ResumenCarrito(List<DetalleCarrito> detalles, double total, Long id, int totalPrendas, Envios envios, Direccion direccion, EstadoCarrito estadoCarrito) {
        this.detalles = detalles;
        this.total = total;
        this.id = id;
        this.totalPrendas = totalPrendas;
        this.envios = envios;
        this.direccion = direccion;
        this.estadoCarrito = estadoCarrito;
    }
}
