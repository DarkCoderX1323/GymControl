package com.utp.gymcontrol.utils;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * JPanel personalizado con esquinas redondeadas, ya que Swing no lo soporta
 * de forma nativa. Reutilizable para tarjetas, contenedores de login, y
 * cualquier superficie elevada del rediseño "modo noche".
 */
public class PanelRedondeado extends JPanel {

    private int radio;
    private Color colorFondo;

    /**
     * @param radio      radio de las esquinas en píxeles
     * @param colorFondo color de fondo del panel
     */
    public PanelRedondeado(int radio, Color colorFondo) {
        this.radio = radio;
        this.colorFondo = colorFondo;
        setOpaque(false);
    }

    public PanelRedondeado(int radio) {
        this(radio, Tema.SUPERFICIE);
    }

    public void setColorFondo(Color colorFondo) {
        this.colorFondo = colorFondo;
        repaint();
    }

    public void setRadio(int radio) {
        this.radio = radio;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(colorFondo);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);

        } finally {
            g2.dispose();
        }

        super.paintComponent(g);
    }
}
