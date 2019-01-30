package uncoupledprograms.pconly;

import javafx.FingerChooseScreenController;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uncoupledglovedatathings.GloveSensors;
import uncoupledglovedatathings.MyColors;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class FingerChoose {



    private int nErrors =0;
    private int prevFinger;
    private MyColors[] colors = new MyColors[6];
    boolean running = false;
    private Thread runningMeasureThread=null;
    private static FingerChoose ME = null;
    private int currentFinger;
    Set<Integer> neutralFingers;
    private Canvas canvas;
    private Stage stage;
    private Scene scene;
    private ArrayList<Rectangle> fingers;

    public void changeColor(MyColors myColors, Shape s) {
        Color color;
        switch (myColors) {
            case BLUE:
                color = new Color(0, 0, 1, 0.8);
                break;
            case RED:
                color = new Color(1, 0, 0, 0.8);
                break;
            case PINK:
                color = new Color(1, 0, 1, 0.8);
                break;
            case GREEN:
                color = new Color(0, 1, 0, 0.8);
                break;
            case PURPLE:
                color = new Color(0.5, 0, 0.5, 0.8);
                break;
            case YELLOW:
                color = new Color(1, 1, 0, 0.8);
                break;
            case TRANSPARENT:
            default:
                color = new Color(1, 1, 1, 0);


        }

        s.setFill(color);
    }
    public void drawFingers(Group root){
        double cx=Screen.getPrimary().getVisualBounds().getWidth()/2;
        double cy=Screen.getPrimary().getVisualBounds().getHeight()/2;
        Ellipse hand = new Ellipse( cx+50,cy+150, 159,107);
        Rectangle thumb= new Rectangle(cx-150,cy,46.0,160.0);
        Rectangle index = new Rectangle(cx-60,cy-100,46.0,160.0);
        Rectangle middle = new Rectangle(cx,cy-100,46.0,160.0);
        Rectangle ring = new Rectangle(cx+60,cy-100,46.0,160.0);
        Rectangle little = new Rectangle(cx+120,cy-100,46.0,160.0);
        fingers=new ArrayList<Rectangle>() {{
            add(thumb);
            add(index);
            add(middle);
            add(ring);
            add(little);
        }};


           // finger.setRotate(90);
        root.getChildren().addAll(thumb,index,middle,ring,little);
        root.getChildren().add(hand);
        thumb.setRotate(120);
        for (Shape s: fingers) {
            changeColor(MyColors.BLUE, s);

        }
        changeColor(MyColors.BLUE,hand);
    }

    private int absRand(int n) {

        Random rn = new Random();
        int i = rn.nextInt() % n;
        if (i < 0) {
            return -i;
        }
        return i;
    }

    public void fullScreen()  {

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        Group root = new Group();
        canvas = new Canvas(Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
        root.getChildren().add(canvas);
        drawFingers(root);
        canvas.setLayoutX(1000);
        canvas.toFront();
        stage = new Stage();
        scene = new Scene(root, gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight());
        stage.setFullScreen(true);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
    public static FingerChoose getInstance() {
        if (ME == null)
            ME = new FingerChoose();
        return ME;
    }

    private FingerChoose() {
        colors[0] = MyColors.RED;
        colors[1] = MyColors.GREEN;
        colors[2] = MyColors.BLUE;
        colors[3] = MyColors.YELLOW;
        colors[4] = MyColors.PURPLE;
        colors[5] = MyColors.PINK;

    }

    private FingerChooseScreenController reactTestScreen;

    public void start(FingerChooseScreenController reactTestScreen, int nMov) {

        currentFinger=-1;
        prevFinger=-1;
        nErrors =0;


        this.reactTestScreen = reactTestScreen;
        if (!isRunning()) {
            running = true;
            fingerChoose(nMov);

        }

    }
    private double getMovement(int index) {
        double movent = GloveSensors.getInstance().getFingerAt(index).getAx().get(GloveSensors.getInstance().getFingerAt(index).getAx().size() - 2) -
                GloveSensors.getInstance().getFingerAt(index).getAx().get(GloveSensors.getInstance().getFingerAt(index).getAx().size() - 1);
        if (movent < 0) {
            movent *= -1;
        }
        return movent;
    }

    public void stop() {
        running = false;
        reactTestScreen.showNotRunningMessage();
        if(runningMeasureThread!=null){
            runningMeasureThread.interrupt();
            System.out.println("interrup +"+runningMeasureThread);

        }
    }

    public boolean isRunning() {
        return running;
    }


    private void chooseFinger(){
        neutralFingers=new HashSet<>();
        neutralFingers.add(currentFinger);

        currentFinger=absRand(5);
        if(currentFinger==prevFinger) {
            currentFinger=(currentFinger+1)%5;
        }

        if(prevFinger!=-1) {
            changeColor(MyColors.BLUE, fingers.get(prevFinger));
        }

        changeColor(MyColors.RED,fingers.get(currentFinger));
        prevFinger=currentFinger;

    }


    boolean movedFinger(){

        boolean movedCorrect=false;

        for (int i=0;i<5;i++){
            if(neutralFingers.contains(i)){
                continue;
            }
            if(getMovement(i)>4 && i==currentFinger){
                movedCorrect=true;
                reactTestScreen.showSucessAlert();
            }else if (getMovement(i)>6){
               reactTestScreen.showErrorAlert();
                neutralFingers.add(i);
                nErrors++;
            }
        }
        return movedCorrect;
    }


    private void fingerChoose(int nMov) {




        fullScreen();
        Task<Void> sleeper = null;
        sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("deu m**** na Thread measure_event");
                }
                return null;
            }
        };

        Task<ArrayList<Long>> measureTime = new Task< ArrayList<Long>>() {
            @Override
            protected ArrayList<Long> call() throws Exception {
                 ArrayList<Long> report = new ArrayList<Long>();

                int count = 0;


                do{
                    count++;
                    chooseFinger();
                    long begin = System.currentTimeMillis();
                    while (!movedFinger()) {

                        Thread.sleep(10);
                    }
                    report.add(System.currentTimeMillis()-begin);

                }while(count<nMov);





                return report;
            }
        };
        measureTime.setOnSucceeded(event -> {

            setStatus(measureTime.getValue(),nErrors);
            running = false;
            stage.close();
        });

        sleeper.setOnSucceeded(event -> {

            runningMeasureThread=new Thread(measureTime);
            runningMeasureThread.start();
        });

        Thread sleep = new Thread(sleeper);
        sleep.start();


    }
    void setStatus(ArrayList<Long> times,int nErrors){
        String s="Number of errors: "+nErrors+"\n";
        int i=0;
        for (long time:times){
            i++;
            s+="Time "+i+": "+time+"ms\n";
        }

        reactTestScreen.setStatus(s);



    }
}
