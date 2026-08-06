package com.mycompany.proyectofinal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Lee y parsea logs/app.log (formato de log4j2.xml: "yyyy-MM-dd HH:mm:ss [thread] LEVEL logger -
 * mensaje") y devuelve solo las entradas ERROR/WARN — el nivel real que log4j2 ya escribe en cada
 * línea, no un filtro por contenido del mensaje. app.log en sí sigue logueando todo sin cambios;
 * el filtro ocurre acá, al leer para la UI, nunca al escribir.
 */
public class LogReader {

    private static final Pattern LINEA_LOG = Pattern.compile(
        "^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\s+\\[[^\\]]*\\]\\s+(\\S+)\\s+(.*)$"
    );

    // Niveles reales de log4j2 que interesan mostrar en la UI — INFO/DEBUG/TRACE quedan afuera.
    private static final Set<String> NIVELES_VISIBLES = Set.of("ERROR", "WARN", "FATAL");

    private LogReader() {}

    public static class LogEntry {
        public final String fechaHora;
        public final String nivel;
        public String mensaje; // mutable: las líneas de stacktrace se van agregando acá

        public LogEntry(String fechaHora, String nivel, String mensaje) {
            this.fechaHora = fechaHora;
            this.nivel = nivel;
            this.mensaje = mensaje;
        }

        void agregarLineaStacktrace(String linea) {
            mensaje = mensaje + "\n" + linea;
        }
    }

    /**
     * @param rutaArchivo      ruta al archivo de log (ej: "logs/app.log")
     * @param maxLineasCrudas  cantidad máxima de líneas crudas a escanear (de las últimas del
     *                         archivo) antes de filtrar — como la mayoría son INFO y se descartan,
     *                         se escanea una ventana más grande que la cantidad de entradas que
     *                         realmente se van a mostrar, sin llegar a cargar el archivo entero.
     * @return entradas ERROR/WARN/FATAL (con su stacktrace, si tenían), más recientes primero
     * @throws IOException si el archivo no existe o no se puede leer
     */
    public static List<LogEntry> leerUltimasLineas(String rutaArchivo, int maxLineasCrudas) throws IOException {
        Path path = Path.of(rutaArchivo);
        if (!Files.exists(path)) {
            throw new IOException("El archivo de log no existe: " + rutaArchivo);
        }
        if (!Files.isReadable(path)) {
            throw new IOException("No se tiene permiso de lectura sobre: " + rutaArchivo);
        }

        // Buffer circular: nunca mantiene más de maxLineasCrudas líneas crudas en memoria,
        // sin importar el tamaño total del archivo.
        Deque<String> ultimas = new ArrayDeque<>(maxLineasCrudas);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (ultimas.size() == maxLineasCrudas) ultimas.removeFirst();
                ultimas.addLast(linea);
            }
        }

        // Reconstruye entradas multi-línea: una línea que no matchea el formato (ej: un renglón
        // de stacktrace) se agrega a la entrada anterior en vez de tratarse como una entrada
        // propia — así un error con excepción se ve completo, no partido en filas sueltas.
        List<LogEntry> entradas = new ArrayList<>();
        for (String linea : ultimas) {
            Matcher m = LINEA_LOG.matcher(linea);
            if (m.matches()) {
                entradas.add(new LogEntry(m.group(1), m.group(2).trim(), m.group(3)));
            } else if (!entradas.isEmpty()) {
                entradas.get(entradas.size() - 1).agregarLineaStacktrace(linea);
            }
            // Si la ventana escaneada empieza en medio de un stacktrace, esa primera línea suelta
            // no tiene entrada padre a la que sumarse — se descarta (caso borde, sin impacto real).
        }

        List<LogEntry> filtradas = entradas.stream()
            .filter(e -> NIVELES_VISIBLES.contains(e.nivel.toUpperCase()))
            .collect(Collectors.toList());

        Collections.reverse(filtradas); // más recientes primero
        return filtradas;
    }
}
