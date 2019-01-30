package javafx;

import bluetooth.BluetoothConnection;
import bluetooth.IBlutoothInfoScreen;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import configs.PreferenceUtils;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import uncoupledprograms.DataSaveCsv;

import javax.swing.*;
import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.util.*;

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

    public void maximize(MouseEvent mouseEvent) {
        Stage st= (Stage)container.getScene().getWindow();
        if (st.isFullScreen()){
            st.setFullScreen(false);
        }else {
            st.setFullScreen(true);
        }
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


    public void toogleAction(ActionEvent event) {
        if (event.getSource() instanceof JFXToggleButton) {
            JFXToggleButton toogle = (JFXToggleButton) event.getSource();
            if (toogle.isSelected()) {
                System.out.println("toogled on");
                bluetoothConnection.tryToConnect(new PreferenceUtils().getUsedGlove());
            } else {
                System.out.println("toogled off");
                bluetoothConnection.disconnect();
            }
        }
    }

    public void keyboardModeBtnAction(ActionEvent event) {
        System.out.println("keyboard");
        loadScreen("fxml/KeyboardModeScreen.fxml", event);
    }

    public void mouseModeBtnAction(ActionEvent event) {
        System.out.println("mouse");
        loadScreen("fxml/MouseModeScreen.fxml", event);
    }

    public void hand3dModeBtnAction(ActionEvent event) {
        System.out.println("3d hand");
        loadScreen("fxml/Hand3DScreen.fxml", event);
    }

    public void chartsBtnAction(ActionEvent event) {
        System.out.println("charts");
        loadScreen("fxml/ChartsScreen.fxml", event);

    }

    public void  fingerChooseModeBtnAction(ActionEvent event) {
        loadScreen("fxml/FingerChooseScreen.fxml", event);;

    }

    public void pathBtnAction(ActionEvent event) {
        System.out.println("path");
        loadScreen("fxml/FollowThePath2Screen.fxml", event);
    }

    public void reactionTestBtnAction(ActionEvent event) {

        System.out.println("reaction");
        loadScreen("fxml/ReactionTest.fxml", event);
    }

    public void configsBtnAction(Event event) {
        List<String> dialogData = new ArrayList<>();
        dialogData.add("LUVAMOUSE");
        dialogData.add("LuvaGestos");
        PreferenceUtils preferenceUtils = new PreferenceUtils();

        ChoiceDialog<String> dialog = new ChoiceDialog<>(preferenceUtils.getUsedGlove(), dialogData);

        dialog.setTitle("Glove selection");
        dialog.setHeaderText("Select your using glove");
        Optional<String> result = dialog.showAndWait();
        String selected = "cancelled.";

        if (result.isPresent()) {
            selected = result.get();
            preferenceUtils.saveUsedGlove(selected);
        }





    }

    private void loadScreen(String fxml, Event event) {
        if (((Node) event.getSource()).getStyleClass().contains("selected")) {
            System.out.println("Screen already Selected");
            return;
        }
        selectButton(event);
        container.getChildren().clear();
        try {

            container.getChildren().add(FXMLLoader.load(getClass().getClassLoader().getResource(fxml)));
        } catch (IOException | NullPointerException e) {
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
    public void connectionFailure(String m) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btStatus.setText("Disconnected\nFailed to Connect\n"+m);
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
                btToogle.setToggleColor(Color.valueOf("#006e1f"));
                btToogle.setToggleLineColor(Color.valueOf("#00c96b"));
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
                btToogle.setToggleColor(Color.valueOf("#2b71dc"));
                btToogle.setToggleLineColor(Color.valueOf("#5398ff"));
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
