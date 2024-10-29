package sango.bucapps.api.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sango.bucapps.api.Models.DTO.EnviosDto;
import sango.bucapps.api.Models.Entity.Envios;
import sango.bucapps.api.Services.EnviosService;

import java.sql.Date;
import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
public class EnviosController {

    @Autowired
    private EnviosService enviosService;


    @GetMapping(value = "/envios")
    public List<EnviosDto> obtenerEnviosDisponibles() {
        System.out.println();
        return enviosService.obtenerEnviosDisponibles(null);
    }

    @GetMapping(value = "/envios/{fechaRecoleccion}")
    public List<EnviosDto> obtenerEnviosDisponiblesParaRecoger(@PathVariable(name = "fechaRecoleccion") Date fechaRecoleccion) {
        System.out.println(fechaRecoleccion);
        return enviosService.obtenerEnviosDisponibles(fechaRecoleccion);
    }

    @PostMapping(value = "/envios/{fechaRecoleccion}/{fechaEntrega}")
    public Envios guardarEnvioEnCarritoTemporal(@PathVariable(name = "fechaRecoleccion") Date fechaRecoleccion,
                                                @PathVariable(name = "fechaEntrega") Date fechaEntrega,
                                                @RequestParam Long carritoId) {

        return enviosService.guardarEnvioEnCarritoTemporal(fechaRecoleccion, fechaEntrega, carritoId);

    }

    @PostMapping(value = "/envios/pedidoCreado/{fechaRecoleccion}/{fechaEntrega}/{carritoId}")
    public ResponseEntity<Envios> guardarEnvioEnCarritoCreado(@PathVariable(name = "fechaRecoleccion") Date fechaRecoleccion,
                                                              @PathVariable(name = "fechaEntrega") Date fechaEntrega,
                                                              @PathVariable(name = "carritoId") Long carritoId,
                                                              @RequestBody EnviosDto enviosDto) {

        return ResponseEntity
                .ok(enviosService.guardarEnvioEnCarritoCreado(fechaRecoleccion, fechaEntrega, carritoId, enviosDto.getMotivo()));

    }
}
