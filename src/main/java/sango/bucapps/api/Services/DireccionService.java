package sango.bucapps.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.Models.DTO.DireccionDto;
import sango.bucapps.api.Models.Entity.Direccion;
import sango.bucapps.api.Repositorys.DireccionRepository;
import sango.bucapps.api.Repositorys.UsuarioRepository;

import java.util.List;

@Service
public class DireccionService {
    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public DireccionDto guardarDireccion(DireccionDto dto, String idUsuario) {

        Direccion direccion = new Direccion();
        direccion.setCp(dto.getCp());
        direccion.setTel(dto.getTel());
        direccion.setIndicacion(dto.getIndicacion());
        direccion.setAlias(dto.getAlias());
        direccion.setDireccion(dto.getDireccion());
        direccion.setLat(dto.getLat());
        direccion.setLng(dto.getLng());
        direccion.setNombre(dto.getNombre());
        direccion.setInterior(dto.getInterior());

        direccion.setUsuario(usuarioRepository.getById(idUsuario));

        direccionRepository.save(direccion);
        return dto;
    }

    public List<Direccion> obtenerTodasLasDirecciones(String idUsuario) {

        return direccionRepository.getAllByUsuarioId(idUsuario);
    }
}
