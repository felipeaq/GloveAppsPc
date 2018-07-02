package swing;

import bluetooth.BluetoothConnection;
import bluetooth.IBlutoothInfoScreen;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class MainScreen extends JFrame implements IBlutoothInfoScreen {

    private JPanel connect;
    public static JLabel lblUnconnected = null;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
		
		/*try{
		new SimpleRealTime().start();
			
		}catch(Exception e) {
			
		}*/
        String name;
        try {
            name = new File(".").getCanonicalPath();
            System.out.println("Test " + name);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainScreen frame = new MainScreen();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public MainScreen() {

        BluetoothConnection bluetoothConnection = BluetoothConnection.getInstance(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        connect = new JPanel();
        connect.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(connect);
        connect.setLayout(null);

        lblUnconnected = new JLabel("unconnected");
        lblUnconnected.setBounds(10, 141, 89, 14);
        connect.add(lblUnconnected);

        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (!bluetoothConnection.isConnected()) {
                    bluetoothConnection.tryToConnect();
                }
            }
        });
        btnConnect.setBounds(10, 166, 89, 23);
        connect.add(btnConnect);

        JButton btnDisconnect = new JButton("Disconnect");
        btnDisconnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bluetoothConnection.disconnect();
            }
        });
        btnDisconnect.setBounds(10, 200, 104, 23);
        connect.add(btnDisconnect);

        JButton btnGraphMode = new JButton("Graph Mode");
		/*btnGraphMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							GraphMode frame = new GraphMode();
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});*/
        btnGraphMode.setBounds(291, 69, 104, 23);
        connect.add(btnGraphMode);

        JButton btnPointingDevice = new JButton("Pointing Device");
        btnPointingDevice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MouseMode.startMouse();
            }
        });
        btnPointingDevice.setBounds(291, 103, 127, 23);
        connect.add(btnPointingDevice);

        JButton btndControl = new JButton("3D Control");
        btndControl.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            Hand3DControl frame = new Hand3DControl();
                            frame.setVisible(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
        btndControl.setBounds(291, 137, 89, 23);
        connect.add(btndControl);

        JButton btnKeyboard = new JButton("Keyboard");
        btnKeyboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            Keyboard frame = new Keyboard();
                            frame.setVisible(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
        btnKeyboard.setBounds(291, 166, 89, 23);
        connect.add(btnKeyboard);

        JButton btnMoreApplications = new JButton("More Applications");
        btnMoreApplications.setBounds(291, 200, 133, 23);
        connect.add(btnMoreApplications);


    }

    @Override
    public void connectionFailure() {
        MainScreen.lblUnconnected.setText("Failure! Try Again!");

    }

    @Override
    public void bluetoothConnected() {
        MainScreen.lblUnconnected.setText("Connected!");
    }

    @Override
    public void bluetoothDisconnected() {
        MainScreen.lblUnconnected.setText("Disconnected!");


    }

    @Override
    public void connecting() {
        MainScreen.lblUnconnected.setText("Connection...");
    }
}
