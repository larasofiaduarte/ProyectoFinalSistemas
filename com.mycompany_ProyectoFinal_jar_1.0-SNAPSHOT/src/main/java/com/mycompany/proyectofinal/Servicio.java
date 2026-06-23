/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name="servicios")
public class Servicio implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String nombre;
    private double precio;
    @OneToOne
    @JoinColumn(name="idEmpleado")
    private Usuario empleado; //idUsuario
    @OneToMany(mappedBy = "servicio")
    private List<Turno> turnos = new ArrayList<>();
    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ServicioProducto> productos = new ArrayList<>();
    @Column(name = "duracion_minutos")
    private int duracionMinutos = 60;

    public List<ServicioProducto> getProductos() {
        return productos;
    }

    public void setProductos(List<ServicioProducto> productos) {
        this.productos = productos;
    }

    public int getId() {
        return id;
    }

    public List<Turno> getTurnos() {
        return turnos;
    }

    public void setTurnos(List<Turno> turnos) {
        this.turnos = turnos;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Usuario getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Usuario emp) {
        this.empleado = emp;
    }
    
    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public void addProducto(ServicioProducto sp) {
        productos.add(sp);
        sp.setServicio(this);
    }

    public void removeProducto(ServicioProducto sp) {
        productos.remove(sp);
        sp.setServicio(null);
    }

    @Override
    public String toString() { return nombre != null ? nombre : ""; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Servicio)) return false;
        return this.id == ((Servicio) o).id;
    }

    @Override
    public int hashCode() { return id; }
}
