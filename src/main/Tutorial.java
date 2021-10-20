package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
/**
 * This is the class for tutorial mode.
 *<br>This is where the tutorial is made.
 *<br>January 22, 2019
 * <br>Tutorial.java
 * @author Richard Pena
 * @author Alex Co
 * @author Mbarak Al-Amry
 */
public class Tutorial extends GUI {

	/**
	 * Draws rectangle used as background for Tutorial texts and images
	 * @param gc
	 * 			The graphics context
	 */
	public static void drawTutorial(GraphicsContext gc) {
		gc.setFill(Color.DARKCYAN);
		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		
	}
	public static void draw(GraphicsContext gc) {
		//	gc.fill()
	}
}
