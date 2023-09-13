package sango.bucapps.api.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sango.bucapps.api.Models.DTO.RepartidorDto;
import sango.bucapps.api.Models.Entity.ComentarioChoferes;
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

    @PostMapping(value = "repartidor/comentario", produces = "application/json")
    @ResponseBody
    public ResponseEntity<ComentarioChoferes> nuevoComentarioChofer(@RequestBody ComentarioChoferes comentarioChoferes) {
        return ResponseEntity.ok(repartidorService.nuevoComentarioChofer(comentarioChoferes));
    }


    @GetMapping(value = "repartidor/comentario", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<ComentarioChoferes>> obtenerComentarios() {
        return ResponseEntity.ok(repartidorService.obtenerComentarios());
    }

    @GetMapping(value = "repartidor/comentario/existente/{idCarrito}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<ComentarioChoferes> obtenerComentarioExistentePorCarritoId
            (@PathVariable(name = "idCarrito") Long idCarrito) {
        return ResponseEntity
                .ok(repartidorService.obtenerComentarioExistentePorCarritoId(idCarrito));
    }
}
