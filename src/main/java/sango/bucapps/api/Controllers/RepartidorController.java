package sango.bucapps.api.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import sango.bucapps.api.Models.Entity.Repartidor;
import sango.bucapps.api.Services.RepartidorService;

@Controller
public class RepartidorController {

    @Autowired
    private RepartidorService repartidorService;

    @PostMapping(value = "repartidor/ping", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Repartidor> guardarUbicacionRepartidor(@RequestBody Repartidor repartidor) {
        return ResponseEntity.ok(repartidorService.guardarUbicacionRepartidor(repartidor));
    }
}
