package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

/**
 * <p>This is the <b>Entity class</b>, where all the getters and setters for each type of entity is created.
 * <p>Player and Enemy are extended from this class.
 * <br>This method creates all the variables needed in making an entity.
 * </p>January 19, 2018
 * <br>Entity.java
 * @author Richard Pena
 * @author Alex Co
 * @author Mbarak Al-Amry
 */
public abstract class Entity {

	protected double x; // x coordinates of entity
	protected double y; // y coordinates of entity
	protected double xSpeed; // x speed of entity
	protected double ySpeed; // y speed of entity
	protected Rectangle hitbox; // hit box for entity
	protected double maxHealth; // Max health of entity
	protected double health; // health of entity(at any time in the game)
	protected double healthRegen; // health recovery for entity  
	Image image; // Image of entity

	/**
	 * Constructor for entity. 
	 * <br>Creates hitbox.
	 */
	public Entity() {
		super();
		hitbox = new Rectangle();
	}
	
	/**
	 * Updates hitbox location on the map.
	 * @param map
	 * map in the game.
	 */
	abstract void update(Map map, GraphicsContext gc);
	
	/**
	 * Abstract method which is a template for draw.
	 * @param gc
	 * The graphics context.
	 */
	abstract void draw (GraphicsContext gc);
	
	/**
	 * Gets the value of the x speed entity.
	 * @return x speed
	 * Returns the current value of the x speed entity.
	 */
	public double getxSpeed() {
		return xSpeed;
	}
	
	/**
	 * Sets the value of the x speed entity.
	 * @param xSpeed
	 * Sets the value of the x speed entity to the new value stored in the parameter.
	 */
	public void setxSpeed(double xSpeed) {
		this.xSpeed = xSpeed;
	}
	
	/**
	 * Gets the value of the y speed entity.
	 * @return y speed
	 * Returns the current value of the y speed entity.
	 */
	public double getySpeed() {
		return ySpeed;
	}
	
	/**
	 * Sets the value of the y speed entity.
	 * @param ySpeed
	 * Sets the value of the y speed entity to the new value stored in the parameter.
	 */
	public void setySpeed(double ySpeed) {
		this.ySpeed = ySpeed;
	}
	
	/**
	 * Gets the rectangle hitbox entity.
	 * @return hitbox
	 * Returns the current saved hitbox.
	 */
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	/**
	 * Sets the hitbox entity.
	 * @param hitbox
	 * Sets the hitbox entity to the new rectangle hitbox sent as a parameter.
	 */
	public void setHitbox(Rectangle hitbox) {
		this.hitbox = hitbox;
	}
	
	/**
	 * Gets the Image.
	 * @return image
	 * Returns the current saved image.
	 */
	public Image getImage() {
		return image;
	}
	
	/**
	 * Sets the image.
	 * @param image
	 * Sets the image entity to the new image sent as a parameter.
	 */
	public void setImage(Image image) {
		this.image = image;
	}
	
	/**
	 * Gets the value of the x coordinate entity.
	 * @return x
	 * Returns the current value of the x coordinate entity.
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Sets the value of the x coordinate entity.
	 * @param x
	 * Sets the value of the x coordinate entity to the new value sent as a parameter.
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Gets the value of the y coordinate entity.
	 * @return y
	 * returns the current value of the y coordinate entity.
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Sets the value of the y coordinate entity.
	 * @param y
	 * Sets the value of the y coordinate entity to the new value sent as a parameter.
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * Gets the value of the x speed entity.
	 * @return xSpeed
	 * Returns the current value of the x speed entity.
	 */
	public double getXSpeed() {
		return xSpeed;
	}
	
	/**
	 * Sets the value of the x speed entity.
	 * @param xSpeed
	 * Sets the value of the x speed entity to the new value sent as a parameter.
	 */
	public void setXSpeed(double xSpeed) {
		this.xSpeed = xSpeed;
	}
	
	/**
	 * Gets the value of the y speed entity.
	 * @return ySpeed
	 * Returns the current value of the y speed entity.
	 */
	public double getYSpeed() {
		return ySpeed;
	}
	
	/**
	 * Sets the value of the y speed entity.
	 * @param ySpeed
	 * Sets the value of the y speed entity to the new value sent as a parameter.
	 */
	public void setYSpeed(double ySpeed) {
		this.ySpeed = ySpeed;
	}
	
	/**
	 * Gets the value of the hitbox width entity.
	 * @return hitbox.getWidth
	 * returns the current value of the hitbox width entity.
	 */
	public double getHitboxWidth() {
		return hitbox.getWidth();
	}
	
	/**
	 * Sets the value of the hitbox width entity.
	 * @param hitboxWidth
	 * Sets the value of the hitbox width entity to the new value sent as a parameter.
	 */
	public void setHitboxWidth(double hitboxWidth) {
		this.hitbox.setWidth(hitboxWidth);
	}
	
	/**
	 * Gets the value of the hitbox height entity.
	 * @return hitbox.getHeight
	 * Returns the current value of the hitbox height entity.
	 */
	public double getHitboxHeight() {
		return hitbox.getHeight();
	}
	
	/**
	 * Sets the value of the hitbox height entity.
	 * @param hitboxHeight
	 * Sets the value of the hitbox height entity to the new value sent as a parameter.
	 */
	public void setHitboxHeight(double hitboxHeight) {
		this.hitbox.setHeight(hitboxHeight);
	}
	
	/**
	 * Gets the value of the max health entity.
	 * @return maxHealth
	 * Returns the current value of the max health entity.
	 */
	public double getMaxHealth() {
		return maxHealth;
	}
	
	/**
	 * Sets the value of the max health entity.
	 * @param maxHealth
	 * Sets the value of the max health entity to the new value sent as a parameter.
	 */
	public void setMaxHealth(double maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	/**
	 * Gets the value of the health of the entity.
	 * @return health
	 * Returns the current value of the health entity.
	 */
	public double getHealth() {
		return health;
	}
	
	/**
	 * Sets the value of the health entity.
	 * @param health
	 * Sets the value of the health entity to the new value sent as a parameter.
	 */
	public void setHealth(double health) {
		this.health = health;
	}
	
	/**
	 * Gets the value of the health regenerate entity.
	 * @return healthRegen
	 * Returns the current value of the healthRegen entity.
	 */
	public double getHealthRegen() {
		return healthRegen;
	}
	
	/**
	 * Sets the value of the health regenerate entity.
	 * @param healthRegen
	 * Sets the value of the healthRegen entity to the new value sent as a parameter.
	 */
	public void setHealthRegen(double healthRegen) {
		this.healthRegen = healthRegen;
	}
	
	/**
	 * Gets the value of the center x coordinate entity.
	 * @return centerX
	 * Returns the current value of the center x coordinate entity using the hitbox.
	 */
	public double getCenterX() {
		return x+hitbox.getWidth()/2;
	}
	
	/**
	 * Gets the value of the center y coordinate entity.
	 * @return centerY
	 * Returns the current value of the center y coordinate entity using the hitbox.
	 */
	public double getCenterY() {
		return y+hitbox.getHeight()/2;
	}
}