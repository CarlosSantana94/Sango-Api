package sango.bucapps.api.v2.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.Models.Entity.SubOpcionesPrenda;
import sango.bucapps.api.Repositorys.SubOpcionesPrendaRepository;
import sango.bucapps.api.v2.Models.Dtos.DetalleCarrito;
import sango.bucapps.api.v2.Models.Dtos.ResumenCarrito;
import sango.bucapps.api.v2.Models.Entities.CarritoItemV2;
import sango.bucapps.api.v2.Models.Entities.CarritoV2;
import sango.bucapps.api.v2.Models.Entities.UsuarioV2;
import sango.bucapps.api.v2.Models.Enums.EstadoCarrito;
import sango.bucapps.api.v2.Repositories.CarritoItemRepositoryV2;
import sango.bucapps.api.v2.Repositories.CarritoRepositoryV2;
import sango.bucapps.api.v2.Repositories.UsuarioRepositoryV2;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarritoServiceV2 {

    @Autowired
    private CarritoRepositoryV2 carritoRepository;

    @Autowired
    private UsuarioRepositoryV2 usuarioRepository;

    @Autowired
    SubOpcionesPrendaRepository subOpcionPrendaRepository;

    @Autowired
    CarritoItemRepositoryV2 carritoItemRepository;

    public CarritoV2 crearCarrito(CarritoV2 carrito, String usuarioId) {
        UsuarioV2 usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        carrito.setUsuario(usuario);
        carrito.setEstado(EstadoCarrito.NUEVO);
        return carritoRepository.save(carrito);
    }

    public List<CarritoV2> obtenerCarritosPorUsuario(String usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
    }

    public CarritoV2 actualizarEstado(Long carritoId, EstadoCarrito nuevoEstado) {
        CarritoV2 carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        carrito.setEstado(nuevoEstado);
        return carritoRepository.save(carrito);
    }

    public List<CarritoV2> obtenerCarritosPorEstado(EstadoCarrito estado) {
        return carritoRepository.findByEstado(estado);
    }

    // Guardar el carrito al actualizarlo
    public CarritoV2 guardarCarrito(CarritoV2 carrito) {
        return carritoRepository.save(carrito);
    }

    public Optional<CarritoV2> obtenerCarritoPorId(Long carritoId) {
        return carritoRepository.findById(carritoId);
    }

    // Crear o recuperar el carrito activo del usuario
    public CarritoV2 obtenerCarritoActivo(String usuarioId) {
        UsuarioV2 usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Optional<CarritoV2> carritoOpt = carritoRepository.findByUsuarioAndEstado(usuario, EstadoCarrito.NUEVO);
        if (carritoOpt.isPresent()) {
            return carritoOpt.get();
        }

        CarritoV2 nuevoCarrito = new CarritoV2();
        nuevoCarrito.setUsuario(usuario);
        nuevoCarrito.setEstado(EstadoCarrito.NUEVO);
        return carritoRepository.save(nuevoCarrito);
    }

    // Método para añadir una prenda al carrito con validación de cantidad
    public CarritoV2 añadirPrendaAlCarrito(String usuarioId, Long prendaId, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        CarritoV2 carrito = obtenerCarritoActivo(usuarioId);
        SubOpcionesPrenda prenda = subOpcionPrendaRepository.findById(prendaId)
                .orElseThrow(() -> new RuntimeException("Prenda no encontrada"));

        Optional<CarritoItemV2> itemExistente = carrito.getItems().stream()
                .filter(item -> item.getPrenda().getId().equals(prendaId))
                .findFirst();

        if (itemExistente.isPresent()) {
            CarritoItemV2 item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            carritoItemRepository.save(item);
        } else {
            CarritoItemV2 nuevoItem = new CarritoItemV2();
            nuevoItem.setPrenda(prenda);
            nuevoItem.setCarrito(carrito);
            nuevoItem.setCantidad(cantidad);
            carrito.getItems().add(nuevoItem);
            carritoItemRepository.save(nuevoItem);
        }

        return carritoRepository.save(carrito);
    }

    // Actualizar la cantidad de una prenda en el carrito
    public CarritoItemV2 actualizarCantidadPrenda(Long carritoId, Long prendaId, int nuevaCantidad) {
        CarritoV2 carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        CarritoItemV2 item = carrito.getItems().stream()
                .filter(i -> i.getPrenda().getId().equals(prendaId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Prenda no encontrada en el carrito"));

        if (nuevaCantidad > 0) {
            item.setCantidad(nuevaCantidad);
            return carritoItemRepository.save(item);
        } else {
            eliminarPrendaDelCarrito(carritoId, prendaId);
            return null; // Se eliminó la prenda
        }
    }

    // Eliminar una prenda del carrito
    public void eliminarPrendaDelCarrito(Long carritoId, Long prendaId) {
        CarritoV2 carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        CarritoItemV2 item = carrito.getItems().stream()
                .filter(i -> i.getPrenda().getId().equals(prendaId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Prenda no encontrada en el carrito"));

        carrito.getItems().remove(item);
        carritoItemRepository.delete(item);
        carritoRepository.save(carrito);
    }

    // Obtener el resumen del carrito
    public ResumenCarrito obtenerResumenCarrito(String usuarioId) {
        CarritoV2 carrito = obtenerCarritoActivo(usuarioId);
        List<DetalleCarrito> detalles = carrito.getItems().stream()
                .map(item -> new DetalleCarrito(item.getPrenda().getNombre(),
                        item.getCantidad(),
                        item.getPrenda().getPrecio(),
                        item.getCantidad() * item.getPrenda().getPrecio(),item.getPrenda().getId()))
                .collect(Collectors.toList());

        double total = detalles.stream().mapToDouble(DetalleCarrito::getSubtotal).sum();

        return new ResumenCarrito(detalles, total);
    }
}

