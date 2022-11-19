package sango.bucapps.api.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sango.bucapps.api.Models.Entity.Repartidor;

import java.util.Date;
import java.util.List;

@Repository
public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {

    @Query(value = "select *\n" +
            "from repartidor\n" +
            "where fecha >= :fecha\n" +
            "and fecha < :diaSiguiente\n" +
            "order by id asc", nativeQuery = true)
    List<Repartidor> getAllByFechaOrderById(@Param("fecha") Date fecha, @Param("diaSiguiente") Date diaSiguiente);
}
