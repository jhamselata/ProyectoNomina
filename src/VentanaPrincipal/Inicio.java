/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package VentanaPrincipal;

import Mantenimientos.Departamentos;
import Mantenimientos.Empleados;
import Mantenimientos.Puestos;
import Mantenimientos.Usuarios;
import static Utilidades.cargarDatosenTabla.cargarEnTabla;
import java.awt.CardLayout;
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
    private void UnaVentanaAlaVez() {
    }

    ;
    
    
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

    private JPopupMenu popupMenuConsultas;

    public void popUpMenuConsultas() {
        popupMenuConsultas = new JPopupMenu();
        popupMenuConsultas.setOpaque(false);
        popupMenuConsultas.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Menú para mostrar panel de departamentos
        popupMenuConsultas.add(createStyledMenuItem("Departamentos", e -> {
            CardLayout cl = (CardLayout) pnlContenido.getLayout();
            cl.show(pnlContenido, "DEPARTAMENTOS");
            cargarEnTabla(tblDepartamentos, "src/BaseDeDatos/Departamentos.txt");
        }));

        // Menú para mostrar panel de puestos
        popupMenuConsultas.add(createStyledMenuItem("Puestos", e -> {
            CardLayout cl = (CardLayout) pnlContenido.getLayout();
            cl.show(pnlContenido, "PUESTOS");
            cargarEnTabla(tblPuestos, "src/BaseDeDatos/Puestos.txt");
        }));

        // Si quieres mostrar Empleados en panel o ventana, elige una
        popupMenuConsultas.add(createStyledMenuItem("Empleados", e -> abrirVentanaEmpleados()));
    }

    public void popUpMenu() {
        popupMenuMantenimientos = new JPopupMenu();
        popupMenuMantenimientos.setOpaque(false);
        popupMenuMantenimientos.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JMenuItem usuariosItem = createStyledMenuItem("Usuarios", e -> {
            abrirVentanaUsuarios();
        });
        //Desactiva la opción de mantenimiento de usuarios si el usuario no es administrador
        if ("1".equals(rolUsuario)) {
            usuariosItem.setEnabled(false);
        }
        popupMenuMantenimientos.add(usuariosItem);

        JMenuItem departamentosItem = createStyledMenuItem("Departamentos", e -> {
            abrirVentanaDepartamentos();
        });
        popupMenuMantenimientos.add(departamentosItem);

        JMenuItem puestosItem = createStyledMenuItem("Puestos", e -> {
            abrirVentanaPuestos();
        });
        popupMenuMantenimientos.add(puestosItem);

        JMenuItem empleadosItem = createStyledMenuItem("Empleados", e -> {
            abrirVentanaEmpleados();
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
        menu.show(componente, 200, componente.getHeight() - 50);
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
        btnProcesosMenu.setBackground(new Color(0, 0, 0, 0));
        btnConsultasMenu.setBackground(new Color(0, 0, 0, 0));
        txtBusquedaDepartamentos.setBackground(new Color(0, 0, 0, 0));
        popUpMenu();
        popUpMenuConsultas();

        //Oculta paneles por defecto
        pnlConsultaDepartamentos.setVisible(false);
        pnlConsultaPuestos.setVisible(false);

        if ("1".equals(rolUsuario)) {
            btnProcesosMenu.setEnabled(false);
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
        java.awt.GridBagConstraints gridBagConstraints;

        popupMenuMantenimientos = new javax.swing.JPopupMenu();
        pnlBarraSuperior = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        pnlBarraLateralIzq = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 50), new java.awt.Dimension(32767, 10));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 50), new java.awt.Dimension(32767, 10));
        pnlContenedorBotonespnlIzq = new javax.swing.JPanel();
        pnlBotonMatenimientos = new Utilidades.PanelesBordesRedondeados();
        btnMantenimientosMenu = new javax.swing.JButton();
        pnlBotonConsultas = new Utilidades.PanelesBordesRedondeados();
        btnConsultasMenu = new javax.swing.JButton();
        pnlBtonProcesos = new Utilidades.PanelesBordesRedondeados();
        btnProcesosMenu = new javax.swing.JButton();
        pnlContenido = new javax.swing.JPanel();
        pnlVacio = new javax.swing.JPanel();
        pnlConsultaPuestos = new javax.swing.JPanel();
        pnlBarraopcionesPuestos = new javax.swing.JPanel();
        pnlOpcionesConsultaDep1 = new javax.swing.JPanel();
        pnlBusquedaDepartamentos1 = new Utilidades.PanelesBordesRedondeados();
        txtBusquedaDepartamentos1 = new javax.swing.JTextField();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 150), new java.awt.Dimension(0, 200), new java.awt.Dimension(32767, 200));
        cbbxFiltroBusqueda1 = new javax.swing.JComboBox<>();
        pnlTablaPuestos = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPuestos = new javax.swing.JTable();
        pnlConsultaDepartamentos = new javax.swing.JPanel();
        pnlBarraopcionesDepartamentos = new javax.swing.JPanel();
        pnlOpcionesConsultaDep = new javax.swing.JPanel();
        pnlBusquedaDepartamentos = new Utilidades.PanelesBordesRedondeados();
        txtBusquedaDepartamentos = new javax.swing.JTextField();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 150), new java.awt.Dimension(0, 200), new java.awt.Dimension(32767, 200));
        cbbxFiltroBusqueda = new javax.swing.JComboBox<>();
        pnlTablaDepartamentos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDepartamentos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
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

        pnlContenedorBotonespnlIzq.setBackground(new java.awt.Color(67, 76, 94));
        pnlContenedorBotonespnlIzq.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        pnlContenedorBotonespnlIzq.add(pnlBotonMatenimientos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 180, 50));

        pnlBotonConsultas.setBackground(new java.awt.Color(76, 86, 106));
        pnlBotonConsultas.setRoundBottomLeft(20);
        pnlBotonConsultas.setRoundBottomRight(20);
        pnlBotonConsultas.setRoundTopLeft(20);
        pnlBotonConsultas.setRoundTopRight(20);
        pnlBotonConsultas.setLayout(new java.awt.BorderLayout());

        btnConsultasMenu.setText("Consultas");
        btnConsultasMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultasMenuActionPerformed(evt);
            }
        });
        pnlBotonConsultas.add(btnConsultasMenu, java.awt.BorderLayout.CENTER);

        pnlContenedorBotonespnlIzq.add(pnlBotonConsultas, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 180, 50));

        pnlBtonProcesos.setBackground(new java.awt.Color(76, 86, 106));
        pnlBtonProcesos.setRoundBottomLeft(20);
        pnlBtonProcesos.setRoundBottomRight(20);
        pnlBtonProcesos.setRoundTopLeft(20);
        pnlBtonProcesos.setRoundTopRight(20);
        pnlBtonProcesos.setLayout(new java.awt.BorderLayout());

        btnProcesosMenu.setText("Procesos");
        pnlBtonProcesos.add(btnProcesosMenu, java.awt.BorderLayout.CENTER);

        pnlContenedorBotonespnlIzq.add(pnlBtonProcesos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 180, 50));

        pnlBarraLateralIzq.add(pnlContenedorBotonespnlIzq, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(pnlBarraLateralIzq, gridBagConstraints);

        pnlContenido.setBackground(new java.awt.Color(46, 52, 64));
        pnlContenido.setPreferredSize(new java.awt.Dimension(700, 524));
        pnlContenido.setLayout(new java.awt.CardLayout());

        pnlVacio.setBackground(new java.awt.Color(46, 52, 64));
        pnlVacio.setLayout(new java.awt.BorderLayout());
        pnlContenido.add(pnlVacio, "VACIO");

        pnlConsultaPuestos.setLayout(new java.awt.BorderLayout());

        pnlBarraopcionesPuestos.setBackground(new java.awt.Color(255, 153, 102));
        pnlBarraopcionesPuestos.setLayout(new java.awt.BorderLayout());

        pnlOpcionesConsultaDep1.setBackground(new java.awt.Color(59, 66, 82));

        pnlBusquedaDepartamentos1.setBackground(new java.awt.Color(76, 86, 106));
        pnlBusquedaDepartamentos1.setPreferredSize(new java.awt.Dimension(500, 40));
        pnlBusquedaDepartamentos1.setRoundBottomLeft(40);
        pnlBusquedaDepartamentos1.setRoundBottomRight(40);
        pnlBusquedaDepartamentos1.setRoundTopLeft(40);
        pnlBusquedaDepartamentos1.setRoundTopRight(40);
        pnlBusquedaDepartamentos1.setLayout(new java.awt.BorderLayout());

        txtBusquedaDepartamentos1.setBackground(new java.awt.Color(236, 239, 244));
        txtBusquedaDepartamentos1.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        txtBusquedaDepartamentos1.setForeground(new java.awt.Color(236, 239, 244));
        txtBusquedaDepartamentos1.setBorder(null);
        txtBusquedaDepartamentos1.setMargin(new java.awt.Insets(10, 10, 10, 10));
        txtBusquedaDepartamentos1.setName(""); // NOI18N
        txtBusquedaDepartamentos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBusquedaDepartamentos1ActionPerformed(evt);
            }
        });
        txtBusquedaDepartamentos1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusquedaDepartamentos1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBusquedaDepartamentos1KeyTyped(evt);
            }
        });
        pnlBusquedaDepartamentos1.add(txtBusquedaDepartamentos1, java.awt.BorderLayout.CENTER);
        pnlBusquedaDepartamentos1.add(filler6, java.awt.BorderLayout.LINE_END);
        pnlBusquedaDepartamentos1.add(filler7, java.awt.BorderLayout.LINE_START);

        pnlOpcionesConsultaDep1.add(pnlBusquedaDepartamentos1);
        pnlOpcionesConsultaDep1.add(filler8);

        cbbxFiltroBusqueda1.setPreferredSize(new java.awt.Dimension(200, 40));
        cbbxFiltroBusqueda1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbxFiltroBusqueda1ActionPerformed(evt);
            }
        });
        pnlOpcionesConsultaDep1.add(cbbxFiltroBusqueda1);

        pnlBarraopcionesPuestos.add(pnlOpcionesConsultaDep1, java.awt.BorderLayout.CENTER);

        pnlConsultaPuestos.add(pnlBarraopcionesPuestos, java.awt.BorderLayout.PAGE_START);

        pnlTablaPuestos.setLayout(new java.awt.BorderLayout());

        tblPuestos.setBackground(new java.awt.Color(204, 153, 0));
        tblPuestos.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        tblPuestos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID del Puesto", "Descripción del Puesto"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPuestos.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(tblPuestos);
        if (tblPuestos.getColumnModel().getColumnCount() > 0) {
            tblPuestos.getColumnModel().getColumn(0).setResizable(false);
            tblPuestos.getColumnModel().getColumn(1).setResizable(false);
        }

        pnlTablaPuestos.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        pnlConsultaPuestos.add(pnlTablaPuestos, java.awt.BorderLayout.CENTER);

        pnlContenido.add(pnlConsultaPuestos, "PUESTOS");

        pnlConsultaDepartamentos.setLayout(new java.awt.BorderLayout());

        pnlBarraopcionesDepartamentos.setBackground(new java.awt.Color(255, 153, 102));
        pnlBarraopcionesDepartamentos.setLayout(new java.awt.BorderLayout());

        pnlOpcionesConsultaDep.setBackground(new java.awt.Color(59, 66, 82));

        pnlBusquedaDepartamentos.setBackground(new java.awt.Color(76, 86, 106));
        pnlBusquedaDepartamentos.setPreferredSize(new java.awt.Dimension(500, 40));
        pnlBusquedaDepartamentos.setRoundBottomLeft(40);
        pnlBusquedaDepartamentos.setRoundBottomRight(40);
        pnlBusquedaDepartamentos.setRoundTopLeft(40);
        pnlBusquedaDepartamentos.setRoundTopRight(40);
        pnlBusquedaDepartamentos.setLayout(new java.awt.BorderLayout());

        txtBusquedaDepartamentos.setBackground(new java.awt.Color(236, 239, 244));
        txtBusquedaDepartamentos.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        txtBusquedaDepartamentos.setForeground(new java.awt.Color(236, 239, 244));
        txtBusquedaDepartamentos.setBorder(null);
        txtBusquedaDepartamentos.setMargin(new java.awt.Insets(10, 10, 10, 10));
        txtBusquedaDepartamentos.setName(""); // NOI18N
        txtBusquedaDepartamentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBusquedaDepartamentosActionPerformed(evt);
            }
        });
        txtBusquedaDepartamentos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusquedaDepartamentosKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBusquedaDepartamentosKeyTyped(evt);
            }
        });
        pnlBusquedaDepartamentos.add(txtBusquedaDepartamentos, java.awt.BorderLayout.CENTER);
        pnlBusquedaDepartamentos.add(filler5, java.awt.BorderLayout.LINE_END);
        pnlBusquedaDepartamentos.add(filler4, java.awt.BorderLayout.LINE_START);

        pnlOpcionesConsultaDep.add(pnlBusquedaDepartamentos);
        pnlOpcionesConsultaDep.add(filler3);

        cbbxFiltroBusqueda.setPreferredSize(new java.awt.Dimension(200, 40));
        cbbxFiltroBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbxFiltroBusquedaActionPerformed(evt);
            }
        });
        pnlOpcionesConsultaDep.add(cbbxFiltroBusqueda);

        pnlBarraopcionesDepartamentos.add(pnlOpcionesConsultaDep, java.awt.BorderLayout.CENTER);

        pnlConsultaDepartamentos.add(pnlBarraopcionesDepartamentos, java.awt.BorderLayout.PAGE_START);

        pnlTablaDepartamentos.setLayout(new java.awt.BorderLayout());

        tblDepartamentos.setBackground(new java.awt.Color(204, 153, 0));
        tblDepartamentos.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        tblDepartamentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID del Departamento", "Descripción del Departamento"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDepartamentos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblDepartamentos);
        if (tblDepartamentos.getColumnModel().getColumnCount() > 0) {
            tblDepartamentos.getColumnModel().getColumn(0).setResizable(false);
            tblDepartamentos.getColumnModel().getColumn(1).setResizable(false);
        }

        pnlTablaDepartamentos.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pnlConsultaDepartamentos.add(pnlTablaDepartamentos, java.awt.BorderLayout.CENTER);

        pnlContenido.add(pnlConsultaDepartamentos, "DEPARTAMENTOS");

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

        popupMenuMantenimientos.show(pnlBotonMatenimientos, 200, pnlBotonMatenimientos.getHeight() - 50);
    }//GEN-LAST:event_btnMantenimientosMenuActionPerformed

    private void btnMantenimientosMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMantenimientosMenuMouseEntered
        btnMantenimientosMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btnMantenimientosMenuMouseEntered

    private void txtBusquedaDepartamentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusquedaDepartamentosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusquedaDepartamentosActionPerformed

    private void txtBusquedaDepartamentosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaDepartamentosKeyPressed

    }//GEN-LAST:event_txtBusquedaDepartamentosKeyPressed

    private void txtBusquedaDepartamentosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaDepartamentosKeyTyped

    }//GEN-LAST:event_txtBusquedaDepartamentosKeyTyped

    private void cbbxFiltroBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbxFiltroBusquedaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbxFiltroBusquedaActionPerformed

    private void btnConsultasMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultasMenuActionPerformed
        if (popupMenuConsultas == null) {
            popUpMenuConsultas();
        }
        popupMenuConsultas.show(pnlBotonConsultas, 200, pnlBotonConsultas.getHeight() + 20);
    }//GEN-LAST:event_btnConsultasMenuActionPerformed

    private void txtBusquedaDepartamentos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusquedaDepartamentos1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusquedaDepartamentos1ActionPerformed

    private void txtBusquedaDepartamentos1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaDepartamentos1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusquedaDepartamentos1KeyPressed

    private void txtBusquedaDepartamentos1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaDepartamentos1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusquedaDepartamentos1KeyTyped

    private void cbbxFiltroBusqueda1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbxFiltroBusqueda1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbxFiltroBusqueda1ActionPerformed

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

    private javax.swing.JPopupMenu popupMenuProcesos;


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConsultasMenu;
    private javax.swing.JButton btnMantenimientosMenu;
    private javax.swing.JButton btnProcesosMenu;
    private javax.swing.JComboBox<String> cbbxFiltroBusqueda;
    private javax.swing.JComboBox<String> cbbxFiltroBusqueda1;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.Box.Filler filler8;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnlBarraLateralIzq;
    private javax.swing.JPanel pnlBarraSuperior;
    private javax.swing.JPanel pnlBarraopcionesDepartamentos;
    private javax.swing.JPanel pnlBarraopcionesPuestos;
    private Utilidades.PanelesBordesRedondeados pnlBotonConsultas;
    private Utilidades.PanelesBordesRedondeados pnlBotonMatenimientos;
    private Utilidades.PanelesBordesRedondeados pnlBtonProcesos;
    private Utilidades.PanelesBordesRedondeados pnlBusquedaDepartamentos;
    private Utilidades.PanelesBordesRedondeados pnlBusquedaDepartamentos1;
    private javax.swing.JPanel pnlConsultaDepartamentos;
    private javax.swing.JPanel pnlConsultaPuestos;
    private javax.swing.JPanel pnlContenedorBotonespnlIzq;
    private javax.swing.JPanel pnlContenido;
    private javax.swing.JPanel pnlOpcionesConsultaDep;
    private javax.swing.JPanel pnlOpcionesConsultaDep1;
    private javax.swing.JPanel pnlTablaDepartamentos;
    private javax.swing.JPanel pnlTablaPuestos;
    private javax.swing.JPanel pnlVacio;
    private javax.swing.JPopupMenu popupMenuMantenimientos;
    private javax.swing.JTable tblDepartamentos;
    private javax.swing.JTable tblPuestos;
    private javax.swing.JTextField txtBusquedaDepartamentos;
    private javax.swing.JTextField txtBusquedaDepartamentos1;
    // End of variables declaration//GEN-END:variables
}
