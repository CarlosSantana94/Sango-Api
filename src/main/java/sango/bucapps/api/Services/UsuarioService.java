package sango.bucapps.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.Models.DTO.MsgRespuestaDto;
import sango.bucapps.api.Models.Entity.Carrito;
import sango.bucapps.api.Models.Entity.Usuario;
import sango.bucapps.api.Repositorys.CarritoRepository;
import sango.bucapps.api.Repositorys.UsuarioRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    public MsgRespuestaDto crearOActualizarUsuario(Usuario usuario) {
        MsgRespuestaDto msg = new MsgRespuestaDto();

        usuarioRepository.save(usuario);
        msg.setMensaje("Usuario Actualizado o Creado");

        if (carritoRepository.getAllByUsuarioIdAndEstado(usuario.getId(), "Nuevo") == null) {
            //Crear Carrito
            Carrito carrito = new Carrito();
            carrito.setUsuario(usuario);
            carrito.setTotal(0.0D);
            carrito.setEstado("Nuevo");
            carrito.setFormaDePago("");
            carrito.setCreado(new Timestamp(new java.util.Date().getTime()));

            carritoRepository.save(carrito);
        }
        return msg;
    }

    public List<Usuario> obtenerUsuarios() {
       return usuarioRepository.findAll();
    }

}
