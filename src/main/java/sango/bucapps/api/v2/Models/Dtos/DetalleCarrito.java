package sango.bucapps.api.v2.Models.Dtos;

import lombok.Data;

@Data
public class DetalleCarrito {

    private String nombrePrenda;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private Long id;
    private String nombreCategoria;
    private Long idCategoria;
    private String Servicio;
    private Long idServicio;
    private String imgPrenda;

    public DetalleCarrito(String nombrePrenda, int cantidad, double precioUnitario, double subtotal, Long id, String nombreCategoria, Long idCategoria, String servicio, Long idServicio, String imgPrenda) {
        this.nombrePrenda = nombrePrenda;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.id = id;
        this.nombreCategoria = nombreCategoria;
        this.idCategoria = idCategoria;
        Servicio = servicio;
        this.idServicio = idServicio;
        this.imgPrenda = imgPrenda;
    }
}
