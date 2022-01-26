package sango.bucapps.api.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import sango.bucapps.api.Models.Entity.SubOpcionesPrenda;

import java.util.List;

public interface SubOpcionesPrendaRepository extends JpaRepository<SubOpcionesPrenda, Long> {

    List<SubOpcionesPrenda> getAllByOpcionesPrendaIdOrderByNombre(Long opcionesPrendaId);

}
