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
        Direccion direccion;

        // Si el DTO tiene un ID, buscar la dirección existente para actualizar
        if (dto.getId() != null) {
            direccion = direccionRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Dirección no encontrada con ID: " + dto.getId()));
        } else {
            // Si no tiene ID, crear una nueva instancia
            direccion = new Direccion();
        }

        // Mapear los campos del DTO a la entidad
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

        // Guardar la entidad en la base de datos
        direccion = direccionRepository.save(direccion);

        // Mapear nuevamente a DTO si es necesario
        dto.setId(direccion.getId()); // Si es una nueva dirección, establecer el ID generado
        return dto;
    }


    public List<Direccion> obtenerTodasLasDirecciones(String idUsuario) {

        return direccionRepository.getAllByUsuarioIdAndHabilitada(idUsuario, true);
    }

    public Direccion obtenerDireccionPorId(Long id) {
        return direccionRepository.findById(id).orElse(null);
    }

    public Direccion deshabilitarDireccion(Long direccionId) {

        Direccion direccion = direccionRepository.findById(direccionId).orElse(null);

        direccion.setHabilitada(Boolean.FALSE);

        return direccionRepository.save(direccion);
    }
}
