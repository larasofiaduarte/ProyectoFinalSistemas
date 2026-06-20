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

/**
 * Panel Swing que embebe un WebView de JavaFX para mostrar el calendario HTML.
 * Se usa en el dashboard (Inicio) como widget de vista rápida.
 *
 * Para que funcione, la app debe ejecutarse con: mvn javafx:run
 * (o configurar JavaFX en el module-path manualmente)
 */
public class CalendarPanel extends JPanel {

    private volatile WebEngine engine;
    private volatile boolean pageLoaded = false;
    private volatile boolean pendingDark = false;

    public CalendarPanel() {
        super(new BorderLayout());
        setOpaque(false); // capa Swing 1: CalendarPanel transparente

        JFXPanel jfxPanel = new JFXPanel();
        jfxPanel.setOpaque(false);                        // capa Swing 2: JFXPanel transparente
        jfxPanel.setBackground(new Color(0, 0, 0, 0));
        add(jfxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            WebView webView = new WebView();
            webView.setStyle("-fx-background-color: transparent;"); // capa JavaFX 1: WebView transparente
            engine = webView.getEngine();

            // Aplica el tema pendiente una vez que la página termine de cargar
            engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    pageLoaded = true;
                    execThemeJs(pendingDark);
                }
            });

            URL url = getClass().getResource("/calendar/calendar.html");
            if (url != null) {
                engine.load(url.toExternalForm());
            } else {
                System.err.println("[CalendarPanel] No se encontró /calendar/calendar.html en el classpath");
            }

            Scene scene = new Scene(webView);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT); // capa JavaFX 2: Scene transparente
            jfxPanel.setScene(scene);
        });
    }

    public void applyTheme(boolean isDark) {
        pendingDark = isDark;
        if (!pageLoaded) return; // la página no está lista; el listener lo aplicará al cargar
        Platform.runLater(() -> execThemeJs(isDark));
    }

    private void execThemeJs(boolean isDark) {
        if (engine == null) return;
        try {
            String js = isDark
                ? "document.body.classList.add('dark-mode')"
                : "document.body.classList.remove('dark-mode')";
            engine.executeScript(js);
        } catch (Exception e) {
            System.err.println("[CalendarPanel] JS error: " + e.getMessage());
        }
    }
}
