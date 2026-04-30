import java.io.*;
import java.util.*;

/**
 * GameModel.java
 * This class represents the model for the Space Invaders game.
 * It contains the game state, including positions of invaders, player, bullets, etc.
 * It handles game logic such as movement, collision detection, and scoring.
 * No Swing imports are allowed in this class.
 */
public class GameModel {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int PLAYER_SPEED = 5;
    public static final int BULLET_SPEED = 10;
    public static final int ALIEN_SPEED = 2;
    public static final int ALIEN_DOWN = 20;
    public static final int ALIEN_ROWS = 5;
    public static final int ALIEN_COLS = 11;
    public static final int ALIEN_SPACING_X = 40;
    public static final int ALIEN_SPACING_Y = 30;
    public static final int PLAYER_Y = HEIGHT - 50;
    public static final int ALIEN_START_Y = 50;
    public static final String HIGH_SCORE_FILE = "highscore.txt";

    private static int highScore = loadHighScore();

    private int playerX = WIDTH / 2;
    private List<Alien> aliens = new ArrayList<>();
    private List<Bullet> playerBullets = new ArrayList<>();
    private List<Bullet> alienBullets = new ArrayList<>();
    private int bulletCount = 1;
    private int score = 0;
    private int lives = 3;
    private int alienDirection = 1; // 1 right, -1 left
    private Random random = new Random();

    public GameModel() {
        initializeAliens();
    }

    private void initializeAliens() {
        for (int row = 0; row < ALIEN_ROWS; row++) {
            for (int col = 0; col < ALIEN_COLS; col++) {
                int x = col * ALIEN_SPACING_X + 50;
                int y = ALIEN_START_Y + row * ALIEN_SPACING_Y;
                AlienShape shape = AlienShape.values()[random.nextInt(AlienShape.values().length)];
                aliens.add(new Alien(x, y, shape, false));
            }
        }

        // Randomly mark three distinct aliens as blue
        Set<Integer> blueIndexes = new HashSet<>();
        while (blueIndexes.size() < 3 && blueIndexes.size() < aliens.size()) {
            blueIndexes.add(random.nextInt(aliens.size()));
        }
        for (int index : blueIndexes) {
            aliens.get(index).blue = true;
        }
    }

    public void movePlayerLeft() {
        playerX -= PLAYER_SPEED;
        if (playerX < 0) playerX = 0;
    }

    public void movePlayerRight() {
        playerX += PLAYER_SPEED;
        if (playerX > WIDTH - 50) playerX = WIDTH - 50;
    }

    public void firePlayerBullet() {
        if (!playerBullets.isEmpty()) {
            return;
        }
        int spacing = 30;
        for (int i = 0; i < bulletCount; i++) {
            int offsetX = (bulletCount - 1) * spacing / 2 - i * spacing;
            playerBullets.add(new Bullet(playerX + 25 + offsetX, PLAYER_Y));
        }
    }

    public void update() {
        updatePlayerBullet();
        updateAliens();
        fireAlienBullet();
        updateAlienBullets();
        checkCollisions();
    }

    private void updatePlayerBullet() {
        for (Iterator<Bullet> it = playerBullets.iterator(); it.hasNext();) {
            Bullet b = it.next();
            b.y -= BULLET_SPEED;
            if (b.y < 0) {
                it.remove();
            }
        }
    }

    private void updateAliens() {
        boolean hitEdge = false;
        for (Alien a : aliens) {
            if (alienDirection == 1 && a.x >= WIDTH - 40) {
                hitEdge = true;
                break;
            } else if (alienDirection == -1 && a.x <= 0) {
                hitEdge = true;
                break;
            }
        }
        if (hitEdge) {
            alienDirection = -alienDirection;
            for (Alien a : aliens) {
                a.y += ALIEN_DOWN;
            }
        } else {
            for (Alien a : aliens) {
                a.x += alienDirection * ALIEN_SPEED;
            }
        }
    }

    private void fireAlienBullet() {
        if (random.nextInt(100) < 2 && !aliens.isEmpty()) {
            Alien shooter = aliens.get(random.nextInt(aliens.size()));
            alienBullets.add(new Bullet(shooter.x + 20, shooter.y + 30));
        }
    }

    private void updateAlienBullets() {
        for (Iterator<Bullet> it = alienBullets.iterator(); it.hasNext();) {
            Bullet b = it.next();
            b.y += BULLET_SPEED;
            if (b.y > HEIGHT) {
                it.remove();
            }
        }
    }

    private void checkCollisions() {
        // Player bullets vs aliens
        for (Iterator<Bullet> bulletIt = playerBullets.iterator(); bulletIt.hasNext();) {
            Bullet b = bulletIt.next();
            for (Iterator<Alien> alienIt = aliens.iterator(); alienIt.hasNext();) {
                Alien a = alienIt.next();
                if (collides(b, a)) {
                    bulletIt.remove();
                    boolean wasBlue = a.blue;
                    alienIt.remove();
                    score += 10;
                    updateHighScore();
                    if (wasBlue) {
                        bulletCount++;
                    }
                    break;
                }
            }
        }
        // Alien bullets vs player
        for (Iterator<Bullet> it = alienBullets.iterator(); it.hasNext();) {
            Bullet b = it.next();
            if (collides(b, playerX, PLAYER_Y, 50, 20)) {
                it.remove();
                lives--;
                if (bulletCount > 1) {
                    bulletCount--;
                }
                break;
            }
        }
    }

    private boolean collides(Bullet b, Alien a) {
        return b.x >= a.x && b.x <= a.x + 40 && b.y >= a.y && b.y <= a.y + 30;
    }

    private boolean collides(Bullet b, int px, int py, int pw, int ph) {
        return b.x >= px && b.x <= px + pw && b.y >= py && b.y <= py + ph;
    }

    // Getters for the view
    public int getPlayerX() { return playerX; }
    public List<Alien> getAliens() { return aliens; }
    public List<Bullet> getPlayerBullets() { return playerBullets; }
    public List<Bullet> getAlienBullets() { return alienBullets; }
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public int getBulletCount() { return bulletCount; }
    public int getHighScore() { return highScore; }

    enum AlienShape {
        SQUARE,
        CIRCLE,
        TRIANGLE
    }

    static class Alien {
        int x, y;
        AlienShape shape;
        boolean blue;
        Alien(int x, int y, AlienShape shape, boolean blue) {
            this.x = x;
            this.y = y;
            this.shape = shape;
            this.blue = blue;
        }
    }

    static class Bullet {
        int x, y;
        Bullet(int x, int y) { this.x = x; this.y = y; }
    }

    // For testing purposes
    public void setPlayerBullet(int x, int y) {
        playerBullets.add(new Bullet(x, y));
    }

    public void setAlienPosition(int index, int x, int y) {
        if (index >= 0 && index < aliens.size()) {
            aliens.get(index).x = x;
            aliens.get(index).y = y;
        }
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    private static int loadHighScore() {
        File file = new File(HIGH_SCORE_FILE);
        if (!file.exists()) {
            return 0;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            return line == null ? 0 : Integer.parseInt(line.trim());
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    private static void saveHighScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORE_FILE))) {
            writer.write(Integer.toString(highScore));
        } catch (IOException ignored) {
        }
    }

    private void updateHighScore() {
        if (score > highScore) {
            highScore = score;
            saveHighScore();
        }
    }
}