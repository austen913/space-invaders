import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.event.*;

/**
 * GameController.java
 * This class acts as the controller for the Space Invaders game.
 * It contains the main method, sets up the GUI, and wires the model and view together.
 * It handles user input and updates the game loop.
 */
public class GameController {
    // Model and view references
    private GameModel model;
    private GameView view;
    private Timer gameTimer;

    // Constructor
    public GameController() {
        model = new GameModel();
        view = new GameView(model);
    }

    // Main method
    public static void main(String[] args) {
        GameController controller = new GameController();
        controller.setupGUI();
    }

    // Method to set up the GUI and start the game
    private void setupGUI() {
        JFrame frame = new JFrame("Space Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(view);
        frame.setSize(800, 600);
        frame.setFocusable(true);
        frame.requestFocus();

        // Add key listener for player controls
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_LEFT) {
                    model.movePlayerLeft();
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    model.movePlayerRight();
                } else if (keyCode == KeyEvent.VK_SPACE) {
                    model.firePlayerBullet();
                }
            }
        });

        frame.setVisible(true);

        // Start the game loop
        gameTimer = new Timer(50, e -> {
            model.update();
            view.repaint();
            if (isGameOver()) {
                gameTimer.stop();
            }
        });
        gameTimer.start();
    }

    // Check if the game is over
    private boolean isGameOver() {
        return model.getLives() <= 0;
    }
}