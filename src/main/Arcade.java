package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
/**
 * <p>This is the <b>Arcade class</b> used to create the <b>Arcade Mode</b> in the game.</p> 
 * This Arcade class will contain things such as:
 * <li><b> High score keeping</b>
 * <li><b> Waves/levels</b> and wave enemies, which will be periodically spawning enemies with higher health and damage.
 * <li><b> Game Over Screen </b> containing kills, wave reached, and exit.
 * <p><br>It will also be outputting to a file with a high score.</p>
 * <br> January 22, 2019
 * <br>Arcade.java
 * @author Richard Pena
 * @author Alex Co
 * @author Mbarak Al-Amry
 */
public class Arcade {

	/**
	 * This constructor sets the health and damage for the Arcade mode.
	 */
	public Arcade() {
		health = 50;
		damage = 10;
	}

	// Variables needed to create waves for Arcade Mode
	static int wave = -1;
	static int kills = -6;
	private static int waveEnemies = 6;
	private static double health = 0;
	private static double damage = 0;
	private static final double healthScaling = 20;
	private static final double damageScaling = 10;
	
	/**
	 * This method <i>updates enemies</i> used for each wave.
	 * <br>Slimes and elementals are spawned in random locations on the map.
	 * <br>and given higher health and damage through each wave.
	 * @param map
	 * Used to spawn enemies on the map.
	 */
	public static void update(Map map) {
		//System.out.println("wave: " + wave + " kills: " + kills);
		if (Enemy.enemyList.size() == 0) { // If no enemies are in the map, spawn more
			for (int i=0; i<waveEnemies; ++i) {
				int enemyType = (int) Math.round(Math.random());
				double x = Math.random()*map.getMapWidth()-map.getX();
				double y = Math.random()*map.getMapHeight()-map.getY();

				if (enemyType==0) { // Slime
					Enemy.enemyList.add(new Enemy ("slime", health, x, y, 0, damage));
				} else if (enemyType==1) {
					Enemy.enemyList.add(new Enemy ("elemental", health/2, x, y, 0, damage*2));
				}
			}
			//spawn a few enemies
			health+=healthScaling;
			damage+=damageScaling;
			wave++;
			// Clear some ArrayLists to prevent lag
			Sound.soundList.clear();
			Sound.soundsPlayed = 0;
			for (int i=0; i<Projectile.projectiles.size(); ++i) {
				Projectile.projectiles.get(i).setDead(true);
			}
			Projectile.projectiles.clear();
		}
	}

	/**
	 * This method is used to <i>reset</i> Arcade Mode.
	 * <br>It resets the waves, kills, health, damage and wave enemies.
	 * <br> and clears all enemies in the enemyList. 
	 */
	public static void reset() {
		Enemy.enemyList.clear();
		for (int i=0; i<Projectile.projectiles.size(); ++i) {
			Projectile.projectiles.get(i).setDead(true);
		}
		Projectile.projectiles.clear();
		wave = -1;
		kills = -6;
		health = 0;
		damage = 0;
		waveEnemies = 6;
	}

	// Variables used for Game Over screen
	static boolean gameOver = false;
	static Rectangle toMainMenu = new Rectangle(565, 680, 149, 39);
	static Image gameOverScreen = new Image("/files/game_over.png");
	static Image gameOverScreenH = new Image("/files/game_over_h.png");
	
	/**
	 * <p>This method draws the <b>Game Over</b> screen
	 * <p> It contains the text <i>"YOU DIED"</i>
	 * <br>the <i>kills</i> the user achieved
	 * <br> and the <i>wave reached</i> by the user.</p>
	 * @param gc
	 * 			The graphics context
	 */
	public static void drawGameOverScreen(GraphicsContext gc) {
		if (toMainMenu.contains(Input.mouseX, Input.mouseY)) gc.drawImage(gameOverScreenH, 0, 0);
		else gc.drawImage(gameOverScreen, 0, 0);
		gc.setFill(Color.WHITE);
		Font temp = gc.getFont();
		gc.setFont(new Font(50));
		gc.fillText(kills+"", 880, 340);
		gc.fillText(wave+"", 880, 490);
		gc.setFont(temp);
	}
}