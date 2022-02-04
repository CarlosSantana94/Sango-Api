package sango.bucapps.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.Models.DTO.CarritoDto;
import sango.bucapps.api.Models.Entity.Carrito;
import sango.bucapps.api.Models.Entity.SubOpcionesPrenda;
import sango.bucapps.api.Repositorys.CarritoRepository;
import sango.bucapps.api.Repositorys.DireccionRepository;

@Service
public class CarritoService {
    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    public CarritoDto actualizarCarrito(String idUsuario, Long subOpcionesPrendaId, Long agregar) {
        CarritoDto carritoDto = obtenerCarritoNuevo(idUsuario);
        if (agregar == 1) {
            //Agregar prenda
            carritoRepository.insertarPrendaEnCarrito(carritoDto.getId(), subOpcionesPrendaId);
        } else if (agregar == 0) {
            // Quitar Prenda
            carritoRepository.borrarPrendaEnCarrito(carritoDto.getId(), subOpcionesPrendaId);
        }

        return carritoDto;
    }


    public CarritoDto obtenerCarritoNuevo(String idUsuario) {
        CarritoDto carritoDto = new CarritoDto();
        carritoDto.setCantidad(0L);

        Carrito carrito = carritoRepository.getAllByUsuarioIdAndEstado(idUsuario, "Nuevo");

        if (carrito != null) {
            carritoDto.setId(carrito.getId());
            double total = 0D;
            for (SubOpcionesPrenda sub : carrito.getSubOpcionesPrendas()) {
                total = Math.round((total + sub.getPrecio()) * 100D) / 100D;
                Long cantidad = carritoDto.getCantidad() + 1;
                carritoDto.setCantidad(cantidad);
            }
            carritoDto.setTotal(total);
            carrito.setTotal(total);
            carritoRepository.save(carrito);
        }


        return carritoDto;
    }

    public CarritoDto actualizarDireccionEnCarrito(String idUsuario, Long direccionId) {
        Carrito carrito = carritoRepository.getAllByUsuarioIdAndEstado(idUsuario, "Nuevo");
        carrito.setDireccion(direccionRepository.getById(direccionId));
        carritoRepository.save(carrito);

        return obtenerCarritoNuevo(idUsuario);
    }
}
