package eu.budick;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by daniel on 17.02.17.
 */
public class Util {
    File resourcesDirectory = new File("src/eu/budick/resources");

    public static float[] normalize(float[] vector, float[] meanVector, float[] deviationVector) {
        float[] normalizedVector = new float[13];
        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = (vector[i] - meanVector[i]) / deviationVector[i];
        }
        return normalizedVector;
    }

    public static float[] getMeanVector(ArrayList<float[]> vectors) {
        float[] meanVector = new float[13];
        Arrays.fill(meanVector, 0);
        for (float[] vector : vectors) {
            for (int i = 0; i < vector.length; i++) {
                meanVector[i] += vector[i];
            }
        }
        for (int i = 0; i < meanVector.length; i++) {
            meanVector[i] = meanVector[i] / Integer.toUnsignedLong(vectors.size());
        }
        return meanVector;
    }

    public static float[] getStandardDeviation(ArrayList<float[]> vectors) {
        float[] meanVector = Util.getMeanVector(vectors);
        float[] deviationVector = new float[13];
        Arrays.fill(deviationVector, 0);
        for (float[] vector : vectors) {
            for (int i = 0; i < vector.length; i++) {
                deviationVector[i] += Math.pow(vector[i] - meanVector[i], 2);
            }
        }
        for (int i = 0; i < deviationVector.length; i++) {
            deviationVector[i] = (float) Math.sqrt(deviationVector[i] /= vectors.size());
        }
        return deviationVector;
    }



    public static String getPhonem(String fileName) {
        return fileName.split("-")[0];
    }



    public static float byteToFloat(byte[] data) {
        ByteBuffer _intShifter = ByteBuffer.allocate(Float.SIZE / Byte.SIZE)
                .order(ByteOrder.LITTLE_ENDIAN);
        _intShifter.clear();
        _intShifter.put(data, 0, Float.SIZE / Byte.SIZE);
        _intShifter.flip();
        return _intShifter.getFloat();
    }
}
