/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package VentanaPrincipal;

import Mantenimientos.Departamentos;
import Mantenimientos.Empleados;
import Mantenimientos.Puestos;
import Mantenimientos.Usuarios;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author jhams
 */
public class Inicio extends javax.swing.JFrame {

    private String rolUsuario;

    /**
     * Creates new form Menu
     */
    private void UnaVentanaAlaVez() {};
    
    
    private void abrirVentanaUsuarios() {
        if (ventanaUsuarios == null || !ventanaUsuarios.isDisplayable()) {
            ventanaUsuarios = new Usuarios();
            ventanaUsuarios.setVisible(true);
        } else {
            ventanaUsuarios.toFront();
        }
    }

    private void abrirVentanaDepartamentos() {
        if (ventanaDepartamentos == null || !ventanaDepartamentos.isDisplayable()) {
            ventanaDepartamentos = new Departamentos();
            ventanaDepartamentos.setVisible(true);
        } else {
            ventanaDepartamentos.toFront();
        }
    }
    
    private void abrirVentanaEmpleados() {
        if (ventanaEmpleados == null || !ventanaEmpleados.isDisplayable()) {
            ventanaEmpleados = new Empleados();
            ventanaEmpleados.setVisible(true);
        } else {
            ventanaEmpleados.toFront();
        }
    }
    
    private void abrirVentanaPuestos() {
        if (ventanaPuestos == null || !ventanaPuestos.isDisplayable()) {
            ventanaPuestos = new Puestos();
            ventanaPuestos.setVisible(true);
        } else {
            ventanaPuestos.toFront();
        }
    }

    private JMenuItem createStyledMenuItem(String text, ActionListener action) {
        JMenuItem item = new JMenuItem(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                if (getModel().isArmed()) {
                    g2.setColor(new Color(100, 149, 237)); // Color hover
                } else {
                    g2.setColor(new Color(60, 63, 65)); // Color normal
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
                g2.dispose();
                setOpaque(false);
                super.paintComponent(g);
            }
        };
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.setForeground(Color.WHITE);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        item.setOpaque(false);
        item.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        item.addActionListener(action);
        return item;
    }

    public void popUpMenu() {
    popupMenuMantenimientos = new JPopupMenu();
    popupMenuMantenimientos.setOpaque(false);
    popupMenuMantenimientos.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

    JMenuItem usuariosItem = createStyledMenuItem("Usuarios", e -> {
        if (ventanaUsuarios == null || !ventanaUsuarios.isDisplayable()) {
            ventanaUsuarios = new Usuarios();
            ventanaUsuarios.setVisible(true);
        } else {
            ventanaUsuarios.toFront();
        }
    });
    // Ejemplo: deshabilitar "Usuarios" si rolUsuario es "1"
    if ("1".equals(rolUsuario)) {
        usuariosItem.setEnabled(false);
    }
    popupMenuMantenimientos.add(usuariosItem);

    JMenuItem departamentosItem = createStyledMenuItem("Departamentos", e -> {
        if (ventanaDepartamentos == null || !ventanaDepartamentos.isDisplayable()) {
            ventanaDepartamentos = new Departamentos();
            ventanaDepartamentos.setVisible(true);
        } else {
            ventanaDepartamentos.toFront();
        }
    });
    popupMenuMantenimientos.add(departamentosItem);

    JMenuItem puestosItem = createStyledMenuItem("Puestos", e -> {
        System.out.println("Abrir Puestos");
    });
    popupMenuMantenimientos.add(puestosItem);

    JMenuItem empleadosItem = createStyledMenuItem("Empleados", e -> {
        System.out.println("Abrir Empleados");
    });
    popupMenuMantenimientos.add(empleadosItem);
}

    private void mostrarPopupEnComponente(JComponent componente, String[] opciones, ActionListener[] acciones) {
        JPopupMenu menu = new JPopupMenu() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(40, 40, 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };

        menu.setOpaque(false);
        menu.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));

        for (int i = 0; i < opciones.length; i++) {
            JMenuItem item = createStyledMenuItem(opciones[i], acciones[i]);
            menu.add(item);
        }

        // Mostrarlo debajo del componente
        menu.show(componente, 200, componente.getHeight()-50);
    }
    

    public Inicio(String rol) {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // <-- Mover aquí
        this.setVisible(true); // <-- Mostrar inmediatamente
        ImageIcon icono = new ImageIcon(getClass().getResource("/Iconos/ProgramIcon.png"));
        this.setIconImage(icono.getImage());

        this.rolUsuario = rol;
        System.out.println(rolUsuario);
        
        //color de botones
        btnMantenimientosMenu.setBackground(new Color(0, 0, 0, 0));
        popUpMenu();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        popupMenuMantenimientos = new javax.swing.JPopupMenu();
        pnlBarraSuperior = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        pnlBarraLateralIzq = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 50), new java.awt.Dimension(32767, 10));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 50), new java.awt.Dimension(32767, 10));
        jPanel4 = new javax.swing.JPanel();
        pnlBotonMatenimientos = new Utilidades.PanelesBordesRedondeados();
        btnMantenimientosMenu = new javax.swing.JButton();
        panelesBordesRedondeados2 = new Utilidades.PanelesBordesRedondeados();
        jLabel2 = new javax.swing.JLabel();
        panelesBordesRedondeados3 = new Utilidades.PanelesBordesRedondeados();
        btnProcesosMenu = new javax.swing.JButton();
        pnlContenido = new javax.swing.JPanel();
        pnlConsultas = new javax.swing.JPanel();
        pnlBarraopciones = new javax.swing.JPanel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 200), new java.awt.Dimension(32767, 20));
        pnlTablas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDepartamentos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        pnlBarraSuperior.setBackground(new java.awt.Color(76, 86, 106));
        pnlBarraSuperior.setPreferredSize(new java.awt.Dimension(20, 50));
        pnlBarraSuperior.setLayout(new java.awt.BorderLayout());

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        pnlBarraSuperior.add(jButton1, java.awt.BorderLayout.LINE_END);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(pnlBarraSuperior, gridBagConstraints);

        pnlBarraLateralIzq.setBackground(new java.awt.Color(67, 76, 94));
        pnlBarraLateralIzq.setPreferredSize(new java.awt.Dimension(200, 100));
        pnlBarraLateralIzq.setLayout(new java.awt.BorderLayout());
        pnlBarraLateralIzq.add(filler1, java.awt.BorderLayout.PAGE_END);
        pnlBarraLateralIzq.add(filler2, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBackground(new java.awt.Color(67, 76, 94));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlBotonMatenimientos.setBackground(new java.awt.Color(76, 86, 106));
        pnlBotonMatenimientos.setRoundBottomLeft(20);
        pnlBotonMatenimientos.setRoundBottomRight(20);
        pnlBotonMatenimientos.setRoundTopLeft(20);
        pnlBotonMatenimientos.setRoundTopRight(20);
        pnlBotonMatenimientos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlBotonMatenimientosMouseClicked(evt);
            }
        });
        pnlBotonMatenimientos.setLayout(new java.awt.BorderLayout());

        btnMantenimientosMenu.setText("Mantenimientos");
        btnMantenimientosMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMantenimientosMenuMouseEntered(evt);
            }
        });
        btnMantenimientosMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMantenimientosMenuActionPerformed(evt);
            }
        });
        pnlBotonMatenimientos.add(btnMantenimientosMenu, java.awt.BorderLayout.CENTER);

        jPanel4.add(pnlBotonMatenimientos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 180, 50));

        panelesBordesRedondeados2.setBackground(new java.awt.Color(76, 86, 106));
        panelesBordesRedondeados2.setRoundBottomLeft(20);
        panelesBordesRedondeados2.setRoundBottomRight(20);
        panelesBordesRedondeados2.setRoundTopLeft(20);
        panelesBordesRedondeados2.setRoundTopRight(20);
        panelesBordesRedondeados2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(236, 239, 244));
        jLabel2.setText("Consultas");
        panelesBordesRedondeados2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 15, -1, -1));
        jLabel2.getAccessibleContext().setAccessibleDescription("");

        jPanel4.add(panelesBordesRedondeados2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 180, 50));

        panelesBordesRedondeados3.setBackground(new java.awt.Color(76, 86, 106));
        panelesBordesRedondeados3.setRoundBottomLeft(20);
        panelesBordesRedondeados3.setRoundBottomRight(20);
        panelesBordesRedondeados3.setRoundTopLeft(20);
        panelesBordesRedondeados3.setRoundTopRight(20);
        panelesBordesRedondeados3.setLayout(new java.awt.BorderLayout());

        btnProcesosMenu.setText("Procesos");
        panelesBordesRedondeados3.add(btnProcesosMenu, java.awt.BorderLayout.CENTER);

        jPanel4.add(panelesBordesRedondeados3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 180, 50));

        pnlBarraLateralIzq.add(jPanel4, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(pnlBarraLateralIzq, gridBagConstraints);

        pnlContenido.setBackground(new java.awt.Color(46, 52, 64));
        pnlContenido.setPreferredSize(new java.awt.Dimension(700, 524));
        pnlContenido.setLayout(new java.awt.BorderLayout());

        pnlConsultas.setLayout(new java.awt.BorderLayout());

        pnlBarraopciones.setBackground(new java.awt.Color(255, 153, 102));
        pnlBarraopciones.add(filler3);

        pnlConsultas.add(pnlBarraopciones, java.awt.BorderLayout.PAGE_START);

        pnlTablas.setLayout(new java.awt.BorderLayout());

        tblDepartamentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID del Departamento", "Descripción del departamento"
            }
        ));
        tblDepartamentos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblDepartamentos);

        pnlTablas.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pnlConsultas.add(pnlTablas, java.awt.BorderLayout.CENTER);

        pnlContenido.add(pnlConsultas, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(pnlContenido, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void pnlBotonMatenimientosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlBotonMatenimientosMouseClicked
        
    }//GEN-LAST:event_pnlBotonMatenimientosMouseClicked

    private void btnMantenimientosMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMantenimientosMenuActionPerformed
        String[] opciones = {"Usuarios", "Departamentos", "Puestos", "Empleados"};
        ActionListener[] acciones = {
            e -> abrirVentanaUsuarios(),
            e -> abrirVentanaDepartamentos(),
            e -> abrirVentanaPuestos(),
            e -> abrirVentanaEmpleados()
        };

        popupMenuMantenimientos.show(pnlBotonMatenimientos, 200, pnlBotonMatenimientos.getHeight()-50);

    }//GEN-LAST:event_btnMantenimientosMenuActionPerformed

    private void btnMantenimientosMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMantenimientosMenuMouseEntered
        btnMantenimientosMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btnMantenimientosMenuMouseEntered

    private Departamentos ventanaDepartamentos = null;

    private Usuarios ventanaUsuarios = null;
    
    private Empleados ventanaEmpleados = null;
    
    private Puestos ventanaPuestos = null;

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
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Inicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
 /*java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Inicio().setVisible(true);
            }
        }); */
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMantenimientosMenu;
    private javax.swing.JButton btnProcesosMenu;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private Utilidades.PanelesBordesRedondeados panelesBordesRedondeados2;
    private Utilidades.PanelesBordesRedondeados panelesBordesRedondeados3;
    private javax.swing.JPanel pnlBarraLateralIzq;
    private javax.swing.JPanel pnlBarraSuperior;
    private javax.swing.JPanel pnlBarraopciones;
    private Utilidades.PanelesBordesRedondeados pnlBotonMatenimientos;
    private javax.swing.JPanel pnlConsultas;
    private javax.swing.JPanel pnlContenido;
    private javax.swing.JPanel pnlTablas;
    private javax.swing.JPopupMenu popupMenuMantenimientos;
    private javax.swing.JTable tblDepartamentos;
    // End of variables declaration//GEN-END:variables
}
