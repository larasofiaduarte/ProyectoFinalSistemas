package com.mycompany.proyectofinal.server;

import com.mycompany.proyectofinal.Controladora;
import com.mycompany.proyectofinal.Session;
import com.mycompany.proyectofinal.Turno;
import com.mycompany.proyectofinal.Usuario;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TurnoServer {

    private static final int PORT = 8080;
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static HttpServer server;

    public static void start() {
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
            server.createContext("/turnos", TurnoServer::handleTurnos);
            server.setExecutor(java.util.concurrent.Executors.newSingleThreadExecutor());
            server.start();
            System.out.println("[TurnoServer] Escuchando en http://localhost:" + PORT + "/turnos");
        } catch (IOException e) {
            System.err.println("[TurnoServer] No se pudo iniciar en el puerto " + PORT + ": " + e.getMessage());
        }
    }

    public static void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("[TurnoServer] Detenido");
        }
    }

    private static void handleTurnos(HttpExchange exchange) throws IOException {
        // CORS para que el WebView embebido pueda hacer fetch a localhost
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        String json = buildTurnosJson();
        byte[] bytes = json.getBytes("UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String buildTurnosJson() {
        Usuario currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            System.err.println("[TurnoServer] Sin usuario en sesión — devolviendo lista vacía");
            return "[]";
        }

        List<Turno> todos = new Controladora().traerTurnos();

        // Usa el empleado directo del turno; si no existe (datos viejos), cae al servicio
        List<Turno> misTurnos = todos.stream()
            .filter(t -> {
                Usuario emp = t.getEmpleado();
                if (emp == null && t.getServicio() != null) {
                    emp = t.getServicio().getEmpleado();
                }
                return emp != null && emp.getId() == currentUser.getId();
            })
            .collect(Collectors.toList());

        System.out.println("[TurnoServer] Enviando " + misTurnos.size() + " turno(s) para " + currentUser.getUsername());

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < misTurnos.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(turnoToJson(misTurnos.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    private static String turnoToJson(Turno t) {
        LocalDateTime start = t.getFecha();
        int duracion = (t.getServicio() != null && t.getServicio().getDuracionMinutos() > 0)
            ? t.getServicio().getDuracionMinutos() : 60;
        LocalDateTime end = (start != null) ? start.plusMinutes(duracion) : null;

        return "{"
            + "\"id\":"     + jsonStr(String.valueOf(t.getId())) + ","
            + "\"title\":"  + jsonStr(buildTitle(t)) + ","
            + "\"start\":"  + jsonStr(start != null ? start.format(ISO) : "") + ","
            + "\"end\":"    + jsonStr(end   != null ? end.format(ISO)   : "") + ","
            + "\"color\":\"#7f34c9\""
            + "}";
    }

    private static String buildTitle(Turno t) {
        String servicio = (t.getServicio() != null) ? t.getServicio().getNombre() : "Turno";
        String cliente  = (t.getCliente()  != null) ? t.getCliente().toString()   : "";
        return cliente.isEmpty() ? servicio : servicio + " — " + cliente;
    }

    // Serialización JSON manual segura: escapa comillas y barras
    private static String jsonStr(String s) {
        if (s == null) return "\"\"";
        return "\""
            + s.replace("\\", "\\\\")
               .replace("\"", "\\\"")
               .replace("\n", "\\n")
               .replace("\r", "\\r")
            + "\"";
    }
}
