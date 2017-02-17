package eu.budick;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Created by daniel on 17.02.17.
 */
public class Vector {
    private float[] values;

    Vector(){
        this.setValues(new float[13]);
        Arrays.fill(this.getValues(), 0);
    }

    Vector(float[] values){
        this.setValues(values);
    }

    public float getValue(int index){
        return this.getValues()[index];
    }

    public void setValue(int index, float value){
        this.values[index] = value;
    }

    public double getEuclidDistance(Vector otherVector) {
        float sum = 0;
        for (int i = 0; i < this.getValues().length; i++) {
            sum += Math.pow(this.getValue(i) - otherVector.getValue(i), 2);
        }
        return Math.sqrt(sum);
    }

    public void load(String fileName) {
        float[] result = new float[13];
        byte[] tmp;
        try {
            byte[] bytes = Files.readAllBytes(new File(fileName).toPath());

            int start = 4 + 52 * ((int) Math.floor((double) (bytes.length - 4) / 104));
            for (int i = 0; i < 13; i++) {
                tmp = new byte[4];
                tmp[3] = bytes[start];
                tmp[2] = bytes[start + 1];
                tmp[1] = bytes[start + 2];
                tmp[0] = bytes[start + 3];

                result[i] = Util.byteToFloat(tmp);
                start += 4;

            }

        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        this.setValues(result);
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }
}
