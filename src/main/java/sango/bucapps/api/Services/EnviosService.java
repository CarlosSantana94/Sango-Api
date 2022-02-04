package sango.bucapps.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.Models.DTO.EnviosDto;
import sango.bucapps.api.Models.Entity.Carrito;
import sango.bucapps.api.Models.Entity.Envios;
import sango.bucapps.api.Repositorys.CarritoRepository;
import sango.bucapps.api.Repositorys.EnviosRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class EnviosService {

    @Autowired
    private EnviosRepository enviosRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    public List<EnviosDto> obtenerEnviosDisponibles(java.util.Date date) {
        List<EnviosDto> listadoDisponible = new ArrayList<>();
        int diasDisponibles = 20;

        java.util.Date hoy = new java.util.Date();

        if (date != null) {
            hoy = new Date(date.getTime());
        }

        Calendar c = Calendar.getInstance();

        c.setTime(hoy);
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);


        while (diasDisponibles > 0) {
            EnviosDto enviosDto = new EnviosDto();


            //  Solo puede haber 20 por dia
            java.sql.Date sqlDate = new java.sql.Date(c.getTimeInMillis());
            enviosDto.setFecha(sqlDate);
            Long disponibles = 20L
                    - enviosRepository.countAllByFechaEntrega(sqlDate)
                    - enviosRepository.countAllByFechaRecoleccion(sqlDate);
            enviosDto.setDisponibles(disponibles);

            if (disponibles > 0) {
                listadoDisponible.add(enviosDto);
                diasDisponibles--;
            }
            c.add(Calendar.DATE, 1);

        }


        return listadoDisponible;
    }


    public Envios guardarEnvioEnCarritoTemporal(Date recoleccion, Date entrega, String idUsuario) {
        Carrito carrito = carritoRepository.getAllByUsuarioIdAndEstado(idUsuario, "Nuevo");
        Envios envios = enviosRepository.getAllByCarritoId(carrito.getId());
        if (envios == null) {
            envios = new Envios();
        }
        envios.setFechaRecoleccion(recoleccion);
        envios.setFechaEntrega(entrega);
        envios.setCarrito(carrito);
        envios.setFechaCreado(new Date(new java.util.Date().getTime()));

        enviosRepository.save(envios);

        return envios;
    }
}
