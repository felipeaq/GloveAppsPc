package javafx;

import bluetooth.BluetoothConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import uncoupledprograms.pconly.VirtualHandContolFunction;

import java.net.URL;
import java.util.ResourceBundle;

public class ChartsScreenController implements Initializable {

    @FXML
    public LineChart gyroX, gyroY, gyroZ;

    private VirtualHandContolFunction virtualHandContolFunction;


        Thread updateThread;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        virtualHandContolFunction = new VirtualHandContolFunction();
        XYChart.Series<Number, Number> seriesXS1G = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesXS2G = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesXS3G = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesXS4G = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesXS5G = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesXS6G = new XYChart.Series<>();


        XYChart.Series<Number, Number> seriesYS1G = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesYS2G = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesYS3G = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesYS4G = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesYS5G = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesYS6G = new XYChart.Series<>();

        XYChart.Series<Number, Number> seriesZS1G = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesZS2G = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesZS3G = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesZS4G = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesZS5G = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesZS6G = new XYChart.Series<>();

        XYChart.Series<Number, Number> seriesXS1A = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesXS2A = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesXS3A = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesXS4A = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesXS5A = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesXS6A = new XYChart.Series<>();


        XYChart.Series<Number, Number> seriesYS1A = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesYS2A = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesYS3A = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesYS4A = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesYS5A = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesYS6A = new XYChart.Series<>();

        XYChart.Series<Number, Number> seriesZS1A = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesZS2A = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesZS3A = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesZS4A = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesZS5A = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesZS6A = new XYChart.Series<>();




        gyroX.getData().add(seriesXS1G);
        gyroX.getData().add(seriesXS2G);
        gyroX.getData().add(seriesXS3G);
        gyroX.getData().add(seriesXS4G);
        gyroX.getData().add(seriesXS5G);
        gyroX.getData().add(seriesXS6G);

        gyroY.getData().add(seriesYS1G);
        gyroY.getData().add(seriesYS2G);
        gyroY.getData().add(seriesYS3G);
        gyroY.getData().add(seriesYS4G);
        gyroY.getData().add(seriesYS5G);
        gyroY.getData().add(seriesYS6G);

        gyroZ.getData().add(seriesZS1G);
        gyroZ.getData().add(seriesZS2G);
        gyroZ.getData().add(seriesZS3G);
        gyroZ.getData().add(seriesZS4G);
        gyroZ.getData().add(seriesZS5G);
        gyroZ.getData().add(seriesZS6G);


//        accX.getData().add(seriesXS1A);
//        accX.getData().add(seriesXS2A);
//        accX.getData().add(seriesXS3A);
//        accX.getData().add(seriesXS4A);
//        accX.getData().add(seriesXS5A);
//        accX.getData().add(seriesXS6A);

//        accY.getData().add(seriesYS1A);
//        accY.getData().add(seriesYS2A);
//        accY.getData().add(seriesYS3A);
//        accY.getData().add(seriesYS4A);
//        accY.getData().add(seriesYS5A);
//        accY.getData().add(seriesYS6A);

//        accZ.getData().add(seriesZS1A);
//        accZ.getData().add(seriesZS2A);
//        accZ.getData().add(seriesZS3A);
//        accZ.getData().add(seriesZS4A);
//        accZ.getData().add(seriesZS5A);
//        accZ.getData().add(seriesZS6A);

        updateThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(100);
                    System.out.println("algo");
                    Platform.runLater(() -> {
                        //appendData(seriesXS1G, i%3);
                        int j=incrementAndGet();


                        appendData(seriesXS1G, j,i % 10);
                        appendData(seriesXS2G, j,i % 2);
                        appendData(seriesXS3G, j,i % 3);
                        appendData(seriesXS4G, j,i % 4);
                        appendData(seriesXS5G, j,i % 5);
                        appendData(seriesXS6G, j,i % 6);

                        appendData(seriesYS1G, j,i % 10);
                        appendData(seriesYS2G, j,i % 2);
                        appendData(seriesYS3G, j,i % 3);
                        appendData(seriesYS4G, j,i % 4);
                        appendData(seriesYS5G, j,i % 5);
                        appendData(seriesYS6G, j,i % 6);

                        appendData(seriesZS1G, j,i % 10);
                        appendData(seriesZS2G, j,i % 2);
                        appendData(seriesZS3G, j,i % 3);
                        appendData(seriesZS4G, j,i % 4);
                        appendData(seriesZS5G, j,i % 5);
                        appendData(seriesZS6G, j,i % 6);
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        updateThread.start();

    }

    private void appendData(XYChart.Series<Number, Number> serie, double x, double y) {
        serie.getData().add(new XYChart.Data<>(x, y));
        if (serie.getData().size()>300)
            serie.getData().remove(0);
    }

    int i;
    private int incrementAndGet() {
        System.out.println(i+1  );
        return Integer.parseInt(String.valueOf(++i));
    }

    public void start(ActionEvent event) {
        updateThread.start();

//        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();
//        if (btStatus.isConnected()) {
//            virtualHandContolFunction.start();
//            status.setText("Running");
//        } else {
//            showBluetoothDisconnectedAlert();
//        }
 }

    public void stop(ActionEvent event) {
        updateThread.interrupt();
//        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();
//        if (btStatus.isConnected()) {
//            virtualHandContolFunction.stop();
//            status.setText("Not Running");
//        } else {
//            showBluetoothDisconnectedAlert();
//        }

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
