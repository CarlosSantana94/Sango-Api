package sango.bucapps.api.v2.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sango.bucapps.api.v2.Models.Entities.UsuarioV2;

@Repository
public interface UsuarioRepositoryV2 extends JpaRepository<UsuarioV2, String> {
}
