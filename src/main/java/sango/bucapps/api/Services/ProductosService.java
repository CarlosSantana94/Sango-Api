package sango.bucapps.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.Models.DTO.*;
import sango.bucapps.api.Models.Entity.OpcionesPrenda;
import sango.bucapps.api.Models.Entity.Servicio;
import sango.bucapps.api.Models.Entity.SubOpcionesPrenda;
import sango.bucapps.api.Repositorys.CarritoRepository;
import sango.bucapps.api.Repositorys.OpcionesPrendaRepository;
import sango.bucapps.api.Repositorys.ServiciosRepository;
import sango.bucapps.api.Repositorys.SubOpcionesPrendaRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
public class ProductosService {

    @Autowired
    private OpcionesPrendaRepository opcionesPrendaRepository;

    @Autowired
    private SubOpcionesPrendaRepository subOpcionesPrendaRepository;

    @Autowired
    private ServiciosRepository serviciosRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    public List<OpcionesPrendaDto> obtenerTodasLasOpcionesPorServicio(Long servicioId) {
        List<OpcionesPrendaDto> opcionesPrendas = new ArrayList<>();

        for (OpcionesPrenda opc : opcionesPrendaRepository.getAllByServicioIdOrderByNombre(servicioId)) {
            OpcionesPrendaDto dto = new OpcionesPrendaDto();
            dto.setId(opc.getId());
            dto.setNombre(opc.getNombre());
            dto.setImg(opc.getImg());

            opcionesPrendas.add(dto);
        }

        return opcionesPrendas;

    }

    public List<SubOpcionesPrendaDto> obtenerTodasLasSubOpcionesPorPrenda(Long opcionPrendaId, String idUsuario) {
        List<SubOpcionesPrendaDto> subOpcionesPrendas = new ArrayList<>();

        for (SubOpcionesPrenda opc : subOpcionesPrendaRepository.getAllByOpcionesPrendaIdOrderByNombre(opcionPrendaId)) {
            SubOpcionesPrendaDto dto = new SubOpcionesPrendaDto();
            dto.setId(opc.getId());
            dto.setNombre(opc.getNombre());
            dto.setPrecio(opc.getPrecio());
            dto.setDescripcion(opc.getDescripcion());
            dto.setImg(opc.getImg());


            dto.setCantidad(carritoRepository
                    .obtenerCantidadDePrendaEnCarrito(carritoRepository
                            .getAllByUsuarioIdAndEstado(idUsuario, "Nuevo").getId(), opc.getId()));

            subOpcionesPrendas.add(dto);
        }

        return subOpcionesPrendas;
    }

    public HashMap<String, HashMap<String, HashMap<String, ServicioConProductoYSubProductosDTO>>> obtenerTodosLosProductos() {
        HashMap<String, HashMap<String, HashMap<String, ServicioConProductoYSubProductosDTO>>> serviciosMap = new HashMap<>();

        for (ServicioConProductoYSubProductosProjection proyeccion : serviciosRepository.encontrarTodo()) {
            ServicioConProductoYSubProductosDTO dto = new ServicioConProductoYSubProductosDTO(
                    proyeccion.getServicioId(),
                    proyeccion.getServicioNombre(),
                    proyeccion.getOpcionId(),
                    proyeccion.getOpcionNombre(),
                    proyeccion.getSubopcionId(),
                    proyeccion.getSubopcionNombre(),
                    proyeccion.getOpcionImg(),
                    proyeccion.getSubopcionPrecio(),
                    proyeccion.getSubopcionDescripcion(),
                    proyeccion.getSubopcionImg(),
                    proyeccion.getSubopcionPorMetro()
            );

            // Check if the outer map already contains the servicioId
            serviciosMap.putIfAbsent(proyeccion.getServicioNombre(), new HashMap<>());
            HashMap<String, HashMap<String, ServicioConProductoYSubProductosDTO>> categoriaMap = serviciosMap.get(proyeccion.getServicioNombre());

            // Check if the middle map already contains the opcionId
            categoriaMap.putIfAbsent(proyeccion.getOpcionNombre(), new HashMap<>());
            HashMap<String, ServicioConProductoYSubProductosDTO> subProductoMap = categoriaMap.get(proyeccion.getOpcionNombre());

            // Add or update the subopcionId and its corresponding DTO
            subProductoMap.put(proyeccion.getSubopcionNombre(), dto);
        }


        return serviciosMap;
    }


    public SubOpcionesPrendaDto crearNuevaSubOpcion(SubOpcionesPrendaDto dto) {
        SubOpcionesPrenda sub = new SubOpcionesPrenda();

        if (dto.getNombre().contains(")")) {
            dto.setNombre(dto.getNombre().split("\\)")[1].trim());
        }
        if (dto.getId() != null) {
            sub.setId(dto.getId());
        }

        sub.setNombre(dto.getNombre().trim());
        sub.setPrecio(dto.getPrecio());
        sub.setDescripcion(dto.getDescripcion().trim());
        sub.setImg(dto.getImg());
        sub.setPorMetro(dto.getPorMetro());

        OpcionesPrenda opcionesPrenda = opcionesPrendaRepository.getById(dto.getOpcionId());

        sub.setOpcionesPrenda(opcionesPrenda);

        subOpcionesPrendaRepository.save(sub);

        return dto;
    }


    public OpcionesPrendaDto crearNuevaOpcion(OpcionesPrendaDto dto) {
        OpcionesPrenda opc = new OpcionesPrenda();

        if (dto.getNombre().contains(")")) {
            dto.setNombre(dto.getNombre().split("\\)")[1].trim());
        }


        opc.setNombre(dto.getNombre().trim());
        opc.setImg(dto.getImg());
        opc.setServicio(serviciosRepository.getById(dto.getServicioId()));

        opcionesPrendaRepository.save(opc);

        return dto;
    }

    public SubOpcionesPrendaDto obtenerProductoPorId(Long prodId) {
        SubOpcionesPrenda sub = subOpcionesPrendaRepository.getById(prodId);

        SubOpcionesPrendaDto dto = new SubOpcionesPrendaDto();
        dto.setId(sub.getId());
        dto.setNombre(sub.getNombre());
        dto.setPrecio(sub.getPrecio());
        dto.setServicio(sub.getOpcionesPrenda().getServicio().getNombre());
        dto.setServicioPadre(sub.getOpcionesPrenda().getNombre());
        dto.setDescripcion(sub.getDescripcion());
        dto.setImg(sub.getImg());
        dto.setPorMetro(sub.getPorMetro());
        dto.setOpcionId(sub.getOpcionesPrenda().getServicio().getId());
        dto.setOpcionPadreId(sub.getOpcionesPrenda().getId());
        dto.setServicioPadreImg(sub.getOpcionesPrenda().getImg());

        return dto;
    }
}
