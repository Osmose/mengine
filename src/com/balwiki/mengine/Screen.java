package com.balwiki.mengine;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * Screen is the actual GUI component that the buffer is painted onto.
 * 
 * @author Michael Kelly
 */
public class Screen extends JComponent {

	private BufferedImage backbuffer;
	
	/**
	 * Creates the screen. The arguments are the scaled width and height.
	 * @param width
	 * @param height
	 */
	public Screen(int width, int height) {
		super();
		
		// setPreferredSize is used as well so that pack() works on Engine
		setSize(width, height);
		setPreferredSize(new Dimension(width, height));
		setDoubleBuffered(true);
		
		backbuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * Draws the screen buffer onto the back buffer, which is used by the
	 * paintComponent method to draw to the GUI.
	 * 
	 * @param buffer
	 */
	public void drawBuffer(BufferedImage buffer) {
		// The scaling occurs here because the source dimensions are unscaled,
		// but the backbuffer's dimensions are scaled.
		backbuffer.getGraphics().drawImage(
				buffer, 
				0, 
				0, 
				backbuffer.getWidth(), 
				backbuffer.getHeight(), 
				0, 
				0, 
				buffer.getWidth(),
				buffer.getHeight(), 
				null
		);
		
		// Call repain to force the GUI to update
		repaint();
	}

	/**
	 * Called by repaint to paint the backbuffer to the graphics object displayed
	 * in the GUI.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(
				backbuffer, 
				0, 
				0, 
				getWidth(), 
				getHeight(), 
				0, 
				0, 
				backbuffer.getWidth(),
				backbuffer.getHeight(), 
				null
		);
	}
}
