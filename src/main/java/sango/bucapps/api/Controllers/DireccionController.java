package sango.bucapps.api.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sango.bucapps.api.Models.DTO.DireccionDto;
import sango.bucapps.api.Models.Entity.Direccion;
import sango.bucapps.api.Services.DireccionService;

import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
public class DireccionController {
    @Autowired
    private DireccionService direccionService;

    @PostMapping(value = "/direccion")
    public DireccionDto guardarDireccion(@RequestBody DireccionDto direccion,
                                      @RequestHeader("idUsuario") String idUsuario){

        return direccionService.guardarDireccion(direccion,idUsuario);
    }

    @GetMapping(value = "/direccion/lista")
    public List<Direccion> obtenerTodasLasDirecciones(@RequestHeader("idUsuario") String idUsuario){
        return direccionService.obtenerTodasLasDirecciones(idUsuario);
    }

}
