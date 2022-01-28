package sango.bucapps.api.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sango.bucapps.api.Models.DTO.CarritoDto;
import sango.bucapps.api.Models.DTO.SubOpcionesPrendaDto;
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

    @PutMapping(value = "/carrito/{subPrendaId}")
    public CarritoDto actualizarCarrito(@RequestHeader("idUsuario") String idUsuario,
                                        @PathVariable("subPrendaId") Long subPrendaId
    ) {

        return carritoService.actualizarCarrito(idUsuario,subPrendaId);
    }
}
