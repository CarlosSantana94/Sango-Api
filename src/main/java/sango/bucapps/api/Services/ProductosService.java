package sango.bucapps.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.Models.DTO.OpcionesPrendaDto;
import sango.bucapps.api.Models.DTO.SubOpcionesPrendaDto;
import sango.bucapps.api.Models.Entity.OpcionesPrenda;
import sango.bucapps.api.Models.Entity.Servicio;
import sango.bucapps.api.Models.Entity.SubOpcionesPrenda;
import sango.bucapps.api.Repositorys.CarritoRepository;
import sango.bucapps.api.Repositorys.OpcionesPrendaRepository;
import sango.bucapps.api.Repositorys.ServiciosRepository;
import sango.bucapps.api.Repositorys.SubOpcionesPrendaRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public List<SubOpcionesPrendaDto> obtenerTodosLosProductos() {
        List<SubOpcionesPrendaDto> respuesta = new ArrayList<>();

        for (Servicio s : serviciosRepository.findAll()) {
            for (OpcionesPrendaDto op : obtenerTodasLasOpcionesPorServicio(s.getId())) {
                for (SubOpcionesPrenda opc : subOpcionesPrendaRepository.getAllByOpcionesPrendaIdOrderByNombre(op.getId())) {
                    SubOpcionesPrendaDto dto = new SubOpcionesPrendaDto();
                    dto.setId(opc.getId());
                    dto.setNombre(opc.getNombre());
                    dto.setPrecio(opc.getPrecio());
                    dto.setDescripcion(opc.getDescripcion());
                    dto.setImg(opc.getImg());
                    dto.setServicioPadre(op.getNombre());
                    dto.setServicio(s.getNombre());

                    respuesta.add(dto);
                }
            }
        }

        Collections.sort(respuesta, Comparator.comparing(SubOpcionesPrendaDto::getNombre));

        return respuesta;
    }

    public SubOpcionesPrendaDto crearNuevaSubOpcion(SubOpcionesPrendaDto dto) {
        SubOpcionesPrenda sub = new SubOpcionesPrenda();

        if (dto.getNombre().contains(")")) {
            dto.setNombre(dto.getNombre().split("\\)")[1].trim());
        }

        sub.setNombre(dto.getNombre().trim());
        sub.setPrecio(dto.getPrecio());
        sub.setDescripcion(dto.getDescripcion().trim());
        sub.setImg(dto.getImg());
        sub.setPorMetro(dto.getPorMetro());
        sub.setOpcionesPrenda(opcionesPrendaRepository.getById(dto.getOpcionId()));
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
}
