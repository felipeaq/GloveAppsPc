package swing;

import uncoupledprograms.pconly.MouseFunction;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class MouseMode extends JFrame {

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void startMouse() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    //isMoving=true;
                    MouseMode frame = new MouseMode();
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
    public MouseMode() {
        MouseFunction mouseFunction = new MouseFunction();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        boolean release = false;
        JButton btnNewButton = new JButton("Start");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(mouseFunction.isWorking());
                    mouseFunction.start();

            }
        });
        btnNewButton.setBounds(278, 30, 115, 93);
        contentPane.add(btnNewButton);

        JButton btnStop = new JButton("Stop");
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                mouseFunction.stop();
            }
        });
        btnStop.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnStop.setBounds(278, 146, 115, 50);
        contentPane.add(btnStop);
    }


}
