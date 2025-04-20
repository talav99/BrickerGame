package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * This class represents a user paddle object in the game, controlled by player,
 * with horizontal movement.
 */
public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 300.0F;
    private static final int MAGIC_37 = 37;
    private static final int MAGIC_39 = 39;
    private static final String EXTRA_PUDDLE = "extraPaddle";
    private final UserInputListener inputListener;
    private final Renderable renderable;
    private final float windowWidth;
    private boolean amIExtraPaddle;
    private int ExtraHitcount = 0;

    /**
     * Constructor for paddle instance.
     *
     * @param topLeftCorner Top-left corner position (0,0).
     * @param dimensions    Size of game window.
     * @param renderable    Display representation of the paddle.
     * @param inputListener Instance of SoundReader to get user's input.
     * @param windowWidth   Width of the game window.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, float windowWidth) {
        super(topLeftCorner, dimensions, renderable);
        this.renderable = renderable;
        this.inputListener = inputListener;
        this.windowWidth = windowWidth;
    }

    public int getExtraHitcount() {
        return ExtraHitcount;
    }

    /**
     * Game update logic Called once per frame, Overriding the GameManager update() method.
     * updates the paddle position in horizontal movement based on user input and
     * ensures it stays within the game window.
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame).
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        // check if this paddle is an extra paddle
        if (this.getTag().equals(EXTRA_PUDDLE)){
            amIExtraPaddle = true;
        }
        // handle left arrow key input
        if (this.inputListener.isKeyPressed(MAGIC_37)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        // handle right arrow key input
        if (this.inputListener.isKeyPressed(MAGIC_39)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        // set paddle velocity based on movement direction
        this.setVelocity(movementDir.mult(MOVEMENT_SPEED));
        // calculate  next position and check if it exceeds the window bounds
        Vector2 nextPosition = this.getTopLeftCorner().add(this.getVelocity().mult(deltaTime));
        if (nextPosition.x() <= 0.0F || nextPosition.x() + this.getDimensions().x() >= this.windowWidth) {
            this.setVelocity(new Vector2(-this.getVelocity().x(), this.getVelocity().y()));
        }
    }

    /**
     * Method is called when a collision occurs, if the ball collides with the
     * user paddle, increment the hit count.
     *
     * @param other     Other game object.
     * @param collision The collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        if (amIExtraPaddle) {
            ExtraHitcount++;
        }
    }
}

