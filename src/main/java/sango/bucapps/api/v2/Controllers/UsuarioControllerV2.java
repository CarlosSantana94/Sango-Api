package sango.bucapps.api.v2.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sango.bucapps.api.v2.Models.Entities.UsuarioV2;
import sango.bucapps.api.v2.Services.UsuarioServiceV2;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v2/usuarios")
@CrossOrigin(maxAge = 3600)
public class UsuarioControllerV2 {

    @Autowired
    UsuarioServiceV2 usuarioService;

    // Crear nuevo usuario
    @PostMapping
    public ResponseEntity<UsuarioV2> crearUsuario(@RequestBody UsuarioV2 usuario) {
        UsuarioV2 nuevoUsuario = usuarioService.crearUsuario(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioV2> obtenerUsuarioPorId(@PathVariable String id) {
        Optional<UsuarioV2> usuario = usuarioService.obtenerUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioV2>> obtenerTodosLosUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodosLosUsuarios());
    }

    // Actualizar usuario por ID
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioV2> actualizarUsuario(@PathVariable String id, @RequestBody UsuarioV2 usuarioActualizado) {
        UsuarioV2 usuario = usuarioService.actualizarUsuario(id, usuarioActualizado);
        return ResponseEntity.ok(usuario);
    }

    // Eliminar usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
