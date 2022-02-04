package sango.bucapps.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.Models.DTO.CarritoDto;
import sango.bucapps.api.Models.DTO.ResumenCarritoDto;
import sango.bucapps.api.Models.DTO.SubOpcionesPrendaDto;
import sango.bucapps.api.Models.Entity.Carrito;
import sango.bucapps.api.Models.Entity.Envios;
import sango.bucapps.api.Models.Entity.SubOpcionesPrenda;
import sango.bucapps.api.Repositorys.CarritoRepository;
import sango.bucapps.api.Repositorys.DireccionRepository;
import sango.bucapps.api.Repositorys.EnviosRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {
    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private EnviosRepository enviosRepository;

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

    public ResumenCarritoDto obtenerResumenDeCarrito(String idUsuario) {
        Carrito carrito = carritoRepository.getAllByUsuarioIdAndEstado(idUsuario, "Nuevo");
        Envios envios = enviosRepository.getAllByCarritoId(carrito.getId());

        ResumenCarritoDto resumen = new ResumenCarritoDto();
        resumen.setEntrega(envios.getFechaEntrega());
        resumen.setRecoleccion(envios.getFechaRecoleccion());
        if (carrito.getDireccion() != null) {
            resumen.setDireccion(carrito.getDireccion().getDireccion());
            resumen.setCp(carrito.getDireccion().getCp());
            resumen.setNombre(carrito.getDireccion().getNombre());
            resumen.setTel(carrito.getDireccion().getTel());
        }
        int cantidadPrendas = 0;

        List<SubOpcionesPrendaDto> subDtoList = new ArrayList<>();

        for (SubOpcionesPrenda sub : carrito.getSubOpcionesPrendas()) {

            Optional<SubOpcionesPrendaDto> subDto = subDtoList.stream().filter(s -> s.getId().equals(sub.getId())).findAny();

            if (subDto.isPresent()) {
                subDto.get().setCantidad(subDto.get().getCantidad() + 1);
                subDto.get().setPrecioTotal(sub.getPrecio() * subDto.get().getCantidad());
            } else {
                SubOpcionesPrendaDto subDtoNew = new SubOpcionesPrendaDto();
                subDtoNew.setId(sub.getId());
                subDtoNew.setNombre(sub.getNombre());
                subDtoNew.setPrecio(sub.getPrecio());
                subDtoNew.setPrecioTotal(sub.getPrecio());
                subDtoNew.setImg(sub.getImg());
                subDtoNew.setCantidad(1L);

                subDtoList.add(subDtoNew);
            }

        }
        resumen.setTotal(carrito.getTotal());
        resumen.setCantidadPrendas(carrito.getSubOpcionesPrendas().size());
        resumen.setPrendasList(subDtoList);


        return resumen;
    }
}
