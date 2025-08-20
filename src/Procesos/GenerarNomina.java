/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Procesos;

import Utilidades.EstilosTabla;
import Utilidades.GenerarPDF;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jhams
 */
public class GenerarNomina extends javax.swing.JFrame {

    /**
     * Creates new form Departamentos
     */
    private File archivoEmpleados = new File("src/BaseDeDatos/Empleados.txt");
    private File archivoCooperativa = new File("src/BaseDeDatos/Cooperativa.txt");
    private File archivoNominas = new File("src/BaseDeDatos/Nominas.txt");

    // Porcentajes para cálculos
    private static final double PORCENTAJE_ARS = 3.04;
    private static final double PORCENTAJE_AFP = 2.87;

    // Rangos
    private static final double ISR_RANGO1 = 34685.00;
    private static final double ISR_RANGO2 = 52027.42;
    private static final double ISR_RANGO3 = 72260.25;
    private static final double ISR_BASE2 = 2601.33;
    private static final double ISR_BASE3 = 6648.00;

    private DecimalFormat formatoMoneda = new DecimalFormat("#,##0.00");
    private Map<String, CooperativaInfo> cooperativaMap = new HashMap<>();

    // Clase interna para manejar información de cooperativa
    private static class CooperativaInfo {

        double porcentaje;
        double balanceAcumulado;

        CooperativaInfo(double porcentaje, double balanceAcumulado) {
            this.porcentaje = porcentaje;
            this.balanceAcumulado = balanceAcumulado;
        }
    }
    
    private boolean validarFechaNomina(Date fecha) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(fecha);
    
    int dia = cal.get(Calendar.DAY_OF_MONTH);
    int mes = cal.get(Calendar.MONTH) + 1; // Enero es 0
    
    // Para febrero (mes 2)
    if (mes == 2) {
        int anio = cal.get(Calendar.YEAR);
        // Verificar si es año bisiesto
        boolean esBisiesto = (anio % 4 == 0 && anio % 100 != 0) || (anio % 400 == 0);
        return dia == 28 || (esBisiesto && dia == 29);
    }
    // Para meses con 30 días
    else if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
        return dia == 30;
    }
    // Para otros meses (31 días)
    else {
        return dia == 30; // O podrías cambiar esto a dia == 31 si prefieres
    }
}

    // Clase para representar un empleado
    private static class Empleado {

        String idEmp;
        String nombre;
        String apellidoPaterno;
        String apellidoMaterno;
        String direccion;
        String telefono;
        String sexo;
        String idDpto;
        String fechaIngreso;
        String idPuesto;
        double salario;
        boolean tieneCoop;

        Empleado(String idEmp, String nombre, String apellidoPaterno, String apellidoMaterno,
                String direccion, String telefono, String sexo, String idDpto, String fechaIngreso,
                String idPuesto, double salario, boolean tieneCoop) {
            this.idEmp = idEmp;
            this.nombre = nombre;
            this.apellidoPaterno = apellidoPaterno;
            this.apellidoMaterno = apellidoMaterno;
            this.direccion = direccion;
            this.telefono = telefono;
            this.sexo = sexo;
            this.idDpto = idDpto;
            this.fechaIngreso = fechaIngreso;
            this.idPuesto = idPuesto;
            this.salario = salario;
            this.tieneCoop = tieneCoop;
        }

        String getNombreCompleto() {
            return nombre + " " + apellidoPaterno + " " + apellidoMaterno;
        }
    }

    public GenerarNomina() {
        initComponents();
        
        ImageIcon icono = new ImageIcon(getClass().getResource("/Iconos/ProgramIcon.png"));
        this.setIconImage(icono.getImage());
        
        setLocationRelativeTo(null); // Centra la ventana en la pantalla.
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30)); //Redondea Bordes de la ventana Jframe
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Permite que solo se cierre la ventana 

        txtEstado.setBackground(new Color(0, 0, 0, 0));
        
        btnSalir.setBackground(new Color(0, 0, 0, 0));
        btnProcesar.setBackground(new Color(0, 0, 0, 0));


        // Configurar fecha por defecto (día 30 del mes actual)
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 30); // Establecer el día 30
        jdcFecha.setCalendar(cal);

        // Configurar tabla para nóminas
        configurarTablaNominas();
        cargarCooperativa();

        // Cargar nómina inicial con la fecha por defecto
        cargarNominaEnTabla();
        
        //Seleccionar fecha minima
        jdcFecha.setMinSelectableDate(new Date());

        configurarEstilosUI();
    }
    
    private void configurarEstilosUI() {
    // Ajuste proporcional automático (por defecto)
    EstilosTabla.aplicarEstiloPrincipal(TableNominas, ScrollPaneDepartamentos);
}

    /**
     * Configura la tabla para mostrar nóminas
     */
    private void configurarTablaNominas() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Nómina");
        modelo.addColumn("ID Empleado");
        modelo.addColumn("Nombre Completo");
        modelo.addColumn("Fecha");
        modelo.addColumn("Salario");
        modelo.addColumn("AFP");
        modelo.addColumn("ARS");
        modelo.addColumn("Cooperativa");
        modelo.addColumn("ISR");
        modelo.addColumn("Sueldo Neto");
        modelo.addColumn("Status");

        TableNominas.setModel(modelo);
        txtEstado.setText("Seleccione fecha para generar nómina");
    }

    /**
     * Carga la información de cooperativa desde archivo
     */
    private void cargarCooperativa() {
        cooperativaMap.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCooperativa))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    String idEmp = partes[0].trim();
                    double porcentaje = Double.parseDouble(partes[1].trim());
                    double balance = Double.parseDouble(partes[2].trim());
                    cooperativaMap.put(idEmp, new CooperativaInfo(porcentaje, balance));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Advertencia: No se pudo cargar archivo de cooperativa: " + e.getMessage());
        }
    }

    /**
     * Actualiza el archivo de cooperativa con nuevos balances
     */
    private void actualizarCooperativa() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivoCooperativa))) {
            for (Map.Entry<String, CooperativaInfo> entry : cooperativaMap.entrySet()) {
                CooperativaInfo info = entry.getValue();
                pw.println(entry.getKey() + ";" + info.porcentaje + ";"
                        + formatoMoneda.format(info.balanceAcumulado).replace(",", ""));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar cooperativa: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga todos los empleados
     */
    private List<Empleado> cargarTodosLosEmpleados() {
        List<Empleado> empleados = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivoEmpleados))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                // Formato: Id_empleado;Nombre;ApellidoPaterno;ApellidoMaterno;Direccion;Telefono;Sexo;IdDpto;FechaIngreso;IdPuesto;Coop(No/Si);Salario
                if (partes.length >= 12) {
                    String idEmp = partes[0].trim();
                    String nombre = partes[1].trim();
                    String apellidoPaterno = partes[2].trim();
                    String apellidoMaterno = partes[3].trim();
                    String direccion = partes[4].trim();
                    String telefono = partes[5].trim();
                    String sexo = partes[6].trim();
                    String idDpto = partes[7].trim();
                    String fechaIngreso = partes[8].trim();
                    String idPuesto = partes[9].trim();
                    boolean tieneCoop = partes[10].trim().equalsIgnoreCase("Si");
                    double salario = Double.parseDouble(partes[11].trim());

                    empleados.add(new Empleado(idEmp, nombre, apellidoPaterno, apellidoMaterno,
                            direccion, telefono, sexo, idDpto, fechaIngreso,
                            idPuesto, salario, tieneCoop));
                }
            }
        } catch (IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar empleados: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        return empleados;
    }

    /**
     * Calcula el ISR según los rangos establecidos
     */
    private double calcularISR(double baseCalculo) {
        if (baseCalculo <= ISR_RANGO1) {
            return 0.0;
        } else if (baseCalculo <= ISR_RANGO2) {
            return (baseCalculo - ISR_RANGO1) * 0.15;
        } else if (baseCalculo <= ISR_RANGO3) {
            return ISR_BASE2 + ((baseCalculo - ISR_RANGO2) * 0.20);
        } else {
            return ISR_BASE3 + ((baseCalculo - ISR_RANGO3) * 0.25);
        }
    }

    /**
     * Genera el próximo ID secuencial para la nómina
     */
    private int obtenerProximoIdNomina() {
        int maxId = 0;

        // Leer el archivo de nóminas para encontrar el ID más alto
        if (archivoNominas.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivoNominas))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(";");
                    if (partes.length > 0) {
                        try {
                            int id = Integer.parseInt(partes[0].trim());
                            if (id > maxId) {
                                maxId = id;
                            }
                        } catch (NumberFormatException e) {
                            // Ignorar líneas con formato incorrecto
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al leer archivo de nóminas para obtener próximo ID: " + e.getMessage());
            }
        }

        return maxId + 1;
    }

    /**
     * Carga y muestra la nómina en la tabla cuando se selecciona una fecha
     */
    private void cargarNominaEnTabla() {
    // Validar fecha
    if (jdcFecha.getDate() == null) {
        txtEstado.setText("Seleccione una fecha válida");
        return;
    }

    // Usar la fecha exacta seleccionada en el JDateChooser
    Calendar cal = Calendar.getInstance();
    cal.setTime(jdcFecha.getDate());
    Date fechaNomina = cal.getTime();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String fechaStr = sdf.format(fechaNomina);

    // Cargar todos los empleados
    List<Empleado> empleados = cargarTodosLosEmpleados();

    if (empleados.isEmpty()) {
        txtEstado.setText("No se encontraron empleados");
        return;
    }

    // Limpiar tabla anterior
    DefaultTableModel modelo = (DefaultTableModel) TableNominas.getModel();
    modelo.setRowCount(0);

    double totalNomina = 0;
    // Obtener un solo ID para toda la nómina (grupo)
    int idNominaGrupo = obtenerProximoIdNomina();

    // Procesar cada empleado y mostrar en tabla
    for (Empleado emp : empleados) {
        // Usar el mismo ID de nómina para todos los empleados
        String idNomina = String.valueOf(idNominaGrupo);

        // Cálculos
        double salario = emp.salario;
        double afp = salario * (PORCENTAJE_AFP / 100);
        double ars = salario * (PORCENTAJE_ARS / 100);

        // Cooperativa - solo si el empleado participa
        double cooperativa = 0;
        if (emp.tieneCoop && cooperativaMap.containsKey(emp.idEmp)) {
            CooperativaInfo info = cooperativaMap.get(emp.idEmp);
            cooperativa = salario * (info.porcentaje / 100);
        }

        // ISR (base: salario - AFP - ARS)
        double baseISR = salario - afp - ars;
        double isr = calcularISR(baseISR);

        // Sueldo neto
        double sueldoNeto = salario - afp - ars - cooperativa - isr;
        totalNomina += sueldoNeto;

        // Agregar a la tabla con la fecha seleccionada
        Object[] fila = {
            idNomina,
            emp.idEmp,
            emp.getNombreCompleto(),
            fechaStr,
            "RD$ " + formatoMoneda.format(salario),
            "RD$ " + formatoMoneda.format(afp),
            "RD$ " + formatoMoneda.format(ars),
            "RD$ " + formatoMoneda.format(cooperativa),
            "RD$ " + formatoMoneda.format(isr),
            "RD$ " + formatoMoneda.format(sueldoNeto),
            "True"
        };
        modelo.addRow(fila);
    }

    // Actualizar estado con información de la fecha seleccionada
    SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd/MM/yyyy");
    txtEstado.setText("Nómina para " + sdfDisplay.format(fechaNomina) + ": "
            + empleados.size() + " empleados. Total: RD$ "
            + formatoMoneda.format(totalNomina));
}

   private void procesarNomina() {
       
       if (!validarFechaNomina(jdcFecha.getDate())) {
        JOptionPane.showMessageDialog(this,
            "La nómina solo puede generarse los días:\n" +
            "- 30 de cada mes\n" +
            "- 28 de febrero (29 en años bisiestos)",
            "Fecha no válida para nómina",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
       
    DefaultTableModel modelo = (DefaultTableModel) TableNominas.getModel();

    // Validar que haya datos en la tabla
    if (modelo.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this,
                "No hay nómina para procesar. Seleccione una fecha primero.",
                "Sin datos", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Obtener mes y año de la fecha seleccionada
    if (jdcFecha.getDate() == null) {
        JOptionPane.showMessageDialog(this,
                "Seleccione una fecha válida antes de procesar.",
                "Fecha no válida", JOptionPane.WARNING_MESSAGE);
        return;
    }
    Calendar calSel = Calendar.getInstance();
    calSel.setTime(jdcFecha.getDate());
    int mesSel = calSel.get(Calendar.MONTH); // 0 = Enero
    int anioSel = calSel.get(Calendar.YEAR);

    // Verificar si ya existe nómina para ese mes y año
    if (archivoNominas.exists()) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivoNominas))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 3) { // Aseguramos que tiene fecha
                    String fechaRegistro = partes[2].trim(); // yyyy-MM-dd
                    try {
                        Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaRegistro);
                        Calendar calReg = Calendar.getInstance();
                        calReg.setTime(fecha);

                        int mesReg = calReg.get(Calendar.MONTH);
                        int anioReg = calReg.get(Calendar.YEAR);

                        if (mesReg == mesSel && anioReg == anioSel) {
                            JOptionPane.showMessageDialog(this,
                                    "Ya existe una nómina registrada para este mes y año.\n"
                                    + "No se puede procesar nuevamente.",
                                    "Nómina Duplicada", JOptionPane.ERROR_MESSAGE);
                            return; // Cancelar el procesamiento
                        }
                    } catch (Exception e) {
                        // Si hay error de formato de fecha, lo ignoramos y seguimos
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al verificar nóminas existentes: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    // Si llegamos aquí, significa que no existe nómina en ese mes y año
    // Confirmar procesamiento
    int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de procesar y guardar esta nómina?\n"
            + "Se guardarán " + modelo.getRowCount() + " registros.",
            "Confirmar Procesamiento",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

    if (respuesta != JOptionPane.YES_OPTION) {
        return;
    }

    // ---- Guardar la nómina ----
    List<String> registrosNomina = new ArrayList<>();
    double totalNomina = 0;

    // Obtener el ID de nómina de la primera fila (todos tienen el mismo)
    String idNominaGrupo = modelo.getValueAt(0, 0).toString();

    for (int i = 0; i < modelo.getRowCount(); i++) {
        // Usar el mismo ID de nómina para todos los empleados
        String idNomina = idNominaGrupo;
        String idEmpleado = modelo.getValueAt(i, 1).toString();
        String fecha = modelo.getValueAt(i, 3).toString();

        String salarioStr = modelo.getValueAt(i, 4).toString().replace("RD$ ", "").replace(",", "");
        String afpStr = modelo.getValueAt(i, 5).toString().replace("RD$ ", "").replace(",", "");
        String arsStr = modelo.getValueAt(i, 6).toString().replace("RD$ ", "").replace(",", "");
        String cooperativaStr = modelo.getValueAt(i, 7).toString().replace("RD$ ", "").replace(",", "");
        String isrStr = modelo.getValueAt(i, 8).toString().replace("RD$ ", "").replace(",", "");
        String sueldoNetoStr = modelo.getValueAt(i, 9).toString().replace("RD$ ", "").replace(",", "");
        String status = modelo.getValueAt(i, 10).toString();

        String registro = String.join(";",
                idNomina, idEmpleado, fecha,
                salarioStr, afpStr, arsStr, cooperativaStr, isrStr, sueldoNetoStr, status);
        registrosNomina.add(registro);

        try {
            totalNomina += Double.parseDouble(sueldoNetoStr);
        } catch (NumberFormatException e) {
            System.out.println("Error al sumar sueldo neto: " + e.getMessage());
        }

        if (!cooperativaStr.equals("0.00") && cooperativaMap.containsKey(idEmpleado)) {
            try {
                double montoCooperativa = Double.parseDouble(cooperativaStr);
                CooperativaInfo info = cooperativaMap.get(idEmpleado);
                info.balanceAcumulado += montoCooperativa;
            } catch (NumberFormatException e) {
                System.out.println("Error al actualizar cooperativa: " + e.getMessage());
            }
        }
    }

    try (PrintWriter pw = new PrintWriter(new FileWriter(archivoNominas, true))) {
        for (String registro : registrosNomina) {
            pw.println(registro);
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this,
                "Error al guardar nómina: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    actualizarCooperativa();

    txtEstado.setText("¡Nómina procesada! " + modelo.getRowCount() + " empleados. Total: RD$ "
            + formatoMoneda.format(totalNomina));

    JOptionPane.showMessageDialog(this,
            "¡Nómina procesada y guardada exitosamente!\n"
            + "Empleados procesados: " + modelo.getRowCount() + "\n"
            + "Total nómina: RD$ " + formatoMoneda.format(totalNomina),
            "Nómina Procesada", JOptionPane.INFORMATION_MESSAGE);
    
    try {
    String rutaNominas = "src/BaseDeDatos/Nominas.txt";
    String rutaEmpleados = "src/BaseDeDatos/Empleados.txt";
    String rutaSalida = "src/VolantesDeNómina/" + new SimpleDateFormat("MM-yyyy").format(jdcFecha.getDate()) + "-Nómina" + ".pdf";
    
    // Formatear la fecha seleccionada para mostrar en el PDF
    SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd/MM/yyyy");
    String fechaParaPDF = sdfDisplay.format(jdcFecha.getDate());
    
    GenerarPDF.generarPDF(rutaNominas, rutaEmpleados, rutaSalida, fechaParaPDF);
    
    JOptionPane.showMessageDialog(this, 
            "PDF generado exitosamente en: " + rutaSalida,
            "PDF Generado", JOptionPane.INFORMATION_MESSAGE);
} catch (IOException e) {
    JOptionPane.showMessageDialog(this,
            "Error al generar PDF: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
}
    
    
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelesBordesRedondeados1 = new Utilidades.PanelesBordesRedondeados();
        lblBuscador = new javax.swing.JLabel();
        lblBtnProcesar = new Utilidades.PanelesBordesRedondeados();
        btnProcesar = new javax.swing.JButton();
        lblEstado = new javax.swing.JLabel();
        txtEstado = new javax.swing.JTextField();
        ScrollPaneDepartamentos = new javax.swing.JScrollPane();
        TableNominas = new javax.swing.JTable();
        jdcFecha = new com.toedter.calendar.JDateChooser();
        lblBtnSalir = new Utilidades.PanelesBordesRedondeados();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        panelesBordesRedondeados1.setBackground(new java.awt.Color(76, 86, 106));
        panelesBordesRedondeados1.setEnabled(false);
        panelesBordesRedondeados1.setFocusable(false);
        panelesBordesRedondeados1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblBuscador.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        lblBuscador.setForeground(new java.awt.Color(236, 239, 244));
        lblBuscador.setText("Fecha");
        panelesBordesRedondeados1.add(lblBuscador, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        lblBtnProcesar.setBackground(new java.awt.Color(59, 66, 82));
        lblBtnProcesar.setPreferredSize(new java.awt.Dimension(132, 40));
        lblBtnProcesar.setRoundBottomLeft(20);
        lblBtnProcesar.setRoundBottomRight(20);
        lblBtnProcesar.setRoundTopLeft(20);
        lblBtnProcesar.setRoundTopRight(20);

        btnProcesar.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        btnProcesar.setForeground(new java.awt.Color(204, 204, 204));
        btnProcesar.setText("Procesar");
        btnProcesar.setBorder(null);
        btnProcesar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProcesar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnProcesarMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnProcesarMousePressed(evt);
            }
        });
        btnProcesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcesarActionPerformed(evt);
            }
        });
        btnProcesar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                btnProcesarKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout lblBtnProcesarLayout = new javax.swing.GroupLayout(lblBtnProcesar);
        lblBtnProcesar.setLayout(lblBtnProcesarLayout);
        lblBtnProcesarLayout.setHorizontalGroup(
            lblBtnProcesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblBtnProcesarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnProcesar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        lblBtnProcesarLayout.setVerticalGroup(
            lblBtnProcesarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblBtnProcesarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnProcesar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelesBordesRedondeados1.add(lblBtnProcesar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 520, -1, -1));

        lblEstado.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        lblEstado.setForeground(new java.awt.Color(236, 239, 244));
        lblEstado.setText("Estado:");
        panelesBordesRedondeados1.add(lblEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 60, -1));

        txtEstado.setEditable(false);
        txtEstado.setBackground(new java.awt.Color(255, 204, 102));
        txtEstado.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        txtEstado.setForeground(new java.awt.Color(236, 239, 244));
        txtEstado.setText("Buscando");
        txtEstado.setBorder(null);
        txtEstado.setEnabled(false);
        txtEstado.setMargin(new java.awt.Insets(10, 10, 10, 10));
        txtEstado.setName(""); // NOI18N
        txtEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEstadoActionPerformed(evt);
            }
        });
        txtEstado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEstadoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEstadoKeyTyped(evt);
            }
        });
        panelesBordesRedondeados1.add(txtEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 740, 40));

        TableNominas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        ScrollPaneDepartamentos.setViewportView(TableNominas);

        panelesBordesRedondeados1.add(ScrollPaneDepartamentos, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 910, 370));

        jdcFecha.setFont(new java.awt.Font("Noto Sans", 0, 12)); // NOI18N
        jdcFecha.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdcFechaPropertyChange(evt);
            }
        });
        panelesBordesRedondeados1.add(jdcFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 280, 40));

        lblBtnSalir.setBackground(new java.awt.Color(59, 66, 82));
        lblBtnSalir.setPreferredSize(new java.awt.Dimension(132, 40));
        lblBtnSalir.setRoundBottomLeft(20);
        lblBtnSalir.setRoundBottomRight(20);
        lblBtnSalir.setRoundTopLeft(20);
        lblBtnSalir.setRoundTopRight(20);

        btnSalir.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        btnSalir.setForeground(new java.awt.Color(204, 204, 204));
        btnSalir.setText("Salir");
        btnSalir.setBorder(null);
        btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalirMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnSalirMousePressed(evt);
            }
        });
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        btnSalir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                btnSalirKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout lblBtnSalirLayout = new javax.swing.GroupLayout(lblBtnSalir);
        lblBtnSalir.setLayout(lblBtnSalirLayout);
        lblBtnSalirLayout.setHorizontalGroup(
            lblBtnSalirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblBtnSalirLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        lblBtnSalirLayout.setVerticalGroup(
            lblBtnSalirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblBtnSalirLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelesBordesRedondeados1.add(lblBtnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 520, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelesBordesRedondeados1, javax.swing.GroupLayout.DEFAULT_SIZE, 991, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelesBordesRedondeados1, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProcesarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProcesarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnProcesarMouseClicked

    private void btnProcesarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnProcesarMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnProcesarMousePressed

    private void btnProcesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcesarActionPerformed
        // TODO add your handling code here:
        procesarNomina();
    }//GEN-LAST:event_btnProcesarActionPerformed

    private void btnProcesarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnProcesarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_btnProcesarKeyTyped

    private void txtEstadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEstadoKeyTyped

    }//GEN-LAST:event_txtEstadoKeyTyped

    private void txtEstadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEstadoKeyPressed

    }//GEN-LAST:event_txtEstadoKeyPressed

    private void txtEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEstadoActionPerformed

    private void btnSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalirMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalirMouseClicked

    private void btnSalirMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalirMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalirMousePressed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnSalirKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSalirKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalirKeyTyped

    private void jdcFechaPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdcFechaPropertyChange
        // TODO add your handling code here:
        if ("date".equals(evt.getPropertyName()) && evt.getNewValue() != null) {
            // Cargar la nómina con la nueva fecha seleccionada
            cargarNominaEnTabla();
        }
    }//GEN-LAST:event_jdcFechaPropertyChange

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GenerarNomina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GenerarNomina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GenerarNomina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GenerarNomina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GenerarNomina().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane ScrollPaneDepartamentos;
    private javax.swing.JTable TableNominas;
    private javax.swing.JButton btnProcesar;
    private javax.swing.JButton btnSalir;
    private com.toedter.calendar.JDateChooser jdcFecha;
    private Utilidades.PanelesBordesRedondeados lblBtnProcesar;
    private Utilidades.PanelesBordesRedondeados lblBtnSalir;
    private javax.swing.JLabel lblBuscador;
    private javax.swing.JLabel lblEstado;
    private Utilidades.PanelesBordesRedondeados panelesBordesRedondeados1;
    private javax.swing.JTextField txtEstado;
    // End of variables declaration//GEN-END:variables
}
