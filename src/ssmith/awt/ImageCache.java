package ssmith.awt;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.scs.trickytowers.Statics;

public class ImageCache implements Runnable { 

	private static final String RESOURCE_DIR = "assets/gfx/";
	//private static final String CACHE_DIR = "./data/images/";

	public Component c;
	private static ImageCache instance;
	private String cacheDir;
	private Hashtable<String, BufferedImage> cache;

	public static synchronized ImageCache GetInstance(String CACHE_DIR) {
		if (instance == null) {
			/*new File("./data/").mkdirs();
			if (new File(CACHE_FILE).exists()) {
				try {
					Statics.p("Loading image cache...");
					synchronized (CACHE_FILE) {
						instance = (ImageCache) Serialize.DeserializeObject(CACHE_FILE);
					}
					Statics.p("Finished loading image cache");
				} catch (Exception e) {
					e.printStackTrace();

					Statics.p("Deleting image cache");
					new File(CACHE_FILE).delete();

					instance = new ImageCache();
				}
			} else {*/
			instance = new ImageCache(CACHE_DIR);
			//}
		}
		return instance;
	}


	private ImageCache(String _CACHE_DIR) {
		super();

		cacheDir = _CACHE_DIR;

		if (cacheDir != null) {
			new File(cacheDir).mkdirs();
		}
		cache = new Hashtable<String, BufferedImage>();

		//Runtime.getRuntime().addShutdownHook(new Thread(this, "ImageCache_Save"));

		Thread t = new Thread(this, "ImageCache_Load");//.start();
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
	}


	public BufferedImage getImageByKey_HeightOnly(String filename, float h) {
		return getImage(filename, (int)-1, (int)h);
	}


	public BufferedImage getImage(String filename, float w, float h) {
		return getImage(filename, (int)w, (int)h);
	}


	public BufferedImage getImage(String filename, int w, int h) {
		if (c == null) {
			throw new RuntimeException("No component passed to imagecache");
		}

		if (filename.indexOf(".") < 0) {
			filename = filename + ".png"; // Default
		}
		try {
			if (filename != null && filename.length() > 0) {
				String key = filename + "_" + w + "_" + h;

				// Is it in the hashmap?
				BufferedImage img = null;
				synchronized (cache) {
					img = cache.get(key);
				}
				if (img != null) {
					return img;
				}

				ClassLoader cl = this.getClass().getClassLoader();
				InputStream is = cl.getResourceAsStream(RESOURCE_DIR + filename);
				if (is != null) {
					img = ImageIO.read(is);

					// Resize it
					if (w < 0) {
						// Scale proportionally
						w = (int)(((float)img.getWidth() / (float)img.getHeight()) * h);
					}

					{
						BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
						scaled.getGraphics().drawImage(img, 0, 0, w, h, c);
						Statics.p("Generated image " + filename + " of " + w + "," + h);
						img = scaled;
					}

					synchronized (cache) {
						cache.put(key, img);
					}

					// Save it
					if (cacheDir != null) {
						File saveAs = new File(cacheDir + key);
						if (saveAs.exists() == false) {
							ImageIO.write(img, "png", saveAs);
						}
					}
					return img;
				} else {
					throw new FileNotFoundException(filename);
				}
			} else {
				return null;
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}

	}


	@Override
	public void run() {
		if (cacheDir != null) {
			Statics.p("Loading image cache...");
			File files[] = new File(cacheDir).listFiles();
			for(File file : files) {
				try {
					BufferedImage img = ImageIO.read(file);
					synchronized (cache) {
						cache.put(file.getName(), img);
						Statics.p("Loaded " + file.getName());
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			Statics.p("Finished loading image cache");
		}
	}

}
