package eu.budick;


import edu.cmu.sphinx.frontend.*;
import edu.cmu.sphinx.frontend.util.AudioFileDataSource;
import edu.cmu.sphinx.frontend.util.StreamDataSource;
import edu.cmu.sphinx.frontend.util.Utterance;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;
import sun.misc.IOUtils;

import javax.sound.sampled.AudioFileFormat;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
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
    private int featureLength = -1;
    private LinkedList<Vector> allFeatures;

    public FeatureCreater(ConfigurationManager cm, String frontEndName)
            throws IOException {
        try {
            frontEnd = (FrontEnd) cm.lookup(frontEndName);
            audioSource = (StreamDataSource) cm.lookup("streamDataSource");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Vector> getFeatures(Utterance audioData) {
        try {
            URL url = new File(Util.resourcesDirectory + "/frontend.config.xml").toURI().toURL();
            ConfigurationManager cm = new ConfigurationManager(url);
            String frontEndName = "cepstraFrontEnd";

            if (cm.lookup(frontEndName) == null) {
                throw new RuntimeException("No such frontend: " + frontEndName);
            }

            FeatureCreater creator = new FeatureCreater(cm, frontEndName);
            //List<float[]> features = creator.processStream(audioData);
            try {
                audioData.save(Util.resourcesDirectory + "/tmp/last.wav", AudioFileFormat.Type.WAVE);
            }catch(IOException ioe){
                System.out.println("Error: "+ioe.getMessage());
            }
            creator.processFile(Util.resourcesDirectory+"/tmp/last.wav");
            return creator.allFeatures;
        } catch (IOException ioe) {
            System.err.println("I/O Error " + ioe);
        } catch (PropertyException p) {
            System.err.println("Bad configuration " + p);
        }
        return null;
    }

    private List<Vector> processStream(Utterance utterance) {
        if(utterance==null){
            throw new InvalidParameterException("Utterance is null");
        }
        audioSource.setInputStream(new ByteArrayInputStream(utterance.getAudio()));
        allFeatures = new LinkedList<Vector>();
        this.getAllFeatures();
        return allFeatures;
    }

    public void processFile(String inputAudioFile) throws FileNotFoundException {
        audioSource .setInputStream(new FileInputStream(inputAudioFile));
        allFeatures = new LinkedList<Vector>();
        this.getAllFeatures();
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
                    allFeatures.add(new Vector(convertedData));
                } else if (feature instanceof FloatData) {
                    float[] featureData = ((FloatData) feature).getValues();
                    if (featureLength < 0) {
                        featureLength = featureData.length;
                    }
                    allFeatures.add(new Vector(featureData));
                }
                feature = frontEnd.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
