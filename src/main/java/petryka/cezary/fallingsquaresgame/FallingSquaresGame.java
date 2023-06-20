package petryka.cezary.fallingsquaresgame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FallingSquaresGame extends Application {
    private static final Random random = new Random();

    // Technical constants
    private int WINDOW_WIDTH = 400;
    private int WINDOW_HEIGHT = 500;
    private int PANEL_HEIGHT = 440;
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
        gamePane.setPrefSize(WINDOW_WIDTH, PANEL_HEIGHT);
        gamePane.setBackground(Background.fill(Color.GOLD));

        // Create the score label
        scoreLabel = new Label("Current score: 0%");
        scoreLabel.setPrefSize(WINDOW_WIDTH, (double)WINDOW_HEIGHT - PANEL_HEIGHT);
        scoreLabel.setAlignment(Pos.CENTER);
        scoreLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        // Create the root pane and add the game pane and the score label to it
        BorderPane root = new BorderPane();
        root.setCenter(gamePane);
        root.setBottom(scoreLabel);

        // Create the scene and set it to the stage
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        // When the width of the scene changes, the width of the game pane and the score label must change accordingly
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            WINDOW_WIDTH = newValue.intValue();
            gamePane.setPrefWidth(WINDOW_WIDTH);
            scoreLabel.setPrefWidth(WINDOW_WIDTH);
            System.out.println("Width changed to " + WINDOW_WIDTH);
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
        primaryStage.show();

        // Start the game and create an animation timer that will update the game
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // There's no sense in updating the game if the game is over
                if (!gameOver) {
                    // updateGame();
                }
            }
        };

        startTime = System.currentTimeMillis();
        gameLoop.start();
    }

    public static void main(String[] args) {
        launch();
    }
}