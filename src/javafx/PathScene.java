package javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.awt.*;

/**
 * @web http://java-buddy.blogspot.com/
 */
@SuppressWarnings("Duplicates")
public class PathScene  {
    String updown = "";
    volatile boolean pressed = false;
    volatile boolean ended = false;
    Circle c = new Circle(5);

    public void start() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Canvas canvas = new Canvas(5000, 768);

        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        initDraw(graphicsContext);
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
        Stage primaryStage=new Stage();
        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setFullScreen(true);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("java-buddy.blogspot.com");
        primaryStage.setScene(scene);
        primaryStage.show();
        graphicsContext.beginPath();
        root.getChildren().add(c);
        c.setLayoutX(1366 / 2);
        c.setLayoutY((768 / 3) * 2);



        new Thread(() -> {
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
            JOptionPane.showMessageDialog(null, "acabou");
        }).start();

    }

    public void moveObject(int y) {


        System.out.println("y:"+ y);
        if (y - c.getRadius() < c.getRadius())
            y = (int) c.getRadius();

        if (y + c.getRadius() >= 755)
            y = (int) (755 - c.getRadius());


        c.setLayoutY(y);


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