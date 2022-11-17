package sango.bucapps.api.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sango.bucapps.api.Models.Entity.Repartidor;

@Repository
public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {
}
