package sango.bucapps.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.Models.DTO.MsgRespuestaDto;
import sango.bucapps.api.Models.Entity.Carrito;
import sango.bucapps.api.Models.Entity.Direccion;
import sango.bucapps.api.Models.Entity.Envios;
import sango.bucapps.api.Models.Entity.Usuario;
import sango.bucapps.api.Repositorys.*;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private ConfirmacionPrendasRepository confirmacionPrendasRepository;

    @Autowired
    private EnviosRepository enviosRepository;
    @Autowired
    private SubOpcionesPrendaRepository subOpcionesPrendaRepository;
    @Autowired
    private CarritoService carritoService;


    public MsgRespuestaDto crearOActualizarUsuario(Usuario usuario) {
        MsgRespuestaDto msg = new MsgRespuestaDto();

        usuario.setNombre(usuario.getNombre().toUpperCase().replace("UNDEFINED",""));

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

            carritoService.eliminarErroresCarrito(usuario.getId());
        }
        return msg;
    }

    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerUsuario(String id) {
        Usuario usuario = usuarioRepository.findById(id).get();

        carritoService.eliminarErroresCarrito(id);

        if (usuario.getPuedePagarConCC() == null) {
            usuario.setPuedePagarConCC(false);
        }

        return usuario;
    }

    public MsgRespuestaDto eliminarUsuarios(String id) {
        MsgRespuestaDto msg = new MsgRespuestaDto();
        msg.setMensaje("Datos Eliminados.");
        msg.setHayError(false);
        try {

            Usuario usuario = obtenerUsuario(id);
            List<Carrito> carrito = carritoRepository.getAllByUsuarioId(id);
            List<Direccion> direccion = direccionRepository.getAllByUsuarioId(id);

            direccionRepository.deleteAll(direccion);

            for (Carrito c : carrito) {
                Envios envios = enviosRepository.getByCarritoV2Id(c.getId());
                if (envios != null) {
                    enviosRepository.delete(envios);
                }


                carritoRepository.delete(c);
            }

            usuarioRepository.delete(usuario);


        } catch (Exception e) {
            e.printStackTrace();
            msg.setMensaje("Error al eliminar el usuario");
            msg.setHayError(true);
        }

        return msg;
    }
}
