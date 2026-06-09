/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;


import java.time.*;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author duart
 */

@Entity
public class Turno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime fecha;
    @ManyToOne
    @JoinColumn(name="Cliente")
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name="Servicio")
    private Servicio servicio;
    @Column(nullable = false)
    private String estado;
    private String detalle;
    @Column(name = "stock_descontado", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean stockDescontado = false;

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

   

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    
    public void setCliente(Cliente cliente){
        this.cliente = cliente;
    }
    
    public Cliente getCliente(){
        return cliente;
    }

    public boolean isStockDescontado() {
        return stockDescontado;
    }

    public void setStockDescontado(boolean stockDescontado) {
        this.stockDescontado = stockDescontado;
    }
}
