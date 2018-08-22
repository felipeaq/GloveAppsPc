package javafx;

import bluetooth.BluetoothConnection;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import uncoupledglovedatathings.MyColors;
import uncoupledprograms.pconly.*;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class FollowThePathScreenController implements Initializable, IPathScreen {
    @FXML
    private Circle theBall;
    @FXML
    private Rectangle goalSquare;
    @FXML
    private AnchorPane rootAP;


    private PathVBox leftPVB = new PathVBox(30, Color.BLACK);
    private PathVBox rightPVB = new PathVBox(30, Color.BLACK);
    private PathHBox topPHB = new PathHBox(30, Color.BLACK);
    private PathHBox topLeftPHB = new PathHBox(1, Color.BLACK);
    private PathHBox topRightPHB = new PathHBox(1, Color.BLACK);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AnchorPane.setLeftAnchor(leftPVB, 121.0);
        AnchorPane.setTopAnchor(leftPVB, 157.0);
        leftPVB.setPrefWidth(75.0);
        leftPVB.setPrefHeight(276.0);
        rootAP.getChildren().add(leftPVB);


        AnchorPane.setLeftAnchor(rightPVB, 571.0);
        AnchorPane.setTopAnchor(rightPVB, 157.0);
        rightPVB.setPrefWidth(75.0);
        rightPVB.setPrefHeight(276.0);
        rootAP.getChildren().add(rightPVB);

        AnchorPane.setLeftAnchor(topPHB, 196.0);
        AnchorPane.setTopAnchor(topPHB, 82.0);
        topPHB.setPrefHeight(75.0);
        topPHB.setPrefWidth(450.0);
        rootAP.getChildren().add(topPHB);

        AnchorPane.setLeftAnchor(topLeftPHB, 121.0);
        AnchorPane.setTopAnchor(topLeftPHB, 82.0);
        topLeftPHB.setPrefHeight(75.0);
        topLeftPHB.setPrefWidth(75.0);
        rootAP.getChildren().add(topLeftPHB);

        AnchorPane.setLeftAnchor(topRightPHB, 571.0);
        AnchorPane.setTopAnchor(topRightPHB, 82.0);
        topRightPHB.setPrefHeight(75.0);
        topRightPHB.setPrefWidth(75.0);
        rootAP.getChildren().add(topRightPHB);

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


        topPHB.colide(theBall, Color.BLUE);
        leftPVB.colide(theBall, Color.BLUE);
        rightPVB.colide(theBall, Color.BLUE);
        topLeftPHB.colide(theBall, Color.BLUE);
        topRightPHB.colide(theBall, Color.BLUE);


        if(theBall.getBoundsInParent().intersects(goalSquare.getBoundsInParent())){
            JOptionPane.showMessageDialog(null,"DONE");
        }


    }
}
