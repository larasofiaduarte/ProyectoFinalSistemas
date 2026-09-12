package com.mycompany.GUI.calendar;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Panel Swing que embebe un WebView de JavaFX para mostrar el calendario HTML.
 * Se usa en el dashboard (Inicio) como widget de vista rápida.
 *
 * Para que funcione, la app debe ejecutarse con: mvn javafx:run
 * (o configurar JavaFX en el module-path manualmente)
 */
public class CalendarPanel extends JPanel {

    private static final Logger logger = LogManager.getLogger(CalendarPanel.class);
    private static final Color DARK_BG  = new Color(43, 43, 45);
    private static final Color LIGHT_BG = new Color(250, 250, 250);

    private volatile WebEngine engine;
    private volatile WebView   webView;
    private volatile boolean   pageLoaded = false;
    private volatile boolean   pendingDark = false;
    private JFXPanel           jfxPanel;
    private volatile Scene     scene;

    public CalendarPanel() {
        super(new BorderLayout());
        setOpaque(false);

        jfxPanel = new JFXPanel();
        jfxPanel.setOpaque(true);
        jfxPanel.setBackground(LIGHT_BG);
        add(jfxPanel, BorderLayout.CENTER);

        // Evita que el runtime de JavaFX se cierre al destruirse la Ventana (logout): sin esto,
        // el segundo login crea un JFXPanel nuevo pero Platform.runLater no tiene toolkit vivo
        // y el WebView nunca se inicializa (queda en blanco).
        Platform.setImplicitExit(false);

        Platform.runLater(() -> {
            webView = new WebView();
            webView.setStyle("-fx-background-color: rgb(250,250,250);");
            engine = webView.getEngine();

            engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    pageLoaded = true;
                    applyAllLayers(pendingDark);
                }
            });

            URL url = getClass().getResource("/calendar/calendar.html");
            if (url != null) {
                engine.load(url.toExternalForm());
            } else {
                logger.error("[CalendarPanel] No se encontró /calendar/calendar.html en el classpath");
            }

            scene = new Scene(webView);
            scene.setFill(javafx.scene.paint.Color.rgb(250, 250, 250));
            jfxPanel.setScene(scene);
        });
    }

    /**
     * Fuerza al FullCalendar embebido a volver a pedir los turnos al servidor local
     * (GET /turnos), sin recargar la página ni cambiar de mes. FullCalendar solo
     * refetchea su "events: {url}" cuando cambia el rango visible, por eso alta/edición
     * de turnos no se reflejaban hasta navegar el calendario manualmente.
     */
    public void refrescarTurnos() {
        Platform.runLater(() -> {
            if (engine != null && pageLoaded) {
                try {
                    engine.executeScript(
                        "if (typeof calendar !== 'undefined' && calendar) { calendar.refetchEvents(); }"
                    );
                } catch (Exception e) {
                    logger.warn("[CalendarPanel] Error al refrescar turnos: {}", e.getMessage());
                }
            }
        });
    }

    public void applyTheme(boolean isDark) {
        pendingDark = isDark;

        // Capa Swing — se puede llamar desde cualquier hilo
        SwingUtilities.invokeLater(() -> {
            jfxPanel.setOpaque(true);
            jfxPanel.setBackground(isDark ? DARK_BG : LIGHT_BG);
            jfxPanel.repaint();
        });

        // Capas JavaFX
        Platform.runLater(() -> applyAllLayers(isDark));
    }

    // Aplica color en todas las capas JavaFX + HTML. Siempre en hilo JavaFX.
    private void applyAllLayers(boolean isDark) {
        Color bg   = isDark ? DARK_BG  : LIGHT_BG;
        String hex = isDark ? "rgb(43,43,45)" : "rgb(250,250,250)";

        if (scene   != null) scene.setFill(javafx.scene.paint.Color.rgb(bg.getRed(), bg.getGreen(), bg.getBlue()));
        if (webView != null) webView.setStyle("-fx-background-color: " + hex + ";");

        if (engine != null && pageLoaded) {
            try {
                engine.executeScript(
                    isDark
                        ? "document.documentElement.classList.add('dark-mode'); document.body.classList.add('dark-mode');"
                        : "document.documentElement.classList.remove('dark-mode'); document.body.classList.remove('dark-mode');"
                );
            } catch (Exception e) {
                logger.warn("[CalendarPanel] JS error: {}", e.getMessage());
            }
        }
    }
}
