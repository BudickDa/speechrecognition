package eu.budick;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        ArrayList<String> trainingCases = Util.getListFromFile("/Listen/training.txt");
        ArrayList<String> testCases = Util.getListFromFile("/Listen/test.txt");
        ArrayList<String> phonemeList = Util.getListFromFile("/Listen/phonemes.txt");

        NearestNeighbour nn = new NearestNeighbour();
        nn.train(trainingCases, phonemeList);
        nn.test(testCases);
        nn.displayResults();

        GaussianClassifier gc = new GaussianClassifier();
        gc.train(trainingCases, phonemeList);
        gc.test(testCases);
        gc.displayResults();

        DtwClassifier dtw = new DtwClassifier();
        Vector test = new Vector();
        test.load(trainingCases.get(0));
        Vector reference = new Vector();
        reference.load(trainingCases.get(1));
        System.out.println(dtw.classify(reference, test));
        dtw.print();

        System.out.println(Arrays.toString(dtw.path.get(3)));
    }
}
