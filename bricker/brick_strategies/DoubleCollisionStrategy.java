package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

import java.util.Random;

/**
 * This class implements the double strategy: applies a base collision strategy and additionally applies
 * up to two random special collision behaviors, Class implements CollisionStrategy interface, as part of the
 * optional collision strategies in the game, in addition to the basic behavior it
 * chooses tow more in the predefined limit.
 */
public class DoubleCollisionStrategy implements CollisionStrategy {
    private static final int DOUBLE_BEHAVIORS = 2;
    private static final int MAX_SPECIAL_BEHAVIORS = 3;
    private static final int MAGIC_10 = 10;
    private static final int EXTRA_BALL_STRATEGY = 5;
    private static final int EXTRA_PADDLE_STRATEGY = 6;
    private static final int TURBO_STRATEGY = 7;
    private static final int EXTRA_HEART_STRATEGY = 8;
    private static final int DOUBLE_STRATEGY = 9;
    private final BrickerGameManager brickerGameManager;
    private CollisionStrategy collisionStrategy;
    private final Random random;
    private int numOfCollisionStrategies = 0;

    /**
     * Constructor for DoubleCollisionStrategy.
     * @param brickerGameManager Instance of BrickerGameManager.
     * @param collisionStrategy  CollisionStrategy to be decorated with extra behavior.
     */
    public DoubleCollisionStrategy(BrickerGameManager brickerGameManager,
                                   CollisionStrategy collisionStrategy) {
        this.brickerGameManager = brickerGameManager;
        this.collisionStrategy = collisionStrategy;
        this.random = new Random();
    }
    /**
     * Handles Collision between the brick and ball.
     * Applies the base collision strategy and up to two additional random special
     * collision behaviors. The total number of special behaviors is capped at
     * MAX_SPECIAL_BEHAVIORS-3 to prevent excessive behavior application.
     * @param brick Brick object.
     * @param ball  Ball object.
     */
    @Override
    public void onCollision(GameObject brick, GameObject ball) {
        //apply the base collision strategy
        collisionStrategy.onCollision(brick, ball);
        for (int i = 0; i < DOUBLE_BEHAVIORS; i++) {
            if (numOfCollisionStrategies >= MAX_SPECIAL_BEHAVIORS) return;
            int rand = random.nextInt(MAGIC_10); //generate random value in the range [0-9]
            CollisionStrategy strategy = null;
            switch (rand) {
                case EXTRA_BALL_STRATEGY: strategy =
                        new ExtraBallStrategy(brickerGameManager, collisionStrategy);
                break;
                case EXTRA_PADDLE_STRATEGY: strategy =
                        new ExtraPaddleCollision(brickerGameManager, collisionStrategy);
                break;
                case TURBO_STRATEGY: strategy =
                        new TurboCollision(brickerGameManager, collisionStrategy);
                break;
                case EXTRA_HEART_STRATEGY: strategy =
                        new ExtraHeartReturnStrategy(brickerGameManager, collisionStrategy);
                break;
                case DOUBLE_STRATEGY:
                    if (numOfCollisionStrategies < MAX_SPECIAL_BEHAVIORS - 1) { //
                        //prevent recursion overflow
                        strategy = new
                                DoubleCollisionStrategy(brickerGameManager, collisionStrategy);
                    }
                    break;
            }
            if (strategy != null) {
                strategy.onCollision(brick, ball);
                numOfCollisionStrategies++;
            }
        }
    }
}