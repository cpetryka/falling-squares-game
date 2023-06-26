package petryka.cezary.fallingsquaresgame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import petryka.cezary.fallingsquaresgame.enums.SquareCreationProbability;
import petryka.cezary.fallingsquaresgame.enums.SquareSpeed;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class FallingSquaresGame extends Application {
    private static final Random random = new Random();

    // Technical parameters
    private int windowWidth = 400;
    private int windowHeight = 500;
    private int panelHeight = 440;
    private final int squareSize = 40;

    // Layouts, controls, ...
    BorderPane root;
    Pane menuPane;
    private Pane gamePane;
    private Label scoreLabel;

    // Game variables
    private int squareSpeed;
    private int gameDuration;
    private double squareCreationProbability;

    private int killedSquares;
    private int missedSquares;
    private int totalSquares;

    private List<Rectangle> squares;
    private boolean gameOver;
    private long startTime;

    // Helpers
    AnimationTimer gameLoop;

    @Override
    public void start(Stage primaryStage) {
        // Assign default values to the variables
        squares = new ArrayList<>();
        killedSquares = 0;
        totalSquares = 0;
        missedSquares = 0;
        gameOver = false;

        // Create the menu pane
        menuPane = generateMenu();
        menuPane.setPrefSize(windowWidth, windowHeight);

        // Create the game pane
        gamePane = new Pane();
        gamePane.setId("game-pane");
        gamePane.setPrefSize(windowWidth, panelHeight);

        // Create the score label
        scoreLabel = new Label("Current score: 0%");
        scoreLabel.setId("score-label");
        scoreLabel.setPrefSize(windowWidth, (double) windowHeight - panelHeight);
        scoreLabel.setAlignment(Pos.CENTER);

        // Create the root element
        // menuPane is assigned to the root element at the beginning
        // After choosing the game parameters gamePane and scoreLabel are assigned to the root element
        root = new BorderPane();
        root.setId("root");
        root.setCenter(menuPane);

        // Create the scene and set it to the stage
        Scene scene = new Scene(root, windowWidth, windowHeight);
        scene.getStylesheets().add("file:src/main/java/petryka/cezary/fallingsquaresgame/style/style.css");

        // When the width of the scene changes, the width of the game pane and the score label must change accordingly
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            windowWidth = newValue.intValue();
            gamePane.setPrefWidth(windowWidth);
            scoreLabel.setPrefWidth(windowWidth);
        });

        // When the height of the scene changes, the height of the game pane and the score label must change accordingly
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            windowHeight = newValue.intValue();
            panelHeight = newValue.intValue() - (int)scoreLabel.getPrefHeight();
            gamePane.setPrefHeight(panelHeight);
            scoreLabel.setPrefHeight(newValue.doubleValue() - panelHeight);
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Falling Squares Game");
        primaryStage.getIcons().add(new Image("file:src/main/java/petryka/cezary/fallingsquaresgame/images/icons/icon.png"));
        primaryStage.show();

        // Create an animation timer that will update the game
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // There's no sense in updating the game if the game is over
                if (!gameOver) {
                    updateGame();
                }
            }
        };
    }

    private Pane generateMenu() {
        var menu = new BorderPane();
        menu.setId("menu");

        // Game title
        var gameTitle = new Label("Falling Squares Game");
        gameTitle.setId("game-title");

        // Add HBox to center the game title
        var gameTitleHBox = new HBox();
        gameTitleHBox.setAlignment(Pos.CENTER);
        gameTitleHBox.getChildren().add(gameTitle);

        menu.setTop(gameTitleHBox);

        // Game duration
        var gameDurationSlider = new Slider(10, 60, 20);
        gameDurationSlider.setId("game-duration-slider");
        gameDurationSlider.setShowTickLabels(true);
        gameDurationSlider.setShowTickMarks(true);
        gameDurationSlider.setMajorTickUnit(10);
        gameDurationSlider.setMinorTickCount(1);
        gameDurationSlider.setSnapToTicks(true);

        var gameDurationLabel = new Label("Game duration");
        gameDurationLabel.getStyleClass().add("menu-label");
        gameDurationLabel.setLabelFor(gameDurationSlider);

        var gameDurationPane = new VBox(5);
        gameDurationPane.setAlignment(Pos.CENTER);
        gameDurationPane.getChildren().addAll(gameDurationLabel, gameDurationSlider);

        // Squares speed
        var squaresSpeed = new ComboBox<SquareSpeed>();
        squaresSpeed.getStyleClass().add("menu-combo-box");
        squaresSpeed.getItems().addAll(SquareSpeed.values());
        squaresSpeed.setValue(SquareSpeed.STANDARD);

        var squaresSpeedLabel = new Label("Squares speed");
        squaresSpeedLabel.getStyleClass().add("menu-label");
        squaresSpeedLabel.setLabelFor(squaresSpeed);

        var squaresSpeedPane = new VBox(5);
        squaresSpeedPane.setAlignment(Pos.CENTER);
        squaresSpeedPane.getChildren().addAll(squaresSpeedLabel, squaresSpeed);

        // Squares creation probability
        var squaresCreationProbability = new ComboBox<SquareCreationProbability>();
        squaresCreationProbability.getStyleClass().add("menu-combo-box");
        squaresCreationProbability.getItems().addAll(SquareCreationProbability.values());
        squaresCreationProbability.setValue(SquareCreationProbability.STANDARD);

        var squaresCreationProbabilityLabel = new Label("Squares number");
        squaresCreationProbabilityLabel.getStyleClass().add("menu-label");
        squaresCreationProbabilityLabel.setLabelFor(squaresCreationProbability);

        var squaresCreationProbabilityPane = new VBox(5);
        squaresCreationProbabilityPane.setAlignment(Pos.CENTER);
        squaresCreationProbabilityPane.getChildren().addAll(squaresCreationProbabilityLabel, squaresCreationProbability);

        // Menu center
        var menuCenter = new VBox(25);
        menuCenter.setAlignment(Pos.CENTER);

        menuCenter.getChildren().addAll(gameDurationPane);
        menuCenter.getChildren().addAll(squaresSpeedPane);
        menuCenter.getChildren().addAll(squaresCreationProbabilityPane);
        menu.setCenter(menuCenter);

        // Start the game button
        var startGameButton = new Button("Start Game");
        startGameButton.setId("start-game-button");
        startGameButton.setOnAction(event -> {
            // Set game parameters
            this.gameDuration = (int)gameDurationSlider.getValue();
            this.squareSpeed = squaresSpeed.getValue().getSpeed();
            this.squareCreationProbability = squaresCreationProbability.getValue().getProbability();

            // Remove the menu pane from the root element and add the game pane and scoreLabel
            root.setCenter(gamePane);
            root.setBottom(scoreLabel);

            // Assign current time
            startTime = System.currentTimeMillis();

            // Start the game loop
            gameLoop.start();
        });

        var startGameButtonHBox = new HBox();
        startGameButtonHBox.setAlignment(Pos.CENTER);
        startGameButtonHBox.setPadding(new Insets(15));
        startGameButtonHBox.getChildren().add(startGameButton);

        menu.setBottom(startGameButtonHBox);

        return menu;
    }

    // Update the game
    private void updateGame() {
        // Check if the game should end
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

        if (elapsedTime >= gameDuration) {
            endGame();
            return;
        }

        // Create a square with a certain probability
        if (random.nextDouble() < squareCreationProbability) {
            createSquare();
        }

        // Move all the squares down
        Iterator<Rectangle> iterator = squares.iterator();
        while (iterator.hasNext()) {
            Rectangle square = iterator.next();
            square.setLayoutY(square.getLayoutY() + squareSpeed); // Move the square down

            if (square.getLayoutY() >= (panelHeight - squareSize)) {
                iterator.remove();
                gamePane.getChildren().remove(square);
                missedSquares++;
                updateScoreLabel();
            }
        }
    }

    // Create a square and add it to the game pane at a random position
    private void createSquare() {
        Rectangle square = new Rectangle(squareSize, squareSize, Color.DARKBLUE);
        square.setLayoutX(random.nextInt(windowWidth - squareSize));
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