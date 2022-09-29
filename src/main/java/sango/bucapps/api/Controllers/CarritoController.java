package sango.bucapps.api.Controllers;

import io.conekta.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sango.bucapps.api.Models.DTO.CarritoDto;
import sango.bucapps.api.Models.DTO.ListaDePrendasDTO;
import sango.bucapps.api.Models.DTO.MsgRespuestaDto;
import sango.bucapps.api.Models.DTO.ResumenCarritoDto;
import sango.bucapps.api.Models.Entity.ConfirmacionPrendas;
import sango.bucapps.api.Services.CarritoService;

import java.sql.Date;
import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping(value = "/carrito", produces = "application/json")
    public CarritoDto obtenerCarritoNuevo(@RequestHeader("idUsuario") String idUsuario) {
        return carritoService.obtenerCarritoNuevo(idUsuario);
    }

    @PostMapping(value = "/carrito/{agregar}/{subPrendaId}", produces = "application/json")
    public CarritoDto actualizarCarrito(@RequestHeader("idUsuario") String idUsuario,
                                        @PathVariable("subPrendaId") Long subPrendaId,
                                        @PathVariable("agregar") Long agregar
    ) {

        return carritoService.actualizarCarrito(idUsuario, subPrendaId, agregar);
    }

    @PostMapping(value = "/carrito/direccion/{direccionId}", produces = "application/json")
    public CarritoDto actualizarDireccionEnCarrito(@RequestHeader("idUsuario") String idUsuario,
                                                   @PathVariable("direccionId") Long direccionId
    ) {
        return carritoService.actualizarDireccionEnCarrito(idUsuario, direccionId);
    }

    @GetMapping(value = "/carrito/resumen", produces = "application/json")
    public ResumenCarritoDto obtenerResumenDeCarrito(@RequestHeader("idUsuario") String idUsuario) {
        return carritoService.obtenerResumenDeCarrito(idUsuario, null);
    }

    @PostMapping(value = "/carrito/pagar/{metodo}/{cuandoOToken}", produces = "application/json")
    public MsgRespuestaDto pagarCarrito(@RequestHeader("idUsuario") String idUsuario,
                                        @PathVariable("metodo") String metodo,
                                        @PathVariable("cuandoOToken") String cuandoOToken
    ) throws Error {
        return carritoService.pagarCarrito(idUsuario, metodo, cuandoOToken);
    }

    @GetMapping(value = "/carrito/pedidos", produces = "application/json")
    public List<ResumenCarritoDto> obtenerPedidos(@RequestHeader("idUsuario") String idUsuario) {
        return carritoService.obtenerPedidos(idUsuario);

    }

    @GetMapping(value = "/carrito/pedidos/repartidor/actuales/{fechaRecoleccion}", produces = "application/json")
    public List<ResumenCarritoDto> obtenerPedidosPorFechaRepartidor(@PathVariable(name = "fechaRecoleccion") Date fechaRecoleccion) {
        return carritoService.obtenerPedidosPorFechaRepartidor(fechaRecoleccion);

    }

    @GetMapping(value = "/carrito/pedidos/repartidor/pendientes/{fechaRecoleccion}", produces = "application/json")
    public List<ResumenCarritoDto> obtenerPedidosPorFechaRepartidorPendientes(@PathVariable(name = "fechaRecoleccion") Date fechaRecoleccion) {
        return carritoService.obtenerPedidosPorFechaRepartidorPendientes(fechaRecoleccion);

    }

    @GetMapping(value = "/carrito/id/{idCarrito}", produces = "application/json")
    public ResumenCarritoDto obtenerResumenCarritoById(@RequestHeader("idUsuario") String idUsuario,
                                                       @PathVariable("idCarrito") Long idCarrito) {

        return carritoService.obtenerResumenDeCarrito(idUsuario, idCarrito);
    }


    @PostMapping(value = "/prendasConfirmar/{idCarrito}", produces = "application/json")
    public List<ListaDePrendasDTO> confirmarPrendas(@PathVariable("idCarrito") Long idCarrito,
                                                    @RequestBody List<ListaDePrendasDTO> listaDePrendasDTOS) {

        return carritoService.confirmarPrendas(idCarrito, listaDePrendasDTOS);
    }

    @PostMapping(value = "/prendaConfirmar/reg/{reg}/{registrada}", produces = "application/json")
    public ConfirmacionPrendas confirmarPrendas(@PathVariable("reg") Long reg,
                                                @PathVariable("registrada") Boolean registrada) {

        return carritoService.cambiarRegistroValor(reg, registrada);
    }

    @PostMapping(value = "/cambiarEstadoCarrito/{estado}/{idCarrito}", produces = "application/json")
    public CarritoDto cambiarEstadoCarrito(@PathVariable("estado") String estado,
                                           @PathVariable("idCarrito") Long idCarrito) {

        return carritoService.cambiarEstadoCarrito(estado, idCarrito);
    }
}
