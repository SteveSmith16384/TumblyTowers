package com.scs.trickytowers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import org.gamepad4j.Controllers;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import ssmith.awt.ImageCache;
import ssmith.lang.Functions;
import ssmith.util.TSArrayList;
import ssmith.util.TimedString;

import com.scs.trickytowers.entity.Entity;
import com.scs.trickytowers.entity.VibratingPlatform;
import com.scs.trickytowers.entity.components.ICollideable;
import com.scs.trickytowers.entity.components.IDrawable;
import com.scs.trickytowers.entity.components.IProcessable;
import com.scs.trickytowers.entity.systems.DrawingSystem;
import com.scs.trickytowers.input.DeviceThread;
import com.scs.trickytowers.input.IInputDevice;
import com.scs.trickytowers.input.NewControllerListener;

public class Main_TumblyTowers implements ContactListener, NewControllerListener, KeyListener {

	public World world;
	public MainWindow window;

	private TSArrayList<Entity> entities;
	private List<IInputDevice> newControllers = new ArrayList<>();
	private List<Player> players = new ArrayList<>();

	private DrawingSystem drawingSystem;
	private List<Contact> collisions = new LinkedList<>();
	private int[] leftPos;
	private int[] rightPos;
	private TimedString msg = new TimedString(2000);

	private boolean restartLevel = false;
	private long restartOn;

	public static void main(String[] args) {
		new Main_TumblyTowers();
	}


	public Main_TumblyTowers() {
		super();

		window = new MainWindow(this);

		try {
			DeviceThread dt = new DeviceThread(window);
			dt.addListener(this);
			dt.start();

			Statics.img_cache = ImageCache.GetInstance(null);
			Statics.img_cache.c = window;

			drawingSystem = new DrawingSystem();

			startLevel();
			this.gameLoop();

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(window, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	private void gameLoop() {
		long interpol = 30;
		final float timeStep = 1.0f / Statics.FPS;//10.f;
		final int velocityIterations = 6;//8;//6;
		final int positionIterations = 4;//3;//2;

		while (window.isVisible()) {

			// Check for new players
			synchronized (newControllers) {
				while (this.newControllers.isEmpty() == false) {
					if (this.players.size() < 3) {
						this.loadPlayer(this.newControllers.remove(0));
					} else {
						this.newControllers.clear();
						msg.setText("No room left!");
					}
				}
			}

			if (restartLevel && this.restartOn < System.currentTimeMillis()) {
				restartLevel = false;
				this.startLevel();
			}

			msg.process(interpol);

			this.entities.refresh();

			// Preprocess
			for (Entity e : this.entities) {
				if (e instanceof IProcessable) {
					IProcessable id = (IProcessable)e;
					id.preprocess(interpol);
				}
			}

			// Player input first
			if (DeviceThread.USE_CONTROLLERS) {
				Controllers.checkControllers();
			}

			for (Player player : this.players) {
				//this.playerInputSystem.process(player);
				player.process();
			}

			collisions.clear();
			world.step(timeStep, velocityIterations, positionIterations);
			while (collisions.isEmpty() == false) {
				processCollision(collisions.remove(0));
			}

			// Draw screen
			Graphics g = window.BS.getDrawGraphics();
			g.setColor(Color.white);
			g.fillRect(0, 0, Statics.WINDOW_WIDTH, Statics.WINDOW_HEIGHT);

			g.setColor(Color.black);
			g.drawString(msg.getString(), 20, 50);
			g.drawString("Num Entities: " + this.entities.size(), 20, 70);

			g.setColor(Color.gray);
			g.drawLine(0, (int)(Statics.WIN_HEIGHT * Statics.LOGICAL_TO_PIXELS), Statics.WINDOW_WIDTH, (int)(Statics.WIN_HEIGHT * Statics.LOGICAL_TO_PIXELS));

			drawingSystem.startOfDrawing(g);
			for (Entity e : this.entities) {
				if (e instanceof IDrawable) {
					IDrawable sprite = (IDrawable)e;
					sprite.draw(g, drawingSystem);
				}
				if (e instanceof IProcessable) {
					IProcessable id = (IProcessable)e;
					id.postprocess(interpol);
				}
			}
			drawingSystem.endOfDrawing();

			window.BS.show();

			try {
				interpol = 1000/Statics.FPS;
				Thread.sleep(interpol); // todo - calc start-end diff
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.exit(0);
	}


	private void startLevel() {
		this.entities = new TSArrayList<Entity>();

		Vec2 gravity = new Vec2(0f, 10f);
		world = new World(gravity);
		world.setContactListener(this);

		//Edge bottom = new Edge(this, 0, (float)(Statics.WORLD_HEIGHT_LOGICAL-10), Statics.WORLD_WIDTH_LOGICAL, (float)(Statics.WORLD_HEIGHT_LOGICAL-10));
		//this.addEntity(bottom);

		leftPos = new int[this.players.size()];
		rightPos = new int[this.players.size()];
		//float secWidth = Statics.WORLD_WIDTH_LOGICAL/(this.players.size()+1);
		float bucketWidth = Statics.WORLD_WIDTH_LOGICAL/7;

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
		int i=1;
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
			i++;
		}

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


	@Override
	public void newController(IInputDevice input) {
		synchronized (newControllers) {
			this.newControllers.add(input);
		}
	}


	private void loadPlayer(IInputDevice input) {
		Player player = new Player(this, input);
		synchronized (players) {
			this.players.add(player);
		}
		this.restartLevel = true;
		this.restartOn = 0;

	}


	public void addEntity(Entity o) {
		synchronized (entities) {
			if (Statics.DEBUG) {
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
		if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
			//restartLevel = true;
		} else if (ke.getKeyCode() == KeyEvent.VK_1) {
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		//  Who uses this?!
	}


	@Override
	public void controllerRemoved(IInputDevice input) {
		synchronized (players) {
			for (Player player : this.players) {
				if (player.input == input) {
					this.players.remove(player);
					break;
				}
			}
		}
	}


	public void playerWon(Player player) {
		msg.setText("Player " + player.id_ZB + " has won!");
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
}

