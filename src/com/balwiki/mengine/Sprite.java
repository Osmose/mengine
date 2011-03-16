package com.balwiki.mengine;

import java.awt.image.BufferedImage;

/**
 * A Sprite is the visual representation of an Entity. Sprites
 * can be static or animated.
 * 
 * Sprites are usually created by the addSprite methods in the
 * Entity class.
 * 
 * @author Michael Kelly
 */
public class Sprite {

	private BufferedImage[] frames;
	private long[] delays;
	
	private long curTime;
	private int curFrame;
	
	/**
	 * Creates an animated sprite using the specified images
	 * and time delays in milliseconds.
	 * 
	 * @param nFrames Array of images for each frame of the animation.
	 * @param nDelays Array of time delays for each frame in milliseconds.
	 */
	public Sprite(BufferedImage[] nFrames, long[] nDelays) {
		frames = nFrames;
		delays = nDelays;
	}
	
	/**
	 * Creates a static sprite that does not animate.
	 * 
	 * @param frame
	 */
	public Sprite(BufferedImage frame) {
		frames = new BufferedImage[] { frame };
		delays = new long[] {Long.MAX_VALUE};
	}
	
	/**
	 * Advances the time counter. If enough time has passed, the
	 * sprite changes to the next image in the animation.
	 * 
	 * @param time
	 */
	public void addTime(long time) {
		curTime += time;
		if (curTime > delays[curFrame]) {
			curTime -= delays[curFrame];
			curFrame = (curFrame + 1) % frames.length;
		}
	}
	
	/**
	 * Grabs the current frame of the animation or static sprite
	 * 
	 * @return
	 */
	public BufferedImage getFrame() {
		return frames[curFrame];
	}
	
	/**
	 * Resets the animation counters to an initial state.
	 */
	public void reset() {
		curTime = 0;
		curFrame = 0;
	}
}
