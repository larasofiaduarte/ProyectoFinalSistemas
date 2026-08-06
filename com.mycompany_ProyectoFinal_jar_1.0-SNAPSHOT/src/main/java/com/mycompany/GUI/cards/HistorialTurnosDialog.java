/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.cards;
import com.mycompany.GUI.Styles;
import com.mycompany.persistencia.TurnoJpaController;
import com.mycompany.proyectofinal.Turno;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
public class HistorialTurnosDialog extends JDialog {

    public HistorialTurnosDialog(Frame parent, int clienteId, String clienteNombre) {
        super(parent, "Historial de Turnos - " + clienteNombre, true);
        setSize(700, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        initUI(clienteId);
    }

    private void initUI(int clienteId) {
        // Columnas
        String[] columnas = {"ID", "Fecha", "Hora", "Servicio", "Empleado", "Estado", "Detalle"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable tabla = new JTable(model);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setReorderingAllowed(false);

        // Cargar datos
        TurnoJpaController turnoDAO = new TurnoJpaController();
        List<Turno> turnos = turnoDAO.findByCliente(clienteId);

        if (turnos.isEmpty()) {
            model.addRow(new Object[]{"-", "Sin turnos registrados", "", "", "", "", ""});
        } else {
            for (Turno t : turnos) {
                String fecha = t.getFecha() != null
                    ? t.getFecha().format(Styles.DATE) : "-";
                String hora = t.getFecha() != null
                    ? t.getFecha().format(Styles.TIME) : "-";
                String servicio = t.getServicio() != null
                    ? t.getServicio().getNombre() : "-";
                String empleado = t.getEmpleado() != null
                    ? t.getEmpleado().getNombre() + " " + t.getEmpleado().getApellido() : "-";

                model.addRow(new Object[]{
                    t.getId(),
                    fecha,
                    hora,
                    servicio,
                    empleado,
                    t.getEstado(),
                    t.getDetalle()
                });
            }
        }

        // Cerrar botón
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel south = new JPanel();
        south.add(btnCerrar);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }
}