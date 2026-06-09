package com.mycompany.proyectofinal.util;

/**
 * Centralised display-only conversion for stock values.
 *
 * Storage is always in the base unit (ml for liquids).
 * This class converts to a human-readable string without touching
 * any persisted value.
 *
 * Rules (ml only):
 *   stock <  1000  →  "500 ml"
 *   stock >= 1000  →  "1.5 lt"  (no unnecessary trailing zeros)
 *
 * All other units are returned as a plain formatted number.
 */
public class StockFormatter {

    private StockFormatter() {}

    /**
     * @param stock  raw stock value as stored in the database
     * @param unidad unit string from Producto.getUnidad() — may be null
     * @return display string, never null
     */
    public static String format(double stock, String unidad) {
        if ("ml".equals(unidad)) {
            if (stock >= 1000) {
                return toLitros(stock) + " lt";
            }
            return DoubleVerifier.format(stock) + " ml";
        }
        return DoubleVerifier.format(stock);
    }

    // ── private ──────────────────────────────────────────────────

    /** Converts ml to a clean litre string: 2000 → "2", 1500 → "1.5", 1250 → "1.25" */
    private static String toLitros(double ml) {
        double litros = ml / 1000.0;
        String s = String.format("%.2f", litros);   // "2.00", "1.50", "1.25"
        s = s.replaceAll("0+$", "");                // "2.", "1.5", "1.25"
        s = s.replaceAll("\\.$", "");               // "2",  "1.5", "1.25"
        return s;
    }
}
