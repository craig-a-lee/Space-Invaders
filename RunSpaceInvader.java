package org.cis120.spaceinvader;

// import java.awt.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class RunSpaceInvader implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for
        // local variables.

        // Top-level frame in which game components live.
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("SPACE INVADERS");
        frame.setLocation(0, 0);

        // Status panel (lives etc)
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // Reset button & pause button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton reset = new JButton("Restart");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset(true);
            }
        });
        control_panel.add(reset);

        final JButton instructions = new JButton("How To Play / Pause Game");
        instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.pause();
                JOptionPane.showOptionDialog(
                        frame,
                        "Oh no! Aliens are trying to invade earth and you were sent \n"
                                + "out into space to stop them before they get too close! \n"
                                + "- You can control your spaceship using the left and right "
                                + "arrow keys. \n"
                                + "- You can fire back at the aliens using spacebar. \n"
                                + "- You start off with 3 lives and each time you get hit by an "
                                + "alien's attack \n"
                                + "  you lose a life. If you lose all your lives then"
                                + " your game is over. \n"
                                + "- Each round has 50 aliens and if you complete a round you "
                                + "get an additional life. \n"
                                + "- However, if even one of the aliens gets past you,"
                                + " it doesn't matter how \n"
                                + "  many lives you have left because your game will be over. \n",
                        "Space Invaders Instructions",
                        JOptionPane.OK_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null, new String[] { "Ok!" }, null
                );
                court.pause();
                control_panel.setFocusable(false);
                court.setFocusable(true);
                court.requestFocusInWindow();
            }
        });
        control_panel.add(instructions);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset(false);
    }
}
