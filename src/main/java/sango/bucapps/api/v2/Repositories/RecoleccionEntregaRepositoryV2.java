package sango.bucapps.api.v2.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sango.bucapps.api.v2.Models.Entities.RecoleccionEntregaV2;

import java.time.LocalDate;
import java.util.List;

public interface RecoleccionEntregaRepositoryV2 extends JpaRepository<RecoleccionEntregaV2, Long> {

    // Buscar recolecciones por fecha de recolección
    List<RecoleccionEntregaV2> findByFechaRecoleccionOriginal(LocalDate fechaRecoleccionOriginal);

    // Buscar entregas por fecha de entrega original
    List<RecoleccionEntregaV2> findByFechaEntregaOriginal(LocalDate fechaEntregaOriginal);

    // Buscar recolecciones reagendadas
    List<RecoleccionEntregaV2> findByEsReagendado(boolean esReagendado);

}