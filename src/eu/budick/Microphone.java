package eu.budick;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by daniel on 20.02.17.
 */
public class Microphone implements Runnable {
    private volatile boolean running = true;
    private TargetDataLine microphone;
    private boolean initialized = false;
    private File wavFile;
    private List<float[]> lastFeatures;

    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

    private static Microphone ourInstance = null;

    public static synchronized Microphone getInstance() {
        if (ourInstance == null) {
            ourInstance = new Microphone();
            ourInstance.init();
        }
        return ourInstance;
    }

    public void activate() {
        if (!this.running) {
            this.running = true;
            this.run();
        }
    }

    public void deactivate() {
        this.running = false;
    }

    public List<float[]> getLastFeatures(){
        return this.lastFeatures;
    }

    public void run() {
        lastFeatures = null;
        AudioInputStream ais = new AudioInputStream(microphone);
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-m-ss").format(new Date());
        this.wavFile = new File(Util.resourcesDirectory + "/tmp/Audio" + timeStamp + ".WAV");
        this.microphone.start(); // start capturing
        try {
            wavFile.createNewFile();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        while (this.running) {
            try {
                AudioSystem.write(ais, this.fileType, wavFile);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        microphone.stop();
        try {
            this.lastFeatures = FeatureCreater.getFeatures(new FileInputStream(wavFile));
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }
    }

    private void init() {
        if (!this.initialized) {
            this.initialized = true;
            AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
            try {
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

                // checks if system supports the data line
                if (!AudioSystem.isLineSupported(info)) {
                    System.out.println("Line not supported");
                    System.exit(0);
                }
                this.microphone = (TargetDataLine) AudioSystem.getLine(info);
                this.microphone.open(format);
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private Microphone() {
    }
}