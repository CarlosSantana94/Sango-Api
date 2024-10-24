package sango.bucapps.api.v2.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sango.bucapps.api.v2.Models.Dtos.ResumenCarrito;
import sango.bucapps.api.v2.Models.Entities.CarritoItemV2;
import sango.bucapps.api.v2.Models.Entities.CarritoV2;
import sango.bucapps.api.v2.Models.Enums.EstadoCarrito;
import sango.bucapps.api.v2.Services.CarritoServiceV2;

import java.util.List;
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
    public ResponseEntity<CarritoV2> obtenerCarritoPorId(@PathVariable Long carritoId) {
        Optional<CarritoV2> carrito = carritoService.obtenerCarritoPorId(carritoId);
        return carrito.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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

}
