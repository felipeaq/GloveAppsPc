package javafx;

import bluetooth.BluetoothConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import uncoupledprograms.pconly.MouseFunction;

import java.net.URL;
import java.util.ResourceBundle;

public class MouseModeScreenController implements Initializable {

    @FXML
    public Label status;

    private MouseFunction mouseFunction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mouseFunction = new MouseFunction();
    }

    public void start(ActionEvent event) {
        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();
        if (btStatus.isConnected()) {
            mouseFunction.start();
            status.setText("Running");
        } else {
            showBluetoothDisconnectedAlert();
        }
    }

    public void stop(ActionEvent event) {
        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();
        if (btStatus.isConnected()) {
            mouseFunction.stop();
            status.setText("Not Running");
        } else {
            showBluetoothDisconnectedAlert();
        }

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
}
