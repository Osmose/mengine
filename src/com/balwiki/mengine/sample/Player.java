package com.balwiki.mengine.sample;

import java.awt.event.KeyEvent;

import com.balwiki.mengine.CollisionEvent;
import com.balwiki.mengine.Entity;

/**
 * Entity representing the player of the game. Handles movement.
 * 
 * @author Michael Kelly
 */
public class Player extends Entity {

	private static final double grav = 1.2;	// Acceleration due to gravity, in pixels
	private static final double maxY = 12;	// maximum speed in the Y axis
	
	private double yAcc; // Current acceleration in the Y axis
	
	private boolean on_ground = true; // True if the player is standing on the ground
	
	/**
	 * Initializes the player with the default collision box at a given
	 * x/y point.
	 * 
	 * @param nx
	 * @param ny
	 */
	public Player(int nx, int ny) {
		x = nx;
		y = ny;
		width = 22;
		height = 24;
	}
	
	/**
	 * Process is called once per frame, and handles moving the player and setting what
	 * sprite should be displayed.
	 */
	@Override
	public void process() {
		// The superclass' process() automatically handles animating the current sprite
		// if needed.
		super.process();
		
		// dx and dy are the desired movement for this frame.
		int dx = 0, dy = 0;
		
		// Set dx based on keyboard input
		if (engine.keys[KeyEvent.VK_LEFT]) {
			dx -= 2;
		}
		if (engine.keys[KeyEvent.VK_RIGHT]) {
			dx += 2;
		}
		
		// Current Y acceleration is modified by grav each frame, and limited by maxY
		yAcc += grav;
		if (yAcc > maxY) {
			yAcc = maxY;
		}
		dy = (int) yAcc;
		
		// If we are standing on the ground and press the D key, we jump by setting our
		// Y acceleration to a negative value (and our current desired movement to match)
		if (on_ground && engine.keys[KeyEvent.VK_D]) {
			yAcc = -12;
			dy = -12;
		}
		
		// If we want to move in the Y axis, we need to check if anything is blocking us.
		if (dy != 0) {
			// checkCollision takes in a dx and dy argument to check our current position plus
			// our desired movement; we want to know if we're blocked where we want to move to.
			CollisionEvent yCollision = engine.checkCollision(this, 0, dy);
			
			// If we are not blocked, we move, change sprite, and mark that we're in the air.
			// If we are blocked, we want to stop our Y acceleration (for when we fall to the
			// ground).
			if (yCollision == null) {
				y += dy;
				setSprite("jump");
				on_ground = false;
			} else if (yCollision != null) {
				yAcc = 0;
				
				// If dy is positive, we were falling and are now standing on something, rather 
				// than jumping into it.
				if (dy > 0) {
					// We only switch to the standing sprite if we were previously falling. If we 
					// didn't check this, the running sprite would keep getting overridden by the 
					// standing sprite.
					if (!on_ground) {
						setSprite("stand");
					}

					// But we're standing now.
					on_ground = true;
					
					// If we're falling fast, collision might have stopped us a few pixels above the
					// ground. To fix this, we stick our bottom to the top of the ground.
					y = yCollision.boxY - height;
				} else if (dy < 0) {
					// Negative dy means we're moving up. Thus, we stick ourselves to the bottom of
					// what we hit, change to the jumping sprite, and set that we're in the air.
					y = yCollision.boxY + yCollision.boxHeight;
					setSprite("jump");
					on_ground = false;
				}
			}
		}
		
		// Now we try to move in the X axis
		if (dx != 0 && engine.checkCollision(this, dx, 0) == null) {
			x += dx;
			
			// Only run if we're on the ground
			if (on_ground) {
				setSprite("run");
			}
		} else if (on_ground) {
			// Not moving and on the ground? Then you will stand still!
			setSprite("stand");
		}

		// Flip the sprite based on which direction it is facing
		if (dx < 0) {
			flipX = true;
		} else if (dx > 0) {
			flipX = false;
		}
	}

}
