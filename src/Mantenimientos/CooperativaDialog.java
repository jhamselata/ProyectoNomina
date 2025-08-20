/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * CooperativaDialog corregido
 */
package Mantenimientos;

import ManejoDeArchivos.ManejoArchivos;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class CooperativaDialog extends javax.swing.JDialog {
    
    private static final String ARCHIVO_COOPERATIVA = "src/BaseDeDatos/Cooperativa.txt";
    private String empleadoId;
    private BigDecimal salarioEmpleado;
    private boolean procesoCompletado = false;
    private boolean esModificacion = false;
    private String registroAnterior = "";
    private File archivo = new File(ARCHIVO_COOPERATIVA);
    
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblIdEmpleado;
    private javax.swing.JTextField txtIdEmpleado;
    private javax.swing.JLabel lblSalario;
    private javax.swing.JTextField txtSalarioRef;
    private javax.swing.JLabel lblPorcentaje;
    private javax.swing.JTextField txtPorcentajeDesc;
    private javax.swing.JLabel lblMaximoPermitido;
    private javax.swing.JLabel lblBalance;
    private javax.swing.JTextField txtBalanceAcumulado;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnCancelar;
    
    public CooperativaDialog(java.awt.Frame parent, String empleadoId, BigDecimal salario) {
        super(parent, true);
        this.empleadoId = empleadoId;
        this.salarioEmpleado = salario;
        initComponents();
        personalizarFormulario();
        configurarVentana();
        verificarRegistroExistente();
    }
    
    public CooperativaDialog() {
        super((java.awt.Frame) null, true);
        initComponents();
        personalizarFormulario();
    }
    
    private void configurarVentana() {
        setLocationRelativeTo(getParent());
        setTitle("Gestión de Cooperativa - Empleado ID: " + empleadoId);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        Color transparente = new Color(0, 0, 0, 0);
        txtIdEmpleado.setBackground(transparente);
        txtPorcentajeDesc.setBackground(transparente);
        txtBalanceAcumulado.setBackground(transparente);
        txtSalarioRef.setBackground(transparente);
        
        txtIdEmpleado.setEditable(false);
        txtSalarioRef.setEditable(false);
        
        txtIdEmpleado.setText(empleadoId);
        txtSalarioRef.setText(salarioEmpleado.toString());
        
        lblMaximoPermitido.setText("Máximo permitido: 5% del salario");

    }

    private void verificarRegistroExistente() {
        String registroExistente = buscarRegistroCooperativa(empleadoId);
        
        if (registroExistente != null) {
            // Es una modificación
            esModificacion = true;
            registroAnterior = registroExistente;
            cargarDatosExistentes(registroExistente);
            lblEstado.setText("Modificando registro existente");
            btnGuardar.setText("Actualizar");
        } else {
            // Es un nuevo registro
            txtBalanceAcumulado.setText("0.00");
            lblEstado.setText("Creando nuevo registro");
            btnGuardar.setText("Guardar");
        }
    }

    private String buscarRegistroCooperativa(String idEmpleado) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 1 && partes[0].equals(idEmpleado)) {
                    return linea;
                }
            }
        } catch (IOException e) {
            System.out.println("Archivo de cooperativa no encontrado, se creará uno nuevo");
        }
        return null;
    }
    
    private void cargarDatosExistentes(String registro) {
        String[] partes = registro.split(";");
        if (partes.length >= 3) {
            txtPorcentajeDesc.setText(partes[1]);
            txtBalanceAcumulado.setText(partes.length > 2 && !partes[2].isEmpty() ? partes[2] : "0.00");
        }
    }

    private boolean validarDatos() {
        // Validar porcentaje
        String porcentajeText = txtPorcentajeDesc.getText().trim();
        if (porcentajeText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El porcentaje de descuento es obligatorio.");
            txtPorcentajeDesc.requestFocus();
            return false;
        }

        try {
            BigDecimal porcentaje = new BigDecimal(porcentajeText);
            
            if (porcentaje.compareTo(BigDecimal.ZERO) <= 0 || porcentaje.compareTo(new BigDecimal("5")) > 0) {
    JOptionPane.showMessageDialog(this, "El porcentaje debe estar entre 1% y 5%.");
    txtPorcentajeDesc.requestFocus();
    return false;
}
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El porcentaje debe ser un número válido.");
            txtPorcentajeDesc.requestFocus();
            return false;
        }

        // Validar balance acumulado
        String balanceText = txtBalanceAcumulado.getText().trim();
        if (!balanceText.isEmpty()) {
            try {
                BigDecimal balance = new BigDecimal(balanceText);
                if (balance.compareTo(BigDecimal.ZERO) < 0) {
                    JOptionPane.showMessageDialog(this, "El balance acumulado no puede ser negativo.");
                    txtBalanceAcumulado.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El balance acumulado debe ser un número válido.");
                txtBalanceAcumulado.requestFocus();
                return false;
            }
        }

        return true;
    }

    private String construirLineaCooperativa() {
        StringBuilder linea = new StringBuilder();
        linea.append(empleadoId).append(";");
        linea.append(txtPorcentajeDesc.getText().trim()).append(";");
        
        String balance = txtBalanceAcumulado.getText().trim();
        if (balance.isEmpty()) {
            balance = "0.00";
        }
        linea.append(balance);
        
        return linea.toString();
    }

    private void guardarRegistro() {
        if (!validarDatos()) {
            return;
        }

        String nuevaLinea = construirLineaCooperativa();
        ManejoArchivos manejo = new ManejoArchivos();

        try {
            if (esModificacion) {
                manejo.Modificar(registroAnterior, nuevaLinea, archivo);
                JOptionPane.showMessageDialog(this, "Registro de cooperativa actualizado correctamente.");
            } else {
                manejo.GuardarDatos(nuevaLinea, archivo);
                JOptionPane.showMessageDialog(this, "Registro de cooperativa guardado correctamente.");
            }
            
            procesoCompletado = true;
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al guardar el registro de cooperativa: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isProcesoCompletado() {
        return procesoCompletado;
    }

    public static BigDecimal obtenerBalanceEmpleado(String idEmpleado) {
        File archivo = new File(ARCHIVO_COOPERATIVA);
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 1 && partes[0].equals(idEmpleado)) {
                    if (partes.length >= 3 && !partes[2].isEmpty()) {
                        return new BigDecimal(partes[2]);
                    }
                    return BigDecimal.ZERO;
                }
            }
        } catch (IOException | NumberFormatException e) {
            // Si hay error, asumir balance cero
        }
        return BigDecimal.ZERO;
    }
    
    private void personalizarFormulario(){
        panelPrincipal = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblIdEmpleado = new javax.swing.JLabel();
        txtIdEmpleado = new javax.swing.JTextField();
        lblSalario = new javax.swing.JLabel();
        txtSalarioRef = new javax.swing.JTextField();
        lblPorcentaje = new javax.swing.JLabel();
        txtPorcentajeDesc = new javax.swing.JTextField();
        lblMaximoPermitido = new javax.swing.JLabel();
        lblBalance = new javax.swing.JLabel();
        txtBalanceAcumulado = new javax.swing.JTextField();
        lblEstado = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); // CORREGIDO AQUÍ
        setTitle("Gestión de Cooperativa");
        setResizable(false);

        panelPrincipal.setBackground(new java.awt.Color(76, 86, 106));
        panelPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Noto Sans", 1, 18));
        lblTitulo.setForeground(new java.awt.Color(236, 239, 244));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Registro de Cooperativa");
        panelPrincipal.add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 300, 30));

        lblIdEmpleado.setFont(new java.awt.Font("Noto Sans", 1, 14));
        lblIdEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        lblIdEmpleado.setText("ID Empleado:");
        panelPrincipal.add(lblIdEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, -1, -1));

        txtIdEmpleado.setFont(new java.awt.Font("Noto Sans", 0, 14));
        txtIdEmpleado.setForeground(new java.awt.Color(236, 239, 244));
        txtIdEmpleado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(59, 66, 82)));
        txtIdEmpleado.setOpaque(false);
        panelPrincipal.add(txtIdEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 65, 100, 30));

        lblSalario.setFont(new java.awt.Font("Noto Sans", 1, 14));
        lblSalario.setForeground(new java.awt.Color(236, 239, 244));
        lblSalario.setText("Salario:");
        panelPrincipal.add(lblSalario, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, -1, -1));

        txtSalarioRef.setFont(new java.awt.Font("Noto Sans", 0, 14));
        txtSalarioRef.setForeground(new java.awt.Color(236, 239, 244));
        txtSalarioRef.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(59, 66, 82)));
        txtSalarioRef.setOpaque(false);
        panelPrincipal.add(txtSalarioRef, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 105, 150, 30));

        lblPorcentaje.setFont(new java.awt.Font("Noto Sans", 1, 14));
        lblPorcentaje.setForeground(new java.awt.Color(236, 239, 244));
        lblPorcentaje.setText("Descuento (%):");
        panelPrincipal.add(lblPorcentaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, -1, -1));
        txtPorcentajeDesc.setToolTipText("Ingrese un porcentaje entre 1 y 5");


        txtPorcentajeDesc.setFont(new java.awt.Font("Noto Sans", 0, 14));
        txtPorcentajeDesc.setForeground(new java.awt.Color(236, 239, 244));
        txtPorcentajeDesc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(59, 66, 82)));
        txtPorcentajeDesc.setOpaque(false);
        panelPrincipal.add(txtPorcentajeDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 145, 100, 30));

        lblMaximoPermitido.setFont(new java.awt.Font("Noto Sans", 2, 12));
        lblMaximoPermitido.setForeground(new java.awt.Color(255, 204, 102));
        lblMaximoPermitido.setText("Máximo permitido: $0.00");
        panelPrincipal.add(lblMaximoPermitido, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 150, -1, -1));

        lblBalance.setFont(new java.awt.Font("Noto Sans", 1, 14));
        lblBalance.setForeground(new java.awt.Color(236, 239, 244));
        lblBalance.setText("Balance Acumulado:");
        panelPrincipal.add(lblBalance, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 190, -1, -1));

        txtBalanceAcumulado.setFont(new java.awt.Font("Noto Sans", 0, 14));
        txtBalanceAcumulado.setForeground(new java.awt.Color(236, 239, 244));
        txtBalanceAcumulado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(59, 66, 82)));
        txtBalanceAcumulado.setOpaque(false);
        panelPrincipal.add(txtBalanceAcumulado, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 185, 150, 30));
        txtBalanceAcumulado.setEditable(false);

        lblEstado.setFont(new java.awt.Font("Noto Sans", 2, 12));
        lblEstado.setForeground(new java.awt.Color(255, 204, 102));
        lblEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEstado.setText("Estado del proceso");
        panelPrincipal.add(lblEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 300, -1));

        btnGuardar.setBackground(new java.awt.Color(59, 66, 82));
        btnGuardar.setFont(new java.awt.Font("Noto Sans", 1, 14));
        btnGuardar.setForeground(new java.awt.Color(204, 204, 204));
        btnGuardar.setText("Guardar");
        btnGuardar.setBorder(null);
        btnGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardar.setFocusPainted(false);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        panelPrincipal.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 280, 100, 35));

        btnCancelar.setBackground(new java.awt.Color(59, 66, 82));
        btnCancelar.setFont(new java.awt.Font("Noto Sans", 1, 14));
        btnCancelar.setForeground(new java.awt.Color(204, 204, 204));
        btnCancelar.setText("Cancelar");
        btnCancelar.setBorder(null);
        btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        panelPrincipal.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 280, 100, 35));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
        );

        pack();
    }
    
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        guardarRegistro();
    }
     
    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de cancelar? Los cambios no se guardarán.",
            "Confirmar cancelación",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            procesoCompletado = false;
            dispose();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
        setTitle("Cooperativa");
        ImageIcon icono = new ImageIcon(getClass().getResource("/Iconos/ProgramIcon.png"));
        this.setIconImage(icono.getImage());
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CooperativaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(() -> {
            new CooperativaDialog().setVisible(true);
        });
    }
}