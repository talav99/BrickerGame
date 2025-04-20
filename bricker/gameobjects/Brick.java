package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * This class represents brick object in Bricker game.
 * The brick interacts with instances of ball objects through collisions, once colliding,
 * collision strategy is being created.
 */
public class Brick extends GameObject {
    private CollisionStrategy collisionStrategy;
    BrickerGameManager brickergameManager;

    /**
     * Constructor for a new Brick instance.
     * @param topLeftCorner      Top-left corner position (0,0).
     * @param dimensions         Size of game window.
     * @param renderable         Display representation of the ball.
     * @param collisionStrategy A collision strategy to execute when hit.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions,
                 Renderable renderable, CollisionStrategy collisionStrategy,
                 BrickerGameManager brickergameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy =  collisionStrategy;
        this.brickergameManager = brickergameManager;
    }

    /**
     * Method is called when a collision occurs, executes the assigned collision strategy
     * @param other     Other game object.
     * @param collision The collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        brickergameManager.decrementBrickCounter();
                collisionStrategy.onCollision(this, other);
    }
}