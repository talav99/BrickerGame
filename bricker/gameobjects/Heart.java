package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Objects;

/**
 * This class implements a heart object that represents life.
 * Heart object is collectible object when collided with paddle, and
 * grants the player an extra life.
 */
public class Heart extends GameObject {
    private BrickerGameManager brickerGameManager;
    private LivesManager livesManager;
    private String tag;

    /**
     * Constructor for a new Heart instance.
     * @param topLeftCorner      Top-left corner position (0,0).
     * @param dimensions         Size of game window.
     * @param renderable         Display representation of the ball.
     * @param brickerGameManager Instance of BrickerGameManager.
     * @param tag                Name tag of the paddle that the heart interacts.
     * @param livesManager       Manages player's lives.
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions,
                 Renderable renderable, BrickerGameManager brickerGameManager,
                 String tag, LivesManager livesManager) {
        super(topLeftCorner, dimensions, renderable);
        this.brickerGameManager = brickerGameManager;
        this.livesManager = livesManager;
        this.tag = tag;
    }
    /**
     * Method is called when a collision occurs, if the heart collides with the
     * user paddle, it grants the player an extra life
     * @param other     Other game object.
     * @param collision The collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (Objects.equals(other.getTag(), this.tag)) {
            this.livesManager.gainLife();
            this.livesManager.updateMaxLife();
            this.brickerGameManager.removeHeart(this);
        }
    }

    /**
     * Determines whether the heart should collide with a user game object
     * according to its name tag.
     * @param other Other game object.
     * @return True if the object has the same tag as the user paddle, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return Objects.equals(other.getTag(), this.tag);
    }

    /**
     * Game update logic Called once per frame, Overriding the GameManager update() method.
     * it removes the heart from the game if it's going out of windows bounds.
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (getTopLeftCorner().y() > brickerGameManager.getWindowDimensions().y()) {
            brickerGameManager.removeHeart(this);
        }
    }
}
