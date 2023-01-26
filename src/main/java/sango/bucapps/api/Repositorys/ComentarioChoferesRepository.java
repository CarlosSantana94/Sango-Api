package sango.bucapps.api.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sango.bucapps.api.Models.Entity.ComentarioChoferes;

@Repository
public interface ComentarioChoferesRepository extends JpaRepository<ComentarioChoferes, Long> {

    ComentarioChoferes getAllByIdCarrito(Long idCarrito);
}
