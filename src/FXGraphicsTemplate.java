import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Use this template to create drawings in FX. Change the name of the class and
 * put your own name as author below. Change the size of the canvas and the
 * window title where marked and add your drawing code where marked.
 *
 * @author Dylan Nguyen
 */

public class FXGraphicsTemplate extends Application {
    /* Current Shape */
    private String currentShape = "square";
    /* List of shapes drawn */
    private ArrayList<GeometricObject> brush = new ArrayList<>();
    /* x coordinate */
    private TextField xField;
    /* y coordinate */
    private TextField yField;
    /* Color choice */
    private Color colorChoice;
    /* Canvas */
    private Canvas canvas;

    /**
     * Draw button handler. This method is called when the draw button draws shapes
     * based on currentShape.
     * @param e
     */
    private void drawButtonHandler(ActionEvent e) {
        try {
            double x = Double.parseDouble(xField.getText());
            double y = Double.parseDouble(yField.getText());

            if (currentShape.equals("square")) {
                Square sq = new Square(x, y, colorChoice, 5);
                sq.draw(canvas.getGraphicsContext2D());
                brush.add(sq);
            } else if (currentShape.equals("circle")) {
                Circle c = new Circle(x, y, colorChoice, 5);
                c.draw(canvas.getGraphicsContext2D());
                brush.add(c);
            }

            System.out.println(Arrays.toString(brush.stream().toArray()));

        } catch (NumberFormatException error) {
            // POP UP
            System.out.println("Invalid input: " + error.getMessage());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Please enter valid numbers for X and Y coordinates.");
            alert.showAndWait();
        }
    }

    /**
     * Erase button handler. This method is called when the erase button is called
     * to erase the canvas and clear the arrayList of brush shapes.
     * @param e
     */
    private void eraseButtonHandler(ActionEvent e) {
        Alert message = new Alert(Alert.AlertType.INFORMATION);
        message.setHeaderText("Erasing");
        message.setContentText("Are you sure you want to erase canvas?");
        message.showAndWait();
        if (message.getResult() == ButtonType.OK) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            brush.clear();
        } else {
            System.out.println("ERASE BUTTON CANCELLED");
        }
    }

    /**
     * Square button handler. This method is called when the square button is
     * pressed changing the current shape to a square.
     *
     * @param e
     */
    private void squareButtonHandler(ActionEvent e) {
        currentShape = "square";
    }

    /**
     * Circle button handler. This method is called when the circle button
     * is pressed changing current shape to circle.
     * @param e
     */
    private void circleButtonHandler(ActionEvent e) {
        currentShape = "circle";
    }

    /**
     * Start method (use this instead of main).
     *
     * @param stage The FX stage to draw on
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root);
        canvas = new Canvas(1000, 1000); // Set canvas Size in Pixels
        stage.setTitle("Drawing Application"); // Set window title
        root.getChildren().add(canvas);
        stage.setScene(scene);
        stage.setResizable(false);

        /* Navbar */
        Pane navbar = new Pane();
        navbar.setPrefSize(canvas.getWidth() * 0.85, 60);
        navbar.setLayoutY((canvas.getHeight() - navbar.getPrefHeight()) - 75);
        navbar.setLayoutX(canvas.getWidth() * .1);
        navbar.setBackground(Background.fill(Color.WHITE));

        DropShadow shadow = new DropShadow(); // Drop Shadow effect.
        shadow.setOffsetX(5);
        shadow.setOffsetY(5);
        navbar.setEffect(shadow);
        navbar.setOpacity(0.75);

        /* Button */
        Button circleButton = new Button("Circle"); // Circle Button
        Button squareButton = new Button("Square"); // Square Button

        circleButton.relocate(10, 10);
        circleButton.setPrefSize(100, 40);
        circleButton.setStyle("-fx-background-color: #232b2b; -fx-text-fill: white;");
        circleButton.setOnAction(this::circleButtonHandler);

        squareButton.relocate(120, 10);
        squareButton.setPrefSize(100, 40);
        squareButton.setStyle("-fx-background-color: #808080; -fx-text-fill: white;");
        squareButton.setOnAction(this::squareButtonHandler);

        /* Color Picker */
        ColorPicker colorPicker = new ColorPicker(); // Drop-menu for colors to choose from.
        colorPicker.setLayoutX(240);
        colorPicker.setLayoutY(10);

        colorPicker.setOnAction(e -> {
            colorChoice = colorPicker.getValue();
            // Add code to set the color for drawing @TODO
            System.out.println("Color selected: " + colorChoice);
        });

        /* X and Y Coordinates for shape */
        xField = new TextField(); // X coordinates
        yField = new TextField(); // Y Coordinates

        xField.setPromptText("X");
        yField.setPromptText("Y");

        xField.relocate(400, 10);
        yField.relocate(450, 10);

        xField.setPrefWidth(50);
        yField.setPrefWidth(50);

        /* Erase Button & Draw Button */
        Button eraseButton = new Button("Erase"); // Erase Button
        Button drawButton = new Button("Draw"); // Draw Button

        drawButton.setStyle("-fx-background-color: #0e1111; -fx-text-fill: white;");

        eraseButton.relocate(510, 10);
        eraseButton.setPrefSize(100, 40);
        eraseButton.setOnAction(this::eraseButtonHandler);

        drawButton.relocate(620, 10);
        drawButton.setPrefSize(100, 40);
        drawButton.setOnAction(this::drawButtonHandler);

        // Brush Shape Selection Items
        MenuButton brushButton = new MenuButton("Brushes Types"); // Brush Selection menu
        brushButton.setLayoutX(730);
        brushButton.setLayoutY(10);
        brushButton.setPrefSize(100, 40);

        MenuItem eraserBrush = new MenuItem("Eraser"); // Eraser menu item
        eraserBrush.setOnAction(e -> {
            canvas.setOnMouseDragged(event -> {
                double x = event.getX();
                double y = event.getY();
                Square sq = new Square(x, y, Color.WHITE, 10);
                sq.draw(canvas.getGraphicsContext2D());
            });
        });

        MenuItem circleBrush = new MenuItem("Circle"); // Circle Menu Item
        circleBrush.setOnAction(e -> {
            currentShape = "circle";

            canvas.setOnMouseDragged(event -> {
                double x = event.getX();
                double y = event.getY();

                try {
                    Circle c = new Circle(x, y, colorChoice, 5);
                    c.draw(canvas.getGraphicsContext2D());
                } catch (NoSuchElementException error) {
                    // Handle exception if brush is empty
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("No Shape Selected");
                    alert.setContentText("Please select a shape before dragging.");
                    alert.showAndWait();
                }
            });
        });

        MenuItem squareBrush = new MenuItem("Square"); // Square Menu Item

        squareBrush.setOnAction(e -> {
            System.out.println("SELECTED SQUARE BRUSH");
            canvas.setOnMouseDragged(event -> {
                try {
                    double x = event.getX();
                    double y = event.getY();
                    Square sq = new Square(x, y, colorChoice, 5);
                    sq.draw(canvas.getGraphicsContext2D());
                } catch (NoSuchElementException error) {
                    // Handle exception if brush is empty
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("No Shape Selected");
                    alert.setContentText("Please select a shape before dragging.");
                    alert.showAndWait();
                }
            });
        });

        /* Add items to brush menu */
        brushButton.getItems().addAll(circleBrush, squareBrush, eraserBrush);

        /* Add Children to navbar */
        navbar.getChildren().addAll(circleButton, squareButton, colorPicker, xField, yField, eraseButton, drawButton, brushButton);

        /* Add Elements to root */
        root.getChildren().add(navbar);

        // Display the stage
        stage.show();
    }

    /**
     * The actual main method that launches the app.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        launch(args);
    }
}