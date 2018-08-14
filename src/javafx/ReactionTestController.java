package javafx;

import bluetooth.BluetoothConnection;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import uncoupledglovedatathings.GloveSensors;

import java.util.ArrayList;


import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;


public class ReactionTestController implements Initializable {


    public class Tuple{
        public int errors=0;
        public int movements=0;
        public ArrayList<Long> times= new ArrayList();

    }



    @FXML
    public Label status;
    public CheckBox multicolor;

    public String[] colors=new String[4];
    boolean started=false;
    int absRand(int n){

        Random rn = new Random();
        int i=rn.nextInt()%n;
        if (i<0){
            return -i;
        }
        return i;
    }






    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colors[0]="-fx-background-color: rgba(255, 0, 0, 0.5); -fx-background-radius: 4;";
        colors[1]="-fx-background-color: rgba(0, 255, 0, 0.5); -fx-background-radius: 4;";
        colors[2]="-fx-background-color: rgba(0, 0, 255, 0.5); -fx-background-radius: 4;";
        colors[3]="-fx-background-color: rgba(255, 255, 255, 0.5); -fx-background-radius: 4;";

    }

    double getMovement(){
        double movent =GloveSensors.getInstance().getSensor2().getAx().get(GloveSensors.getInstance().getSensor2().getAx().size() - 2)-
                GloveSensors.getInstance().getSensor2().getAx().get(GloveSensors.getInstance().getSensor2().getAx().size() - 1);
        if (movent < 0) {
            movent *= -1;
        }
        return movent;
    }

    public void start(ActionEvent event) {

        BluetoothConnection.BluetoothStatus btStatus = BluetoothConnection.getBluetoothStatus();
        System.out.println(multicolor.isSelected());

        if (btStatus.isConnected() && !started) {
            started=true;
            mesasure_event();
            started=false;
        } else if (!btStatus.isConnected()) {
            showBluetoothDisconnectedAlert();
        }else{
            System.out.println("inicie apenas uma vez "+started);
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


    int getRandom(int min, int max){
        if (max<min){
            int aux=max;
            max=min;
            min=aux;
        }


        Random rn = new Random();
        int i = (rn.nextInt() % (max-min));

        if(i<0){
            i*=-1;
        }
        return i+min;
    }
    void mesasure_event(){
        status.setText("Move the indicator when\n the red color appears");




        Task<Void> sleeper= null;

            sleeper = new Task<Void>() {


                @Override
                protected Void call() throws Exception {
                    int i = getRandom(2000, 5000);
                    try {

                        Thread.sleep(i);

                    } catch (InterruptedException e) {
                        System.out.println("deu merda na Thread measure_event");

                    }
                    return null;
                }

            };




        Task<Tuple> measureTime = new Task<Tuple>() {


            @Override
            protected Tuple  call() throws Exception {
                Tuple report=new Tuple();
                try {


                    double movent=0;

                    int countPositives=0;



                    if(multicolor.isSelected()){
                        int i = absRand(4);

                         //TODO escolher cor
                        while(countPositives<3){


                            boolean moved=false;
                            int delay_time=getRandom(2000, 5000);
                            long start = System.nanoTime();
                            int auxRand = absRand(4);
                            if (auxRand==i)
                                //garante que não vai repetir a cor
                                i = (auxRand+1)%4;
                            else
                                i=auxRand;

                            status.setStyle(colors[i]);
                            System.out.println("tipo ->"+i);
                            report.movements++;
                            long startTime = System.nanoTime();

                            //TODO esperar tempo de dalay
                            while((System.nanoTime()-start)/1E6<delay_time){

                                if (!moved && getMovement()>1.5) {
                                    moved = true;
                                    if(i!=0) {

                                        report.errors++;
                                        System.out.println("errors>>>>"+report.errors);
                                    }else {
                                        countPositives++;
                                        report.times.add(System.nanoTime()-startTime);
                                        System.out.println("positives>>>>>"+countPositives);
                                    }
                                }
                                Thread.sleep(1);

                            }
                            //TODO cor generica
                            if (!moved &&i==0){
                                //caso não tenha movido adciona um tempo alto
                                report.times.add(10_000_000_000L);
                                countPositives++;

                            }

                        }
                    }else {
                        status.setStyle(colors[0]);
                        long start_time=System.nanoTime();
                        while (movent < 1.5) {
                            movent = getMovement();
                            Thread.sleep(1);
                        }
                        report.times.add(System.nanoTime()-start_time);



                    }


                } catch (InterruptedException e) {
                }

                return report;
            }

        };
        measureTime.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                status.setStyle("-fx-color-label-visible: false ;");
                if (!multicolor.isSelected()) {
                    //System.out.println(measureTime.getValue());

                    long error = measureTime.getValue().times.get(0);

                    status.setText("Terminou em " + (int) (error / 1E6) + "ms ! ");

                }else{
                    System.out.println("Total de erros "+measureTime.getValue().errors);
                    System.out.println("Total de tentativas "+ measureTime.getValue().movements);
                    status.setText("Results in report!");
                    for (long time:measureTime.getValue().times) {
                        System.out.println(time/1E6);
                    }
                }
            }
        });



            sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override

                public void handle(WorkerStateEvent event) {
                    //status.setText("Agora!");
                    status.setStyle(colors[0]);
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
