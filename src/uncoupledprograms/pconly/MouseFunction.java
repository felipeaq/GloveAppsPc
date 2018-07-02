package uncoupledprograms.pconly;

import uncoupledglovedatathings.GloveSensors;

import java.awt.*;
import java.awt.event.InputEvent;

public class MouseFunction {

    boolean work = false;

    public void start() {
        if (!isWorking()) {
            work = true;
            mouseMove();
        }
    }

    public void stop() {
        work = false;
    }

    public boolean isWorking() {
        return work;
    }

    private void mouseMove() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        try {
            final Robot mouse = new Robot();


            Thread thread = new Thread() {
                public void run() {
                    int x = gd.getDisplayMode().getWidth() / 2;
                    int y = gd.getDisplayMode().getHeight() / 2;
                    int maxX = gd.getDisplayMode().getWidth();
                    int maxY = gd.getDisplayMode().getHeight();


                    boolean parado = false;
                    boolean clique = false;
                    boolean release = false;
                    boolean released = false;
                    boolean cliqued = false;
                    double resto_pointerx = 0;
                    double resto_pointerX = 0;
                    double resto_pointerY = 0;

                    int d = 1;
                    double alpha = 2;
                    int cont = 0;
                    int cont_restox = 0;
                    int cont_restoy = 0;
                    double threshold_parada = 2.7;
                    double threshold_clique = 5;
                    double delta_pointerX = 0;
                    double delta_pointerY = 0;
                    //double deltaX2=0;
                    int x_anterior = 0;
                    int y_anterior = 0;
                    boolean continua_leitura = true;

                    double x1_inicial = GloveSensors.getInstance().getSensor1().getGx().lastElement();
                    double y1_inicial = GloveSensors.getInstance().getSensor1().getGy().lastElement();
                    double z1_inicial = GloveSensors.getInstance().getSensor1().getGz().lastElement();
                    double x2_inicial = GloveSensors.getInstance().getSensor2().getGx().lastElement();
                    double y2_inicial = GloveSensors.getInstance().getSensor2().getGy().lastElement();
                    double z2_inicial = GloveSensors.getInstance().getSensor2().getGz().lastElement();
                    double x4_inicial = GloveSensors.getInstance().getSensor4().getGx().lastElement();
                    double y4_inicial = GloveSensors.getInstance().getSensor4().getGy().lastElement();
                    double z4_inicial = GloveSensors.getInstance().getSensor4().getGz().lastElement();


                    mouse.mouseMove(x, y);

                    while (work) {
                        double angX1 = GloveSensors.getInstance().getSensor1().getGx().lastElement() - x1_inicial;
                        double angY1 = GloveSensors.getInstance().getSensor1().getGy().lastElement() - y1_inicial;
                        double angZ1 = GloveSensors.getInstance().getSensor1().getGz().lastElement() - z1_inicial;
                        double angX2 = GloveSensors.getInstance().getSensor2().getGx().lastElement() - x2_inicial;
                        double angY2 = GloveSensors.getInstance().getSensor2().getGy().lastElement() - y2_inicial;
                        double angZ2 = GloveSensors.getInstance().getSensor2().getGz().lastElement() - z2_inicial;
                        double angX4 = GloveSensors.getInstance().getSensor4().getGx().lastElement() - x4_inicial;
                        double angY4 = GloveSensors.getInstance().getSensor4().getGy().lastElement() - y4_inicial;
                        double angZ4 = GloveSensors.getInstance().getSensor4().getGz().lastElement() - z4_inicial;

                        double deltaY1 = -GloveSensors.getInstance().getSensor1().getAz().lastElement() * Math.sin(angY1) + GloveSensors.getInstance().getSensor1().getAy().lastElement() * Math.cos(angY1);
                        double deltaX1 = GloveSensors.getInstance().getSensor1().getAz().lastElement() * Math.cos(angY1) + GloveSensors.getInstance().getSensor1().getAy().lastElement() * Math.sin(angY1);
                        double deltaY2 = -GloveSensors.getInstance().getSensor2().getAz().lastElement() * Math.sin(angY2) + GloveSensors.getInstance().getSensor2().getAy().lastElement() * Math.cos(angY2);
                        double deltaX2 = GloveSensors.getInstance().getSensor2().getAz().lastElement() * Math.cos(angY2) + GloveSensors.getInstance().getSensor2().getAy().lastElement() * Math.sin(angY2);
                        double deltaY4 = -GloveSensors.getInstance().getSensor4().getAz().lastElement() * Math.sin(angY4) + GloveSensors.getInstance().getSensor4().getAy().lastElement() * Math.cos(angY4);
                        double deltaX4 = GloveSensors.getInstance().getSensor4().getAz().lastElement() * Math.cos(angY4) + GloveSensors.getInstance().getSensor4().getAy().lastElement() * Math.sin(angY4);
                        //double angY = y2(j);
                        //double angZ = z2(j);
                        deltaX1 *= -1;
                        deltaY1 *= -1;

                        if ((GloveSensors.getInstance().getSensor2().getAy().lastElement() - GloveSensors.getInstance().getSensor4().getAy().lastElement()) > 8 && d == 1) {
                            d = 2;
                        }
                        if ((GloveSensors.getInstance().getSensor2().getAy().lastElement() - GloveSensors.getInstance().getSensor4().getAy().lastElement()) < -8 && d == 2) {


                            d = 1;
                        }

                        if ((deltaY1 - deltaY4) < -threshold_parada && !parado) {
                            parado = true;
                            System.out.println("Mouse parado");
                        }


                        if ((deltaY1 - deltaY4) > threshold_parada && parado) {
                            parado = false;
                            System.out.println("Mouse em movimenento");
                        }


                        // clique
                        //threshold_clique = 5;

                        //if (Math.abs(deltaay12)>3)  // verifica o movimento relativo do dedo em rela��o � m�o
                        //if (GloveSensors.getInstance().getSensor2().getAy().lastElement()>threshold_clique && parado)
                        if ((deltaY2 - deltaY4) > threshold_clique && parado) {
                            clique = true;
                            //cliqued = 1;
                            //mouse.mousePress(InputEvent.BUTTON1_MASK);
                        }


                        //if (GloveSensors.getInstance().getSensor2().getAy().lastElement()<-threshold_clique && parado)
                        if ((deltaY2 - deltaY4) < -threshold_clique && parado) {
                            //clique = 0;
                            release = true;
                        }


                        if (!parado) {
                            double ST = 0.01;
                            //double delta_pointerY;
                            if (Math.abs(deltaY2) > ST) {
                                // faixas de valores
                                // Faixa 1
                                if (Math.abs(deltaY2) < 0.4) {
                                    delta_pointerY = deltaY2 * 4;
                                    //System.out.println('Faixa 1');
                                }
                                // Faixa 2
                                if (Math.abs(deltaY2) >= 0.4 && Math.abs(deltaY2) < 1.3) {
                                    delta_pointerY = 4.625 * deltaY2 - 0.28125;
                                    //System.out.println('Faixa 2');
                                }
                                // Faixa 3
                                if (Math.abs(deltaY2) >= 1.3 && Math.abs(deltaY2) < 3.9) {
                                    delta_pointerY = 6.9811 * deltaY2 - 3.22642;
                                    //System.out.println('Faixa 3');
                                }
                                // Faixa 4
                                if (Math.abs(deltaY2) >= 3.9) {
                                    delta_pointerY = 15.12465 * deltaY2 - 34.9861;
                                    //System.out.println('Faixa 4');
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

                            // Faixa 1
                            if (Math.abs(deltaX2) > ST) {
                                if (Math.abs(deltaX2) < 0.4) {
                                    delta_pointerX = deltaX2 * 4;
                                }
                                // Faixa 2
                                if (Math.abs(deltaX2) >= 0.4 && Math.abs(deltaX2) < 1.3) {
                                    delta_pointerX = 4.625 * deltaX2 - 0.28125;
                                }
                                // Faixa 3
                                if (Math.abs(deltaX2) >= 1.3 && Math.abs(deltaX2) < 3.9) {
                                    delta_pointerX = 6.9811 * deltaX2 - 3.22642;
                                    //System.out.println('Faixa3')
                                }
                                // Faixa 4
                                if (Math.abs(deltaX2) >= 3.9) {
                                    delta_pointerX = 15.12465 * deltaX2 - 34.9861;
                                    //System.out.println('Faixa4')
                                }
                                delta_pointerX = delta_pointerX * (1.7 * alpha) + resto_pointerX;
                                resto_pointerX = delta_pointerX - (int) delta_pointerX;
                                x = x - (int) delta_pointerX;
                                cont_restox = cont_restox + 1;


                                if (cont_restox >= 2) {

                                    resto_pointerx = 0;
                                    cont_restox = 0;
                                }


                            }

//			             if (deltaY2>ST && y<screenSize(4))
//			                 y=y+deltaY2*alpha;
//			             elseif(deltaY2<-ST && y>=0)
//			                 y=y+deltaY2*alpha;
//			             }
//
//			             if (Math.abs(deltaX2)>ST)
//			                 x=x-deltaX2*alpha;
//			             }
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
                        }


                        if (clique) {

                            if (!cliqued) {
                                mouse.mousePress(InputEvent.BUTTON1_MASK);
                                System.out.println("Mouse pressed");
                                cliqued = true;
                            }
                        } else {
                            cliqued = false;
                        }

                        if (release) {
                            if (!released) {
                                mouse.mouseRelease(InputEvent.BUTTON1_MASK);
                                System.out.println("Mouse released");
                                released = true;
                            }
                        } else {
                            released = false;
                        }

                        clique = false;
                        release = false;

                        if (x != x_anterior || y != y_anterior) {
                            mouse.mouseMove(x, y);
                        }


                        x_anterior = x;
                        y_anterior = y;


                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
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
