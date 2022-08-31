package org.cis120.spaceinvader;

/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * 
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * GameCourt
 *
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    // the state of the game logic
    private Shooter shooter;
    private Integer lives = 3;
    private Invader[][] invader = new Invader[5][10];
    private int invaderY = 0; // used to initialize invaders to correct positions
    private int invaderX = 0; // used to initialize invader to correct positions
    private Boolean aboutToBounce;
    private Integer numInvaders = 50;
    private LinkedList<Attack> playerAttack = new LinkedList<Attack>();
    private LinkedList<Attack> invaderAttack = new LinkedList<Attack>();
    private LinkedList<Attack> playerAttacksToRemove = new LinkedList<Attack>();
    private LinkedList<Attack> invaderAttacksToRemove = new LinkedList<Attack>();
    private LinkedList<Shield> shields = new LinkedList<Shield>();
    private Boolean paused = false;
    private Boolean canAttack = false;
    private static BufferedImage background;
    private Integer score = 0;
    private Integer highScore = 0;
    private Integer upperRowY; /*
                                * stores y position of highest row of invaders (even if row is
                                * empty)
                                */

    private boolean playing = false;
    private JLabel status;

    // Game constants
    public static final int COURT_WIDTH = 340;
    public static final int COURT_HEIGHT = 550;
    public static final int SHOOTER_VELOCITY = 6;
    public static final int ATTACK_SPEED = 14;

    // timers
    public static final int INTERVAL = 35;
    public static final int ATTACK_INTERVAL = 500;

    public GameCourt(JLabel status) {
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!paused) {
                    tick();
                }
            }
        });
        timer.start();

        Timer invaderTimer = new Timer(ATTACK_INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!paused) {
                    invaderAttack();
                }
            }
        });
        invaderTimer.start();

        Timer shooterTimer = new Timer(ATTACK_INTERVAL * 2, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!canAttack) {
                    canAttack = true;
                }
            }
        });
        shooterTimer.start();

        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    shooter.setVx(-SHOOTER_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    shooter.setVx(SHOOTER_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE && canAttack) {
                    canAttack = false;
                    playerAttack.add(
                            new Attack(
                                    shooter.getPx() + shooter.getWidth() / 2,
                                    shooter.getPy(), -ATTACK_SPEED, COURT_WIDTH, COURT_HEIGHT,
                                    Color.BLUE
                            )
                    );
                }
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    shooter.setVx(0);
                    shooterTimer.setDelay(300);
                }
            }
        });

        this.status = status;
    }

    // methods created for testing purposes
    public Shooter getShooter() {
        return shooter;
    }

    public void setShooterPosX(int x) {
        shooter.setPx(x);
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public int getNumInvaders() {
        return numInvaders;
    }

    public int getUpperRowY() {
        return upperRowY;
    }

    public void setNumInvaders(int num) {
        this.numInvaders = num;
    }

    public void setInvaderXPosition(int row, int col, int x) {
        invader[row][col].setPx(x);
    }

    public void setInvaderYPosition(int row, int col, int y) {
        invader[row][col].setPy(y);
    }

    public void removeInvader(int row, int col) {
        invader[row][col] = null;
    }

    public void addInvaderAttack(Attack a) {
        invaderAttack.add(a);
    }

    public void addPlayerAttack(Attack a) {
        playerAttack.add(a);
    }

    @SuppressWarnings("unchecked")
    public List<Attack> getPlayerAttack() {
        return (List<Attack>) playerAttack.clone();
    }

    @SuppressWarnings("unchecked")
    public List<Attack> getInvaderAttack() {
        return (List<Attack>) invaderAttack.clone();
    }

    public Invader[][] getInvaders() {
        Invader[][] copy = new Invader[5][10];
        for (int i = 0; i < 5; i++) {
            copy[i] = Arrays.copyOf(invader[i], 10);
        }
        return copy;
    }

    public Shield getShield(int i) {
        return shields.get(i);
    }
    // end of methods for testing purposes

    /*
     * (Re-)set the game to its initial state.
     * fullReset is true if game should be reset to initial conditions and false
     * if data should be read from game file
     */
    public void reset(Boolean fullReset) {
        playerAttack.clear();
        invaderAttack.clear();
        shields.clear();
        try {
            if (background == null) {
                background = ImageIO.read(new File("files/space1.png"));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        if (fullReset) {
            shooter = new Shooter(0, COURT_WIDTH, COURT_HEIGHT);
            if (numInvaders > 0) {
                score = 0;
            }
            for (int row = 0; row < invader.length; row++) {
                for (int col = 0; col < invader[row].length; col++) {
                    invader[row][col] = new Invader(
                            score / 500 + 3, 0,
                            invaderX + 5, invaderY, COURT_WIDTH, COURT_HEIGHT
                    );
                    invaderX = invaderX + 25;
                }
                invaderY = invaderY + 35;
                invaderX = 0;
            }

            for (int i = 0; i < 3; i++) {
                Shield s = new Shield(50 + (100 * i), COURT_WIDTH, COURT_HEIGHT, 5);
                shields.add(s);
            }

            invaderY = 0;
            score = 0;
            lives = 3;
            numInvaders = 50;
            upperRowY = 0;

        } else {
            List<String> data = new LinkedList<String>();
            FileLineIterator li = new FileLineIterator("files/data.txt");
            while (li.hasNext()) {
                String s = li.next();
                data.add(s);
            }
            lives = Integer.valueOf(data.get(0));
            score = Integer.valueOf(data.get(1));
            highScore = Integer.valueOf(data.get(2));
            shooter = new Shooter(Integer.valueOf(data.get(3)), COURT_WIDTH, COURT_HEIGHT);
            for (int i = 0; i < 3; i++) {
                shields.add(
                        new Shield(
                                50 + (100 * i), COURT_WIDTH, COURT_HEIGHT,
                                Integer.valueOf(data.get(4 + i))
                        )
                );

            }

            upperRowY = Integer.valueOf(data.get(7));
            numInvaders = Integer.valueOf(data.get(8));
            int i = 9;
            for (int row = 0; row < invader.length; row++) {
                for (int col = 0; col < invader[row].length; col++) {
                    if (data.get(i).equals("null")) {
                        invader[row][col] = null;
                    } else {
                        invader[row][col] = new Invader(
                                score / 500 + 3, 0,
                                Integer.valueOf(data.get(i)), upperRowY, COURT_WIDTH, COURT_HEIGHT
                        );
                        invaderX = invaderX + 25;
                    }
                    i++;
                }
                upperRowY = upperRowY + 35;
                invaderX = 0;
            }
            upperRowY = Integer.valueOf(data.get(7));
        }

        playing = true;
        aboutToBounce = false;
        status.setText(
                "Lives Left: " + lives + " | Score: " + score + " | High Score: " + highScore
        );

        requestFocusInWindow();
    }

    public List<String> readDataFromFile() {
        List<String> data = new LinkedList<String>();
        FileLineIterator li = new FileLineIterator("files/data.txt");
        while (li.hasNext()) {
            String s = li.next();
            data.add(s);
        }
        return data;
    }

    // reverses x direction of invaders and moves y position downwards
    private void bounceAll(Direction d) {
        for (int row = 0; row < invader.length; row++) {
            for (int col = 0; col < invader[row].length; col++) {
                if (invader[row][col] != null) {
                    invader[row][col].bounce(d);
                    invader[row][col].setPy(invader[row][col].getPy() + 10);

                }
            }
        }
    }

    // checks if invader is about to be hit by attack
    public void checkInvaderDestroyed(Attack attack) {
        for (int row = 0; row < invader.length; row++) {
            for (int col = 0; col < invader[row].length; col++) {
                if (invader[row][col] != null && attack.intersects(invader[row][col])) {
                    invader[row][col] = null;
                    playerAttacksToRemove.add(attack);
                    numInvaders--;
                    score = score + 10;
                }
            }
        }
    }

    // checks if shield was hit
    public void checkShieldDamaged(Attack a) {
        Iterator<Shield> iter = shields.iterator();
        while (iter.hasNext()) {
            Shield s = iter.next();
            if (s.intersects(a) && s.getHealth() != 0) {
                s.setHealth(s.getHealth() - 1);
                if (a.getColor() == Color.RED) {
                    invaderAttacksToRemove.add(a);
                } else {
                    playerAttacksToRemove.add(a);
                }
            }
        }
    }

    // removes attacks that have hit an object
    public void removeAttacks() {
        Iterator<Attack> playerIter = playerAttacksToRemove.iterator();
        while (playerIter.hasNext()) {
            Attack a = playerIter.next();
            playerAttack.remove(a);
        }

        Iterator<Attack> invaderIter = invaderAttacksToRemove.iterator();
        while (invaderIter.hasNext()) {
            Attack a = invaderIter.next();
            invaderAttack.remove(a);
        }

    }

    // makes random bottom row invader shoot attack
    private void invaderAttack() {
        int col = 0;
        Boolean isColEmpty = false;
        int lowestInvader = 0;

        while (!isColEmpty) {
            col = (int) ((Math.random() * 10));
            for (int row = 0; row < invader.length; row++) {
                if (invader[row][col] != null) {
                    isColEmpty = true;
                    lowestInvader = row;
                }
            }
        }

        invaderAttack.add(
                new Attack(
                        invader[lowestInvader][col].getPx() +
                                invader[lowestInvader][col].getWidth() / 2,
                        invader[lowestInvader][col].getPy(), ATTACK_SPEED, COURT_WIDTH,
                        COURT_HEIGHT, Color.RED
                )
        );
    }

    // checks if game has met any of the ending conditions
    public int checkGameOver() {
        if (lives == 0) {
            return 1;
        } else if (numInvaders == 0) {
            int currScore = score;
            int currLives = lives + 1;
            reset(true);
            score = currScore;
            lives = currLives;
            return 2;
        } else {
            for (int row = 0; row < invader.length; row++) {
                for (int col = 0; col < invader[row].length; col++) {
                    if (invader[row][col] != null) {
                        if (invader[row][col].getPy() >= shooter.getPy()) {
                            return 1;
                        }
                    }
                }
            }
            return 0;
        }
    }

    private void drawAttacks(Graphics g) {
        Iterator<Attack> iter = playerAttack.iterator();
        while (iter.hasNext()) {
            Attack a = iter.next();
            if (a.getPy() > 3 && a != null) {
                a.draw(g);
            }
        }

        Iterator<Attack> iter2 = invaderAttack.iterator();
        while (iter2.hasNext()) {
            Attack a = iter2.next();
            if (a.getPy() < 530 && a != null) {
                a.draw(g);
            }
        }

    }

    private void drawShields(Graphics g) {
        Iterator<Shield> iter = shields.iterator();
        while (iter.hasNext()) {
            Shield s = iter.next();
            if (s.getHealth() != 0) {
                s.draw(g);
            }

        }
    }

    // checks if player was hit by an invader attack
    public void checkPlayerHit(Attack a) {
        if (a.intersects(shooter)) {
            lives--;
            invaderAttack.clear();
            playerAttack.clear();
            if (lives != 0) {
                resetPlayerHit();
            }
        }
    }

    // resets player to initial position
    private void resetPlayerHit() {
        shooter.setPx(0);
    }

    private void moveAttacks() {
        if (!playerAttack.isEmpty()) {
            Iterator<Attack> iter = playerAttack.iterator();
            while (iter.hasNext()) {
                Attack a = iter.next();
                if (a != null) {
                    a.move();
                    checkInvaderDestroyed(a);
                    checkShieldDamaged(a);
                }
            }
        }

        if (!invaderAttack.isEmpty()) {
            Iterator<Attack> iter = invaderAttack.iterator();
            while (iter.hasNext()) {
                Attack a = iter.next();
                if (a != null) {
                    a.move();
                    checkPlayerHit(a);
                    checkShieldDamaged(a);
                }
            }
        }
        removeAttacks();
    }

    public Boolean pause() {
        paused = !paused;
        return paused;
    }

    // stores game info in list
    private List<String> createStringListForFile() {
        List<String> data = new LinkedList<String>();
        data.add(lives.toString());
        data.add(score.toString());
        data.add(highScore.toString());
        data.add(((Integer) shooter.getPx()).toString());

        data.add(((Integer) shields.get(0).getHealth()).toString());
        data.add(((Integer) shields.get(1).getHealth()).toString());
        data.add(((Integer) shields.get(2).getHealth()).toString());

        data.add(upperRowY.toString());
        data.add(numInvaders.toString());
        for (int row = 0; row < invader.length; row++) {
            for (int col = 0; col < invader[row].length; col++) {
                if (invader[row][col] == null) {
                    data.add("null");
                } else {
                    data.add(((Integer) invader[row][col].getPx()).toString());
                }
            }
        }
        return data;
    }

    // writes list of game info to file
    public void writeStringsToFile(List<String> data, String filePath) {
        File file = Paths.get(filePath).toFile();
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, false));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Iterator<String> info = data.iterator();
        if (bw != null) {
            while (info.hasNext()) {
                String s = info.next();
                try {
                    bw.write(s);
                    if (info.hasNext()) {
                        bw.newLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // checks if invaders should move downwards and change direction
    public void checkBounceWall() {
        for (int row = 0; row < invader.length; row++) {
            for (int col = 0; col < invader[row].length; col++) {
                if (invader[row][col] != null) {

                    if (invader[row][col].hitWall() == Direction.RIGHT) {
                        aboutToBounce = true;
                        bounceAll(Direction.RIGHT);
                        upperRowY = upperRowY + 10;
                        break;
                    } else if (invader[row][col].hitWall() == Direction.LEFT) {
                        aboutToBounce = true;
                        bounceAll(Direction.LEFT);
                        upperRowY = upperRowY + 10;
                        break;
                    }
                }
            }
            if (aboutToBounce) {
                aboutToBounce = false;
                break;
            }
        }
    }

    public void moveInvaders() {
        for (int row = 0; row < invader.length; row++) {
            for (int col = 0; col < invader[row].length; col++) {
                if (invader[row][col] != null) {
                    invader[row][col].move();
                }
            }
        }
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing) {
            shooter.move();
            moveInvaders();
            checkBounceWall();
            int gameResponse = checkGameOver();
            if (gameResponse == 1) {
                playing = false;

                if (score > highScore) {
                    highScore = score;
                    status.setText("You finished with a new high score of: " + score + "!!!");
                } else if (lives == 0 && score == 0) {
                    status.setText("Restart To Play");
                } else {
                    status.setText("You finished with a score of: " + score);
                }
                score = 0;
            } else if (gameResponse == 0 || gameResponse == 2) {
                status.setText(
                        "Lives Left: " + lives + " | Score: " + score + " | High Score: "
                                + highScore
                );
            }
            List<String> data = createStringListForFile();
            writeStringsToFile(data, "files/data.txt");
            moveAttacks();

            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, COURT_WIDTH, COURT_HEIGHT, status);
        shooter.draw(g);
        for (int row = 0; row < invader.length; row++) {
            for (int col = 0; col < invader[row].length; col++) {
                if (invader[row][col] != null) {
                    invader[row][col].draw(g);
                }
            }
        }
        drawShields(g);
        drawAttacks(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}