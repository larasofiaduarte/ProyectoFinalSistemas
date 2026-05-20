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
import com.mycompany.proyectofinal.Cliente;
import com.mycompany.proyectofinal.Controladora;
import com.mycompany.proyectofinal.Proveedor;
import com.mycompany.proyectofinal.util.ReportManager;
import com.mycompany.proyectofinal.util.EmailVerifier;
import com.mycompany.proyectofinal.util.WebsiteVerifier;
import com.mycompany.GUI.components.CustomTableModel;
import java.util.List;
import java.util.function.Function;

public class Proveedores extends MainPanelBase {

    private Ventana ventana;
    private Controladora control;

    public Proveedores(Ventana ventana) {
        super("Proveedores");
        this.ventana = ventana;
        this.control = new Controladora(); 
        initUI();
    }

    private void initUI() {
        // Add components into panels created by MainPanelBase
        cargarTabla();
        
        
        //buttons
        btnAlta.addActionListener(e -> abrirAltaProveedor());
        btnElim.addActionListener(e -> eliminarProveedor());
        btnEdit.addActionListener(e -> editarProveedor());
        titlePanel.addReportButtonListener(e -> generarReport());
    }
    
    private void cargarTabla(){
        // data from DB
        List<Proveedor> proveedores = control.traerProveedores();

        String[] columns = {
            "ID",
            "Nombre",
            "Teléfono",
            "Email",
            "Web"
        };

        List<Function<Proveedor, Object>> getters = List.of(
            c -> c.getId(),
            c -> c.getNombre(),
            c -> c.getTelefono(),
            c -> c.getEmail(),
            c -> c.getWebsite()
        );



        setTableData(proveedores, columns, getters);

        @SuppressWarnings("unchecked")
        CustomTableModel<Proveedor> provModel = (CustomTableModel<Proveedor>) table.getModel();
        provModel.setValueSetter(1, (p, v) -> p.setNombre(v.toString()));
        provModel.setValueSetter(2, (p, v) -> p.setTelefono(v.toString()));
        provModel.setValueSetter(3, (p, v) -> p.setEmail(v.toString()));
        provModel.setValueSetter(4, (p, v) -> p.setWebsite(v.toString()));
        provModel.setOnPersist(p -> {
            control.modificarProveedor(p, p.getNombre(), p.getTelefono(), p.getEmail(), p.getWebsite());
            showToast("Cambio guardado");
        });

        SwingUtilities.invokeLater(() -> {
            JTextField emailField = new JTextField();
            DefaultCellEditor emailEditor = new DefaultCellEditor(emailField) {
                @Override
                public boolean stopCellEditing() {
                    String text = emailField.getText().trim();
                    if (!EmailVerifier.isValid(text)) {
                        JOptionPane.showMessageDialog(table,
                                "Ingrese una dirección de correo válida (ej: usuario@dominio.com).",
                                "Email inválido", JOptionPane.WARNING_MESSAGE);
                        cancelCellEditing();
                        return false;
                    }
                    return super.stopCellEditing();
                }
            };
            table.getColumnModel().getColumn(colIndex("Email")).setCellEditor(emailEditor);

            JTextField webField = new JTextField();
            DefaultCellEditor webEditor = new DefaultCellEditor(webField) {
                @Override
                public boolean stopCellEditing() {
                    String text = webField.getText().trim();
                    if (!WebsiteVerifier.isValid(text)) {
                        JOptionPane.showMessageDialog(table,
                                "Ingrese una URL válida (ej: www.sitio.com o https://sitio.com).",
                                "Sitio web inválido", JOptionPane.WARNING_MESSAGE);
                        cancelCellEditing();
                        return false;
                    }
                    return super.stopCellEditing();
                }
            };
            table.getColumnModel().getColumn(colIndex("Web")).setCellEditor(webEditor);
        });
    }
    
    private void abrirAltaProveedor() {
        AltaProveedores dialog =
        new AltaProveedores(ventana, true, this::cargarTabla);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void eliminarProveedor() {

        int filaSeleccionada = table.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Seleccione un proveedor para eliminar.",
                "Ninguna selección",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea eliminar este proveedor?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Number idNum = (Number) table.getValueAt(filaSeleccionada, 0);
        int id = idNum.intValue();

        control.borrarProveedor(id);

        JOptionPane.showMessageDialog(
                this,
                "Proveedor borrado correctamente.",
                "Eliminación exitosa",
                JOptionPane.INFORMATION_MESSAGE
        );

        cargarTabla();
    }
    
    private void editarProveedor() {

        int fila = table.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente.");
            return;
        }

        int id = ((Number) table.getValueAt(fila, 0)).intValue();

        Proveedor prov = control.findProveedor(id);

        AltaProveedores dialog =
            new AltaProveedores(ventana, true, prov, this::cargarTabla);

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
            ReportManager.generateReport(this, "prov.jrxml", null, "ListaProveedores", formato);
        }
    }
}
