package sango.bucapps.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.Models.DTO.EnviosDto;
import sango.bucapps.api.Models.Entity.Envios;
import sango.bucapps.api.Repositorys.CarritoRepository;
import sango.bucapps.api.Repositorys.EnviosRepository;
import sango.bucapps.api.v2.Models.Entities.CarritoV2;
import sango.bucapps.api.v2.Repositories.CarritoRepositoryV2;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class EnviosService {

    @Autowired
    private EnviosRepository enviosRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoRepositoryV2 carritoRepositoryV2;

    public static final List<LocalDate> DIAS_FESTIVOS = List.of(
            LocalDate.of(2024, 1, 1),    // Año Nuevo
            LocalDate.of(2024, 2, 5),    // Día de la Constitución
            LocalDate.of(2024, 3, 18),   // Natalicio de Benito Juárez
            LocalDate.of(2024, 5, 1),    // Día del Trabajo
            LocalDate.of(2024, 9, 16),   // Día de la Independencia
            LocalDate.of(2024, 11, 2),   // Día de Muertos
            LocalDate.of(2024, 11, 20),  // Revolución Mexicana
            LocalDate.of(2024, 12, 25)   // Navidad
    );


    public List<EnviosDto> obtenerEnviosDisponibles(java.util.Date fechaInicial) {
        List<EnviosDto> listadoDisponible = new ArrayList<>();
        int diasDisponibles = 20;

        // Fecha base para empezar el conteo (hoy o fecha proporcionada)
        LocalDate fechaBase = fechaInicial != null
                ? new java.sql.Date(fechaInicial.getTime()).toLocalDate()
                : LocalDate.now();

        // Avanzar 3 días naturales para empezar la búsqueda
        fechaBase = fechaBase.plusDays(2);

        // Generar los próximos días disponibles
        while (diasDisponibles > 0) {
            // Saltar fines de semana
            if (fechaBase.getDayOfWeek() != DayOfWeek.SUNDAY && !DIAS_FESTIVOS.contains(fechaBase)) {
                EnviosDto enviosDto = new EnviosDto();
                enviosDto.setFecha(java.sql.Date.valueOf(fechaBase));

                // Consultar la cantidad de envíos disponibles (máximo 20 por día)
                long recolecciones = enviosRepository.countAllByFechaRecoleccion(java.sql.Date.valueOf(fechaBase));
                long entregas = enviosRepository.countAllByFechaEntrega(java.sql.Date.valueOf(fechaBase));
                long disponibles = 20 - (recolecciones + entregas);

                enviosDto.setDisponibles(disponibles);

                // Agregar solo si hay disponibilidad
                if (disponibles > 0) {
                    listadoDisponible.add(enviosDto);
                    diasDisponibles--;
                }
            }
            // Avanzar al siguiente día
            fechaBase = fechaBase.plusDays(1);
        }

        return listadoDisponible;
    }


    public Envios guardarEnvioEnCarritoTemporal(Date recoleccion, Date entrega, Long carritoId) {
        //   Carrito carrito = carritoRepository.getAllByUsuarioIdAndEstado(idUsuario, "Nuevo");

        CarritoV2 carritoV2 = carritoRepositoryV2.getById(carritoId);

        Envios envios = enviosRepository.getByCarritoV2Id(carritoId);
        if (envios == null) {
            envios = new Envios();
        }
        envios.setFechaRecoleccion(recoleccion);
        envios.setFechaEntrega(entrega);
        envios.setCarritoV2(carritoV2);
        envios.setFechaOriginalEntrega(entrega);
        envios.setFechaOriginalRecoleccion(recoleccion);
        envios.setFechaCreado(new Date(new java.util.Date().getTime()));

        Envios enviosSaved = enviosRepository.save(envios);

        carritoV2.setEnvios(enviosSaved);
        carritoRepositoryV2.save(carritoV2);

        return envios;
    }

    public Envios guardarEnvioEnCarritoCreado(Date fechaRecoleccion, Date fechaEntrega, Long carritoId, String motivo) {

        CarritoV2 carritoV2 = carritoRepositoryV2.getById(carritoId);

        Envios envios = enviosRepository.getByCarritoV2Id(carritoId);
        if (envios == null) {
            envios = new Envios();
        }
        envios.setFechaRecoleccion(fechaRecoleccion);
        envios.setFechaEntrega(fechaEntrega);
        envios.setCarritoV2(carritoV2);
        envios.setMotivoModificacion(motivo);
        envios.setFechaModificado(new Date(new java.util.Date().getTime()));

        Envios enviosSaved = enviosRepository.save(envios);

        carritoV2.setEnvios(enviosSaved);
        carritoRepositoryV2.save(carritoV2);

        return envios;
    }
}
