package com.balwiki.mengine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

/**
 * Engine is the core of the game. It manages all entities in the
 * game world, runs the game loop, monitors the keyboard, and is
 * the window that contians the game itself.
 * 
 * Games are expected to subclass Engine and implement an empty
 * constructor that calls the superclass constructor with the desired
 * dimensions, scale, and FPS. They are also expected to implement
 * initialize, which should create the entities needed to start the game.
 * 
 * @author Michael Kelly
 */
public abstract class Engine extends JFrame implements KeyListener {
	protected Screen screen;
	protected BufferedImage buffer;
	protected Graphics g;
	
	protected int trueWidth;
	protected int trueHeight;
	protected int scale;
	protected int fps;
	
	protected int msPerFrame;
	protected long frameStart;
	
	protected ArrayList<Entity> entities = new ArrayList<Entity>();
	protected ArrayList<Entity> addQueue = new ArrayList<Entity>();
	
	/**
	 * Keys contains the status of the keyboard, indexed by KeyEvent.VK 
	 * integer values. True means a key is down; false means it is up.
	 */
	public boolean[] keys = new boolean[65536];
	
	/**
	 * Creates the screen and buffer, hooks into the keyboard, initializes
	 * the game, and runs the game loop.
	 * 
	 * @param width
	 * @param height
	 * @param scale
	 * @param fps
	 */
	public Engine(int width, int height, int scale, int fps) {
		trueWidth = width;
		trueHeight = height;
		this.scale = scale;
		this.fps = fps;
		msPerFrame = 1000 / fps;
		
		// Make sure to close when we're gone, and hook into keyboard events
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(this);
		
		// Create the screen that we draw on, and pack the window to fit
		// the screen
		screen = new Screen(width * scale, height * scale);
		add(screen);
		pack();
		
		setVisible(true);
		
		// Initialize the drawing buffer
		buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g = buffer.getGraphics();
		
		// Run the subclass' game initialization
		initialize();
		
		// And bam, here's the mystical game loop. So impressive!
		while (true) {
			step();
		}
	}
	
	/**
	 * Called by the constructor to initialize the game before starting the game loop.
	 * initialize() should create all the entities and other things needed to begin
	 * playing the game.
	 */
	public abstract void initialize();
	
	/**
	 * Run once per frame. Performs processing, which includes processing input, changing
	 * position, and other non-drawing tasks.
	 * 
	 * Any overriding method in a subclass should call the superclass version, as it runs
	 * the processing for all entities.
	 */
	public void process() {
		for (Entity e : entities) {
			e.process();
		}
	}
	
	/**
	 * Draws each entity onto the specified graphics surface.
	 * 
	 * Any overriding method in a subclass should call the superclass version, as it draws
	 * all entities in the world.
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		for (Entity e : entities) {
			e.draw(g);
		}
	}
	
	/**
	 * Clears the buffer, calls the process and draw steps, then draws the buffer to
	 * the screen. Also handles adding queued entities to the world and timing to 
	 * achieve the desired FPS.
	 */
	public void step() {
		frameStart = System.currentTimeMillis();
		
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// Process and draw all entities and any extra processing added by the subclass
		process();
		draw(g);
		
		screen.drawBuffer(buffer);
		
		// Add queued entities
		entities.addAll(addQueue);
		addQueue.clear();
		
		// Timing
		while (System.currentTimeMillis() < frameStart + msPerFrame) {
			// Do nothing
		}
	}
	
	/**
	 * Adds an entity to the game world. Entities are stored in a queue and added after
	 * the current frame finishes.
	 * 
	 * @param e
	 */
	public void addEntity(Entity e) {
		e.setEngine(this);
		addQueue.add(e);
	}
	
	/**
	 * Checks for collision against every other entity in the game world.
	 * 
	 * This method is very primitive and doesn't really optimize anything. A good improvement
	 * to the engine would be implementing a quadtree or hierarchical grid to organize where
	 * objects are, so that only objects that are close enough to collide with the object
	 * are checked. In other words, google for "hierarchical grid collision"
	 * 
	 * @param e
	 * @param dx
	 * @param dy
	 * @return
	 */
	public CollisionEvent checkCollision(Entity e, int dx, int dy) {
		CollisionEvent event;
		for (Entity e2 : entities) {
			if(e != e2 && (event = e2.collidesWith(e, dx, dy)) != null) {
				return event;
			}
		}
		
		return null;
	}

	/**
	 * Below are the keylistener methods. They simply change the keys array when
	 * the keyboard state changes.
	 */
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() < keys.length) {
			keys[e.getKeyCode()] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() < keys.length) {
			keys[e.getKeyCode()] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
