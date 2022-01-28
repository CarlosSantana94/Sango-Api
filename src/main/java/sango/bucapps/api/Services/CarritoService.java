package sango.bucapps.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.Models.DTO.CarritoDto;
import sango.bucapps.api.Models.Entity.Carrito;
import sango.bucapps.api.Repositorys.CarritoRepository;

@Service
public class CarritoService {
    @Autowired
    private CarritoRepository carritoRepository;

    public CarritoDto actualizarCarrito(String idUsuario, Long subOpcionesPrendaId) {
        CarritoDto carritoDto = obtenerCarritoNuevo(idUsuario);

        carritoRepository.insertarPrendaEnCarrito(carritoDto.getId(),subOpcionesPrendaId);

        return carritoDto;
    }

    public Long obtenerCantidadPrenda(Long subPrendaId) {

        return 0L;
    }

    public CarritoDto obtenerCarritoNuevo(String idUsuario) {
        CarritoDto carritoDto = new CarritoDto();

        Carrito carrito = carritoRepository.getAllByUsuarioIdAndEstado(idUsuario, "Nuevo");

        if (carrito != null) {
            carritoDto.setId(carrito.getId());
        }


        return carritoDto;
    }
}
