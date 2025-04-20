__****Bricker Game****__
_Overview_
Bricker is a classic brick-breaking game implemented in Java. The player controls a paddle at the bottom of the screen to bounce a ball and destroy bricks arranged at the top. The game includes several special power-ups and features to enhance gameplay.
Game Features
Basic Gameplay

Control a paddle using keyboard input to bounce a ball
Break bricks by hitting them with the ball
Lose a life when the ball falls below the screen
Win by destroying all bricks on the screen

Special Features

Lives System: Both numeric and graphical representation of remaining lives
Multiple Balls: Some bricks release additional "puck balls" when destroyed
Extra Paddle: Get an additional paddle that lasts for 4 ball hits
Turbo Ball: Special balls that move faster and change color
Extra Hearts: Collect falling hearts to gain additional lives

Collision Strategies
The game implements various collision strategies for bricks:

Basic Collision: Removes the brick when hit
Extra Ball Strategy: Creates two additional puck balls
Extra Paddle Strategy: Adds a secondary paddle
Turbo Ball Strategy: Makes the ball move faster
Extra Heart Strategy: Drops a collectible heart
Double Collision Strategy: Combines multiple strategies for complex behaviors

Technical Implementation
Design Pattern
The project uses the Strategy design pattern for collision handling, which:

Encapsulates collision logic in separate strategy objects
Follows the Open-Closed Principle for easy extension
Allows for flexible combination of behaviors

Key Classes

BrickerGameManager: Main game controller that initializes and updates game elements
Ball & PuckBall: Game objects representing the main ball and special puck balls
Brick: Game object with assigned collision strategies
Paddle: Player-controlled game object for bouncing the ball
LivesManager: Manages player lives with both numeric and graphical displays
CollisionStrategy: Interface implemented by various brick collision strategies

Game Loop
The game follows a standard game loop pattern:

Initialize game objects
Update game state each frame
Check for collisions and apply appropriate strategies
Handle win/lose conditions

Running the Game
Execute the main method in the BrickerGameManager class. You can provide command-line arguments to specify the number of rows and columns for the brick grid:
java bricker.main.BrickerGameManager [rows] [columns]
If no arguments are provided, the game uses default values (7 rows, 8 columns).
Controls

Move paddle left/right using the mouse or keyboard
Press 'W' to trigger win condition (for debugging)

Project Structure
The code is organized into packages:

bricker.main: Contains the main game manager
bricker.gameobjects: Game object classes like Ball, Brick, Paddle
bricker.brick_strategies: Different collision strategies for bricks