package uncoupledprograms.pconly;

import uncoupledglovedatathings.GloveSensors;

import java.awt.*;
import java.awt.event.InputEvent;

@SuppressWarnings("Duplicates")
public class PathObjectMoveFunction {

    boolean work = false;

    private static PathObjectMoveFunction ME;

    private PathObjectMoveFunction() {
    }

    public static PathObjectMoveFunction getInstance() {
        if (ME == null) {
            ME = new PathObjectMoveFunction();
        }
        return ME;
    }

    public void start(IPathScreen pathScreen, int containerW, int containerH) {
        if (!isWorking()) {
            work = true;
            mouseMove(pathScreen, containerW, containerH);
        }
    }

    public void stop() {
        work = false;
    }

    public boolean isWorking() {
        return work;
    }

    private void mouseMove(IPathScreen pathScreen, int containerW, int containerH) {


        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        try {
            final Robot mouse = new Robot();


            Thread thread = new Thread() {
                public void run() {
                    int x = gd.getDisplayMode().getWidth() / 2;
                    int y = gd.getDisplayMode().getHeight() / 2;
                    int maxX = gd.getDisplayMode().getWidth();
                    int maxY = gd.getDisplayMode().getHeight();



                    double resto_pointerX = 0;
                    double resto_pointerY = 0;

                    int d = 1;
                    double alpha = 2;
                    int cont_restox = 0;
                    int cont_restoy = 0;

                    double delta_pointerX = 0;
                    double delta_pointerY = 0;

                    int y_anterior = 0;



                    mouse.mouseMove(x, y);

                    while (work) {

                        double deltaX2 = 0;//GloveSensors.getInstance().getSensor2().getAz().lastElement() * Math.cos(angY2) + GloveSensors.getInstance().getSensor2().getAy().lastElement() * Math.sin(angY2);

                        double deltaY2 =  GloveSensors.getInstance().getSensor2().getAy().lastElement();

                        if ((GloveSensors.getInstance().getSensor2().getAy().lastElement() - GloveSensors.getInstance().getSensor4().getAy().lastElement()) > 8 && d == 1) {
                            d = 2;
                        }
                        if ((GloveSensors.getInstance().getSensor2().getAy().lastElement() - GloveSensors.getInstance().getSensor4().getAy().lastElement()) < -8 && d == 2) {
                            d = 1;
                        }


                        double ST = 0.012;
                        if (Math.abs(deltaY2) > ST) {
                            // faixas de valores
                            if (Math.abs(deltaY2) < 0.4) {
                                delta_pointerY = deltaY2 * 4;
                            }
                            // Faixa 2
                            if (Math.abs(deltaY2) >= 0.4 && Math.abs(deltaY2) < 1.3) {
                                delta_pointerY = 4.625 * deltaY2 - 0.28125;
                            }
                            // Faixa 3
                            if (Math.abs(deltaY2) >= 1.3 && Math.abs(deltaY2) < 3.9) {
                                delta_pointerY = 6.9811 * deltaY2 - 3.22642;
                            }
                            // Faixa 4
                            if (Math.abs(deltaY2) >= 3.9) {
                                delta_pointerY = 15.12465 * deltaY2 - 34.9861;
                            }
                            delta_pointerY = delta_pointerY * alpha + resto_pointerY;

                            resto_pointerY = delta_pointerY - (int) delta_pointerY;
                            y = y + (int) delta_pointerY;
                            cont_restoy = cont_restoy + 1;

                            if (cont_restoy > 5) {
                                resto_pointerY = 0;
                                cont_restoy = 0;
                            }
                        }

                       
                        if (x > maxX) {
                            x = maxX;
                        }
                        if (x < 0) {
                            x = 0;
                        }
                        if (y > maxY) {
                            y = maxY;
                        }
                        if (y < 0) {
                            y = 0;
                        }


                        if (y != y_anterior) {
                            pathScreen.moveObject(y);
                        }
                        y_anterior = y;


                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }

                }
            };

            thread.start();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

}
