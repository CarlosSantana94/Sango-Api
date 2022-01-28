package sango.bucapps.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.Models.DTO.OpcionesPrendaDto;
import sango.bucapps.api.Models.DTO.SubOpcionesPrendaDto;
import sango.bucapps.api.Models.Entity.OpcionesPrenda;
import sango.bucapps.api.Models.Entity.SubOpcionesPrenda;
import sango.bucapps.api.Repositorys.CarritoRepository;
import sango.bucapps.api.Repositorys.OpcionesPrendaRepository;
import sango.bucapps.api.Repositorys.SubOpcionesPrendaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductosService {

    @Autowired
    private OpcionesPrendaRepository opcionesPrendaRepository;

    @Autowired
    private SubOpcionesPrendaRepository subOpcionesPrendaRepository;

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
}
