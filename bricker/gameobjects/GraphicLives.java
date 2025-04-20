package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * This class represents a graphical display of lives as red heart icons.
 */
public class GraphicLives extends GameObject {
    private Renderable heartImage;
    private final LivesManager livesManager;
    private BrickerGameManager brickerGameManager;
    private GameObject[] lives;

    /**
     * Constructor for a new graphic live instance.
     * @param livesManager      Manages player's lives.
     * @param topLeftCorner     Top-left corner position of heart display (0,0).
     * @param dimensions        Size of game window.
     * @param heartImage        Display representation of heart.
     * @param brickerGameManager Instance of BrickerGameManager.
     */
    public GraphicLives(LivesManager livesManager, Vector2 topLeftCorner,
                        Vector2 dimensions, Renderable heartImage, BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, heartImage);
        this.livesManager = livesManager;
        this.brickerGameManager = brickerGameManager;
        this.heartImage = heartImage;
        lives = new GameObject[livesManager.getMaxLives()];
        for (int i = 0; i < livesManager.getDefaultLives(); i++) {
            Vector2 position = topLeftCorner.add(new Vector2(i * (dimensions.x() + 5), 0));
            GameObject heart = new GameObject(position, dimensions, heartImage);
            lives[i] = heart;
            brickerGameManager.addOutsideGameObject(heart, Layer.BACKGROUND);
        }
    }
    /**
     * Updates the graphic heart display based on current lives and removes heart
     * objects when the player loses lives.
     */
    public void updateDisplayGraphicHerts() {
        int currentLives = livesManager.getCurLives();
        int maxLives = livesManager.getMaxLives();
        //remove hearts for lost lives
        for (int i = currentLives; i < lives.length; i++) {
            if (lives[i] != null) {
                brickerGameManager.deleteGameObject(lives[i], Layer.BACKGROUND);
                lives[i] = null;
            }
        }
        //add hearts for gained lives
        for (int i = 0; i < currentLives; i++) {
            if (lives[i] == null) {
                Vector2 position = getTopLeftCorner().add(new Vector2(i * (getDimensions().x() + 5), 0));
                GameObject heart = new GameObject(position, getDimensions(), heartImage);
                lives[i] = heart;
                brickerGameManager.addOutsideGameObject(heart, Layer.BACKGROUND);
            }
        }
    }
    /**
     * Game update logic Called once per frame, Overriding the GameManager update() method.
     * it updates the graphic counter to be the current number of lives.
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateDisplayGraphicHerts();
    }
}

