package sango.bucapps.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.Models.DTO.RepartidorDto;
import sango.bucapps.api.Models.Entity.Repartidor;
import sango.bucapps.api.Repositorys.RepartidorRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@Service
public class RepartidorService {

    @Autowired
    private RepartidorRepository repartidorRepository;

    public Repartidor guardarUbicacionRepartidor(Repartidor repartidor) {
        return repartidorRepository.save(repartidor);
    }

    public List<RepartidorDto> obtenerRutaRepartidorPorDia(Date fechaRecoleccion) {
        List<RepartidorDto> markers = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        c.setTime(fechaRecoleccion);
        c.add(Calendar.DATE, 1);

        for (Repartidor rep : repartidorRepository.getAllByFechaOrderById(fechaRecoleccion, c.getTime())) {
            RepartidorDto mark = new RepartidorDto();

            mark.setLng(rep.getLng());
            mark.setLat(rep.getLat());
            mark.setAccuracy(rep.getAccuracy());

            mark.setSpeed(rep.getSpeed());
            mark.setId(rep.getId());
            mark.setIdRepartidor(rep.getIdRepartidor());

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("GMT-6"));
            calendar.setTimeInMillis(rep.getFecha().getTime());
            calendar.add(Calendar.HOUR, -6);

            mark.setFecha(calendar.getTime());

            markers.add(mark);
        }


        return markers;
    }

}
