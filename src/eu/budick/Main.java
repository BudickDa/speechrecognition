package eu.budick;

import edu.cmu.sphinx.frontend.Data;
import edu.cmu.sphinx.frontend.DataProcessor;
import edu.cmu.sphinx.frontend.FrontEnd;
import edu.cmu.sphinx.frontend.frequencywarp.MelFrequencyFilterBank;
import edu.cmu.sphinx.frontend.transform.DiscreteCosineTransform2;
import edu.cmu.sphinx.frontend.transform.DiscreteFourierTransform;
import edu.cmu.sphinx.frontend.util.AudioFileDataSource;
import edu.cmu.sphinx.frontend.window.RaisedCosineWindower;

import javax.security.sasl.SaslServer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ArrayList<String> trainingCases = Util.getListFromFile("/Listen/training.txt");
        ArrayList<String> testCases = Util.getListFromFile("/Listen/test.txt");
        ArrayList<String> phonemeList = Util.getListFromFile("/Listen/phonemes.txt");
        ArrayList<String> allWav = Util.getListFromFile("/Listen/all.txt");

        AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
        try {
            TargetDataLine microphone = AudioSystem.getTargetDataLine(format);
            Scanner keyboard = new Scanner(System.in);
            boolean exit = false;
            while (!exit) {
                System.out.println("Enter command (quit to exit):");
                String input = keyboard.nextLine();
                if(input != null) {
                    System.out.println("Your input is : " + input);
                    if ("quit".equals(input)) {
                        System.out.println("Exit programm");
                        exit = true;
                    } else if ("x".equals(input)) {
                        if(microphone.isActive()){
                            microphone.stop();
                        }else {
                            Main.listen(microphone);
                        }
                    }
                    else if ("n".equals(input)) {
                        NearestNeighbour nn = new NearestNeighbour();
                        nn.train(trainingCases, phonemeList);
                        nn.test(testCases);
                        nn.displayResults();
                    }
                    else if ("g".equals(input)) {
                        GaussianClassifier gc = new GaussianClassifier();
                        gc.train(trainingCases, phonemeList);
                        gc.test(testCases);
                        gc.displayResults();
                    }
                    else if ("d".equals(input)) {
                        DtwClassifier dtw = new DtwClassifier();
                        Vector test = new Vector();
                        test.load(trainingCases.get(0));
                        Vector reference = new Vector();
                        reference.load(trainingCases.get(1));
                        dtw.classify(reference, test);
                        dtw.print();
                        dtw.printPath();
                    }

                }
            }
            keyboard.close();

        }catch(Exception e){
            System.out.println("ERROR: "+e.getMessage());
        }
    }

    public static void listen(TargetDataLine microphone){
        microphone.start();
        while (!microphone.isOpen()) {
            System.out.println("Listening...");
        }
    }
}
