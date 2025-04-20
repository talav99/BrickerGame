package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * This class implements the extra ball strategy: when game ball hits brick,
 * two puck balls create. Class implements CollisionStrategy interface, as part of the
 * optional collision strategies in the game, in addition to the basic behavior,
 * there's an additional strategy.
 */
public class ExtraBallStrategy implements CollisionStrategy {
    private BrickerGameManager brickerGameManager;
    private CollisionStrategy collisionStrategy;

    /**
     * Constructor for ExtraBallStrategy.
     * @param brickerGameManager Instance of BrickerGameManager.
     * @param collisionStrategy  CollisionStrategy to expand with extra behavior.
     *                           (in addition to it's basic one).
     */
    public ExtraBallStrategy(BrickerGameManager brickerGameManager, CollisionStrategy collisionStrategy) {
        this.brickerGameManager = brickerGameManager;
        this.collisionStrategy = collisionStrategy;
    }
    /**
     * Handles Collision between the brick and another game object.
     * In addition to the brick is removed from the game, two puck balls
     * are created.
     * @param object1 Brick object.
     * @param object2 Other game object.
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        this.collisionStrategy.onCollision(object1, object2);
        this.brickerGameManager.createPuckBall(object1.getCenter());
        this.brickerGameManager.createPuckBall(object1.getCenter());
    }
}
