package sango.bucapps.api.v2.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sango.bucapps.api.tickets.SangoTicket;
import sango.bucapps.api.v2.Models.Entities.CarritoV2;

import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    SangoTicket sangoTicket;

    @GetMapping("/print")
    public ResponseEntity imprimir(@RequestParam Long carritoId) {
        sangoTicket.printTicket(carritoId);
        return ResponseEntity.ok(null);
    }
}
