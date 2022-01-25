package sango.bucapps.api.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import sango.bucapps.api.Models.Entity.Servicio;

public interface ServiciosRepository extends JpaRepository<Servicio, Long> {
}
