package org.cis120.spaceinvader;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Invader extends GameObj {
    public static final int SIZE = 20;
    public static final String IMG_FILE = "files/invader1.png";

    private static BufferedImage img;

    public Invader(int vx, int vy, int px, int py, int courtWidth, int courtHeight) {
        super(vx, vy, px, py, SIZE, SIZE, courtWidth, courtHeight);

        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
}
