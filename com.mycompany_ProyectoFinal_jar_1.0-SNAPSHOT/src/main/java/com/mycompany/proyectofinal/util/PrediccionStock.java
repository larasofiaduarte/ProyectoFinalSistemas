package com.mycompany.proyectofinal.util;

import com.mycompany.proyectofinal.Producto;
import com.mycompany.proyectofinal.ServicioProducto;
import com.mycompany.proyectofinal.Turno;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calcula alertas de agotamiento de stock basándose en los turnos pendientes
 * dentro de una ventana de 14 días.
 *
 * Solo considera ServicioProducto con producto específico (no los de categoría),
 * ya que para los de categoría no se sabe de antemano qué producto se consumirá.
 */
public class PrediccionStock {

    private static final int DIAS_VENTANA = 14;

    private PrediccionStock() {}

    /**
     * Genera la lista de alertas de stock para los próximos {@value DIAS_VENTANA} días.
     *
     * @param turnos     todos los turnos del sistema
     * @param productos  todos los productos del sistema
     * @return lista de alertas ordenada: CRITICAL primero, luego WARNING
     */
    public static List<StockAlerta> generarAlertas(List<Turno> turnos, List<Producto> productos) {
        LocalDate hoy    = LocalDate.now();
        LocalDate limite = hoy.plusDays(DIAS_VENTANA);

        // Acumula el consumo proyectado por producto ID sumando todos los turnos en ventana
        Map<Integer, Double> consumo = new HashMap<>();
        for (Turno t : turnos) {
            if (!"Pendiente".equalsIgnoreCase(t.getEstado())) continue;
            if (t.getFecha() == null) continue;

            LocalDate fechaTurno = t.getFecha().toLocalDate();
            // Solo incluye turnos desde hoy hasta hoy+14 (inclusive en ambos extremos)
            if (fechaTurno.isBefore(hoy) || fechaTurno.isAfter(limite)) continue;
            if (t.getServicio() == null) continue;

            for (ServicioProducto sp : t.getServicio().getProductos()) {
                // Ignorar entradas de categoría: no hay producto específico a evaluar
                if (sp.getProducto() == null) continue;
                int pid = sp.getProducto().getId();
                consumo.merge(pid, sp.getCantidadUsada(), Double::sum);
            }
        }

        if (consumo.isEmpty()) return Collections.emptyList();

        // Construye lookup de productos por ID para acceso O(1)
        Map<Integer, Producto> prodMap = new HashMap<>();
        for (Producto p : productos) prodMap.put(p.getId(), p);

        List<StockAlerta> alertas = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : consumo.entrySet()) {
            Producto p = prodMap.get(entry.getKey());
            if (p == null) continue;

            double stock = p.getStock();
            double uso   = entry.getValue();

            StockAlerta.Nivel nivel;
            if (uso >= stock) {
                // El consumo iguala o supera el stock disponible → se agota
                nivel = StockAlerta.Nivel.CRITICAL;
            } else if (uso >= stock * 0.8) {
                // El consumo consume más del 80% del stock → bajo stock
                nivel = StockAlerta.Nivel.WARNING;
            } else {
                continue; // stock suficiente, sin alerta
            }

            alertas.add(new StockAlerta(
                p.getId(), p.getNombre(), stock, uso, p.getUnidad(), nivel
            ));
        }

        // CRITICAL primero, después WARNING
        alertas.sort(Comparator.comparingInt(a -> a.getNivel() == StockAlerta.Nivel.CRITICAL ? 0 : 1));
        return alertas;
    }
}
