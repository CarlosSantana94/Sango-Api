package sango.bucapps.api.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import sango.bucapps.api.Models.Entity.Envios;

import java.sql.Date;


public interface EnviosRepository extends JpaRepository<Envios, Long> {

    Long countAllByFechaRecoleccion(Date fechaRecoleccion);

    Long countAllByFechaEntrega(Date fechaEntrega);

    Envios getAllByCarritoId(Long carritoId);

}
