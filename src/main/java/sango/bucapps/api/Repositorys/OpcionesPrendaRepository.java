package sango.bucapps.api.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import sango.bucapps.api.Models.Entity.OpcionesPrenda;

import java.util.List;

public interface OpcionesPrendaRepository extends JpaRepository<OpcionesPrenda, Long> {

    List<OpcionesPrenda> getAllByServicioIdOrderByNombre(Long servicioId);
}
