package sango.bucapps.api.v2.Controllers;

import io.conekta.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sango.bucapps.api.Models.DTO.MsgRespuestaDto;
import sango.bucapps.api.Models.Entity.Usuario;
import sango.bucapps.api.v2.Models.Dtos.ResumenCarrito;
import sango.bucapps.api.v2.Models.Entities.CarritoItemV2;
import sango.bucapps.api.v2.Models.Entities.CarritoV2;
import sango.bucapps.api.v2.Models.Enums.EstadoCarrito;
import sango.bucapps.api.v2.Services.CarritoServiceV2;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/carritos")
public class CarritoControllerV2 {

    private final CarritoServiceV2 carritoService;

    public CarritoControllerV2(CarritoServiceV2 carritoService) {
        this.carritoService = carritoService;
    }

    @PostMapping("/{usuarioId}")
    public ResponseEntity<CarritoV2> crearCarrito(@RequestBody CarritoV2 carrito, @PathVariable String usuarioId) {
        return ResponseEntity.ok(carritoService.crearCarrito(carrito, usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CarritoV2>> obtenerCarritosPorUsuario(@PathVariable String usuarioId) {
        return ResponseEntity.ok(carritoService.obtenerCarritosPorUsuario(usuarioId));
    }

    @PutMapping("/{carritoId}/estado")
    public ResponseEntity<CarritoV2> actualizarEstado(@PathVariable Long carritoId, @RequestParam EstadoCarrito estado) {
        return ResponseEntity.ok(carritoService.actualizarEstado(carritoId, estado));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<CarritoV2>> obtenerCarritosPorEstado(@PathVariable EstadoCarrito estado) {
        return ResponseEntity.ok(carritoService.obtenerCarritosPorEstado(estado));
    }

    @GetMapping("/{carritoId}")
    public ResponseEntity<ResumenCarrito> obtenerCarritoPorId(@PathVariable Long carritoId) {
        ResumenCarrito carrito = carritoService.obtenerResumenCarritoPorId(carritoId);
        return ResponseEntity.ok(carrito);
    }

    // Obtener el carrito activo
    @GetMapping("nuevo/{usuarioId}")
    public ResponseEntity<CarritoV2> obtenerCarritoActivo(@PathVariable String usuarioId) {
        CarritoV2 carrito = carritoService.obtenerCarritoActivo(usuarioId);
        return ResponseEntity.ok(carrito);
    }

    // Añadir prenda al carrito con una cantidad
    @PostMapping("/{usuarioId}/anadir-prenda/{prendaId}")
    public ResponseEntity<CarritoV2> añadirPrendaAlCarrito(@PathVariable String usuarioId, @PathVariable Long prendaId,
                                                           @RequestParam int cantidad) {
        CarritoV2 carritoActualizado = carritoService.añadirPrendaAlCarrito(usuarioId, prendaId, cantidad);
        return ResponseEntity.ok(carritoActualizado);
    }

    // Actualizar cantidad de una prenda en el carrito
    @PostMapping("/{carritoId}/actualizar-cantidad/{prendaId}")
    public ResponseEntity<CarritoItemV2> actualizarCantidadPrenda(@PathVariable Long carritoId, @PathVariable Long prendaId,
                                                                  @RequestParam int cantidad) {
        CarritoItemV2 itemActualizado = carritoService.actualizarCantidadPrenda(carritoId, prendaId, cantidad);
        return itemActualizado != null ? ResponseEntity.ok(itemActualizado) : ResponseEntity.noContent().build();
    }

    // Eliminar prenda del carrito
    @DeleteMapping("/{carritoId}/eliminar-prenda/{prendaId}")
    public ResponseEntity<Void> eliminarPrendaDelCarrito(@PathVariable Long carritoId, @PathVariable Long prendaId) {
        carritoService.eliminarPrendaDelCarrito(carritoId, prendaId);
        return ResponseEntity.noContent().build();
    }

    // Obtener el resumen del carrito
    @GetMapping("/{usuarioId}/resumen")
    public ResponseEntity<ResumenCarrito> obtenerResumenCarrito(@PathVariable String usuarioId) {
        ResumenCarrito resumen = carritoService.obtenerResumenCarrito(usuarioId);
        return ResponseEntity.ok(resumen);
    }

    // Asignar dirección a un carrito
    @PostMapping("/{carritoId}/direccion/{direccionId}")
    public ResponseEntity<CarritoV2> asignarDireccion(@PathVariable Long carritoId, @PathVariable Long direccionId) {
        CarritoV2 carritoActualizado = carritoService.asignarDireccion(carritoId, direccionId);
        if (carritoActualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carritoActualizado);
    }


    @PostMapping(value = "/carrito/pagar/{metodo}/{cuandoOToken}", produces = "application/json")
    public MsgRespuestaDto pagarCarritoV2(@RequestHeader("idUsuario") String idUsuario,
                                          @PathVariable("metodo") String metodo,
                                          @PathVariable("cuandoOToken") String cuandoOToken,
                                          @RequestParam String email
    ) throws Error {
        return carritoService.pagarCarrito(idUsuario, metodo, cuandoOToken, email);
    }

    // Endpoint para obtener carritos agrupados por estado (excluyendo "NUEVO")
    @GetMapping("/agrupados-por-estado")
    public ResponseEntity<Map<EstadoCarrito, Map<String, Object>>> obtenerCarritosAgrupadosPorEstado() {
        Map<EstadoCarrito, Map<String, Object>> carritosAgrupados = carritoService.obtenerCarritosAgrupadosPorEstado();
        return ResponseEntity.ok(carritosAgrupados);
    }

    @PostMapping("/{id}/imprimir")
    public ResponseEntity<String> actualizarImprimir(@PathVariable("id") Long carritoId) {
        carritoService.actualizarImprimir(carritoId);
        return ResponseEntity.ok("El campo 'imprimir' ha sido actualizado a false para el carrito con id: " + carritoId);
    }

    @PutMapping("/item/{idPrenda}/comentario")
    public ResponseEntity<CarritoItemV2> actualizarComentarioPrenda(@PathVariable Long idPrenda, @RequestParam String comentario) {
        try {
            CarritoItemV2 itemActualizado = carritoService.actualizarComentario(idPrenda, comentario);
            return ResponseEntity.ok(itemActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
