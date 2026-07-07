package com.mycompany.proyectofinal;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "productos")
public class Producto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nombre;

    private double stock;
    private double minimo;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "idProveedor")
    private Proveedor Proveedor;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getStock() { return stock; }
    public void setStock(double stock) { this.stock = stock; }

    public double getMinimo() { return minimo; }
    public void setMinimo(double minimo) { this.minimo = minimo; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    /** Unidad derivada de la categoría; "ml" como fallback si no hay categoría asignada. */
    public String getUnidad() {
        return categoria != null ? categoria.getUnidad() : "ml";
    }

    public Proveedor getProveedor() { return Proveedor; }
    public void setProveedor(Proveedor proveedor) { this.Proveedor = proveedor; }

    @Override
    public String toString() { return nombre; }
}
