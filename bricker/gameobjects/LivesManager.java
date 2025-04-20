package bricker.gameobjects;


/**
 * This class manages the player's lives in the game, including tracking current and maximum lives,
 * and providing methods to increment, decrement, and check game-over status.
 */
public class LivesManager{
    private final static int MAX_LIVES = 4;
    private int defaultLives = 3;
    private int curLives;
    private int maxLives;

    /**
     * Constructor for LivesManager.
     * @param maxLives Maximum lives number the player can have.
     * @param initialLives Initial number of lives player starts with.
     */
    public LivesManager(int maxLives, int initialLives) {
        this.maxLives = maxLives;
        this.curLives = initialLives;
    }
    /**
     * Getter for current number of lives the player has.
     * @return Current lives number.
     */

    public int getCurLives() {
        return curLives;
    }
    /**
     * Getter for maximum number of lives player has.
     * @return Maximum number of lives.
     */
    public int getMaxLives() {
        return maxLives;
    }

    /**
     * Getter for default number of lives player has.
     * @return Default number of lives.
     */
    public int getDefaultLives() {
        return defaultLives;
    }

    /**
     * Decreases player's current lives by 1, not below 0.
     */
    public void loseLife() {
        if (this.curLives > 0) {
            this.curLives --;
        }
    }

    /**
     * Increases player's current lives by 1, not above maximum lives.
     */
    public void gainLife() {
        if(this.curLives < this.maxLives) {
            this.curLives ++;
        }
    }
    /**
     * Checks whether the game is over, based on the player's lives.
     * @return True if player's current lives are 0 (or less), otherwise false.
     */
    public boolean isGameOver() {
        return curLives <= 0;
    }

    /**
     * Increases maximum number of lives the player can have.
     * Nothing if the maximum lives are already at the limit.
     */
    public void updateMaxLife(){
        if(this.maxLives == 4) {
            return;
        }
        else{
            this.maxLives++;
        }
    }
}