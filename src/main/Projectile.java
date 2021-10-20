package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * <p>This is the <b>Projectiles</b> class that allows players and enemies to shoot projectile based attacks 
 * <br> at each other to inflict damage.
 * <br>It extends the combat class as another way for a player or enemy to attack.</p>
 * This class includes:
 * <li>Rotate Image
 * <br> Rotates image of projectile
 * <li>Circle hit
 * <br> Creates a Circle variable for projectile and checks if the circle projectile intersects the hitbox of the player or enemy target.
 * <li> Sqaure hit 
 * <br> Creates a Rectangle variable for projectile and checks if the rectangle projectile intersects the hitbox of the player or enemy target.
 * <li> Projectile Hit
 * <br> Checks if the projectile hits an enemy or player and destroys it 
 * <li> Parrying
 * <br>This class also creates parrying for the player, meaning reflects projectiles if the parry ability is used.
 * <br>January 22, 2019
 * <br>Projectile.java
 * @author Richard Pena
 * @author Alex Co
 * @author Mbarak Al-Amry
 */
public class Projectile extends Combat {

	private double xSpeed; // The x speed of the projectile
	private double ySpeed; // The y speed of the projectile
	private double theta; // The angle used from the player to where it's clicked
	Media hit = new Media(getClass().getResource("/files/Damage4.m4a").toString()); // gets an image
	static List<Projectile> projectiles = Collections.synchronizedList(new ArrayList<Projectile>()); // list of projectiles

	private boolean destroyOnHit = true; // Creates the variable destroyOnHit to check whether the projectile hit it's target to destroy the projectile shot  
	/**
	 * This constructor is for colored balls used as projectiles.
	 *<br> It gets all the things needed to make the colored balls a projectile as a parameter then sets it.
	 * @param x
	 * The colored ball beginning x coordinate
	 * @param y
	 * The colored ball beginning y coordinate
	 * @param radius
	 * Colored ball radius
	 * @param xSpeed
	 * The colored ball x speed
	 * @param ySpeed
	 * The colored ball y speed
	 * @param source
	 * Checks to see who's throwing the projectile (Player or Enemy)
	 * @param damage
	 * A variable holding how much damage the projectile does
	 */
	public Projectile(double x, double y, double radius, double xSpeed, double ySpeed, String source, double damage) {
		super();
		isColor = true;
		if (source.equals("ENEMY")) this.color = Color.RED;
		if (source.equals("PLAYER")) this.color = Color.BLUE;
		this.radius = radius;
		this.x = x;
		this.y = y;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		pauseDuration = 1000/60;
		this.source = source;
		this.damage = damage;
		this.c = new Circle(x, y, radius); // Creates the circle c with the x,y coordinates and radius
		deadTimer = 120; // sets deadTimer
		startThread();
	} 
	/**
	 * This constructor is for images used as projectiles. 
	 * <br>It gets all the things needed to make the image a projectile as a parameter then sets it.
	 * @param image
	 * Image used as the projectile
	 * @param x
	 * The Images beginning x coordinate
	 * @param y
	 * The Images beginning y coordinate
	 * @param xSpeed
	 * The images x speed
	 * @param ySpeed
	 * The images y speed
	 * @param source
	 * Checks to see who's throwing the projectile( Player or Enemy)
	 * @param damage
	 * A variable holding how much damage the projectile delivers
	 */
	public Projectile(Image image, double x, double y, double xSpeed, double ySpeed, String source, double damage) {
		super();
		this.image = image;
		isImage = true;
		//this.radius = radius;
		this.x = x;
		this.y = y;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		pauseDuration = 1000/60;
		this.source = source;
		this.damage = damage;
		deadTimer = 120; // sets deadTimer
		this.hitbox = new Rectangle(x, y, image.getWidth(), image.getHeight()); // Creates a rectangle called hitBox
		startThread();
		theta = calculateAngle(x, y, Input.mouseX, Input.mouseY); // calculates the angle for theta
		theta = Math.PI-theta;
	}

	ImageView iv; // Creates the variable iv to change the properties of an image
	SnapshotParameters params; // Creates the variable params to change attributes of an image
	Image rotatedImage; // Creates an Image variable for rotated images
	/**
	 * This method is used to rotate the projectile images thrown 
	 * <br>This is done by angles used to check where to rotate it 
	 * @param image
	 * Image needed to rotate
	 * @return rotatedimage
	 * returns the rotated image/projectile 
	 */
	private Image rotateImage(Image image) {
		iv.setRotate(Math.toDegrees(theta-Math.PI/2));
		params.setFill(Color.TRANSPARENT);
		rotatedImage = iv.snapshot(params, null);
		return rotatedImage;
	}
	/**
	 * This method is called in the run() method every frame. Whenever this method is called, the x and y coordinates
	 * <br>of this projectile are updated based on the xSpeed and ySpeed values of the projectile. 
	 * <br>It also sets the hitBox and center of the circles, x and y coordinates.
	 */

	public void animateOneStep() {
		deadTimer--;
		if (deadTimer < 0) dead = true;
		if (moving) {

			x += xSpeed; // updates x coordinate so the projectile moves to the xspeed
			y += ySpeed; // updates y coordinate so the projectile moves to the yspeed
			if (isColor) {
				this.c.setCenterX(x); 
				this.c.setCenterY(y);
			}
			if (isImage) {
				this.hitbox.setX(x);
				this.hitbox.setY(y);
			}
		}
	}
	/**
	 * Checks to see if the colored ball projectile hits its required target.
	 * <br>This is done by using the circle c to check if a player or enemy is within the projectiles circle bounds
	 * <br>if it is returns true else returns false.
	 * @param player
	 * The player used in the game, used to check if they intersect the projectile
	 * @return
	 * returns true or false as in if the projectile hit something or not
	 */
	private boolean circleHit(Player player) { 
		if (source.equals("PLAYER") && !dead) { // Checks if it is a player shooting the projectile
			for (int i=0; i<Enemy.enemyList.size(); ++i) {
				if (Enemy.enemyList.get(i).hitbox.intersects(this.c.getLayoutBounds()))  { // Checks to see if the enemy hitbox intersects with the projectile
					Enemy.enemyList.get(i).setHealth(Enemy.enemyList.get(i).getHealth()-damage); 
					return true; // returns true since the enemy has been hit/intersected with the projectile
				}
			}
		}
		if (source.equals("ENEMY") && !dead && !player.isInvincible()) { // Checks if it is an enemy shooting the projectile
			if (player.hitbox.intersects(this.c.getLayoutBounds())) { // Checks to see if the player intersects with the projectile
				if (!player.isParrying()) { // Checks to see if the player has not parried the attack 
					player.setHealth(player.getHealth()-damage);
					return true; // returns true since the player has been hit/intersected with the projectile
				} else {
					this.source = "PLAYER"; // Changes who shot the projectile to player since the projectile has been parried
					xSpeed*=-1;
					ySpeed*=-1;
				}

			}
		}
		return false; // returns false if the projectile did not hit an enemy or player
	}
	/**
	 * Checks to see if the image projectile hits its required target.
	 * <br>This is done by using the rectangle hitBox to check if a player or enemy is within the projectiles rectangle bounds
	 * <br>if they are returns true else returns false.
	 * @param player
	 * The player used in the game, used to check if they intersect the projectile
	 * @return
	 * returns true or false as in if the projectile hit something or not
	 */
	private boolean squareHit(Player player) {
		if (source.equals("PLAYER") && !dead) { // Checks if it is a player shooting the projectile
			for (int i=0; i<Enemy.enemyList.size(); ++i) {
				if (Enemy.enemyList.get(i).hitbox.intersects(this.hitbox.getLayoutBounds()))  { // Checks to see if the enemy intersects the projectile
					Enemy.enemyList.get(i).setHealth(Enemy.enemyList.get(i).getHealth()-damage);
					return true; // returns true since the enemy has been hit/intersected with the projectile
				}
			}
		}
		if (source.equals("ENEMY") && !dead && !player.isInvincible()) { // Checks if it is an enemy shooting the projectile
			if (player.hitbox.intersects(this.hitbox.getLayoutBounds())) { // Checks to see if the player intersects with the projectile
				player.setHealth(player.getHealth()-damage);
				return true; // returns true since the player has been hit/intersected with the projectile
			}
		}
		return false; // returns false if the projectile did not hit an enemy or player
	}
	/**
	 * This method destroys projectiles after they have hit an enemy or player.
	 * @param player
	 * The player used in the game, used to check if it is hit 
	 */
	private void projectileHit(Player player) {
		try {
			//should check for square-square, square-circle, circle-square and circle-circle
			for (int i=0; i<projectiles.size(); ++i) { // Creates for loop to check each projectile
				// checks projectiles to see if the projectile hit an enemy and if it does sets it to dead
				if (isColor) {
					if (projectiles.get(i).isColor) {
						if (c.intersects(projectiles.get(i).c.getLayoutBounds()) && projectiles.get(i).source.equals("ENEMY")) { // checks projectiles to see if the projectile hit an enemy
							if (i < projectiles.size() || projectiles.size() > 0) projectiles.get(i).setDead(true);
						}
					} else if (projectiles.get(i).isImage) {
						if (c.intersects(projectiles.get(i).hitbox.getLayoutBounds()) && projectiles.get(i).source.equals("ENEMY")) { // checks projectiles to see if the projectile hit an enemy
							if (i < projectiles.size() || projectiles.size() > 0) projectiles.get(i).setDead(true);
						}
					}
				}
				if (isImage) {
					if (projectiles.get(i).isColor) {
						if (hitbox.intersects(projectiles.get(i).c.getLayoutBounds()) && projectiles.get(i).source.equals("ENEMY")) {
							if (i < projectiles.size() || projectiles.size() > 0) projectiles.get(i).setDead(true);
						}
					} else if (projectiles.get(i).isImage) {
						if (hitbox.intersects(projectiles.get(i).c.getLayoutBounds()) && projectiles.get(i).source.equals("ENEMY")) { // checks projectiles to see if the projectile hit an enemy
							if (i < projectiles.size() || projectiles.size() > 0) projectiles.get(i).setDead(true);
						}
					}

				}
			}
		} catch (Exception e) {}
	}
	// Starts thread
	public void startThread() {
		moving = true;
		Thread t = new Thread(this);
		t.start();

	}

	public void stopThread() {
		moving = false;
	}
	@Override
	public void run() {

		while (moving && !dead) { // Thread goes while moving is true
			if (dead) interrupt();
			if (!Input.pause) { // Checks to see if the game is not paused
				animateOneStep();
				if (destroysProjectiles) {
					projectileHit(Player.player);
				}
				if (isColor && Player.player != null)
					if(circleHit(Player.player)) {
						if (Input.pause == false) // plays hit sound if the game is not paused
							Sound.playSound(hit);
						if (destroyOnHit) dead = true;
					}
				if (isImage && Player.player != null)
					if(squareHit(Player.player)) {
						if (Input.pause == false) // plays hit sound if the game is not paused
							Sound.playSound(hit);
						if (destroyOnHit) dead = true;
					}
			}
			try { Thread.sleep(pauseDuration); } catch (InterruptedException e) {}
		}

	}
	/**
	 * Draws image or colored ball
	 * @param gc
	 * 			The graphics context
	 */
	public void draw(GraphicsContext gc) {
		double drawX = x - radius;
		double drawY = y - radius;

		gc.setFill(color);
		if (isColor)
			gc.fillOval(drawX, drawY, radius * 2, radius * 2);

		if (isImage) {
			iv = new ImageView(image);
			params = new SnapshotParameters();
			rotatedImage = rotateImage(image);

			gc.drawImage(rotatedImage, hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());

		}
	}

	/**
	 * Sets the x speed.
	 * 
	 * @param xSpeed
	 *            New x speed.
	 */
	public void setXSpeed(double xSpeed) {
		this.xSpeed = xSpeed;
	}

	/**
	 * Sets the y speed.
	 * 
	 * @param ySpeed
	 *            New y speed.
	 */
	public void setYSpeed(double ySpeed) {
		this.ySpeed = ySpeed;
	}

	/**
	 * Sets the x location.
	 * 
	 * @param x
	 *            New x location.
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Sets the y location.
	 * 
	 * @param y
	 *            New y location.
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Sets color of object.
	 * 
	 * @param color
	 *            New color.
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	/**
	 * Gets y coordinate
	 * 
	 * @return y
	 * returns y coordinate
	 */
	public double getY() {
		return this.y;
	}
	/**
	 * Gets x coordinate
	 * 
	 * @return x
	 * returns x coordinate
	 */
	public double getX() {
		return this.x;
	}
	/**
	 * A boolean containing whether the projectile is dead
	 * @return dead
	 * To check if the projectile is dead 
	 */
	public boolean isDead() {
		return dead;
	}
	/**
	 * Sets true or false for whether the projectile is dead
	 * @param dead
	 * The boolean used to set if it's dead
	 */
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	/**
	 * A boolean that tells you whether the projectile hit it's target and destroys it
	 * @return destroyOnHit
	 * True or false of whether the projectile hit 
	 */
	public boolean isDestroyOnHit() {
		return destroyOnHit;
	}
	/**
	 * Sets true or false of whether the projecitle hit its target
	 * @param destroyOnHit
	 * The boolean used to set
	 */
	public void setDestroyOnHit(boolean destroyOnHit) {
		this.destroyOnHit = destroyOnHit;
	}

}