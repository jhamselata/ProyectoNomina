package Utilidades;

import java.awt.Color;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public class GenerarPDF {

    public static void generarPDF(String rutaNominas, String rutaEmpleados, String rutaSalida) throws IOException {
        // Leer empleados
        List<String[]> empleados = new ArrayList<>();
        for (String linea : Files.readAllLines(Paths.get(rutaEmpleados))) {
            empleados.add(linea.split(";"));
        }

        try (PDDocument doc = new PDDocument()) {

            for (String[] empleado : empleados) {
                int idEmpleado = Integer.parseInt(empleado[0]);
                
                String[] nomina = null;
                for (String linea : Files.readAllLines(Paths.get(rutaNominas))) {
                    String[] partes = linea.split(";");
                    if (Integer.parseInt(partes[1]) == idEmpleado) {
                        nomina = partes;
                        break;
                    }
                }
                if (nomina == null) continue;
                
                String nombreCompleto = empleado[1] + " " + empleado[2] + " " + empleado[3];
                String direccion = empleado[4];
                String telefono = empleado[5];
                String fechaIngreso = empleado[8];

                String fechaNomina = nomina[2];
                double salarioBase = Double.parseDouble(nomina[3]);
                double ars = Double.parseDouble(nomina[4]);
                double afp = Double.parseDouble(nomina[5]);
                double coop = Double.parseDouble(nomina[6]);
                double isr = Double.parseDouble(nomina[7]);
                double salarioNeto = Double.parseDouble(nomina[8]);
                
                PDPage pagina = new PDPage(PDRectangle.A4);
                doc.addPage(pagina);

                try (PDPageContentStream contenido = new PDPageContentStream(doc, pagina)) {

                    
                    contenido.beginText();
                    contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
                    contenido.newLineAtOffset(200, 780);
                    contenido.showText("SISTEMA DE NÓMINAS");
                    contenido.endText();

                    contenido.beginText();
                    contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 14);
                    contenido.newLineAtOffset(250, 760);
                    contenido.showText("VOLANTE DE PAGO");
                    contenido.endText();

                    contenido.beginText();
                    contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE), 12);
                    contenido.newLineAtOffset(270, 740);
                    contenido.showText("PERÍODO: " + fechaNomina.substring(5, 7) + "/" + fechaNomina.substring(0, 4));
                    contenido.endText();

                    
                    float x = 50;
                    float y = 700;
                    float anchoCaja = 500;
                    float altoCaja = 100;

                    contenido.setLineWidth(1.5f);
                    contenido.addRect(x, y - altoCaja, anchoCaja, altoCaja);
                    contenido.stroke();

                    contenido.beginText();
                    contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                    contenido.newLineAtOffset(x + 10, y - 20);
                    contenido.showText("ID Empleado: " + idEmpleado);
                    contenido.newLineAtOffset(0, -15);
                    contenido.showText("Nombre Completo: " + nombreCompleto);
                    contenido.newLineAtOffset(0, -15);
                    contenido.showText("Dirección: " + direccion);
                    contenido.newLineAtOffset(0, -15);
                    contenido.showText("Teléfono: " + telefono);
                    contenido.newLineAtOffset(0, -15);
                    contenido.showText("Fecha de Ingreso: " + fechaIngreso);
                    contenido.newLineAtOffset(0, -15);
                    contenido.showText("Fecha de Generación: " + fechaNomina);
                    contenido.endText();

                    
                    float tablaX = 50;
                    float tablaY = 570;
                    float anchoCol1 = 200; 
                    float anchoCol2 = 100; 
                    float anchoCol3 = 200; 
                    float altoFila = 20;

                    String[][] filas = {
                        {"Concepto", "Porcentaje", "Monto (RD$)"},
                        {"Salario Base", "", String.format("%,.2f", salarioBase)},
                        {"DESCUENTOS", "", ""},
                        {"Seguro de Salud (ARS)", "3.04%", String.format("%,.2f", ars)},
                        {"Administradora de Pensiones (AFP)", "2.87%", String.format("%,.2f", afp)},
                        {"Cooperativa", "3.8%", String.format("%,.2f", coop)},
                        {"Impuesto Sobre la Renta (ISR)", "-", String.format("%,.2f", isr)},
                        {"Salario Neto", "", String.format("%,.2f", salarioNeto)}
                    };

                    
                    for (int i = 0; i < filas.length; i++) {
                        if (i == 0 || filas[i][0].equals("DESCUENTOS")) {
                            contenido.setNonStrokingColor(new Color(220, 220, 220));
                            contenido.addRect(tablaX, tablaY - i * altoFila - altoFila, anchoCol1 + anchoCol2 + anchoCol3, altoFila);
                            contenido.fill();
                            contenido.setNonStrokingColor(Color.BLACK);
                        }
                    }

                    
                    contenido.setLineWidth(1.5f);
                    for (int i = 0; i <= filas.length; i++) {
                        contenido.moveTo(tablaX, tablaY - i * altoFila);
                        contenido.lineTo(tablaX + anchoCol1 + anchoCol2 + anchoCol3, tablaY - i * altoFila);
                    }
                    contenido.stroke();

                    
                    contenido.moveTo(tablaX, tablaY);
                    contenido.lineTo(tablaX, tablaY - filas.length * altoFila);
                    contenido.stroke();

                    contenido.moveTo(tablaX + anchoCol1, tablaY);
                    contenido.lineTo(tablaX + anchoCol1, tablaY - filas.length * altoFila);
                    contenido.stroke();

                    contenido.moveTo(tablaX + anchoCol1 + anchoCol2, tablaY);
                    contenido.lineTo(tablaX + anchoCol1 + anchoCol2, tablaY - filas.length * altoFila);
                    contenido.stroke();

                    contenido.moveTo(tablaX + anchoCol1 + anchoCol2 + anchoCol3, tablaY);
                    contenido.lineTo(tablaX + anchoCol1 + anchoCol2 + anchoCol3, tablaY - filas.length * altoFila);
                    contenido.stroke();

                    
                    for (int i = 0; i < filas.length; i++) {
                        contenido.beginText();
                        if (i == 0 || filas[i][0].equals("DESCUENTOS")) {
                            contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                        } else {
                            contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                        }
                        contenido.newLineAtOffset(tablaX + 5, tablaY - (i * altoFila) - 15);
                        contenido.showText(filas[i][0]);
                        contenido.endText();

                        contenido.beginText();
                        contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                        contenido.newLineAtOffset(tablaX + anchoCol1 + 5, tablaY - (i * altoFila) - 15);
                        contenido.showText(filas[i][1]);
                        contenido.endText();

                        contenido.beginText();
                        contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                        contenido.newLineAtOffset(tablaX + anchoCol1 + anchoCol2 + 5, tablaY - (i * altoFila) - 15);
                        contenido.showText(filas[i][2]);
                        contenido.endText();
                    }
                }
            }

            doc.save(rutaSalida);
        }
    }

    public static void main(String[] args) throws IOException {
        generarPDF("src/BaseDeDatos/Nominas.txt", "src/BaseDeDatos/Empleados.txt", "src/VolantesDeNómina/Nómina.pdf");
    }
}
