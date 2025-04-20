package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * This class represents a numerical display of lives as numbers, text color changes
 * based on the number of lives left.
 */
public class NumericLives extends GameObject {
    private static final String LIVES = "Lives: ";
    private TextRenderable textRenderable;
    private final LivesManager livesManager;

    /**
     * Constructor for NumericLives.
     * @param livesManager Manages the player's lives.
     * @param topLeftCorner Top-left corner position of heart display (0,0).
     * @param dimensions Size of game window.
     * @param textRenderable Text renderable object used for display.
     */
    public NumericLives(LivesManager livesManager, Vector2 topLeftCorner,
                        Vector2 dimensions, TextRenderable textRenderable) {
        super(topLeftCorner, dimensions, textRenderable);
        this.textRenderable = textRenderable;
        this.livesManager = livesManager;
        updateTextAndColor();
    }

    /**
     * Game update logic Called once per frame, Overriding the GameManager update() method.
     * it dynamically updates displayed text and its color to reflect the current number of lives.
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateTextAndColor();
    }

    /**
     * Updates text and color of the lives display based on current lives number.
     * Green: 3 or more lives.
     * Yellow: 2 lives.
     * Red: 1 or fewer lives.
     */
    private void updateTextAndColor() {
        int curLives = livesManager.getCurLives();
        textRenderable.setString(LIVES + curLives);
        if (curLives >= 3) {
            textRenderable.setColor(Color.GREEN);
        } else if (curLives == 2) {
            textRenderable.setColor(Color.YELLOW);
        } else {
            textRenderable.setColor(Color.RED);
        }
    }
}




