package sango.bucapps.api.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import sango.bucapps.api.Models.Entity.ConfirmacionPrendas;

public interface ConfirmacionPrendasRepository extends JpaRepository<ConfirmacionPrendas, Long> {


    ConfirmacionPrendas getAllBySubOpcionesPrendaIdAndIdCarrito(Long id, Long idCarrito);

    ConfirmacionPrendas getAllByReg(Long reg);
}
