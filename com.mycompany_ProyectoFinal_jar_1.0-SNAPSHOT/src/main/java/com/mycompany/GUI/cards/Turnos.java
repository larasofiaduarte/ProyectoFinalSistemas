/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.cards;
import com.mycompany.GUI.Styles;
import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.abm.*;
import com.mycompany.GUI.components.CustomTableModel;
import com.mycompany.GUI.components.FilteredComboBoxEditor;
import com.mycompany.proyectofinal.Cliente;
import com.mycompany.proyectofinal.Controladora;
import com.mycompany.proyectofinal.Servicio;
import com.mycompany.proyectofinal.util.ReportManager;
import com.mycompany.proyectofinal.Turno;
import java.util.List;
import java.util.function.Function;
import java.time.format.DateTimeFormatter;



public class Turnos extends MainPanelBase{
     
    private Ventana ventana;
    private Controladora control;
    
    public Turnos(Ventana ventana){
        super("Turnos");
        this.ventana = ventana;
        this.control = new Controladora(); 
        initUI();
    }
    
    private void initUI(){
        cargarTabla();

        btnAlta.addActionListener(e -> abrirAltaTurnos());
        btnElim.addActionListener(e -> eliminarTurno());
        btnEdit.addActionListener(e -> modificarTurno());
        titlePanel.addReportButtonListener(e -> generarReport());

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (!table.isEditing() && row >= 0 && table.isCellEditable(row, col)) {
                    int c1 = colIndex("Cliente");
                    int c2 = colIndex("Servicio");
                    if (col == c1 || col == c2) {
                        table.editCellAt(row, col, e);
                        Component comp = table.getEditorComponent();
                        if (comp != null) comp.requestFocusInWindow();
                    }
                }
            }
        });
    }
    
    
    
    private void cargarTabla(){
        List<Turno> turnos = control.traerTurnos();

        String[] columns = {
            "ID",
            "Fecha",
            "Cliente",
            "Servicio",
            "Estado",
            "Detalle",
        };

        List<Function<Turno, Object>> getters = List.of(
            c -> c.getId(),
            c -> c.getFecha() != null ? c.getFecha().format(Styles.DATE_TIME) : "",
            c -> c.getCliente(),
            c -> c.getServicio(),
            c -> c.getEstado(),
            c -> c.getDetalle()
        );

        setTableData(turnos, columns, getters);

        @SuppressWarnings("unchecked")
        CustomTableModel<Turno> turnoModel = (CustomTableModel<Turno>) table.getModel();
        turnoModel.setValueSetter(2, (t, v) -> t.setCliente((Cliente) v));
        turnoModel.setValueSetter(3, (t, v) -> t.setServicio((Servicio) v));
        turnoModel.setValueSetter(4, (t, v) -> t.setEstado(v.toString()));
        turnoModel.setValueSetter(5, (t, v) -> t.setDetalle(v.toString()));
        turnoModel.setOnPersist(t -> {
            control.modificarTurno(t, t.getServicio(), t.getFecha(),
                    t.getCliente(), t.getEstado(), t.getDetalle());
            showToast("Cambio guardado");
        });

        List<Cliente> clientes = control.traerClientes();
        List<Servicio> servicios = control.traerServicios();

        SwingUtilities.invokeLater(() -> {
            int colCliente = colIndex("Cliente");
            int colServicio = colIndex("Servicio");

            FilteredComboBoxEditor<Cliente> clienteEditor = new FilteredComboBoxEditor<>(
                clientes,
                c -> c.getNombre() + " " + c.getApellido(),
                Cliente::getId,
                control::traerClientes,
                () -> {
                    AltaClientes d = new AltaClientes(ventana, true, () -> {});
                    d.setLocationRelativeTo(this);
                    d.setVisible(true);
                }
            );
            table.getColumnModel().getColumn(colCliente).setCellEditor(clienteEditor);
            table.getColumnModel().getColumn(colCliente).setCellRenderer(clienteEditor.getRenderer());

            FilteredComboBoxEditor<Servicio> servicioEditor = new FilteredComboBoxEditor<>(
                servicios,
                Servicio::getNombre,
                Servicio::getId,
                control::traerServicios,
                () -> {
                    AltaServicios d = new AltaServicios(ventana, true, () -> {});
                    d.setLocationRelativeTo(this);
                    d.setVisible(true);
                }
            );
            table.getColumnModel().getColumn(colServicio).setCellEditor(servicioEditor);
            table.getColumnModel().getColumn(colServicio).setCellRenderer(servicioEditor.getRenderer());
        });
    }
    
    private void abrirAltaTurnos() {
        AltaTurnos dialog = new AltaTurnos(
            ventana, true,
            () -> {
                cargarTabla();
                ventana.recargarCaja();
            }
        );
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    
    
    private void eliminarTurno() {

        int filaSeleccionada = table.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Seleccione un turno para eliminar.",
                "Ninguna selección",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea eliminar este turno?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Number idNum = (Number) table.getValueAt(filaSeleccionada, 0);
        int id = idNum.intValue();

        control.borrarTurno(id);

        JOptionPane.showMessageDialog(
                this,
                "Turno borrado correctamente.",
                "Eliminación exitosa",
                JOptionPane.INFORMATION_MESSAGE
        );

        cargarTabla();
    }
    
    private void modificarTurno() {

        int fila = table.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para modificar.");
            return;
        }

        int id = ((Number) table.getValueAt(fila, 0)).intValue();

        Turno turno = control.findTurno(id);

        AltaTurnos dialog = new AltaTurnos(
            ventana, true, turno,
            () -> {
                cargarTabla();
                ventana.recargarCaja();
            }
        );
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    @Override
    public void applyTheme() {
        super.applyTheme();
    }
    
    private void generarReport() {
        String[] opciones = {"PDF", "DOCX"};
        String formato = (String) JOptionPane.showInputDialog(
            this,
            "Seleccionar formato de exportación:",
            "Exportar Reporte",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            "PDF"
        );
        if (formato != null) {
            ReportManager.generateReport(this, "turnos.jrxml", null, "ListaTurnos", formato);
        }
    }
}
