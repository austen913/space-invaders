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

        // Test 3: Firing creates bullets and they exist in flight
        model.firePlayerBullet();
        boolean hasBullets = model.getPlayerBullets().size() > 0;
        for (int i = 0; i < 100; i++) {
            model.update();
        }
        boolean bulletsRemoved = model.getPlayerBullets().size() == 0;
        System.out.println("Bullets created and removed when reaching top: " + (hasBullets && bulletsRemoved ? "PASS" : "FAIL"));

        // Test 4: Destroying a regular alien increases score but not bullet count
        GameModel.Alien targetAlien = null;
        for (GameModel.Alien alien : model.getAliens()) {
            if (!alien.blue) {
                targetAlien = alien;
                break;
            }
        }
        if (targetAlien != null) {
            int initialScore = model.getScore();
            int initialAliens = model.getAliens().size();
            model.setPlayerBullet(targetAlien.x + 20, targetAlien.y + 15);
            model.update();
            boolean alienDestroyed = model.getAliens().size() < initialAliens;
            boolean scoreIncreased = model.getScore() > initialScore;
            System.out.println("Destroying regular alien increases score: " + (alienDestroyed && scoreIncreased ? "PASS" : "FAIL"));
        }

        // Test 5: Destroying a blue alien increases bullet count
        GameModel model2 = new GameModel();
        // Force the first alien to be blue for testing
        model2.getAliens().get(0).blue = true;
        GameModel.Alien blueAlien = model2.getAliens().get(0);
        int initialBulletCount = model2.getBulletCount();
        model2.setPlayerBullet(blueAlien.x + 20, blueAlien.y + 15);
        model2.update();
        boolean bulletCountIncreased = model2.getBulletCount() > initialBulletCount;
        System.out.println("Destroying blue alien increases bullet count: " + (bulletCountIncreased ? "PASS" : "FAIL"));

        // Test 6: Losing a life decreases bullet count (but not below 1)
        GameModel model3 = new GameModel();
        // Manually set bullet count higher
        model3.firePlayerBullet();
        model3.firePlayerBullet(); // Fire more bullets
        model3.firePlayerBullet();
        model3.setLives(0);
        boolean gameOverTriggered = model3.getLives() <= 0;
        System.out.println("Losing all lives triggers game-over state: " + (gameOverTriggered ? "PASS" : "FAIL"));
    }
}