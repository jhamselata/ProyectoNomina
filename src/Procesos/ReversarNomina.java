/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Procesos;

import Utilidades.ConsultaNominas;
import Utilidades.EstilosTabla;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Duanel
 */
public class ReversarNomina extends javax.swing.JFrame {

    public ReversarNomina() {
        initComponents();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        
        ImageIcon icono = new ImageIcon(getClass().getResource("/Iconos/ProgramIcon.png"));
        this.setIconImage(icono.getImage());
        
        setTitle("Reversar Nómina");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla.
        
        ConsultaNominas.cargarNominasConEmpleados(tblNominas, 
        "src/BaseDeDatos/Nominas.txt", 
        "src/BaseDeDatos/Empleados.txt", "src/BaseDeDatos/Cooperativa.txt");
        
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tblNominas.getModel());
        tblNominas.setRowSorter(sorter);
        
        jdcFecha.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                Date fecha = jdcFecha.getDate();
                if (fecha != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fechaFormat = sdf.format(fecha);
                
                sorter.setRowFilter(RowFilter.regexFilter("^" + fechaFormat + "$", 3));
                } else {
                    sorter.setRowFilter(null);
                }
            }
        });
        
        configurarEstilosUI();
        
    }
    private void configurarEstilosUI() {
    // Ajuste proporcional automático (por defecto)
    EstilosTabla.aplicarEstiloPrincipal(tblNominas, jScrollPane4);
    
    // Todas las columnas del mismo tamaño
   // EstilosTabla.aplicarEstiloConColumnasIguales(jTable2, jScrollPane2);
    
    // Columnas con porcentajes específicos
    //double[] pesos = {0.15, 0.25, 0.20, 0.25, 0.15}; // ID, Nombre, Salario, AFP, ARS
    //EstilosTabla.aplicarEstiloConPesos(jTable3, jScrollPane3, pesos);
}
    
    
    public void ProcesoReversar() {
    File coopFile   = new File("src/BaseDeDatos/Cooperativa.txt");
    File nominaFile = new File("src/BaseDeDatos/Nominas.txt");

    SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd");

    Date fechaSeleccionada = jdcFecha.getDate();
    if (fechaSeleccionada == null) {
        JOptionPane.showMessageDialog(null,
                "Debe seleccionar la fecha a reversar.",
                "Fecha no seleccionada",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    String fechaString = sdfFull.format(fechaSeleccionada);

    if (!nominaFile.exists()) {
        JOptionPane.showMessageDialog(null,
                "El archivo de nómina no se encuentra.",
                "Archivo no encontrado",
                JOptionPane.ERROR_MESSAGE);
        return;
    }

    int opcion = JOptionPane.showConfirmDialog(
            null,
            "¿Seguro de reversar la nómina con la fecha: " + fechaString + "?",
            "Confirmar reversión",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
    );

    if (opcion != JOptionPane.YES_OPTION) {
        return;
    }

    List<String> lineasNuevas = new ArrayList<>();
    Map<String, Double> descuentosCooperativa = new HashMap<>();
    boolean encontrado = false;

    try (BufferedReader br = new BufferedReader(new FileReader(nominaFile))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            if (linea.trim().isEmpty()) continue;
            String[] cols = linea.split(";");
            if (cols.length < 9) continue;
            String fechaLinea = cols[2].trim();
            String idEmp      = cols[1].trim();
            double coop       = Double.parseDouble(cols[6].trim());
            if (fechaLinea.equals(fechaString)) {
                encontrado = true;
                if (coop > 0) {
                    descuentosCooperativa.put(idEmp, coop);
                }
            } else {
                lineasNuevas.add(linea);
            }
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null,
                "Error al leer la nómina: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (!encontrado) {
        JOptionPane.showMessageDialog(null,
                "No es posible reversar porque no existe una nómina con fecha " + fechaString + ".",
                "No se puede reversar",
                JOptionPane.ERROR_MESSAGE);
        return;
    }

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(nominaFile, false))) {
        for (String ln : lineasNuevas) {
            bw.write(ln);
            bw.newLine();
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null,
                "Error al actualizar el archivo de nómina: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (!descuentosCooperativa.isEmpty() && coopFile.exists()) {
        List<String> lineasNuevasCoop = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(coopFile))) {
            String ln;
            while ((ln = br.readLine()) != null) {
                if (ln.trim().isEmpty()) {
                    lineasNuevasCoop.add(ln);
                    continue;
                }
                String[] p = ln.split(";");
                if (p.length < 3) {
                    lineasNuevasCoop.add(ln);
                    continue;
                }
                String id   = p[0].trim();
                double pct  = Double.parseDouble(p[1].trim());
                double bal  = Double.parseDouble(p[2].trim());
                if (descuentosCooperativa.containsKey(id)) {
                    bal -= descuentosCooperativa.get(id);
                    if (bal < 0) bal = 0;
                }
                lineasNuevasCoop.add(String.format(Locale.US, "%s;%.2f;%.2f", id, pct, bal));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al leer archivo de cooperativa: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(coopFile, false))) {
            for (String s : lineasNuevasCoop) {
                bw.write(s);
                bw.newLine();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al actualizar archivo de cooperativa: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    ///Usar solo el mes y año para eliminar la nómina///
    
    SimpleDateFormat sdfMesAnio = new SimpleDateFormat("MM-yyyy");
    String mesAnio = sdfMesAnio.format(fechaSeleccionada);

    String rutaPDF = "src/VolantesDeNómina/" + mesAnio + "-Nómina.pdf";
    File pdf = new File(rutaPDF);
    if (pdf.exists()) {
        pdf.delete();
    }

    JOptionPane.showMessageDialog(null,
            "Nómina del " + fechaString + " reversada exitosamente.",
            "Completado",
            JOptionPane.INFORMATION_MESSAGE);
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlContenido = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblNominas = new javax.swing.JTable();
        jdcFecha = new com.toedter.calendar.JDateChooser();
        pnlBotonReversar = new Utilidades.PanelesBordesRedondeados();
        btnReversar = new javax.swing.JButton();
        pnlBotonSalir = new Utilidades.PanelesBordesRedondeados();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(991, 618));

        pnlContenido.setBackground(new java.awt.Color(76, 86, 106));
        pnlContenido.setForeground(new java.awt.Color(76, 86, 106));
        pnlContenido.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblNominas.setBackground(new java.awt.Color(204, 153, 0));
        tblNominas.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        tblNominas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "ID Empleado", "Nombre Empleado", "Fecha Nómina", "Salario", "AFP", "ARS", "Cooperativa", "IRS", "Sueldo Neto", "Balance Coop. Acumulado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNominas.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(tblNominas);
        if (tblNominas.getColumnModel().getColumnCount() > 0) {
            tblNominas.getColumnModel().getColumn(0).setResizable(false);
            tblNominas.getColumnModel().getColumn(1).setResizable(false);
            tblNominas.getColumnModel().getColumn(2).setResizable(false);
            tblNominas.getColumnModel().getColumn(3).setResizable(false);
            tblNominas.getColumnModel().getColumn(4).setResizable(false);
            tblNominas.getColumnModel().getColumn(5).setResizable(false);
            tblNominas.getColumnModel().getColumn(6).setResizable(false);
            tblNominas.getColumnModel().getColumn(7).setResizable(false);
            tblNominas.getColumnModel().getColumn(8).setResizable(false);
            tblNominas.getColumnModel().getColumn(9).setResizable(false);
            tblNominas.getColumnModel().getColumn(10).setResizable(false);
        }

        pnlContenido.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, 883, -1));
        pnlContenido.add(jdcFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 40, 250, 50));

        pnlBotonReversar.setRoundBottomLeft(20);
        pnlBotonReversar.setRoundBottomRight(20);
        pnlBotonReversar.setRoundTopLeft(20);
        pnlBotonReversar.setRoundTopRight(20);
        pnlBotonReversar.setLayout(new java.awt.BorderLayout());

        btnReversar.setText("Reversar");
        btnReversar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReversarActionPerformed(evt);
            }
        });
        pnlBotonReversar.add(btnReversar, java.awt.BorderLayout.CENTER);

        pnlContenido.add(pnlBotonReversar, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 560, 130, 50));

        pnlBotonSalir.setRoundBottomLeft(20);
        pnlBotonSalir.setRoundBottomRight(20);
        pnlBotonSalir.setRoundTopLeft(20);
        pnlBotonSalir.setRoundTopRight(20);
        pnlBotonSalir.setLayout(new java.awt.BorderLayout());

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        pnlBotonSalir.add(btnSalir, java.awt.BorderLayout.CENTER);

        pnlContenido.add(pnlBotonSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 560, 130, 50));

        getContentPane().add(pnlContenido, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnReversarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReversarActionPerformed
        ProcesoReversar();
        
        ConsultaNominas.cargarNominasConEmpleados(tblNominas, 
        "src/BaseDeDatos/Nominas.txt", 
        "src/BaseDeDatos/Empleados.txt", "src/BaseDeDatos/Cooperativa.txt");
    }//GEN-LAST:event_btnReversarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

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
            java.util.logging.Logger.getLogger(ReversarNomina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReversarNomina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReversarNomina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReversarNomina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReversarNomina().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReversar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JScrollPane jScrollPane4;
    private com.toedter.calendar.JDateChooser jdcFecha;
    private Utilidades.PanelesBordesRedondeados pnlBotonReversar;
    private Utilidades.PanelesBordesRedondeados pnlBotonSalir;
    private javax.swing.JPanel pnlContenido;
    private javax.swing.JTable tblNominas;
    // End of variables declaration//GEN-END:variables
}
