/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package Utilidades;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

/**
 *
 * @author Duanel
 */

public class GenerarPDF {
    public static void main(String[] args) {
        System.setProperty("org.apache.pdfbox.base.font.disablesystemfont", "true");
        
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            
            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                content.setFont(font, 12);
                
                content.beginText();
                content.newLineAtOffset(50, 700);
                content.showText("PDF generado sin warnings de fuentes");
                content.endText();
            }
            
            doc.save("C:\\Users\\Duanel\\Desktop\\ejemplo.pdf");
            System.out.println("PDF creado exitosamente!");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}