package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;

/**
 * This class implements extra paddle strategy: when game ball hits brick,
 * an extra paddle object is created. Class implements CollisionStrategy interface, as part of the
 *  * optional collision strategies in the game, in addition to the basic behavior,
 *  * there's an additional strategy.
 */
public class ExtraPaddleCollision implements CollisionStrategy {
    private final BrickerGameManager gameManager;

    /**
     * Constructor for ExtraPaddleCollision.
     * @param brickerGameManager Instance of BrickerGameManager.
     * @param collisionStrategy  CollisionStrategy to expand with extra behavior
     *                          (in addition to it's basic one).
     */
    public ExtraPaddleCollision(BrickerGameManager brickerGameManager,
                                CollisionStrategy collisionStrategy) {
        this.gameManager = brickerGameManager;
    }
    /**
     * Handles Collision between the brick and another game object.
     * In addition to the brick is removed from the game, extra paddle is being created.
     * @param object1 Brick object.
     * @param object2 Other game object.
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        gameManager.createExtraPaddle();
        gameManager.deleteGameObject(object1, Layer.DEFAULT);
    }
}
