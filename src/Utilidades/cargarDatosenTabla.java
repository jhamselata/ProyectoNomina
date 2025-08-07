package Utilidades;

import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class cargarDatosenTabla {

    // Método para cargar datos en una JTable desde un archivo .txt
    public static void cargarEnTabla(JTable tabla, String rutaArchivo) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0); // Borra solo las filas, NO las columnas

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(";");
                modelo.addRow(campos); // Agrega los datos directamente
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
