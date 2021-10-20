package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;

/**
 * <p>This is the <b>Enemy</b> class, the class where <b>Enemies</b> are created.
 * <p>This class creates and controls the enemies movement, attacks, health, speed, hitboxes and drawing the enemies. 
 * <br>There are two types of Enemies which can be created:
 * <li><i>Slimes<i>
 * <br>They use melee attacks, so that they fight with players in close range.
 * <br>Their movement created in this class, allows them to always close to the player by moving closer if it is too far away from the player. 
 * <li><i>Elementals<i>
 * <br>They use projectile based attacks, so that they can fight with players from long range.
 * <br>Their movement created in this class, allows them to either move away from the player when they are too close, or move towards the player if they are too far away.</p>
 * <br>January 19, 2019
 * <br>Enemy.java
 * @author Richard Pena
 * @author Alex Co
 * @author Mbarak Al-Amry
 */
public class Enemy extends Entity {
	
	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}
	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	/**
	 * @return the damage
	 */
	public double getDamage() {
		return damage;
	}
	/**
	 * @param damage the damage to set
	 */
	public void setDamage(double damage) {
		this.damage = damage;
	}

	private String type; //slime, elemental
	private Image image; // Image of enemy
	static List<Enemy> enemyList = Collections.synchronizedList(new ArrayList<>()); // List of enemies
	private int frame = 0;
	private double speed;
	private double damage;

	/**
	 * <p>This constructor is used to get the conditions/parameters needed to make a specific type of enemy.
	 * <br>The values stored within the parameters are then set to the type of enemy.</p>
	 * @param type
	 * The type of enemy whether a slime or elemental. 
	 * @param health
	 * Health of enemy.
	 * @param x
	 * x coordinate of the enemy.
	 * @param y
	 * y coordinate of the enemy.
	 * @param speed
	 * speed of the enemy.
	 */
	public Enemy(String type, double health, double x, double y, double speed, double damage) {
		this.type = type; //test
		if (type.equals("slime")) {
			this.image = new Image("/files/slime.png");
		} else if (type.equals("elemental")) {
			this.image = new Image("/files/elemental.png");
		}
		this.x = x;
		this.y = y;
		this.xSpeed = speed;
		this.ySpeed = speed;
		this.maxHealth = health;
		this.health = health;
		// Creates hit box for enemy
		this.hitbox.setWidth(image.getWidth());
		this.hitbox.setHeight(image.getHeight());
		this.speed = speed;
		if (type.equals("elemental")) this.speed = 0.5;
		if (type.equals("slime")) this.speed = 2;
		this.damage = damage;
	}
	
	/**
	 * <p>This method is used to <i>update</i> the enemy every frame.</p>
	 * <p>This is done by re-setting the hitbox coordinates.
	 * <br>Calling the moveEnemy method to check if the enemy needs to move again.
	 * <br>Calling the attack method so the enemy constantly attacks the player.</p>  
	 * @param map
	 * The map that is displayed in game.
	 * @param gc
	 * The graphics context.
	 */
	public void update(Map map, GraphicsContext gc) {
		frame++;
		// Changes hitbox coordinates
		hitbox.setX(x);
		hitbox.setY(y);

		moveEnemy(Player.player, map);
		attack(Player.player);
	}
	
	/**
	 * <p>This method is used to allow enemies to <i>move</i>.</p>
	 * <p>The angle and distance between enemies and the player are calculated, so that when the enemies move different distances towards or away from the player, the x speed and y speed are not changed.
	 * <br>Depending on the enemy type, the enemy is then moved in different ways.</p>
	 * <li><i>Slimes</i>
	 * <br>Slimes are moved closer to the player when they are a certain distance away. 
	 * <p><br><li><i>Elementals</i>
	 * <br>Elementals are moved away from the player when they are at too close of a certain distance.
	 * <br>Elementals are also moved closer to the player when they are a certain distance away.</p>
	 * <br>
	 * @param player
	 * The player object used to determine the distance between the player and the enemies.
	 * @param map 
	 * The map in the game.
	 */
	private void moveEnemy(Player player, Map map) {
		double angle = Combat.calculateAngle(x, y, player.getX(), player.getY());
		double distance = Math.sqrt(Math.pow(player.x - x, 2) + Math.pow(player.y - y, 2));

		if (type.equals("slime") && distance >= 50) { // move close to player
			x-=-Math.cos(angle)*speed;
			y-=Math.sin(angle)*speed;
		} if (type.equals("elemental") && distance <= 200) { // move away when too close
			double xSpeed = -Math.cos(angle)*speed;
			double ySpeed = Math.sin(angle)*speed;
			if (x+xSpeed > 0-map.getX() && x+xSpeed < map.getMapWidth()-this.hitbox.getWidth()-map.getX()) { // checking location so they can stay in bounds of the map
				x+=xSpeed;
			}
			if (y+ySpeed > 0-map.getY() && y+ySpeed < map.getMapHeight()-this.hitbox.getHeight()-map.getY()) { // checking location so they can stay in bounds of the map
				y+=ySpeed;
			}
		} else if (type.equals("elemental") && distance >= 400) {
			x-=-Math.cos(angle)*speed*4;
			y-=Math.sin(angle)*speed*4;
		} 
		this.hitbox.setX(x);
		this.hitbox.setY(y);

	}
	
	/**
	 * <p>This method is used to create the <i>attacks</i> for enemies.</p>
	 * Each type of enemy uses their own different type of attacks, so different calculations are used for each enemy type.
	 * <li><i>Elementals:</i>
	 * They use projectile based attacks, so that they fight from long range.
	 * <li><i>Slime:</i>
	 * They use melee based attacks so that they fight in close range.</p>
	 * <br>
	 * @param player
	 * Player object used to create attack.
	 */
	private void attack(Player player) {
		int fireSpeed = 5;
		if (frame%90 == 0 && type.equals("elemental")) {
			double radius = 15;
			double angle = Combat.calculateAngle(x, y, player.getX(), player.getY());
			double xSpeed = -(Math.cos(angle-Math.PI)*fireSpeed);
			double ySpeed = Math.sin(angle-Math.PI)*fireSpeed;
			Projectile fireball = new Projectile(getCenterX(), getCenterY(), radius, xSpeed, ySpeed, "ENEMY", damage);
			Projectile.projectiles.add(fireball);
		}
		if (frame%90 == 0 && type.equals("slime")) {
			double radius = 60;
			double angle = Combat.calculateAngle(x, y, player.getX(), player.getY());
			Arc slash = new Arc(getCenterX(), getCenterY(), radius, radius, Math.toDegrees(angle)-45, 90);
			slash.setFill(Color.GRAY);
			Combat.melee(player, damage, "ENEMY", slash, null, false);	
		}
	}
	
	/**
	 * Draws the enemy.
	 * @param gc
	 * The graphics context.
	 */
	public void draw(GraphicsContext gc) {
		gc.drawImage(image, hitbox.getX(), hitbox.getY());
		gc.setFill(Color.LIGHTGREEN);
		gc.fillRect(x, y-15, this.hitbox.getWidth()*(health/maxHealth), 10); // Draw health bar
	}
	
	/**
	 * Gets the type of enemy.
	 * @return type 
	 * Enemy type (slime/elemental). 
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Sets the type of enemy.
	 * @param type
	 * A string containing which type of enemy it is.
	 */
	public void setType(String type) {
		this.type = type;
	}
}