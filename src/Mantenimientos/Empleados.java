/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Mantenimientos;

import ManejoDeArchivos.ManejoArchivos;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Duanel
 */

public class Empleados extends javax.swing.JFrame {

    /**
     * Creates new form Departamentos
     */
    private static final String ARCHIVO_EMPLEADOS = "src/BaseDeDatos/Empleados.txt";
    private static final String ARCHIVO_DEPARTAMENTOS = "src/BaseDeDatos/Departamentos.txt";
    private static final String ARCHIVO_PUESTOS = "src/BaseDeDatos/Puestos.txt";
    private static final String ESTADO_CREANDO = "Creando";
    private static final String ESTADO_MODIFICANDO = "Modificando";

    // Variables de control
    private boolean encontrado = false;
    private String cadenaAnterior = "";
    private File archivo = new File(ARCHIVO_EMPLEADOS);
    private boolean cooperativaConfiguracionCompletada = false;

    public Empleados() {
        initComponents();
        configurarVentana();
        configurarCamposTransparentes();
        cargarComboBoxes();
        configurarValidaciones();
        configurarListenersCooperativa();

        // Ocultar los botones al inicio
        btnRegistrar.setVisible(false);
        btnEliminar.setVisible(false);
        lblBotonRegistrar.setVisible(false);
        lblBotonEliminar.setVisible(false);
        txtFechaIngreEmpleado.setEditable(false);

        establecerFechaActual();
        cooperativaConfiguracionCompletada = false; 
    }

    /**
     * Configura la apariencia de la ventana
     */
    private boolean validarCambioCooperativa() {
        // Si está marcando "No" en cooperativa, verificar balance
        if (rbtnNo.isSelected()) {
            String idEmpleado = txtIDEmpleado.getText().trim();
            if (!idEmpleado.isEmpty()) {
                BigDecimal balance = CooperativaDialog.obtenerBalanceEmpleado(idEmpleado);
                if (balance.compareTo(BigDecimal.ZERO) > 0) {
                    JOptionPane.showMessageDialog(this,
                            String.format("No se puede quitar al empleado de la cooperativa.\n"
                                    + "Tiene un balance acumulado de $%.2f", balance.doubleValue()),
                            "Operación no permitida", JOptionPane.WARNING_MESSAGE);
                    rbtnSi.setSelected(true); // Forzar a "Si"
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Abre la ventana de gestión de cooperativa
     */
    private void gestionarCooperativa() {
        // Validar que haya salario ingresado
       String salarioText = txtSalario.getText().trim();
        if (salarioText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar un salario antes de configurar la cooperativa.",
                    "Salario requerido", JOptionPane.WARNING_MESSAGE);
            txtSalario.requestFocus();
            rbtnNo.setSelected(true); // Si no hay salario, no puede ser de cooperativa
            cooperativaConfiguracionCompletada = false; // Resetear la bandera
            return;
        }

        try {
            BigDecimal salario = new BigDecimal(salarioText);
            String idEmpleado = txtIDEmpleado.getText().trim();

            CooperativaDialog dialog = new CooperativaDialog(this, idEmpleado, salario);
            dialog.setVisible(true);

            // Si no completó el proceso, desmarcar cooperativa
            if (!dialog.isProcesoCompletado()) {
                rbtnNo.setSelected(true);
                JOptionPane.showMessageDialog(this,
                        "Se canceló el proceso de cooperativa. El empleado se guardará sin cooperativa.",
                        "Proceso cancelado", JOptionPane.INFORMATION_MESSAGE);
                cooperativaConfiguracionCompletada = false; // El proceso no se completó
            } else {
                cooperativaConfiguracionCompletada = true; // El proceso se completó exitosamente
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El salario debe ser un número válido para configurar la cooperativa.",
                    "Salario inválido", JOptionPane.ERROR_MESSAGE);
            txtSalario.requestFocus();
            rbtnNo.setSelected(true); // Si el salario es inválido, no puede ser de cooperativa
            cooperativaConfiguracionCompletada = false; // Resetear la bandera
        }
    }

    private void configurarVentana() {
        setLocationRelativeTo(null);
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    /////////////////////////////////////////////////////////////////////////////

    private void configurarListenersCooperativa() {
        rbtnSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnSiActionPerformed(evt);
            }
        });

        rbtnNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnNoActionPerformed(evt);
            }
        });
    }

    /**
     * Configura los campos con fondo transparente
     */
    private void configurarCamposTransparentes() {
        Color transparente = new Color(0, 0, 0, 0);
        txtIDEmpleado.setBackground(transparente);
        txtNomEmpleado.setBackground(transparente);
        txtApePatEmpleado.setBackground(transparente);
        txtApeMatEmpleado.setBackground(transparente);
        txtDirecEmpleado.setBackground(transparente);
        txtTelEmpleado.setBackground(transparente);
        txtFechaIngreEmpleado.setBackground(transparente);
        txtSalario.setBackground(transparente);
        txtEstado.setBackground(transparente);
    }
//////////

    /**
     * Carga los datos en los ComboBox desde archivos
     */
    private void cargarComboBoxes() {
        cargarDepartamentos();
        cargarPuestos();
    }

    /**
     * Carga departamentos desde archivo
     */
    private void cargarDepartamentos() {
        cmbDepEmpleado.removeAllItems();
        cmbDepEmpleado.addItem("-- Seleccione --");

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_DEPARTAMENTOS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 2) {
                    cmbDepEmpleado.addItem(partes[0] + " - " + partes[1]);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar departamentos: " + e.getMessage());
        }
    }

    /**
     * Carga puestos desde archivo
     */
    private void cargarPuestos() {
        cmbPuestoEmpleado.removeAllItems();
        cmbPuestoEmpleado.addItem("-- Seleccione --");

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_PUESTOS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 2) {
                    cmbPuestoEmpleado.addItem(partes[0] + " - " + partes[1]);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar puestos: " + e.getMessage());
        }
    }

    /**
     * Configura las validaciones de los campos
     */
    private void configurarValidaciones() {
        // Inicialmente deshabilitar campos hasta que se ingrese ID
        habilitarCampos(false);
    }

    /**
     * Busca un empleado por ID en el archivo
     *
     * @param empleadoBuscado ID del empleado a buscar
     * @return línea encontrada o null si no existe
     */
    public String buscarEmpleado(String empleadoBuscado) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 1 && partes[0].equalsIgnoreCase(empleadoBuscado.trim())) {
                    return linea;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el empleado: " + e.getMessage());
        }
        return null;
    }

    /**
     * Habilita o deshabilita los campos del formulario
     *
     * @param habilitar true para habilitar, false para deshabilitar
     */
    private void habilitarCampos(boolean habilitar) {
        txtNomEmpleado.setEnabled(habilitar);
        txtApePatEmpleado.setEnabled(habilitar);
        txtApeMatEmpleado.setEnabled(habilitar);
        txtDirecEmpleado.setEnabled(habilitar);
        txtTelEmpleado.setEnabled(habilitar);
        rbtnMasculino.setEnabled(habilitar);
        rbtnFemenino.setEnabled(habilitar);
        cmbDepEmpleado.setEnabled(habilitar);
        txtFechaIngreEmpleado.setEnabled(habilitar);
        cmbPuestoEmpleado.setEnabled(habilitar);
        rbtnSi.setEnabled(habilitar);
        rbtnNo.setEnabled(habilitar);
        txtSalario.setEnabled(habilitar);
    }

    /**
     * Limpia todos los campos del formulario
     */
    private void limpiarCampos() {
        txtIDEmpleado.setText("");
        txtNomEmpleado.setText("");
        txtApePatEmpleado.setText("");
        txtApeMatEmpleado.setText("");
        txtDirecEmpleado.setText("");
        txtTelEmpleado.setText("");
        txtSalario.setText("");

        SexoGroup.clearSelection();
        CooperativaGroup.clearSelection();
        cmbDepEmpleado.setSelectedIndex(0);
        cmbPuestoEmpleado.setSelectedIndex(0);

        txtEstado.setText(ESTADO_CREANDO);
        habilitarCampos(false);

        encontrado = false;
        cadenaAnterior = "";

        establecerFechaActual();
        
        // ¡Importante! Reiniciar la bandera cuando se limpian los campos para un nuevo registro
        cooperativaConfiguracionCompletada = false; 
    }

    private void establecerFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = sdf.format(new Date());
        txtFechaIngreEmpleado.setText(fechaActual);
    }

    /**
     * Carga los datos de un empleado en el formulario
     *
     * @param datos array con los datos del empleado
     */
    private void cargarDatosEmpleado(String[] datos) {
        if (datos.length >= 12) {
            txtNomEmpleado.setText(datos[1]);
            txtApePatEmpleado.setText(datos[2]);
            txtApeMatEmpleado.setText(datos[3]);
            txtDirecEmpleado.setText(datos[4]);
            txtTelEmpleado.setText(datos[5]);

            // Sexo
            if ("M".equals(datos[6])) {
                rbtnMasculino.setSelected(true);
            } else if ("F".equals(datos[6])) {
                rbtnFemenino.setSelected(true);
            }

            // Departamento - buscar y seleccionar
            seleccionarComboBoxPorId(cmbDepEmpleado, datos[7]);

            txtFechaIngreEmpleado.setText(datos[8]);

            // Puesto - buscar y seleccionar
            seleccionarComboBoxPorId(cmbPuestoEmpleado, datos[9]);

            // Cooperativa
            if ("Si".equals(datos[10])) {
                rbtnSi.setSelected(true);
                // Si el empleado ya tiene cooperativa registrada, se asume que la configuración está completa
                cooperativaConfiguracionCompletada = true; 
            } else if ("No".equals(datos[10])) {
                rbtnNo.setSelected(true);
                cooperativaConfiguracionCompletada = false;
            }

            txtSalario.setText(datos[11]);
        }
    }

    /**
     * Selecciona un item en el ComboBox basado en el ID
     */
    private void seleccionarComboBoxPorId(javax.swing.JComboBox<String> combo, String id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            String item = combo.getItemAt(i);
            if (item.startsWith(id + " - ")) {
                combo.setSelectedIndex(i);
                break;
            }
        }
    }

    private boolean validarCampos() {
        // Validar campos vacíos
        if (txtIDEmpleado.getText().trim().isEmpty()
                || txtNomEmpleado.getText().trim().isEmpty()
                || txtApePatEmpleado.getText().trim().isEmpty()
                || txtApeMatEmpleado.getText().trim().isEmpty()
                || txtDirecEmpleado.getText().trim().isEmpty()
                || txtTelEmpleado.getText().trim().isEmpty()
                || txtFechaIngreEmpleado.getText().trim().isEmpty()
                || txtSalario.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return false;
        }

        // Validar ID (solo números)
        if (!txtIDEmpleado.getText().trim().matches("^\\d+$")) {
            JOptionPane.showMessageDialog(this, "El ID solo debe contener números.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            txtIDEmpleado.requestFocus();
            return false;
        }

        // Validar nombres (solo letras, espacios y guiones)
        String patronNombres = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s-]+$";

        if (!txtNomEmpleado.getText().trim().matches(patronNombres)) {
            JOptionPane.showMessageDialog(this, "El nombre solo debe contener letras, espacios o guiones.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            txtNomEmpleado.requestFocus();
            return false;
        }

        if (!txtApePatEmpleado.getText().trim().matches(patronNombres)) {
            JOptionPane.showMessageDialog(this, "El apellido paterno solo debe contener letras, espacios o guiones.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            txtApePatEmpleado.requestFocus();
            return false;
        }

        if (!txtApeMatEmpleado.getText().trim().matches(patronNombres)) {
            JOptionPane.showMessageDialog(this, "El apellido materno solo debe contener letras, espacios o guiones.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            txtApeMatEmpleado.requestFocus();
            return false;
        }

        // Validar teléfono (números, guiones y espacios)
        if (!txtTelEmpleado.getText().trim().matches("^[0-9\\s-]+$")) {
            JOptionPane.showMessageDialog(this, "El teléfono solo debe contener números, espacios y guiones.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            txtTelEmpleado.requestFocus();
            return false;
        }

        // Validar fecha
        if (!validarFecha(txtFechaIngreEmpleado.getText().trim())) {
            JOptionPane.showMessageDialog(this, "La fecha debe tener el formato DD/MM/YYYY.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            txtFechaIngreEmpleado.requestFocus();
            return false;
        }

        // Validar salario (solo números y punto decimal)
        if (!txtSalario.getText().trim().matches("^\\d+(\\.\\d{1,2})?$")) {
            JOptionPane.showMessageDialog(this, "El salario debe ser un número válido (puede incluir decimales).",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            txtSalario.requestFocus();
            return false;
        }

        // Validar selecciones
        if (!rbtnMasculino.isSelected() && !rbtnFemenino.isSelected()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar el sexo del empleado.");
            return false;
        }

        if (cmbDepEmpleado.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un departamento.");
            return false;
        }

        if (cmbPuestoEmpleado.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un puesto.");
            return false;
        }

        if (!rbtnSi.isSelected() && !rbtnNo.isSelected()) {
            JOptionPane.showMessageDialog(this, "Debe indicar si pertenece a la cooperativa.");
            return false;
        }

        return true;
    }

    /**
     * Valida que la fecha tenga el formato correcto DD/MM/YYYY
     */
    private boolean validarFecha(String fecha) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            Date fechaParseada = sdf.parse(fecha);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Construye la línea de datos del empleado para guardar
     */
    private String construirLineaEmpleado() {
        StringBuilder linea = new StringBuilder();

        linea.append(txtIDEmpleado.getText().trim()).append(";");
        linea.append(txtNomEmpleado.getText().trim()).append(";");
        linea.append(txtApePatEmpleado.getText().trim()).append(";");
        linea.append(txtApeMatEmpleado.getText().trim()).append(";");
        linea.append(txtDirecEmpleado.getText().trim()).append(";");
        linea.append(txtTelEmpleado.getText().trim()).append(";");

        // Sexo
        linea.append(rbtnMasculino.isSelected() ? "M" : "F").append(";");

        // Departamento (extraer ID)
        String depSeleccionado = (String) cmbDepEmpleado.getSelectedItem();
        String idDep = depSeleccionado.split(" - ")[0];
        linea.append(idDep).append(";");

        linea.append(txtFechaIngreEmpleado.getText().trim()).append(";");

        // Puesto (extraer ID)
        String puestoSeleccionado = (String) cmbPuestoEmpleado.getSelectedItem();
        String idPuesto = puestoSeleccionado.split(" - ")[0];
        linea.append(idPuesto).append(";");

        // Cooperativa
        linea.append(rbtnSi.isSelected() ? "Si" : "No").append(";");

        linea.append(txtSalario.getText().trim());

        return linea.toString();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SexoGroup = new javax.swing.ButtonGroup();
        CooperativaGroup = new javax.swing.ButtonGroup();
        panelesBordesRedondeados1 = new Utilidades.PanelesBordesRedondeados();
        sdrIDDep = new javax.swing.JSeparator();
        sprCampoSalEmpleado = new javax.swing.JSeparator();
        sprCampoFechaEmpleado1 = new javax.swing.JSeparator();
        sprCampoNomEmpleado = new javax.swing.JSeparator();
        sprCampoApeMatEmpleado1 = new javax.swing.JSeparator();
        sprCampoDireEmpleado1 = new javax.swing.JSeparator();
        sprCampoTelEmpleado1 = new javax.swing.JSeparator();
        sprCampoApePatEmpleado = new javax.swing.JSeparator();
        txtEstado = new javax.swing.JTextField();
        lblUsuario = new javax.swing.JLabel();
        lblNomEmpleado = new javax.swing.JLabel();
        lblIDEmpleado = new javax.swing.JLabel();
        lblBotonRegistrar = new Utilidades.PanelesBordesRedondeados();
        btnRegistrar = new javax.swing.JButton();
        lblBotonEliminar = new Utilidades.PanelesBordesRedondeados();
        btnEliminar = new javax.swing.JButton();
        txtNomEmpleado = new javax.swing.JTextField();
        lblBtnSalir = new Utilidades.PanelesBordesRedondeados();
        btnSalir = new javax.swing.JButton();
        txtIDEmpleado = new javax.swing.JTextField();
        lblApePatEmpleado = new javax.swing.JLabel();
        txtApePatEmpleado = new javax.swing.JTextField();
        txtApeMatEmpleado = new javax.swing.JTextField();
        lblApeMatEmpleado = new javax.swing.JLabel();
        txtDirecEmpleado = new javax.swing.JTextField();
        lblDirecEmpleado = new javax.swing.JLabel();
        txtTelEmpleado = new javax.swing.JTextField();
        lblTelEmpleado = new javax.swing.JLabel();
        lblSexEmpleado = new javax.swing.JLabel();
        rbtnMasculino = new javax.swing.JRadioButton();
        rbtnFemenino = new javax.swing.JRadioButton();
        lblDepEmpleado = new javax.swing.JLabel();
        cmbDepEmpleado = new javax.swing.JComboBox<>();
        lblFechaEmpleado = new javax.swing.JLabel();
        txtFechaIngreEmpleado = new javax.swing.JTextField();
        lblPuestoEmpleado = new javax.swing.JLabel();
        cmbPuestoEmpleado = new javax.swing.JComboBox<>();
        lblCoopEmpleado = new javax.swing.JLabel();
        rbtnSi = new javax.swing.JRadioButton();
        rbtnNo = new javax.swing.JRadioButton();
        txtSalario = new javax.swing.JTextField();
        lblSalarioEmpleado = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        panelesBordesRedondeados1.setBackground(new java.awt.Color(76, 86, 106));
        panelesBordesRedondeados1.setEnabled(false);
        panelesBordesRedondeados1.setFocusable(false);
        panelesBordesRedondeados1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelesBordesRedondeados1.add(sdrIDDep, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 130, 120, 10));
        panelesBordesRedondeados1.add(sprCampoSalEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 630, 240, 10));
        panelesBordesRedondeados1.add(sprCampoFechaEmpleado1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 530, 240, 10));
        panelesBordesRedondeados1.add(sprCampoNomEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 180, 240, 10));
        panelesBordesRedondeados1.add(sprCampoApeMatEmpleado1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 280, 240, 10));
        panelesBordesRedondeados1.add(sprCampoDireEmpleado1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 330, 240, 10));
        panelesBordesRedondeados1.add(sprCampoTelEmpleado1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 380, 240, 10));
        panelesBordesRedondeados1.add(sprCampoApePatEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 230, 240, 10));

        txtEstado.setEditable(false);
        txtEstado.setBackground(new java.awt.Color(255, 204, 102));
        txtEstado.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        txtEstado.setForeground(new java.awt.Color(236, 239, 244));
        txtEstado.setText("Creando");
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
        panelesBordesRedondeados1.add(txtEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 20, 100, 40));

        lblUsuario.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        lblUsuario.setForeground(new java.awt.Color(236, 239, 244));
        lblUsuario.setText("Estado:");
        panelesBordesRedondeados1.add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 30, -1, -1));

        lblNomEmpleado.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        lblNomEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        lblNomEmpleado.setText("Nombre");
        panelesBordesRedondeados1.add(lblNomEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 160, -1, -1));

        lblIDEmpleado.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        lblIDEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        lblIDEmpleado.setText("ID del empleado");
        panelesBordesRedondeados1.add(lblIDEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, -1, -1));

        lblBotonRegistrar.setBackground(new java.awt.Color(59, 66, 82));
        lblBotonRegistrar.setPreferredSize(new java.awt.Dimension(132, 40));
        lblBotonRegistrar.setRoundBottomLeft(20);
        lblBotonRegistrar.setRoundBottomRight(20);
        lblBotonRegistrar.setRoundTopLeft(20);
        lblBotonRegistrar.setRoundTopRight(20);

        btnRegistrar.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(204, 204, 204));
        btnRegistrar.setText("Registrar");
        btnRegistrar.setBorder(null);
        btnRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnRegistrarMousePressed(evt);
            }
        });
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });
        btnRegistrar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                btnRegistrarKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout lblBotonRegistrarLayout = new javax.swing.GroupLayout(lblBotonRegistrar);
        lblBotonRegistrar.setLayout(lblBotonRegistrarLayout);
        lblBotonRegistrarLayout.setHorizontalGroup(
            lblBotonRegistrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblBotonRegistrarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        lblBotonRegistrarLayout.setVerticalGroup(
            lblBotonRegistrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblBotonRegistrarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelesBordesRedondeados1.add(lblBotonRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 740, -1, -1));

        lblBotonEliminar.setBackground(new java.awt.Color(59, 66, 82));
        lblBotonEliminar.setPreferredSize(new java.awt.Dimension(132, 40));
        lblBotonEliminar.setRoundBottomLeft(20);
        lblBotonEliminar.setRoundBottomRight(20);
        lblBotonEliminar.setRoundTopLeft(20);
        lblBotonEliminar.setRoundTopRight(20);

        btnEliminar.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(204, 204, 204));
        btnEliminar.setText("Eliminar");
        btnEliminar.setBorder(null);
        btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnEliminarMousePressed(evt);
            }
        });
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        btnEliminar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                btnEliminarKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout lblBotonEliminarLayout = new javax.swing.GroupLayout(lblBotonEliminar);
        lblBotonEliminar.setLayout(lblBotonEliminarLayout);
        lblBotonEliminarLayout.setHorizontalGroup(
            lblBotonEliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblBotonEliminarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        lblBotonEliminarLayout.setVerticalGroup(
            lblBotonEliminarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblBotonEliminarLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelesBordesRedondeados1.add(lblBotonEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 740, -1, -1));

        txtNomEmpleado.setBackground(new java.awt.Color(255, 204, 102));
        txtNomEmpleado.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        txtNomEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        txtNomEmpleado.setBorder(null);
        txtNomEmpleado.setMargin(new java.awt.Insets(10, 10, 10, 10));
        txtNomEmpleado.setName(""); // NOI18N
        txtNomEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomEmpleadoActionPerformed(evt);
            }
        });
        txtNomEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNomEmpleadoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNomEmpleadoKeyTyped(evt);
            }
        });
        panelesBordesRedondeados1.add(txtNomEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 150, 240, 40));

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

        panelesBordesRedondeados1.add(lblBtnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 740, -1, -1));

        txtIDEmpleado.setBackground(new java.awt.Color(255, 204, 102));
        txtIDEmpleado.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        txtIDEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        txtIDEmpleado.setBorder(null);
        txtIDEmpleado.setMargin(new java.awt.Insets(10, 10, 10, 10));
        txtIDEmpleado.setName(""); // NOI18N
        txtIDEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDEmpleadoActionPerformed(evt);
            }
        });
        txtIDEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIDEmpleadoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIDEmpleadoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIDEmpleadoKeyTyped(evt);
            }
        });
        panelesBordesRedondeados1.add(txtIDEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 100, 120, 40));

        lblApePatEmpleado.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        lblApePatEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        lblApePatEmpleado.setText("Apellido Paterno");
        panelesBordesRedondeados1.add(lblApePatEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 210, -1, -1));

        txtApePatEmpleado.setBackground(new java.awt.Color(255, 204, 102));
        txtApePatEmpleado.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        txtApePatEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        txtApePatEmpleado.setBorder(null);
        txtApePatEmpleado.setMargin(new java.awt.Insets(10, 10, 10, 10));
        txtApePatEmpleado.setName(""); // NOI18N
        txtApePatEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApePatEmpleadoActionPerformed(evt);
            }
        });
        txtApePatEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtApePatEmpleadoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApePatEmpleadoKeyTyped(evt);
            }
        });
        panelesBordesRedondeados1.add(txtApePatEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 200, 240, 40));

        txtApeMatEmpleado.setBackground(new java.awt.Color(255, 204, 102));
        txtApeMatEmpleado.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        txtApeMatEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        txtApeMatEmpleado.setBorder(null);
        txtApeMatEmpleado.setMargin(new java.awt.Insets(10, 10, 10, 10));
        txtApeMatEmpleado.setName(""); // NOI18N
        txtApeMatEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtApeMatEmpleadoActionPerformed(evt);
            }
        });
        txtApeMatEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtApeMatEmpleadoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApeMatEmpleadoKeyTyped(evt);
            }
        });
        panelesBordesRedondeados1.add(txtApeMatEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 250, 240, 40));

        lblApeMatEmpleado.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        lblApeMatEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        lblApeMatEmpleado.setText("Apellido Materno");
        panelesBordesRedondeados1.add(lblApeMatEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, -1, -1));

        txtDirecEmpleado.setBackground(new java.awt.Color(255, 204, 102));
        txtDirecEmpleado.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        txtDirecEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        txtDirecEmpleado.setBorder(null);
        txtDirecEmpleado.setMargin(new java.awt.Insets(10, 10, 10, 10));
        txtDirecEmpleado.setName(""); // NOI18N
        txtDirecEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDirecEmpleadoActionPerformed(evt);
            }
        });
        txtDirecEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDirecEmpleadoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDirecEmpleadoKeyTyped(evt);
            }
        });
        panelesBordesRedondeados1.add(txtDirecEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 300, 240, 40));

        lblDirecEmpleado.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        lblDirecEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        lblDirecEmpleado.setText("Dirección");
        panelesBordesRedondeados1.add(lblDirecEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 310, -1, -1));

        txtTelEmpleado.setBackground(new java.awt.Color(255, 204, 102));
        txtTelEmpleado.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        txtTelEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        txtTelEmpleado.setBorder(null);
        txtTelEmpleado.setMargin(new java.awt.Insets(10, 10, 10, 10));
        txtTelEmpleado.setName(""); // NOI18N
        txtTelEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelEmpleadoActionPerformed(evt);
            }
        });
        txtTelEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelEmpleadoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelEmpleadoKeyTyped(evt);
            }
        });
        panelesBordesRedondeados1.add(txtTelEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 350, 240, 40));

        lblTelEmpleado.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        lblTelEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        lblTelEmpleado.setText("Telefono");
        panelesBordesRedondeados1.add(lblTelEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 360, -1, -1));

        lblSexEmpleado.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        lblSexEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        lblSexEmpleado.setText("Sexo");
        panelesBordesRedondeados1.add(lblSexEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 410, -1, -1));

        SexoGroup.add(rbtnMasculino);
        rbtnMasculino.setText("Masculino");
        panelesBordesRedondeados1.add(rbtnMasculino, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 410, -1, -1));

        SexoGroup.add(rbtnFemenino);
        rbtnFemenino.setText("Femenino");
        panelesBordesRedondeados1.add(rbtnFemenino, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 410, -1, -1));

        lblDepEmpleado.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        lblDepEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        lblDepEmpleado.setText("Departamento");
        panelesBordesRedondeados1.add(lblDepEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 460, -1, -1));

        cmbDepEmpleado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelesBordesRedondeados1.add(cmbDepEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 450, 240, 40));

        lblFechaEmpleado.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        lblFechaEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        lblFechaEmpleado.setText("Fecha de ingreso");
        panelesBordesRedondeados1.add(lblFechaEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 510, -1, -1));

        txtFechaIngreEmpleado.setBackground(new java.awt.Color(255, 204, 102));
        txtFechaIngreEmpleado.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        txtFechaIngreEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        txtFechaIngreEmpleado.setBorder(null);
        txtFechaIngreEmpleado.setMargin(new java.awt.Insets(10, 10, 10, 10));
        txtFechaIngreEmpleado.setName(""); // NOI18N
        txtFechaIngreEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaIngreEmpleadoActionPerformed(evt);
            }
        });
        txtFechaIngreEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFechaIngreEmpleadoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFechaIngreEmpleadoKeyTyped(evt);
            }
        });
        panelesBordesRedondeados1.add(txtFechaIngreEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 500, 240, 40));

        lblPuestoEmpleado.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        lblPuestoEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        lblPuestoEmpleado.setText("Puesto");
        panelesBordesRedondeados1.add(lblPuestoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 560, -1, -1));

        cmbPuestoEmpleado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelesBordesRedondeados1.add(cmbPuestoEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 550, 240, 40));

        lblCoopEmpleado.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        lblCoopEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        lblCoopEmpleado.setText("Cooperativa");
        panelesBordesRedondeados1.add(lblCoopEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 660, -1, -1));

        CooperativaGroup.add(rbtnSi);
        rbtnSi.setText("Si");
        rbtnSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnSiActionPerformed(evt);
            }
        });
        panelesBordesRedondeados1.add(rbtnSi, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 660, -1, -1));

        CooperativaGroup.add(rbtnNo);
        rbtnNo.setText("No");
        rbtnNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnNoActionPerformed(evt);
            }
        });
        panelesBordesRedondeados1.add(rbtnNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 660, -1, -1));

        txtSalario.setBackground(new java.awt.Color(255, 204, 102));
        txtSalario.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        txtSalario.setForeground(new java.awt.Color(236, 239, 244));
        txtSalario.setBorder(null);
        txtSalario.setMargin(new java.awt.Insets(10, 10, 10, 10));
        txtSalario.setName(""); // NOI18N
        txtSalario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSalarioActionPerformed(evt);
            }
        });
        txtSalario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSalarioKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSalarioKeyTyped(evt);
            }
        });
        panelesBordesRedondeados1.add(txtSalario, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 600, 240, 40));

        lblSalarioEmpleado.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        lblSalarioEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        lblSalarioEmpleado.setText("Salario");
        panelesBordesRedondeados1.add(lblSalarioEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 610, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelesBordesRedondeados1, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelesBordesRedondeados1, javax.swing.GroupLayout.DEFAULT_SIZE, 810, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEstadoActionPerformed

    private void txtEstadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEstadoKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && !txtEstado.getText().isEmpty()) {
            txtNomEmpleado.requestFocus();
        }
    }//GEN-LAST:event_txtEstadoKeyPressed

    private void txtEstadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEstadoKeyTyped

        txtNomEmpleado.setEnabled(!txtEstado.getText().isEmpty());
    }//GEN-LAST:event_txtEstadoKeyTyped

    private void btnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarMouseClicked

    private void btnEliminarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarMousePressed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        String idEmpleado = txtIDEmpleado.getText().trim();

        if (idEmpleado.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese un ID de empleado para eliminar.");
            return;
        }

        String lineaEncontrada = buscarEmpleado(idEmpleado);

        if (lineaEncontrada == null) {
            JOptionPane.showMessageDialog(null, "El empleado no existe.");
            return;
        }

        // Obtener datos para mostrar en confirmación
        String[] datos = lineaEncontrada.split(";");
        String nombreCompleto = datos[1] + " " + datos[2] + " " + datos[3];

        // Confirmar eliminación
        int confirmacion = JOptionPane.showConfirmDialog(
                null,
                "¿Está seguro de que desea eliminar el empleado '" + nombreCompleto + "'?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            ManejoArchivos manejo = new ManejoArchivos();

            try {
                manejo.Eliminar(lineaEncontrada, archivo);
                JOptionPane.showMessageDialog(null, "Empleado eliminado correctamente.");
                limpiarCampos();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al eliminar el empleado: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnEliminarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEliminarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarKeyTyped

    private void btnRegistrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegistrarMouseClicked

    private void btnRegistrarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegistrarMousePressed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        if (!validarCampos()) {
            return; // Si la validación falla, salir del método
        }

        // Validar cambio de cooperativa antes de proceder
        if (!validarCambioCooperativa()) {
            return;
        }

        String idEmpleado = txtIDEmpleado.getText().trim();
        String lineaExistente = buscarEmpleado(idEmpleado);

        // Verificar si ya existe y no estamos modificando
        if (lineaExistente != null && !encontrado) {
            JOptionPane.showMessageDialog(null, "El empleado ya existe. Use el autocompletado para modificarlo.");
            return;
        }

        String nuevaLinea = construirLineaEmpleado();
        ManejoArchivos manejo = new ManejoArchivos();

        try {
            if (!encontrado) {
                // Guardar nuevo empleado
                manejo.GuardarDatos(nuevaLinea, archivo);
                JOptionPane.showMessageDialog(null, "Empleado guardado correctamente.");
            } else {
                // Modificar empleado existente
                manejo.Modificar(cadenaAnterior, nuevaLinea, archivo);
                JOptionPane.showMessageDialog(null, "Empleado modificado correctamente.");
            }

            // Limpiar formulario
            limpiarCampos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnRegistrarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnRegistrarKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegistrarKeyTyped

    private void txtNomEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomEmpleadoActionPerformed

    private void txtNomEmpleadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomEmpleadoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomEmpleadoKeyPressed

    private void txtNomEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomEmpleadoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomEmpleadoKeyTyped

    private void btnSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalirMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalirMouseClicked

    private void btnSalirMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalirMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalirMousePressed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnSalirKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSalirKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalirKeyTyped

    private void txtIDEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIDEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDEmpleadoActionPerformed

    private void txtIDEmpleadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIDEmpleadoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDEmpleadoKeyPressed

    private void txtIDEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIDEmpleadoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDEmpleadoKeyTyped

    private void txtIDEmpleadoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIDEmpleadoKeyReleased
        String idEmpleado = txtIDEmpleado.getText().trim();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaActual = sdf.format(new Date());

        if (idEmpleado.isEmpty()) {
            // Si el campo ID está vacío, deshabilita y limpia todo
            limpiarCampos();
            txtEstado.setText(ESTADO_CREANDO); // Asegura que el estado sea "Creando"
            habilitarCampos(false); // Deshabilita los campos

            // Ocultar ambos botones y sus contenedores
            btnRegistrar.setVisible(false);
            lblBotonRegistrar.setVisible(false);
            btnEliminar.setVisible(false);
            lblBotonEliminar.setVisible(false);

            encontrado = false;
            cadenaAnterior = "";
            return; // Sal del método
        }

        // Asegurarse de que el ID solo contenga números para la búsqueda
        if (!idEmpleado.matches("^\\d+$")) {
            JOptionPane.showMessageDialog(this, "El ID solo debe contener números.",
                    "Dato inválido", JOptionPane.WARNING_MESSAGE);
            limpiarCampos();
            txtIDEmpleado.setText(idEmpleado); // Mantener el ID inválido para que el usuario lo corrija
            txtEstado.setText(ESTADO_CREANDO);
            habilitarCampos(false); // Deshabilita los campos hasta que el ID sea válido

            // Ocultar ambos botones y sus contenedores
            btnRegistrar.setVisible(false);
            lblBotonRegistrar.setVisible(false);
            btnEliminar.setVisible(false);
            lblBotonEliminar.setVisible(false);

            encontrado = false;
            cadenaAnterior = "";
            return;
        }

        // Realiza la búsqueda
        String lineaEncontrada = buscarEmpleado(idEmpleado);

        if (lineaEncontrada != null) {
            // Empleado encontrado: Modo Modificación
            String[] datos = lineaEncontrada.split(";");
            cargarDatosEmpleado(datos);
            txtEstado.setText(ESTADO_MODIFICANDO);
            habilitarCampos(true); // Habilita los campos para edición

            btnRegistrar.setText("Modificar"); // Cambia el texto del botón
            btnRegistrar.setVisible(true); // Asegura que el botón de modificar esté visible
            lblBotonRegistrar.setVisible(true); // Asegura que su contenedor sea visible

            btnEliminar.setVisible(true);  // Asegura que el botón de eliminar esté visible
            lblBotonEliminar.setVisible(true);  // Asegura que su contenedor sea visible

            encontrado = true;
            cadenaAnterior = lineaEncontrada;
        } else {
            // Empleado NO encontrado: Modo Creación
            // Limpia todos los campos excepto el ID que ya fue ingresado
            txtNomEmpleado.setText("");
            txtApePatEmpleado.setText("");
            txtApeMatEmpleado.setText("");
            txtDirecEmpleado.setText("");
            txtTelEmpleado.setText("");
            txtFechaIngreEmpleado.setText(fechaActual);
            txtSalario.setText("");

            SexoGroup.clearSelection();
            CooperativaGroup.clearSelection();
            cmbDepEmpleado.setSelectedIndex(0);
            cmbPuestoEmpleado.setSelectedIndex(0);

            txtEstado.setText(ESTADO_CREANDO);
            habilitarCampos(true); // Habilita los campos para ingresar nuevos datos

            btnRegistrar.setText("Registrar"); // Asegura el texto original del botón
            btnRegistrar.setVisible(true); // Asegura que el botón de registrar esté visible
            lblBotonRegistrar.setVisible(true); // Asegura que su contenedor sea visible

            btnEliminar.setVisible(false); // Oculta el botón de eliminar para un nuevo registro
            lblBotonEliminar.setVisible(false); // Oculta su contenedor

            encontrado = false;
            cadenaAnterior = ""; // No hay cadena anterior para un nuevo registro
        }
    }//GEN-LAST:event_txtIDEmpleadoKeyReleased

    private void txtApePatEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApePatEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApePatEmpleadoActionPerformed

    private void txtApePatEmpleadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApePatEmpleadoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApePatEmpleadoKeyPressed

    private void txtApePatEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApePatEmpleadoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApePatEmpleadoKeyTyped

    private void txtApeMatEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApeMatEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApeMatEmpleadoActionPerformed

    private void txtApeMatEmpleadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApeMatEmpleadoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApeMatEmpleadoKeyPressed

    private void txtApeMatEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApeMatEmpleadoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApeMatEmpleadoKeyTyped

    private void txtDirecEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDirecEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDirecEmpleadoActionPerformed

    private void txtDirecEmpleadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDirecEmpleadoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDirecEmpleadoKeyPressed

    private void txtDirecEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDirecEmpleadoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDirecEmpleadoKeyTyped

    private void txtTelEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelEmpleadoActionPerformed

    private void txtTelEmpleadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelEmpleadoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelEmpleadoKeyPressed

    private void txtTelEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelEmpleadoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelEmpleadoKeyTyped

    private void txtFechaIngreEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaIngreEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaIngreEmpleadoActionPerformed

    private void txtFechaIngreEmpleadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFechaIngreEmpleadoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaIngreEmpleadoKeyPressed

    private void txtFechaIngreEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFechaIngreEmpleadoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaIngreEmpleadoKeyTyped

    private void txtSalarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSalarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSalarioActionPerformed

    private void txtSalarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalarioKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSalarioKeyPressed

    private void txtSalarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalarioKeyTyped
        // TODO add your handling code here:
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                String salarioText = txtSalario.getText().trim();
                boolean tieneSalario = !salarioText.isEmpty();

                // Solo permitir seleccionar cooperativa si hay salario
                if (!tieneSalario && rbtnSi.isSelected()) {
                    rbtnNo.setSelected(true);
                }
            }
        });
    }//GEN-LAST:event_txtSalarioKeyTyped

    private void rbtnSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnSiActionPerformed
        if (rbtnSi.isSelected()) {
            // Caso 1: Estás en modo "Creando" O estás en modo "Modificando" y el empleado NO fue cargado como "Si" en cooperativa
            // En estos casos, siempre queremos abrir el diálogo si se selecciona "Si"
            if (txtEstado.getText().equals(ESTADO_CREANDO) || !cooperativaConfiguracionCompletada) {
                gestionarCooperativa();
                // La bandera cooperativaConfiguracionCompletada se actualiza dentro de gestionarCooperativa()
                // según si el proceso fue exitoso o cancelado.
            } // Caso 2: Estás en modo "Modificando" Y el empleado FUE cargado como "Si" en cooperativa
            // En este caso, si el usuario vuelve a presionar "Si", también queremos que el diálogo se abra.
            else if (txtEstado.getText().equals(ESTADO_MODIFICANDO) && encontrado && rbtnSi.isSelected()) {
                // Forzamos la apertura del diálogo si el usuario vuelve a presionar "Si"
                // Esto permite re-configurar o revisar la información de la cooperativa
                gestionarCooperativa();
                // La bandera cooperativaConfiguracionCompletada se actualiza dentro de gestionarCooperativa()
                // según si el proceso fue exitoso o cancelado.
            }
        }
    }//GEN-LAST:event_rbtnSiActionPerformed

    private void rbtnNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnNoActionPerformed
        // TODO add your handling code here:
        validarCambioCooperativa();
        cooperativaConfiguracionCompletada = false;
    }//GEN-LAST:event_rbtnNoActionPerformed

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
        java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
        java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
        java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new Empleados().setVisible(true);
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup CooperativaGroup;
    private javax.swing.ButtonGroup SexoGroup;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cmbDepEmpleado;
    private javax.swing.JComboBox<String> cmbPuestoEmpleado;
    private javax.swing.JLabel lblApeMatEmpleado;
    private javax.swing.JLabel lblApePatEmpleado;
    private Utilidades.PanelesBordesRedondeados lblBotonEliminar;
    private Utilidades.PanelesBordesRedondeados lblBotonRegistrar;
    private Utilidades.PanelesBordesRedondeados lblBtnSalir;
    private javax.swing.JLabel lblCoopEmpleado;
    private javax.swing.JLabel lblDepEmpleado;
    private javax.swing.JLabel lblDirecEmpleado;
    private javax.swing.JLabel lblFechaEmpleado;
    private javax.swing.JLabel lblIDEmpleado;
    private javax.swing.JLabel lblNomEmpleado;
    private javax.swing.JLabel lblPuestoEmpleado;
    private javax.swing.JLabel lblSalarioEmpleado;
    private javax.swing.JLabel lblSexEmpleado;
    private javax.swing.JLabel lblTelEmpleado;
    private javax.swing.JLabel lblUsuario;
    private Utilidades.PanelesBordesRedondeados panelesBordesRedondeados1;
    private javax.swing.JRadioButton rbtnFemenino;
    private javax.swing.JRadioButton rbtnMasculino;
    private javax.swing.JRadioButton rbtnNo;
    private javax.swing.JRadioButton rbtnSi;
    private javax.swing.JSeparator sdrIDDep;
    private javax.swing.JSeparator sprCampoApeMatEmpleado1;
    private javax.swing.JSeparator sprCampoApePatEmpleado;
    private javax.swing.JSeparator sprCampoDireEmpleado1;
    private javax.swing.JSeparator sprCampoFechaEmpleado1;
    private javax.swing.JSeparator sprCampoNomEmpleado;
    private javax.swing.JSeparator sprCampoSalEmpleado;
    private javax.swing.JSeparator sprCampoTelEmpleado1;
    private javax.swing.JTextField txtApeMatEmpleado;
    private javax.swing.JTextField txtApePatEmpleado;
    private javax.swing.JTextField txtDirecEmpleado;
    private javax.swing.JTextField txtEstado;
    private javax.swing.JTextField txtFechaIngreEmpleado;
    private javax.swing.JTextField txtIDEmpleado;
    private javax.swing.JTextField txtNomEmpleado;
    private javax.swing.JTextField txtSalario;
    private javax.swing.JTextField txtTelEmpleado;
    // End of variables declaration//GEN-END:variables
}
