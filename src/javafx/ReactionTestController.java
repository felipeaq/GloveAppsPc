package javafx;

import bluetooth.BluetoothConnection;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import uncoupledglovedatathings.GloveSensors;


import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;


public class ReactionTestController implements Initializable {

    @FXML
    public Label status;






    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void start(ActionEvent event) {

        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();
        boolean x=true;

        if (btStatus.isConnected()) {

            mesasure_event();

        } else {
            showBluetoothDisconnectedAlert();
        }



    }

    public void stop(ActionEvent event) {
        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();
        if (btStatus.isConnected()) {

            status.setText("Not Running");
        } else {
            showBluetoothDisconnectedAlert();
        }

    }


    void mesasure_event(){
        status.setText("Preparar...");

        Task<Void> sleeper = new Task<Void>() {
            Random rn = new Random();






            @Override
            protected Void call() throws Exception {
                int i = (rn.nextInt() % 3000);

                if(i<0){
                    i*=-1;
                }
                i+=2000;
                try {
                    System.out.println("inicio "+i);
                    Thread.sleep(i);
                    System.out.println("Fim");

                } catch (InterruptedException e) {
                    System.out.println("deu merda na Thread measure_event");

                }
                return null;
            }

        };




        Task<Long> measureTime = new Task<Long>() {


            @Override
            protected Long call() throws Exception {
                final long start_time = System.nanoTime();
                try {

                    double movent=0;

                    while (movent < 1.5) {
                        movent=GloveSensors.getInstance().getSensor2().getAx().get(GloveSensors.getInstance().getSensor2().getAx().size() - 2)-
                                GloveSensors.getInstance().getSensor2().getAx().get(GloveSensors.getInstance().getSensor2().getAx().size() - 1);
                        if (movent < 0) {
                            movent *= -1;
                        }
                        Thread.sleep(1);
                    }

                } catch (InterruptedException e) {
                }

                return System.nanoTime()-start_time;
            }

        };
        measureTime.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println(measureTime.getValue());

                status.setText("Terminou em "+measureTime.getValue()/1E6+"ms ! " );
                status.setStyle("-fx-color-label-visible: false ;");
            }
        });

        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override

            public void handle(WorkerStateEvent event) {
                status.setText("Agora!");
                status.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5); -fx-background-radius: 4;");
                System.out.println("aaa");
                new Thread(measureTime).start();
            }
        });


        //new Thread(sleeper).start()
        Thread sleep =  new Thread(sleeper);
        sleep.start();



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
