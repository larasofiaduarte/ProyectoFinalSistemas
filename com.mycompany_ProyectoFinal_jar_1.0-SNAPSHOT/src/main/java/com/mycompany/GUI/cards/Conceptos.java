/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.cards;

import com.mycompany.proyectofinal.util.ReportManager;
import com.mycompany.proyectofinal.util.LocalDoubleVerifier;
import com.mycompany.GUI.Styles;
import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.abm.*;
import com.mycompany.proyectofinal.*;
import com.mycompany.proyectofinal.Controladora;
import java.time.LocalDateTime;
import com.mycompany.GUI.components.CustomTableModel;
import com.mycompany.GUI.components.DateTimeCellEditor;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.persistence.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Conceptos extends MainPanelBase {

    private Ventana ventana;
    private Controladora control;
    private Usuario currentUser;
    protected String currentRol;
    

    public Conceptos(Ventana ventana) {
        super("Caja");
        this.ventana = ventana;
        this.control = new Controladora(); 
        initUI();
    }

    private void initUI() {
        cargarTabla();

        
        //buttons
        btnAlta.addActionListener(e -> abrirAltaCaja());
        btnElim.addActionListener(e -> eliminarConcepto());
        btnEdit.addActionListener(e-> modificarConcepto());
        
        titlePanel.addReportButtonListener(e -> generarReport());
        
    }
    
    public  void cargarTabla(){
        // data from DB
        java.util.List<Caja> conceptos = control.traerConceptos();

        String[] columns = {
            "ID",
            "Tipo",
            "Monto",
            "Medio",
            "Fecha",
            "Detalle"  
        };

        java.util.List<Function<Caja, Object>> getters = java.util.List.of(
            c -> c.getId(),
            c -> c.getTipo(),
            c -> LocalDoubleVerifier.format(c.getMonto()),
            c -> c.getMedio(),
            c -> c.getFecha() != null
            ? c.getFecha().format(Styles.DATE_TIME)
            : "",
            c -> c.getDetalle()
        );

        setTableData(conceptos, columns, getters);

        @SuppressWarnings("unchecked")
        CustomTableModel<Caja> cajaModel = (CustomTableModel<Caja>) table.getModel();
        cajaModel.setValueSetter(1, (c, v) -> c.setTipo(v.toString()));
        cajaModel.setLocalDecimalColumns(2);
        cajaModel.setValueSetter(2, (c, v) -> c.setMonto(Double.parseDouble(v.toString())));
        cajaModel.setValueSetter(3, (c, v) -> c.setMedio(v.toString()));
        cajaModel.setValueSetter(4, (c, v) -> c.setFecha(LocalDateTime.parse(v.toString(), Styles.DATE_TIME)));
        cajaModel.setValueSetter(5, (c, v) -> c.setDetalle(v.toString()));
        cajaModel.setEntityClass(Caja.class, Map.of(1, "tipo", 3, "medio"));
        cajaModel.setOnPersist(c -> {
            control.modificarConcepto(c, c.getTipo(), c.getMonto(), c.getMedio(), c.getDetalle());
            showToast("Cambio guardado");
        });

        SwingUtilities.invokeLater(() -> {
            table.getColumnModel().getColumn(colIndex("Fecha")).setCellEditor(new DateTimeCellEditor());

            int colTipo = colIndex("Tipo");
            JComboBox<String> tipoCombo = new JComboBox<>();
            tipoCombo.addItem("Ingreso");
            tipoCombo.addItem("Gasto");
            table.getColumnModel().getColumn(colTipo).setCellEditor(new DefaultCellEditor(tipoCombo));

            int colMedio = colIndex("Medio");
            JComboBox<String> medioCombo = new JComboBox<>();
            medioCombo.addItem("MercadoPago");
            medioCombo.addItem("Efectivo");
            medioCombo.addItem("Débito");
            medioCombo.addItem("Crédito");
            medioCombo.addItem("Transferencia");
            table.getColumnModel().getColumn(colMedio).setCellEditor(new DefaultCellEditor(medioCombo));

            int colMonto = colIndex("Monto");
            JTextField montoField = new JTextField();
            montoField.addKeyListener(new LocalDoubleVerifier());
            table.getColumnModel().getColumn(colMonto).setCellEditor(new DefaultCellEditor(montoField));
        });
    }
    
    private void abrirAltaCaja(){
        AltaCaja dialog = new AltaCaja(ventana, true, this::cargarTabla);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void eliminarConcepto() {
         Usuario currentUser = Session.getCurrentUser(); // obtenerlo acá, no en constructor
    
        System.out.println("Rol del usuario: '" + currentUser.getRol() + "'");
        if (currentUser == null || !currentUser.getRol().equalsIgnoreCase("Administrador")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Solamente el administrador puede eliminar movimientos de caja.",
                    "Acceso denegado",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int filaSeleccionada = table.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Seleccione un registro para eliminar.",
                "Ninguna selección",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea eliminar este registro?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Number idNum = (Number) table.getValueAt(filaSeleccionada, 0);
        int id = idNum.intValue();

        control.borrarConcepto(id);

        JOptionPane.showMessageDialog(
                this,
                "Registro borrado correctamente.",
                "Eliminación exitosa",
                JOptionPane.INFORMATION_MESSAGE
        );

        cargarTabla();
    }
    
    private void modificarConcepto() {

        int fila = table.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para modificar.");
            return;
        }

        int id = ((Number) table.getValueAt(fila, 0)).intValue();

        Caja caja = control.findConcepto(id);

        AltaCaja dialog =
            new AltaCaja(ventana, true, caja, this::cargarTabla);

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
            ReportManager.generateReport(this, "caja.jrxml", null, "ListaConceptos", formato);
        }
    }
}  
 

