import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

/**
 * GameView.java
 * This class represents the view for the Space Invaders game.
 * It extends JPanel and handles rendering the game graphics.
 * It draws the player, invaders, bullets, score, etc., based on the model.
 */
public class GameView extends JPanel {
    // Model reference
    private GameModel model;

    // Constructor
    public GameView(GameModel model) {
        this.model = model;
    }

    // Paint the game components
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw player
        g.setColor(Color.GREEN);
        g.fillRect(model.getPlayerX(), GameModel.PLAYER_Y, 50, 20);

        // Draw aliens
        for (GameModel.Alien alien : model.getAliens()) {
            g.setColor(alien.blue ? Color.BLUE : Color.RED);
            switch (alien.shape) {
                case SQUARE:
                    g.fillRect(alien.x, alien.y, 40, 30);
                    break;
                case CIRCLE:
                    g.fillOval(alien.x, alien.y, 40, 30);
                    break;
                case TRIANGLE:
                    int[] xs = {alien.x, alien.x + 20, alien.x + 40};
                    int[] ys = {alien.y + 30, alien.y, alien.y + 30};
                    g.fillPolygon(xs, ys, 3);
                    break;
            }
        }

        // Draw player bullet
        GameModel.Bullet playerBullet = model.getPlayerBullet();
        if (playerBullet != null) {
            g.setColor(Color.YELLOW);
            g.fillRect(playerBullet.x - 2, playerBullet.y, 5, 10);
        }

        // Draw alien bullets
        g.setColor(Color.WHITE);
        for (GameModel.Bullet bullet : model.getAlienBullets()) {
            g.fillRect(bullet.x - 2, bullet.y, 5, 10);
        }

        // Draw score
        g.setColor(Color.WHITE);
        g.drawString("Score: " + model.getScore(), 10, 20);
        g.drawString("High Score: " + model.getHighScore(), 10, 40);

        // Draw lives
        g.drawString("Lives: " + model.getLives(), getWidth() - 100, 20);

        // Draw game over message if game ended
        if (model.getLives() <= 0 || model.getAliens().isEmpty()) {
            g.setColor(Color.RED);
            String message = model.getLives() <= 0 ? "Game Over" : "You Win!";
            int x = getWidth() / 2 - g.getFontMetrics().stringWidth(message) / 2;
            int y = getHeight() / 2;
            g.drawString(message, x, y);
        }
    }
}