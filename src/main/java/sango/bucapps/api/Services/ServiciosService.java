package sango.bucapps.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.Models.Entity.Servicio;
import sango.bucapps.api.Repositorys.ServiciosRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiciosService {
    @Autowired
    private ServiciosRepository serviciosRepository;

    public List<Servicio> obtenerServicios() {
        List<Servicio> serviciosList = new ArrayList<>();

        return serviciosRepository.findAll();
    }
}
