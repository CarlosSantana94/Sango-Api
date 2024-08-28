package sango.bucapps.api.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import sango.bucapps.api.Models.DTO.ServicioConProductoYSubProductosDTO;
import sango.bucapps.api.Models.DTO.ServicioConProductoYSubProductosProjection;
import sango.bucapps.api.Models.Entity.Servicio;

import javax.persistence.Entity;
import javax.transaction.Transactional;
import java.util.List;

public interface ServiciosRepository extends JpaRepository<Servicio, Long> {

    @Query(value = "SELECT " +
            "    s.id AS servicioId, " +
            "    s.nombre AS servicioNombre, " +
            "    op.id AS opcionId, " +
            "    op.nombre AS opcionNombre, " +
            "    subop.id AS subopcionId, " +
            "    subop.nombre AS subopcionNombre, " +
            "    op.img AS opcionImg, " +
            "    subop.precio AS subopcionPrecio, " +
            "    subop.descripcion AS subopcionDescripcion, " +
            "    subop.img AS subopcionImg, " +
            "    subop.por_metro AS subopcionPorMetro " +
            "FROM servicio s " +
            "JOIN opciones_prenda op ON op.servicio_id = s.id " +
            "LEFT JOIN sub_opciones_prenda subop ON subop.opcion_id = op.id " +
            "WHERE subop.nombre IS NOT NULL AND op.nombre IS NOT NULL " +
            "ORDER BY s.id, op.nombre, subop.nombre", nativeQuery = true)
    List<ServicioConProductoYSubProductosProjection> encontrarTodo();

}
