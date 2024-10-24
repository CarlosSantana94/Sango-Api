package sango.bucapps.api.v2.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.v2.Models.Entities.CarritoV2;
import sango.bucapps.api.v2.Models.Entities.UsuarioV2;
import sango.bucapps.api.v2.Models.Enums.EstadoCarrito;
import sango.bucapps.api.v2.Repositories.CarritoRepositoryV2;
import sango.bucapps.api.v2.Repositories.UsuarioRepositoryV2;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceV2 {

    @Autowired
    UsuarioRepositoryV2 usuarioRepository;

    @Autowired
    CarritoRepositoryV2 carritoRepositoryV2;

    // Crear usuario o retornarlo si ya existe
    public UsuarioV2 crearUsuario(UsuarioV2 usuario) {
        // Verificar si el usuario ya existe
        Optional<UsuarioV2> usuarioExistente = obtenerUsuarioPorId(usuario.getId());

        if (usuarioExistente.isPresent()) {
            UsuarioV2 usuarioEncontrado = usuarioExistente.get();

            // Verificar si el usuario ya tiene un carrito NUEVO
            Optional<CarritoV2> carritoNuevo = carritoRepositoryV2.findByUsuarioAndEstado(usuarioEncontrado, EstadoCarrito.NUEVO);

            if (carritoNuevo.isEmpty()) {
                // Crear carrito NUEVO si no tiene
                CarritoV2 nuevoCarrito = new CarritoV2();
                nuevoCarrito.setUsuario(usuarioEncontrado);
                nuevoCarrito.setEstado(EstadoCarrito.NUEVO);
                carritoRepositoryV2.save(nuevoCarrito);
            }
            // Retornar el usuario junto con su carrito NUEVO
            usuarioEncontrado.setCarritos(null);
            return usuarioEncontrado;
        } else {
            // Crear el nuevo usuario
            UsuarioV2 nuevoUsuario = usuarioRepository.save(usuario);

            // Crear un carrito NUEVO para el nuevo usuario
            CarritoV2 nuevoCarrito = new CarritoV2();
            nuevoCarrito.setUsuario(nuevoUsuario);
            nuevoCarrito.setEstado(EstadoCarrito.NUEVO);
            nuevoCarrito.setFechaCreacion(new Date());
            carritoRepositoryV2.save(nuevoCarrito);

            return nuevoUsuario;
        }
    }

    // Obtener usuario por ID
    public Optional<UsuarioV2> obtenerUsuarioPorId(String id) {
        return usuarioRepository.findById(id);
    }

    // Obtener todos los usuarios
    public List<UsuarioV2> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    // Actualizar usuario
    public UsuarioV2 actualizarUsuario(String id, UsuarioV2 usuarioActualizado) {
        UsuarioV2 usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar los campos del usuario existente
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setImg(usuarioActualizado.getImg());
        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setPassword(usuarioActualizado.getPassword());
        usuarioExistente.setTel(usuarioActualizado.getTel());
        usuarioExistente.setToken(usuarioActualizado.getToken());
        usuarioExistente.setPuedePagarConCC(usuarioActualizado.isPuedePagarConCC());

        return usuarioRepository.save(usuarioExistente);
    }

    // Eliminar usuario
    public void eliminarUsuario(String id) {
        usuarioRepository.deleteById(id);
    }
}
