package sango.bucapps.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.Models.Entity.Repartidor;
import sango.bucapps.api.Repositorys.RepartidorRepository;

@Service
public class RepartidorService {

    @Autowired
    private RepartidorRepository repartidorRepository;

    public Repartidor guardarUbicacionRepartidor(Repartidor repartidor) {
        return repartidorRepository.save(repartidor);
    }
}
