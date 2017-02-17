package eu.budick;

import java.util.ArrayList;

/**
 * Created by daniel on 17.02.17.
 */
public interface Classifier {
    public void train(ArrayList<String> trainingCases);
    public void test(ArrayList<String> testCases);
    public void displayResults();
    public void classify(Vector v);
}
