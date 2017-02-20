package eu.budick;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ArrayList<String> trainingCases = Util.getListFromFile("/Listen/training.txt");
        ArrayList<String> testCases = Util.getListFromFile("/Listen/test.txt");
        ArrayList<String> phonemeList = Util.getListFromFile("/Listen/phonemes.txt");
        ArrayList<String> allWav = Util.getListFromFile("/Listen/all.txt");

        Microphone micro = Microphone.getInstance();

        Scanner keyboard = new Scanner(System.in);
        boolean exit = false;
        boolean active = false;
        while (!exit) {
            String input = keyboard.nextLine();
            if (input != null) {
                System.out.println("Your input is : " + input);
                if ("quit".equals(input)) {
                    System.out.println("Exit programm");
                    exit = true;
                } else if ("x".equals(input)) {
                    if (active) {
                        micro.deactivate();
                    } else {
                        micro.activate();
                    }
                } else if ("n".equals(input)) {
                    NearestNeighbour nn = new NearestNeighbour();
                    nn.train(trainingCases, phonemeList);
                    nn.test(testCases);
                    nn.displayResults();
                } else if ("g".equals(input)) {
                    GaussianClassifier gc = new GaussianClassifier();
                    gc.train(trainingCases, phonemeList);
                    gc.test(testCases);
                    gc.displayResults();
                } else if ("d".equals(input)) {
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
    }
}
