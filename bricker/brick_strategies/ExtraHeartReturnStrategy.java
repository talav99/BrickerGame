package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * this class implements the extra heart strategy: when game ball hits brick,
 * a heart object representing additional life is created, and player can
 * gain it (up to maximum number). Class implements CollisionStrategy interface, as part of the
 * optional collision strategies in the game, in addition to the basic behavior,
 * there's an additional strategy.
 */
public class ExtraHeartReturnStrategy implements CollisionStrategy{
    private final BrickerGameManager brickerGameManager;
    private final CollisionStrategy collisionStrategy;

    /**
     * Constructor for ExtraHeartReturnStrategy.
     * @param brickerGameManager Instance of BrickerGameManager.
     * @param collisionStrategy  CollisionStrategy to expand with extra behavior.
     *                           (in addition to it's basic one).
     */
    public ExtraHeartReturnStrategy(BrickerGameManager brickerGameManager,
                                    CollisionStrategy collisionStrategy) {
        this.brickerGameManager = brickerGameManager;
        this.collisionStrategy = collisionStrategy;
    }

    /**
     * Handles Collision between the brick and another game object.
     * In addition to the brick is removed from the game, heart object representing
     * additional life is being created.
     * @param object1 Brick object.
     * @param object2 Other game object.
     */
    @Override
    public void onCollision(GameObject object1, GameObject object2) {
        this.collisionStrategy.onCollision(object1, object2);
        this.brickerGameManager.createExtraHeart(object1.getCenter());
    }
}
