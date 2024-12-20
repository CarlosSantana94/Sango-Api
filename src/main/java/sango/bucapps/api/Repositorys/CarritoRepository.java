package sango.bucapps.api.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sango.bucapps.api.Models.Entity.Carrito;

import javax.transaction.Transactional;
import java.util.Date;
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


    @Query(value = "select count(*)\n" +
            "from carrito_sub_opciones_prendas\n" +
            "where carrito_id = :carritoId\n" +
            "  and sub_opciones_prendas_id = :prendaporKiloId ", nativeQuery = true)
    Long contarCuantasPrendasPorKiloHay(@Param("carritoId") Long carritoId, @Param("prendaporKiloId") Long prendaPorKiloId);

    List<Carrito> getAllByUsuarioId(String idUsuario);

    @Query(value = "select *\n" +
            "from carrito\n" +
            "where (estado = 'Creada' or estado = 'Terminada')\n" +
            "  and id in (select carrito_id from envios where fecha_recoleccion <= :fechaRecoleccion  " +
            "order by fecha_recoleccion)", nativeQuery = true)
    List<Carrito> obtenerCarritosNoNuevos(@Param("fechaRecoleccion") Date fechaRecoleccion);


    @Query(value = "select *\n" +
            "from carrito\n" +
            "where (estado = 'Creada' or estado = 'Terminada')\n" +
            "  and id in (select carrito_id from envios where fecha_recoleccion >= :fechaRecoleccion  " +
            "order by fecha_recoleccion)", nativeQuery = true)
    List<Carrito> obtenerCarritosNoNuevosPendientes(@Param("fechaRecoleccion") Date fechaRecoleccion);


    @Query(value = "select *\n" +
            "from carrito\n" +
            "where (estado = 'Recolectada')", nativeQuery = true)
    List<Carrito> obtenerCarritosRecolectados();

    @Query(value = "select *\n" +
            "from carrito\n" +
            "where (estado = 'Entrega')", nativeQuery = true)
    List<Carrito> obtenerCarritosParaEntrega();


    @Query(value = "select *\n" +
            "from carrito\n" +
            "where (estado = 'Creada' or estado = 'Entrega')\n" +
            "  and id in (select carrito_id from envios where fecha_recoleccion <= :fechaRecoleccion order by fecha_recoleccion)\n"
            , nativeQuery = true)
    List<Carrito> obtenerCarritosRepartidor(@Param("fechaRecoleccion") Date fechaRecoleccion);

    @Query(value = "select * from carrito where estado = :estado", nativeQuery = true)
    List<Carrito> getAllByEstado(@Param("estado") String estado);

    @Modifying
    @Transactional
    @Query(value = "delete from carrito where usuario_id = :id and creado is null;",nativeQuery = true)
    int deleteErrorCarritosUser(@Param("id") String id);
}
