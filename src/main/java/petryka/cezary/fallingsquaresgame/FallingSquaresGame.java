package petryka.cezary.fallingsquaresgame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class FallingSquaresGame extends Application {
    private static final Random random = new Random();

    // Technical constants
    private static int WINDOW_WIDTH = 400;
    private static int WINDOW_HEIGHT = 500;
    private static int PANEL_HEIGHT = 440;
    private static final int SQUARE_SIZE = 40;
    private static final int SQUARE_SPEED = 3;
    private static final int GAME_DURATION = 20; // in seconds
    private static final double SQUARE_CREATION_PROBABILITY = 0.03;

    // Pane and label of the game
    private Pane gamePane;
    private Label scoreLabel;

    // Game variables
    private int killedSquares;
    private int missedSquares;
    private int totalSquares;
    private boolean gameOver;
    private long startTime;

    private List<Rectangle> squares;

    @Override
    public void start(Stage primaryStage) {
        // Assign default values to the variables
        squares = new ArrayList<>();
        killedSquares = 0;
        totalSquares = 0;
        missedSquares = 0;
        gameOver = false;

        // Create the game pane
        gamePane = new Pane();
        gamePane.setId("game-pane");
        gamePane.setPrefSize(WINDOW_WIDTH, PANEL_HEIGHT);

        // Create the score label
        scoreLabel = new Label("Current score: 0%");
        scoreLabel.setId("score-label");
        scoreLabel.setPrefSize(WINDOW_WIDTH, (double)WINDOW_HEIGHT - PANEL_HEIGHT);
        scoreLabel.setAlignment(Pos.CENTER);

        // Create the root pane and add the game pane and the score label to it
        BorderPane root = new BorderPane();
        root.setId("root");
        root.setCenter(gamePane);
        root.setBottom(scoreLabel);

        // Create the scene and set it to the stage
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add("file:src/main/java/petryka/cezary/fallingsquaresgame/style/style.css");

        // When the width of the scene changes, the width of the game pane and the score label must change accordingly
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            WINDOW_WIDTH = newValue.intValue();
            gamePane.setPrefWidth(WINDOW_WIDTH);
            scoreLabel.setPrefWidth(WINDOW_WIDTH);
        });

        // When the height of the scene changes, the height of the game pane and the score label must change accordingly
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            WINDOW_HEIGHT = newValue.intValue();
            PANEL_HEIGHT = newValue.intValue() - (int)scoreLabel.getPrefHeight();
            gamePane.setPrefHeight(PANEL_HEIGHT);
            scoreLabel.setPrefHeight(newValue.doubleValue() - PANEL_HEIGHT);
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Falling Squares Game");
        primaryStage.getIcons().add(new Image("file:src/main/java/petryka/cezary/fallingsquaresgame/images/icons/icon.png"));
        primaryStage.show();

        // Start the game and create an animation timer that will update the game
        var gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // There's no sense in updating the game if the game is over
                if (!gameOver) {
                    updateGame();
                }
            }
        };

        startTime = System.currentTimeMillis();
        gameLoop.start();
    }

    // Update the game
    private void updateGame() {
        // Check if the game should end
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

        if (elapsedTime >= GAME_DURATION) {
            endGame();
            return;
        }

        // Create a square with a certain probability
        if (random.nextDouble() < SQUARE_CREATION_PROBABILITY) {
            createSquare();
        }

        // Move all the squares down
        Iterator<Rectangle> iterator = squares.iterator();
        while (iterator.hasNext()) {
            Rectangle square = iterator.next();
            square.setLayoutY(square.getLayoutY() + SQUARE_SPEED); // Move the square down

            if (square.getLayoutY() >= (PANEL_HEIGHT - SQUARE_SIZE)) {
                iterator.remove();
                gamePane.getChildren().remove(square);
                missedSquares++;
                updateScoreLabel();
            }
        }
    }

    // Create a square and add it to the game pane at a random position
    private void createSquare() {
        Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE, Color.DARKBLUE);
        square.setLayoutX(random.nextInt(WINDOW_WIDTH - SQUARE_SIZE));
        square.setOnMousePressed(this::handleMouseClick);
        gamePane.getChildren().add(square);
        squares.add(square);
        totalSquares++;
    }

    // Update the score label
    private void updateScoreLabel() {
        double score = (killedSquares / (double)(killedSquares + missedSquares)) * 100;
        scoreLabel.setText(String.format("Current score: %.0f%%", score));
    }

    // Method that ends the game and shows an alert with the final score
    private void endGame() {
        gameOver = true;

        // Show an alert with the final score
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("You " + (killedSquares > missedSquares ? "won!" : "lost!") + " You managed to kill " + killedSquares + " out of " + (killedSquares + missedSquares) + " squares.");
        alert.initOwner(gamePane.getScene().getWindow()); // Makes the alert appear in the center of the gamePane
        alert.setOnCloseRequest(event -> {
            alert.close();
            System.exit(0);
        });
        alert.show();
    }

    // Handle the mouse click on a square
    private void handleMouseClick(MouseEvent event) {
        // There's no sense in handling the mouse click if the game is over
        if (!gameOver) {
            Rectangle clickedSquare = (Rectangle) event.getSource();
            gamePane.getChildren().remove(clickedSquare);
            squares.remove(clickedSquare);
            killedSquares++;

            // After a square is killed, update the score label
            updateScoreLabel();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}