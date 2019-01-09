package bluetooth;

import com.intel.bluetooth.RemoteDeviceHelper;
import javafx.fxml.Initializable;
import uncoupledglovedatathings.GloveSensors;
import uncoupledprograms.DataSaveCsv;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class BluetoothConnection {

    private boolean scanFinished = false;
    private RemoteDevice hc05device;
    private String hc05Url;
    private StreamConnection streamConnection = null;
    private OutputStream os = null;
    private InputStream is = null;
    private boolean isConnected = false;
    private boolean hasError = false;
    private static BluetoothConnection ME;
    private IBlutoothInfoScreen blutoothInfoScreen;
    private List<IPostAppendScreen> postAppendScreenList = new ArrayList<>();

    private final static Logger LOGGER = Logger.getLogger(BluetoothConnection.class.getName());

    public static class BluetoothStatus {
        private BluetoothConnection b;

        private BluetoothStatus(BluetoothConnection b) {
            this.b = b;
        }

        public boolean isConnected() {
            if (b == null)
                return false;
            return b.isConnected();
        }
    }


    private BluetoothConnection() {
    }




    public static BluetoothStatus getBluetoothStatus() {
        return new BluetoothStatus(ME);
    }

    public static BluetoothConnection getInstance(IBlutoothInfoScreen blutoothInfoScreen) {
        if (ME == null) {
            ME = new BluetoothConnection();
        }
        ME.blutoothInfoScreen = blutoothInfoScreen;
        return ME;

    }


    public boolean isConnected() {
        return isConnected;
    }

    public void tryToConnect(String gloveName) {


        if (isConnected)
            return;
        Thread t = new Thread(() -> {
            try {
                blutoothInfoScreen.connecting();
                scanFinished = false;
                LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, new DiscoveryListener() {
                    @Override
                    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                        try {
                            String name = btDevice.getFriendlyName(false);
                            System.out.format("%s (%s)\n", name, btDevice.getBluetoothAddress());
                            if (name.equals(gloveName)) {
                                hc05device = btDevice;
                                RemoteDeviceHelper.authenticate(hc05device, "1234");
                                System.out.println("got it!");
                                LocalDevice.getLocalDevice().getDiscoveryAgent().cancelInquiry(this);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void inquiryCompleted(int discType) {
                        scanFinished = true;
                    }

                    @Override
                    public void serviceSearchCompleted(int transID, int respCode) {
                    }

                    @Override
                    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                    }
                });
                while (!scanFinished) {
                    Thread.sleep(1000);
                }

                //search for services:
                UUID uuid = new UUID(0x1101); //scan for btspp://... services (as HC-05 offers it)
                UUID[] searchUuidSet = new UUID[]{uuid};
                int[] attrIDs = new int[]{
                        0x0100
                };


                if (hc05device == null) {
                    blutoothInfoScreen.connectionFailure("Glove not found.\nMake sure it is on");
                }                    //todo hc05device != null assert
                scanFinished = false;
                LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(attrIDs, searchUuidSet,
                        hc05device, new DiscoveryListener() {
                            @Override
                            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                            }

                            @Override
                            public void inquiryCompleted(int discType) {
                            }

                            @Override
                            public void serviceSearchCompleted(int transID, int respCode) {
                                scanFinished = true;
                            }

                            @Override
                            public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                                for (int i = 0; i < servRecord.length; i++) {
                                    hc05Url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                                    if (hc05Url != null) {
                                        break; //take the first one
                                    }
                                }
                            }
                        });

                while (!scanFinished) {
                    Thread.sleep(500);
                }

                System.out.println(hc05device.getBluetoothAddress());
                streamConnection = (StreamConnection) Connector.open(hc05Url);
                os = streamConnection.openOutputStream();
                is = streamConnection.openInputStream();

                os.write("U".getBytes());
                isConnected = true;
                boolean shownConnected = false;
                while (isConnected) {
                    sync();
                    if (hasError) {
                        break;
                    } else if (!shownConnected) {
                        blutoothInfoScreen.bluetoothConnected();
                        shownConnected = true;
                    }
                    appendSensors();
                    autoAppendValues();
                }

                os.close();
                is.close();
                if (hasError) {
                    blutoothInfoScreen.connectionFailure("");
                    hasError = false;
                    System.out.println("BT connection closed by error");
                } else {
                    blutoothInfoScreen.bluetoothDisconnected();
                    System.out.println("BT connection closed");
                }
                streamConnection.close();


            } catch (Exception e) {
                e.printStackTrace();
                blutoothInfoScreen.connectionFailure("");
                PrintWriter pw = null;
                try {
                    File f = (new File("log" + new Date().toString() + ".txt"));
                    f.createNewFile();
                    pw = new PrintWriter(f);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace(pw);
                pw.close();


            }
        });

        t.start();

    }

    public static boolean schedulePostAppendRunnable(IPostAppendScreen initializable) {
        if (ME != null) {
            if (!ME.postAppendScreenList.contains(initializable))
                ME.postAppendScreenList.add(initializable);
            return true;
        }
        return false;
    }


    public void disconnect() {
        isConnected = false;
    }

    private void disconnectWithError() {
        isConnected = false;
        hasError = true;
    }


    private void sync() {
        int cod1, cod2, cod3, cod4;
        boolean continua = false;
        int errorCount = 0;

        while (!continua) {
            try {
                cod1 = is.read();
                if (cod1 == 255) {  // 255
                    cod2 = is.read();
                    if (cod2 == 127) {  // 127
                        cod3 = is.read();
                        if (cod3 == 254) {  // 254
                            cod4 = is.read();
                            if (cod4 == 255) {  // 255
                                continua = true;
                                errorCount = 0;
                            } else {
                                errorCount++;
                            }
                        } else {
                            errorCount++;

                        }
                    } else {
                        errorCount++;
                    }
                } else {
                    errorCount++;
                }
            } catch (IOException ex) {
                errorCount++;
                System.out.println("Sync hasError");
                continue;
            }
            if (errorCount > 0)
                System.out.println(errorCount);
            if (errorCount > 100000) {
                continua = true;
                disconnectWithError();
            }
        }
    }

    private void autoAppendValues() {
        DataSaveCsv saveCsv = DataSaveCsv.getInstance();

        final GloveSensors glove = GloveSensors.getInstance();
        if (saveCsv.isAutoSavingGyro()) {
            //System.out.println(saveCsv.isAutoSavingGyro());

            ArrayList<Double> arrGyro = new ArrayList<Double>() {{
                add(glove.getSensor1().getGx().lastElement());
                add(glove.getSensor1().getGy().lastElement());
                add(glove.getSensor1().getGz().lastElement());
                add(glove.getSensor2().getGx().lastElement());
                add(glove.getSensor2().getGy().lastElement());
                add(glove.getSensor2().getGz().lastElement());
                add(glove.getSensor3().getGx().lastElement());
                add(glove.getSensor3().getGy().lastElement());
                add(glove.getSensor3().getGz().lastElement());
                add(glove.getSensor4().getGx().lastElement());
                add(glove.getSensor4().getGy().lastElement());
                add(glove.getSensor4().getGz().lastElement());
                add(glove.getSensor5().getGx().lastElement());
                add(glove.getSensor5().getGy().lastElement());
                add(glove.getSensor5().getGz().lastElement());
                add(glove.getSensor6().getGx().lastElement());
                add(glove.getSensor6().getGy().lastElement());
                add(glove.getSensor6().getGz().lastElement());

            }};

            saveCsv.appendGyroData(arrGyro);

        }

        if (saveCsv.isAutoSavingAcc()) {
            ArrayList<Double> arrAcc = new ArrayList<Double>() {{
                add(glove.getSensor1().getAx().lastElement());
                add(glove.getSensor1().getAy().lastElement());
                add(glove.getSensor1().getAz().lastElement());
                add(glove.getSensor2().getAx().lastElement());
                add(glove.getSensor2().getAy().lastElement());
                add(glove.getSensor2().getAz().lastElement());
                add(glove.getSensor3().getAx().lastElement());
                add(glove.getSensor3().getAy().lastElement());
                add(glove.getSensor3().getAz().lastElement());
                add(glove.getSensor4().getAx().lastElement());
                add(glove.getSensor4().getAy().lastElement());
                add(glove.getSensor4().getAz().lastElement());
                add(glove.getSensor5().getAx().lastElement());
                add(glove.getSensor5().getAy().lastElement());
                add(glove.getSensor5().getAz().lastElement());
                add(glove.getSensor6().getAx().lastElement());
                add(glove.getSensor6().getAy().lastElement());
                add(glove.getSensor6().getAz().lastElement());

            }};
            saveCsv.appendAccData(arrAcc);

        }

    }

    private void appendSensors() {
        GloveSensors.getInstance().appendDataWithToRadAndResist(
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue(),
                readNextValue());
        List<IPostAppendScreen> removeList = new ArrayList<>();
        for (IPostAppendScreen ipas : postAppendScreenList) {
            try {
                ipas.getPostAppendRunnable().run();
            } catch (Exception e) {
                removeList.add(ipas);
                e.printStackTrace();
            }
        }
        for (IPostAppendScreen ipas : removeList) {
            postAppendScreenList.remove(ipas);
        }

    }

    private double readNextValue() {
        try {
            byte[] buffer_low = new byte[1];
            byte[] buffer_high = new byte[1];
            //byte[] buffer_full = new byte[4];
            is.read(buffer_low);
            is.read(buffer_high);
            ByteBuffer wrapped = ByteBuffer.wrap((new byte[]{0, 0, buffer_high[0], buffer_low[0]}));
            double num_double = wrapped.getInt();
            double a = Math.pow(2, 15) - 1;
            if (num_double > (a)) {
                num_double = num_double - Math.pow(2, 16);
            }
            return num_double;
        } catch (IOException ex) {
            return 0;
        }
    }
}
    
    
    

