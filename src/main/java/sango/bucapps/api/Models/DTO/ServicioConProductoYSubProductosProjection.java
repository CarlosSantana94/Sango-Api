package sango.bucapps.api.Models.DTO;

import java.math.BigDecimal;

public interface ServicioConProductoYSubProductosProjection {
    Long getServicioId();
    String getServicioNombre();
    Long getOpcionId();
    String getOpcionNombre();
    Long getSubopcionId();
    String getSubopcionNombre();
    String getOpcionImg();
    BigDecimal getSubopcionPrecio();
    String getSubopcionDescripcion();
    String getSubopcionImg();
    Boolean getSubopcionPorMetro();
}
