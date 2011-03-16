package com.balwiki.mengine;

/**
 * Represents a collision. Specifically, CollisionEvent stores the entity that
 * is being collided with, and the collision box that's being collided with.
 * Note that this implies that the collision box and entity are not the same, 
 * such as tilemaps, which use the specific tile's collision box.
 * 
 * For performance reasons, there is only one instance of CollisionEvent; this
 * may not be necessary, and is especially limiting if you need to use data from
 * two different collisions at the same time. 
 * 
 * A possible improvement would be maintaining a pool of CollisionEvents that
 * can be reused and grows when more simultaneous collisions are needed.
 */
public class CollisionEvent {
	private static CollisionEvent instance = new CollisionEvent();
	
	public int boxX;
	public int boxY;
	public int boxWidth;
	public int boxHeight;
	public Entity entity;
	
	public static CollisionEvent create(int x, int y, int width, int height, Entity e) {
		instance.boxX = x;
		instance.boxY = y;
		instance.boxWidth = width;
		instance.boxHeight = height;
		instance.entity = e;
		
		return instance;
	}
}
