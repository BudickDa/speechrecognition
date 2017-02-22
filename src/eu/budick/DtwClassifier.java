package eu.budick;

import edu.cmu.sphinx.frontend.util.Utterance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by daniel on 17.02.17.
 */
public class DtwClassifier extends Classifier {
    int[][] path;
    float[][] DTW;

    public float classify(Vector reference, Vector test) {
        return this.getDtwDistance(reference, test);
    }

    public String classify(Utterance utterance) {
        int bestIndex = -1;
        double bestDistance = Double.MAX_VALUE;
        for (int i = 0; i < this.trainingsUtterances.size(); i++) {
            Utterance referenceUtterance = this.trainingsUtterances.get(i);
            double distance = this.getDtwDistance(utterance, referenceUtterance);
            if (distance < bestDistance) {
                bestIndex = i;
                bestDistance = distance;
            }
        }
        System.out.println(bestDistance);
        return this.trainingsDataIndex.get(bestIndex);
    }

    public void setPath(int i, int j, int direction) {
        this.path[i][j] = direction;
    }

    public float getDtwDistance(Vector v1, Vector v2) {
        float cost;
        DTW = new float[v1.length()][v2.length()];
        this.path = new int[v1.length()][v2.length()];
        for (int i = 0; i > v1.length(); i++) {
            DTW[i][0] = Float.POSITIVE_INFINITY;
        }
        for (int j = 0; j > v2.length(); j++) {
            DTW[0][j] = Float.POSITIVE_INFINITY;
        }

        for (int i = 1; i < DTW.length; i++) {
            for (int j = 1; j < DTW[i].length; j++) {
                cost = Math.abs(v1.getValue(i) - v2.getValue(j));
                ArrayList<Float> edges = new ArrayList<>();
                edges.add(DTW[i - 1][j]);
                edges.add(DTW[i][j - 1]);
                edges.add(DTW[i - 1][j - 1]);
                float minValue = Util.min(edges);
                int direction = 1 << edges.indexOf(minValue);
                this.setPath(i, j, direction);
                DTW[i][j] = cost + minValue;
            }
        }
        return DTW[v1.length() - 1][v2.length() - 1];
    }

    public float getDtwDistance(Utterance u1, Utterance u2) {
        double cost;
        List<Vector> f1 = FeatureCreater.getFeatures(u1);
        List<Vector> f2 = FeatureCreater.getFeatures(u2);
        DTW = new float[f1.size()][f2.size()];
        this.path = new int[f1.size()][f2.size()];
        for (int i = 0; i > f1.size(); i++) {
            DTW[i][0] = Float.MAX_VALUE;
        }
        for (int j = 0; j > f2.size(); j++) {
            DTW[0][j] = Float.MAX_VALUE;
        }

        for (int i = 1; i < DTW.length; i++) {
            for (int j = 1; j < DTW[i].length; j++) {
                cost = f1.get(i).getEuclidDistance(f2.get(j));
                ArrayList<Float> edges = new ArrayList<>();
                edges.add(DTW[i - 1][j]);
                edges.add(DTW[i][j - 1]);
                edges.add(DTW[i - 1][j - 1]);
                float minValue = Util.min(edges);
                int direction = 1 << edges.indexOf(minValue);
                this.setPath(i, j, direction);
                DTW[i][j] = (float)cost + minValue;
            }
        }
        return DTW[f1.size() - 1][f2.size() - 1];
    }

    public void print() {
        for (int i = this.DTW.length - 1; i >= 0; --i) {
            System.out.print('|');
            for (int j = 0; j < this.DTW[i].length; j++) {
                System.out.print(" " + Math.floor(this.DTW[i][j]*10)/10 + " |");
            }
            System.out.println("");
        }
    }
    public void printPath() {
        for (int i = this.path.length - 1; i >= 0; --i) {
            for (int j = 0; j < this.path[i].length; j++) {
                String symbol = "-";
                if(this.path[i][j]==2){
                    symbol = "|";
                }
                if(this.path[i][j]==4){
                    symbol = "/";
                }
                System.out.print(" " + symbol + " ");
            }
            System.out.println("");
        }
    }
}
