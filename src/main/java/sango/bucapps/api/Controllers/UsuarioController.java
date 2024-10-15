package sango.bucapps.api.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sango.bucapps.api.Models.DTO.MsgRespuestaDto;
import sango.bucapps.api.Models.Entity.Usuario;
import sango.bucapps.api.Services.UsuarioService;

import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;


    @PostMapping(value = "/usuario")
    @ResponseBody
    private MsgRespuestaDto crearOActualizarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.crearOActualizarUsuario(usuario);
    }

    @GetMapping(value = "/usuarios")
    @ResponseBody
    private List<Usuario> obtenerUsuarios() {
        return usuarioService.obtenerUsuarios();
    }

    @GetMapping(value = "/usuario/{id}")
    @ResponseBody
    private Usuario obtenerUsuario(@PathVariable String id) {
        System.out.println(id);
        return usuarioService.obtenerUsuario(id);
    }

    @DeleteMapping(value = "/usuario/{id}")
    @ResponseBody
    private MsgRespuestaDto eliminarUsuarios(@PathVariable String id) {
        return usuarioService.eliminarUsuarios(id);
    }


}
