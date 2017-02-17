package eu.budick;

import java.util.ArrayList;

/**
 * Created by daniel on 17.02.17.
 */
public class NearestNeighbour extends Classifier {
    public String classify(Vector vector) {
        int bestIndex = -1;
        double bestDistance = Double.MAX_VALUE;

        Vector meanVector = Util.getMeanVector(this.trainingsData);
        Vector deviationVector = Util.getStandardDeviation(this.trainingsData);
        vector = vector.normalize(meanVector, deviationVector);

        for (int i = 0; i < this.trainingsData.size(); i++) {
            Vector tmpVector = this.trainingsData.get(i);
            tmpVector = tmpVector.normalize(meanVector, deviationVector);

            double distance = tmpVector.getEuclidDistance(vector);
            if (distance < bestDistance) {
                bestIndex = i;
                bestDistance = distance;
            }
        }
        return this.trainingsDataIndex.get(bestIndex);
    }
}
