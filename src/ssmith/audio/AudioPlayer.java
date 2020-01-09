package ssmith.audio;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class AudioPlayer extends Thread {

	private String filename;
	private volatile boolean stop_now = false;
	public volatile boolean paused = false;
	private boolean loop;
	private ClassLoader cl = this.getClass().getClassLoader();

	public AudioPlayer(String fname, boolean _loop) {
		super(AudioPlayer.class.getSimpleName() + "_" + fname);

		loop = _loop;
		this.filename = fname;

		this.setDaemon(true);
	}


	public void run() {
		AudioInputStream din = null;
		try {
			do {
				InputStream is = cl.getResourceAsStream(filename);
				AudioInputStream in = AudioSystem.getAudioInputStream(is);
				AudioFormat baseFormat = in.getFormat();
				AudioFormat decodedFormat = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED,
						baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
						baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
						false);
				din = AudioSystem.getAudioInputStream(decodedFormat, in);
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
				SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
				if (line != null) {
					line.open(decodedFormat);
					byte[] data = new byte[4096];
					// Start
					line.start();

					int nBytesRead;
					while ((nBytesRead = din.read(data, 0, data.length)) != -1 && stop_now == false) {
						line.write(data, 0, nBytesRead);
						while (this.paused && stop_now == false) {
							Thread.sleep(200);
						}
					}
					// Stop
					line.drain();
					line.stop();
					line.close();
					//din.close();
					in.close();
				} else {
					throw new IOException("File '" + this.filename + "' does not exist");
				}
			} while (loop && !stop_now);
		} catch(Exception ex) {
			System.err.println("Cannot play '" + this.filename + "': " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if(din != null) {
				try { 
					din.close(); 
				} catch(IOException e) { 
					// Do nothing
				}
			}
		}
	}


	public void stopNow() {
		this.stop_now = true;
	}

}