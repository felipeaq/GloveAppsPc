package swing;

import uncoupledglovedatathings.IPredictScreen;
import uncoupledprograms.SVC;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineStateError;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Synthesizer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;


//import com.gtranslate.Language;

public class Keyboard extends JFrame implements IPredictScreen {
    // SVC svc=null;
    private JPanel labelState;
    static javax.speech.synthesis.Synthesizer synthesizer = null; // TODO

    /**
     * Launch the application.
     */

    /**
     * Create the frame.
     */
    private JLabel lblState = null;
    private JTextField txtText;
    private SVC svc;
//!System.getProperty("os.name").contains("Windows")
    public Keyboard() {

        try {
            svc = SVC.getInstance(new File("Files/data.json"));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 599);
        labelState = new JPanel();
        labelState.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(labelState);
        labelState.setLayout(null);

        JButton btnStart = new JButton("Start");
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!svc.isWorking()) {

                    lblState.setText("Starting...");
                    svc.startPredict(Keyboard.this);
                }
            }
        });
        btnStart.setBounds(78, 526, 89, 23);
        labelState.add(btnStart);

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                svc.stopPredict();
                synthesizer = null;
            }
        });
        btnClose.setBounds(208, 526, 89, 23);
        labelState.add(btnClose);

        lblState = new JLabel("State");
        lblState.setBounds(649, 530, 94, 14);
        labelState.add(lblState);

        txtText = new JTextField();
        txtText.setFont(new Font("Tahoma", Font.PLAIN, 21));
        txtText.setHorizontalAlignment(SwingConstants.CENTER);
        txtText.setBounds(10, 11, 764, 504);
        labelState.add(txtText);
        txtText.setColumns(10);

    }

    @Override
    public void closeTheSpeaker() {
        try {
            if (synthesizer != null)
                synthesizer.deallocate();
        } catch (EngineException | EngineStateError e) {
            // TODO Descobrir o erro
            e.printStackTrace();
        }
    }

    @Override
    public void getReadyToSpeak() {
        try {
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
            synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));
        } catch (IllegalArgumentException | EngineException e1) {

            System.out.println("Erro ao criar o TTS: " + e1.getMessage());
        }
        try {
            synthesizer.allocate();
        } catch (EngineException | EngineStateError e1) {
            System.out.println("Erro ao alocar o TTS: " + e1.getMessage());
        }
    }

    @Override
    public void speak() {
        try {
            synthesizer.resume();
            synthesizer.speakPlainText(txtText.getText(), null);
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
        } catch (AudioException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void appendCharaterToScreen(char character) {

        txtText.setText(txtText.getText() + character);

    }
}
