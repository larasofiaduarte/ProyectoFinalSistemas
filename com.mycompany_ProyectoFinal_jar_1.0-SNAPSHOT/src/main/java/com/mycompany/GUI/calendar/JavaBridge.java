package com.mycompany.GUI.calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Objeto expuesto al JavaScript del WebView como "window.javaBridge".
 * Todos los métodos deben ser public para que JSObject los pueda invocar.
 *
 * Fase actual: solo logging. La lógica real se implementa en la siguiente fase.
 */
public class JavaBridge {

    private static final Logger logger = LogManager.getLogger(JavaBridge.class);

    /** Llamado desde JS cuando FullCalendar terminó de inicializarse. */
    public void onCalendarReady() {
        logger.debug("[JavaBridge] Calendar listo");
    }

    /**
     * Llamado desde JS cuando el usuario hace clic en un evento.
     *
     * @param id ID del turno como string
     */
    public void onEventClicked(String id) {
        logger.debug("[JavaBridge] Evento clickeado: id={}", id);
    }
}
