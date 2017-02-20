package eu.budick;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by daniel on 20.02.17.
 */
public class Microphone extends Thread {
	private boolean active = false;
	private TargetDataLine microphone;

	AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

	private static Microphone ourInstance = new Microphone();
	
	private Blackboard blackBoard;

	public static Microphone getInstance() {
		return ourInstance;
	}

	/**
	 * Closes the target data line to finish capturing and recording
	 */
	void finish() {
		this.active = false;
		microphone.stop();
		microphone.close();
		System.out.println("Finished");
	}

	public void run() {
		while (this.blackBoard.isListing()) {
			System.out.println("Listening");
			// numBytesRead = line.read(data, 0, data.length);
			// out.write(data, 0, numBytesRead);
		}
		this.run();
	}

	public void activate() {
		this.active = true;
		try {
			// AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
			// DataLine.Info info = new DataLine.Info(TargetDataLine.class,
			// format);
			//
			// // checks if system supports the data line
			// if (!AudioSystem.isLineSupported(info)) {
			// System.out.println("Line not supported");
			// System.exit(0);
			// }
			// microphone = (TargetDataLine) AudioSystem.getLine(info);
			// microphone.open(format);
			microphone.start(); // start capturing

			System.out.println("Start capturing...");

			AudioInputStream ais = new AudioInputStream(microphone);

			System.out.println("Start recording...");

			String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-m-ss").format(new Date());

			File wavFile = new File(Util.resourcesDirectory + "/WAV/Audio" + timeStamp + ".WAV");
			wavFile.createNewFile();

			AudioSystem.write(ais, fileType, wavFile);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void deactivate() {
		this.active = false;
		this.microphone.stop();
	}

	private Microphone() {
		this.blackBoard = Blackboard.getInstance();
		AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
		try {
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			// checks if system supports the data line
			if (!AudioSystem.isLineSupported(info)) {
				System.out.println("Line not supported");
				System.exit(0);
			}
			this.microphone = (TargetDataLine) AudioSystem.getLine(info);
			// this.microphone = AudioSystem.getTargetDataLine(format);
			this.microphone.open(format);

				this.run();
			
		
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
		}
	}
}