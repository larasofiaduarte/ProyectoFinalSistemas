/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.GUI.abm;

import com.mycompany.GUI.*;
import com.mycompany.GUI.components.Btn;
import com.mycompany.proyectofinal.util.RegistrarActividad;
import com.mycompany.proyectofinal.Caja;
import com.mycompany.proyectofinal.Cliente;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import com.mycompany.proyectofinal.Controladora;
import com.mycompany.proyectofinal.Servicio;
import com.mycompany.proyectofinal.Turno;
import com.mycompany.proyectofinal.Usuario;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;
import java.util.*;
import javax.swing.*;


public class AltaTurnos extends JDialog {

    Controladora control = new Controladora();
    Servicio servicioSeleccionado;
    Cliente clienteSeleccionado;
    private Runnable onSave;
    private Turno turnoEditar;
    private List<Usuario> listaUsuarios;
    
    //MODO ALTA
    public AltaTurnos(Frame parent, boolean modal, Runnable onSave) {
        super(parent, modal);
        this.onSave = onSave;
        initComponents();
        
        
        //panelBtns.setBorder(Styles.paddingBottom);
        
        Btn btnAlta = Btn.primary("Guardar");
        btnAlta.setPreferredSize(Styles.btnSizeSm);
        panelBtns.add(btnAlta);
        
        initUI();
        
        btnAlta.addActionListener(e -> guardarTurno());
        
    }
    //MODO MODIFICAR
    public AltaTurnos(Frame parent, boolean modal, Turno turno, Runnable onSave) {
        super(parent, modal);
        initComponents();
        this.turnoEditar = turno;
        this.onSave = onSave;
        
        Btn btnAlta = Btn.primary("Guardar");
        btnAlta.setPreferredSize(Styles.btnSizeSm);
        panelBtns.add(btnAlta);
        
        initUI();

        cargarDatosTurno(); // cargar datos en los campos
        btnAlta.addActionListener(e -> guardarTurno());
    }
    
    private void cargarDatosTurno(){
        cboServicio.setSelectedItem(turnoEditar.getServicio().getNombre());
        cboEstado.setSelectedItem(turnoEditar.getEstado());
        cboClientes.setSelectedItem(turnoEditar.getCliente().getNombre()+" "+turnoEditar.getCliente().getApellido());
        txtDetalle.setText(turnoEditar.getDetalle());

        // Preselecciona el empleado actual del turno en el combo
        Usuario empActual = turnoEditar.getEmpleado();
        if (empActual == null && turnoEditar.getServicio() != null) {
            empActual = turnoEditar.getServicio().getEmpleado(); // fallback para datos viejos
        }
        if (empActual != null) {
            cboEmpleado.setSelectedItem(empActual.getNombre() + " " + empActual.getApellido());
            cboEmpleado.setEnabled(true);
        }
        
        if (turnoEditar != null && turnoEditar.getFecha() != null) {

            LocalDateTime fechaHora = turnoEditar.getFecha();

            Date fecha = Date.from(
                    fechaHora.atZone(ZoneId.systemDefault()).toInstant()
            );

            calendar.setDate(fecha);

            String horaStr = fechaHora.toLocalTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm"));

            cboHora.setSelectedItem(horaStr);
        }
    
    }
    
  private void guardarTurno() {

    try {

        String nombreCliente = (String) cboClientes.getSelectedItem();
        String servicioStr = (String) cboServicio.getSelectedItem();
        servicioSeleccionado = guardarServicio(servicioStr);
        clienteSeleccionado = guardarCliente(nombreCliente);
        

        if (!validarCampos()) {
            return;
        }

        String hora = (String) cboHora.getSelectedItem();
        String estado = (String) cboEstado.getSelectedItem();
        String detalle = txtDetalle.getText();

        Date fecha = calendar.getDate();
        LocalDateTime fechaFinal = obtenerFecha(hora, fecha);

        boolean turnoYaExiste = control.turnoYaExiste(servicioSeleccionado, fechaFinal);

        if (turnoEditar == null && turnoYaExiste) {
            JOptionPane.showMessageDialog(this,
                    "Ya existe un turno para ese servicio en esa fecha y horario.",
                    "No se puede guardar",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Resuelve el empleado seleccionado en el combo, con fallback al empleado del servicio
        String empStr = (String) cboEmpleado.getSelectedItem();
        Usuario empleadoSeleccionado = encontrarEmpleado(empStr);
        if (empleadoSeleccionado == null && servicioSeleccionado != null) {
            empleadoSeleccionado = servicioSeleccionado.getEmpleado();
        }

        if (turnoEditar == null) {
            // 🔹 ALTA
            control.guardarTurno(servicioSeleccionado, fechaFinal, clienteSeleccionado, estado, detalle, empleadoSeleccionado);
            RegistrarActividad.registrar(
                "TURNOS",
                "nuevo registro",
                "alta",
                null,
                "Cliente: " + nombreCliente + " | Servicio: " + servicioStr + " | Fecha: " + fechaFinal + " | Estado: " + estado,
                "ALTA"
            );
            JOptionPane.showMessageDialog(this,
                    "Turno guardado correctamente.",
                    "Alta exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            // MODIFICAR - guardar estado ANTES de modificar
            boolean eraFinalizado = "Finalizado".equals(turnoEditar.getEstado());
            boolean ahoraFinalizado = "Finalizado".equals(estado);

            turnoEditar.setServicio(servicioSeleccionado);
            turnoEditar.setCliente(clienteSeleccionado);
            turnoEditar.setEmpleado(empleadoSeleccionado);
            control.modificarTurno(turnoEditar, servicioSeleccionado, fechaFinal, clienteSeleccionado, estado, detalle);

            if (!eraFinalizado && ahoraFinalizado) {
                if (!control.existsCajaByTurnoId(turnoEditar.getId())) {
                    control.registrarIngresoEnCaja(turnoEditar);
                }
                control.descontarStockProductos(turnoEditar);
            } else if (eraFinalizado && !ahoraFinalizado) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Desea eliminar el ingreso registrado en Caja para este turno?",
                    "Revertir finalización",
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    control.deleteCajaByTurnoId(turnoEditar.getId());
                }
            }

            JOptionPane.showMessageDialog(this,
                    "Turno modificado correctamente.",
                    "Modificación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
        }

                if (onSave != null) {
                    onSave.run();
                }

                dispose();

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this,
                "ID de cliente inválido.",
                "Error",
                JOptionPane.WARNING_MESSAGE);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this,
                "Ocurrió un error al guardar el turno.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblCargaTurnos = new javax.swing.JLabel();
        panelBtns = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cboServicio = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cboHora = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cboEstado = new javax.swing.JComboBox<>();
        calendar = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDetalle = new javax.swing.JTextArea();
        cboClientes = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        cboEmpleado = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));

        lblCargaTurnos.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblCargaTurnos.setText("Carga de Turnos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 567, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(210, 210, 210)
                    .addComponent(lblCargaTurnos)
                    .addContainerGap(219, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(37, 37, 37)
                    .addComponent(lblCargaTurnos)
                    .addContainerGap(38, Short.MAX_VALUE)))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        panelBtns.setBackground(new java.awt.Color(250, 250, 250));
        getContentPane().add(panelBtns, java.awt.BorderLayout.PAGE_END);

        jPanel3.setBackground(new java.awt.Color(250, 250, 250));

        jLabel1.setText("Cliente*");

        jLabel2.setText("Servicio*");

        jLabel3.setText("Fecha*");

        jLabel4.setText("Horario*");

        // Los horarios se cargan dinámicamente en actualizarHorariosDisponibles() según servicio/empleado/fecha

        jLabel5.setText("Estado*");

        cboEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pendiente", "Finalizado" }));

        jLabel6.setText("Detalle");

        txtDetalle.setColumns(20);
        txtDetalle.setRows(5);
        jScrollPane1.setViewportView(txtDetalle);

        jLabel7.setText("Empleado*");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cboEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jScrollPane1))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cboHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(calendar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cboServicio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(cboClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(196, 196, 196))))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(cboClientes))
                .addGap(38, 38, 38)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(cboServicio))
                .addGap(31, 31, 31)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(calendar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(34, 34, 34)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(cboHora))
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(cboEstado))
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(cboEmpleado))
                .addGap(31, 31, 31)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(70, 70, 70))
                    .addComponent(jScrollPane1))
                .addGap(115, 115, 115))
        );

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //UI
    private void initUI(){
        
        jLabel1.setForeground(Styles.fontDark);
        jPanel3.setBackground(Styles.bgLight);
        jPanel1.setBackground(Styles.bgLight);
        panelBtns.setBackground(Styles.bgLight);
        
        Btn btnLimpiar = Btn.secondary("Limpiar");
        btnLimpiar.setPreferredSize(Styles.btnSizeSm);
        panelBtns.add(btnLimpiar);
        
        Btn btnCerrar = Btn.secondary("Cerrar");
        btnCerrar.setPreferredSize(Styles.btnSizeSm);
        panelBtns.add(btnCerrar);
        
        obtenerServicios();
        obtenerClientes();
        obtenerEmpleados();

        // Cuando se elige un servicio, habilita cboEmpleado, preselecciona su empleado y recarga slots
        cboServicio.addActionListener(e -> {
            String servicioStr = (String) cboServicio.getSelectedItem();
            Servicio serv = guardarServicio(servicioStr);
            if (serv != null) {
                cboEmpleado.setEnabled(true);
                Usuario defEmpleado = serv.getEmpleado();
                if (defEmpleado != null) {
                    cboEmpleado.setSelectedItem(defEmpleado.getNombre() + " " + defEmpleado.getApellido());
                } else {
                    cboEmpleado.setSelectedIndex(-1);
                }
            } else {
                cboEmpleado.setEnabled(false);
                cboEmpleado.setSelectedIndex(-1);
            }
            actualizarHorariosDisponibles();
        });

        // Cuando cambia el empleado manualmente, recarga los slots disponibles
        cboEmpleado.addActionListener(e -> actualizarHorariosDisponibles());

        calendar.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    if (calendar.getDate() == null) return;
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.setTime(calendar.getDate());
                    if (selectedDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        JOptionPane.showMessageDialog(null, "No se pueden seleccionar los domingos. Por favor, elija otro día.", "Error", JOptionPane.ERROR_MESSAGE);
                        calendar.setDate(null);
                        return;
                    }
                    actualizarHorariosDisponibles();
                }
            }
        });
        
        
        btnLimpiar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cboClientes.setSelectedIndex(-1);
                    cboServicio.setSelectedIndex(-1);
                    cboHora.setSelectedIndex(-1);
                    calendar.setDate(new java.util.Date());
                    cboEstado.setSelectedIndex(-1);
                    txtDetalle.setText("");
                    cboEmpleado.setSelectedIndex(-1);
                    cboEmpleado.setEnabled(false);
                }
        });
        
        btnCerrar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
        });
        
    
    
    }
    
    public LocalDateTime obtenerFecha(String hora, Date fecha){
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        String[] timeParts = hora.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        Date combinedDate = calendar.getTime();
        
        return combinedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    
    }
    
    private boolean validarCampos() {
    if (cboClientes.getSelectedItem() == null  || 
        cboServicio.getSelectedItem() == null || 
        cboHora.getSelectedItem() == null || 
        cboEstado.getSelectedItem() == null || 
        calendar.getDate() == null ) {

        JOptionPane.showMessageDialog(this, 
            "Por favor, complete todos los campos obligatorios.", 
            "Campos vacíos", 
            JOptionPane.WARNING_MESSAGE);
        return false;
    }
    return true;
}

    // Recarga cboHora con slots disponibles según servicio, empleado y fecha actuales
    private void actualizarHorariosDisponibles() {
        Servicio serv = guardarServicio((String) cboServicio.getSelectedItem());
        Usuario emp   = encontrarEmpleado((String) cboEmpleado.getSelectedItem());
        Date fecha    = calendar.getDate();

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        if (serv != null && emp != null && fecha != null) {
            LocalDate localFecha = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int excludeId = (turnoEditar != null) ? turnoEditar.getId() : -1;
            List<LocalTime> slots = control.generarHorariosDisponibles(localFecha, serv, emp, excludeId);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
            for (LocalTime t : slots) model.addElement(t.format(fmt));
        }
        cboHora.setModel(model);
        cboHora.setSelectedIndex(-1);
    }

    //cargar servicios a cbo
    public void obtenerServicios(){
        List<Servicio> servicios = control.traerServicios();
        List<String> nombres = new ArrayList<>();
        for (Servicio servicio : servicios) {
            String nombreServicio = servicio.getNombre();
            cboServicio.addItem(nombreServicio);  // Assuming the toString method is implemented in Servicio
            nombres.add(nombreServicio);
        }
        Styles.addAutoComplete(cboServicio, nombres); //permitir busqueda
    }
    //cargar clientes a cbo
    public void obtenerClientes(){
        List<Cliente> clientes = control.traerClientes().stream()
                .filter(c -> c.isActivo())
                .collect(java.util.stream.Collectors.toList());
        List<String> nombres = new ArrayList<>();
        for (Cliente cliente : clientes) {
            String nombreCliente = (cliente.getNombre()+" " +cliente.getApellido());
            cboClientes.addItem(nombreCliente);
            nombres.add(nombreCliente);
        }
        Styles.addAutoComplete(cboClientes, nombres);
    }
    
    private void obtenerEmpleados() {
        listaUsuarios = control.traerUsuarios();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (Usuario u : listaUsuarios) {
            model.addElement(u.getNombre() + " " + u.getApellido());
        }
        cboEmpleado.setModel(model);
        cboEmpleado.setSelectedIndex(-1);
        cboEmpleado.setEnabled(false);
    }

    private Usuario encontrarEmpleado(String nombre) {
        if (nombre == null || listaUsuarios == null) return null;
        for (Usuario u : listaUsuarios) {
            if ((u.getNombre() + " " + u.getApellido()).equals(nombre)) return u;
        }
        return null;
    }

    //encontrar servicio por nombre y guardar
    public Servicio guardarServicio(String ser){
        List<Servicio> servicios = control.traerServicios();
        Servicio servicioSeleccionado = null;

        // Loop through the list to find the Servicio object with the matching name
        for (Servicio servicio : servicios) {
            if (servicio.getNombre().equals(ser)) {  // Compare the name of the service
                servicioSeleccionado = servicio;
                break;  // Stop the loop once the match is found
            }
        }

        // If the service is found, you can now use 'servicioSeleccionado' for further operations
        if (servicioSeleccionado != null) {
            return servicioSeleccionado;
        } else {
            return null;
        }
    }
    //encontrar cliente x nombre y guardar
    public Cliente guardarCliente(String cli){
        List<Cliente> clientes = control.traerClientes();
        Cliente clienteSeleccionado = null;

        // Loop through the list to find the Servicio object with the matching name
        for (Cliente cliente : clientes) {
            if ((cliente.getNombre()+" "+cliente.getApellido()).equals(cli)) {  // Compare the name of the service
                clienteSeleccionado = cliente;
                break;  // Stop the loop once the match is found
            }
        }

        // If the service is found, you can now use 'servicioSeleccionado' for further operations
        if (clienteSeleccionado != null) {
            return clienteSeleccionado;
        } else {
            return null;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser calendar;
    private javax.swing.JComboBox<String> cboClientes;
    private javax.swing.JComboBox<String> cboEmpleado;
    private javax.swing.JComboBox<String> cboEstado;
    private javax.swing.JComboBox<String> cboHora;
    private javax.swing.JComboBox<String> cboServicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCargaTurnos;
    private javax.swing.JPanel panelBtns;
    private javax.swing.JTextArea txtDetalle;
    // End of variables declaration//GEN-END:variables
}
