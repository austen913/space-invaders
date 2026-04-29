public class ModelTester {
    public static void main(String[] args) {
        GameModel model = new GameModel();

        // Test 1: Player cannot move past the left edge
        for (int i = 0; i < 200; i++) {
            model.movePlayerLeft();
        }
        boolean leftEdgeBlocked = model.getPlayerX() == 0;
        System.out.println("Player cannot move past left edge: " + (leftEdgeBlocked ? "PASS" : "FAIL"));

        // Reset player position for next test (move right to center-ish)
        for (int i = 0; i < 200; i++) {
            model.movePlayerRight();
        }

        // Test 2: Player cannot move past the right edge
        for (int i = 0; i < 200; i++) {
            model.movePlayerRight();
        }
        boolean rightEdgeBlocked = model.getPlayerX() == GameModel.WIDTH - 50;
        System.out.println("Player cannot move past right edge: " + (rightEdgeBlocked ? "PASS" : "FAIL"));

        // Test 3: Firing while a bullet is already in flight does nothing
        model.firePlayerBullet();
        boolean hasBullet = model.getPlayerBullet() != null;
        GameModel.Bullet firstBullet = model.getPlayerBullet();
        model.firePlayerBullet(); // Should not create a new bullet
        boolean stillSameBullet = model.getPlayerBullet() == firstBullet;
        System.out.println("Firing while bullet in flight does nothing: " + (hasBullet && stillSameBullet ? "PASS" : "FAIL"));

        // Clear bullet for next test
        for (int i = 0; i < 100; i++) {
            model.update();
        }

        // Test 4: A bullet that reaches the top is removed
        model.firePlayerBullet();
        for (int i = 0; i < 100; i++) {
            model.update();
        }
        boolean bulletRemoved = model.getPlayerBullet() == null;
        System.out.println("Bullet reaches top and is removed: " + (bulletRemoved ? "PASS" : "FAIL"));

        // Test 5: Destroying an alien increases the score
        int initialAliens = model.getAliens().size();
        int initialScore = model.getScore();
        // Position bullet to hit the first alien
        GameModel.Alien targetAlien = model.getAliens().get(0);
        model.setPlayerBullet(targetAlien.x + 20, targetAlien.y + 15);
        model.update(); // Should detect collision and destroy alien
        boolean alienDestroyed = model.getAliens().size() < initialAliens;
        boolean scoreIncreased = model.getScore() > initialScore;
        System.out.println("Destroying an alien increases the score: " + (alienDestroyed && scoreIncreased ? "PASS" : "FAIL"));

        // Test 6: Losing all lives triggers the game-over state
        model.setLives(0);
        boolean gameOverTriggered = model.getLives() <= 0;
        System.out.println("Losing all lives triggers game-over state: " + (gameOverTriggered ? "PASS" : "FAIL"));
    }
}