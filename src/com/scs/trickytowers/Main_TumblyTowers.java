package com.scs.trickytowers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import com.scs.trickytowers.entity.Entity;
import com.scs.trickytowers.entity.VibratingPlatform;
import com.scs.trickytowers.entity.components.ICollideable;
import com.scs.trickytowers.entity.components.IDrawable;
import com.scs.trickytowers.entity.components.IProcessable;
import com.scs.trickytowers.entity.systems.DrawingSystem;
import com.scs.trickytowers.input.IInputDevice;
import com.scs.trickytowers.input.KeyboardInput;
import com.scs.trickytowers.input.PS4Controller;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import ssmith.audio.AudioPlayer;
import ssmith.awt.ImageCache;
import ssmith.lang.Functions;
import ssmith.util.TSArrayList;

public class Main_TumblyTowers implements ContactListener, KeyListener {

	public World world;
	public MainWindow window;

	private TSArrayList<Entity> entities = new TSArrayList<Entity>();
	private List<Controller> foundControllers = new ArrayList<Controller>();

	private long lastCheckTime;
	private List<Player> players = new ArrayList<>();
	private boolean keyboard1Created, keyboard2Created;
	private DrawingSystem drawingSystem;
	private List<Contact> collisions = new LinkedList<>();
	private int[] leftPos;
	private int[] rightPos;
	private Font font;
	private long lastCollisionTime = 0;
	private int currentBackgroundOption = Statics.rnd.nextInt(3);

	private boolean restartLevel = false, createKeyboard1 = false, createKeyboard2 = false, menuStage = true;
	private long restartOn;
	private Image background;
	private ArrayList<String> log = new ArrayList<String>();	

	public static void main(String[] args) {
		new Main_TumblyTowers();
	}


	public Main_TumblyTowers() {
		super();

		System.setProperty("net.java.games.input.librarypath", new File("libs/jinput").getAbsolutePath());

		if (Statics.RELEASE_MODE == false) {
			// Test all sounds
			File[] files = new File("bin/assets/sfx").listFiles();
			for (File file : files) {
				this.playSound(file.getName());
			}
		}

		try{
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Font font = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/assets/fonts/SHOWG.TTF"));
			ge.registerFont(font);
		} catch(Exception e){
			e.printStackTrace();
		}

		window = new MainWindow(this);

		//try {
		font = new Font("Showcard Gothic", Font.BOLD, 18);
		Statics.img_cache = new ImageCache("assets/gfx/");

		drawingSystem = new DrawingSystem(this);

		this.addLogEntry("Choose a game map now.");
		this.addLogEntry("Press K and J to select the background.");
		this.addLogEntry("When you're ready, press SPACE to join!");


		startLevel();
		this.gameLoop();

		/*} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(window, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}*/

	}


	private void startLevel() {
		this.entities.clear();// = new TSArrayList<Entity>();

		Vec2 gravity = new Vec2(0f, 9.81f);
		world = new World(gravity);
		world.setContactListener(this);

		leftPos = new int[this.players.size()];
		rightPos = new int[this.players.size()];
		float bucketWidth = Statics.WORLD_WIDTH_LOGICAL/7;

		if (this.players.size() > 0) {
			this.addLogEntry("Starting level!");

			if (this.players.size() == 1) {
				leftPos[0] = (int)(Statics.WORLD_WIDTH_LOGICAL-bucketWidth)/2;
				rightPos[0] = (int)(Statics.WORLD_WIDTH_LOGICAL+bucketWidth)/2;
			} else if (this.players.size() == 2) {
				leftPos[0] = (int)(bucketWidth/2);
				rightPos[0] = leftPos[0] + (int)bucketWidth;
				leftPos[1] = (int)(Statics.WORLD_WIDTH_LOGICAL-bucketWidth-(bucketWidth/2));
				rightPos[1] = leftPos[1] + (int)bucketWidth;
			} else if (this.players.size() == 3) {
				leftPos[0] = (int)(bucketWidth/2);
				rightPos[0] = leftPos[0] + (int)bucketWidth;
				leftPos[1] = (int)(Statics.WORLD_WIDTH_LOGICAL-bucketWidth)/2;
				rightPos[1] = (int)(Statics.WORLD_WIDTH_LOGICAL+bucketWidth)/2;
				leftPos[2] = (int)(Statics.WORLD_WIDTH_LOGICAL-bucketWidth-(bucketWidth/2));
				rightPos[2] = leftPos[2] + (int)bucketWidth;
			}

			// Create avatars
			for (Player player : this.players) {
				player.currentShape = null;
				//leftPos[i-1] = (int)((secWidth*i)-(bucketWidth/2));
				//rightPos[i-1] = (int)((secWidth*i)+(bucketWidth/2));

				// Create edges
				/*Edge leftEdge = new Edge(this, this.getLeftBucketPos(player.id_ZB), (float)(Statics.WORLD_HEIGHT_LOGICAL/2), this.getLeftBucketPos(player.id_ZB), (float)(Statics.WORLD_HEIGHT_LOGICAL-10));
			this.addEntity(leftEdge);

			Edge rightEdge = new Edge(this, this.getRightBucketPos(player.id_ZB), (float)(Statics.WORLD_HEIGHT_LOGICAL-10), this.getRightBucketPos(player.id_ZB), (float)(Statics.WORLD_HEIGHT_LOGICAL/2));
			this.addEntity(rightEdge);*/

				VibratingPlatform v = new VibratingPlatform(this, this.getCentreBucketPos(player.id_ZB), (float)(Statics.WORLD_HEIGHT_LOGICAL-20), bucketWidth*0.9f);
				this.addEntity(v);

				player.vib = v;
			}
			// todo - play start sound
		}

		switch (currentBackgroundOption) {
		case 0:
			background = Statics.img_cache.getImage("Castle Sunset.jpg", window.getWidth(), window.getHeight());
			break;
		case 1:
			background = Statics.img_cache.getImage("City Night.jpg", window.getWidth(), window.getHeight());
			break;
		case 2:
			background = Statics.img_cache.getImage("Jungle Day.jpg", window.getWidth(), window.getHeight());
			break;
		}

	}


	private void gameLoop() {
		final long interpolMillis = 1000/Statics.FPS;
		final float timeStepSecs = 1.0f / Statics.FPS;//10.f;
		final int velocityIterations = 6;//8;//6;
		final int positionIterations = 4;//3;//2;

		while (window.isVisible()) {

			window.checkBufferStrategy();

			long start = System.currentTimeMillis();

			this.checkForControllers();

			// Check for new players
			if (this.createKeyboard1) {
				this.createKeyboard1 = false;
				if (this.keyboard1Created == false) {
					keyboard1Created = true;
					this.createPlayer(new KeyboardInput(window, KeyboardInput.KEYBOARD1_ID));
				}
			}
			if (this.createKeyboard2) {
				this.createKeyboard2 = false;
				if (this.keyboard2Created == false) {
					keyboard2Created = true;
					this.createPlayer(new KeyboardInput(window, KeyboardInput.KEYBOARD2_ID));
				}
			}

			if (restartLevel && this.restartOn < System.currentTimeMillis()) {
				restartLevel = false;
				this.startLevel();
			}

			this.entities.refresh();

			// Preprocess
			for (Entity e : this.entities) {
				if (e instanceof IProcessable) {
					IProcessable id = (IProcessable)e;
					id.preprocess(interpolMillis);
				}
			}

			for (Player player : this.players) {
				player.process();
			}

			collisions.clear();
			world.step(timeStepSecs, velocityIterations, positionIterations);
			while (collisions.isEmpty() == false) {
				processCollision(collisions.remove(0));
			}

			// Draw screen
			Graphics g = window.BS.getDrawGraphics();
			g.setFont(font);

			g.drawImage(this.background, 0, 0, this.window);

			g.setColor(Color.white);
			for (int i=0 ; i<this.log.size() ; i++) {
				g.drawString(this.log.get(i), 20, 200+(i*22));
			}
			/*if (!Statics.RELEASE_MODE) {
				g.drawString("Num Entities: " + this.entities.size(), 400, 70);
			}*/
			g.setColor(Color.white);
			g.drawLine(0, (int)(Statics.LOGICAL_WINNING_HEIGHT * Statics.LOGICAL_TO_PIXELS), window.getWidth(), (int)(Statics.LOGICAL_WINNING_HEIGHT * Statics.LOGICAL_TO_PIXELS));

			drawingSystem.startOfDrawing(g);
			for (Entity e : this.entities) {
				if (e instanceof IDrawable) {
					IDrawable sprite = (IDrawable)e;
					sprite.draw(g, drawingSystem);
				}
				if (e instanceof IProcessable) {
					IProcessable id = (IProcessable)e;
					id.postprocess(interpolMillis);
				}
			}
			drawingSystem.endOfDrawing();
			drawingSystem.paintFixedMenu(g,window);
			window.BS.show();

			long diff = System.currentTimeMillis() - start;
			if (diff < interpolMillis) {
				try {
					Thread.sleep(interpolMillis - diff);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		System.exit(0);
	}


	public int getLeftBucketPos(int playernum) {
		return leftPos[playernum];
	}


	public int getRightBucketPos(int playernum) {
		return rightPos[playernum];
	}


	public int getCentreBucketPos(int playernum) {
		return (leftPos[playernum] + rightPos[playernum])/2;
	}


	public float getShapeStartPosX(int playernum) {
		return Functions.rndFloat(leftPos[playernum], rightPos[playernum]);
	}


	private void processCollision(Contact contact) {
		if (lastCollisionTime + 200 < System.currentTimeMillis()) {
			this.playSound("collide.wav");
			this.lastCollisionTime = System.currentTimeMillis();
		}

		Body ba = contact.getFixtureA().getBody();
		Body bb = contact.getFixtureB().getBody();

		BodyUserData ba_ud = (BodyUserData)ba.getUserData();
		BodyUserData bb_ud = (BodyUserData)bb.getUserData();

		//Statics.p("BeginContact BodyUserData A:" + ba_ud);
		//Statics.p("BeginContact BodyUserData B:" + bb_ud);

		if (ba_ud != null && bb_ud != null) {
			Entity entityA = ba_ud.entity;
			Entity entityB = bb_ud.entity;

			//Statics.p("BeginContact Entity A:" + entityA);
			//Statics.p("BeginContact Entity B:" + entityB);

			if (entityA instanceof ICollideable) {
				ICollideable ic = (ICollideable) entityA;
				ic.collided(entityB);
			}
			if (entityB instanceof ICollideable) {
				ICollideable ic = (ICollideable) entityB;
				ic.collided(entityA);
			}

		}

	}


	@Override
	public void beginContact(Contact contact) {
		this.collisions.add(contact);

	}


	@Override
	public void endContact(Contact contact) {
		//p("EndContact");

	}


	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}


	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}


	public void removeEntity(Entity b) {
		b.cleanup(world);

		synchronized (entities) {
			this.entities.remove(b);
		}		
	}


	private void createPlayer(IInputDevice input) {
		if (this.players.size() < 3) {
			Player player = new Player(this, input);
			synchronized (players) {
				this.players.add(player);
			}
			this.restartLevel = true;
			this.restartOn = 0;
			this.addLogEntry("Player created for " + input);
		} else {
			this.addLogEntry("No room left for more players!");
		}
	}


	public void addEntity(Entity o) {
		synchronized (entities) {
			if (!Statics.RELEASE_MODE) {
				if (this.entities.contains(o)) {
					throw new RuntimeException(o + " has already been added");
				}
			}
			this.entities.add(o);
		}
	}


	@Override
	public void keyPressed(KeyEvent arg0) {

	}


	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
			this.createKeyboard1 = true;
			this.menuStage = false;
		} else if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
			this.createKeyboard2 = true;
			this.menuStage = false;
		} else if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		} else if (ke.getKeyCode() == KeyEvent.VK_R) {
			this.restartLevel = true;
			this.restartOn = 0;
		}else if (ke.getKeyCode() == KeyEvent.VK_K){
			if(this.menuStage == true){
				if(this.currentBackgroundOption == 0){
					this.currentBackgroundOption= 2;
					
				}else{
					this.currentBackgroundOption -=1;
				}
	
				this.restartLevel = true;
				this.restartOn = 0;
			}
		}else if (ke.getKeyCode() == KeyEvent.VK_L){
			if(this.menuStage == true){
				if(this.currentBackgroundOption == 2){
					this.currentBackgroundOption = 0;
				}else{
					this.currentBackgroundOption +=1;
	
				}
				this.restartLevel = true;
				this.restartOn = 0;
			}
			
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		//  Who uses this?!
	}


	public void playerWon(Player player) {
		if (restartLevel) { // Already got a winner
			return;
		}
		
		this.playSound("success.wav");
		this.addLogEntry("Player " + (player.id_ZB+1) + " has won!");
		this.restartLevel = true;
		this.restartOn = System.currentTimeMillis() + 4000;

		// Remove other player's vibrating platforms
		synchronized (players) {
			for (Player p : this.players) {
				if (player != p) {
					this.removeEntity(p.vib);
				}
			}
		}
	}


	private void addLogEntry(String s) {
		this.log.add(s);
		while (this.log.size() > 5) {
			this.log.remove(0);
		}
	}


	private void checkForControllers() {
		if (lastCheckTime + 2000 > System.currentTimeMillis()) {
			return;
		}
		lastCheckTime = System.currentTimeMillis();
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		for (Controller c : controllers) {
			if (this.foundControllers.contains(c) == false) {
				c.poll();
				Event event = new Event();
				EventQueue queue = c.getEventQueue();
				while (queue.getNextEvent(event)) {
					Component comp = event.getComponent();
					if (comp.isAnalog() == false) { // Only interested in digital events
						if (event.getValue() > 0.5f) {
							if (comp.getIdentifier() == Identifier.Button._1) {
								this.createPlayer(new PS4Controller(c));
								this.foundControllers.add(c);
								this.addLogEntry("Player joined");
							}
						}
					}
				}
			}
		}
	}


	public void playSound(String filename) {
		new AudioPlayer("assets/sfx/" + filename, false).start();
	}

}