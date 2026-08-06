package com.mycompany.GUI.components;

import com.mycompany.proyectofinal.util.LogReader;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Visor de logs/app.log — mismo patrón que HistorialUsuarioDialog (tabla de solo lectura). */
public class AppLogDialog extends JDialog {

    private static final Logger logger = LogManager.getLogger(AppLogDialog.class);
    private static final String RUTA_LOG = "logs/app.log";
    // Ventana de líneas CRUDAS a escanear, no de entradas a mostrar: la mayoría son INFO/DEBUG
    // y se descartan al filtrar (ver LogReader), así que hay que mirar más atrás en el archivo
    // para encontrar suficientes ERROR/WARN reales sin llegar a cargarlo entero.
    private static final int MAX_LINEAS_ESCANEADAS = 3000;

    private DefaultTableModel model;
    private List<LogReader.LogEntry> entradasActuales = List.of();

    public AppLogDialog(Window parent) {
        super(parent, "App Log", ModalityType.APPLICATION_MODAL);
        setSize(950, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        String[] columnas = {"Fecha/Hora", "Nivel", "Mensaje"};
        model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable tabla = new JTable(model);
        tabla.setRowHeight(24);
        tabla.getTableHeader().setReorderingAllowed(false);

        tabla.getColumnModel().getColumn(0).setPreferredWidth(150); // Fecha/Hora
        tabla.getColumnModel().getColumn(1).setPreferredWidth(70);  // Nivel
        tabla.getColumnModel().getColumn(2).setPreferredWidth(680); // Mensaje

        // Mensajes largos (con stacktrace) se ven truncados por el ancho de columna — el
        // tooltip muestra el texto completo, envuelto en HTML para que los saltos de línea
        // del stacktrace se respeten (un tooltip plano ignora "\n").
        tabla.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setToolTipText(value != null ? aHtml(value.toString()) : null);
                return c;
            }
        });

        // Doble click en una fila: muestra el mensaje/stacktrace completo en un diálogo con
        // texto seleccionable/copiable — un tooltip no alcanza para leer o copiar un stacktrace largo.
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) return;
                int row = tabla.rowAtPoint(e.getPoint());
                if (row < 0 || row >= entradasActuales.size()) return;
                mostrarDetalle(entradasActuales.get(row));
            }
        });

        cargarLog();

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarLog());

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        JPanel south = new JPanel();
        south.add(btnActualizar);
        south.add(btnCerrar);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }

    private void cargarLog() {
        model.setRowCount(0);
        try {
            entradasActuales = LogReader.leerUltimasLineas(RUTA_LOG, MAX_LINEAS_ESCANEADAS);
            if (entradasActuales.isEmpty()) {
                model.addRow(new Object[]{"-", "-", "Sin errores ni advertencias recientes."});
            } else {
                for (LogReader.LogEntry entry : entradasActuales) {
                    model.addRow(new Object[]{entry.fechaHora, entry.nivel, entry.mensaje});
                }
            }
        } catch (IOException e) {
            logger.error("Error leyendo el archivo de log", e);
            entradasActuales = List.of();
            model.addRow(new Object[]{"-", "-", "No se pudo leer el archivo de log: " + e.getMessage()});
        }
    }

    private void mostrarDetalle(LogReader.LogEntry entry) {
        JTextArea texto = new JTextArea(
            entry.fechaHora + "  [" + entry.nivel + "]\n\n" + entry.mensaje
        );
        texto.setEditable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(texto);
        scroll.setPreferredSize(new Dimension(700, 400));
        JOptionPane.showMessageDialog(this, scroll, "Detalle del log", JOptionPane.PLAIN_MESSAGE);
    }

    /** Envuelve texto en HTML para tooltip, escapando caracteres especiales y respetando saltos de línea. */
    private static String aHtml(String texto) {
        String escapado = texto
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\n", "<br>");
        return "<html>" + escapado + "</html>";
    }
}
