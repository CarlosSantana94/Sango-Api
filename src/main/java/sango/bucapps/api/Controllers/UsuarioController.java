package sango.bucapps.api.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sango.bucapps.api.Models.DTO.MsgRespuestaDto;
import sango.bucapps.api.Models.Entity.Usuario;
import sango.bucapps.api.Services.UsuarioService;

@RestController
@CrossOrigin(maxAge = 3600)
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;


    @PostMapping(value = "/usuario")
    @ResponseBody
    private MsgRespuestaDto crearOActualizarUsuario(@RequestBody Usuario usuario){
        return usuarioService.crearOActualizarUsuario(usuario);
    }

}