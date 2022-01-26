package sango.bucapps.api.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sango.bucapps.api.Models.DTO.OpcionesPrendaDto;
import sango.bucapps.api.Models.DTO.SubOpcionesPrendaDto;
import sango.bucapps.api.Services.ProductosService;

import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
public class ProductosController {

    @Autowired
    private ProductosService productosService;

    @GetMapping(value = "opciones/{servicioId}")
    private List<OpcionesPrendaDto> obtenerTodasLasOpcionesPorServicio(@PathVariable("servicioId") Long servicioId) {

        return productosService.obtenerTodasLasOpcionesPorServicio(servicioId);
    }

    @GetMapping(value = "subOpciones/{opcionId}")
    private List<SubOpcionesPrendaDto> obtenerTodasLasSubOpcionesPorPrenda(@PathVariable("opcionId") Long opcionId) {
        return productosService.obtenerTodasLasSubOpcionesPorPrenda(opcionId);
    }
}
