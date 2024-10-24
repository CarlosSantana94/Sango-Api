package sango.bucapps.api.v2.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sango.bucapps.api.v2.Models.Entities.CarritoV2;
import sango.bucapps.api.v2.Models.Entities.UsuarioV2;
import sango.bucapps.api.v2.Models.Enums.EstadoCarrito;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoRepositoryV2 extends JpaRepository<CarritoV2, Long> {
    List<CarritoV2> findByUsuarioId(String usuarioId);
    List<CarritoV2> findByEstado(EstadoCarrito estado);
    Optional<CarritoV2> findByUsuarioAndEstado(UsuarioV2 usuario, EstadoCarrito estadoCarrito);
}
