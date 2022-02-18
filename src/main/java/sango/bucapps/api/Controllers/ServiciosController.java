package sango.bucapps.api.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sango.bucapps.api.Models.Entity.Servicio;
import sango.bucapps.api.Services.ServiciosService;

import java.util.List;


@RestController
@CrossOrigin(maxAge = 3600)
public class ServiciosController {
    @Autowired
    private ServiciosService serviciosService;

    @GetMapping(value = "servicios", produces = "application/json")
    @ResponseBody
    public List<Servicio> obtenerServicios() {
        return serviciosService.obtenerServicios();
    }

}
