import javax.swing.JFrame;

/**
 * GameController.java
 * This class acts as the controller for the Space Invaders game.
 * It contains the main method, sets up the GUI, and wires the model and view together.
 * It handles user input and updates the game loop.
 */
public class GameController {
    // Placeholder for model and view references
    private GameModel model;
    private GameView view;

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

    // Method to set up the GUI
    private void setupGUI() {
        JFrame frame = new JFrame("Space Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(view);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}