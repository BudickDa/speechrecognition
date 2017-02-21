package eu.budick;

import javafx.scene.control.TextArea;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by daniel on 17.02.17.
 */
public class Classifier implements ClassifierInterface {
    int errors;
    int matches;
    ArrayList<Vector> trainingsData = new ArrayList<Vector>();
    ArrayList<String> trainingsDataIndex = new ArrayList<String>();
    ArrayList<Vector> meanVectors = new ArrayList<Vector>();
    ArrayList<Vector> deviationVectors = new ArrayList<Vector>();
    ArrayList<String> phonemeList = new ArrayList<String>();
    File resourcesDirectory = new File("src/eu/budick/resources");

    public void displayResults(TextArea ouput) {
        float sum = this.matches + this.errors;
        float percent = (Integer.toUnsignedLong(this.matches) / sum) * 100;
        ouput.appendText("Errors: " + this.errors + ", Matches: " + this.matches + ", " + percent + " % correct.");
        ouput.appendText("\n");
    }

    public void train(ArrayList<String> trainingCases, ArrayList<String> phonemeList) {
        this.phonemeList = phonemeList;
        for (String fileName : trainingCases) {
            String phonem = Util.getPhonem(fileName);
            Vector vector = new Vector();
            vector.load(fileName);
            this.trainingsData.add(vector);
            this.trainingsDataIndex.add(phonem);
        }

        for (String phoneme : this.phonemeList) {
            ArrayList<Integer> indicies = Util.indexOfAll(phoneme, this.trainingsDataIndex);
            ArrayList<Vector> tmp = new ArrayList<Vector>();
            for (int index : indicies) {
                tmp.add(this.trainingsData.get(index));
            }
            Vector tmpMean = Util.getMeanVector(tmp);
            this.meanVectors.add(tmpMean);
            this.deviationVectors.add(Util.getStandardDeviation(tmp));
        }
    }

    public void test(ArrayList<String> testCases) {
        this.errors = 0;
        this.matches = 0;
        for (String fileName : testCases) {
            String phonem = Util.getPhonem(fileName);
            Vector vector = new Vector();
            vector.load(fileName);
            String result = this.classify(vector);
            if (phonem.equals(result)) {
                this.matches++;
            } else {
                this.errors++;
            }
        }
    }

    @Override
    public String classify(Vector v) {
        return null;
    }
}
