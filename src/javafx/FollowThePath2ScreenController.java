package javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import uncoupledprograms.pconly.IPathScreen;
import uncoupledprograms.pconly.PathHBox;
import uncoupledprograms.pconly.PathObjectMoveFunction;
import uncoupledprograms.pconly.PathVBox;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;


public class FollowThePath2ScreenController implements Initializable, IPathScreen {
    @FXML
    private Circle theBall;
    @FXML
    private Rectangle goalSquare;
    @FXML
    private AnchorPane rootAP;




    private void initDraw(GraphicsContext gc){
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
        gc.setLineWidth(1);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Canvas canvas = new Canvas(5000, 500);

        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        initDraw(graphicsContext);

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> {
                    graphicsContext.beginPath();
                    graphicsContext.moveTo(event.getX(), event.getY());
                    graphicsContext.stroke();
                });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                event -> {
                    graphicsContext.lineTo(event.getX(), event.getY());
                    graphicsContext.stroke();
                    canvas.setLayoutX(canvas.getLayoutX()-1);
                });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                event -> {

                });

        rootAP.getChildren().add(canvas);

        goalSquare.toFront();
        theBall.toFront();


    }

/*

            <AnchorPane fx:id="l" layoutX="121.0" layoutY="157.0" prefHeight="276.0" prefWidth="75.0" style="-fx-background-color: black;" />
            <AnchorPane fx:id="r" layoutX="571.0" layoutY="157.0" prefHeight="276.0" prefWidth="75.0" style="-fx-background-color: black;" />
            <AnchorPane fx:id="t" layoutX="121.0" layoutY="82.0" prefHeight="75.0" prefWidth="525.0" style="-fx-background-color: black;" />



* */

    @FXML
    public void start(ActionEvent event) {
        PathObjectMoveFunction.getInstance().start(this, 800, 510);
    }

    @FXML
    public void stop(ActionEvent event) {
        PathObjectMoveFunction.getInstance().stop();
    }

    private void showBluetoothDisconnectedAlert() {
        Alert closeConfirmation = new Alert(
                Alert.AlertType.WARNING,
                "You should to connect with the glove first."
        );
        closeConfirmation.setHeaderText("Bluetooth Disconnected");
        closeConfirmation.setTitle("Bluetooth Disconnected");
        closeConfirmation.show();
    }

    @Override
    public void moveObject(int x, int y) {
        if (x - theBall.getRadius() < theBall.getRadius())
            x = (int) theBall.getRadius();

        if (y - theBall.getRadius() < theBall.getRadius())
            y = (int) theBall.getRadius();

        if (x + theBall.getRadius() >= 800)
            x = (int) (800 - theBall.getRadius());
        if (y + theBall.getRadius() >= 510)
            y = (int) (510 - theBall.getRadius());


        theBall.setLayoutX(x);
        theBall.setLayoutY(y);



        if(theBall.getBoundsInParent().intersects(goalSquare.getBoundsInParent())){
            JOptionPane.showMessageDialog(null,"DONE");
        }


    }
}
