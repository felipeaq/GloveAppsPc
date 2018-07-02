package swing;

import uncoupledglovedatathings.GloveSensors;
import uncoupledprograms.pconly.VirtualHandContolFunction;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;

public class Hand3DControl extends JFrame {
    private JPanel contentPane;


    public Hand3DControl() {
        VirtualHandContolFunction virtualHandContolFunction = new VirtualHandContolFunction();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton btnStart = new JButton("Start");

        btnStart.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                virtualHandContolFunction.start();
            }
        });

        btnStart.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
        btnStart.setBounds(334, 154, 90, 65);
        contentPane.add(btnStart);

        JButton btnStop = new JButton("Stop");
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                virtualHandContolFunction.stop();
            }
        });
        btnStop.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btnStop.setBounds(334, 88, 89, 33);
        contentPane.add(btnStop);


    }


}
