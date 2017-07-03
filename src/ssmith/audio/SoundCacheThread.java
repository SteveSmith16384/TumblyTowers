package ssmith.audio;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

public class SoundCacheThread extends Thread {

	private static Hashtable<String, AudioClip> sounds = new Hashtable<String, AudioClip>(); // Cache of sound data
	private ArrayList<String> filenames = new ArrayList<String>(); // Sounds to play
	private Object lock = new Object();
	private volatile boolean stop_now = false;
	private String root;

	public SoundCacheThread(String _root) {
		super("SoundCacheThread");

		root = _root;
		this.setDaemon(true);
		start();

	}


	public void stopNow() {
		this.stop_now = true;
		this.interrupt();
	}


	public void playSound(String f) {
		synchronized (filenames) {
			this.filenames.add(f);
		}
		this.interrupt();
	}


	public void run() {
		try {
			while (!stop_now) {
				synchronized (lock) {
					// Wait until we are interupted to play a sound
					try {
						lock.wait();
					} catch (InterruptedException ex) {
						// Do nothing
					}
				}

				while (this.filenames.size() > 0) {
					final String filename = this.filenames.remove(0);
					//AppletMain.p("Playing " + filename);
					if (filename.endsWith("mp3")) {
						new MP3Player(root + filename, false).start(); // Can't cache mp3
					} else {
						if (sounds.containsKey(filename) == false) {
							ClassLoader cl = this.getClass().getClassLoader();
							URL url = cl.getResource(root + filename);
							if (url == null) {
								url = new URL("file:./" + root + filename);
							}
							AudioClip clip = Applet.newAudioClip(url);
							sounds.put(filename, clip);
						}
						Thread playsound = new Thread("SFX_Thread") {
							public void run() {
								AudioClip clip = sounds.get(filename);
								clip.play();
							}};
							playsound.start();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}


	}

}