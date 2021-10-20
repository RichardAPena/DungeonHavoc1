package main;

import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
/**
 * <p>This is the <b>Combat</b> class.</p>
 * <p>This class contains attacks created for <i>Players</i> and <i>Enemies</i>.
 * <br>There are two general attacks created for them:
 * <p><li><i>Area of effect</i>, which is used for area based attacks in the game.
 * <br>This attack is broken down to Delayed, and Periodic.</p>
 * <p><br><li><i>Melee</i>, which is used for close range attacks.
 * <br>It uses the arc class to calculate the range of the attack.
 * <br>It can also destroy projectile based attacks.</p>
 * <br>These attacks are drawn/created using the drawShapes method, which draws the shapes necessary for the attack.</p>
 * <br>January 21, 2019
 * <br>Combat.java
 * @author Richard Pena
 * @author Alex Co
 * @author Mbarak Al-Amry
 */
public class Combat extends Thread {

	public static void main(String[] args) {
		Circle attack = new Circle(50, 50, 50);
		System.out.println("circle x: " + attack.getCenterX() + " y: " + attack.getCenterY() + " r: " + attack.getRadius());
		Player player = new Player();
		player.setX(500);
		player.setY(500);
		player.hitbox.setWidth(10);
		player.hitbox.setHeight(10);
		System.out.println("player x: " +player.getX() + " y: " + player.getY());
		player.setMaxHealth(100);
		player.setHealth(100);
		System.out.println(player.getHealth() + " / " + player.getMaxHealth());
		System.out.println("executing area attack...");
		areaDamage(attack, 20, player, "ENEMY", null);
		System.out.println(player.getHealth() + " / " + player.getMaxHealth());
		player.setX(50);
		player.setY(50);
		System.out.println("moving player...");
		System.out.println("executing area attack...");
		areaDamage(attack, 20, player, "ENEMY", null);
		System.out.println(player.getHealth() + " / " + player.getMaxHealth());

	}
	protected double x; // The x coordinate of the attack
	protected double y; // The y coordinate of the attack
	protected Circle c; // A circle variable used as the bounds for an attack
	protected double radius; // The radius of the attack
	protected String source; // The user of the attack, player or enemy
	protected double damage; // The variable used for the amount of damage
	protected Paint color; // Color variable
	protected boolean moving; // The variable used to know if the attack is being used
	protected boolean isImage = false; // A boolean used to see if an image is used for the attack
	protected boolean isColor = false; // A boolean used to see if a non image is used for the attack
	protected static int pauseDuration = 1000/60; // pause duration after each attack
	protected boolean dead = false; // A boolean to check if the attack is dead
	protected Rectangle hitbox; // A rectangle hitBox used to determine if an attack hits

	protected Image image; // The attack image used 
	protected double deadTimer; // The timer in the attack
	protected boolean destroysProjectiles; // A boolean to determine whether to destroy a projectile
	static ArrayList<Circle> circles = new ArrayList<>(); // An arraylist of circles
	static ArrayList<Arc> arcs = new ArrayList<>(); // An arraylist of arcs
	static ArrayList<Circle> delayedList = new ArrayList<>(); // An arraylist of circles for delayed attacks
	static ArrayList<Circle> periodicList = new ArrayList<>(); // An arraylist of circles for periodic attacks

	/**
	 * A default constructor
	 */
	public Combat() {
		super();
	}

	/**
	 * <p>This method is called so that a cast time is initialized before allowing the use of the ability.
	 * <br>This is done by creating a <i>Current cast time</i> that goes through a loop and
	 * <br>and waits a certain amount of frames before, executing the abilities set up.</br></p>
	 * @param player Player object.
	 * @param map the current map, used for mobility abilities, such as <i>Blink</i>.
	 * @param ability the ability that will be cast after the cast time is finished.
	 * @param castTime amount of frames before the ability is executed.
	 */
	public static void cast(Player player, Map map, int ability, double castTime) {

		Thread cast = new Thread(new Runnable() {
			/**
			 * Waits a certain amount of frames (castTime) before executing the ability that was queued up.
			 */
			@Override
			public void run() {
				player.setCurrentCast(castTime);
				player.setMaxCast(castTime);
				player.setCasting(true);
				while (player.getCurrentCast() > 0) { // waits x frames before doing something
					if(!Input.pause) player.setCurrentCast(player.getCurrentCast()-1);			
					try { Thread.sleep(pauseDuration); } catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (ability == 1) player.castAbility1();
				if (ability == 2) player.castAbility2();
				if (ability == 3) player.castAbility3(map);
				if (ability == 4) player.castAbility4();
				if (ability == 5) player.castAbility5();
				player.setCasting(false);
				player.setCurrentCast(0);
				player.setMaxCast(1); // can't be 0, otherwise it will explode because it would be dividing by 0
			}
		});
		cast.start();
	}

	/**
	 * <p>This method is used for <b>Delayed Area Of Effect</b> attacks.</p> 
	 * <p>This means it is a type of attack that only after a certain delay/duration time has passed.
	 * <br>After this delay, the areaDamage method will be called to perform an area of effect attack using the parameters stored.
	 * <br>This method creates this delay then sends it to the areaDamage method to create the attack.</p>
	 * @param damage
	 * A variable holding how much damage the attack does.
	 * @param circle
	 * A variable with the radius of the attack.
	 * @param player
	 * The player used in the game used to send to area of effect.
	 * @param source
	 * A String showing whether it's an enemy or player using the attack.
	 * @param delay
	 * A variable used as the delayed time.
	 */
	public static void delayedAOE(Circle circle, double damage, Player player, String source, double delay, Media sound) { // for arcane barrage
		Thread delayed = new Thread(new Runnable() {
			/**
			 * Counts down frames (delay) before calling the areaDamage method.
			 */
			@Override
			public void run() { 
				delayedList.add(circle); // Need to do this so that it can be moved around when scrolling
				int index = delayedList.size()-1;

				for (int i=0; i<delay;) {
					if (!Input.pause) ++i;
					try {  Thread.sleep(pauseDuration); } catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// Calls area damage to finish creating the attack  
				areaDamage(delayedList.get(index), damage, player, source, sound);
			}
		});
		delayed.start(); // Starts thread
	}

	/**
	 * <p>This method is used for <b>Periodic Area Of Effect</b> attacks.</p>
	 * <p>This means it is a type of attack that deals damage in a certain area.
	 * <br>But does the attack in periods so it counts every frame and calls the areaDamage method every tick.
	 * <br>A thread is used to perform this attack.</p>
	 * @param damage
	 * A variable holding how much damage the attack does.
	 * @param circle
	 * A variable with the radius of the attack.
	 * @param player
	 * The player used in the game used to send to area of effect.
	 * @param source
	 * A String showing whether it's an enemy or player using the attack.
	 * @param ticks
	 * The amount of ticks used to compare.
	 * @param duration
	 * The duration variable used to go through the for loop.
	 * @param sound
	 * A sound sent to areaDamage.
	 */
	public static void periodicAOE(Circle circle, double damage, Player player, String source, double ticks, double duration, Media sound) {

		Color stroke = (Color) circle.getFill();
		circle.setStroke(stroke);
		Thread periodic = new Thread(new Runnable() {
			/**
			 * Counts every frame and calls the areaDamage method every tick.
			 * For example, an area of effect attack that ticks every frame and lasts 30 frames will call the areaDamage method 6 times.
			 */
			@Override
			public void run() {
				periodicList.add(circle); // Need to do this so that it can be moved around when scrolling
				int index = periodicList.size()-1;
				for (int i=0; i<=duration;) {
					if (i%ticks==0) {
						areaDamage(periodicList.get(index), damage, player, source, sound);
					}
					try { Thread.sleep(pauseDuration); } catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (!Input.pause) ++i;
				}
				periodicList.get(index).setStroke(Color.TRANSPARENT);
			}
		});
		periodic.start(); // Starts thread
	}

	/**
	 * <p>This is the <b>areaDamage</b> method which is used to create the <i>area of effect</i> attacks.</p>
	 * <p>Depending on the source (PLAYER/ENEMY), a circle parameter is used to check if the area based attack casted intersects with either the player or enemies hitbox.
	 * <br>If it intersects with the hitbox, damage is dealt to either the enemy or player that was hit.
	 * <br>This method also gets the attack to disappear after a certain period of time.</p> 
	 * @param circle
	 * A circle used to check if an enemy or player are within the attacks bounds/range.
	 * @param damage
	 * A variable holding how much damage the attack does.
	 * @param player
	 * The player object used to determine whether the player has been hit by an enemy attack or not.
	 * @param source
	 * A String showing whether it's an enemy or player using the attack.
	 * @param sound
	 * Used to store the sound of the attack. 
	 */
	public static void areaDamage(Circle circle, double damage, Player player, String source, Media sound) {
		Color shade = (Color) circle.getFill();
		Thread drawCircle = new Thread(new Runnable() {
			/**
			 * Causes circles to fade out after 10 frames.
			 */
			@Override
			public void run() {
				circles.add(circle);
				int index = circles.size()-1;
				while (circles.get(index).getOpacity() >= 0.0) {
					//if (!Input.pause) circles.get(index).setOpacity(circles.get(index).getOpacity()-0.1); // fades out circle by reducing its opacity

					try { if (circles.get(index).getOpacity() > 0) circles.get(index).setFill(new Color(shade.getRed(), shade.getGreen(), shade.getBlue(), circles.get(index).getOpacity())); 
					} catch (IllegalArgumentException e) { System.err.println(e.getMessage()); }
					try { Thread.sleep(pauseDuration); } catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		drawCircle.start(); 
		if (source.equals("PLAYER")) { // Checks if it is a player shooting the attack
			for (int i=0; i<Enemy.enemyList.size(); ++i) {
				if (Enemy.enemyList.get(i).getHitbox().intersects(circle.getLayoutBounds())) { // Checks to see if the enemy intersects/gets hit with the attack
					Enemy.enemyList.get(i).setHealth(Enemy.enemyList.get(i).getHealth()-damage); // Reduces enemy health
					if (sound != null) 
						Sound.playSound(sound); // Plays the attacks hit sound since it has hit its target
				}
			}
		} else if (source.equals("ENEMY")) { // Checks if it is an enemy shooting the attack
			if (player.getHitbox().intersects(circle.getLayoutBounds()) && !player.isParrying()) { // Checks to see if the player intersects attack bounds
				player.setHealth(player.getHealth()-damage); // Reduces player health
			}
		}
	}

	/**
	 * <p>This method is used to <i>calculate the angle</i> of two points.</p>
	 * <p>This is done by comparing the first two x and y coordinates (beginning coordinates).
	 * <br>with the second x and y coordinates (destination) to get the angle needed, such as when shooting a projectile and performing other attacks.
	 * <br>This is so it goes to the intended destination with no change in original speed.</p>
	 * @param x1
	 * The beginning x coordinate.
	 * @param y1
	 * The beginning y coordinate.
	 * @param x2
	 * The second x coordinate(where it's aimed).
	 * @param y2
	 * The second y coordinate(where it's aimed).
	 * @return 
	 * The angle needed when shooting an attack, angle theta.
	 */
	public static double calculateAngle(double x1, double y1, double x2, double y2) {
		double opp; // A variable that will hold the difference in coordinates
		double adj; // A variable that will hold the difference in coordinates

		double theta = 0; // The angle needed to calculate
		// Calculates the angle theta in every possible way
		if(y2 <= y1 && x2 >= x1) {
			opp = y1 - y2;
			adj = x2 - x1;
			theta = Math.atan2(opp, adj);
		}
		else if(y2 <= y1 && x2 <= x1) {
			opp = x1 - x2;
			adj = y1 - y2;
			theta = Math.atan2(opp, adj) + Math.PI/2; // 90
		}
		else if(y2 >= y1 && x2 <= x1) {
			opp = y2 - y1;
			adj = x1 - x2;
			theta = Math.atan2(opp, adj) + Math.PI; // 180
		}
		else if(y2 >= y1 && x2 >= x1) {
			opp = x2 - x1;
			adj = y2 - y1;
			theta = Math.atan2(opp, adj) + 3*Math.PI/2; // 270
		}
		return theta; // Returns the angle calculated
	}

	/**
	 * <p>This method draws the <i>shapes</i> needed for each attack.</p>
	 * <p>Every attack generally has its own shape made:
	 * <br><li>Melee attacks <br>Draws an arc representing the range of the attack</p>
	 * <p><br><li>Area Of Effect attacks <br>Draws an oval representing how far/radius of area damage</p>
	 * <br>
	 * @param gc
	 *  The graphics context.
	 */
	public static void drawShapes(GraphicsContext gc) {
		// Draws shapes/attacks
		try {
			double temp = gc.getLineWidth();
			gc.setLineWidth(10);
			for (Circle circle : circles) {
				if (circle.getOpacity() > 0) {
					double drawX = circle.getCenterX() - circle.getRadius();
					double drawY = circle.getCenterY() - circle.getRadius();
					if (!Input.pause) circle.setOpacity(circle.getOpacity() - 0.1);
					gc.setFill(circle.getFill());
					gc.fillOval(drawX, drawY, circle.getRadius() * 2, circle.getRadius() * 2);
				}
			}
			for (Arc arc : arcs) {
				if (arc.getOpacity() > 0) {
					double drawX = arc.getCenterX() - arc.getRadiusX();
					double drawY = arc.getCenterY() - arc.getRadiusY();
					if (!Input.pause) arc.setOpacity(arc.getOpacity() - 0.1);
					gc.setFill(arc.getFill());
					gc.fillArc(drawX, drawY, arc.getRadiusX() * 2, arc.getRadiusY() * 2, arc.getStartAngle(), arc.getLength(), ArcType.ROUND);
				}
			}
			for (Circle circle : periodicList) {
				double drawX = circle.getCenterX() - circle.getRadius();
				double drawY = circle.getCenterY() - circle.getRadius();
				gc.setStroke(circle.getStroke());
				gc.strokeOval(drawX, drawY, circle.getRadius() * 2, circle.getRadius() * 2);
			}
			for (Circle circle : delayedList) {
				double drawX = circle.getCenterX() - circle.getRadius();
				double drawY = circle.getCenterY() - circle.getRadius();
				gc.setStroke(circle.getFill());
				gc.strokeOval(drawX, drawY, circle.getRadius() * 2, circle.getRadius() * 2);
			}
			gc.setLineWidth(temp);
		} catch (Exception e) { 
			//System.err.println(e.getMessage() + " on drawShapes method"); 
		}
	}

	/**
	 * This method allows entities to do <b>melee attacks</b> or <b>cone-shaped attacks</b> using the <i>Arc</i> class. 
	 * <p>This method will check if an entity, depending on the source, intersects with an arc-shaped attack, and deals that much damage if it does.
	 * @param player Player object, used to determine whether the player has been hit by an enemy attack or not.
	 * @param damage How much damage the attack will deal if it connects.
	 * @param source Source of the attack. Can be either a player or an enemy.
	 * @param arc Arc object used to calculate whether an attack has hit an entity or not.
	 * @param sound Sound that is played if the attack hits an enemy.
	 * @param destroysProjectiles whether the attack destroys projectiles or not.
	 */
	public static void melee(Player player, double damage, String source, Arc arc, Media sound, boolean destroysProjectiles) {
		Color shade = (Color) arc.getFill();
		Thread drawArc = new Thread(new Runnable() {
			/**
			 * Causes arcs to fade out after 10 frames.
			 */
			@Override
			public void run() {
				arcs.add(arc);
				int index = arcs.size()-1;
				while (arcs.get(index).getOpacity() >= 0.0) {
					//if (!Input.pause) {
					//	arcs.get(index).setOpacity(arcs.get(index).getOpacity()-0.1); 
					try { if (arcs.get(index).getOpacity() >= 0) arcs.get(index).setFill(new Color(shade.getRed(), shade.getGreen(), shade.getBlue(), arcs.get(index).getOpacity())); }
					catch (Exception e) {
						e.printStackTrace();
					}
					//}
					try { Thread.sleep(pauseDuration); } catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		});
		drawArc.start(); // Starts thread

		if (destroysProjectiles) // Checks for projectile hit
			meleeProjectileHit(source, arc);

		if (source.equals("PLAYER")) { // Since the source is a player checks if the enemy was hit

			for (int i=0; i<Enemy.enemyList.size(); ++i)
				if (Enemy.enemyList.get(i).getHitbox().intersects(arc.getLayoutBounds())) {
					Enemy.enemyList.get(i).setHealth(Enemy.enemyList.get(i).getHealth()-damage);
					if (sound != null) Sound.playSound(sound);
				}
		}
		if (source.equals("ENEMY") && !player.isInvincible() && !player.isParrying()) // Since the source is an enemy checks if the player was hit
			if (arc.intersects(player.getHitbox().getLayoutBounds())) {
				player.setHealth(player.getHealth()-damage);
				if (sound != null) Sound.playSound(sound);	
			}
	}
	/**
	 * <p>This method is used to check for <b>melee hits on projectiles.</b>
	 * <p>If the melee hit (Arc) intersects the projectile, it will destroy it.
	 * <br>This is done by checking for a melee hit from a player or enemy.
	 * <br>Then by going through a loop of projectiles to check if the enemy or player projectile is within bounds of the
	 * <br>arc created for the melee attack, and if it is removes the projectile from an ArrayList which holds the projectiles.</p> 
	 * @param source
	 * Source of the attack. Can be either a player or an enemy.
	 * @param arc
	 * Arc object.
	 */
	public static void meleeProjectileHit(String source, Arc arc) {
		// Destroys enemy projectile if the player stops it with a melee hit
		if (source.equals("PLAYER")) {
			for (int i=0; i<Projectile.projectiles.size(); ++i) {
				if (Projectile.projectiles.get(i).isColor && Projectile.projectiles.get(i).source.equals("ENEMY")) {
					if (Projectile.projectiles.get(i).c.intersects(arc.getLayoutBounds())) {
						Projectile.projectiles.get(i).setDead(true);
						Projectile.projectiles.remove(i);
						--i;
					}
				} else if (Projectile.projectiles.get(i).isImage && Projectile.projectiles.get(i).source.equals("ENEMY")) {
					if (Projectile.projectiles.get(i).hitbox.intersects(arc.getLayoutBounds())) {
						Projectile.projectiles.get(i).setDead(true);
						Projectile.projectiles.remove(i);
						--i;
					}
				}
			}
		// Destroys player projectile if the enemy stops it with a melee hit
		} else if (source.equals("ENEMY")) {
			for (int i=0; i<Projectile.projectiles.size(); ++i) {
				if (Projectile.projectiles.get(i).isColor && Projectile.projectiles.get(i).source.equals("PLAYER")) {
					if (Projectile.projectiles.get(i).c.intersects(arc.getLayoutBounds())) {
						Projectile.projectiles.get(i).setDead(true);
						Projectile.projectiles.remove(i);
						--i;
					}
				} else if (Projectile.projectiles.get(i).isImage && Projectile.projectiles.get(i).source.equals("PLAYER")) {
					if (Projectile.projectiles.get(i).hitbox.intersects(arc.getLayoutBounds())) {
						Projectile.projectiles.get(i).setDead(true);
						Projectile.projectiles.remove(i);
						--i;
					}
				}
			}
		}
	}
	/**
	 * Gets the boolean destroysProjectiles that determines whether to destroy a projectile or not.
	 * @return destroyProjectiles
	 * A boolean containing true or false to destroy a projectile.
	 */
	public boolean isDestroysProjectiles() {
		return destroysProjectiles;
	}
	/**
	 * Sets true or false of whether a projectile is to be destroyed.
	 * @param destroysProjectiles
	 * The boolean used to know if the projectile is to be destroyed. 
	 */
	public void setDestroysProjectiles(boolean destroysProjectiles) {
		this.destroysProjectiles = destroysProjectiles;
	}
}
