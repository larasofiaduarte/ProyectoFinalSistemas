/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI.cards;

import java.awt.*;
import javax.swing.*;
import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.Styles;
import com.mycompany.GUI.abm.AltaClientes;
import com.mycompany.GUI.abm.AltaEmpleados;
import com.mycompany.proyectofinal.*;
import java.util.function.Function;

public class Usuarios extends MainPanelBase {

    private Ventana ventana;
    private Controladora control;
    private Usuario currentUser;
    protected String currentRol;

    public Usuarios(Ventana ventana) {
        super("Empleados");
        this.ventana = ventana;
        this.control = new Controladora();
        initUI();
    }

    private void initUI() {
        // Add components into panels created by MainPanelBase
        
        cargarTabla();
        
        
        //buttons
        
        btnAlta.addActionListener(e -> abrirAltaUser());
        btnElim.addActionListener(e -> eliminarUser());
        btnEdit.addActionListener(e -> modificarUser());

    }
    
    private void cargarTabla(){
        
        // data from DB
        java.util.List<Usuario> usuarios = control.traerUsuarios();

        String[] columns = {
            "ID",
            "Nombre de Usuario",
            "Dni",
            "Nombre",
            "Apellido",
            "Teléfono",
            "Rol"
        };

        java.util.List<Function<Usuario, Object>> getters = java.util.List.of(
            c -> c.getId(),
            c -> c.getUsername(),
            c -> c.getDni(),
            c -> c.getNombre(),
            c -> c.getApellido(),
            c -> c.getTelefono(),
            c -> c.getRol()
        );



        setTableData(usuarios, columns, getters);
    }
    
    private void abrirAltaUser() {
        AltaEmpleados dialog =
        new AltaEmpleados(ventana, true, this::cargarTabla);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    
    private void eliminarUser() {
        Usuario currentUser = Session.getCurrentUser();
        if (currentUser == null || !currentUser.getRol().equals("ADMIN")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Solamente el administrador puede eliminar usuarios.",
                    "Acceso denegado",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        int filaSeleccionada = table.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Seleccione un usuario para eliminar.",
                "Ninguna selección",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea eliminar este usuario?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Number idNum = (Number) table.getValueAt(filaSeleccionada, 0);
        int id = idNum.intValue();

        control.borrarUsuario(id);

        JOptionPane.showMessageDialog(
                this,
                "Usuario borrado correctamente.",
                "Eliminación exitosa",
                JOptionPane.INFORMATION_MESSAGE
        );

        cargarTabla();
    }
    
    
    private void modificarUser() {

        int fila = table.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario.");
            return;
        }

        int id = ((Number) table.getValueAt(fila, 0)).intValue();

        Usuario user = control.findUsuario(id);

        AltaEmpleados dialog =
            new AltaEmpleados(ventana, true, user, this::cargarTabla);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    @Override
    public void applyTheme() {
        super.applyTheme();
    }
}