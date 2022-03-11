package sango.bucapps.api.Services;

import com.google.gson.Gson;
import io.conekta.Conekta;
import io.conekta.Error;
import io.conekta.ErrorList;
import io.conekta.Order;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.Models.DTO.CarritoDto;
import sango.bucapps.api.Models.DTO.MsgRespuestaDto;
import sango.bucapps.api.Models.DTO.ResumenCarritoDto;
import sango.bucapps.api.Models.DTO.SubOpcionesPrendaDto;
import sango.bucapps.api.Models.DTO.conekta.*;
import sango.bucapps.api.Models.Entity.Carrito;
import sango.bucapps.api.Models.Entity.Envios;
import sango.bucapps.api.Models.Entity.SubOpcionesPrenda;
import sango.bucapps.api.Models.Entity.Usuario;
import sango.bucapps.api.Repositorys.CarritoRepository;
import sango.bucapps.api.Repositorys.DireccionRepository;
import sango.bucapps.api.Repositorys.EnviosRepository;
import sango.bucapps.api.Repositorys.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CarritoService {
    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private EnviosRepository enviosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Long ID_KILO_LAVANDERIA = 79L;

    public CarritoDto actualizarCarrito(String idUsuario, Long subOpcionesPrendaId, Long agregar) {
        CarritoDto carritoDto = obtenerCarritoNuevo(idUsuario);
        if (agregar == 1) {
            // Agregar prenda
            if (Objects.equals(subOpcionesPrendaId, ID_KILO_LAVANDERIA)
                    && carritoRepository.contarCuantasPrendasPorKiloHay(carritoDto.getId()) == 0) {
                for (int i = 0; i < 4; i++) {
                    carritoRepository.insertarPrendaEnCarrito(carritoDto.getId(), ID_KILO_LAVANDERIA);
                }

            }else {
                carritoRepository.insertarPrendaEnCarrito(carritoDto.getId(), subOpcionesPrendaId);
            }

        } else if (agregar == 0) {
            // Quitar Prenda
            if (Objects.equals(subOpcionesPrendaId, ID_KILO_LAVANDERIA)
                    && carritoRepository.contarCuantasPrendasPorKiloHay(carritoDto.getId()) <= 4) {
                carritoRepository.borrarPrendaEnCarritoPorKiloMenorA4(carritoDto.getId(), ID_KILO_LAVANDERIA);
            } else {
                carritoRepository.borrarPrendaEnCarrito(carritoDto.getId(), subOpcionesPrendaId);
            }
        }

        return carritoDto;
    }


    public CarritoDto obtenerCarritoNuevo(String idUsuario) {
        CarritoDto carritoDto = new CarritoDto();
        carritoDto.setCantidad(0L);

        Carrito carrito = carritoRepository.getAllByUsuarioIdAndEstado(idUsuario, "Nuevo");

        if (carrito != null) {
            carritoDto.setId(carrito.getId());
            double total = 0D;
            for (SubOpcionesPrenda sub : carrito.getSubOpcionesPrendas()) {
                total = Math.round((total + sub.getPrecio()) * 100D) / 100D;
                Long cantidad = carritoDto.getCantidad() + 1;
                carritoDto.setCantidad(cantidad);
            }
            carritoDto.setTotal(total);
            carrito.setTotal(total);
            carritoRepository.save(carrito);
        }


        return carritoDto;
    }

    public CarritoDto actualizarDireccionEnCarrito(String idUsuario, Long direccionId) {
        Carrito carrito = carritoRepository.getAllByUsuarioIdAndEstado(idUsuario, "Nuevo");
        carrito.setDireccion(direccionRepository.getById(direccionId));
        carritoRepository.save(carrito);

        return obtenerCarritoNuevo(idUsuario);
    }

    public ResumenCarritoDto obtenerResumenDeCarrito(String idUsuario, Long idCarrito) {
        Carrito carrito = new Carrito();
        if (idCarrito == null) {
            carrito = carritoRepository.getAllByUsuarioIdAndEstado(idUsuario, "Nuevo");
        } else {
            carrito = carritoRepository.getById(idCarrito);
        }
        Envios envios = enviosRepository.getAllByCarritoId(carrito.getId());

        ResumenCarritoDto resumen = new ResumenCarritoDto();
        resumen.setEntrega(envios.getFechaEntrega());
        resumen.setRecoleccion(envios.getFechaRecoleccion());
        resumen.setId(carrito.getId());
        if (carrito.getDireccion() != null) {
            resumen.setDireccion(carrito.getDireccion().getDireccion());
            resumen.setCp(carrito.getDireccion().getCp());
            resumen.setNombre(carrito.getDireccion().getNombre());
            resumen.setTel(carrito.getDireccion().getTel());
        }
        int cantidadPrendas = 0;

        List<SubOpcionesPrendaDto> subDtoList = new ArrayList<>();

        for (SubOpcionesPrenda sub : carrito.getSubOpcionesPrendas()) {

            Optional<SubOpcionesPrendaDto> subDto = subDtoList.stream().filter(s -> s.getId().equals(sub.getId())).findAny();

            if (subDto.isPresent()) {
                subDto.get().setCantidad(subDto.get().getCantidad() + 1);
                subDto.get().setPrecioTotal(sub.getPrecio() * subDto.get().getCantidad());
            } else {
                SubOpcionesPrendaDto subDtoNew = new SubOpcionesPrendaDto();
                subDtoNew.setId(sub.getId());
                subDtoNew.setNombre(sub.getNombre());
                subDtoNew.setPrecio(sub.getPrecio());
                subDtoNew.setPrecioTotal(sub.getPrecio());
                subDtoNew.setImg(sub.getImg());
                subDtoNew.setCantidad(1L);
                subDtoNew.setServicio(sub.getOpcionesPrenda().getServicio().getNombre());

                subDtoList.add(subDtoNew);
            }

        }
        resumen.setCreado(carrito.getCreado());
        resumen.setTotal(carrito.getTotal());
        resumen.setCantidadPrendas(carrito.getSubOpcionesPrendas().size());
        resumen.setPrendasList(subDtoList);


        return resumen;
    }

    public MsgRespuestaDto pagarCarrito(String idUsuario, String metodo, String cuandoOToken) throws Error {
        ResumenCarritoDto resumenCarritoDto = obtenerResumenDeCarrito(idUsuario, null);
        Carrito carrito = carritoRepository.getAllByUsuarioIdAndEstado(idUsuario, "Nuevo");
        MsgRespuestaDto respuestaDto = new MsgRespuestaDto();

        carrito.setFormaDePago(metodo);
        carrito.setCuandoOToken(cuandoOToken);

        if (metodo.equals("tarjeta")) {
            ConektaDto conektaDto = new ConektaDto();
            MetadataDto metadataDto = new MetadataDto();
            List<LineItemDto> lineItemDtoList = new ArrayList<>();
            CustomerInfoDto customerInfoDto = new CustomerInfoDto();
            List<ChargesDto> chargesDtoList = new ArrayList<>();

            metadataDto.setTest(true);

            for (SubOpcionesPrendaDto prenda : resumenCarritoDto.getPrendasList()) {
                LineItemDto lineItemDto = new LineItemDto();
                lineItemDto.setName(prenda.getNombre());
                lineItemDto.setDescription(prenda.getDescripcion());
                lineItemDto.setUnit_price((int) (prenda.getPrecio() * 100));
                lineItemDto.setQuantity(prenda.getCantidad());
                lineItemDto.setType(prenda.getServicio());

                lineItemDtoList.add(lineItemDto);
            }

            Usuario usuario = usuarioRepository.getById(idUsuario);

            customerInfoDto.setName(resumenCarritoDto.getNombre());
            customerInfoDto.setPhone(resumenCarritoDto.getTel().toString());
            customerInfoDto.setEmail(usuario.getEmail());

            PaymentMethodDto paymentMethodDto = new PaymentMethodDto();
            paymentMethodDto.setType("card");
            paymentMethodDto.setToken_id("tok_test_visa_4242");

            ChargesDto chargesDto = new ChargesDto();
            chargesDto.setPayment_method(paymentMethodDto);
            chargesDto.setAmount((int) (resumenCarritoDto.getTotal() * 100));
            chargesDtoList.add(chargesDto);

            conektaDto.setMetadata(metadataDto);
            conektaDto.setCharges(chargesDtoList);
            conektaDto.setLine_items(lineItemDtoList);
            conektaDto.setCustomer_info(customerInfoDto);


            try {
                Conekta.setApiKey("key_oLvDZeZYTN7HAoNYYAffuw");

                String jsonInString = new Gson().toJson(conektaDto);
                System.out.println(jsonInString);
                JSONObject completeOrderJSON = new JSONObject(jsonInString);


                Order completeOrder = Order.create(completeOrderJSON);
                carrito.setOrdenConekta(completeOrder.getId());
                carrito.setEstado("Creada");
                respuestaDto.setMensaje("Orden Completada");

                carritoRepository.save(carrito);
                obtenerCarritoNuevo(idUsuario);

            } catch (ErrorList e) {
                respuestaDto.setHayError(true);
                respuestaDto.setMensaje(e.details.toString());
                System.out.println(e.details);
            }


        } else if (metodo.equals("efectivo")) {
            carrito.setEstado("Creada");
            respuestaDto.setMensaje("Orden Completada");

            carritoRepository.save(carrito);
            obtenerCarritoNuevo(idUsuario);
        }


        return respuestaDto;
    }


    public List<ResumenCarritoDto> obtenerPedidos(String idUsuario) {
        List<ResumenCarritoDto> list = new ArrayList<>();

        List<Carrito> carritos = carritoRepository.getAllByUsuarioId(idUsuario);

        for (Carrito c : carritos) {
            if (!c.getEstado().equals("Nuevo")) {
                ResumenCarritoDto resumenCarritoDto = new ResumenCarritoDto();
                Envios envios = enviosRepository.getAllByCarritoId(c.getId());

                resumenCarritoDto.setId(c.getId());
                resumenCarritoDto.setRecoleccion(envios.getFechaRecoleccion());
                resumenCarritoDto.setEntrega(envios.getFechaEntrega());
                resumenCarritoDto.setCantidadPrendas(c.getSubOpcionesPrendas().size());
                resumenCarritoDto.setTotal(c.getTotal());
                resumenCarritoDto.setEstado(c.getEstado());

                list.add(resumenCarritoDto);
            }
        }

        return list;
    }

}
