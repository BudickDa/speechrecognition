package eu.budick;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by daniel on 17.02.17.
 */
public class Util {
    public static File resourcesDirectory = new File("src/eu/budick/resources");

    public static Vector getMeanVector(ArrayList<Vector> vectors) {
        Vector meanVector = new Vector();
        for (Vector vector : vectors) {
            meanVector = meanVector.add(vector);
        }
        meanVector = meanVector.divideByValue(Integer.toUnsignedLong(vectors.size()));
        return meanVector;
    }

    public static Vector getStandardDeviation(ArrayList<Vector> vectors) {
        Vector meanVector = Util.getMeanVector(vectors);
        Vector deviationVector = new Vector();
        for (Vector vector : vectors) {
            for (int i = 0; i < vector.length(); i++) {
                double newValue = deviationVector.getValue(i) + Math.pow(vector.getValue(i) - meanVector.getValue(i), 2);
                deviationVector.setValue(i, (float)newValue);
            }
        }
        for (int i = 0; i < deviationVector.length(); i++) {
            double newValue= Math.sqrt(deviationVector.getValue(i) / vectors.size());
            deviationVector.setValue(i, (float)newValue);
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

    public static ArrayList<Integer> indexOfAll(Object obj, ArrayList list) {
        ArrayList<Integer> indexList = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++)
            if (obj.equals(list.get(i)))
                indexList.add(i);
        return indexList;
    }

    public static String getVectorPath(String fileName) {
        String vectorDirName = Util.resourcesDirectory.getAbsolutePath() + "/CEP";
        return vectorDirName + "/" + fileName.replace(".WAV", ".cep");
    }

    public static String getWavPath(String fileName) {
        String vectorDirName = Util.resourcesDirectory.getAbsolutePath() + "/WAV";
        return vectorDirName + "/" + fileName;
    }

    public static ArrayList<String> getListFromFile(String fileName) {
        ArrayList<String> result = new ArrayList<String>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(Util.resourcesDirectory + fileName));

            while (in.ready()) {
                result.add(in.readLine());
            }
            in.close();
        } catch (Exception e) {
            System.out.print("Error: " + e.getMessage());
        }
        return result;
    }

    public static float min(ArrayList<Float> input){
        return Collections.min(input);
    }
}
