package sango.bucapps.api.v2.Services;

import com.google.gson.Gson;
import io.conekta.Conekta;
import io.conekta.Error;
import io.conekta.ErrorList;
import io.conekta.Order;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sango.bucapps.api.Models.DTO.MsgRespuestaDto;
import sango.bucapps.api.Models.DTO.ResumenCarritoDto;
import sango.bucapps.api.Models.DTO.SubOpcionesPrendaDto;
import sango.bucapps.api.Models.DTO.conekta.*;
import sango.bucapps.api.Models.Entity.Direccion;
import sango.bucapps.api.Models.Entity.SubOpcionesPrenda;
import sango.bucapps.api.Models.Entity.Usuario;
import sango.bucapps.api.Repositorys.DireccionRepository;
import sango.bucapps.api.Repositorys.SubOpcionesPrendaRepository;
import sango.bucapps.api.v2.Models.Dtos.DetalleCarrito;
import sango.bucapps.api.v2.Models.Dtos.ResumenCarrito;
import sango.bucapps.api.v2.Models.Entities.CarritoItemV2;
import sango.bucapps.api.v2.Models.Entities.CarritoV2;
import sango.bucapps.api.v2.Models.Entities.UsuarioV2;
import sango.bucapps.api.v2.Models.Enums.EstadoCarrito;
import sango.bucapps.api.v2.Repositories.CarritoItemRepositoryV2;
import sango.bucapps.api.v2.Repositories.CarritoRepositoryV2;
import sango.bucapps.api.v2.Repositories.UsuarioRepositoryV2;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarritoServiceV2 {

    private static final Long PRENDA_ID_LAVANDERIA = 1L;

    @Autowired
    private CarritoRepositoryV2 carritoRepository;

    @Autowired
    private UsuarioRepositoryV2 usuarioRepository;

    @Autowired
    SubOpcionesPrendaRepository subOpcionPrendaRepository;

    @Autowired
    CarritoItemRepositoryV2 carritoItemRepository;

    @Autowired
    private DireccionRepository direccionRepository;

    public CarritoV2 crearCarrito(CarritoV2 carrito, String usuarioId) {
        UsuarioV2 usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        carrito.setUsuario(usuario);
        carrito.setEstado(EstadoCarrito.NUEVO);
        return carritoRepository.save(carrito);
    }

    public List<CarritoV2> obtenerCarritosPorUsuario(String usuarioId) {

        List<CarritoV2> carritoV2List = carritoRepository.findByUsuarioId(usuarioId);

        List<CarritoV2> carritoV2ListToSend = new ArrayList<>();
        for (CarritoV2 carritoV2 : carritoV2List) {
            if (carritoV2.getEstado() != EstadoCarrito.NUEVO) {
                carritoV2ListToSend.add(carritoV2);
            }
        }

        return carritoV2ListToSend;
    }

    public CarritoV2 actualizarEstado(Long carritoId, EstadoCarrito nuevoEstado) {
        CarritoV2 carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        carrito.setEstado(nuevoEstado);
        return carritoRepository.save(carrito);
    }

    public List<CarritoV2> obtenerCarritosPorEstado(EstadoCarrito estado) {
        return carritoRepository.findByEstado(estado);
    }

    // Guardar el carrito al actualizarlo
    public CarritoV2 guardarCarrito(CarritoV2 carrito) {
        return carritoRepository.save(carrito);
    }

    public Optional<CarritoV2> obtenerCarritoPorId(Long carritoId) {
        return carritoRepository.findById(carritoId);
    }

    // Crear o recuperar el carrito activo del usuario
    public CarritoV2 obtenerCarritoActivo(String usuarioId) {
        UsuarioV2 usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Optional<CarritoV2> carritoOpt = carritoRepository.findByUsuarioAndEstado(usuario, EstadoCarrito.NUEVO);
        if (carritoOpt.isPresent()) {
            return carritoOpt.get();
        }

        CarritoV2 nuevoCarrito = new CarritoV2();
        nuevoCarrito.setUsuario(usuario);
        nuevoCarrito.setFechaCreacion(new Date());
        nuevoCarrito.setEstado(EstadoCarrito.NUEVO);
        return carritoRepository.save(nuevoCarrito);
    }

    // Método para añadir una prenda al carrito con validación de cantidad
    public CarritoV2 añadirPrendaAlCarrito(String usuarioId, Long prendaId, int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        CarritoV2 carrito = obtenerCarritoActivo(usuarioId);
        SubOpcionesPrenda prenda = subOpcionPrendaRepository.findById(prendaId)
                .orElseThrow(() -> new RuntimeException("Prenda no encontrada"));

        Optional<CarritoItemV2> itemExistente = carrito.getItems().stream()
                .filter(item -> item.getPrenda().getId().equals(prendaId))
                .findFirst();

        if (itemExistente.isPresent()) {
            CarritoItemV2 item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            carritoItemRepository.save(item);
        } else {
            if (prendaId.equals(PRENDA_ID_LAVANDERIA)) {
                cantidad = 4;
            }

            CarritoItemV2 nuevoItem = new CarritoItemV2();
            nuevoItem.setPrenda(prenda);
            nuevoItem.setCarrito(carrito);
            nuevoItem.setCantidad(cantidad);
            carrito.getItems().add(nuevoItem);
            carritoItemRepository.save(nuevoItem);
        }

        return carritoRepository.save(carrito);
    }

    // Actualizar la cantidad de una prenda en el carrito
    public CarritoItemV2 actualizarCantidadPrenda(Long carritoId, Long prendaId, int nuevaCantidad) {
        CarritoV2 carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        CarritoItemV2 item = carrito.getItems().stream()
                .filter(i -> i.getPrenda().getId().equals(prendaId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Prenda no encontrada en el carrito"));
        if (prendaId.equals(PRENDA_ID_LAVANDERIA)) {
            if (nuevaCantidad  >= 4) {
                item.setCantidad(nuevaCantidad);
                return carritoItemRepository.save(item);
            } else {
                eliminarPrendaDelCarrito(carritoId, prendaId);
                return null; // Se eliminó la prenda
            }
        } else {
            if (nuevaCantidad > 0) {
                item.setCantidad(nuevaCantidad);
                return carritoItemRepository.save(item);
            } else {
                eliminarPrendaDelCarrito(carritoId, prendaId);
                return null; // Se eliminó la prenda
            }
        }

    }

    // Eliminar una prenda del carrito
    public void eliminarPrendaDelCarrito(Long carritoId, Long prendaId) {
        CarritoV2 carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        CarritoItemV2 item = carrito.getItems().stream()
                .filter(i -> i.getPrenda().getId().equals(prendaId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Prenda no encontrada en el carrito"));

        carrito.getItems().remove(item);
        carritoItemRepository.delete(item);
        carritoRepository.save(carrito);
    }

    // Obtener el resumen del carrito
    public ResumenCarrito obtenerResumenCarrito(String usuarioId) {
        CarritoV2 carrito = obtenerCarritoActivo(usuarioId);
        List<DetalleCarrito> detalles = carrito.getItems().stream()
                .map(item -> new DetalleCarrito(item.getPrenda().getNombre(),
                        item.getCantidad(),
                        item.getPrenda().getPrecio(),
                        item.getCantidad() * item.getPrenda().getPrecio(),
                        item.getPrenda().getId(),
                        item.getPrenda().getOpcionesPrenda().getNombre(),
                        item.getPrenda().getOpcionesPrenda().getId(),
                        item.getPrenda().getOpcionesPrenda().getServicio().getNombre(),
                        item.getPrenda().getOpcionesPrenda().getServicio().getId(),
                        item.getPrenda().getImg()
                ))
                .collect(Collectors.toList());

        double total = detalles.stream().mapToDouble(DetalleCarrito::getSubtotal).sum();

        // Calcular el total de prendas (sumando las cantidades de cada ítem)
        int totalPrendas = detalles.stream().mapToInt(DetalleCarrito::getCantidad).sum();

        return new ResumenCarrito(detalles, total, carrito.getId(), totalPrendas, carrito.getEnvios(), carrito.getDireccion());
    }

    public ResumenCarrito obtenerResumenCarritoPorId(Long carritoId) {
        CarritoV2 carrito = carritoRepository.getById(carritoId);
        List<DetalleCarrito> detalles = carrito.getItems().stream()
                .map(item -> new DetalleCarrito(item.getPrenda().getNombre(),
                        item.getCantidad(),
                        item.getPrenda().getPrecio(),
                        item.getCantidad() * item.getPrenda().getPrecio(),
                        item.getPrenda().getId(),
                        item.getPrenda().getOpcionesPrenda().getNombre(),
                        item.getPrenda().getOpcionesPrenda().getId(),
                        item.getPrenda().getOpcionesPrenda().getServicio().getNombre(),
                        item.getPrenda().getOpcionesPrenda().getServicio().getId(),
                        item.getPrenda().getImg()
                ))
                .collect(Collectors.toList());

        double total = detalles.stream().mapToDouble(DetalleCarrito::getSubtotal).sum();

        // Calcular el total de prendas (sumando las cantidades de cada ítem)
        int totalPrendas = detalles.stream().mapToInt(DetalleCarrito::getCantidad).sum();

        return new ResumenCarrito(detalles, total, carrito.getId(), totalPrendas, carrito.getEnvios(), carrito.getDireccion());
    }

    public CarritoV2 asignarDireccion(Long carritoId, Long direccionId) {
        // Buscar el carrito por ID
        CarritoV2 carrito = carritoRepository.findById(carritoId).orElse(null);
        if (carrito == null) {
            return null; // Carrito no encontrado
        }

        // Buscar la dirección por ID
        Direccion direccion = direccionRepository.findById(direccionId).orElse(null);
        if (direccion == null) {
            return null; // Dirección no encontrada
        }

        // Asignar la dirección al carrito
        carrito.setDireccion(direccion);

        // Guardar el carrito con la nueva dirección
        return carritoRepository.save(carrito);
    }


    public MsgRespuestaDto pagarCarrito(String usuarioId, String metodo, String cuandoOToken, String email) throws Error {
        ResumenCarrito resumenCarrito = obtenerResumenCarrito(usuarioId);
        CarritoV2 carrito = carritoRepository.getById(resumenCarrito.getId());

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

            for (DetalleCarrito prenda : resumenCarrito.getDetalles()) {
                LineItemDto lineItemDto = new LineItemDto();
                lineItemDto.setName(prenda.getNombrePrenda());
                lineItemDto.setUnit_price((int) (prenda.getPrecioUnitario() * 100));
                lineItemDto.setQuantity((long) prenda.getCantidad());
                lineItemDto.setType(prenda.getServicio());
                lineItemDto.setDescription(prenda.getServicio() + " > "
                        + prenda.getNombreCategoria() + " > "
                        + prenda.getNombrePrenda());

                lineItemDtoList.add(lineItemDto);
            }

            UsuarioV2 usuarioV2 = usuarioRepository.getById(usuarioId);

            customerInfoDto.setEmail(email);
            customerInfoDto.setName(usuarioV2.getNombre());

            PaymentMethodDto paymentMethodDto = new PaymentMethodDto();
            paymentMethodDto.setType("card");
            paymentMethodDto.setToken_id(cuandoOToken);

            ChargesDto chargesDto = new ChargesDto();
            chargesDto.setPayment_method(paymentMethodDto);
            chargesDto.setAmount((int) (resumenCarrito.getTotal() * 100));
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
                carrito.setEstado(EstadoCarrito.CREADO);
                respuestaDto.setMensaje("Orden Completada");

                carritoRepository.save(carrito);
                obtenerCarritoActivo(usuarioId);

            } catch (ErrorList e) {
                respuestaDto.setHayError(true);
                respuestaDto.setMensaje(e.details.toString());
                System.out.println(e.details);
            }


        } else if (metodo.equals("efectivo")) {
            carrito.setEstado(EstadoCarrito.CREADO);
            respuestaDto.setMensaje("Orden Completada");

            carritoRepository.save(carrito);
            obtenerCarritoActivo(usuarioId);
        }
        return respuestaDto;
    }

    // Obtener carritos agrupados por estado (excluyendo "NUEVO")
    public Map<EstadoCarrito, Map<String, Object>> obtenerCarritosAgrupadosPorEstado() {
        return carritoRepository.findAll().stream()
                .filter(carrito -> carrito.getEstado() != EstadoCarrito.NUEVO) // Ignorar carritos en estado NUEVO
                .collect(Collectors.groupingBy(
                        CarritoV2::getEstado,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                carritos -> {
                                    Map<String, Object> result = new HashMap<>();
                                    result.put("total", carritos.size());

                                    // Calcular el total de cada carrito
                                    List<Map<String, Object>> carritosConTotal = carritos.stream()
                                            .map(carrito -> {
                                                double totalCarrito = carrito.getItems().stream()
                                                        .mapToDouble(item -> item.getPrenda().getPrecio() * item.getCantidad()) // Precio * cantidad
                                                        .sum();

                                                Map<String, Object> carritoMap = new HashMap<>();
                                                carritoMap.put("carrito", carrito);
                                                carritoMap.put("totalCarrito", totalCarrito); // Total monetario del carrito
                                                return carritoMap;
                                            })
                                            .collect(Collectors.toList());

                                    result.put("carritos", carritosConTotal);
                                    return result;
                                }
                        )
                ));
    }

    @Transactional
    public void actualizarImprimir(Long carritoId) {
        CarritoV2 carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado con id: " + carritoId));
        carrito.setImprimir(false);
        carritoRepository.save(carrito);
    }
}

