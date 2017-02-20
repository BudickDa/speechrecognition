package eu.budick;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import java.util.Scanner;

/**
 * Created by daniel on 20.02.17.
 */
public class Microphone extends Thread {
    private boolean active = false;
    private TargetDataLine microphone;

    private static Microphone ourInstance = new Microphone();

    public static Microphone getInstance() {
        return ourInstance;
    }

    public void run() {
        while (this.active) {
            //numBytesRead =  line.read(data, 0, data.length);
            //out.write(data, 0, numBytesRead);
        }
    }


    public void activate() {
        this.active = true;
        this.microphone.start();
    }

    public void deactivate(){
        this.active = false;
        this.microphone.stop();
    }

    private Microphone() {
        AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
        try {
            this.microphone = AudioSystem.getTargetDataLine(format);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}