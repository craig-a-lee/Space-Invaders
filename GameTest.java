package org.cis120.spaceinvader;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.List;

import javax.swing.JLabel;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class GameTest {
    private static final int COURT_WIDTH = 340;
    private static final int COURT_HEIGHT = 550;
    private static final int ATTACK_SPEED = 14;

    @Test
    public void testCheckPlayerHitPlayerLosesLife() {
        JLabel status = new JLabel("Running...");
        GameCourt court = new GameCourt(status);
        court.reset(true);

        court.setShooterPosX(100);
        Attack a = new Attack(
                100,
                500,
                -ATTACK_SPEED, COURT_WIDTH, COURT_HEIGHT,
                Color.RED
        );
        court.checkPlayerHit(a);
        int expected = 2;
        assertEquals(expected, court.getLives());
    }

    @Test
    public void testCheckPlayerHitResetsPlayerPosition() {
        JLabel status = new JLabel("Running...");
        GameCourt court = new GameCourt(status);
        court.reset(true);

        court.setShooterPosX(100);
        Attack a = new Attack(
                100,
                500,
                -ATTACK_SPEED, COURT_WIDTH, COURT_HEIGHT,
                Color.RED
        );
        court.checkPlayerHit(a);
        int expected = 0;
        assertEquals(expected, court.getShooter().getPx());
    }

    @Test
    public void testGameOverPlayerHasZeroLives() {
        JLabel status = new JLabel("Testing...");
        GameCourt court = new GameCourt(status);
        court.reset(true);

        for (int i = 0; i < 3; i++) {
            Attack a = new Attack(
                    0, 500, ATTACK_SPEED,
                    COURT_WIDTH, COURT_HEIGHT, Color.RED
            ); // create attacks to hit player
            court.checkPlayerHit(a);
        }

        int actual = court.checkGameOver();
        int expected = 1; // Game is completely over
        assertEquals(expected, actual);
    }

    @Test
    public void testGameOverInvaderHasReachedShooter() {
        JLabel status = new JLabel("Testing...");
        GameCourt court = new GameCourt(status);
        court.reset(true);

        court.setInvaderYPosition(4, 9, 520);

        int actual = court.checkGameOver();
        int expected = 1; // Game is completely over
        assertEquals(expected, actual);

    }

    @Test
    public void testGameOverPlayerWinsRoundSoGetsAddtionalLife() {
        JLabel status = new JLabel("Testing...");
        GameCourt court = new GameCourt(status);
        court.reset(true);

        court.setNumInvaders(0);
        court.checkGameOver();
        int expected = 4;
        assertEquals(expected, court.getLives());
    }

    @Test
    public void testcheckInvaderDestroyedInvaderBecomesNull() {
        JLabel status = new JLabel("Testing...");
        GameCourt court = new GameCourt(status);
        court.reset(true);
        Invader[][] originalInvaders = court.getInvaders();
        assertNotNull(originalInvaders[4][6]);
        // sets attack at position of one of the invaders
        Attack a = new Attack(
                originalInvaders[4][6].getPx(),
                originalInvaders[4][6].getPy(),
                -ATTACK_SPEED, COURT_WIDTH, COURT_HEIGHT,
                Color.BLUE
        );
        court.checkInvaderDestroyed(a);
        Invader[][] updatedInvaders = court.getInvaders();
        assertNull(updatedInvaders[4][6]);
    }

    @Test
    public void testcheckInvaderDestroyedNumInvadersDecreases() {
        JLabel status = new JLabel("Testing...");
        GameCourt court = new GameCourt(status);
        court.reset(true);
        Invader[][] originalInvaders = court.getInvaders();
        // sets attack at position of one of the invaders
        Attack a = new Attack(
                originalInvaders[4][6].getPx(),
                originalInvaders[4][6].getPy(),
                -ATTACK_SPEED, COURT_WIDTH, COURT_HEIGHT,
                Color.BLUE
        );
        court.checkInvaderDestroyed(a);
        int expected = 49;
        assertEquals(expected, court.getNumInvaders());
    }

    @Test
    public void testCheckInvaderDestroyedScoreIncreases() {
        JLabel status = new JLabel("Testing...");
        GameCourt court = new GameCourt(status);
        court.reset(true);
        Invader[][] originalInvaders = court.getInvaders();
        Attack a = new Attack(
                originalInvaders[3][5].getPx(),
                originalInvaders[3][5].getPy(),
                -ATTACK_SPEED, COURT_WIDTH, COURT_HEIGHT,
                Color.BLUE
        );
        court.checkInvaderDestroyed(a);
        int expected = 10;
        assertEquals(expected, court.getScore());
    }

    @Test
    public void testInvaderSpeedIncreasesAtStartOfNewRound() {
        JLabel status = new JLabel("Testing...");
        GameCourt court = new GameCourt(status);
        court.reset(true);

        Invader[][] originalInvaders = court.getInvaders();
        assertEquals(3, originalInvaders[0][0].getVx());

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                Attack a = new Attack(
                        originalInvaders[row][col].getPx(),
                        originalInvaders[row][col].getPy(),
                        -ATTACK_SPEED, COURT_WIDTH, COURT_HEIGHT,
                        Color.BLUE
                );
                court.checkInvaderDestroyed(a);
            }
        }

        court.checkGameOver();

        Invader[][] updatedInvaders = court.getInvaders();
        assertEquals(4, updatedInvaders[0][0].getVx());
    }

    @Test
    public void testCheckPlayerHitAttackMissesSoPlayerPositionIsSame() {
        JLabel status = new JLabel("Testing...");
        GameCourt court = new GameCourt(status);
        court.reset(true);

        court.setShooterPosX(100);
        Attack a = new Attack(
                200,
                500,
                -ATTACK_SPEED, COURT_WIDTH, COURT_HEIGHT,
                Color.RED
        );
        court.checkPlayerHit(a);
        int expected = 100;
        assertEquals(expected, court.getShooter().getPx());
    }

    @Test
    public void testCheckPlayerHitAttackMissesSoLivesAreSame() {
        JLabel status = new JLabel("Testing...");
        GameCourt court = new GameCourt(status);
        court.reset(true);

        court.setShooterPosX(100);
        Attack a = new Attack(
                200,
                500,
                -ATTACK_SPEED, COURT_WIDTH, COURT_HEIGHT,
                Color.RED
        );
        court.checkPlayerHit(a);
        int expected = 3;
        assertEquals(expected, court.getLives());
    }

    @Test
    public void testCheckInvaderHitAttackMissesSoNumInvadersIsSame() {
        JLabel status = new JLabel("Testing...");
        GameCourt court = new GameCourt(status);
        court.reset(true);
        Attack a = new Attack(
                300,
                0,
                -ATTACK_SPEED, COURT_WIDTH, COURT_HEIGHT,
                Color.BLUE
        );
        court.checkInvaderDestroyed(a);
        int expected = 50;
        assertEquals(expected, court.getNumInvaders());
    }

    @Test
    public void testCheckBounceWall() {
        JLabel status = new JLabel("Testing...");
        GameCourt court = new GameCourt(status);
        court.reset(true);
        Invader[][] originalInvaders = court.getInvaders();

        // test before bounce
        assertEquals(3, originalInvaders[0][0].getVx());
        assertEquals(0, originalInvaders[0][0].getPy());
        // slightly moves all invaders to right so bounce is effective
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                court.setInvaderXPosition(row, col, col + 5);

            }
        }

        // moves last column to the right-sided wall to cause bounce
        int col = 9;
        for (int row = 0; row < 5; row++) {
            court.setInvaderXPosition(row, col, 340);
        }

        court.checkBounceWall();

        // test after bounce
        Invader[][] updatedInvaders = court.getInvaders();
        assertEquals(-3, updatedInvaders[0][0].getVx());
        assertEquals(10, updatedInvaders[0][0].getPy());

    }

    @Test
    public void testbounceWallNullInvadersHaveNoEffectOnBouncing() {
        JLabel status = new JLabel("Testing...");
        GameCourt court = new GameCourt(status);
        court.reset(true);
        Invader[][] originalInvaders = court.getInvaders();

        // test before bounce
        assertEquals(3, originalInvaders[0][0].getVx());
        assertEquals(0, originalInvaders[0][0].getPy());

        // slightly moves all invaders to right so bounce is effective
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                court.setInvaderXPosition(row, col, col + 5);

            }
        }

        // moves last column to right-sided wall so bounce can occur
        int col = 9;
        for (int row = 0; row < 5; row++) {
            court.setInvaderXPosition(row, col, 340);
        }

        // sets entire last column to null
        for (int row = 0; row < 5; row++) {
            court.removeInvader(row, col);
        }

        court.checkBounceWall();

        // verifies that bounce did not occur
        Invader[][] updatedInvaders = court.getInvaders();
        assertEquals(3, updatedInvaders[0][0].getVx());
        assertEquals(0, updatedInvaders[0][0].getPy());
    }

    @Test
    public void testRemoveAttackAfterHittingPlayer() {
        JLabel status = new JLabel("Testing...");
        GameCourt court = new GameCourt(status);
        court.reset(true);
        Attack a = new Attack(
                0,
                500,
                -ATTACK_SPEED, COURT_WIDTH, COURT_HEIGHT,
                Color.RED
        );
        court.addInvaderAttack(a);
        List<Attack> attacksBefore = court.getInvaderAttack();

        assertTrue(attacksBefore.contains(a));
        assertEquals(1, attacksBefore.size());
        court.checkPlayerHit(a);
        court.removeAttacks();

        List<Attack> attacksAfter = court.getInvaderAttack();

        assertFalse(attacksAfter.contains(a));
        assertEquals(0, attacksAfter.size());
    }

    @Test
    public void testRemoveInvaderAttackAfterHittingShield() {
        JLabel status = new JLabel("Testing...");
        GameCourt court = new GameCourt(status);
        court.reset(true);
        Attack a = new Attack(
                50,
                400,
                -ATTACK_SPEED, COURT_WIDTH, COURT_HEIGHT,
                Color.RED
        );
        court.addInvaderAttack(a);
        List<Attack> attacksBefore = court.getInvaderAttack();

        assertTrue(attacksBefore.contains(a));
        assertEquals(1, attacksBefore.size());
        court.checkShieldDamaged(a);
        court.removeAttacks();

        List<Attack> attacksAfter = court.getInvaderAttack();

        assertFalse(attacksAfter.contains(a));
        assertEquals(0, attacksAfter.size());
    }

    @Test
    public void testRemovePlayerAttackAfterHittingShield() {
        JLabel status = new JLabel("Testing...");
        GameCourt court = new GameCourt(status);
        court.reset(true);
        Attack a = new Attack(
                50,
                400,
                -ATTACK_SPEED, COURT_WIDTH, COURT_HEIGHT,
                Color.BLUE
        );
        court.addPlayerAttack(a);
        List<Attack> attacksBefore = court.getPlayerAttack();

        assertTrue(attacksBefore.contains(a));
        assertEquals(1, attacksBefore.size());
        court.checkShieldDamaged(a);
        court.removeAttacks();

        List<Attack> attacksAfter = court.getPlayerAttack();

        assertFalse(attacksAfter.contains(a));
        assertEquals(0, attacksAfter.size());
    }

    @Test
    public void testRemoveAttackAfterHittingInvader() {
        JLabel status = new JLabel("Testing...");
        GameCourt court = new GameCourt(status);
        court.reset(true);
        Attack a = new Attack(
                0,
                0,
                -ATTACK_SPEED, COURT_WIDTH, COURT_HEIGHT,
                Color.BLUE
        );
        court.addPlayerAttack(a);
        List<Attack> attacksBefore = court.getPlayerAttack();

        assertTrue(attacksBefore.contains(a));
        assertEquals(1, attacksBefore.size());
        court.checkInvaderDestroyed(a);
        court.removeAttacks();

        List<Attack> attacksAfter = court.getPlayerAttack();

        assertFalse(attacksAfter.contains(a));
        assertEquals(0, attacksAfter.size());
    }

    @Test
    public void testCheckShieldDamagedHealthDecreases() {
        JLabel status = new JLabel("Testing...");
        GameCourt court = new GameCourt(status);
        court.reset(true);
        Attack a = new Attack(
                50,
                400,
                -ATTACK_SPEED, COURT_WIDTH, COURT_HEIGHT,
                Color.BLUE
        );
        // check health before
        assertEquals(5, court.getShield(0).getHealth());
        court.checkShieldDamaged(a);
        // check health after
        assertEquals(4, court.getShield(0).getHealth());
    }
}
