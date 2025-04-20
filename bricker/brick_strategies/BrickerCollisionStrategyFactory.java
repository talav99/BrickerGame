package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import java.util.Random;

/**
 * This class is a factory that creates collision strategies according to the
 * required probabilities (detailed below).
 */
public class BrickerCollisionStrategyFactory {
    private static final int MAGIC_10 = 10;
    private static final int EXTRA_BALL_STRATEGY = 5;
    private static final int EXTRA_PADDLE_STRATEGY = 6;
    private static final int TURBO_STRATEGY = 7;
    private static final int EXTRA_HEART_STRATEGY = 8;
    private static final int DOUBLE_STRATEGY = 9;
    private final BrickerGameManager brickerGameManager;
    private final Random random;

    /**
     * Constructor for BrickerCollisionStrategyFactory.
     * @param brickergameManager Instance of BrickerGameManager.
     */
    public BrickerCollisionStrategyFactory(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
        this.random = new Random();
    }
    /**
     * Creates a collision strategy based on random selection.
     * The method generates a random number [0,10) to decide which special collision strategy,
     * to create, based on the following requirements:
     *  50% Basic behavior
     *  50% Special behaviors-
     *      10% Extra ball
     *      10% Extra paddle
     *      10% Extra heart
     *      10% turbo mode
     *      10% Double strategy (combination of the 2 above)
     * @return A CollisionStrategy instance representing the selected strategy.
     */
    public CollisionStrategy createStrategy() {
        CollisionStrategy basicStrategy = new BasicCollisionStrategy(brickerGameManager);
        int rand = random.nextInt(MAGIC_10);
        return switch (rand) {
            case EXTRA_BALL_STRATEGY -> new ExtraBallStrategy(this.brickerGameManager, basicStrategy);
            case EXTRA_PADDLE_STRATEGY -> new ExtraPaddleCollision(this.brickerGameManager, basicStrategy);
            case TURBO_STRATEGY -> new TurboCollision(this.brickerGameManager, basicStrategy);
            case EXTRA_HEART_STRATEGY -> new ExtraHeartReturnStrategy(this.brickerGameManager, basicStrategy);
            case DOUBLE_STRATEGY -> new DoubleCollisionStrategy(this.brickerGameManager, basicStrategy);
            default -> new BasicCollisionStrategy(this.brickerGameManager);
        };
    }
}