package sango.bucapps.api.Services;

import com.google.gson.Gson;
import io.conekta.Conekta;
import io.conekta.Error;
import io.conekta.ErrorList;
import io.conekta.Order;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.Models.DTO.*;
import sango.bucapps.api.Models.DTO.conekta.*;
import sango.bucapps.api.Models.Entity.*;
import sango.bucapps.api.Repositorys.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private SubOpcionesPrendaRepository subOpcionesPrendaRepository;

    @Autowired
    private ConfirmacionPrendasRepository confirmacionPrendasRepository;

    private static final Long ID_KILO_LAVANDERIA = 1L;

    public CarritoDto actualizarCarrito(String idUsuario, Long subOpcionesPrendaId, Long agregar) {
        CarritoDto carritoDto = obtenerCarritoNuevo(idUsuario);
        if (agregar == 1) {
            // Agregar prenda
            if (Objects.equals(subOpcionesPrendaId, ID_KILO_LAVANDERIA)
                    && carritoRepository.contarCuantasPrendasPorKiloHay(carritoDto.getId(), ID_KILO_LAVANDERIA) == 0) {
                for (int i = 0; i < 4; i++) {
                    carritoRepository.insertarPrendaEnCarrito(carritoDto.getId(), ID_KILO_LAVANDERIA);
                }

            } else {
                carritoRepository.insertarPrendaEnCarrito(carritoDto.getId(), subOpcionesPrendaId);
            }

        } else if (agregar == 0) {
            // Quitar Prenda
            if (Objects.equals(subOpcionesPrendaId, ID_KILO_LAVANDERIA)
                    && carritoRepository.contarCuantasPrendasPorKiloHay(carritoDto.getId(), ID_KILO_LAVANDERIA) <= 4) {
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
        } else {
            carrito = new Carrito();
            carrito.setEstado("Nuevo");
            carrito.setTotal(0D);
            carrito.setUsuario(usuarioRepository.getById(idUsuario));

            Carrito c2 = carritoRepository.save(carrito);
            carritoDto.setId(c2.getId());
            carritoDto.setTotal(0D);
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
        Carrito carrito;
        if (idCarrito == null) {
            carrito = carritoRepository.getAllByUsuarioIdAndEstado(idUsuario, "Nuevo");
        } else {
            carrito = carritoRepository.getById(idCarrito);
        }
        Envios envios = enviosRepository.getAllByCarritoId(carrito.getId());

        ResumenCarritoDto resumen = new ResumenCarritoDto();
        if (envios != null) {
            resumen.setEntrega(envios.getFechaEntrega());
            resumen.setRecoleccion(envios.getFechaRecoleccion());
        }
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
        resumen.setEstado(carrito.getEstado());
        resumen.setCreado(carrito.getCreado());
        resumen.setTotal(carrito.getTotal());
        resumen.setFormaDePago(carrito.getFormaDePago());
        resumen.setCantidadPrendas(carrito.getSubOpcionesPrendas().size());
        resumen.setPrendasList(subDtoList);
        resumen.setCuandoEfectivo(carrito.getCuandoOToken());


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

        list.sort(Comparator.comparing(ResumenCarritoDto::getRecoleccion));

        return list;
    }

    public List<ResumenCarritoDto> obtenerCarritosRecolectados() {
        return listarYCompararPorFecha(carritoRepository.obtenerCarritosRecolectados());
    }

    public List<ResumenCarritoDto> obtenerPedidosPorFechaRepartidor(Date fechaRecoleccion) {
        return listarYCompararPorFecha(carritoRepository.obtenerCarritosNoNuevos(fechaRecoleccion));
    }

    public List<ListaDePrendasDTO> confirmarPrendas(Long idCarrito, List<ListaDePrendasDTO> listaDePrendasDTOS) {
        List<ListaDePrendasDTO> listaDePrendasDTOSActualizada = new ArrayList<>();

        for (ListaDePrendasDTO l : listaDePrendasDTOS) {

            ConfirmacionPrendas prenda = confirmacionPrendasRepository.getAllBySubOpcionesPrendaIdAndIdCarrito(l.getId(), idCarrito);
            if (prenda != null) {
                //Ya existe registro
                if (l.getRevisada() != null) {
                    prenda.setRevisada(l.getRevisada());
                }

            } else {
                prenda = new ConfirmacionPrendas();
                prenda.setCantidad(l.getCantidad());
                prenda.setSubOpcionesPrenda(subOpcionesPrendaRepository.getById(l.getId()));
                prenda.setImg(l.getImg());
                prenda.setNombre(l.getNombre());
                prenda.setPrecio(l.getPrecio());
                prenda.setPrecioTotal(l.getPrecioTotal());
                prenda.setServicio(l.getServicio());
                prenda.setIdCarrito(idCarrito);
                prenda.setRevisada(false);
            }

            ConfirmacionPrendas conf = confirmacionPrendasRepository.save(prenda);
            l.setReg(conf.getReg());
            l.setRevisada(conf.getRevisada());
            l.setComentario(conf.getCommentario());
            l.setPadre(conf.getSubOpcionesPrenda().getOpcionesPrenda().getNombre());

            listaDePrendasDTOSActualizada.add(l);
        }

        return listaDePrendasDTOSActualizada;
    }

    public ConfirmacionPrendas cambiarRegistroValor(Long reg, Boolean registrada) {

        ConfirmacionPrendas prenda = confirmacionPrendasRepository.getAllByReg(reg);
        prenda.setRevisada(registrada);

        return confirmacionPrendasRepository.save(prenda);
    }

    public CarritoDto cambiarEstadoCarrito(String estado, Long idCarrito) {
        Carrito carrito = carritoRepository.getById(idCarrito);
        carrito.setEstado(estado);

        CarritoDto dto = new CarritoDto();
        dto.setId(carrito.getId());
        dto.setEstado(carrito.getEstado());

        carritoRepository.save(carrito);

        return dto;
    }


    public List<ResumenCarritoDto> obtenerPedidosPorFechaRepartidorPendientes(Date fechaRecoleccion) {
        return listarYCompararPorFecha(carritoRepository.obtenerCarritosNoNuevosPendientes(fechaRecoleccion));
    }

    public List<ResumenCarritoDto> obtenerCarritosParaEntrega() {
        return listarYCompararPorFecha(carritoRepository.obtenerCarritosParaEntrega());
    }


    private List<ResumenCarritoDto> listarYCompararPorFecha(List<Carrito> carritos) {
        List<ResumenCarritoDto> list = new ArrayList<>();
        for (Carrito c : carritos) {
            ResumenCarritoDto resumenCarritoDto = new ResumenCarritoDto();


            Envios envios = enviosRepository.getAllByCarritoId(c.getId());

            resumenCarritoDto.setId(c.getId());
            resumenCarritoDto.setRecoleccion(envios.getFechaRecoleccion());
            resumenCarritoDto.setEntrega(envios.getFechaEntrega());
            resumenCarritoDto.setCantidadPrendas(c.getSubOpcionesPrendas().size());
            resumenCarritoDto.setTotal(c.getTotal());
            resumenCarritoDto.setEstado(c.getEstado());
            resumenCarritoDto.setDireccion(c.getDireccion().getDireccion());
            resumenCarritoDto.setNombre(c.getDireccion().getNombre());
            resumenCarritoDto.setTel(c.getDireccion().getTel());
            resumenCarritoDto.setCreado(c.getCreado());
            resumenCarritoDto.setFormaDePago(c.getFormaDePago());
            resumenCarritoDto.setUsuario(c.getUsuario().getToken());
            resumenCarritoDto.setCuandoEfectivo(c.getCuandoOToken());
            resumenCarritoDto.setLat(c.getDireccion().getLat());
            resumenCarritoDto.setLng(c.getDireccion().getLng());

            list.add(resumenCarritoDto);
        }

        list.sort(Comparator.comparing(ResumenCarritoDto::getRecoleccion));

        return list;
    }

    public List<ResumenCarritoDto> obtenerCarritosParaRepartidor(Date fechaRecoleccion) {
        return listarYCompararPorFecha(carritoRepository.obtenerCarritosRepartidor(fechaRecoleccion));
    }

    public ListaDePrendasDTO comentarPrenda(Long registro, ListaDePrendasDTO listaDePrendasDTO) {

        ConfirmacionPrendas prenda = confirmacionPrendasRepository.getAllByReg(registro);
        prenda.setCommentario(listaDePrendasDTO.getComentario());

        confirmacionPrendasRepository.save(prenda);

        return listaDePrendasDTO;
    }

    public CarritoDto solicitarCancelacion(CarritoDto carritoDto) {
        Carrito carrito = carritoRepository.getById(carritoDto.getId());
        carrito.setEstado(carritoDto.getEstado());
        carrito.setMotivoCancelacion(carritoDto.getMotivoCancelacion());

        carritoRepository.save(carrito);

        return carritoDto;
    }

    public DesgloseTodosLosCarritos obtenerDesgloseDeCarritos() {
        List<Carrito> carritos = carritoRepository.findAll();
        List<Envios> envios = enviosRepository.findAll();

        DesgloseTodosLosCarritos desgloseTodosLosCarritos = new DesgloseTodosLosCarritos();

        for (Carrito c : carritos) {

            Envios e = envios.stream()
                    .filter(env -> c.getId().equals(env.getCarrito().getId()))
                    .findAny().orElse(null);

            if (e != null) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                Calendar today = new GregorianCalendar();
                today.setTime(e.getFechaRecoleccion());
                today.set(Calendar.MILLISECOND, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.HOUR, 0);

                Calendar cal = new GregorianCalendar();
                cal.setTime(e.getFechaRecoleccion());
                cal.set(Calendar.MILLISECOND, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.HOUR, 0);

                switch (c.getEstado()) {
                    case "Creada":
                        if (cal.before(today)) {
                            //PASADO
                            desgloseTodosLosCarritos.setRecolectarAtrasados(desgloseTodosLosCarritos.getRecolectarAtrasados() + 1);
                        } else if (cal.after(today)) {
                            //FUTURO
                            desgloseTodosLosCarritos.setRecolectarFuturos(desgloseTodosLosCarritos.getRecolectarFuturos() + 1);
                        } else if (cal.equals(today)) {
                            //HOY
                            desgloseTodosLosCarritos.setRecolectarHoy(desgloseTodosLosCarritos.getRecolectarHoy() + 1);
                        }
                        break;
                    case "Recolectada":
                        desgloseTodosLosCarritos.setEnTienda(desgloseTodosLosCarritos.getEnTienda() + 1);
                        break;
                    case "Entrega":
                        if (cal.before(today)) {
                            //PASADO
                            desgloseTodosLosCarritos.setEntregarAtrasados(desgloseTodosLosCarritos.getEntregarAtrasados() + 1);
                        } else if (cal.after(today)) {
                            //FUTURO
                            desgloseTodosLosCarritos.setEntregarFuturos(desgloseTodosLosCarritos.getEntregarFuturos() + 1);
                        } else if (cal.equals(today)) {
                            //HOY
                            desgloseTodosLosCarritos.setEntregarHoy(desgloseTodosLosCarritos.getEntregarHoy() + 1);
                        }
                        break;
                    case "Finalizada":
                        desgloseTodosLosCarritos.setFinalizados(desgloseTodosLosCarritos.getFinalizados() + 1);
                        break;
                    case "SolicitaCancelacion":
                        desgloseTodosLosCarritos.setSolicitudCancelacion(desgloseTodosLosCarritos.getSolicitudCancelacion() + 1);
                        break;
                    case "Cancelada":
                        desgloseTodosLosCarritos.setCanceladosHistorico(desgloseTodosLosCarritos.getCanceladosHistorico() + 1);
                        break;

                }
            }
        }

        return desgloseTodosLosCarritos;
    }
}
