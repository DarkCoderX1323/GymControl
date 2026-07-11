package com.utp.gymcontrol.utils;

import java.awt.Color;
import java.awt.Font;

/**
 * Paleta de colores y tipografía centralizada para el rediseño visual
 * "modo noche" de GymControl.
 *
 * Todas las vistas deben tomar sus colores y fuentes de esta clase en vez
 * de definir valores sueltos, para mantener consistencia visual y facilitar
 * cambios futuros de tema.
 */
public class Tema {

    // =========================
    // COLORES DE FONDO
    // =========================

    /** Fondo general de las ventanas. */
    public static final Color FONDO = new Color(0x0B, 0x0D, 0x12);

    /** Superficie para tarjetas y paneles elevados. */
    public static final Color SUPERFICIE = new Color(0x14, 0x16, 0x1D);

    /** Superficie clara para inputs (campos de texto, etc.). */
    public static final Color SUPERFICIE_CLARA = new Color(0x1C, 0x1F, 0x28);

    // =========================
    // COLOR DE ACENTO
    // =========================

    public static final Color ACENTO = new Color(0x4F, 0x7C, 0xFF);

    /** Versión más oscura del acento, útil para hover/pressed. */
    public static final Color ACENTO_OSCURO = new Color(0x3D, 0x63, 0xD1);

    // =========================
    // TEXTO
    // =========================

    public static final Color TEXTO_PRIMARIO = new Color(0xF2, 0xF3, 0xF5);
    public static final Color TEXTO_SECUNDARIO = new Color(0x9A, 0xA0, 0xAC);

    // =========================
    // ESTADOS
    // =========================

    public static final Color EXITO = new Color(0x34, 0xC7, 0x85);
    public static final Color ADVERTENCIA = new Color(0xF5, 0xA5, 0x24);
    public static final Color PELIGRO = new Color(0xE2, 0x4B, 0x4A);

    // =========================
    // TIPOGRAFÍA
    // =========================

    private static final String FAMILIA_FUENTE = "Segoe UI";

    public static Font fuenteTitulo() {
        return new Font(FAMILIA_FUENTE, Font.BOLD, 32);
    }

    public static Font fuenteSubtitulo() {
        return new Font(FAMILIA_FUENTE, Font.PLAIN, 15);
    }

    public static Font fuenteRegular() {
        return new Font(FAMILIA_FUENTE, Font.PLAIN, 16);
    }

    public static Font fuenteEtiqueta() {
        return new Font(FAMILIA_FUENTE, Font.PLAIN, 14);
    }

    public static Font fuenteBoton() {
        return new Font(FAMILIA_FUENTE, Font.BOLD, 16);
    }

    private Tema() {
        // Clase de solo constantes, no instanciable.
    }
}
