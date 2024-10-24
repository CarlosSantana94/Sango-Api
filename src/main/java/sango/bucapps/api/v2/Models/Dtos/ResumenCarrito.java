package sango.bucapps.api.v2.Models.Dtos;

import java.util.List;

public class ResumenCarrito {

    private List<DetalleCarrito> detalles;
    private double total;

    public ResumenCarrito(List<DetalleCarrito> detalles, double total) {
        this.detalles = detalles;
        this.total = total;
    }

    public List<DetalleCarrito> getDetalles() {
        return detalles;
    }

    public double getTotal() {
        return total;
    }
}
