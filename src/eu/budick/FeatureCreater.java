package eu.budick;


import edu.cmu.sphinx.frontend.*;
import edu.cmu.sphinx.frontend.util.AudioFileDataSource;
import edu.cmu.sphinx.frontend.util.StreamDataSource;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Created by daniel on 20.02.17.
 */
public class FeatureCreater {
    private FrontEnd frontEnd;
    private StreamDataSource audioSource;
    private List<float[]> allFeatures;
    private int featureLength = -1;

    public FeatureCreater(ConfigurationManager cm, String frontEndName)
            throws IOException {
        try {
            frontEnd = (FrontEnd) cm.lookup(frontEndName);
            audioSource = (StreamDataSource) cm.lookup("streamDataSource");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<float[]> getFeatures(FileInputStream inputAudioStream) {
        try {
            URL url = FeatureCreater.class
                    .getResource("frontend.config.xml");
            ConfigurationManager cm = new ConfigurationManager(url);
            String frontEndName = "cepstraFrontEnd";

            if (cm.lookup(frontEndName) == null) {
                throw new RuntimeException("No such frontend: " + frontEndName);
            }

            FeatureCreater creator = new FeatureCreater(cm, frontEndName);
            List<float[]> features = creator.processStream(inputAudioStream);
            return features;
        } catch (IOException ioe) {
            System.err.println("I/O Error " + ioe);
        } catch (PropertyException p) {
            System.err.println("Bad configuration " + p);
        }
        return null;
    }

    private List<float[]> processStream(FileInputStream inputAudioStream){
        audioSource.setInputStream(inputAudioStream);
        allFeatures = new LinkedList<float[]>();
        this.getAllFeatures();
        return allFeatures;
    }

    private void getAllFeatures() {
        /*
         * Run through all the data and produce feature.
         */
        try {
            assert (allFeatures != null);
            Data feature = frontEnd.getData();
            while (!(feature instanceof DataEndSignal)) {
                if (feature instanceof DoubleData) {
                    double[] featureData = ((DoubleData) feature).getValues();
                    if (featureLength < 0) {
                        featureLength = featureData.length;
                    }
                    float[] convertedData = new float[featureData.length];
                    for (int i = 0; i < featureData.length; i++) {
                        convertedData[i] = (float) featureData[i];
                    }
                    allFeatures.add(convertedData);
                } else if (feature instanceof FloatData) {
                    float[] featureData = ((FloatData) feature).getValues();
                    if (featureLength < 0) {
                        featureLength = featureData.length;
                    }
                    allFeatures.add(featureData);
                }
                feature = frontEnd.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
