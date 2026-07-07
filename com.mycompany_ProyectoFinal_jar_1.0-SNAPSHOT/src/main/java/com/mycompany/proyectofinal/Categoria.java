package com.mycompany.proyectofinal;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "categoria")
public class Categoria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private String unidad; // "ml", "gr", "unidades"

    public Categoria() {}

    public Categoria(String nombre, String unidad) {
        this.nombre = nombre;
        this.unidad = unidad;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    @Override
    public String toString() { return nombre != null ? nombre : ""; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;
        return this.id == ((Categoria) o).id;
    }

    @Override
    public int hashCode() { return id; }
}
