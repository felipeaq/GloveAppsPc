package javafx;

import bluetooth.BluetoothConnection;
import bluetooth.IPostAppendScreen;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.events.Chart2DActionSetAxis;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.util.Range;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import uncoupledglovedatathings.GloveSensors;
import uncoupledprograms.pconly.VirtualHandContolFunction;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.TimerTask;

public class ChartsScreenController implements Initializable, IPostAppendScreen {


    public VBox accGraphsVbox;
    public VBox gyroGraphsVbox;
    Thread updateThread;

        Chart2D chart = new Chart2D();
    ITrace2D trace = new Trace2DLtd(500);
    ITrace2D trace2 = new Trace2DLtd(500);
    ITrace2D trace3 = new Trace2DLtd(500);
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        trace.setColor(Color.RED);
        trace2.setColor(Color.BLUE);
        trace3.setColor(Color.YELLOW);
        chart.addTrace(trace);
        chart.addTrace(trace2);
        chart.addTrace(trace3);
        chart.getAxisY(trace).setRange(new Range(-10, 10));
        SwingNode s = new SwingNode();
        createSwingContent(s, chart);
        accGraphsVbox.getChildren().add(s);
        updateThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
            }
        });
        updateThread.start();

        BluetoothConnection.schedulePostAppendRunnable(this);

    }




    private void createSwingContent(final SwingNode swingNode, JComponent f) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VBox.setVgrow(swingNode, Priority.ALWAYS);
                swingNode.setContent(f);
                f.setBounds(500, 500, 500, 500);
            }
        });
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

    @Override
    public Runnable getPostAppendRunnable() {
        return () -> Platform.runLater(()->{
            trace.addPoint(GloveSensors.getInstance().getSensor1().getAx().getRealSize(), GloveSensors.getInstance().getSensor1().getAx().lastElement());
            trace2.addPoint(GloveSensors.getInstance().getSensor2().getAx().getRealSize(), GloveSensors.getInstance().getSensor2().getAx().lastElement());
            trace3.addPoint(GloveSensors.getInstance().getSensor3().getAx().getRealSize(), GloveSensors.getInstance().getSensor3().getAx().lastElement());
        });
    }
}
