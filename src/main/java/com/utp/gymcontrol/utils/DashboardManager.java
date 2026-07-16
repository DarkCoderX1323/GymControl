package com.utp.gymcontrol.utils;

import com.utp.gymcontrol.view.DashboardView;

/**
 * Mantiene una referencia única al Dashboard abierto para poder
 * actualizar sus métricas desde cualquier módulo del sistema.
 */
public final class DashboardManager {

    private static DashboardView dashboard;

    private DashboardManager() {
        // Evita instancias
    }

    /**
     * Registra la instancia activa del Dashboard.
     */
    public static void registrar(DashboardView view) {

        dashboard = view;

    }

    /**
     * Elimina la referencia cuando el Dashboard se cierra.
     */
    public static void limpiar() {

        dashboard = null;

    }

    /**
     * Actualiza las métricas del Dashboard si existe una instancia abierta.
     */
    public static void actualizar() {

        if (dashboard != null) {

            dashboard.actualizarDashboard();

        }

    }

    /**
     * Devuelve el Dashboard registrado.
     */
    public static DashboardView getDashboard() {

        return dashboard;

    }

}