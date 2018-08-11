package javafx;

import bluetooth.BluetoothConnection;
import bluetooth.IBlutoothInfoScreen;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable, IBlutoothInfoScreen {
    @FXML
    public VBox buttonsContainer;
    @FXML
    VBox container;
    @FXML
    Label btStatus;
    @FXML
    JFXToggleButton btToogle;

    private BluetoothConnection bluetoothConnection = BluetoothConnection.getInstance(this);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void minimize(MouseEvent mouseEvent) {
        minimizeStageOfNode((Node) mouseEvent.getSource());
    }

    private void minimizeStageOfNode(Node node) {
        ((Stage) (node).getScene().getWindow()).setIconified(true);
    }

    public void close(MouseEvent mouseEvent) {

        Alert closeConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Tem Certeza que deseja sair do programa?"
        );
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                ButtonType.OK
        );
        exitButton.setText("Fechar");
        closeConfirmation.setHeaderText("Confirmar Saida");
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);

        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (ButtonType.OK.equals(closeResponse.get())) {
            System.exit(0);
        }
    }


    public void toogleAction(ActionEvent event){
        if(event.getSource() instanceof JFXToggleButton){
            JFXToggleButton toogle = (JFXToggleButton) event.getSource();
            if(toogle.isSelected()){
                System.out.println("toogled on");
                bluetoothConnection.tryToConnect();
            }else {
                System.out.println("toogled off");
                bluetoothConnection.disconnect();
            }
        }
    }
    public void keyboardModeBtnAction(ActionEvent event) {
        System.out.println("keyboard");
        loadScreen("/javafx/fxml/KeyboardModeScreen.fxml", event);
    }

    public void mouseModeBtnAction(ActionEvent event) {
        System.out.println("mouse");
        loadScreen("/javafx/fxml/MouseModeScreen.fxml", event);
    }

    public void hand3dModeBtnAction(ActionEvent event) {
        System.out.println("3d hand");
        loadScreen("/javafx/fxml/Hand3DScreen.fxml", event);
    }

    public void chartsBtnAction(ActionEvent event) {
        System.out.println("charts");
        loadScreen("/javafx/fxml/ChartsScreen.fxml", event);

    }

    public void futureAppsBtnAction(ActionEvent event) {
        System.out.println("future");
        loadScreen("/javafx/fxml/OtherApps.fxml", event);


    }

    public void reactionTestWithFruFruBtnAction(ActionEvent event) {
        loadScreen("/javafx/fxml/ReactionTestWithFruFru.fxml", event);


    }
    public void reactionTestBtnAction(ActionEvent event) {
        loadScreen("/javafx/fxml/ReactionTest.fxml", event);
    }

    public void loadScreen(String fxml, Event event) {
        if (((JFXButton) event.getSource()).getStyleClass().contains("selected")) {
            System.out.println("Screen already Selected");
            return;
        }
        selectButton(event);
        container.getChildren().clear();
        try {
            container.getChildren().add(FXMLLoader.load(getClass().getResource(fxml)));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void selectButton(Event event) {


        for (Node n : buttonsContainer.getChildren()) {
            if (n instanceof JFXButton) {
                JFXButton b = (JFXButton) n;
                if (b == event.getSource()) {
                    b.getStyleClass().removeAll("normal-menu-button");
                    b.getStyleClass().add("selected");
                } else {
                    b.getStyleClass().removeAll("selected");
                    b.getStyleClass().add("normal-menu-button");
                }
            }

        }
    }

    @Override
    public void connectionFailure() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btStatus.setText("Disconnected\nFailed to Connect");
                btToogle.setSelected(false);
            }
        });

    }

    @Override
    public void bluetoothConnected() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btStatus.setText("Connected");
                btToogle.setSelected(true);
            }
        });


    }

    @Override
    public void bluetoothDisconnected() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btStatus.setText("Disconnected");
                btToogle.setSelected(false);
            }
        });


    }

    @Override
    public void connecting() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btStatus.setText("Connecting");
                btToogle.setSelected(true);
            }
        });



    }



}
