package javafx;

import bluetooth.BluetoothConnection;
import configs.PreferenceUtils;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;
import uncoupledprograms.pconly.IPathScreen;
import uncoupledprograms.pconly.PathObjectMoveFunction;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;


public class FollowThePath2ScreenController implements Initializable, IPathScreen {

    @FXML
    public ImageView generatedImage;
    public Label status;
    public Label selectedImage;
    public Label score;
    private BufferedImage bufferedImage;

    @FXML
    private Slider fps,pixelsJumped;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PreferenceUtils preferenceUtils = new PreferenceUtils();
        if (preferenceUtils.isDefaultPathImage())
            selectedImage.setText("Default Image");
        else
            selectedImage.setText(preferenceUtils.getLastUsedPathImage().split(":")[1]);
    }

    public void setScore(double scoreRate){
        this.score.setText("score: "+String.format("%.2f", scoreRate)+"%");
    }

    public void setScore(){
        this.score.setText("score: ");
    }

    @FXML
    public void start(ActionEvent event) {


        if (BluetoothConnection.getBluetoothStatus().isConnected()) {
            PathObjectMoveFunction.getInstance().start(this, 800, 510);
            status.setText("Running");
            PathScene.getInstance().start(this, new PreferenceUtils().getLastUsedPathImage(),(int)fps.getValue(),
                    (int)pixelsJumped.getValue());
        } else {
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
        PathScene.getInstance().moveObject(y);


    }

    @Override
    public void setCreatedImage(BufferedImage combined) {

        this.bufferedImage = combined;
        if (Platform.isFxApplicationThread())
            generatedImage.setImage(SwingFXUtils.toFXImage(combined, null));
        else
            Platform.runLater(() -> generatedImage.setImage(SwingFXUtils.toFXImage(combined, null)));
    }

    @Override
    public void serviceStopped() {
        if (Platform.isFxApplicationThread())
            status.setText("Not Running");
        else
            Platform.runLater(() -> status.setText("Not Running"));

    }

    public void saveImage(ActionEvent event) {
        if (bufferedImage == null) {
            showError("No Generated Image", "You must run the test at least once.");

        }
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("PNG file", "*.png"));
            ImageIO.write(bufferedImage, "png", fileChooser.showSaveDialog(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectPathImage(ActionEvent event) throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Image file", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        String extension = FilenameUtils.getExtension(file.toURI().toURL().toString());
        if (!extension.equalsIgnoreCase("png")) {
            if (!extension.equalsIgnoreCase("jpg")) {
                if (!extension.equalsIgnoreCase("jpeg")) {
                    showError("Archive Error", "The selected file isn't an image. Selean an PNG, JPG or JPEG image");
                } else {
                    selectedImage.setText(file.toURI().toURL().toString().split(":")[1]);
                    new PreferenceUtils().setLastUsedPathImage(file.toURI().toURL().toString());
                }
            } else {
                selectedImage.setText(file.toURI().toURL().toString().split(":")[1]);
                new PreferenceUtils().setLastUsedPathImage(file.toURI().toURL().toString());
            }
        } else {
            selectedImage.setText(file.toURI().toURL().toString().split(":")[1]);
            new PreferenceUtils().setLastUsedPathImage(file.toURI().toURL().toString());
        }
    }

    private void showError(String title, String contenctText) {
        Alert alert = new Alert(
                Alert.AlertType.WARNING,
                contenctText
        );
        alert.setTitle(title);
        alert.show();
    }

    public void useDefaultImage(ActionEvent event) {
        new PreferenceUtils().setDefaultPathImage();
        selectedImage.setText("Default Image");
    }
}
