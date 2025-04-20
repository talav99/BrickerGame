package bricker.brick_strategies;

import danogl.GameObject;
import bricker.main.BrickerGameManager;
import danogl.collisions.Layer;

/**
 * This class implements basic collision strategy for bricks in the Bricker game so
 * that the brick is removed from the game, and brick counter
 * is decremented. it implements CollisionStrategy interface
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    private BrickerGameManager brickergameManager;

    /**
     * Constructor for BasicCollisionStrategy.
     * @param brickergameManager Instance of BrickerGameManager.
     */
    public BasicCollisionStrategy(BrickerGameManager brickergameManager) {
        this.brickergameManager = brickergameManager;
    }

    /**
     * Handles Collision between the brick and another game object.
     * The brick is removed from the game, and the brick counter is decremented.
     * @param object1 Brick object.
     * @param object2 Other game object.
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        brickergameManager.deleteGameObject(object1, Layer.STATIC_OBJECTS);
    }
}
