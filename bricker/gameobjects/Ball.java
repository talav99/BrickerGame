package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * This class represents ball in Bricker game.
 * Ball object responsible for collision with the bricks.
 */
public class Ball extends GameObject {
    private static final String HEART_NAME_TAG = "heart";
    private static final String BALL_NAME_TAG = "ball";
    private Sound collisionsound;
    private static final float VELOCITY_FACTOR = 1.4f;
    private int collisionCounter = 0;
    private Renderable originalRenderable;
    private Vector2 originalVelocity;
    private boolean amITurboBall = false;
    private BrickerGameManager brickerGameManager;

    /**
     * Constructor for a new Ball instance.
     * @param topLeftCorner      Top-left corner position (0,0).
     * @param dimensions         Size of game window.
     * @param renderable         Display representation of the ball.
     * @param collisionsound     Sound when the ball collides with another object.
     * @param brickerGameManager Instance of BrickerGameManager.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable
            , Sound collisionsound, BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionsound = collisionsound;
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Getter for number of collisions.
     * @return Current collisions counter.
     */
    public int getCollisionCounter(){
        return this.collisionCounter;
    }


    /**
     * Method is called when a collision occurs, it changes the velocity of
     * the ball and plays a sound.
     * @param other     Other game object.
     * @param collision The collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVelocity = getVelocity().flipped(collision.getNormal());
        setVelocity(newVelocity);
        collisionsound.play();
        // handles turbo mode in a different way
        if (this.amITurboBall && !other.getTag().equals(HEART_NAME_TAG)) {
            increaseCollisionCounter();
            if (collisionCounter >= 6) {
                resetTurboBall();
            }
        }
    }

    /**
     * Increments the collision counter.
     */
    public void increaseCollisionCounter() {
        this.collisionCounter++;
    }

    /**
     * Sets the ball's original appearance.
     * @param renderable Original renderable.
     */
    public void setOriginalRenderable(Renderable renderable) {
        this.originalRenderable = renderable;
    }

    /**
     * Sets the ball's original velocity.
     * @param velocity The original velocity.
     */
    public void setOriginalVelocity(Vector2 velocity) {
        this.originalVelocity = velocity;
    }

    /**
     * Retrieves the ball's original velocity.
     * @return The original velocity.
     */
    public Vector2 getOriginalVelocity() {
        return this.originalVelocity;
    }

    /**
     * Activates turbo mode for the ball.
     */
    public void setTurboBall() {
        this.amITurboBall = true;
    }

    /**
     * Checks if the ball is currently in turbo mode.
     * @return True if the ball is in turbo mode, otherwise false.
     */
    public boolean isTurboBall() {
        return amITurboBall;
    }

    /**
     * Resets the ball from turbo mode to its original state.
     */
    public void resetTurboBall() {
        this.collisionCounter = 0;
        this.amITurboBall = false;
        renderer().setRenderable(originalRenderable);
        setVelocity(this.getVelocity().mult((1 / VELOCITY_FACTOR)));
        this.setTag(BALL_NAME_TAG);
    }
}