package sango.bucapps.api.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import sango.bucapps.api.Models.Entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
}
