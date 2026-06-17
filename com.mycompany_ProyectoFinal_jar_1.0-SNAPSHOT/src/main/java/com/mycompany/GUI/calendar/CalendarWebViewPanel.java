package com.mycompany.GUI.calendar;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.net.URL;

/**
 * Panel Swing que embebe un WebView de JavaFX con FullCalendar.
 *
 * Ciclo de vida:
 *   1. Constructor crea JFXPanel en el EDT de Swing.
 *   2. Platform.runLater inicializa el WebView en el hilo de JavaFX.
 *   3. Cuando el HTML termina de cargar, se inyecta el bridge JS↔Java.
 *
 * NOTA DE EJECUCIÓN: Esta clase requiere que JavaFX esté en el module-path.
 * Correr la app con: mvn javafx:run
 * O en NetBeans: click derecho → Customize → Run → Goals: javafx:run
 */
public class CalendarWebViewPanel extends JPanel {

    private final JFXPanel jfxPanel;
    private final JavaBridge javaBridge;
    private WebEngine webEngine;

    public CalendarWebViewPanel() {
        super(new BorderLayout());

        javaBridge = new JavaBridge();

        // JFXPanel inicializa el runtime de JavaFX automáticamente (primera vez)
        jfxPanel = new JFXPanel();
        add(jfxPanel, BorderLayout.CENTER);

        // Evita que JavaFX se cierre cuando el panel se oculta (CardLayout)
        Platform.setImplicitExit(false);

        // La inicialización del WebView debe ocurrir en el hilo de JavaFX
        Platform.runLater(this::initWebView);
    }

    /** Inicializa el WebView y carga el HTML. Ejecuta en el hilo de JavaFX. */
    private void initWebView() {
        WebView webView = new WebView();
        webEngine = webView.getEngine();

        // Cuando el HTML termina de cargar, inyecta el bridge y notifica al JS
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                injectarBridge();
            } else if (newState == Worker.State.FAILED) {
                System.err.println("[Calendar] Falló la carga del HTML");
            }
        });

        webEngine.setOnError(e ->
            System.err.println("[Calendar] Error WebEngine: " + e.getMessage())
        );

        cargarHTML();

        jfxPanel.setScene(new Scene(webView));
    }

    /** Carga el archivo calendar.html desde los resources del classpath. */
    private void cargarHTML() {
        URL htmlUrl = getClass().getResource("/calendar/calendar.html");
        if (htmlUrl != null) {
            webEngine.load(htmlUrl.toExternalForm());
            System.out.println("[Calendar] Cargando: " + htmlUrl.toExternalForm());
        } else {
            System.err.println("[Calendar] Error crítico: no se encontró /calendar/calendar.html");
            // Muestra un fallback en el panel Swing
            SwingUtilities.invokeLater(() -> {
                removeAll();
                JLabel lbl = new JLabel("Error: calendar.html no encontrado en resources");
                lbl.setForeground(Color.RED);
                add(lbl, BorderLayout.CENTER);
                revalidate();
            });
        }
    }

    /**
     * Inyecta el objeto JavaBridge como window.javaBridge en la página.
     * Luego llama a onJavaBridgeReady() si el JS ya la definió.
     * Ejecuta en el hilo de JavaFX (llamado desde el listener del worker).
     */
    private void injectarBridge() {
        try {
            JSObject window = (JSObject) webEngine.executeScript("window");
            window.setMember("javaBridge", javaBridge);
            System.out.println("[Calendar] Bridge JS↔Java inyectado");
            // Notifica al JS que el bridge está listo
            webEngine.executeScript(
                "if (typeof onJavaBridgeReady === 'function') onJavaBridgeReady();"
            );
        } catch (Exception e) {
            System.err.println("[Calendar] Error al inyectar bridge: " + e.getMessage());
        }
    }

    // ── API pública para la siguiente fase ─────────────────────────────────

    /**
     * Envía un array JSON de turnos al calendario para que los renderice.
     * Puede llamarse desde cualquier hilo.
     *
     * @param json array JSON (e.g. "[{\"id\":\"1\",\"title\":\"Corte\",\"start\":\"2026-06-20T10:00\"}]")
     */
    public void enviarTurnosAlCalendario(String json) {
        Platform.runLater(() -> {
            if (webEngine != null) {
                webEngine.executeScript(
                    "if (typeof renderEvents === 'function') renderEvents(" + json + ");"
                );
            }
        });
    }

    /**
     * Placeholder para construir el JSON de turnos.
     * Implementación real en la siguiente fase.
     *
     * @return array JSON vacío por ahora
     */
    public String cargarTurnosComoJson() {
        return "[]";
    }
}
