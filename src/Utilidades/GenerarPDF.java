/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package Utilidades;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

/**
 *
 * @author Duanel
 */

public class GenerarPDF {

    public static void generarPDF(String rutaNominas, int idEmpleado, String rutaSalida) throws IOException {
        List<String> lineas = Files.readAllLines(Paths.get(rutaNominas));

       
        String[] datos = null;
        for (String linea : lineas) {
            String[] partes = linea.split(";");
            if (Integer.parseInt(partes[1]) == idEmpleado) {
                datos = partes;
                break;
            }
        }

        if (datos == null) {
            throw new RuntimeException("Empleado no encontrado en Nominas.txt");
        }
        
        double salarioBase = Double.parseDouble(datos[3]);
        double ars = Double.parseDouble(datos[4]);
        double afp = Double.parseDouble(datos[5]);
        double coop = Double.parseDouble(datos[6]);
        double isr = Double.parseDouble(datos[7]);
        double salarioNeto = Double.parseDouble(datos[8]);
        
        
        try (PDDocument doc = new PDDocument()) {
            PDPage pagina = new PDPage(PDRectangle.A4);
            doc.addPage(pagina);

            try (PDPageContentStream contenido = new PDPageContentStream(doc, pagina)) {
                contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
                contenido.beginText();
                contenido.newLineAtOffset(200, 780);
                contenido.showText("SISTEMA DE NÓMINAS");
                contenido.endText();

                contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
                contenido.beginText();
                contenido.newLineAtOffset(250, 760);
                contenido.showText("VOLANTE DE PAGO");
                contenido.endText();

                
                contenido.beginText();
                contenido.newLineAtOffset(270, 740);
                contenido.showText("PERÍODO: " + datos[2].substring(5, 7) + "/" + datos[2].substring(0, 4));
                contenido.endText();

                
                contenido.beginText();
                contenido.newLineAtOffset(50, 700);
                contenido.showText("ID Empleado: " + datos[1]);
                contenido.newLineAtOffset(0, -15);
                contenido.showText("Nombre Completo: [Nombre desde GenerarNomina.java]");
                contenido.newLineAtOffset(0, -15);
                contenido.showText("Dirección: [Dirección]");
                contenido.newLineAtOffset(0, -15);
                contenido.showText("Teléfono: [Teléfono]");
                contenido.newLineAtOffset(0, -15);
                contenido.showText("Fecha de Ingreso: [Fecha ingreso]");
                contenido.newLineAtOffset(0, -15);
                contenido.showText("Fecha de Generación: " + datos[2]);
                contenido.endText();

                
                float y = 600;
                contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
                contenido.beginText();
                contenido.newLineAtOffset(50, y);
                contenido.showText("Salario Base: RD$ " + String.format("%,.2f", salarioBase));
                contenido.endText();

                y -= 20;
                contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
                contenido.beginText();
                contenido.newLineAtOffset(50, y);
                contenido.showText("Seguro de Salud (ARS): RD$ " + String.format("%,.2f", ars));
                contenido.newLineAtOffset(0, -15);
                contenido.showText("Administradora de Pensiones (AFP): RD$ " + String.format("%,.2f", afp));
                contenido.newLineAtOffset(0, -15);
                contenido.showText("Cooperativa: RD$ " + String.format("%,.2f", coop));
                contenido.newLineAtOffset(0, -15);
                contenido.showText("Impuesto Sobre la Renta (ISR): RD$ " + String.format("%,.2f", isr));
                contenido.endText();

                
                y -= 70;
               contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
                contenido.beginText();
                contenido.newLineAtOffset(50, y);
                contenido.showText("SALARIO NETO: RD$ " + String.format("%,.2f", salarioNeto));
                contenido.endText();
            }

            doc.save(rutaSalida);
        }
    }

    public static void main(String[] args) throws IOException {
        generarPDF("src/BaseDeDatos/Nominas.txt", 4, "C:\\\\Users\\\\Duanel\\\\Desktop\\\\nomina.pdf");System.out.println("PDF generado con éxito.");
    }
}
