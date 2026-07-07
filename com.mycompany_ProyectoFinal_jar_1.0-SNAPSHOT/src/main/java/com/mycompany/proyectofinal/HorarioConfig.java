package com.mycompany.proyectofinal;

import java.time.LocalTime;
import java.util.List;

public class HorarioConfig {

    // Default: corrido 08:00–20:00
    private static List<LocalTime[]> intervalos = List.<LocalTime[]>of(
        new LocalTime[]{LocalTime.of(8, 0), LocalTime.of(20, 0)}
    );

    public static List<LocalTime[]> getIntervalos() { return intervalos; }

    public static void setIntervalos(List<LocalTime[]> nuevos) {
        intervalos = nuevos;
    }
}
