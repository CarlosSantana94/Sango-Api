package sango.bucapps.api.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sango.bucapps.api.Models.Entity.Carrito;

import javax.transaction.Transactional;
import java.util.List;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    Carrito getAllByUsuarioIdAndEstado(String usuario, String estado);

    List<Carrito> getAllByUsuario(String usuarioId);

    @Query(value = "select count(*) from carrito_sub_opciones_prendas " +
            "where carrito_id = :carritoId " +
            "and sub_opciones_prendas_id = :subPrendaId", nativeQuery = true)
    Long obtenerCantidadDePrendaEnCarrito(@Param("carritoId") Long carritoId,
                                          @Param("subPrendaId") Long subPrendaId);

    @Modifying
    @Transactional
    @Query(value = "insert into carrito_sub_opciones_prendas(carrito_id, sub_opciones_prendas_id) " +
            "values (:carritoId,:subPrendaId);", nativeQuery = true)
    int insertarPrendaEnCarrito(@Param("carritoId") Long carritoId,
                                @Param("subPrendaId") Long subPrendaId);




    @Modifying
    @Transactional
    @Query(value = "delete\n" +
            "from carrito_sub_opciones_prendas\n" +
            "where id = (\n" +
            "    select max(id)\n" +
            "    from carrito_sub_opciones_prendas\n" +
            "    where carrito_id = :carritoId\n" +
            "      and sub_opciones_prendas_id = :subPrendaId);", nativeQuery = true)
    int borrarPrendaEnCarrito(@Param("carritoId") Long carritoId,
                              @Param("subPrendaId") Long subPrendaId);

    @Modifying
    @Transactional
    @Query(value = "delete\n" +
            "from carrito_sub_opciones_prendas\n" +
            "where id in (\n" +
            "    select id\n" +
            "    from carrito_sub_opciones_prendas\n" +
            "    where carrito_id = :carritoId\n" +
            "      and sub_opciones_prendas_id = :subPrendaId);", nativeQuery = true)
    int borrarPrendaEnCarritoPorKiloMenorA4(@Param("carritoId") Long carritoId,
                              @Param("subPrendaId") Long subPrendaId);



    List<Carrito> getAllByUsuarioId(String idUsuario);
}
