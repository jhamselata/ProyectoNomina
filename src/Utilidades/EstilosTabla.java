package Utilidades;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class EstilosTabla {

    public static final Color COLOR_FONDO = new Color(216, 222, 233);
    public static final Color COLOR_SELECCION = new Color(129, 161, 193);
    public static final Color COLOR_HEADER = new Color(76, 86, 106);
    public static final Color COLOR_TEXTO = Color.BLACK;
    public static final Color COLOR_TEXTO_HEADER = Color.WHITE;
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;

    public static void aplicarEstiloPrincipal(JTable tabla, JScrollPane scrollPane) {
        aplicarEstiloPrincipal(tabla, scrollPane, true);
    }

    public static void aplicarEstiloPrincipal(JTable tabla, JScrollPane scrollPane, boolean ajustarColumnas) {

    tabla.setShowHorizontalLines(true);

    tabla.setShowVerticalLines(true);
    
    tabla.setShowGrid(true);

    tabla.setGridColor(new Color (145, 149, 156));

        
        
        if (tabla == null || scrollPane == null) {
            System.err.println("Advertencia: Tabla o ScrollPane es null");
            return;
        }

        try {
            tabla.setForeground(Color.BLACK);
            tabla.setBackground(new Color(216, 222, 233));
            tabla.setGridColor(Color.LIGHT_GRAY);
            tabla.setSelectionBackground(new Color(129, 161, 193));

            JTableHeader header = tabla.getTableHeader();
            header.setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel lbl = new JLabel(value.toString());
                    lbl.setOpaque(true);
                    lbl.setBackground(new Color(76, 86, 106));
                    lbl.setForeground(Color.WHITE);
                    lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    lbl.setHorizontalAlignment(SwingConstants.CENTER);
                    return lbl;
                }
            });

            tabla.setOpaque(false);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setBackground(new Color(216, 222, 233));

            tabla.setBorder(null);
            scrollPane.setBorder(null);

            if (ajustarColumnas) {
                ajustarColumnasProporcionalmente(tabla, scrollPane);
            }

        } catch (Exception e) {
            System.err.println("Error al aplicar estilo a la tabla: " + e.getMessage());
        }
    }

    public static void ajustarColumnasProporcionalmente(JTable tabla, JScrollPane scrollPane) {
        if (tabla == null || scrollPane == null) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                int anchoDisponible = scrollPane.getViewport().getWidth();
                if (anchoDisponible <= 0) {
                    anchoDisponible = scrollPane.getWidth() - 20;
                }

                if (anchoDisponible <= 0) {
                    return;
                }

                TableColumnModel columnModel = tabla.getColumnModel();
                int numColumnas = tabla.getColumnCount();

                if (numColumnas == 0) {
                    return;
                }

                int[] anchosPreferidos = new int[numColumnas];
                int totalAnchoPreferido = 0;

                for (int i = 0; i < numColumnas; i++) {
                    int anchoHeader = calcularAnchoHeader(tabla, i);
                    anchosPreferidos[i] = Math.max(anchoHeader, 60);
                    totalAnchoPreferido += anchosPreferidos[i];
                }

                if (totalAnchoPreferido <= anchoDisponible) {
                    int espacioExtra = anchoDisponible - totalAnchoPreferido;
                    int extraPorColumna = espacioExtra / numColumnas;
                    int resto = espacioExtra % numColumnas;

                    for (int i = 0; i < numColumnas; i++) {
                        TableColumn column = columnModel.getColumn(i);
                        int anchoFinal = anchosPreferidos[i] + extraPorColumna + (i < resto ? 1 : 0);
                        column.setPreferredWidth(anchoFinal);
                        column.setWidth(anchoFinal);
                    }
                } else {
                    double factor = (double) anchoDisponible / totalAnchoPreferido;
                    int totalAsignado = 0;

                    for (int i = 0; i < numColumnas - 1; i++) {
                        TableColumn column = columnModel.getColumn(i);
                        int anchoFinal = (int) Math.round(anchosPreferidos[i] * factor);
                        anchoFinal = Math.max(anchoFinal, 40);
                        column.setPreferredWidth(anchoFinal);
                        column.setWidth(anchoFinal);
                        totalAsignado += anchoFinal;
                    }

                    TableColumn ultimaColumna = columnModel.getColumn(numColumnas - 1);
                    int anchoUltima = Math.max(anchoDisponible - totalAsignado, 40);
                    ultimaColumna.setPreferredWidth(anchoUltima);
                    ultimaColumna.setWidth(anchoUltima);
                }

                tabla.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
                tabla.revalidate();
                tabla.repaint();

            } catch (Exception e) {
                System.err.println("Error al ajustar columnas proporcionalmente: " + e.getMessage());
            }
        });
    }

    private static int calcularAnchoHeader(JTable tabla, int columna) {
        try {
            TableColumn tableColumn = tabla.getColumnModel().getColumn(columna);
            TableCellRenderer headerRenderer = tabla.getTableHeader().getDefaultRenderer();
            Component headerComponent = headerRenderer.getTableCellRendererComponent(
                tabla, tableColumn.getHeaderValue(), false, false, 0, columna);
            return headerComponent.getPreferredSize().width + 20;
        } catch (Exception e) {
            return 80;
        }
    }

    public static void ajustarColumnasIguales(JTable tabla, JScrollPane scrollPane) {
        if (tabla == null || scrollPane == null) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                int anchoDisponible = scrollPane.getViewport().getWidth();
                if (anchoDisponible <= 0) {
                    anchoDisponible = scrollPane.getWidth() - 20;
                }

                if (anchoDisponible <= 0) {
                    return;
                }

                TableColumnModel columnModel = tabla.getColumnModel();
                int numColumnas = tabla.getColumnCount();

                if (numColumnas == 0) {
                    return;
                }

                int anchoPorColumna = anchoDisponible / numColumnas;
                int resto = anchoDisponible % numColumnas;

                for (int i = 0; i < numColumnas; i++) {
                    TableColumn column = columnModel.getColumn(i);
                    int ancho = anchoPorColumna + (i < resto ? 1 : 0);
                    column.setPreferredWidth(ancho);
                    column.setWidth(ancho);
                }

                tabla.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
                tabla.revalidate();
                tabla.repaint();

            } catch (Exception e) {
                System.err.println("Error al ajustar columnas iguales: " + e.getMessage());
            }
        });
    }

    public static void ajustarColumnasConPesos(JTable tabla, JScrollPane scrollPane, double[] pesos) {
        if (tabla == null || scrollPane == null || pesos == null) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                int anchoDisponible = scrollPane.getViewport().getWidth();
                if (anchoDisponible <= 0) {
                    anchoDisponible = scrollPane.getWidth() - 20;
                }

                if (anchoDisponible <= 0) {
                    return;
                }

                TableColumnModel columnModel = tabla.getColumnModel();
                int numColumnas = Math.min(tabla.getColumnCount(), pesos.length);

                if (numColumnas == 0) {
                    return;
                }

                double sumaPesos = 0;
                for (int i = 0; i < numColumnas; i++) {
                    sumaPesos += pesos[i];
                }

                if (sumaPesos == 0) {
                    return;
                }

                for (int i = 0; i < numColumnas; i++) {
                    TableColumn column = columnModel.getColumn(i);
                    int ancho = (int) Math.round((anchoDisponible * pesos[i]) / sumaPesos);
                    ancho = Math.max(ancho, 30);
                    column.setPreferredWidth(ancho);
                    column.setWidth(ancho);
                }

                tabla.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
                tabla.revalidate();
                tabla.repaint();

            } catch (Exception e) {
                System.err.println("Error al ajustar columnas con pesos: " + e.getMessage());
            }
        });
    }

    public static void ajustarAnchoColumnas(JTable tabla) {
        ajustarAnchoColumnas(tabla, true, false, 20);
    }

    public static void ajustarAnchoColumnas(JTable tabla, boolean incluirHeader, boolean incluirContenido, int margenExtra) {
        if (tabla == null) {
            return;
        }

        try {
            TableColumnModel columnModel = tabla.getColumnModel();

            for (int column = 0; column < tabla.getColumnCount(); column++) {
                TableColumn tableColumn = columnModel.getColumn(column);
                int preferredWidth = 0;
                int maxWidth = 300;
                int minWidth = 50;

                if (incluirHeader) {
                    TableCellRenderer headerRenderer = tabla.getTableHeader().getDefaultRenderer();
                    Component headerComponent = headerRenderer.getTableCellRendererComponent(
                        tabla, tableColumn.getHeaderValue(), false, false, 0, column);
                    preferredWidth = Math.max(preferredWidth, headerComponent.getPreferredSize().width);
                }

                if (incluirContenido && tabla.getRowCount() > 0) {
                    int maxRows = Math.min(tabla.getRowCount(), 10);

                    for (int row = 0; row < maxRows; row++) {
                        TableCellRenderer cellRenderer = tabla.getCellRenderer(row, column);
                        Component cellComponent = cellRenderer.getTableCellRendererComponent(
                            tabla, tabla.getValueAt(row, column), false, false, row, column);
                        preferredWidth = Math.max(preferredWidth, cellComponent.getPreferredSize().width);
                    }
                }

                preferredWidth += margenExtra;

                preferredWidth = Math.max(minWidth, Math.min(maxWidth, preferredWidth));

                tableColumn.setPreferredWidth(preferredWidth);
            }

            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        } catch (Exception e) {
            System.err.println("Error al ajustar columnas: " + e.getMessage());
        }
    }

    private static void configurarHeader(JTable tabla) {
        JTableHeader header = tabla.getTableHeader();
        if (header != null) {
            header.setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel lbl = new JLabel(value.toString());
                    lbl.setOpaque(true);
                    lbl.setBackground(new Color(76, 86, 106));
                    lbl.setForeground(Color.WHITE);
                    lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    lbl.setHorizontalAlignment(SwingConstants.CENTER);
                    return lbl;
                }
            });
        }
    }

    public static void aplicarEstiloConColumnasIguales(JTable tabla, JScrollPane scrollPane) {
        aplicarEstiloPrincipal(tabla, scrollPane, false);
        ajustarColumnasIguales(tabla, scrollPane);
    }

    public static void aplicarEstiloConPesos(JTable tabla, JScrollPane scrollPane, double[] pesos) {
        aplicarEstiloPrincipal(tabla, scrollPane, false);
        ajustarColumnasConPesos(tabla, scrollPane, pesos);
    }

    public static void aplicarEstiloSinHeader(JTable tabla, JScrollPane scrollPane) {
        if (tabla == null || scrollPane == null) {
            return;
        }

        tabla.setForeground(COLOR_TEXTO);
        tabla.setBackground(COLOR_FONDO);
        tabla.setGridColor(COLOR_GRID);
        tabla.setSelectionBackground(COLOR_SELECCION);
        tabla.setOpaque(false);

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBackground(COLOR_FONDO);
        tabla.setBorder(null);
        scrollPane.setBorder(null);
    }

    public static void aplicarEstiloVerde(JTable tabla, JScrollPane scrollPane) {
        aplicarEstiloPersonalizado(tabla, scrollPane,
            new Color(220, 233, 216),
            new Color(161, 193, 129),
            new Color(86, 106, 76));
    }

    public static void aplicarEstiloPersonalizado(JTable tabla, JScrollPane scrollPane,
        Color colorFondo, Color colorSeleccion, Color colorHeader) {

        if (tabla == null || scrollPane == null) {
            return;
        }

        tabla.setForeground(COLOR_TEXTO);
        tabla.setBackground(colorFondo);
        tabla.setGridColor(COLOR_GRID);
        tabla.setSelectionBackground(colorSeleccion);
        tabla.setOpaque(false);

        JTableHeader header = tabla.getTableHeader();
        if (header != null) {
            header.setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                    JLabel lbl = new JLabel(value != null ? value.toString() : "");
                    lbl.setOpaque(true);
                    lbl.setBackground(colorHeader);
                    lbl.setForeground(COLOR_TEXTO_HEADER);
                    lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    lbl.setHorizontalAlignment(SwingConstants.CENTER);
                    lbl.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));

                    return lbl;
                }
            });
        }

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setBackground(colorFondo);
        tabla.setBorder(null);
        scrollPane.setBorder(null);
    }

    public static void aplicarEstiloSinAjusteColumnas(JTable tabla, JScrollPane scrollPane) {
        aplicarEstiloPrincipal(tabla, scrollPane, false);
    }
}
