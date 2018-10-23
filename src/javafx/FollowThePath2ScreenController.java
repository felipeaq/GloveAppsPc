package javafx;

import bluetooth.BluetoothConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
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
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;


public class FollowThePath2ScreenController implements Initializable, IPathScreen,PathSceneCaller {
    @FXML
    private AnchorPane rootAP;

    Circle c = new Circle(5);


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    PathScene pathScene = new PathScene();

    @FXML
    public void start(ActionEvent event) {
        if (BluetoothConnection.getBluetoothStatus().isConnected()) {
            PathObjectMoveFunction.getInstance().start(this, 800, 510);
            pathScene.start(this);
        }else{
            showBluetoothDisconnectedAlert();
        }

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
    }

    @Override
    public void moveObject(int y) {
        pathScene.moveObject(y);


    }
}
