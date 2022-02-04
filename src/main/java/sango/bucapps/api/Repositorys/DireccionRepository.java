package sango.bucapps.api.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import sango.bucapps.api.Models.Entity.Direccion;

import java.util.List;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    List<Direccion> getAllByUsuarioId(String idUsuario);
}
