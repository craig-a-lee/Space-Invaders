package org.cis120.spaceinvader;

import java.awt.*;

public class Attack extends GameObj {
    public static final int SIZE_Y = 10;
    public static final int SIZE_X = 5;
    public static final int INIT_VEL_X = 0;

    private Color color;

    public Attack(int posX, int posY, int velY, int courtWidth, int courtHeight, Color color) {
        super(INIT_VEL_X, velY, posX, posY, SIZE_X, SIZE_Y, courtWidth, courtHeight);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}
