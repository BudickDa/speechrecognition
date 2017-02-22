package eu.budick;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;

/**
 * Created by daniel on 17.02.17.
 */
public class GaussianClassifier extends Classifier {
    public String classify(Vector vector) {
        int bestIndex = -1;
        double bestDistance = Double.MAX_VALUE;

        for (int i = 0; i < this.phonemeList.size(); i++) {
            double distance = this.getGaussianDistance(vector, i);
            if (distance < bestDistance) {
                bestIndex = i;
                bestDistance = distance;
            }
        }

        return this.phonemeList.get(bestIndex);
    }

    public double getGaussianDistance(Vector vector, int phonemeIndex) {
        Vector deviationVector = this.deviationVectors.get(phonemeIndex);
        Vector meanVector = this.meanVectors.get(phonemeIndex);
        float sum1 = 0;
        for (int i = 0; i < vector.length(); i++) {
            sum1 += Math.log(2 * Math.PI * Math.pow(deviationVector.getValue(i), 2));
        }
        float sum2 = 0;
        for (int i = 0; i < vector.length(); i++) {
            sum2 += Math.pow(vector.getValue(i) - meanVector.getValue(i), 2) / Math.pow(deviationVector.getValue(i), 2);
        }
        return -2 * Math.log(1. / 15.) + sum1 + sum2;

    }
}
