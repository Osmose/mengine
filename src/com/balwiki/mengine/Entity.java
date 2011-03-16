package com.balwiki.mengine;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Represents a "thing" inside of the game world. Entities have a position,
 * a collision box (defined by x/y and width/height), and a graphical
 * representation (via a Sprite).
 * 
 * @author Michael Kelly
 */
public abstract class Entity implements Comparable<Entity> {	
	public int x;
	public int y;
	public int z;
	public int width;
	public int height;
	
	public boolean flipX = false;
	public boolean flipY = false;
	
	protected HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	protected Sprite curSprite = null;
	protected String curSpriteKey = null;
	
	protected Engine engine;
	
	protected long processStartTime;
	
	/**
	 * Animates the current sprite if needed. Subclasses will
	 * most likely override this with extra functionality.
	 */
	public void process() {
		long newProcessStartTime = System.currentTimeMillis();
		long timePassed = newProcessStartTime - processStartTime;
		processStartTime = newProcessStartTime;
		
		if (curSprite != null) {
			curSprite.addTime(timePassed);
		}
	}
	
	/**
	 * Draws the current sprite to the screen if needed. Some
	 * subclasses, like tilemaps, are drawn differently and
	 * will override this function.
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		if (curSprite != null) {
			BufferedImage curFrame = curSprite.getFrame();
			g.drawImage(curFrame, 
					x + (flipX ? curFrame.getWidth() : 0), 
					y + (flipY ? curFrame.getHeight() : 0), 
					curFrame.getWidth() * (flipX ? -1 : 1), 
					curFrame.getHeight() * (flipY ? -1 : 1), 
					null);
		}
	}
	
	/**
	 * Checks if the given entity and this entity are colliding. Does a 
	 * simple box collision by default using x,y,width, and height.
	 * 
	 * @param e		Entity to check for collision with
	 * @param dx	Desired X movement for the other entity
	 * @param dy	Desired Y movement for the other entity
	 * @return		True if colliding, false otherwise
	 */
	public CollisionEvent collidesWith(Entity e, int dx, int dy) {
		if (Util.boxCollide(x, y, width, height, e.x + dx, e.y + dy, e.width, e.height)) {
			return CollisionEvent.create(x, y, width, height, this);
		}
		
		return null;
	}
	
	/**
	 * Creates a static sprite and stores it in this sprite map
	 * 
	 * @param key
	 * @param frame
	 */
	public void addSprite(String key, BufferedImage frame) {
		Sprite newSprite = new Sprite(frame);
		sprites.put(key, newSprite);
	}
	
	/**
	 * Creates an animated sprite and stores it in this sprite map
	 * 
	 * @param key
	 * @param frames
	 * @param delays
	 */
	public void addSprite(String key, BufferedImage[] frames, long[] delays) {
		Sprite newSprite = new Sprite(frames, delays);
		sprites.put(key, newSprite);
	}
	
	/**
	 * Changes to the sprite in the stored sprite map with the given key.
	 * Resets any animation counters on the current sprite before changing.
	 * 
	 * If the current sprite is already set to the requested sprite, no 
	 * change is made, making this safe to call repeatedly for an animated 
	 * sprite.
	 * 
	 * @param key
	 */
	public void setSprite(String key) {
		if (!key.equals(curSpriteKey)) {
			if (curSprite != null) {
				curSprite.reset();
			}
			curSprite = sprites.get(key);
			curSpriteKey = key;
		}
	}
	
	/**
	 * Used for sorting entities in drawing order based on the z value.
	 */
	public int compareTo(Entity e) {
		return z - e.z;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}
}
