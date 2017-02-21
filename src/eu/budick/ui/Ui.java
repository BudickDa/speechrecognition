package eu.budick.ui;

import eu.budick.GaussianClassifier;
import eu.budick.Microphone;
import eu.budick.NearestNeighbour;
import eu.budick.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import javax.swing.text.View;
import javax.swing.text.html.ListView;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by daniel on 20.02.17.
 */
public class Ui implements Initializable {

    ArrayList<String> trainingCases = Util.getListFromFile("/Listen/training.txt");
    ArrayList<String> testCases = Util.getListFromFile("/Listen/test.txt");
    ArrayList<String> phonemeList = Util.getListFromFile("/Listen/phonemes.txt");
    ArrayList<String> allWav = Util.getListFromFile("/Listen/all.txt");

    @FXML
    public TextArea exerciseTwoOutput;

    @FXML
    public TextArea exerciseThreeOutput;

    @FXML
    private void handleNearestNeighbour(ActionEvent event) {
        NearestNeighbour nn = new NearestNeighbour();
        nn.train(trainingCases, phonemeList);
        nn.test(testCases);
        nn.displayResults(exerciseTwoOutput);
    }

    @FXML
    private void handleGauss(ActionEvent event) {
        GaussianClassifier gc = new GaussianClassifier();
        gc.train(trainingCases, phonemeList);
        gc.test(testCases);
        gc.displayResults(exerciseTwoOutput);
    }

    @FXML
    private void startListen() {
        Microphone.getInstance().activate();
    }
    @FXML
    private void stopListen() {
        Microphone m = Microphone.getInstance();
        m.deactivate();
        List<float[]> lastFeatures = m.getLastFeatures();
        System.out.println(lastFeatures.get(0)[0]);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {}
}
