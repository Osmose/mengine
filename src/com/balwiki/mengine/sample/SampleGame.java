package com.balwiki.mengine.sample;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.balwiki.mengine.Engine;
import com.balwiki.mengine.Tilemap;

/**
 * Sample game for MEngine. A side-scrolling platformer
 * similar to Megaman.
 * 
 * @author Michael Kelly
 */
public class SampleGame extends Engine {
	
	public SampleGame() {
		// The Engine constructor initializes the engine and calls the 
		// initialize() function. It's four arguments are:
		//  * width of screen
		//  * height of screen
		//  * scale (2 would resize all sprites to 2 times their normal size)
		//  * desired frames per second
		super(256, 240, 2, 40);
	}

	/**
	 * Initialize is run before the game engine starts, and is where you
	 * initialize the starting entities in your game.
	 */
	public void initialize() {
		// Player is the entity for the PC
		Player player = new Player(10, 10);
		
		// BufferedImage is used to store most graphics to be displayed.
		// In a more advanced engine, this should be handled by a loader
		// class of some kind.
		BufferedImage tubeTile = null, 
			shoaffStand = null,
			shoaffJump = null,
			shoaffRun1 = null, 
			shoaffRun2 = null, 
			shoaffRun3 = null;
		try {
			tubeTile = ImageIO.read(new File("resources/tiles/tube.png"));
			shoaffStand = ImageIO.read(new File("resources/player/stand.png"));
			shoaffJump = ImageIO.read(new File("resources/player/jump.png"));
			shoaffRun1 = ImageIO.read(new File("resources/player/_run0.png"));
			shoaffRun2 = ImageIO.read(new File("resources/player/_run1.png"));
			shoaffRun3 = ImageIO.read(new File("resources/player/_run2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Entities can be assigned sprites. A sprite is either a static graphic or
		// and animation sequence of many graphics. Sprites are stored within the
		// entity and given a string key that is used when switching to them.
		
		// addSprite can take a string and a BufferedImage, and will create a static,
		// non-animated sprite with the string as the key.
		player.addSprite("stand", shoaffStand);
		player.addSprite("jump", shoaffJump);
		
		// It can also take an array of BufferedImages and longs; they should be
		// the same length. The BufferedImages are the frames of an animation, and
		// the longs are the time in milliseconds that each individual frame should 
		// be displayed. Here, we have 4 frames shown for 150 ms each.
		player.addSprite("run", new BufferedImage[] {
				shoaffRun1, shoaffRun2, shoaffRun3, shoaffRun2
		}, new long[] {
			150, 150, 150, 150
		});
		
		// Set sprite changes the player's graphic to the sprite matching the string
		// given. We need to set the initial sprite for the player when the game begins.
		player.setSprite("stand");
		
		// Now we add the player to the game world so that they are processed and drawn.
		addEntity(player);
		
		// Tilesets use a HashMap to map strings to sprites. Here we create a single tile.
		HashMap<Character, BufferedImage> tileset = new HashMap<Character, BufferedImage>();
		tileset.put('=', tubeTile);
		
		// See the Tilemap class for a detailed description of the parameters.
		// A string array is useful here because it can visually represent what the map will
		// be. It doesn't scale well, however. A better engine would define a map format that
		// a map editor could create; these maps would then be loaded from a file.
		// OgmoEditor is a good free map editor that generates XML map files that can be read
		// into your engine.
		Tilemap tilemap = new Tilemap(16, 16, 0, 0, 16, 15, tileset, "=", new String[] {
				"                ",
				"                ",
				"                ",
				"                ",
				"                ",
				"                ",
				"                ",
				"                ",
				"                ",
				"                ",
				"   ===          ",
				"          ==    ",
				"                ",
				"================",
				"                "
		});
		
		// Of course, since the tilemap is just an entity, we have to add it to the game world
		// as well.
		addEntity(tilemap);
	}

	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		// Since the engine is booted up using the constructor, we just call it here to start.
		SampleGame g = new SampleGame();
	}
}
