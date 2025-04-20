package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * This class represents puck ball object in the game, special ball that extends ball behaviors.
 */
public class PuckBall extends Ball {
    private BrickerGameManager brickerGameManager;

    /**
     * Constructor for puck ball instance.
     * @param topLeftCorner Top-left corner position (0,0).
     * @param dimensions    Size of game window.
     * @param renderable    Display representation of puck ball.
     */
    public PuckBall(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable
            , Sound collisionsound, BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable, collisionsound,brickerGameManager);
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Game update logic Called once per frame, Overriding the GameManager update() method.
     * checks id the puck ball out of windows bounds.
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (brickerGameManager.isBallOutOfWindow(this)) {
            //game manager handles further logic
        }
    }
}
