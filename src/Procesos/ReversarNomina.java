/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Procesos;

import Utilidades.ConsultaNominas;
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
        "src/BaseDeDatos/Empleados.txt");
        
    }
    
    public void ProcesoReversar() {
        File Coop = new File("src/BaseDeDatos/Cooperativa.txt");
        File Nomina = new File("src/BaseDeDatos/Nominas.txt");
        
        SimpleDateFormat sdfFull = new SimpleDateFormat("dd/MM/yyyy");
        
        Date fechaSeleccionada = jdcFecha.getDate();
        
        if (fechaSeleccionada == null) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar la fecha a reversar.", "Fecha no seleccionada", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String fechaString = sdfFull.format(fechaSeleccionada);
        
        if (!Nomina.exists()) {
            JOptionPane.showMessageDialog(null, "El archivo de nómina no se encuentra", "Archivo no encontrado", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int opcion = JOptionPane.showConfirmDialog(null, "¿Seguro de reversar la nómina con la fecha: " + fechaString + "?", "Confirmar reversión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        
        List<String> LineasNuevas = new ArrayList<>();
        Map<String, Double> descuentosCooperativa = new HashMap<>();
        
        boolean encontrado = false;
        
        try (BufferedReader br = new BufferedReader(new FileReader(Nomina))) {
            String linea;
            
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] cols = linea.split(";");
                if (cols.length < 9) continue;
                
                String fechaLinea = cols[2].trim();
                String idEmp = cols[1].trim();
                double coop = Double.parseDouble(cols[6].trim());
                
                if (fechaLinea.equals(fechaString)) {
                    encontrado = true;
                    
                    if (coop > 0) {
                        descuentosCooperativa.put(idEmp, coop);
                    }
                } else {
                    LineasNuevas.add(linea);
                }
            }
        }
        
        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "No es posible reversar porque no existe una nómina en la fecha" + fechaString, "No se puede reversar", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Nomina, false))) {
            for (String In : LineasNuevas) {
                bw.write(In);
                bw.newLine();
            }
        }
        
        if (!descuentosCooperativa.isEmpty() && Coop.exists()) {
            List<String> lineasNuevasCoop = new ArrayList<>();
            
            try (BufferedReader br = new BufferedReader(new FileReader(Coop))) {
                String I;
                
                while ((I = br.readLine())!= null) {
                    if (I.trim().isEmpty()) {
                        lineasNuevasCoop.add(I);
                        continue;
                    }
                    
                    String[] p = I.split(";");
                    if (p.length < 3) {
                        lineasNuevasCoop.add(I);
                        continue;
                    }
                    
                    String id = p[0].trim();
                    double pct = Double.parseDouble(p[1].trim());
                    double bal = Double.parseDouble(p[2].trim());
                    
                    if (descuentosCooperativa.containsKey(id)) {
                        bal = bal - descuentosCooperativa.get(id);
                        if (bal < 0) bal = 0;
                    }
                    
                    lineasNuevasCoop.add(String.format(Locale.US, "%s;%.2f,%.2f", id, pct, bal));
                }
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(Cooperativa, false))) {
                for (String s : lineasNuevasCoop) {
                    bw.write(s);
                    bw.newLine();
                }
            }
        }
        
        String rutaPDF = "src/VolantesDeNómina/Nominas.txt" + fechaString.replace("/", "-") + ".pdf";
        
        File pdf = new File(rutaPDF);
        
        if (pdf.exists()) {
            pdf.delete();
        }
        
        JOptionPane.showMessageDialog(null, "Nómina del" + fechString + " reversada exitosamente.", "Completado", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception ex) {
    ex.printStackTrace();
    JOptionPane.showMessageDialog(null, "Error al reversar la nómina: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        pnlBotonReverser = new Utilidades.PanelesBordesRedondeados();
        btnReversar = new javax.swing.JButton();

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
                "ID", "ID Empleado", "Nombre Empleado", "Fecha Nómina", "Salario", "AFP", "ARS", "Cooperativa", "IRS", "Sueldo Neto", "Status"
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

        pnlContenido.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, 883, -1));
        pnlContenido.add(jdcFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, -1, 50));

        pnlBotonReverser.setRoundBottomLeft(20);
        pnlBotonReverser.setRoundBottomRight(20);
        pnlBotonReverser.setRoundTopLeft(20);
        pnlBotonReverser.setRoundTopRight(20);
        pnlBotonReverser.setLayout(new java.awt.BorderLayout());

        btnReversar.setText("Reversar");
        btnReversar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReversarActionPerformed(evt);
            }
        });
        pnlBotonReverser.add(btnReversar, java.awt.BorderLayout.CENTER);

        pnlContenido.add(pnlBotonReverser, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 130, 50));

        getContentPane().add(pnlContenido, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnReversarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReversarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnReversarActionPerformed

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
    private javax.swing.JScrollPane jScrollPane4;
    private com.toedter.calendar.JDateChooser jdcFecha;
    private Utilidades.PanelesBordesRedondeados pnlBotonReverser;
    private javax.swing.JPanel pnlContenido;
    private javax.swing.JTable tblNominas;
    // End of variables declaration//GEN-END:variables
}
