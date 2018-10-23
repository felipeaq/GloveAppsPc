package javafx;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.util.Objects;
import java.util.Stack;

/**
 * @web http://java-buddy.blogspot.com/
 */
public class DrawOnCanvas extends Application {
    String updown = "";
    volatile boolean pressed = false;
    volatile boolean ended = false;

    @Override
    public void start(Stage primaryStage) throws InterruptedException {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        //Canvas canvas = new Canvas(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight());
        //   Canvas canvas = new Canvas(5000, gd.getDisplayMode().getHeight());
        Canvas canvas = new Canvas(5000, 768);

        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        initDraw(graphicsContext);
        Circle c = new Circle(5);
        c.setFill(Color.BLUE);

        AnchorPane root = new AnchorPane();
        //   root.setBackground(new Background(new BackgroundFill(Color.RED,new CornerRadii(0), new Insets(0))));
        ImageView im = new ImageView("images/noTitle.png");
        root.getChildren().add(im);
        im.setLayoutX(1000);
        canvas.setLayoutX(1000);
        root.getChildren().add(canvas);
        canvas.toFront();
        System.out.println();
        primaryStage.setFullScreen(true);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("java-buddy.blogspot.com");
        graphicsContext.beginPath();
        root.getChildren().add(c);
        c.setLayoutX(1366 / 2);
        c.setLayoutY((768 / 3) * 2);

        Scene scene = new Scene(root, 1366, 768);



        StackPane stackPane = new StackPane();
        Label countLabel = new Label();
        countLabel.setTextAlignment(TextAlignment.CENTER);
        countLabel.setFont(new Font("Arial  ",30 ));
        countLabel.setText("Começando em:\n3");
        stackPane.getChildren().add(countLabel);
        root.getChildren().add(stackPane);


        FadeTransition ft = new FadeTransition(Duration.millis(3000), stackPane);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);

        ft.play();
        ft.setOnFinished(event -> stackPane.setVisible(false));


        primaryStage.setScene(scene);
        primaryStage.show();
        /*
        funções dos botoes
         */
        scene.setOnKeyPressed(event -> {
            pressed = true;
            if (event.getCode() == KeyCode.UP) {
                updown = "UP";
            } else if (event.getCode() == KeyCode.DOWN) {
                updown = "DOWN";
            }
        });
        scene.setOnKeyReleased(event -> {
            pressed = false;
            if (event.getCode() == KeyCode.UP) {
                updown = "NONE";
            } else if (event.getCode() == KeyCode.DOWN) {
                updown = "NONE";
            }
        });

/*
    Thread do teclado
*/
/*
        new Thread(() -> {
            while (!ended) {
                while (pressed) {

                    if (updown.equals("UP")) {
                        Platform.runLater(() -> {
                            int y = (int) c.getLayoutY() - 1;

                            if (y - c.getRadius() < c.getRadius() + 3)
                                y = (int) c.getRadius() + 3;

                            if (y + c.getRadius() >= 755)
                                y = (int) (755 - c.getRadius());

                            c.setLayoutY(y);
                        });
                    } else if (updown.equals("DOWN")) {
                        Platform.runLater(() -> {
                            int y = (int) c.getLayoutY() + 1;

                            if (y - c.getRadius() < c.getRadius() + 3)
                                y = (int) c.getRadius() + 8;

                            if (y + c.getRadius() >= 755)
                                y = (int) (755 - c.getRadius());

                            c.setLayoutY(y);
                        });
                    }
                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        */
        /*new Thread(() -> {
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            for (int i = 0; i < canvas.getWidth() - gd.getDisplayMode().getWidth(); i++) {

                Point2D b = canvas.screenToLocal(c.getLayoutX(), c.getLayoutY());
                graphicsContext.lineTo(b.getX(), b.getY());
                Platform.runLater(() -> {
                    graphicsContext.stroke();
                    canvas.setLayoutX(canvas.getLayoutX() - 1);
                    im.setLayoutX(im.getLayoutX() - 1);
                });
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/

    }

    public static void main(String[] args) {
        launch(args);
    }

    private void initDraw(GraphicsContext gc) {
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);

        gc.fill();
        gc.strokeRect(
                0,              //x of the upper left corner
                0,              //y of the upper left corner
                canvasWidth,    //width of the rectangle
                canvasHeight);  //height of the rectangle

        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(10);

    }

}