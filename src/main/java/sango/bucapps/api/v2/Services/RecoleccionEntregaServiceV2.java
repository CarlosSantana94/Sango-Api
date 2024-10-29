package sango.bucapps.api.v2.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.v2.Models.Entities.RecoleccionEntregaV2;
import sango.bucapps.api.v2.Repositories.RecoleccionEntregaRepositoryV2;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecoleccionEntregaServiceV2 {
    @Autowired
    private RecoleccionEntregaRepositoryV2 recoleccionEntregaRepository;

    // Guardar recolección/entrega en la base de datos
    public RecoleccionEntregaV2 guardarRecoleccion(RecoleccionEntregaV2 recoleccionEntrega) {
        return recoleccionEntregaRepository.save(recoleccionEntrega);
    }

    // Buscar una recolección/entrega por ID
    public RecoleccionEntregaV2 findById(Long id) {
        return recoleccionEntregaRepository.findById(id).orElse(null);
    }

    // Actualizar una recolección/entrega (reagendar)
    public RecoleccionEntregaV2 actualizarRecoleccion(RecoleccionEntregaV2 recoleccionEntrega) {
        return recoleccionEntregaRepository.save(recoleccionEntrega);
    }

    private static final List<LocalDate> DIAS_FESTIVOS_MEXICO = List.of(
            LocalDate.of(2024, 1, 1),  // Año Nuevo
            LocalDate.of(2024, 2, 5)// Día de la Constitución
            // Agrega más días festivos aquí
    );

    // Simulamos una base de datos para las fechas ocupadas
    private final List<LocalDate> fechasOcupadas = new ArrayList<>();

    // Validar si un día es hábil (excluye fines de semana y días festivos)
    private boolean esDiaHabil(LocalDate fecha) {
        return !(fecha.getDayOfWeek() == DayOfWeek.SATURDAY ||
                fecha.getDayOfWeek() == DayOfWeek.SUNDAY ||
                DIAS_FESTIVOS_MEXICO.contains(fecha));
    }

    // Obtener la próxima fecha hábil
    public LocalDate obtenerProximaFechaHabil(LocalDate fechaInicial) {
        LocalDate fecha = fechaInicial.plusDays(1); // Mínimo a partir de mañana
        while (!esDiaHabil(fecha)) {
            fecha = fecha.plusDays(1);
        }
        return fecha;
    }

    // Verificar disponibilidad para una fecha (máximo 20 servicios por día)
    public boolean hayDisponibilidad(LocalDate fecha) {
        long conteoServicios = fechasOcupadas.stream().filter(f -> f.equals(fecha)).count();
        return conteoServicios < 20;
    }

    // Obtener los próximos días hábiles con disponibilidad para recolección
    public List<LocalDate> obtenerDiasDisponiblesRecoleccion(int dias) {
        List<LocalDate> diasDisponibles = new ArrayList<>();
        LocalDate fecha = LocalDate.now();

        while (diasDisponibles.size() < dias) {
            fecha = obtenerProximaFechaHabil(fecha);
            if (hayDisponibilidad(fecha)) {
                diasDisponibles.add(fecha);
            }
        }

        return diasDisponibles;
    }

    // Obtener la fecha de entrega (3 días hábiles después de la recolección)
    public LocalDate obtenerFechaEntrega(LocalDate fechaRecoleccion) {
        LocalDate fechaEntrega = fechaRecoleccion;
        for (int i = 0; i < 3; i++) {
            fechaEntrega = obtenerProximaFechaHabil(fechaEntrega);
        }
        return fechaEntrega;
    }
}
