package sango.bucapps.api.Models.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DesgloseTodosLosCarritos {
    private Long entregarHoy;
    private Long entregarAtrasados;
    private Long entregarFuturos;
    private Long recolectarAtrasados;
    private Long recolectarHoy;
    private Long recolectarFuturos;
    private Long canceladosHistorico;
    private Long solicitudCancelacion;
    private Long finalizados;
    private Long enTienda;

    public DesgloseTodosLosCarritos() {
        entregarHoy = 0L;
        entregarAtrasados = 0L;
        entregarFuturos = 0L;
        recolectarAtrasados = 0L;
        recolectarHoy = 0L;
        recolectarFuturos = 0L;
        canceladosHistorico = 0L;
        solicitudCancelacion = 0L;
        finalizados = 0L;
        enTienda = 0L;
    }

}
