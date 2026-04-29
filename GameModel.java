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

    private int playerX = WIDTH / 2;
    private List<Alien> aliens = new ArrayList<>();
    private Bullet playerBullet = null;
    private List<Bullet> alienBullets = new ArrayList<>();
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
                aliens.add(new Alien(x, y));
            }
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
        if (playerBullet == null) {
            playerBullet = new Bullet(playerX + 25, PLAYER_Y);
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
        if (playerBullet != null) {
            playerBullet.y -= BULLET_SPEED;
            if (playerBullet.y < 0) {
                playerBullet = null;
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
        // Player bullet vs aliens
        if (playerBullet != null) {
            for (Iterator<Alien> it = aliens.iterator(); it.hasNext();) {
                Alien a = it.next();
                if (collides(playerBullet, a)) {
                    it.remove();
                    playerBullet = null;
                    score += 10;
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
    public Bullet getPlayerBullet() { return playerBullet; }
    public List<Bullet> getAlienBullets() { return alienBullets; }
    public int getScore() { return score; }
    public int getLives() { return lives; }

    static class Alien {
        int x, y;
        Alien(int x, int y) { this.x = x; this.y = y; }
    }

    static class Bullet {
        int x, y;
        Bullet(int x, int y) { this.x = x; this.y = y; }
    }
}