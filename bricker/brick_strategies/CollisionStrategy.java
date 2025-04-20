package bricker.brick_strategies;

import danogl.GameObject;

/**
 * This interface defines different ways to handle collisions.
 * Classes implementing this interface creates custom collision strategies.
 */
public interface CollisionStrategy {
    /**
     * Represents the strategy to implement when there is a collision.
     * @param object1 First game object.
     * @param object2 Second game object.
     */
    void onCollision(GameObject object1, GameObject object2);
}
