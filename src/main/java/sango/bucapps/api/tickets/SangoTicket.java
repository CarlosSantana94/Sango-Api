package sango.bucapps.api.tickets;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.PrintModeStyle;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.barcode.BarCode;
import com.github.anastaciocintra.escpos.barcode.QRCode;
import com.github.anastaciocintra.output.PrinterOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sango.bucapps.api.v2.Models.Dtos.DetalleCarrito;
import sango.bucapps.api.v2.Models.Dtos.ResumenCarrito;
import sango.bucapps.api.v2.Repositories.CarritoRepositoryV2;
import sango.bucapps.api.v2.Services.CarritoServiceV2;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SangoTicket {
    @Autowired
    private CarritoServiceV2 carritoServiceV2;

    public void printTicket(Long carritoId) {
        try {
            ResumenCarrito resumenCarrito = carritoServiceV2.obtenerResumenCarritoPorId(carritoId);
            // Define el formato deseado para la fecha
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");

            // Localiza el servicio de impresión para la impresora Epson
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
            if (printService == null) {
                System.out.println("No se encontró ninguna impresora.");
                return;
            }

            // Crea el objeto PrinterOutputStream con la impresora seleccionada
            PrinterOutputStream printerOutputStream = new PrinterOutputStream(printService);
            EscPos escpos = new EscPos(printerOutputStream);

            // Estilo simple para texto
            Style titleStyleCenter = new Style().setBold(true).setFontSize(Style.FontSize._2, Style.FontSize._2)
                    .setJustification(EscPosConst.Justification.Center);

            Style titleStyleCenterSize3 = new Style().setBold(true).setFontSize(Style.FontSize._3, Style.FontSize._3)
                    .setJustification(EscPosConst.Justification.Center);

            // Estilo simple para texto
            Style titleStyleCenterSize1 = new Style().setBold(true).setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setJustification(EscPosConst.Justification.Center);


            Style centerStyle = new Style().setBold(false).setJustification(EscPosConst.Justification.Center);
            escpos.setCharacterCodeTable(EscPos.CharacterCodeTable.ISO8859_15_Latin9);

            Style bold = new Style().setBold(true);

            escpos.writeLF(titleStyleCenter, "TINTORERIA ELECTRONICA");
            escpos.writeLF(titleStyleCenter, "SANGO MATRIZ");
            escpos.writeLF(centerStyle, "DAVID SANTILLAN CORTES");
            escpos.writeLF(centerStyle, "SACD911211AV9");
            escpos.writeLF(centerStyle, "REGIMEN DE INCORPORACION FISCAL");
            escpos.writeLF(centerStyle, "AV. GUADALUPE 4331");
            escpos.writeLF(centerStyle, "CIUDAD DE LOS NIÑOS");
            escpos.writeLF(centerStyle, "GUADALAJARA JALISCO ");
            escpos.writeLF(centerStyle, "CP: 45040");
            escpos.writeLF(centerStyle, "31221189");
            line(escpos);
            QRCode qrCode = new QRCode();
            qrCode.setJustification(EscPosConst.Justification.Center);
            qrCode.setSize(5);
            escpos.write(qrCode, resumenCarrito.getId().toString());
            escpos.writeLF(titleStyleCenterSize3, "Orden #" + resumenCarrito.getId());
            escpos.writeLF(titleStyleCenterSize1, "FECHA:       " + dateFormat.format(resumenCarrito.getEnvios().getFechaCreado()));

            line(escpos);
            escpos.writeLF(titleStyleCenterSize3, "RECOLECCION:");
            escpos.writeLF(titleStyleCenterSize3, dateFormat.format(resumenCarrito.getEnvios().getFechaRecoleccion()));
            escpos.writeLF(titleStyleCenterSize3, "ENTREGA:");
            escpos.writeLF(titleStyleCenterSize3, dateFormat.format(resumenCarrito.getEnvios().getFechaEntrega()));
            escpos.feed(1);
            line(escpos);
            escpos.writeLF(titleStyleCenterSize1, "DATOS DEL CLIENTE");
            escpos.writeLF("NOMBRE CLIENTE:     " + resumenCarrito.getDireccion().getNombre());
            escpos.writeLF("TELEFONO CLIENTE:   " + resumenCarrito.getDireccion().getTel());
            escpos.writeLF("DIRECCION CLIENTE:  " + resumenCarrito.getDireccion().getDireccion());
            line(escpos);
            line(escpos);


            // Agrupar las prendas por servicio
            Map<String, List<DetalleCarrito>> prendasPorServicio = resumenCarrito.getDetalles().stream()
                    .collect(Collectors.groupingBy(detalle -> detalle.getServicio()));

// Iterar sobre cada servicio y sus prendas
            for (Map.Entry<String, List<DetalleCarrito>> entry : prendasPorServicio.entrySet()) {
                String servicio = entry.getKey();
                List<DetalleCarrito> detalles = entry.getValue();

                // Imprimir encabezado del servicio
                escpos.writeLF(titleStyleCenterSize1, servicio);

                // Imprimir cada prenda dentro del servicio
                for (DetalleCarrito detalle : detalles) {
                    escpos.writeLF(detalle.getNombreCategoria() + " > " + detalle.getNombrePrenda() + " x" + detalle.getCantidad());
                    escpos.writeLF("Precio Unitario: $" + detalle.getPrecioUnitario());
                    escpos.writeLF("Subtotal: $" + (detalle.getPrecioUnitario() * detalle.getCantidad()));
                }

                line(escpos);
            }

// Imprimir subtotales y total
            double total = resumenCarrito.getDetalles().stream()
                    .mapToDouble(detalle -> detalle.getPrecioUnitario() * detalle.getCantidad()).sum();

            escpos.writeLF("SUBTOTAL: $" + total);
            escpos.writeLF("TOTAL:    $" + total);

            line(escpos);
            footer(escpos);


            escpos.feed(3); // Alimenta un poco más el papel
            escpos.cut(EscPos.CutMode.FULL);

            // Cierra el objeto escpos
            escpos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void line(EscPos escpos) throws IOException {
        Style titleStyleCenter = new Style().setBold(false).setJustification(EscPosConst.Justification.Center);
        escpos.writeLF(titleStyleCenter, "_____________________________________________");
    }

    private void footer(EscPos escpos) throws IOException {
        Style titleStyleCenter = new Style().setBold(false).setJustification(EscPosConst.Justification.Center);
        escpos.feed(1);
        escpos.writeLF(titleStyleCenter, "DONDE TRATAMOS SU ROPA CON AMOR");
        escpos.feed(1);
        escpos.writeLF(titleStyleCenter, "¡GRACIAS POR SU PREFERENCIA!");
        escpos.writeLF(titleStyleCenter, "NO NOS HACEMOS RESPONSABLES POR TRABAJOS QUE HAN SALIDO DE NUESTRAS INSTALACIONES Y/O POR TRABAJOS QUE NO HAN SIDO RECOGIDOS DESPUES DE 30 DIAS DE SU FECHA DE ENTREGA");
        escpos.writeLF(titleStyleCenter, "ESTO NO ES UN COMPROBANTE FISCAL");
        escpos.feed(2);
    }
}
