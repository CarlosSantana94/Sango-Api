package sango.bucapps.api.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sango.bucapps.api.Models.DTO.RepartidorDto;
import sango.bucapps.api.Models.Entity.Repartidor;
import sango.bucapps.api.Services.RepartidorService;

import java.sql.Date;
import java.util.List;

@Controller
public class RepartidorController {

    @Autowired
    private RepartidorService repartidorService;

    @PostMapping(value = "repartidor/ping", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Repartidor> guardarUbicacionRepartidor(@RequestBody Repartidor repartidor) {
        return ResponseEntity.ok(repartidorService.guardarUbicacionRepartidor(repartidor));
    }

    @GetMapping(value = "repartidor/dia/{fechaRecoleccion}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<RepartidorDto>> obtenerRutaRepartidorPorDia(@PathVariable(name = "fechaRecoleccion") Date fechaRecoleccion) {
        return ResponseEntity.ok(repartidorService.obtenerRutaRepartidorPorDia(fechaRecoleccion));

    }
}
