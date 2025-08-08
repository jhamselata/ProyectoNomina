/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package VentanaPrincipal;

import Login.Login;
import Mantenimientos.Departamentos;
import Mantenimientos.Empleados;
import Mantenimientos.Puestos;
import Mantenimientos.Usuarios;
import static Utilidades.cargarDatosenTabla.activarFiltro;
import static Utilidades.cargarDatosenTabla.cargarEnTabla;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
 *
 * @author jhams
 */
public class Inicio extends javax.swing.JFrame {

private void configurarCalendario() {
    // Configurar el listener del JDateChooser
    jdcFecha.addPropertyChangeListener("date", new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Date fechaSeleccionada = jdcFecha.getDate();
            if (fechaSeleccionada != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                txtBusquedaEmpleados.setText(sdf.format(fechaSeleccionada));
            } else {
                txtBusquedaEmpleados.setText("");
            }
        }
    });
    
    // Opcional: Configurar formato de fecha para el calendario
    jdcFecha.setDateFormatString("MM/dd/yyyy");
}
    
    
    private String rolUsuario;
    
    
    private Departamentos ventanaDepartamentos = null;
    private Usuarios ventanaUsuarios = null;
    private Empleados ventanaEmpleados = null;
    private Puestos ventanaPuestos = null;
    
    private void cerrarTodasLasVentanas() {
        if (ventanaUsuarios != null && ventanaUsuarios.isDisplayable()) {
            ventanaUsuarios.dispose();
            ventanaUsuarios = null;
        }
        if (ventanaDepartamentos != null && ventanaDepartamentos.isDisplayable()) {
            ventanaDepartamentos.dispose();
            ventanaDepartamentos = null;
        }
        if (ventanaEmpleados != null && ventanaEmpleados.isDisplayable()) {
            ventanaEmpleados.dispose();
            ventanaEmpleados = null;
        }
        if (ventanaPuestos != null && ventanaPuestos.isDisplayable()) {
            ventanaPuestos.dispose();
            ventanaPuestos = null;
        }
    }

    /**
     * Creates new form Menu
     */
    private void cambiarEstadoInicio(int caso) {
        switch (caso) {
            case 1: {
                txtEstadoInicio.setText("Mantenimiento de Usuarios");
                break;
            }
            case 2: {
                txtEstadoInicio.setText("Mantenimiento de Departamentos");
                break;
            }
            case 3: {
                txtEstadoInicio.setText("Mantenimiento de Puestos");
                break;
            }
            case 4: {
                txtEstadoInicio.setText("Mantenimiento de Empleados");
                break;
            }
            case 5: {
                txtEstadoInicio.setText("Cosulta de Empleados");
                break;
            }
            case 6: {
                txtEstadoInicio.setText("Cosulta de Puestos");
                break;
            }
            case 7: {
                txtEstadoInicio.setText("Cosulta de Departamentos");
                break;
            }

            default: {
                txtEstadoInicio.setText("Menú Principal");
                break;

            }
        }
    }

    private void SetImageLabel(JLabel labelName, String root) {
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(
            image.getImage().getScaledInstance(labelName.getWidth(), labelName.getHeight(), Image.SCALE_SMOOTH)
        );
        labelName.setIcon(icon);
        this.repaint();
    }

    private void abrirVentanaUsuarios() {
        // Cerrar todas las otras ventanas antes de abrir esta
        cerrarTodasLasVentanas();
        
        // Abrir la ventana de usuarios
        ventanaUsuarios = new Usuarios();
        ventanaUsuarios.setVisible(true);
        
        // Agregar listener para detectar cuando se cierre la ventana
        ventanaUsuarios.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                ventanaUsuarios = null;
                cambiarEstadoInicio(0); // Volver al estado principal
            }
        });
    }

    private void abrirVentanaDepartamentos() {
        // Cerrar todas las otras ventanas antes de abrir esta
        cerrarTodasLasVentanas();
        
        // Abrir la ventana de departamentos
        ventanaDepartamentos = new Departamentos();
        ventanaDepartamentos.setVisible(true);
        
        // Agregar listener para detectar cuando se cierre la ventana
        ventanaDepartamentos.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                ventanaDepartamentos = null;
                cambiarEstadoInicio(0); // Volver al estado principal
            }
        });
    }

    private void abrirVentanaEmpleados() {
        // Cerrar todas las otras ventanas antes de abrir esta
        cerrarTodasLasVentanas();
        
        // Abrir la ventana de empleados
        ventanaEmpleados = new Empleados();
        ventanaEmpleados.setVisible(true);
        
        // Agregar listener para detectar cuando se cierre la ventana
        ventanaEmpleados.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                ventanaEmpleados = null;
                cambiarEstadoInicio(0); // Volver al estado principal
            }
        });
    }

    private void abrirVentanaPuestos() {
        // Cerrar todas las otras ventanas antes de abrir esta
        cerrarTodasLasVentanas();
        
        // Abrir la ventana de puestos
        ventanaPuestos = new Puestos();
        ventanaPuestos.setVisible(true);
        
        // Agregar listener para detectar cuando se cierre la ventana
        ventanaPuestos.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                ventanaPuestos = null;
                cambiarEstadoInicio(0); // Volver al estado principal
            }
        });
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

    private JPopupMenu popupMenuProcesos;
    
    
    
    public JTable getTablaEmpleados() {
    return tblEmpleados;
}

    public void popUpMenuConsultas() {
        popupMenuConsultas = new JPopupMenu();
        popupMenuConsultas.setOpaque(false);
        popupMenuConsultas.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        
        // Empleados
        popupMenuConsultas.add(createStyledMenuItem("Empleados", e -> {
            CardLayout cl = (CardLayout) pnlContenido.getLayout();
            cl.show(pnlContenido, "EMPLEADOS");
            cargarEnTabla(tblEmpleados, "src/BaseDeDatos/Empleados.txt");
            activarFiltro(tblEmpleados, txtBusquedaEmpleados, cbbxFiltroEmpleados);
            cambiarEstadoInicio(5);
            cerrarTodasLasVentanas();
        }));

        // Menú para mostrar panel de departamentos
        popupMenuConsultas.add(createStyledMenuItem("Departamentos", e -> {
            CardLayout cl = (CardLayout) pnlContenido.getLayout();
            cl.show(pnlContenido, "DEPARTAMENTOS");
            cargarEnTabla(tblDepartamentos, "src/BaseDeDatos/Departamentos.txt");
            activarFiltro(tblDepartamentos, txtBusquedaDepartamentos, cbbxFiltroBusqueda);
            cambiarEstadoInicio(7);
            cerrarTodasLasVentanas();
        }));

        // Menú para mostrar panel de puestos
        popupMenuConsultas.add(createStyledMenuItem("Puestos", e -> {
            CardLayout cl = (CardLayout) pnlContenido.getLayout();
            cl.show(pnlContenido, "PUESTOS");
            cargarEnTabla(tblPuestos, "src/BaseDeDatos/Puestos.txt");
            activarFiltro(tblPuestos, txtBusquedaPuestos, cbbxFiltroPuestos);
            cambiarEstadoInicio(6);
            cerrarTodasLasVentanas();
        }));
    }

    public void popUpMenu() {
        popupMenuMantenimientos = new JPopupMenu();
        popupMenuMantenimientos.setOpaque(false);
        popupMenuMantenimientos.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JMenuItem usuariosItem = createStyledMenuItem("Usuarios", e -> {
            abrirVentanaUsuarios();
            CardLayout cl = (CardLayout) pnlContenido.getLayout();
            cl.show(pnlContenido, "VACIO");
            cambiarEstadoInicio(1);
        });
        //Desactiva la opción de mantenimiento de usuarios si el usuario no es administrador
        if ("1".equals(rolUsuario)) {
            usuariosItem.setEnabled(false);
        }
        popupMenuMantenimientos.add(usuariosItem);
        
        JMenuItem empleadosItem = createStyledMenuItem("Empleados", e -> {
            abrirVentanaEmpleados();
            CardLayout cl = (CardLayout) pnlContenido.getLayout();
            cl.show(pnlContenido, "VACIO");
            cambiarEstadoInicio(4);
        });
        popupMenuMantenimientos.add(empleadosItem);

        JMenuItem departamentosItem = createStyledMenuItem("Departamentos", e -> {
            abrirVentanaDepartamentos();
            CardLayout cl = (CardLayout) pnlContenido.getLayout();
            cl.show(pnlContenido, "VACIO");
            cambiarEstadoInicio(2);
        });
        popupMenuMantenimientos.add(departamentosItem);
        
        JMenuItem puestosItem = createStyledMenuItem("Puestos", e -> {
            abrirVentanaPuestos();
            CardLayout cl = (CardLayout) pnlContenido.getLayout();
            cl.show(pnlContenido, "VACIO");
            cambiarEstadoInicio(3);
        });
        popupMenuMantenimientos.add(puestosItem);
    }

    public void popUpMenuProcesos() {
        popupMenuProcesos = new JPopupMenu();
        popupMenuProcesos.setOpaque(false);
        popupMenuProcesos.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Menú para mostrar panel de Crear Nómina
        popupMenuProcesos.add(createStyledMenuItem("Crear Nómina", e -> {
            /*CardLayout cl = (CardLayout) pnlContenido.getLayout();
            cl.show(pnlContenido, "Nómina");*/
        }));

        // Menú para mostrar panel de Reversar Nómina
        popupMenuProcesos.add(createStyledMenuItem("Reversar Nómina", e -> {
            /*CardLayout cl = (CardLayout) pnlContenido.getLayout();
            cl.show(pnlContenido, "Nómina");*/
        }));
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

    private void configurarImagenFondo() {
        // Crear un panel personalizado con imagen de fondo
        JPanel panelConFondo = new JPanel() {
            private Image imagenFondo;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Cargar la imagen si no está cargada
                if (imagenFondo == null) {
                    try {
                        imagenFondo = new ImageIcon(getClass().getResource("/Imágenes/ImagenFondoPrincipal.png")).getImage();
                    } catch (Exception e) {
                        System.err.println("No se pudo cargar la imagen de fondo: " + e.getMessage());
                        return;
                    }
                }

                // Dibujar la imagen escalada al tamaño del panel
                if (imagenFondo != null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                    g2d.dispose();
                }
            }
        };

        // Establecer el layout del panel con fondo
        panelConFondo.setLayout(new BorderLayout());

        // Reemplazar el contenido de pnlVacio
        pnlVacio.removeAll();
        pnlVacio.setLayout(new BorderLayout());
        pnlVacio.add(panelConFondo, BorderLayout.CENTER);
        pnlVacio.revalidate();
        pnlVacio.repaint();
    }

    public Inicio(String rol) {
        initComponents();

        configurarImagenFondo();
        
        configurarCalendario();
        
        jdcFecha.setEnabled(false);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        ImageIcon icono = new ImageIcon(getClass().getResource("/Iconos/ProgramIcon.png"));
        this.setIconImage(icono.getImage());

        this.rolUsuario = rol;
        System.out.println(rolUsuario);

        //color de botones
        btnCerrarSesion.setBackground(new Color(0, 0, 0, 0));
        btnProcesosMenu.setBackground(new Color(0, 0, 0, 0));
        btnConsultasMenu.setBackground(new Color(0, 0, 0, 0));
        txtBusquedaDepartamentos.setBackground(new Color(0, 0, 0, 0));
        txtBusquedaPuestos.setBackground(new Color(0, 0, 0, 0));
        txtBusquedaEmpleados.setBackground(new Color(0, 0, 0, 0));

        txtBotoninicioIcono.setBackground(new Color(163, 190, 140));
        txtEstadoInicio.setBackground(new Color(0, 0, 0, 0));
        btnMantenimientosMenu.setBackground(new Color(0, 0, 0, 0));

        //Iconos
        SetImageLabel(txtBotoninicioIcono, "src/Iconos/inicioIcon.png");

        popUpMenu();
        popUpMenuConsultas();

        //Oculta paneles por defecto
        pnlConsultaDepartamentos.setVisible(false);
        pnlConsultaPuestos.setVisible(false);

        if ("1".equals(rolUsuario)) {
            btnProcesosMenu.setEnabled(false);
        }
        txtEstadoInicio.setText("Menú Principal");

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
        pnlBotonInicio = new Utilidades.PanelesBordesRedondeados();
        txtBotoninicioIcono = new javax.swing.JLabel();
        txtEstadoInicio = new javax.swing.JTextField();
        pnlBotonSalir = new Utilidades.PanelesBordesRedondeados();
        txtBotoninicioIcono1 = new javax.swing.JLabel();
        pnlBarraLateralIzq = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 50), new java.awt.Dimension(32767, 10));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 50), new java.awt.Dimension(32767, 10));
        pnlContenedorBotonespnlIzq = new javax.swing.JPanel();
        pnlBotonMatenimientos = new Utilidades.PanelesBordesRedondeados();
        btnCerrarSesion = new javax.swing.JButton();
        pnlBotonConsultas = new Utilidades.PanelesBordesRedondeados();
        btnConsultasMenu = new javax.swing.JButton();
        pnlBtonProcesos = new Utilidades.PanelesBordesRedondeados();
        btnProcesosMenu = new javax.swing.JButton();
        pnlBotonMatenimientos1 = new Utilidades.PanelesBordesRedondeados();
        btnMantenimientosMenu = new javax.swing.JButton();
        pnlContenido = new javax.swing.JPanel();
        pnlVacio = new javax.swing.JPanel();
        pnlConsultaPuestos = new javax.swing.JPanel();
        pnlBarraopcionesPuestos = new javax.swing.JPanel();
        pnlOpcionesConsultaDep1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        pnlBusquedaDepartamentos1 = new Utilidades.PanelesBordesRedondeados();
        txtBusquedaPuestos = new javax.swing.JTextField();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 150), new java.awt.Dimension(0, 200), new java.awt.Dimension(32767, 200));
        jLabel1 = new javax.swing.JLabel();
        cbbxFiltroPuestos = new javax.swing.JComboBox<>();
        pnlTablaPuestos = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPuestos = new javax.swing.JTable();
        pnlConsultaDepartamentos = new javax.swing.JPanel();
        pnlBarraopcionesDepartamentos = new javax.swing.JPanel();
        pnlOpcionesConsultaDep = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        pnlBusquedaDepartamentos = new Utilidades.PanelesBordesRedondeados();
        txtBusquedaDepartamentos = new javax.swing.JTextField();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 150), new java.awt.Dimension(0, 200), new java.awt.Dimension(32767, 200));
        jLabel3 = new javax.swing.JLabel();
        cbbxFiltroBusqueda = new javax.swing.JComboBox<>();
        pnlTablaDepartamentos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDepartamentos = new javax.swing.JTable();
        pnlConsultaEmpleados = new javax.swing.JPanel();
        pnlBarraopcionesEmpleados = new javax.swing.JPanel();
        pnlOpcionesConsultaEmpleados = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        pnlBusquedaDepartamentos2 = new Utilidades.PanelesBordesRedondeados();
        txtBusquedaEmpleados = new javax.swing.JTextField();
        filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler10 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        filler11 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 150), new java.awt.Dimension(0, 200), new java.awt.Dimension(32767, 200));
        jLabel5 = new javax.swing.JLabel();
        cbbxFiltroEmpleados = new javax.swing.JComboBox<>();
        filler12 = new javax.swing.Box.Filler(new java.awt.Dimension(50, 0), new java.awt.Dimension(50, 0), new java.awt.Dimension(50, 32767));
        jdcFecha = new com.toedter.calendar.JDateChooser();
        pnlTablaEmpleados = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblEmpleados = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        pnlBarraSuperior.setBackground(new java.awt.Color(76, 86, 106));
        pnlBarraSuperior.setPreferredSize(new java.awt.Dimension(20, 50));
        pnlBarraSuperior.setLayout(new java.awt.GridBagLayout());

        pnlBotonInicio.setBackground(new java.awt.Color(163, 190, 140));
        pnlBotonInicio.setForeground(new java.awt.Color(163, 190, 140));
        pnlBotonInicio.setPreferredSize(new java.awt.Dimension(70, 40));
        pnlBotonInicio.setRoundBottomLeft(30);
        pnlBotonInicio.setRoundBottomRight(30);
        pnlBotonInicio.setRoundTopLeft(30);
        pnlBotonInicio.setRoundTopRight(30);
        pnlBotonInicio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlBotonInicioMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pnlBotonInicioMouseEntered(evt);
            }
        });
        pnlBotonInicio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pnlBotonInicioKeyPressed(evt);
            }
        });
        pnlBotonInicio.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtBotoninicioIcono.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtBotoninicioIconoMouseClicked(evt);
            }
        });
        pnlBotonInicio.add(txtBotoninicioIcono, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 30, 30));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 20);
        pnlBarraSuperior.add(pnlBotonInicio, gridBagConstraints);

        txtEstadoInicio.setEditable(false);
        txtEstadoInicio.setBackground(new java.awt.Color(255, 204, 102));
        txtEstadoInicio.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        txtEstadoInicio.setForeground(new java.awt.Color(236, 239, 244));
        txtEstadoInicio.setBorder(null);
        txtEstadoInicio.setFocusable(false);
        txtEstadoInicio.setMargin(new java.awt.Insets(10, 10, 10, 10));
        txtEstadoInicio.setName(""); // NOI18N
        txtEstadoInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEstadoInicioActionPerformed(evt);
            }
        });
        txtEstadoInicio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEstadoInicioKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEstadoInicioKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        pnlBarraSuperior.add(txtEstadoInicio, gridBagConstraints);

        pnlBotonSalir.setBackground(new java.awt.Color(191, 97, 106));
        pnlBotonSalir.setForeground(new java.awt.Color(163, 190, 140));
        pnlBotonSalir.setPreferredSize(new java.awt.Dimension(70, 40));
        pnlBotonSalir.setRoundBottomLeft(30);
        pnlBotonSalir.setRoundBottomRight(30);
        pnlBotonSalir.setRoundTopLeft(30);
        pnlBotonSalir.setRoundTopRight(30);
        pnlBotonSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlBotonSalirMouseClicked(evt);
            }
        });
        pnlBotonSalir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pnlBotonSalirKeyPressed(evt);
            }
        });
        pnlBotonSalir.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtBotoninicioIcono1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtBotoninicioIcono1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtBotoninicioIcono1MouseEntered(evt);
            }
        });
        pnlBotonSalir.add(txtBotoninicioIcono1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 20, 20));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 10);
        pnlBarraSuperior.add(pnlBotonSalir, gridBagConstraints);

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

        btnCerrarSesion.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        btnCerrarSesion.setForeground(new java.awt.Color(236, 239, 244));
        btnCerrarSesion.setText("Cerrar Sesión");
        btnCerrarSesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCerrarSesionMouseEntered(evt);
            }
        });
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });
        pnlBotonMatenimientos.add(btnCerrarSesion, java.awt.BorderLayout.CENTER);

        pnlContenedorBotonespnlIzq.add(pnlBotonMatenimientos, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 300, 130, 50));

        pnlBotonConsultas.setBackground(new java.awt.Color(76, 86, 106));
        pnlBotonConsultas.setRoundBottomLeft(20);
        pnlBotonConsultas.setRoundBottomRight(20);
        pnlBotonConsultas.setRoundTopLeft(20);
        pnlBotonConsultas.setRoundTopRight(20);
        pnlBotonConsultas.setLayout(new java.awt.BorderLayout());

        btnConsultasMenu.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        btnConsultasMenu.setForeground(new java.awt.Color(236, 239, 244));
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

        btnProcesosMenu.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        btnProcesosMenu.setForeground(new java.awt.Color(236, 239, 244));
        btnProcesosMenu.setText("Procesos");
        btnProcesosMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcesosMenuActionPerformed(evt);
            }
        });
        pnlBtonProcesos.add(btnProcesosMenu, java.awt.BorderLayout.CENTER);

        pnlContenedorBotonespnlIzq.add(pnlBtonProcesos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 180, 50));

        pnlBotonMatenimientos1.setBackground(new java.awt.Color(76, 86, 106));
        pnlBotonMatenimientos1.setRoundBottomLeft(20);
        pnlBotonMatenimientos1.setRoundBottomRight(20);
        pnlBotonMatenimientos1.setRoundTopLeft(20);
        pnlBotonMatenimientos1.setRoundTopRight(20);
        pnlBotonMatenimientos1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlBotonMatenimientos1MouseClicked(evt);
            }
        });
        pnlBotonMatenimientos1.setLayout(new java.awt.BorderLayout());

        btnMantenimientosMenu.setFont(new java.awt.Font("Noto Sans", 1, 14)); // NOI18N
        btnMantenimientosMenu.setForeground(new java.awt.Color(236, 239, 244));
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
        pnlBotonMatenimientos1.add(btnMantenimientosMenu, java.awt.BorderLayout.CENTER);

        pnlContenedorBotonespnlIzq.add(pnlBotonMatenimientos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 180, 50));

        pnlBarraLateralIzq.add(pnlContenedorBotonespnlIzq, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
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

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Buscar:");
        pnlOpcionesConsultaDep1.add(jLabel2);

        pnlBusquedaDepartamentos1.setBackground(new java.awt.Color(76, 86, 106));
        pnlBusquedaDepartamentos1.setPreferredSize(new java.awt.Dimension(500, 40));
        pnlBusquedaDepartamentos1.setRoundBottomLeft(40);
        pnlBusquedaDepartamentos1.setRoundBottomRight(40);
        pnlBusquedaDepartamentos1.setRoundTopLeft(40);
        pnlBusquedaDepartamentos1.setRoundTopRight(40);
        pnlBusquedaDepartamentos1.setLayout(new java.awt.BorderLayout());

        txtBusquedaPuestos.setBackground(new java.awt.Color(236, 239, 244));
        txtBusquedaPuestos.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        txtBusquedaPuestos.setForeground(new java.awt.Color(236, 239, 244));
        txtBusquedaPuestos.setBorder(null);
        txtBusquedaPuestos.setMargin(new java.awt.Insets(10, 10, 10, 10));
        txtBusquedaPuestos.setName(""); // NOI18N
        txtBusquedaPuestos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBusquedaPuestosActionPerformed(evt);
            }
        });
        txtBusquedaPuestos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusquedaPuestosKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBusquedaPuestosKeyTyped(evt);
            }
        });
        pnlBusquedaDepartamentos1.add(txtBusquedaPuestos, java.awt.BorderLayout.CENTER);
        pnlBusquedaDepartamentos1.add(filler6, java.awt.BorderLayout.LINE_END);
        pnlBusquedaDepartamentos1.add(filler7, java.awt.BorderLayout.LINE_START);

        pnlOpcionesConsultaDep1.add(pnlBusquedaDepartamentos1);
        pnlOpcionesConsultaDep1.add(filler8);

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Buscar por:");
        pnlOpcionesConsultaDep1.add(jLabel1);

        cbbxFiltroPuestos.setPreferredSize(new java.awt.Dimension(200, 40));
        cbbxFiltroPuestos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbxFiltroPuestosActionPerformed(evt);
            }
        });
        pnlOpcionesConsultaDep1.add(cbbxFiltroPuestos);

        pnlBarraopcionesPuestos.add(pnlOpcionesConsultaDep1, java.awt.BorderLayout.CENTER);

        pnlConsultaPuestos.add(pnlBarraopcionesPuestos, java.awt.BorderLayout.PAGE_START);

        pnlTablaPuestos.setLayout(new java.awt.BorderLayout());

        tblPuestos.setBackground(new java.awt.Color(204, 153, 0));
        tblPuestos.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        tblPuestos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Descripción del Puesto"
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

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Buscar:");
        pnlOpcionesConsultaDep.add(jLabel4);

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

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Buscar por:");
        pnlOpcionesConsultaDep.add(jLabel3);

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
                "ID", "Descripción del Departamento"
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

        pnlConsultaEmpleados.setLayout(new java.awt.BorderLayout());

        pnlBarraopcionesEmpleados.setBackground(new java.awt.Color(255, 153, 102));
        pnlBarraopcionesEmpleados.setLayout(new java.awt.BorderLayout());

        pnlOpcionesConsultaEmpleados.setBackground(new java.awt.Color(59, 66, 82));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Buscar:");
        pnlOpcionesConsultaEmpleados.add(jLabel6);

        pnlBusquedaDepartamentos2.setBackground(new java.awt.Color(76, 86, 106));
        pnlBusquedaDepartamentos2.setPreferredSize(new java.awt.Dimension(500, 40));
        pnlBusquedaDepartamentos2.setRoundBottomLeft(40);
        pnlBusquedaDepartamentos2.setRoundBottomRight(40);
        pnlBusquedaDepartamentos2.setRoundTopLeft(40);
        pnlBusquedaDepartamentos2.setRoundTopRight(40);
        pnlBusquedaDepartamentos2.setLayout(new java.awt.BorderLayout());

        txtBusquedaEmpleados.setBackground(new java.awt.Color(236, 239, 244));
        txtBusquedaEmpleados.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        txtBusquedaEmpleados.setForeground(new java.awt.Color(236, 239, 244));
        txtBusquedaEmpleados.setBorder(null);
        txtBusquedaEmpleados.setMargin(new java.awt.Insets(10, 10, 10, 10));
        txtBusquedaEmpleados.setName(""); // NOI18N
        txtBusquedaEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBusquedaEmpleadosActionPerformed(evt);
            }
        });
        txtBusquedaEmpleados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusquedaEmpleadosKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBusquedaEmpleadosKeyTyped(evt);
            }
        });
        pnlBusquedaDepartamentos2.add(txtBusquedaEmpleados, java.awt.BorderLayout.CENTER);
        pnlBusquedaDepartamentos2.add(filler9, java.awt.BorderLayout.LINE_END);
        pnlBusquedaDepartamentos2.add(filler10, java.awt.BorderLayout.LINE_START);

        pnlOpcionesConsultaEmpleados.add(pnlBusquedaDepartamentos2);
        pnlOpcionesConsultaEmpleados.add(filler11);

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Buscar por:");
        pnlOpcionesConsultaEmpleados.add(jLabel5);

        cbbxFiltroEmpleados.setPreferredSize(new java.awt.Dimension(200, 40));
        cbbxFiltroEmpleados.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbxFiltroEmpleadosItemStateChanged(evt);
            }
        });
        cbbxFiltroEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbxFiltroEmpleadosActionPerformed(evt);
            }
        });
        pnlOpcionesConsultaEmpleados.add(cbbxFiltroEmpleados);
        pnlOpcionesConsultaEmpleados.add(filler12);

        jdcFecha.setFont(new java.awt.Font("Noto Sans", 0, 12)); // NOI18N
        pnlOpcionesConsultaEmpleados.add(jdcFecha);

        pnlBarraopcionesEmpleados.add(pnlOpcionesConsultaEmpleados, java.awt.BorderLayout.CENTER);

        pnlConsultaEmpleados.add(pnlBarraopcionesEmpleados, java.awt.BorderLayout.PAGE_START);

        pnlTablaEmpleados.setLayout(new java.awt.BorderLayout());

        tblEmpleados.setBackground(new java.awt.Color(204, 153, 0));
        tblEmpleados.setFont(new java.awt.Font("Noto Sans", 0, 14)); // NOI18N
        tblEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Apellido Paterno", "Apellido Materno", "Dirección", "Teléfono", "Sexo", "ID Departamento", "Fecha de Ingreso", "ID Puesto", "Cooperativa", "Salario"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmpleados.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tblEmpleados);
        if (tblEmpleados.getColumnModel().getColumnCount() > 0) {
            tblEmpleados.getColumnModel().getColumn(0).setResizable(false);
            tblEmpleados.getColumnModel().getColumn(1).setResizable(false);
            tblEmpleados.getColumnModel().getColumn(2).setResizable(false);
            tblEmpleados.getColumnModel().getColumn(3).setResizable(false);
            tblEmpleados.getColumnModel().getColumn(4).setResizable(false);
            tblEmpleados.getColumnModel().getColumn(5).setResizable(false);
            tblEmpleados.getColumnModel().getColumn(6).setResizable(false);
            tblEmpleados.getColumnModel().getColumn(7).setResizable(false);
            tblEmpleados.getColumnModel().getColumn(8).setResizable(false);
            tblEmpleados.getColumnModel().getColumn(9).setResizable(false);
            tblEmpleados.getColumnModel().getColumn(10).setResizable(false);
            tblEmpleados.getColumnModel().getColumn(11).setResizable(false);
        }

        pnlTablaEmpleados.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        pnlConsultaEmpleados.add(pnlTablaEmpleados, java.awt.BorderLayout.CENTER);

        pnlContenido.add(pnlConsultaEmpleados, "EMPLEADOS");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(pnlContenido, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pnlBotonMatenimientosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlBotonMatenimientosMouseClicked

    }//GEN-LAST:event_pnlBotonMatenimientosMouseClicked

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        cerrarTodasLasVentanas();
        
        this.dispose();
        
        Login login = new Login();
            login.setVisible(true);
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    private void btnCerrarSesionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarSesionMouseEntered
        btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_btnCerrarSesionMouseEntered

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
        popupMenuConsultas.show(pnlBotonConsultas, 200, pnlBotonConsultas.getHeight() - 50);
    }//GEN-LAST:event_btnConsultasMenuActionPerformed

    private void txtBusquedaPuestosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusquedaPuestosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusquedaPuestosActionPerformed

    private void txtBusquedaPuestosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaPuestosKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusquedaPuestosKeyPressed

    private void txtBusquedaPuestosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaPuestosKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusquedaPuestosKeyTyped

    private void cbbxFiltroPuestosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbxFiltroPuestosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbxFiltroPuestosActionPerformed

    private void txtBusquedaEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusquedaEmpleadosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusquedaEmpleadosActionPerformed

    private void txtBusquedaEmpleadosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaEmpleadosKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusquedaEmpleadosKeyPressed

    private void txtBusquedaEmpleadosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusquedaEmpleadosKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusquedaEmpleadosKeyTyped

    private void cbbxFiltroEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbxFiltroEmpleadosActionPerformed

    }//GEN-LAST:event_cbbxFiltroEmpleadosActionPerformed

    private void btnProcesosMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcesosMenuActionPerformed
        if (popupMenuProcesos == null) {
            popUpMenuProcesos();
        }
        popupMenuProcesos.show(btnProcesosMenu, 200, btnProcesosMenu.getHeight() - 50);
    }//GEN-LAST:event_btnProcesosMenuActionPerformed

    private void txtEstadoInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEstadoInicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEstadoInicioActionPerformed

    private void txtEstadoInicioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEstadoInicioKeyPressed

    }//GEN-LAST:event_txtEstadoInicioKeyPressed

    private void txtEstadoInicioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEstadoInicioKeyTyped

    }//GEN-LAST:event_txtEstadoInicioKeyTyped

    private void pnlBotonInicioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pnlBotonInicioKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_pnlBotonInicioKeyPressed

    private void pnlBotonInicioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlBotonInicioMouseClicked
        CardLayout cl = (CardLayout) pnlContenido.getLayout();
        cl.show(pnlContenido, "VACIO");
        txtEstadoInicio.setText("Menú Principal");
    }//GEN-LAST:event_pnlBotonInicioMouseClicked

    private void txtBotoninicioIconoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBotoninicioIconoMouseClicked
        CardLayout cl = (CardLayout) pnlContenido.getLayout();
        cl.show(pnlContenido, "VACIO");
        txtEstadoInicio.setText("Menú Principal");

    }//GEN-LAST:event_txtBotoninicioIconoMouseClicked

    private void pnlBotonSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlBotonSalirMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_pnlBotonSalirMouseClicked

    private void pnlBotonSalirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pnlBotonSalirKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_pnlBotonSalirKeyPressed

    private void txtBotoninicioIcono1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBotoninicioIcono1MouseClicked
        System.exit(0);
    }//GEN-LAST:event_txtBotoninicioIcono1MouseClicked

    private void btnMantenimientosMenuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMantenimientosMenuMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMantenimientosMenuMouseEntered

    private void btnMantenimientosMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMantenimientosMenuActionPerformed
        popupMenuMantenimientos.show(btnMantenimientosMenu, 200, btnMantenimientosMenu.getHeight()- 50);
    }//GEN-LAST:event_btnMantenimientosMenuActionPerformed

    private void pnlBotonMatenimientos1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlBotonMatenimientos1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_pnlBotonMatenimientos1MouseClicked

    private void txtBotoninicioIcono1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBotoninicioIcono1MouseEntered
        pnlBotonSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_txtBotoninicioIcono1MouseEntered

    private void pnlBotonInicioMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlBotonInicioMouseEntered
        pnlBotonSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_pnlBotonInicioMouseEntered

    private void cbbxFiltroEmpleadosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbxFiltroEmpleadosItemStateChanged
        String filtroSeleccionado = (String) cbbxFiltroEmpleados.getSelectedItem();
        
        if(filtroSeleccionado.equals("Fecha de Ingreso")){
            jdcFecha.setEnabled(true);
            txtBusquedaEmpleados.setEditable(false);
            txtBusquedaEmpleados.setFocusable(false);
        } else {
            jdcFecha.setEnabled(false);
            txtBusquedaEmpleados.setText("");
            txtBusquedaEmpleados.setEditable(true);
            txtBusquedaEmpleados.setFocusable(true);
            txtBusquedaEmpleados.requestFocus();

        }
    }//GEN-LAST:event_cbbxFiltroEmpleadosItemStateChanged


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
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnConsultasMenu;
    private javax.swing.JButton btnMantenimientosMenu;
    private javax.swing.JButton btnProcesosMenu;
    private javax.swing.JComboBox<String> cbbxFiltroBusqueda;
    private javax.swing.JComboBox<String> cbbxFiltroEmpleados;
    private javax.swing.JComboBox<String> cbbxFiltroPuestos;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler10;
    private javax.swing.Box.Filler filler11;
    private javax.swing.Box.Filler filler12;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.Box.Filler filler7;
    private javax.swing.Box.Filler filler8;
    private javax.swing.Box.Filler filler9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private com.toedter.calendar.JDateChooser jdcFecha;
    private javax.swing.JPanel pnlBarraLateralIzq;
    private javax.swing.JPanel pnlBarraSuperior;
    private javax.swing.JPanel pnlBarraopcionesDepartamentos;
    private javax.swing.JPanel pnlBarraopcionesEmpleados;
    private javax.swing.JPanel pnlBarraopcionesPuestos;
    private Utilidades.PanelesBordesRedondeados pnlBotonConsultas;
    private Utilidades.PanelesBordesRedondeados pnlBotonInicio;
    private Utilidades.PanelesBordesRedondeados pnlBotonMatenimientos;
    private Utilidades.PanelesBordesRedondeados pnlBotonMatenimientos1;
    private Utilidades.PanelesBordesRedondeados pnlBotonSalir;
    private Utilidades.PanelesBordesRedondeados pnlBtonProcesos;
    private Utilidades.PanelesBordesRedondeados pnlBusquedaDepartamentos;
    private Utilidades.PanelesBordesRedondeados pnlBusquedaDepartamentos1;
    private Utilidades.PanelesBordesRedondeados pnlBusquedaDepartamentos2;
    private javax.swing.JPanel pnlConsultaDepartamentos;
    private javax.swing.JPanel pnlConsultaEmpleados;
    private javax.swing.JPanel pnlConsultaPuestos;
    private javax.swing.JPanel pnlContenedorBotonespnlIzq;
    private javax.swing.JPanel pnlContenido;
    private javax.swing.JPanel pnlOpcionesConsultaDep;
    private javax.swing.JPanel pnlOpcionesConsultaDep1;
    private javax.swing.JPanel pnlOpcionesConsultaEmpleados;
    private javax.swing.JPanel pnlTablaDepartamentos;
    private javax.swing.JPanel pnlTablaEmpleados;
    private javax.swing.JPanel pnlTablaPuestos;
    private javax.swing.JPanel pnlVacio;
    private javax.swing.JPopupMenu popupMenuMantenimientos;
    private javax.swing.JTable tblDepartamentos;
    private javax.swing.JTable tblEmpleados;
    private javax.swing.JTable tblPuestos;
    private javax.swing.JLabel txtBotoninicioIcono;
    private javax.swing.JLabel txtBotoninicioIcono1;
    private javax.swing.JTextField txtBusquedaDepartamentos;
    private javax.swing.JTextField txtBusquedaEmpleados;
    private javax.swing.JTextField txtBusquedaPuestos;
    private javax.swing.JTextField txtEstadoInicio;
    // End of variables declaration//GEN-END:variables
}
