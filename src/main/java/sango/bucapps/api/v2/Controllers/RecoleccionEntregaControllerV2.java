package sango.bucapps.api.v2.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sango.bucapps.api.Repositorys.CarritoRepository;
import sango.bucapps.api.v2.Models.Entities.CarritoV2;
import sango.bucapps.api.v2.Models.Entities.RecoleccionEntregaV2;
import sango.bucapps.api.v2.Repositories.CarritoRepositoryV2;
import sango.bucapps.api.v2.Services.RecoleccionEntregaServiceV2;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/recoleccion-entrega")
public class RecoleccionEntregaControllerV2 {

    @Autowired
    private RecoleccionEntregaServiceV2 recoleccionEntregaService;

    @Autowired
    private CarritoRepositoryV2 carritoRepository;

    // Obtener los días disponibles para recolección
    @GetMapping("/recoleccion/disponibilidad")
    public ResponseEntity<List<LocalDate>> obtenerDisponibilidadRecoleccion(@RequestParam(defaultValue = "5") int dias) {
        List<LocalDate> diasDisponibles = recoleccionEntregaService.obtenerDiasDisponiblesRecoleccion(dias);
        return ResponseEntity.ok(diasDisponibles);
    }

    // Reagendar una recolección o entrega
    @PutMapping("/reagendar")
    public ResponseEntity<RecoleccionEntregaV2> reagendar(@RequestParam Long recoleccionId,
                                                          @RequestParam LocalDate nuevaFechaRecoleccion,
                                                          @RequestParam LocalDate nuevaFechaEntrega,
                                                          @RequestParam String comentario) {
        // Obtener la recolección/entrega original
        RecoleccionEntregaV2 recoleccionEntrega = recoleccionEntregaService.findById(recoleccionId);
        if (recoleccionEntrega == null) {
            return ResponseEntity.notFound().build(); // No se encontró la recolección
        }

        // Actualizar las fechas y el comentario
        recoleccionEntrega.setFechaRecoleccionNueva(nuevaFechaRecoleccion);
        recoleccionEntrega.setFechaEntregaNueva(nuevaFechaEntrega);
        recoleccionEntrega.setComentario(comentario);
        recoleccionEntrega.setEsReagendado(true);

        // Guardar los cambios en la base de datos
        RecoleccionEntregaV2 updatedRecoleccion = recoleccionEntregaService.actualizarRecoleccion(recoleccionEntrega);

        return ResponseEntity.ok(updatedRecoleccion);
    }

    @PostMapping("/agendar")
    public ResponseEntity<RecoleccionEntregaV2> agendarRecoleccion(@RequestParam Long carritoId, @RequestParam LocalDate fechaRecoleccion) {
        // Verificar disponibilidad
        if (!recoleccionEntregaService.hayDisponibilidad(fechaRecoleccion)) {
            return ResponseEntity.badRequest().body(null); // No hay disponibilidad
        }

        // Buscar el carrito
        CarritoV2 carrito = carritoRepository.findById(carritoId).orElse(null);
        if (carrito == null) {
            return ResponseEntity.notFound().build(); // Carrito no encontrado
        }

        // Crear y guardar la recolección vinculada al carrito
        LocalDate fechaEntrega = recoleccionEntregaService.obtenerFechaEntrega(fechaRecoleccion);
        RecoleccionEntregaV2 recoleccionEntrega = new RecoleccionEntregaV2();
        recoleccionEntrega.setCarrito(carrito); // Vincular el carrito
        recoleccionEntrega.setFechaRecoleccionOriginal(fechaRecoleccion);
        recoleccionEntrega.setFechaEntregaOriginal(fechaEntrega);

        RecoleccionEntregaV2 savedRecoleccion = recoleccionEntregaService.guardarRecoleccion(recoleccionEntrega);

        return ResponseEntity.ok(savedRecoleccion);
    }

}
