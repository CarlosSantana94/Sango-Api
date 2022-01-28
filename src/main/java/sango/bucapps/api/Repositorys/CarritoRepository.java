package sango.bucapps.api.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sango.bucapps.api.Models.Entity.Carrito;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    Carrito getAllByUsuarioIdAndEstado(String usuario, String estado);

    @Query(value = "select count(*) from carrito_sub_opciones_prendas " +
            "where carrito_id = :carritoId " +
            "and sub_opciones_prendas_id = :subPrendaId", nativeQuery = true)
    Long obtenerCantidadDePrendaEnCarrito(@Param("carritoId") Long carritoId,
                                          @Param("subPrendaId") Long subPrendaId);

    @Query(value = "insert into carrito_sub_opciones_prendas(carrito_id, sub_opciones_prendas_id) " +
            "values (:carritoId,:subPrendaId);", nativeQuery = true)
    void insertarPrendaEnCarrito(@Param("carritoId") Long carritoId,
                                 @Param("subPrendaId") Long subPrendaId);

}
