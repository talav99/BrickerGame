package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import bricker.gameobjects.Ball;
import danogl.GameObject;

/**
 * This class implements turbo mode: when game ball hits brick,
 * the game ball turns red, and it's velocity is multiplied by constant factor.
 * Class implements CollisionStrategy interface, as part of the
 * optional collision strategies in the game, in addition to the basic behavior,
 * there's an additional strategy.
 */
public class TurboCollision implements CollisionStrategy {
    private static final String BRICK_NAMETAG = "brick";
    private static final float VELOCITY_FACTOR = 1.4F;
    private static final int RESET_THRESHOLD = 3;
    private final BrickerGameManager brickerGameManager;
    private CollisionStrategy collisionStrategy;

    /**
     * Constructor for TurboCollision.
     * @param brickerGameManager Instance of BrickerGameManager.
     * @param collisionStrategy  CollisionStrategy to expand with extra behavior.
     *                           (in addition to it's basic one).
     */
    public TurboCollision(BrickerGameManager brickerGameManager, CollisionStrategy collisionStrategy) {
        this.brickerGameManager = brickerGameManager;
        this.collisionStrategy = collisionStrategy;
    }
    /**
     * Handles Collision between the brick and another game object.
     * In addition to the brick is removed from the game, ball game enters turbo mode.
     * @param object1 Brick object.
     * @param object2 Other game object.
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        this.collisionStrategy.onCollision(object1, object2);
        if (object2.getTag().equals("ball") && object1.getTag().equals(BRICK_NAMETAG) &&
                !((Ball)object2).isTurboBall()) {
            Ball ball = (Ball)object2;
            brickerGameManager.turboUpdate(ball);
        }
    }
}
