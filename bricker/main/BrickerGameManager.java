package bricker.main;

import bricker.brick_strategies.BrickerCollisionStrategyFactory;
import bricker.brick_strategies.CollisionStrategy;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import danogl.collisions.Layer;
import bricker.gameobjects.Paddle;
import java.awt.*;
import java.util.Random;

/**
 * BrickerGameManager implements a "Brick" game. It creates and initializes the game, updating
 * it according to current state.
 */
public class BrickerGameManager extends GameManager {
    // Images & Sound Paths
    private static final String BALL_PATH = "assets/ball.png";
    private static final String PUCK_BALL_PATH = "assets/mockBall.png";
    private static final String BACKGROUND_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String BALL_SOUND_PATH = "assets/blop.wav";
    private static final String PADDLE_PATH = "assets/paddle.png";
    private static final String BRICK_PATH = "assets/brick.png";
    private static final String HEART_PATH = "assets/heart.png";
    private static final String RED_BALL_PATH = "assets/redball.png";

    // Game constants
    private static final int ZERO = 0;
    private static final int WALLS_WIDTH = 10;
    private static final int CEILING_HEIGHT = 10;
    private static final int BRICK_HEIGHT = 20;
    private static final int W_KEY = 87;
    private static final int BALL_VELOCITY = 150;
    private static final int HEART_VELOCITY = 100;
    private static final int EXTRA_PUDDLE_MAX_COLLISIONS = 4;
    private static final int ROWS = 7;
    private static final int COLS = 8;
    private static final int FALLING_HEARTS_ARR_SIZE = 5;
    private static final int PADDLES_NUMBER = 2;
    private static final float BRICK_CONST = 5;
    private static final float VELOCITY_FACTOR = 1.4F;
    private static final int MAX_HEARTS = 4;
    private static final int INITIAL_HEARTS = 3;

    // Objects dimensions and positions
    private static final Vector2 BALL_DIM = new Vector2(20,20);
    private static final Vector2 ORIGINAL_VELOCITY = new Vector2(150, 150);
    private static final Vector2 PUCK_BALL_DIM =  new Vector2(15, 15);
    private static final Vector2 PADDLE_DIM = new Vector2(100, 15);
    private static final Vector2 HEART_DIM = new Vector2(15, 15);
    private static final Vector2 LIVES_TEXT_POSITION = new Vector2(10, 420);
    private static final Vector2 LIVES_TEXT_DIM = new Vector2(40, 30);
    private static final Vector2 WINDOW_DIMENSIONS = new Vector2(700, 500);
    private static final Vector2 HEART_SIZE = new Vector2(30, 30);
    private static final Vector2 HEART_START_POSITION = new Vector2(10, 460);
    private static final Vector2 BALL_START_POSITION = new Vector2(350, 250);

    // Window messages
    private static final String WINDOW_NAME = "Brick";
    private static final String LOSING_MESSSAGE = "You lose! Play again?";
    private static final String WINNIG_MESSAGE = "You win! Play again?";
    private static final String LIVES = "Lives: ";
    // Name tags
    private static final String BALL_TAG = "ball";
    private static final String BRICK_TAG = "brick";
    private static final String EXTRA_PUDDLE_TAG = "extraPaddle";
    private static final String USER_PADDLE_TAG = "userPaddle";
    private static final String LEFT_WALL_TAG = "leftWall";
    private static final String RIGHT_WALL_TAG = "rightWall";
    private static final String HEART_TAG = "heart";
    private static final String PUCK_BALL_TAG = "puckBall";
    private static final String FONT = "Impact";

    // Game Objects as class fields
    private final Vector2 brickDimensions;
    private ImageReader imageReader;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private Ball ball;
    private SoundReader soundReader;
    private UserInputListener userInputListener;
    private LivesManager livesManager;
    private GraphicLives graphicLives;
    private float brickCounter;
    private Heart[] hearts;
    private Paddle[] gamePaddles;
    private Paddle extraPaddle;
    private boolean isExtraPaddleActive = false;
    private boolean isHeartActive = false;
    private Renderable ballImage;

    /**
     * Constructor for Bricker game manager.
     *
     * @param windowTitle      Title of the game window.
     * @param windowDimensions Dimensions of the game window.
     * @param brickDimensions  Dimensions for bricks (rows, columns).
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions,
                              Vector2 brickDimensions) {
        super(windowTitle, windowDimensions);
        this.brickDimensions = brickDimensions;
    }
    /**
     * Getter for Window dimensions.
     * @return Window dimensions
     */
    public Vector2 getWindowDimensions() {
        return this.windowDimensions;
    }

    /**
     * Initializes game by setting up objects and game window.
     * This methods called only once, in the begging of the game.
     * @param imageReader      Instance of ImageReader that Contains readImage method,
     *                         reads an image from disk.
     * @param soundReader      Instance of SoundReader that Contains readSound method,
     *                         reads a wav file.
     * @param inputListener    Instance of SoundReader to get user's input.
     * @param windowController Instance of windowController that holds information and methods
     *                         to control game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.imageReader = imageReader;
        this.windowDimensions = windowController.getWindowDimensions();
        this.windowController = windowController;
        this.soundReader = soundReader;
        this.userInputListener = inputListener;
        Vector2 windowDimensions = windowController.getWindowDimensions();
        this.gamePaddles = new Paddle[PADDLES_NUMBER];
        // Calling all the functions that build the basic game
        addBackground(imageReader, windowDimensions);
        initializeLivesSystem();
        createBall();
        createUserPuddle();
        initializeBorders(this.windowDimensions);
        createBricks(this.brickDimensions);
        this.hearts = new Heart[FALLING_HEARTS_ARR_SIZE];
    }

    /**
     * Game update logic Called once per frame, Overriding the GameManager update() method.
     * update every element, and handling winning and losing.
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.extraPaddle != null){
            if (this.extraPaddle.getExtraHitcount() >= EXTRA_PUDDLE_MAX_COLLISIONS) {
                removeExtraPaddle();
            }
        }
        if (ball.getCenter().y() > windowDimensions.y()) {
            livesManager.loseLife();
            if (livesManager.isGameOver()) {
                if (windowController.openYesNoDialog(LOSING_MESSSAGE)) {
                    windowController.resetGame();
                } else {
                    windowController.closeWindow();
                }
            } else {
                ball.setTopLeftCorner(new Vector2(
                        windowDimensions.x()/2,windowDimensions.y()/2));
            }
        }
        if (hearts != null) {
            for (int i = 0; i < hearts.length; i++) {
                if(hearts[i] != null){
                    hearts[i].setVelocity(new Vector2(ZERO, HEART_VELOCITY));
                }
            }
        }
        if (this.userInputListener.isKeyPressed(W_KEY)) {
            handleWinCondition();
        }
    }
    /**
     * Handles winning condition when the player destroys all bricks, informs it,
     * and checks if the player wants to play again or exit the game.
     */
    private void handleWinCondition() {
        if (windowController.openYesNoDialog(WINNIG_MESSAGE)) {
            windowController.resetGame();
        } else {
            windowController.closeWindow();
        }
    }

    // THESE NEXT FUNCTIONS CREATING AND INITIALIZE THE BASIC GAME, WITHOUT EXTENSIONS
    /**
     * Adds background image to the game.
     * @param imageReader      Background image reader to load.
     * @param windowDimensions Dimensions of game window.
     */
    private void addBackground(ImageReader imageReader, Vector2 windowDimensions) {
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_PATH,
                false);
        GameObject background = new GameObject(
                Vector2.ZERO,
                windowDimensions,
                backgroundImage);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }
    /**
     * Creates left wall, right wall and ceiling.
     * @param windowDimensions Dimensions of game window.
     */
    private void initializeBorders(Vector2 windowDimensions) {
        float boardHeight = windowDimensions.y();
        float boardWidth = windowDimensions.x();
        // Left wall creation
        GameObject leftWall = new GameObject(
                Vector2.ZERO,
                new Vector2(WALLS_WIDTH, boardHeight),
                null);
        gameObjects().addGameObject(leftWall);
        leftWall.setTag(LEFT_WALL_TAG);
        // Right wall creation
        GameObject rightWall = new GameObject(
                new Vector2(boardWidth - WALLS_WIDTH, ZERO),
                new Vector2(WALLS_WIDTH, boardHeight), null);
        gameObjects().addGameObject(rightWall);
        // Ceiling creation
        GameObject ceiling = new GameObject(
                Vector2.ZERO,
                new Vector2(boardWidth, CEILING_HEIGHT),
                null);
        gameObjects().addGameObject(ceiling);
        rightWall.setTag(RIGHT_WALL_TAG);
    }
    /**
     * Creates and initializes a ball object for the game.
     */
    private void createBall() {
        Renderable ballImage =
                this.imageReader.readImage(BALL_PATH, true);
        this.ballImage = ballImage;
        Sound collisionSound = this.soundReader.readSound(BALL_SOUND_PATH);
        this.ball = new Ball(Vector2.ZERO, BALL_DIM,
                ballImage, collisionSound,this);
        // create random bool value, if true, velocity will be positive,
        // if false, velocity will be negative (to move in some horizontal direction),
        // then creates 2D vector accordingly.
        ball.setVelocity(new Vector2(new Random().nextBoolean() ? BALL_VELOCITY :
                -BALL_VELOCITY, BALL_VELOCITY));
        ball.setCenter(BALL_START_POSITION);
        this.gameObjects().addGameObject(ball);
        ball.setTag(BALL_TAG);
    }
    /**
     * Creates and initializes a user paddle object.
     */
    private void createUserPuddle() {
        Renderable paddleImage =
                this.imageReader.readImage(PADDLE_PATH, true);
        Paddle userPaddle = new Paddle(
                Vector2.ZERO, PADDLE_DIM, paddleImage,
                this.userInputListener, windowDimensions.x());
        userPaddle.setTag(USER_PADDLE_TAG);
        userPaddle.setCenter(
                new Vector2(windowDimensions.x() / 2, (int) windowDimensions.y() - 30));
        gameObjects().addGameObject(userPaddle);
        this.gamePaddles[0] = userPaddle;

    }
    /**
     * Initializes Player's lives system by setting maximum and initial lives.
     * Adds numeric and graphical displays.
     */
    private void initializeLivesSystem() {
        int initialLives = INITIAL_HEARTS;
        this.livesManager = new LivesManager(MAX_HEARTS, initialLives);
        addNumericLivesDisplay(initialLives);
        addGraphicLivesDisplay();
    }
    /**
     * Adds a Numeric lives display to game.
     * @param initialLives Initial number of lives to display.
     */
    private void addNumericLivesDisplay(int initialLives) {
        TextRenderable textRenderable = new TextRenderable(LIVES + initialLives,
                FONT,false, false);
        textRenderable.setColor(Color.GREEN);
        NumericLives numericLives = new NumericLives(livesManager, LIVES_TEXT_POSITION, LIVES_TEXT_DIM, textRenderable);
        gameObjects().addGameObject(numericLives, Layer.BACKGROUND);
    }
    /**
     * Adds a Graphic lives display to game.
     */
    private void addGraphicLivesDisplay() {
        Renderable heartImage = this.imageReader.readImage(HEART_PATH, true);
        this.graphicLives = new GraphicLives(livesManager, HEART_START_POSITION, HEART_SIZE, heartImage,
                this);
        gameObjects().addGameObject(graphicLives, Layer.BACKGROUND);
    }

    // THESE NEXT FUNCTIONS REPRESENTS THE EXTENSION OF THE BASIC GAME
    /**
     * Creates extra paddle and adds it to the game only if not already active.
     */
    public void createExtraPaddle() {
        if (!isExtraPaddleActive) {
            Renderable paddleImage = imageReader.readImage(PADDLE_PATH, true);
            extraPaddle = new Paddle(
                    new Vector2(windowDimensions.x() / 2 - 50, windowDimensions.y() /2 ),
                    PADDLE_DIM,
                    paddleImage,
                    userInputListener,
                    windowDimensions.x()
            );
            gameObjects().addGameObject(extraPaddle);
            extraPaddle.setTag(EXTRA_PUDDLE_TAG);
            isExtraPaddleActive = true;
            this.extraPaddle = extraPaddle;
        }
    }
    /**
     * Removes the extra paddle from the game if active.
     */
    private void removeExtraPaddle() {
        if (isExtraPaddleActive && extraPaddle != null) {
            gameObjects().removeGameObject(extraPaddle,Layer.DEFAULT);
            isExtraPaddleActive = false;
            extraPaddle = null;
        }
    }
    /**
     * Creates new puck ball with random velocity and orientation on the upper half of the unit circle
     * and adds it to the game.
     * @param puckPlace Initial position where puck ball will be placed.
     */
    public void createPuckBall(Vector2 puckPlace){
        Renderable puckimage = this.imageReader.readImage(PUCK_BALL_PATH, true);
        Sound collisionSound = this.soundReader.readSound(BALL_SOUND_PATH);
        // Randomize orientation on the upper half of the unit circle.
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI;
        float velX = (float)Math.cos(angle) * BALL_VELOCITY;
        float velY = (float)Math.sin(angle) * BALL_VELOCITY;
        PuckBall puckBall = new PuckBall(Vector2.ZERO, PUCK_BALL_DIM,
                puckimage, collisionSound, this);
        puckBall.setVelocity(new Vector2(velX, velY));
        puckBall.setCenter(puckPlace);
        puckBall.setTag(PUCK_BALL_TAG);
        this.gameObjects().addGameObject(puckBall);
    }
    /**
     * Creates only one extra heart in brick, with specific position and adds it to game.
     * Heart falls downward once created.
     * @param pos Initial position the heart will appear.
     */
    public void createExtraHeart(Vector2 pos) {
        Renderable heartImage = this.imageReader.readImage(HEART_PATH, true);
        for (int i = 0; i < hearts.length; i++) {
            if (hearts[i] == null) {
                hearts[i] = new Heart(Vector2.ZERO, HEART_DIM,
                        heartImage, this, this.gamePaddles[0].getTag(),
                        this.livesManager);
                hearts[i].setTag(HEART_TAG);
                hearts[i].setCenter(pos);
                this.gameObjects().addGameObject(hearts[i]);
                break;
            }
            this.isHeartActive = true;
        }
    }
    /**
     * Removes heart from the game and resets.
     *
     * @param heartToRemove Heart object to removed.
     */
    public void removeHeart(Heart heartToRemove) {
        for (int i = 0; i < hearts.length; i++) {
            if (hearts[i] == heartToRemove) {
                gameObjects().removeGameObject(hearts[i]);
                hearts[i] = null;
                this.isHeartActive = false;
                break;
            }
        }
    }
    /**
     * Updates game ball to turbo mode: increasing velocity and coloring it red.
     * @param ball Ball object to update to turbo.
     */
    public void turboUpdate(Ball ball) {
        Renderable turboImage = this.imageReader.readImage(RED_BALL_PATH, true);
        ball.setOriginalRenderable(this.ballImage);
        ball.setOriginalVelocity(ORIGINAL_VELOCITY);
        ball.setVelocity(ball.getOriginalVelocity().mult(VELOCITY_FACTOR));
        ball.renderer().setRenderable(turboImage);
        ball.setTurboBall();
    }

    // TO COMBINE THE EXTENSION AND THE BASIC - CREATE BRICK FUNCTION.
    /**
     * Creates and initializes a block of bricks in the game based on the given
     * dimensions. Determine each brick strategy by creating strategies
     * from strategies factory.
     * @param brick_dimensions Vector2, x num of rows and
     *                       y num of columns for brick block.
     */
    private void createBricks(Vector2 brick_dimensions) {
        this.brickCounter = brick_dimensions.x() * brick_dimensions.y();
        Renderable brickImage = this.imageReader.readImage(BRICK_PATH, false);
        float bricksWidthSum = this.windowDimensions.x() - (WALLS_WIDTH * 6) - brick_dimensions.x();
        float brickWidth = (bricksWidthSum) / brick_dimensions.x();
        for (int i = 0; i < brick_dimensions.y(); i++) {
            for (int j = 0; j < brick_dimensions.x(); j++) {
                float xPosition = j * (brickWidth + BRICK_CONST) + WALLS_WIDTH;
                float yPosition = i * (BRICK_HEIGHT + BRICK_CONST) + WALLS_WIDTH;
                float x = brickWidth / 2;
                float y = (float) BRICK_HEIGHT / 2;
                Vector2 brickCenter = new Vector2(xPosition + x, yPosition + y);
                // choosing strategies
                BrickerCollisionStrategyFactory brickerCollisionStrategyFactory =
                        new BrickerCollisionStrategyFactory(this);
                CollisionStrategy chosenStrategy =
                        brickerCollisionStrategyFactory.createStrategy();
                Brick brick = new Brick(Vector2.ZERO, new Vector2(brickWidth, BRICK_HEIGHT),
                        brickImage, chosenStrategy,this);
                brick.setCenter(brickCenter);
                this.gameObjects().addGameObject(brick);
                brick.setTag(BRICK_TAG);
            }
        }
    }

    // THESE NEXT FUNCTIONS ARE FOR USED BY EXTERNAL CLASSES
    /**
     * Adds object to the game, by adding it to it's layer and the general game object list.
     * @param obj Instance of game object.
     * @param layer Object's layer
     */
    public void addOutsideGameObject(GameObject obj, int layer){
        gameObjects().addGameObject(obj, layer);
    }
    /**
     * Delete object from the game, by removing
     * it from its layer and the general game object list.
     * @param obj Instance of game object.
     * @param layer Object's layer
     */
    public void deleteGameObject(GameObject obj, int layer) {
        gameObjects().removeGameObject(obj, layer);
        gameObjects().removeGameObject(obj);
    }

    /**
     * Checks if specified puck ball has fallen out of the game window.
     * @param puckBall Puck ball to check.
     * @return True if puck ball's y-coordinate is greater than the window height meaning
     *         that it's out of window. otherwise false.
     */
    public boolean isBallOutOfWindow(PuckBall puckBall) {
        return puckBall.getCenter().y() > windowDimensions.y();
    }

    /**
     * Decreases brick counter and checks if all bricks have been destroyed.
     * If brick counter is zero, player wins, and the win condition is handled.
     * else, change nothing.
     */
    public void decrementBrickCounter() {
        brickCounter--;
        if (brickCounter <= 0) {
            handleWinCondition(); //handle win condition
        }
    }
    // MAIN FUNCTION
    /**
     * Main method of brick game, initializes game with command line arguments
     * of in a default way with constants rows and cols.
     * @param args Command arguments of rows and columns
     *            for brick grid.
     */
    public static void main(String[] args) {
        Vector2 brick_dim;
        if (args.length == 2) {
            int cols = Integer.parseInt(args[0]);
            int rows = Integer.parseInt(args[1]);
            brick_dim = new Vector2(rows, cols);
        } else {
            brick_dim = new Vector2(ROWS, COLS);
        }
        BrickerGameManager BrickGame = new BrickerGameManager(WINDOW_NAME,WINDOW_DIMENSIONS,
                brick_dim);
        BrickGame.run();
    }
}