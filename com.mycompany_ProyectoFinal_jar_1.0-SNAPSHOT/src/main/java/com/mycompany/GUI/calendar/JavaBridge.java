package com.mycompany.GUI.calendar;

/**
 * Objeto expuesto al JavaScript del WebView como "window.javaBridge".
 * Todos los métodos deben ser public para que JSObject los pueda invocar.
 *
 * Fase actual: solo logging. La lógica real se implementa en la siguiente fase.
 */
public class JavaBridge {

    /** Llamado desde JS cuando FullCalendar terminó de inicializarse. */
    public void onCalendarReady() {
        System.out.println("[JavaBridge] Calendar listo");
    }

    /**
     * Llamado desde JS cuando el usuario hace clic en un evento.
     *
     * @param id ID del turno como string
     */
    public void onEventClicked(String id) {
        System.out.println("[JavaBridge] Evento clickeado: id=" + id);
    }
}
