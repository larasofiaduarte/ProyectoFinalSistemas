package com.mycompany.proyectofinal.util;

/** Representa una alerta de stock proyectado para un producto. */
public class StockAlerta {

    public enum Nivel { CRITICAL, WARNING }

    private final int    productoId;
    private final String productoNombre;
    private final double stockActual;
    private final double consumoProyectado;
    private final String unidad;
    private final Nivel  nivel;

    public StockAlerta(int productoId, String productoNombre,
                       double stockActual, double consumoProyectado,
                       String unidad, Nivel nivel) {
        this.productoId        = productoId;
        this.productoNombre    = productoNombre;
        this.stockActual       = stockActual;
        this.consumoProyectado = consumoProyectado;
        this.unidad            = unidad;
        this.nivel             = nivel;
    }

    public int    getProductoId()        { return productoId; }
    public String getProductoNombre()    { return productoNombre; }
    public double getStockActual()       { return stockActual; }
    public double getConsumoProyectado() { return consumoProyectado; }
    public String getUnidad()            { return unidad; }
    public Nivel  getNivel()             { return nivel; }
}
