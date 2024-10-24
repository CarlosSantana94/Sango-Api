package sango.bucapps.api.tickets;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.PrintModeStyle;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.barcode.BarCode;
import com.github.anastaciocintra.escpos.barcode.QRCode;
import com.github.anastaciocintra.output.PrinterOutputStream;
import org.springframework.stereotype.Service;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SangoTicket {
    public void printTicket() {
        try {
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
            escpos.write(qrCode, "1234");
            escpos.writeLF(titleStyleCenter, "NOTA x APP");
            escpos.writeLF(titleStyleCenter, "1234");
            line(escpos);

            // Crea el objeto Date para la fecha actual
            Date currentDate = new Date();

            // Define el formato deseado para la fecha
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm");
            String formattedDate = dateFormat.format(currentDate);


            escpos.writeLF("FECHA:       " + formattedDate);
            escpos.writeLF("RECOLECCION: " + formattedDate);
            escpos.writeLF("ENTREGA:     " + formattedDate);
            //escpos.writeLF("\n");
            escpos.feed(1);
            escpos.writeLF("DATOS DEL CLIENTE");
            escpos.writeLF("NOMBRE CLIENTE");
            escpos.writeLF("TELEFONO CLIENTE");
            escpos.writeLF("DIRECCION CLIENTE");
            line(escpos);

            // TODO el pedido completo

            // TODO subtotal y totales


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
        escpos.writeLF(titleStyleCenter, "____________________________________________");
    }

    private void footer(EscPos escpos) throws IOException {
        Style titleStyleCenter = new Style().setBold(false).setJustification(EscPosConst.Justification.Center);
        escpos.feed(1);
        escpos.writeLF(titleStyleCenter,"DONDE TRATAMOS SU ROPA CON AMOR");
        escpos.feed(1);
        escpos.writeLF(titleStyleCenter,"¡GRACIAS POR SU PREFERENCIA!");
        escpos.writeLF(titleStyleCenter,"NO NOS HACEMOS RESPONSABLES POR TRABAJOS QUE HAN SALIDO DE NUESTRAS INSTALACIONES Y/O POR TRABAJOS QUE NO HAN SIDO RECOGIDOS DESPUES DE 30 DIAS DE SU FECHA DE ENTREGA");
        escpos.writeLF(titleStyleCenter,"ESTO NO ES UN COMPROBANTE FISCAL");
    }
}
