/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.GUI.cards;

import com.mycompany.proyectofinal.util.ReportManager;
import com.mycompany.GUI.Styles;
import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.abm.AltaClientes;
import com.mycompany.proyectofinal.*;
import java.util.List;
import java.util.function.Function;
import com.mycompany.proyectofinal.Cliente;
import javax.swing.table.TableCellRenderer;


public class Clientes extends MainPanelBase {

    private Ventana ventana;
    private Controladora control;

    public Clientes(Ventana ventana) {
        super("Clientes");
        this.ventana = ventana;
        this.control = new Controladora();
        initUI();
    }

    private void initUI() {
        // cargar datos tabla
        cargarTabla();

        // events botones
        btnAlta.addActionListener(e -> abrirAltaCliente());
        btnElim.addActionListener(e -> eliminarCliente());
        btnEdit.addActionListener(e -> modificarCliente());
        
        titlePanel.addReportButtonListener(e -> generarReport()); //btn report
        
    }

    private void cargarTabla() {
        List<Cliente> clientes = control.traerClientes();

        String[] columns = {
            "ID", "Nombre", "Apellido", "Teléfono", "Género", "Historial"
        };

        List<Function<Cliente, Object>> getters = List.of(
            c -> c.getId(),
            c -> c.getNombre(),
            c -> c.getApellido(),
            c -> c.getTelefono(),
            c -> c.getGenero(),
            c -> "Ver Turnos"
        );

        // última columna true para que el botón sea clickeable
        /*
        boolean[] editables = {false, false, false, false, false, true};

        setTableData(clientes, columns, getters, editables);*/
        
        setTableData(clientes, columns, getters, defaultEditables(columns.length));

        SwingUtilities.invokeLater(() -> {
            int colBoton = table.getColumnCount() - 1;
            table.getColumnModel().getColumn(colBoton).setCellRenderer(new ButtonRenderer());
            table.getColumnModel().getColumn(colBoton).setCellEditor(new ButtonEditor(table));
            table.setRowHeight(35);
        });
    }

    
    private void abrirAltaCliente() { //btn alta
        AltaClientes dialog =
        new AltaClientes(ventana, true, this::cargarTabla);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void eliminarCliente() { //btn elim

        int filaSeleccionada = table.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Seleccione un cliente para eliminar.",
                "Ninguna selección",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog( //confimacion
                this, 
                "¿Está seguro que desea eliminar este cliente?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Number idNum = (Number) table.getValueAt(filaSeleccionada, 0);
        int id = idNum.intValue();

        control.borrarCliente(id);

        JOptionPane.showMessageDialog(
                this,
                "Cliente borrado correctamente.",
                "Eliminación exitosa",
                JOptionPane.INFORMATION_MESSAGE
        );

        cargarTabla(); //re carga datos tabla
    }

    private void modificarCliente() { //btn modif

        int fila = table.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente.");
            return;
        }

        int id = ((Number) table.getValueAt(fila, 0)).intValue();

        Cliente cliente = control.findCliente(id);

        AltaClientes dialog =  //abre alta en modo modificacion
            new AltaClientes(ventana, true, cliente, this::cargarTabla);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    @Override
    public void applyTheme() { //dark mode
        super.applyTheme();
    }
    
    private void generarReport() { //report
        String[] opciones = {"PDF", "DOCX"};
        String formato = (String) JOptionPane.showInputDialog( //formatos
            this,
            "Seleccionar formato de exportación:",
            "Exportar Reporte",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            "PDF"
        );
        if (formato != null) {
            ReportManager.generateReport(this, "clientes.jrxml", null, "ListaClientes", formato);
        }
    }
    
    
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Ver Turnos");
            setFocusPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

        class ButtonEditor extends DefaultCellEditor {
            private JButton button;
            private JTable tabla;
            private int currentRow; // ← agregá esto

            public ButtonEditor(JTable tabla) {
                super(new JCheckBox());
                this.tabla = tabla;
                button = new JButton("Ver Turnos");
                button.setFocusPainted(false);
                

                button.addActionListener(e -> {
                    System.out.println("BOTON CLICKEADO, fila: " + currentRow); 
                    // usa currentRow en vez de tabla.getSelectedRow()
                    int clienteId = ((Number) tabla.getValueAt(currentRow, 0)).intValue();
                    String nombre = tabla.getValueAt(currentRow, 1) + " " + tabla.getValueAt(currentRow, 2);

                    Frame parent = (Frame) SwingUtilities.getWindowAncestor(tabla);
                    HistorialTurnosDialog dialog = new HistorialTurnosDialog(parent, clienteId, nombre);
                    dialog.setVisible(true);
                    fireEditingStopped();
                });
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                    boolean isSelected, int row, int column) {
                currentRow = row; // ← guardá la fila acá
                return button;
            }

            @Override
            public Object getCellEditorValue() { return "Ver Turnos"; }
        }
}
