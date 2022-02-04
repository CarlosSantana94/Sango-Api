package sango.bucapps.api.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sango.bucapps.api.Models.DTO.CarritoDto;
import sango.bucapps.api.Models.DTO.ResumenCarritoDto;
import sango.bucapps.api.Services.CarritoService;

@RestController
@CrossOrigin(maxAge = 3600)
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping(value = "/carrito")
    public CarritoDto obtenerCarritoNuevo(@RequestHeader("idUsuario") String idUsuario) {
        return carritoService.obtenerCarritoNuevo(idUsuario);
    }

    @PostMapping(value = "/carrito/{agregar}/{subPrendaId}")
    public CarritoDto actualizarCarrito(@RequestHeader("idUsuario") String idUsuario,
                                        @PathVariable("subPrendaId") Long subPrendaId,
                                        @PathVariable("agregar") Long agregar
    ) {

        return carritoService.actualizarCarrito(idUsuario,subPrendaId,agregar);
    }

    @PostMapping(value = "/carrito/direccion/{direccionId}")
    public CarritoDto actualizarDireccionEnCarrito(@RequestHeader("idUsuario") String idUsuario,
                                        @PathVariable("direccionId") Long direccionId
    ) {
        return carritoService.actualizarDireccionEnCarrito(idUsuario,direccionId);
    }

    @GetMapping(value = "/carrito/resumen")
    public ResumenCarritoDto obtenerResumenDeCarrito(@RequestHeader("idUsuario") String idUsuario){
        return carritoService.obtenerResumenDeCarrito(idUsuario);
    }


}
