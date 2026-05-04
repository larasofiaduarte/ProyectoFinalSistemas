/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.cards;

import com.mycompany.persistencia.ActividadUsuarioJpaController;
import com.mycompany.persistencia.TurnoJpaController;
import com.mycompany.proyectofinal.ActividadUsuario;
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

public class HistorialUsuarioDialog extends JDialog {

    public HistorialUsuarioDialog(Frame parent, int usuarioId, String userNombre) {
        super(parent, "Historial de Actividad - " + userNombre, true);
        setSize(900, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        initUI(usuarioId);
    }

    private void initUI(int usuarioId) {
        String[] columnas = {"ID", "Fecha", "Hora", "Tabla", "Fila", "Columna", "Anterior", "Nuevo", "Acción"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable tabla = new JTable(model);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setReorderingAllowed(false);

        // Ajustar anchos de columna
        tabla.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
        tabla.getColumnModel().getColumn(1).setPreferredWidth(90);   // Fecha
        tabla.getColumnModel().getColumn(2).setPreferredWidth(70);   // Hora
        tabla.getColumnModel().getColumn(3).setPreferredWidth(90);   // Tabla
        tabla.getColumnModel().getColumn(4).setPreferredWidth(70);   // Fila
        tabla.getColumnModel().getColumn(5).setPreferredWidth(90);   // Columna
        tabla.getColumnModel().getColumn(6).setPreferredWidth(100);  // Anterior
        tabla.getColumnModel().getColumn(7).setPreferredWidth(100);  // Nuevo
        tabla.getColumnModel().getColumn(8).setPreferredWidth(80);   // Acción

        ActividadUsuarioJpaController dao = new ActividadUsuarioJpaController();
        List<ActividadUsuario> actividades = dao.findByUsuario(usuarioId);

        if (actividades.isEmpty()) {
            model.addRow(new Object[]{"-", "Sin actividad registrada", "", "", "", "", "", "", ""});
        } else {
            for (ActividadUsuario a : actividades) {
                String fecha = a.getFechaHora() != null ? a.getFechaHora().toLocalDate().toString() : "-";
                String hora  = a.getFechaHora() != null ? a.getFechaHora().toLocalTime().toString() : "-";
                model.addRow(new Object[]{
                    a.getId(),
                    fecha,
                    hora,
                    a.getTablaAfectada(),
                    a.getFilaAfectada(),
                    a.getColumnaAfectada(),
                    a.getValorAnterior(),
                    a.getValorNuevo(),
                    a.getTipoAccion()
                });
            }
        }

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel south = new JPanel();
        south.add(btnCerrar);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
    }
}