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
import com.mycompany.GUI.components.DateCellEditor;
import com.mycompany.GUI.components.TimeCellEditor;
import com.mycompany.GUI.components.FilteredComboBoxEditor;
import com.mycompany.proyectofinal.Cliente;
import com.mycompany.proyectofinal.Controladora;
import com.mycompany.proyectofinal.Servicio;
import com.mycompany.proyectofinal.util.ReportManager;
import com.mycompany.proyectofinal.Turno;
import com.mycompany.proyectofinal.Usuario;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableCellRenderer;



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
        // btnEdit.addActionListener(e -> modificarTurno()); // disabled — editing is handled inline
        titlePanel.addReportButtonListener(e -> generarReport());

        enableUltimoModificadoSort(control, "TURNOS");

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (!table.isEditing() && row >= 0 && table.isCellEditable(row, col)) {
                    int c1 = colIndex("Cliente");
                    int c2 = colIndex("Servicio");
                    int c3 = colIndex("Empleado");
                    if (col == c1 || col == c2 || col == c3) {
                        table.editCellAt(row, col, e);
                        Component comp = table.getEditorComponent();
                        if (comp != null) comp.requestFocusInWindow();
                    }
                }
            }
        });
    }
    
    
    
    /** Aplica la fecha/hora combinada al turno y re-evalúa el estado, igual que antes de separar Fecha/Hora. */
    private void aplicarFecha(Turno t, LocalDateTime ldt) {
        t.setFecha(ldt);
        if (ldt.isBefore(LocalDateTime.now())) {
            t.setEstado("Finalizado");
        }
    }

    private void cargarTabla(){
        List<Turno> turnos = control.traerTurnos();

        String[] columns = {
            "ID",
            "Fecha",
            "Hora",
            "Cliente",
            "Servicio",
            "Empleado",
            "Estado",
            "Detalle",
        };

        List<Function<Turno, Object>> getters = List.of(
            c -> c.getId(),
            c -> c.getFecha() != null ? c.getFecha().format(Styles.DATE) : "",
            c -> c.getFecha() != null ? c.getFecha().format(Styles.TIME) : "",
            c -> c.getCliente(),
            c -> c.getServicio(),
            c -> c.getEmpleado(),
            c -> c.getEstado(),
            c -> c.getDetalle()
        );

        // Fecha y Hora editan por separado pero ambas escriben el mismo campo t.fecha (single datetime).
        setTableData(turnos, columns, getters,
            new boolean[]{false, true, true, true, true, true, true, true});

        @SuppressWarnings("unchecked")
        CustomTableModel<Turno> turnoModel = (CustomTableModel<Turno>) table.getModel();
        turnoModel.setValueSetter(1, (t, v) -> {
            LocalDate fecha = LocalDate.parse(v.toString(), Styles.DATE);
            LocalTime horaActual = t.getFecha() != null ? t.getFecha().toLocalTime() : LocalTime.MIDNIGHT;
            aplicarFecha(t, LocalDateTime.of(fecha, horaActual));
        });
        turnoModel.setValueSetter(2, (t, v) -> {
            LocalTime hora = LocalTime.parse(v.toString(), Styles.TIME);
            LocalDate fechaActual = t.getFecha() != null ? t.getFecha().toLocalDate() : LocalDate.now();
            aplicarFecha(t, LocalDateTime.of(fechaActual, hora));
        });
        turnoModel.setValueSetter(3, (t, v) -> t.setCliente((Cliente) v));
        turnoModel.setValueSetter(4, (t, v) -> t.setServicio((Servicio) v));
        turnoModel.setValueSetter(5, (t, v) -> t.setEmpleado((Usuario) v));
        turnoModel.setValueSetter(6, (t, v) -> t.setEstado(v.toString()));
        turnoModel.setValueSetter(7, (t, v) -> t.setDetalle(v.toString()));
        turnoModel.setEntityClass(Turno.class, Map.of(6, "estado", 7, "detalle"));
        turnoModel.setTableName("TURNOS");
        turnoModel.setOnPersist(t -> {
            // Compara el estado guardado en BD con el nuevo para detectar cambios de estado relevantes
            Turno dbTurno = control.findTurno(t.getId());
            boolean eraFinalizado = dbTurno != null && "Finalizado".equals(dbTurno.getEstado());
            boolean ahoraFinalizado = "Finalizado".equals(t.getEstado());

            // Si el turno pasa de Finalizado a otro estado y ya descontó stock, confirma antes de continuar
            if (eraFinalizado && !ahoraFinalizado && dbTurno != null && dbTurno.isStockDescontado()) {
                int confirmStock = JOptionPane.showConfirmDialog(
                    SwingUtilities.getWindowAncestor(Turnos.this),
                    "Este turno ya descontó stock. ¿Deseas restaurarlo?",
                    "Restaurar stock",
                    JOptionPane.YES_NO_OPTION);
                if (confirmStock != JOptionPane.YES_OPTION) {
                    cargarTabla(); // Cancela el cambio y recarga la tabla original
                    return;
                }
            }

            try {
                control.modificarTurno(t, t.getServicio(), t.getFecha(),
                        t.getCliente(), t.getEstado(), t.getDetalle());
            } catch (IllegalArgumentException | IllegalStateException ex) {
                JOptionPane.showMessageDialog(
                    SwingUtilities.getWindowAncestor(Turnos.this),
                    ex.getMessage(),
                    "No se puede guardar",
                    JOptionPane.WARNING_MESSAGE);
                cargarTabla(); // Revierte la edición en memoria al último valor persistido
                return;
            }

            if (!eraFinalizado && ahoraFinalizado) {
                // Pendiente → Finalizado: registra ingreso en caja y descuenta stock
                if (!control.existsCajaByTurnoId(t.getId())) {
                    control.registrarIngresoEnCaja(t);
                    ventana.recargarCaja();
                }
                control.descontarStockProductos(t);
                ventana.recargarInventario();
            } else if (eraFinalizado && !ahoraFinalizado) {
                // Finalizado → Pendiente: restaura stock y ofrece eliminar el ingreso de caja
                control.revertirStockProductos(t);
                ventana.recargarInventario();
                int confirm = JOptionPane.showConfirmDialog(
                    SwingUtilities.getWindowAncestor(Turnos.this),
                    "¿Desea eliminar el ingreso registrado en Caja para este turno?",
                    "Revertir finalización",
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    control.deleteCajaByTurnoId(t.getId());
                    ventana.recargarCaja();
                }
            }
            showToast("Cambio guardado");
        });

        List<Cliente> clientes = control.traerClientes();
        List<Servicio> servicios = control.traerServicios();
        List<Usuario> empleados = control.traerUsuarios();

        SwingUtilities.invokeLater(() -> {
            table.getColumnModel().getColumn(colIndex("Fecha")).setCellEditor(new DateCellEditor());

            table.getColumnModel().getColumn(colIndex("Hora")).setCellEditor(new TimeCellEditor());
            DefaultTableCellRenderer horaRenderer = new DefaultTableCellRenderer();
            horaRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            table.getColumnModel().getColumn(colIndex("Hora")).setCellRenderer(horaRenderer);

            int colCliente = colIndex("Cliente");
            int colServicio = colIndex("Servicio");
            int colEmpleado = colIndex("Empleado");

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
            ).withEmptyDisplay("No seleccionado", "El servicio original fue eliminado");
            table.getColumnModel().getColumn(colServicio).setCellEditor(servicioEditor);
            table.getColumnModel().getColumn(colServicio).setCellRenderer(servicioEditor.getRenderer());

            FilteredComboBoxEditor<Usuario> empleadoEditor = new FilteredComboBoxEditor<>(
                empleados,
                u -> u.getNombre() + " " + u.getApellido(),
                Usuario::getId,
                control::traerUsuarios,
                () -> {
                    AltaEmpleados d = new AltaEmpleados(ventana, true, () -> {});
                    d.setLocationRelativeTo(this);
                    d.setVisible(true);
                }
            );
            table.getColumnModel().getColumn(colEmpleado).setCellEditor(empleadoEditor);
            table.getColumnModel().getColumn(colEmpleado).setCellRenderer(empleadoEditor.getRenderer());

            int colEstado = colIndex("Estado");
            JComboBox<String> estadoCombo = new JComboBox<>();
            estadoCombo.addItem("Pendiente");
            estadoCombo.addItem("Finalizado");
            table.getColumnModel().getColumn(colEstado).setCellEditor(new DefaultCellEditor(estadoCombo));
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
    
    // private void modificarTurno() { // disabled — editing is handled inline
    //     int fila = table.getSelectedRow();
    //     if (fila == -1) {
    //         JOptionPane.showMessageDialog(this, "Seleccione un registro para modificar.");
    //         return;
    //     }
    //     int id = ((Number) table.getValueAt(fila, 0)).intValue();
    //     Turno turno = control.findTurno(id);
    //     AltaTurnos dialog = new AltaTurnos(
    //         ventana, true, turno,
    //         () -> { cargarTabla(); ventana.recargarCaja(); }
    //     );
    //     dialog.setLocationRelativeTo(this);
    //     dialog.setVisible(true);
    // }

    public void recargarClientes() {
        cargarTabla();
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
