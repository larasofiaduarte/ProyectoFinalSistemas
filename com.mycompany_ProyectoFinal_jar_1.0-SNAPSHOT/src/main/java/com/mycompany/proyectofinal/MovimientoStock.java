package com.mycompany.proyectofinal;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

/**
 * Audit trail for every stock change.
 *
 * tipo values: "SALIDA" (stock discounted when a Turno is finalised)
 *              "ENTRADA" (stock restored when a Turno is reverted)
 *
 * Previous movements are NEVER deleted. A revert always adds an
 * ENTRADA row; it does not remove the original SALIDA row.
 */
@Entity
@Table(name = "movimiento_stock")
public class MovimientoStock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private double cantidad;

    /** "SALIDA" or "ENTRADA" */
    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private LocalDateTime fecha;

    /** References the Turno that triggered this movement (nullable). */
    @Column(name = "turno_id")
    private Integer turnoId;

    public MovimientoStock() {}

    // ── getters / setters ────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Integer getTurnoId() { return turnoId; }
    public void setTurnoId(Integer turnoId) { this.turnoId = turnoId; }
}
