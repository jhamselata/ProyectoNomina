package Utilidades;

import java.io.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class cargarDatosenTabla {

    // Método para cargar datos en una JTable desde un archivo .txt
    public static void cargarEnTabla(JTable tabla, String rutaArchivo) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0); // Borra solo las filas

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
    
    // Método para aplicar filtro dinámico por columna usando JTextField y JComboBox
    public static void activarFiltro(JTable tabla, JTextField campoTexto, JComboBox<String> comboColumnas) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);

        // Llenar el combo con nombres de columnas si está vacío
        if (comboColumnas.getItemCount() == 0) {
            for (int i = 0; i < tabla.getColumnCount(); i++) {
                comboColumnas.addItem(tabla.getColumnName(i));
            }
        }

        // Escuchar cambios en el campo de texto
        campoTexto.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                aplicarFiltro();
            }
            public void removeUpdate(DocumentEvent e) {
                aplicarFiltro();
            }
            public void changedUpdate(DocumentEvent e) {
                aplicarFiltro();
            }

            private void aplicarFiltro() {
                String texto = campoTexto.getText();
                int columnaSeleccionada = comboColumnas.getSelectedIndex();

                if (texto.trim().length() == 0) {
                    sorter.setRowFilter(null); //Mostrar todo si está vacío
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, columnaSeleccionada));
                }
            }
        });
    }
}
