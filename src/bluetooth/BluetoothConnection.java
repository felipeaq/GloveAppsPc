package bluetooth;

import uncoupledglovedatathings.GloveSensors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
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
                                System.out.println("got it!");
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
                }

                os.close();
                is.close();
                if (hasError) {
                    blutoothInfoScreen.connectionFailure();
                    hasError = false;
                    System.out.println("BT connection closed by error");
                } else {
                    blutoothInfoScreen.bluetoothDisconnected();
                    System.out.println("BT connection closed");
                }
                streamConnection.close();


            } catch (Exception e) {
                e.printStackTrace();
                blutoothInfoScreen.connectionFailure();
            }
        });

        t.start();
    }

    public void disconnect() {
        isConnected = false;
    }

    public void disconnectWithError() {
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
    
    
    

