package uncoupledprograms.pconly;

import uncoupledglovedatathings.GloveSensors;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class VirtualHandContolFunction {


    boolean isWorking = false;

    public void start() {
        if (!isWorking) {
            isWorking = true;
            control();
        }
    }

    public void stop() {
        isWorking=false;
    }


    private void control() {
        File dir = new File("C:/temp/arquivos/");

        for (File file : dir.listFiles()) {

            file.delete();
        }

        Thread thread = new Thread() {

            public void run() {
                int count = 0;
                System.out.println("come�ou");
                while (isWorking) {
                    count++;


                    String filename = String.format("%011d", count);
                    List<String> lines = Arrays.asList(
                            String.valueOf(GloveSensors.getInstance().getSensor1().getAx().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor2().getAx().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor3().getAx().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor4().getAx().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor5().getAx().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor6().getAx().lastElement()),

                            String.valueOf(GloveSensors.getInstance().getSensor1().getAy().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor2().getAy().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor3().getAy().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor4().getAy().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor5().getAy().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor6().getAy().lastElement()),

                            String.valueOf(GloveSensors.getInstance().getSensor1().getAz().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor2().getAz().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor3().getAz().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor4().getAz().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor5().getAz().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor6().getAz().lastElement()),


                            String.valueOf(GloveSensors.getInstance().getSensor1().getGx().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor2().getGx().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor3().getGx().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor4().getGx().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor5().getGx().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor6().getGx().lastElement()),

                            String.valueOf(GloveSensors.getInstance().getSensor1().getGy().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor2().getGy().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor3().getGy().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor4().getGy().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor5().getGy().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor6().getGy().lastElement()),

                            String.valueOf(GloveSensors.getInstance().getSensor1().getGz().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor2().getGz().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor3().getGz().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor4().getGz().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor5().getGz().lastElement()),
                            String.valueOf(GloveSensors.getInstance().getSensor6().getGz().lastElement())


                    );
                    Path file = Paths.get("C:\\temp\\arquivos\\" + filename + ".txt");
                    try {
                        Files.write(file, lines, Charset.forName("UTF-8"));
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                    if (count > 10) {
                        filename = String.format("%011d", count - 10);
                        file = Paths.get("C:\\temp\\arquivos\\" + filename + ".txt");
                        try {
                            Files.delete(file);
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }

                    }


                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }

                }

            }
        };
        thread.start();

    }

}
