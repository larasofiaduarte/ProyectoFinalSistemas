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
import com.mycompany.proyectofinal.Caja;
import com.mycompany.proyectofinal.Controladora;
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
        // Add components into panels created by MainPanelBase
        cargarTabla();
        
        
        //buttons
        btnAlta.addActionListener(e -> abrirAltaTurnos());
        btnElim.addActionListener(e -> eliminarTurno());
        btnEdit.addActionListener(e -> modificarTurno());
        titlePanel.addReportButtonListener(e -> generarReport());
        
    }
    
    
    
    private void cargarTabla(){
        // data from DB
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
            c -> c.getFecha() != null
            ? c.getFecha().format(Styles.DATE_TIME)
            : "",
            c -> c.getCliente() != null
                    ? c.getCliente().getNombre() + " " +
                      c.getCliente().getApellido()
                    : "",
            c -> c.getServicio() != null
                    ? c.getServicio().getNombre()
                    : "",
            c -> c.getEstado(),
            c -> c.getDetalle()
        );




        setTableData(turnos, columns, getters);
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
