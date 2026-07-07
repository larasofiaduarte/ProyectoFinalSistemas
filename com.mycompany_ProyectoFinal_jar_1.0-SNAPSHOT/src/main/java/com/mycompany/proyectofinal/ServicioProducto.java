package com.mycompany.proyectofinal;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class ServicioProducto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "servicio_id")
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private double cantidadUsada;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public ServicioProducto() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public double getCantidadUsada() { return cantidadUsada; }
    public void setCantidadUsada(double cantidadUsada) { this.cantidadUsada = cantidadUsada; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}
