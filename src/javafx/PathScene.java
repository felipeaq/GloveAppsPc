package javafx;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.FillRule;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import uncoupledprograms.pconly.IPathScreen;
import uncoupledprograms.pconly.PathObjectMoveFunction;

import java.awt.*;

import javafx.scene.control.Label;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @web http://java-buddy.blogspot.com/
 */

public class PathScene {
    private volatile boolean locked = true;
    private Circle fingerCircle = new Circle(5);
    private Canvas canvas;
    private ImageView im;
    private boolean isRunning = false;
    private FollowThePath2ScreenController caller;
    private Stage stage;
    private static PathScene ME;
    private int miss;
    private int hit;
    private int pixelsJumped = 3;
    private int delayTime=10;

    private PathScene(){
    }

    public static PathScene getInstance(){
        if (ME==null)
            ME=new PathScene();
        return ME;
    }

    public void start(FollowThePath2ScreenController caller, String pathImage,int fps,int pixelsJumped) {
        delayTime= (int)(1/(double)fps*1000);
        this.pixelsJumped=pixelsJumped;
        if (isRunning)
            return;
        isRunning = true;
        this.caller = caller;
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        im = new ImageView(pathImage);

        AnchorPane root = new AnchorPane();
        root.getChildren().add(im);

        canvas = new Canvas(im.getImage().getWidth(), im.getImage().getHeight());
        root.getChildren().add(canvas);

        // canvas.getGraphicsContext2D().drawImage(image, 0, 0);

        im.setLayoutX(1000);
        canvas.setLayoutX(1000);
        canvas.toFront();

        stage = new Stage();
        Scene scene = new Scene(root, gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight());
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
        //TODO pegar o tamanho de tela pelo S.O.
        fingerCircle.setFill(Color.BLUE);
        fingerCircle.setLayoutX(1366 / 2);
        fingerCircle.setLayoutY((768 / 3) * 2);
        root.getChildren().add(fingerCircle);
        fingerCircle.setVisible(false);
        graphicsContext.beginPath();

        miss=0;
        hit=0;
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
                stackPane.setVisible(false);
                fingerCircle.setVisible(true);
                locked = false;

/*
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
                ft.play();*/

            } catch (InterruptedException e) {
                e.printStackTrace();
            }







            double fingerCircleX = fingerCircle.getLayoutX();
            while ((fingerCircleX < canvas.getLayoutX() + canvas.getWidth() + 10) && work.get()) {
                //System.out.print(im.getImage().getPixelReader().getColor((int)fingerCircle.getLayoutX(), (int)fingerCircleX).toString()+" ");//TODO descobrir x e y
                //System.out.println(fingerCircle.getLayoutY()+" "+fingerCircleX);
//
                Point2D b = null;
                try {
                    b = canvas.screenToLocal(fingerCircleX, fingerCircle.getLayoutY());
                } catch (NullPointerException n) {
                    System.out.println("1");
                }

                try {
                    //double time =System.currentTimeMillis();

                    if(im.getImage().getPixelReader().getColor((int)b.getX(), (int)b.getY()).equals(Color.WHITE)){
                        miss++;
                    }else{
                        hit++;
                    }

                    //System.out.println((b.getX()+" "+ b.getY()+" "+(System.currentTimeMillis()-time)));








                } catch (NullPointerException n) {
                    System.out.println("44");
                } catch (IndexOutOfBoundsException e){

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
                    Thread.sleep(delayTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (work.get())
                postDoneOperation();
            caller.serviceStopped();
        });


        t.start();


        //Nada dps daqui é executado


        stage.setOnHiding(event -> {
            System.out.println("Closing Stage");
            work.set(false);
            PathObjectMoveFunction.getInstance().stop();
            isRunning = false;

            if (miss+hit>0) {
                this.caller.setScore((double)hit/(hit+miss)*100);
            }else{
                this.caller.setScore();
            }
        });



    }

    private void postDoneOperation() {
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);


        AtomicReference<WritableImage> snapshot = new AtomicReference<>();
        Platform.runLater(() -> {
            canvas.getGraphicsContext2D().setFillRule(FillRule.EVEN_ODD);
            BufferedImage combined = new BufferedImage((int) im.getImage().getWidth(), (int) im.getImage().getHeight(), BufferedImage.TYPE_INT_RGB);//);

            snapshot.set(canvas.snapshot(params, null));

            Graphics g = combined.getGraphics();
            g.setColor(java.awt.Color.WHITE);
            g.drawImage(SwingFXUtils.fromFXImage(im.getImage(), null), 0, 0, null);
            g.drawImage(SwingFXUtils.fromFXImage(snapshot.get(), null), 0, 0, null);
            caller.setCreatedImage(combined);

//            BufferedImage image = SwingFXUtils.fromFXImage(im.getImage(), null);


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