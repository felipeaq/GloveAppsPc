package javafx;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import uncoupledprograms.pconly.PathObjectMoveFunction;

import java.awt.*;

import javafx.scene.control.Label;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @web http://java-buddy.blogspot.com/
 */
@SuppressWarnings("Duplicates")
public class PathScene {
    private volatile boolean locked = true;
    private Circle fingerCircle = new Circle(5);
    private Canvas canvas;
    private ImageView im;
    private boolean isRunning = false;
    private PathSceneCaller caller;
    private Stage stage;


    public void start(PathSceneCaller caller) {
        if (isRunning)
            return;
        isRunning = true;
        this.caller = caller;
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        im = new ImageView("images/noTitle2.png");
        AnchorPane root = new AnchorPane();
        root.getChildren().add(im);

        canvas = new Canvas(im.getImage().getWidth(), im.getImage().getHeight());
        root.getChildren().add(canvas);

        im.setLayoutX(1000);
        canvas.setLayoutX(1000);
        canvas.toFront();

        stage = new Stage();
        Scene scene = new Scene(root, 1366, 768);
        stage.setFullScreen(true);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();


        StackPane stackPane = new StackPane();
        stackPane.setPrefWidth(scene.getWidth());
        stackPane.setPrefHeight(scene.getHeight());

        Label countLabel = new Label();
        countLabel.setTextAlignment(TextAlignment.CENTER);
        countLabel.setFont(new Font("Arial  ", 40));
        stackPane.getChildren().add(countLabel);
        root.getChildren().add(stackPane);


        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        initDraw(graphicsContext);
        fingerCircle.setFill(Color.BLUE);
        fingerCircle.setLayoutX(1366 / 2);
        fingerCircle.setLayoutY((768 / 3) * 2);
        root.getChildren().add(fingerCircle);
        fingerCircle.setVisible(false);
        graphicsContext.beginPath();


        AtomicBoolean work = new AtomicBoolean(true);
        Thread t = new Thread(() -> {
            try {
                Platform.runLater(() -> countLabel.setText(("Começando em: \n5")));
                Thread.sleep(1000);
                Platform.runLater(() -> countLabel.setText(("Começando em: \n4")));
                Thread.sleep(1000);
                Platform.runLater(() -> countLabel.setText(("Começando em: \n3")));
                Thread.sleep(1000);
                Platform.runLater(() -> countLabel.setText(("Começando em: \n2")));
                Thread.sleep(1000);
                Platform.runLater(() -> countLabel.setText(("Começando em: \n1")));
                Thread.sleep(1000);
                Platform.runLater(() -> countLabel.setText(("Começando em: \n0")));

                FadeTransition ft = new FadeTransition(Duration.millis(1000), stackPane);
                ft.setFromValue(1.0);
                ft.setToValue(0);
                ft.setCycleCount(1);
                ft.setAutoReverse(false);
                ft.setOnFinished(event -> {
                    stackPane.setVisible(false);
                    fingerCircle.setVisible(true);
                    locked = false;
                });
                ft.play();


            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            int pixelsJumped = 3;

            double fingerCircleX = fingerCircle.getLayoutX();
            while ((fingerCircleX < canvas.getLayoutX() + canvas.getWidth() + 10) && work.get()) {
//                System.out.println((fingerCircleX<totalXFingerCirclePixelRun)+" ++ "+fingerCircleX+" ++ "+ totalXFingerCirclePixelRun);
                Point2D b = null;
                try {
                    b = canvas.screenToLocal(fingerCircleX, fingerCircle.getLayoutY());
                } catch (NullPointerException n) {
                    System.out.println("1");
                }
                try {
                    graphicsContext.lineTo(b.getX(), b.getY());
                } catch (NullPointerException n) {
                    System.out.println("2");
                }

                Platform.runLater(() -> {
                    try {
                        graphicsContext.stroke();
                    } catch (NullPointerException n) {
                        System.out.println("3");
                    }

                    try {
                        canvas.setLayoutX(canvas.getLayoutX() - pixelsJumped);
                    } catch (NullPointerException n) {
                        System.out.println("4");
                    }
                    try {
                        im.setLayoutX(im.getLayoutX() - pixelsJumped);
                    } catch (NullPointerException n) {
                        System.out.println("5");
                    }

                });
                try {
                    Thread.sleep(10 * pixelsJumped);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (work.get())
                postDoneOperation();
        });

        t.start();


        stage.setOnHiding(event -> {
            System.out.println("Closing Stage");
            work.set(false);
            PathObjectMoveFunction.getInstance().stop();
            isRunning = false;
        });

    }

    private void postDoneOperation() {
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        AtomicReference<WritableImage> snapshot = new AtomicReference<>();
        Platform.runLater(() -> {
            snapshot.set(canvas.snapshot(params, null));
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot.get(), null), "png", new FileChooser().showSaveDialog(null));
            } catch (IOException e) {
                e.printStackTrace();
            }
            isRunning = false;
            stage.close();
        });
    }

    public void moveObject(int y) {


        if (!locked) {
            if (y - fingerCircle.getRadius() < fingerCircle.getRadius())
                y = (int) fingerCircle.getRadius();

            if (y + fingerCircle.getRadius() >= 755)
                y = (int) (755 - fingerCircle.getRadius());


            fingerCircle.setLayoutY(y);
        }

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
        gc.setLineWidth(5);

    }

}