package ssmith.awt;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.scs.trickytowers.Statics;

public class ImageCache { 

	private Component c;
	private String cacheDir;
	private Hashtable<String, BufferedImage> cache;
	private ClassLoader cl = this.getClass().getClassLoader();

	public ImageCache(String _dir) {
		super();

		cacheDir = _dir;

		cache = new Hashtable<String, BufferedImage>();
	}


	public BufferedImage getImageByKey_HeightOnly(String filename, float h) {
		return getImage(filename, (int)-1, (int)h);
	}


	public BufferedImage getImage(String filename, float w, float h) {
		return getImage(filename, (int)w, (int)h);
	}


	public BufferedImage getImage(String filename, int w, int h) {
		/*if (c == null) {
			throw new RuntimeException("No component passed to imagecache");
		}*/

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

				InputStream is = cl.getResourceAsStream(this.cacheDir + filename);
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

}
