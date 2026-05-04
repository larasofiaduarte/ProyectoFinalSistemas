/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "actividad_usuario")
public class ActividadUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTablaAfectada() {
        return tablaAfectada;
    }

    public void setTablaAfectada(String tablaAfectada) {
        this.tablaAfectada = tablaAfectada;
    }

    public String getFilaAfectada() {
        return filaAfectada;
    }

    public void setFilaAfectada(String filaAfectada) {
        this.filaAfectada = filaAfectada;
    }

    public String getColumnaAfectada() {
        return columnaAfectada;
    }

    public void setColumnaAfectada(String columnaAfectada) {
        this.columnaAfectada = columnaAfectada;
    }

    public String getValorAnterior() {
        return valorAnterior;
    }

    public void setValorAnterior(String valorAnterior) {
        this.valorAnterior = valorAnterior;
    }

    public String getValorNuevo() {
        return valorNuevo;
    }

    public void setValorNuevo(String valorNuevo) {
        this.valorNuevo = valorNuevo;
    }

    public String getTipoAccion() {
        return tipoAccion;
    }

    public void setTipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "tabla_afectada")
    private String tablaAfectada;      // e.g. "clientes", "turnos", "inventario"

    @Column(name = "fila_afectada")
    private String filaAfectada;       // e.g. "ID: 42"

    @Column(name = "columna_afectada")
    private String columnaAfectada;    // e.g. "nombre"

    @Column(name = "valor_anterior")
    private String valorAnterior;

    @Column(name = "valor_nuevo")
    private String valorNuevo;

    @Column(name = "tipo_accion")
    private String tipoAccion;         // "INSERCION", "EDICION", "ELIMINACION"

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    // getters y setters...
}