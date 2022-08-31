package org.cis120.spaceinvader;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Shield extends GameObj {
    public static final int SIZE = 40;
    public static final int INIT_POS_Y = 400;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    private BufferedImage img;
    private String imgFile;
    private Integer health;

    public Shield(int px, int courtWidth, int courtHeight, Integer health) {
        super(INIT_VEL_X, INIT_VEL_Y, px, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);
        this.health = health;
        switch (health) {
            case 1:
                imgFile = "files/shield5.png";
                break;
            case 2:
                imgFile = "files/shield4.png";
                break;
            case 3:
                imgFile = "files/shield3.png";
                break;
            case 4:
                imgFile = "files/shield2.png";
                break;
            case 5:
                imgFile = "files/shield1.png";
                break;
            default:
                imgFile = "files/shield1.png";
                break;
        }

        try {
            img = ImageIO.read(new File(imgFile));
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;

        switch (health) {
            case 1:
                imgFile = "files/shield5.png";
                break;
            case 2:
                imgFile = "files/shield4.png";
                break;
            case 3:
                imgFile = "files/shield3.png";
                break;
            case 4:
                imgFile = "files/shield2.png";
                break;
            case 5:
                imgFile = "files/shield1.png";
                break;
            default:
                imgFile = "files/shield1.png";
                break;
        }

        try {
            img = ImageIO.read(new File(imgFile));
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }

}
