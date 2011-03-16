package com.balwiki.mengine;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Tilemaps are an entity consisting of a grid of sprites. Tilemaps are given
 * a tileset in the form of a HashMap, and use it to draw tiles to the screen
 * and manage collision with other entities.
 * 
 * @author Michael Kelly
 */
public class Tilemap extends Entity {

	public int tileWidth;
	public int tileHeight;
	public String[] tileMap;
	public HashMap<Character, BufferedImage> tileset;
	public String solidTiles;
	
	/**
	 * Creates a tilemap.
	 * 
	 * @param tw		Width of each tile
	 * @param th		Height of each tile
	 * @param mapX		X position of the entire tilemap in pixels
	 * @param mapY		Y position of the entire tilemap in pixels
	 * @param mapWidth	Width of the entire tilemap in tiles
	 * @param mapHeight	Height of the entire tilemap in tiles
	 * @param tiles		Hashmap mapping characters to BufferedImages for the tiles
	 * @param solids	String of all characters that are considered "solid"
	 * @param map		Array of strings defining the map. Uses characters from the tilemap.
	 */
	public Tilemap(int tw, int th, int mapX, int mapY, int mapWidth, int mapHeight, HashMap<Character, BufferedImage> tiles, String solids, String[] map) {
		tileWidth = tw;
		tileHeight = th;
		tileMap = map;
		tileset = tiles;
		
		x = mapX;
		y = mapY;
		width = mapWidth;
		height = mapHeight;
		solidTiles = solids;
	}
	
	public void process() {
		// No processing needed
	}

	/**
	 * Iterates through each tile in the tilemap and draws it at the correct
	 * position on the screen.
	 */
	public void draw(Graphics g) {
		BufferedImage tile;
		for (int mx = 0; mx < width; mx++) {
			for (int my = 0; my < height; my++) {
				tile = tileset.get(tileMap[my].charAt(mx));
				if (tile != null) {
					g.drawImage(tile, x + (mx * tileWidth), y + (my * tileHeight), tileWidth, tileHeight, null);
				}
			}
		}
	}

	/**
	 * Iterates through each tile in the tilemap and checks if it is solid and
	 * if it collides with the player. Returns the first tile that collides with
	 * the player, or null if there are none.
	 * 
	 * A useful improvement would be to store the solid tiles in a quadtree or a
	 * hierarchical grid, and use the entity's position relative to the tilemap
	 * to limit the collision check to the relevant section of tiles.
	 */
	public CollisionEvent collidesWith(Entity e, int dx, int dy) {
		for (int mx = 0; mx < width; mx++) {
			for (int my = 0; my < height; my++) {
				if (solidTiles.indexOf(tileMap[my].charAt(mx)) != -1) {
					if (Util.boxCollide(x + (mx * tileWidth), y + (my * tileHeight), tileWidth, tileHeight, e.x + dx, e.y + dy, e.width, e.height)) {
						return CollisionEvent.create(
								mx * tileWidth,
								my * tileHeight,
								tileWidth,
								tileHeight,
								this
						);
					}
				}
			}
		}
		
		return null;
	}

}
