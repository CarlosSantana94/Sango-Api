package sango.bucapps.api.Models.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServicioConProductoYSubProductosDTO {

    private Long servicioId;
    private String servicioNombre;
    private Long opcionId;
    private String opcionNombre;
    private Long subopcionId;
    private String subopcionNombre;
    private String opcionImg;
    private BigDecimal subopcionPrecio;
    private String subopcionDescripcion;
    private String subopcionImg;
    private Boolean subopcionPorMetro;

    // Constructor
    public ServicioConProductoYSubProductosDTO(Long servicioId, String servicioNombre,
                                               Long opcionId, String opcionNombre, Long subopcionId,
                                               String subopcionNombre, String opcionImg,
                                               BigDecimal subopcionPrecio, String subopcionDescripcion,
                                               String subopcionImg, Boolean subopcionPorMetro) {
        this.servicioId = servicioId;
        this.servicioNombre = servicioNombre;
        this.opcionId = opcionId;
        this.opcionNombre = opcionNombre;
        this.subopcionId = subopcionId;
        this.subopcionNombre = subopcionNombre;
        this.opcionImg = opcionImg;
        this.subopcionPrecio = subopcionPrecio;
        this.subopcionDescripcion = subopcionDescripcion;
        this.subopcionImg = subopcionImg;
        this.subopcionPorMetro = subopcionPorMetro;
    }

}

