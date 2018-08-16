package uncoupledprograms.pconly;

import javafx.concurrent.Task;
import uncoupledglovedatathings.GloveSensors;
import uncoupledglovedatathings.MyColors;
import uncoupledglovedatathings.Triplet;

import java.util.ArrayList;
import java.util.Random;

public class ReactionTestFunction {


    private MyColors[] colors = new MyColors[6];
    boolean running = false;
    private Thread runningMeasureThread=null;
    private static ReactionTestFunction ME = null;

    public static ReactionTestFunction getInstance() {
        if (ME == null)
            ME = new ReactionTestFunction();
        return ME;
    }

    private ReactionTestFunction() {
        colors[0] = MyColors.RED;
        colors[1] = MyColors.GREEN;
        colors[2] = MyColors.BLUE;
        colors[3] = MyColors.YELLOW;
        colors[4] = MyColors.PURPLE;
        colors[5] = MyColors.PINK;

    }

    private IReactTestScreen reactTestScreen;

    public void start(IReactTestScreen reactTestScreen, boolean isMulticolor, int minTime, int maxTime, int numberOfColors) throws IllegalArgumentException {
        if (minTime >= maxTime) {
            throw new IllegalArgumentException("The minimum change time need to be greater than the maximum change time");
        }
        if (numberOfColors < 3 || numberOfColors > 6) {
            throw new IllegalArgumentException("The number of colors need to be greater or equals than 3 and smaller or equals than 6");
        }

        this.reactTestScreen = reactTestScreen;
        if (!isRunning()) {
            running = true;
            mesasure_event(isMulticolor, minTime, maxTime, numberOfColors);
        }

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


    private int absRand(int n) {

        Random rn = new Random();
        int i = rn.nextInt() % n;
        if (i < 0) {
            return -i;
        }
        return i;
    }


    private double getMovement() {
        double movent = GloveSensors.getInstance().getSensor2().getAx().get(GloveSensors.getInstance().getSensor2().getAx().size() - 2) -
                GloveSensors.getInstance().getSensor2().getAx().get(GloveSensors.getInstance().getSensor2().getAx().size() - 1);
        if (movent < 0) {
            movent *= -1;
        }
        return movent;
    }


    private int getRandom(int min, int max) {
        if (max < min) {
            int aux = max;
            max = min;
            min = aux;
        }


        Random rn = new Random();
        int i = (rn.nextInt() % (max - min));

        if (i < 0) {
            i *= -1;
        }
        return i + min;
    }

    private void mesasure_event(boolean isMulticolor, int minTime, int maxTime, int numberOfColors) {

        reactTestScreen.showReadyMessage("Move the indicator when\n the red color appears");
        Task<Void> sleeper = null;
        sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int i = getRandom(minTime, maxTime);
                try {
                    Thread.sleep(i);
                } catch (InterruptedException e) {
                    System.out.println("deu m**** na Thread measure_event");
                }
                return null;
            }
        };

        Task<Triplet<Integer, Integer, ArrayList<Long>>> measureTime = new Task<Triplet<Integer, Integer, ArrayList<Long>>>() {
            @Override
            protected Triplet<Integer, Integer, ArrayList<Long>> call() throws Exception {
                Triplet<Integer, Integer, ArrayList<Long>> report =
                        new Triplet<>(0, 0, new ArrayList<Long>());
                try {
                    double movent = 0;
                    int reactColorShown = 0;
                    int maxReactColorShown = 3;

                    if (isMulticolor) {
                        int absRandGenerated = absRand(numberOfColors);
                        while (reactColorShown < maxReactColorShown && !Thread.currentThread().isInterrupted()) {
                            System.out.println(Thread.currentThread().isInterrupted());
                            boolean moved = false;
                            int delay_time = getRandom(minTime, maxTime);
                            long start = System.nanoTime();
                            int auxRand = absRand(numberOfColors);
                            if (auxRand == absRandGenerated)
                                absRandGenerated = (auxRand + 1) % numberOfColors;
                            else
                                absRandGenerated = auxRand;

                            reactTestScreen.changeColor(colors[absRandGenerated]);
                            System.out.println("tipo ->" + absRandGenerated);
                            report.setSecond(report.getSecond() + 1);

                            long startTime = System.nanoTime();

                            while ((System.nanoTime() - start) / 1E6 < delay_time) {

                                if (!moved && getMovement() > 1.5) {
                                    moved = true;
                                    //TODO cor generica
                                    if (absRandGenerated != 0) {
                                        //error
                                        report.setFirst(report.getFirst() + 1);
                                        reactTestScreen.showErrorAlert();
                                        System.out.println("errors>>>>" + report.getFirst());
                                    } else {
                                        //sucess
                                        reactColorShown++;
                                        report.getThird().add(System.nanoTime() - startTime);
                                        reactTestScreen.showSucessAlert();
                                        System.out.println("positives>>>>>" + reactColorShown);
                                    }
                                }
                                Thread.sleep(1);
                            }
                            if (!moved && absRandGenerated == 0) {
                                report.getThird().add(10_000_000_000_000L);
                                reactColorShown++;
                            }
                        }
                        if (Thread.currentThread().isInterrupted())
                            return null;
                    } else {
                        reactTestScreen.changeColor(colors[0]);
                        long start_time = System.nanoTime();
                        while (movent < 1.5) {
                            movent = getMovement();
                            Thread.sleep(1);
                        }
                        report.getThird().add(System.nanoTime() - start_time);
                    }
                } catch (InterruptedException ignored) {
                }
                return report;
            }
        };
        measureTime.setOnSucceeded(event -> {
            reactTestScreen.changeColor(MyColors.RED);
            if (measureTime.getValue() == null) {
                reactTestScreen.changeColor(MyColors.TRANSPARENT);
                reactTestScreen.showNotRunningMessage();
            } else if (!isMulticolor) {

                long error = measureTime.getValue().getThird().get(0);
                reactTestScreen.showResult((int) (error / 1E6));

            } else {
                System.out.println("Total de erros " + measureTime.getValue().getFirst());
                System.out.println("Total de tentativas " + measureTime.getValue().getSecond());
                for (int i = 0; i < measureTime.getValue().getThird().size(); i++) {
                    measureTime.getValue().getThird().set(i, (long) (measureTime.getValue().getThird().get(i) / 1E6));
                }
                reactTestScreen.showResult(measureTime.getValue().getFirst(), measureTime.getValue().getSecond(), measureTime.getValue().getThird());
            }
            running = false;
            runningMeasureThread=null;
        });

        sleeper.setOnSucceeded(event -> {
            reactTestScreen.changeColor(MyColors.RED);
            System.out.println("aaa");
            runningMeasureThread=new Thread(measureTime);
            runningMeasureThread.start();
            System.out.println("aa+"+runningMeasureThread);
        });

        Thread sleep = new Thread(sleeper);
        sleep.start();


    }

}
