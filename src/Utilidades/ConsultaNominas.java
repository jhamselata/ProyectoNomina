package Utilidades;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class ConsultaNominas {
    
    // Método para cargar datos de nóminas con información de empleados
    public static void cargarNominasConEmpleados(JTable tablaNominas, String rutaNominas, String rutaEmpleados) {
        DefaultTableModel modelo = (DefaultTableModel) tablaNominas.getModel();
        modelo.setRowCount(0); // Limpiar la tabla
        
        // Cargar empleados en un mapa para acceso rápido
        Map<String, String[]> empleados = cargarEmpleados(rutaEmpleados);
        
        try (BufferedReader br = new BufferedReader(new FileReader(rutaNominas))) {
            String linea;
            
            while ((linea = br.readLine()) != null) {
                String[] camposNomina = linea.split(";");
                
                if (camposNomina.length >= 10) { // Verificar que tenga todos los campos
                    String idEmpleado = camposNomina[1]; // ID Empleado está en posición 1
                    String[] datosEmpleado = empleados.get(idEmpleado);
                    
                    // Crear fila con datos combinados
                    Object[] fila = new Object[11];
                    fila[0] = camposNomina[0]; // ID Nómina
                    fila[1] = idEmpleado; // ID Empleado
                    
                    // Si encontramos el empleado, usar su nombre completo, sino "Empleado no encontrado"
                    if (datosEmpleado != null) {
                        String nombreCompleto = datosEmpleado[1] + " " + datosEmpleado[2] + " " + datosEmpleado[3];
                        fila[2] = nombreCompleto; // Nombre Completo
                    } else {
                        fila[2] = "Empleado no encontrado";
                    }
                    
                    fila[3] = camposNomina[2]; // Fecha Nómina
                    fila[4] = camposNomina[3]; // Salario
                    fila[5] = camposNomina[4]; // AFP
                    fila[6] = camposNomina[5]; // ARS
                    fila[7] = camposNomina[6]; // Cooperativa
                    fila[8] = camposNomina[7]; // ISR
                    fila[9] = camposNomina[8]; // Sueldo Neto
                    fila[10] = camposNomina[9]; // Status
                    
                    modelo.addRow(fila);
                }
            }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo de nóminas:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método auxiliar para cargar empleados en un mapa
    private static Map<String, String[]> cargarEmpleados(String rutaEmpleados) {
        Map<String, String[]> empleados = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(rutaEmpleados))) {
            String linea;
            
            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(";");
                if (campos.length >= 4) { // ID, Nombre, Apellido Paterno, Apellido Materno mínimo
                    empleados.put(campos[0], campos); // Usar ID como clave
                }
            }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo de empleados:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return empleados;
    }
    
    // Método para configurar filtros específicos de nóminas
    public static void configurarFiltrosNominas(JTable tabla, JTextField campoTexto, JComboBox<String> comboFiltro) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        
        // Configurar opciones de filtro específicas para nóminas
        comboFiltro.removeAllItems();
        comboFiltro.addItem("ID Nómina");
        comboFiltro.addItem("ID Empleado");
        comboFiltro.addItem("Nombre Empleado");
        comboFiltro.addItem("Fecha Nómina");
        
        // Listener para cambios en el campo de texto
        campoTexto.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { aplicarFiltro(); }
            public void removeUpdate(DocumentEvent e) { aplicarFiltro(); }
            public void changedUpdate(DocumentEvent e) { aplicarFiltro(); }
            
            private void aplicarFiltro() {
                String texto = campoTexto.getText().trim();
                String filtroSeleccionado = (String) comboFiltro.getSelectedItem();
                
                if (texto.length() == 0) {
                    sorter.setRowFilter(null);
                    return;
                }
                
                int columnaFiltro = getColumnaFiltro(filtroSeleccionado);
                
                if (filtroSeleccionado.equals("Fecha Nómina")) {
                    // Filtro especial para fechas - buscar coincidencias parciales
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, columnaFiltro));
                } else {
                    // Para otros campos, buscar que comience con el texto
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)^" + texto, columnaFiltro));
                }
            }
        });
        
        // Listener para cambios en el combo de filtro
        comboFiltro.addActionListener(e -> {
            campoTexto.setText(""); // Limpiar el campo cuando cambie el filtro
        });
    }
    
    // Método auxiliar para obtener el índice de columna según el filtro
    private static int getColumnaFiltro(String filtro) {
        switch (filtro) {
            case "ID Nómina": return 0;
            case "ID Empleado": return 1;
            case "Nombre Empleado": return 2;
            case "Fecha Nómina": return 3;
            default: return 0;
        }
    }
    
    // Método para validar formato de fecha en filtros
    public static boolean esFormatoFechaValido(String fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(fecha);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    
    // Método para formatear fecha para mostrar
    public static String formatearFecha(String fechaOriginal) {
        try {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = formatoEntrada.parse(fechaOriginal);
            return formatoSalida.format(fecha);
        } catch (ParseException e) {
            return fechaOriginal; // Devolver original si no se puede parsear
        }
    }
}