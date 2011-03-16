package com.balwiki.mengine;

/**
 * Utility functions that are useful in multiple places.
 * 
 * @author Michael Kelly
 */
public class Util {
	/**
	 * Checks if two boxes are colliding.
	 * 
	 * @param x1 X position of the first box's top left corner
	 * @param y1 Y position of the first box's top left corner
	 * @param w1 Width of the first box
	 * @param h1 Height of the first box
	 * @param x2 X position of the second box's top left corner
	 * @param y2 Y position of the second box's top left corner
	 * @param w2 Width of the second box
	 * @param h2 Height of the second box
	 * @return True if the boxes collide, false otherwise
	 */
	public static boolean boxCollide(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
		// Right sides
		int r1 = x1 + w1;
		int r2 = x2 + w2;
		
		// Bottom sides
		int b1 = y1 + h1;
		int b2 = y2 + h2;
		
		if (b1 <= y2) return false;
		if (y1 >= b2) return false;

		if (r1 <= x2) return false;
		if (x1 >= r2) return false;
		
		return true;
	}
}
