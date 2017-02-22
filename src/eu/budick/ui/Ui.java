package eu.budick.ui;

import edu.cmu.sphinx.frontend.Data;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.frontend.util.Utterance;
import eu.budick.*;
import eu.budick.Vector;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Circle;
import javax.sound.sampled.AudioFileFormat;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by daniel on 20.02.17.
 */
public class Ui implements Initializable {
    private ArrayList<String> trainingCases = Util.getListFromFile("/Listen/training.txt");
    private ArrayList<String> testCases = Util.getListFromFile("/Listen/test.txt");
    private ArrayList<String> phonemeList = Util.getListFromFile("/Listen/phonemes.txt");
    private ArrayList<String> allWav = Util.getListFromFile("/Listen/all.txt");


    private NearestNeighbour nn = new NearestNeighbour();
    private GaussianClassifier gc = new GaussianClassifier();
    private DtwClassifier dtw = new DtwClassifier();
    private Microphone microphone = new Microphone(
            16000, 16, 1,
            true, true, false,
            10, true, "average",
            0, "default", 6400);

    private boolean recording = false;

    @FXML
    public TextArea exerciseTwoOutput;

    @FXML
    public Circle recordFeedback;

    @FXML
    public TextArea exerciseThreeOutput;

    @FXML
    private void handleNearestNeighbour(ActionEvent event) {
        this.nn.displayResults(exerciseTwoOutput);
    }

    @FXML
    private void handleGauss(ActionEvent event) {
        this.gc.displayResults(exerciseTwoOutput);
    }

    @FXML
    private void handleDTW(ActionEvent event) {
        this.dtw.displayResults(exerciseTwoOutput);
    }

    @FXML
    private void toggleRecord(Event e) {
        this.recording = !this.recording;
        recordFeedback.setVisible(this.recording);
        if (this.recording) {
            microphone.startRecording();
        } else {
            System.out.println(microphone.getAudioFormat());
            Utterance utterance = microphone.getUtterance();
            microphone.stopRecording();
            System.out.println(analyse(utterance));
        }
    }

    @FXML
    public void sayHut(){
        Utterance utterance = Util.wavToUtterance(new File(Util.resourcesDirectory+"/WAV/NEIN-AB.WAV"));
        System.out.println(analyse(utterance));
    }

    @FXML
    public void sayJa(){
        Utterance utterance = Util.wavToUtterance(new File(Util.resourcesDirectory+"/WAV/JA-AB.WAV"));
        System.out.println(analyse(utterance));
    }

    @FXML
    public void sayNein(){
        Utterance utterance = Util.wavToUtterance(new File(Util.resourcesDirectory+"/WAV/HUT-AB.WAV"));
        System.out.println(analyse(utterance));
    }

    @FXML
    public void sayGruen(){
        Utterance utterance = Util.wavToUtterance(new File(Util.resourcesDirectory+"/WAV/GRUEN-AB.WAV"));
        System.out.println(analyse(utterance));
    }

    @FXML
    public void sayA(){
        Utterance utterance = Util.wavToUtterance(new File(Util.resourcesDirectory+"/WAV/A-UA.WAV"));
        System.out.println(analyse(utterance));
    }

    @FXML
    public void sayM(){
        Utterance utterance = Util.wavToUtterance(new File(Util.resourcesDirectory+"/WAV/M-UA.WAV"));
        System.out.println(analyse(utterance));
    }

    @FXML
    public void sayS(){
        Utterance utterance = Util.wavToUtterance(new File(Util.resourcesDirectory+"/WAV/S-SB.WAV"));
        System.out.println(analyse(utterance));
    }

    @FXML
    public void sayU(){
        Utterance utterance = Util.wavToUtterance(new File(Util.resourcesDirectory+"/WAV/U-TB.WAV"));
        System.out.println(analyse(utterance));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recordFeedback.setVisible(false);
        microphone.initialize();
        this.nn.train(trainingCases, phonemeList);
        this.nn.test(testCases);
        this.gc.train(trainingCases, phonemeList);
        this.gc.test(testCases);
        this.dtw.train(trainingCases, phonemeList);
        this.dtw.test(trainingCases);

    }

    public String analyse(Utterance utterance){
        if (utterance != null) {
            return dtw.classify(utterance);
        }
        return "-1";
    }
}
