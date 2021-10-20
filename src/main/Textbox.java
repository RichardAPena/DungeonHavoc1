package main;

import java.util.ArrayList;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
/**
 * This is the Textbox class used to create the <b>textbox</b> seen in the game  
 * <br> This is done by creating the rectangle and text for the textbox 
 * <br>and the portrait image used
 * <br>January 22, 2019
 * <br>TextBox.java
 * @author Richard Pena
 * @author Alex Co
 * @author Mbarak Al-Amry
 */
public class Textbox {

	// Sets variables needed to make the text and the textbox
	private Paint textPaint = new Color(0.0, 0.0, 0.0, 1.0);

	private Paint foregroundPaint = new Color(0.0, 0.0, 0.0, 1.0);
	private Paint backgroundPaint = new Color(0.0, 0.0, 0.0, 1.0);
	private Paint arrowPaint = new Color(0.0, 0.0, 0.0, 1.0);

	double[] xPoints = {620, 660, 640};
	double[] yPoints = {550+150, 550+150, 570+150};
	int nPoints = 3;

	private Image portraitImage;

	private Font font;

	private Text text;
	private Rectangle foregroundRectangle;
	private Rectangle backgroundRectangle;

	static Textbox instance;

	static String msg;
	static boolean printTextbox = false;

	//--------------------------------
	/**
	 * Gets the font
	 * @return font 
	 * Returns the font
	 */
	public Font getFont() {
		return font;
	}
	/**
	 * Sets the font of textbox.
	 * 
	 * @param font
	 *            New font.
	 */
	public void setFont(Font font) {
		this.font = font;
	}
	/**
	 * Sets the background paint.
	 * 
	 * @param backgroundPaint
	 *            New background paint.
	 */
	public void setBackgroundPaint(Paint backgroundPaint) {
		this.backgroundPaint = backgroundPaint;
	}
	/**
	 * Sets the foreground paint.
	 * 
	 * @param foregroundPaint
	 *            New forground paint.
	 */
	public void setForegroundPaint(Paint foregroundPaint) {
		this.foregroundPaint = foregroundPaint;
	}
	/**
	 * Sets the arrow paint.
	 * 
	 * @param arrowPaint
	 *            New arrow paint.
	 */
	public void setArrowPaint(Paint arrowPaint) {
		this.arrowPaint = arrowPaint;
	}
	/**
	 * Sets the textbox message.
	 * 
	 * @param msg
	 *            New textbox message.
	 */
	public void setMessage(String msg) {
		printTextbox = true;
		Textbox.msg = msg;
	}
	/**
	 * Default constructor
	 */
	private Textbox() {
		super();
	}

	static ArrayList<String> textboxes = new ArrayList<>();
	static int index;

	public static Textbox getInstance() {
		if (instance == null) {
			instance = new Textbox();

			Thread waitForInput = new Thread(new Runnable() {
				@Override
				public void run() {
					if(Input.pause == false)
					{
						while (true) {
							if (Input.buttonPressed[Input.keyMap.get("ENTER").ordinal()] && textboxes.size() > 0) {
								if (index+1 == textboxes.size()) {
									msg = "";
									index = -1;
									textboxes.clear();
									printTextbox = false;
								} else { 
									index++;
									try { Thread.sleep(500); } catch (InterruptedException e) {}
								}
							}
							try { Thread.sleep(1000/60); } catch (InterruptedException e) {}
						}
					}
				}
			});
			waitForInput.start();
		}
		return instance;
	}
	/**
	 * Draws the rectangle for the hitbox
	 * @param gc
	 * The graphics context
	 * @param rect
	 * Rectangle used for hitbox
	 * @param colour
	 * Color used for hitbox
	 */
	private void drawRectangle(GraphicsContext gc,Rectangle rect, Paint colour){
		gc.setFill(colour);
		gc.fillRoundRect(rect.getX(),      
				rect.getY(), 
				rect.getWidth(), 
				rect.getHeight(), 45, 45);
	}
	/**
	 * Sets text Paint
	 * @param textPaint
	 * New text paint
	 */
	public void setTextPaint(Paint textPaint) {
		this.textPaint = textPaint;
	}
	/**
	 * Sets portrait image
	 * @param portraitImage
	 * New portrait image
	 */
	public void setPortraitImage(Image portraitImage) {
		this.portraitImage = portraitImage;
	}
	/**
	 * Draws textbox used in the game
	 * @param gc
	 * The graphics context
	 * @param width
	 * TextBox width
	 * @param height
	 * TextBox height
	 */
	public void drawTextBox(GraphicsContext gc, int width, int height) {
		if (textboxes.size() == 0) {
			textboxes.add(msg);
		}
		// Creates textbox
		this.text = new Text(textboxes.get(index));
		text.setTextAlignment(TextAlignment.CENTER);
		text.setFill(textPaint);
		text.setTextOrigin(VPos.CENTER);
		text.setFontSmoothingType(FontSmoothingType.LCD);
		// Draws rectangles for textbox
		this.backgroundRectangle = new Rectangle(0, 540, width, height);
		this.foregroundRectangle = new Rectangle(0+15, 540+10, width-30, height-20);
		drawRectangle(gc, this.backgroundRectangle, backgroundPaint);
		drawRectangle(gc, this.foregroundRectangle, foregroundPaint);
		// draws portrait
		drawPortrait(gc, width * 0.125, height * 0.8);
		// Sets font
		Font tmp = gc.getFont(); // Store original font.
		gc.setFont(font);

		gc.setFill(textPaint);
		// Creates words in the textbox
		String[] words = textboxes.get(index).split(" ");
		String line = "";
		String remainingText = "";
		double yPos = gc.getCanvas().getHeight() - (height * 0.80);
		double spaceOccupied = 0;

		for (int i=0; i<words.length; ++i) { 

			if (line.length() < 2140/font.getSize()) {
				line = line + " " + words[i];
			} else if (spaceOccupied >= height-40-font.getSize()) {
				for (; i<words.length; i++) { 
					remainingText = remainingText +  " " + words[i];
				} 		
				break;
			}
			else { 
				gc.setFill(Color.BLACK);
				gc.fillText(line, 144 + 60, yPos);
				spaceOccupied+=font.getSize();
				i--;
				line = ""; 
				yPos += font.getSize();
			}			

		}
		if (line.length() > 0) gc.fillText(line, 144 + 60, yPos);

		if (index < textboxes.size()-1) {
			gc.setFill(arrowPaint);
			gc.fillPolygon(xPoints, yPoints, nPoints);
		}

		boolean duplicateText = false;
		for (int i=0; i<textboxes.size(); ++i) {
			if (textboxes.get(i).equals(remainingText)) duplicateText = true;
		}
		if (remainingText.length() > 0 && !duplicateText) {
			textboxes.add(remainingText);
		}

		gc.setFont(tmp); // Restore original font.
	}
	/**
	 * Draws portrait image for the textbox.
	 * @param gc
	 * The graphics context
	 * @param width
	 * portrait width
	 * @param height
	 * portrait height
	 */
	private void drawPortrait(GraphicsContext gc, double width, double height) {
		if (portraitImage != null) {
			gc.drawImage(portraitImage, 30, 540+20, width, height);
		}
	}
}
