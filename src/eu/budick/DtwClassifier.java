package eu.budick;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by daniel on 17.02.17.
 */
public class DtwClassifier extends Classifier {
    float[][] DTW;
    int[][] path;

    public float classify(Vector reference, Vector test) {
        return this.getDtwDistance(reference, test);
    }

    public void setPath(int i, int j, int direction) {
        this.path[i][j] = direction;
    }

    public float getDtwDistance(Vector v1, Vector v2) {
        float cost;
        this.DTW = new float[v1.length()][v2.length()];
        this.path = new int[v1.length()][v2.length()];
        for (int i = 0; i > v1.length(); i++) {
            this.DTW[i][0] = Float.POSITIVE_INFINITY;
        }
        for (int j = 0; j > v2.length(); j++) {
            this.DTW[0][j] = Float.POSITIVE_INFINITY;
        }

        for (int i = 1; i < this.DTW.length; i++) {
            for (int j = 1; j < this.DTW[i].length; j++) {
                cost = Math.abs(v1.getValue(i) - v2.getValue(j));
                ArrayList<Float> edges = new ArrayList<>();
                edges.add(this.DTW[i - 1][j]);
                edges.add(this.DTW[i][j - 1]);
                edges.add(this.DTW[i - 1][j - 1]);
                float minValue = Util.min(edges);
                int direction = 1 << edges.indexOf(minValue);
                this.setPath(i, j, direction);
                this.DTW[i][j] = cost + minValue;
            }
        }
        return this.DTW[v1.length() - 1][v2.length() - 1];
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
