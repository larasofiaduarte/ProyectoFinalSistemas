package com.mycompany.proyectofinal.util;

/**
 * Conversión y parseo centralizado de cantidades de stock.
 * Regla de almacenamiento: siempre en la unidad base (ml o gr). "lt" se convierte a ml al parsear.
 * Regla de display: nunca se muestran litros — siempre "1000ml", nunca "1L".
 */
public class StockFormatter {

    private StockFormatter() {}

    /** Resultado de parsear un input de cantidad con unidad. */
    public static class ParseResult {
        public final double valor;   // valor en la unidad base (ml o gr)
        public final String unidad;  // "ml" o "gr" (nunca "lt")
        public ParseResult(double valor, String unidad) {
            this.valor = valor;
            this.unidad = unidad;
        }
    }

    /**
     * Formatea el stock para display con conversión automática ml↔lt.
     * Si el valor en ml es >= 1000 se muestra en litros (ej: 1500ml → "1.5lt").
     * Por debajo de 1000 se muestra en ml (ej: "500ml").
     */
    public static String format(double stock, String unidad) {
        if ("ml".equals(unidad)) {
            if (stock >= 1000) {
                return formatLt(stock / 1000.0) + "lt";
            }
            return DoubleVerifier.format(stock) + "ml";
        }
        if ("lt".equals(unidad)) {
            // Producto ya almacenado en lt (raro, pero se soporta)
            return formatLt(stock) + "lt";
        }
        if ("gr".equals(unidad) || "g".equals(unidad)) {
            return DoubleVerifier.format(stock) + "gr";
        }
        // unidades, null u otro → solo el número
        return DoubleVerifier.format(stock);
    }

    /** Formatea un valor en litros quitando ceros y punto innecesarios: 1.50 → "1.5", 2.00 → "2". */
    private static String formatLt(double litros) {
        String s = String.format("%.2f", litros);
        s = s.replaceAll("0+$", "").replaceAll("\\.$", "");
        return s;
    }

    /**
     * Parsea un string de cantidad con unidad y retorna valor + unidad normalizada.
     * Formatos aceptados: "1000ml", "1lt", "0.5lt", "250gr", "250g".
     * Número puro sin unidad: mantiene la unidad actual del producto (para compatibilidad con "unidades").
     * Lanza NumberFormatException si el formato no es reconocible.
     *
     * @param input        lo que escribió el usuario (ej: "1lt", "500ml")
     * @param unidadActual unidad actual del producto (usada cuando el input no tiene sufijo)
     */
    public static ParseResult parseConUnidad(String input, String unidadActual) {
        if (input == null || input.isBlank()) throw new NumberFormatException("vacío");
        // Quita espacios internos y pone en minúsculas para comparar unidades
        String s = input.trim().toLowerCase().replaceAll("\\s+", "");

        if (s.endsWith("lt")) {
            // lt se normaliza a ml: 1lt = 1000ml
            double val = Double.parseDouble(s.substring(0, s.length() - 2));
            return new ParseResult(val * 1000.0, "ml");
        }
        if (s.endsWith("ml")) {
            return new ParseResult(Double.parseDouble(s.substring(0, s.length() - 2)), "ml");
        }
        if (s.endsWith("gr")) {
            return new ParseResult(Double.parseDouble(s.substring(0, s.length() - 2)), "gr");
        }
        if (s.endsWith("g")) {
            // Acepta "g" como alias de "gr" (compatibilidad con registros anteriores)
            return new ParseResult(Double.parseDouble(s.substring(0, s.length() - 1)), "gr");
        }
        if (s.matches("\\d+(\\.\\d+)?")) {
            // Número puro sin unidad → mantiene la unidad actual (sirve para productos en "unidades")
            return new ParseResult(Double.parseDouble(s), unidadActual != null ? unidadActual : "ml");
        }
        throw new NumberFormatException("Formato inválido: " + input);
    }
}
