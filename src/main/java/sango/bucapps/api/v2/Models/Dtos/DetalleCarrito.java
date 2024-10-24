package sango.bucapps.api.v2.Models.Dtos;

import lombok.Data;

@Data
public class DetalleCarrito {

    private String nombrePrenda;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private Long id;

    public DetalleCarrito(String nombrePrenda, int cantidad, double precioUnitario, double subtotal, Long id) {
        this.nombrePrenda = nombrePrenda;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.id = id;
    }

}
