package sango.bucapps.api.v2.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sango.bucapps.api.v2.Models.Entities.CarritoItemV2;

@Repository
public interface CarritoItemRepositoryV2 extends JpaRepository<CarritoItemV2, Long> {
}
