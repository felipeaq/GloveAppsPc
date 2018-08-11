package javafx;

import bluetooth.BluetoothConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;

import uncoupledglovedatathings.IPredictScreen;
import uncoupledprograms.SVC;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineStateError;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class KeyboardModeScreenController implements Initializable, IPredictScreen {
    @FXML
    public Label predictedText;
    @FXML
    public HBox predictedTextParent;
    private SVC svc;
    private javax.speech.synthesis.Synthesizer synthesizer = null; // TODO

    public void startPredict(ActionEvent event) {
        // lblState.setText("Starting...");
        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();
        if (btStatus.isConnected())
            svc.startPredict(this);
        else {
            showBluetoothDisconnectedAlert();
        }
    }

    public void stopPredict(ActionEvent event) {
        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();
        if (btStatus.isConnected()) {
            svc.stopPredict();
            synthesizer = null;
        } else {
            showBluetoothDisconnectedAlert();
        }
    }

    public void deleteLast(ActionEvent event) {
        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();
        if (btStatus.isConnected()) {
            String palavra = predictedText.getText();
            StringBuilder novaPalavra = new StringBuilder();
            if (palavra.length() > 0) {
                for (int i = 0; i < palavra.length() - 1; i++)
                    novaPalavra.append(palavra.charAt(i));
            }
            predictedText.setText(novaPalavra.toString());

        } else {
            showBluetoothDisconnectedAlert();
        }
    }

    public void clear(ActionEvent event) {
        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();
        if (btStatus.isConnected()) {
            predictedText.setText("");
        } else {
            showBluetoothDisconnectedAlert();
        }
    }

    public void speak(ActionEvent event) {
        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();
        if (btStatus.isConnected()) {
            speak();
        } else {
            showBluetoothDisconnectedAlert();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            svc = SVC.getInstance(new File("Files/data.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeTheSpeaker() {
        try {
            if (synthesizer != null)
                synthesizer.deallocate();
        } catch (EngineException | EngineStateError e) {
            // TODO Descobrir o erro
            e.printStackTrace();
        }
    }

    @Override
    public void getReadyToSpeak() {
        try {
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
            synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
        } catch (IllegalArgumentException | EngineException e1) {

            System.out.println("Erro ao criar o TTS: " + e1.getMessage());
        }
        try {
            synthesizer.allocate();
        } catch (EngineException | EngineStateError e1) {
            System.out.println("Erro ao alocar o TTS: " + e1.getMessage());
        }
    }

    @Override
    public void speak() {
        try {
            synthesizer.resume();
            synthesizer.speakPlainText(predictedText.getText(), null);
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
        } catch (AudioException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void appendCharaterToScreen(char character) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

        predictedText.setText(predictedText.getText() + character);
            }
        });

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
