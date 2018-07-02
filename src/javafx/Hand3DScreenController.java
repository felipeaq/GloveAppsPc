package javafx;

import bluetooth.BluetoothConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import uncoupledprograms.pconly.MouseFunction;
import uncoupledprograms.pconly.VirtualHandContolFunction;

import java.net.URL;
import java.util.ResourceBundle;

public class Hand3DScreenController implements Initializable {

    @FXML
    public Label status;

    private VirtualHandContolFunction virtualHandContolFunction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        virtualHandContolFunction = new VirtualHandContolFunction();
    }

    public void start(ActionEvent event) {
        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();
        if (btStatus.isConnected()) {
            virtualHandContolFunction.start();
            status.setText("Running");
        } else {
            showBluetoothDisconnectedAlert();
        }
    }

    public void stop(ActionEvent event) {
        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();
        if (btStatus.isConnected()) {
            virtualHandContolFunction.stop();
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
