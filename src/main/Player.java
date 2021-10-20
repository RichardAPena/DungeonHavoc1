package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
//import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
/**
 * This is the <b>Player</p> class, which extends the entity class.
 * <br>This class creates the <b>player</b> in the game.
 * <p> This is done by creating:
 * <li><i> Moving
 * <br> Moving is made through the moveCharacter method.
 * <br> By setting speed and setting the w,a,s,d to moving from the Input class it creates moving for the player
 * <li><i>Cast Abilities</i>
 * <br> The Player class creates five cast abilities for each player class/type to be used in the game
 * <li><i>Cool Downs<i> 
 * <br>Creates cool downs needed after every attack  
 * <li><i>Draws Player</i>
 * <br> Drawing the character is done by creating frames for every direction 
 * <br> and every step in that direction for that specific player
 * <br>January 22, 2019
 * <br>Player.java
 * @author Richard Pena
 * @author Alex Co
 * @author Mbarak Al-Amry
 */
public class Player extends Entity {

	static Player player; // The player variable used to create the player

	private String playerClass; // The type of player, Archer, Mage, Warrior 
	private String direction = "DOWN"; // String direction variable
	private double maxMana; // Max mana 
	private double mana; // Mana(at any time in the game)

	private double maxAmmo; // Max ammo
	private double ammo; // ammo(at any time in the game)
	private double manaRegen; // mana regen variable
	private double subImageWidth; // sub image width
	private double subImageHeight; // sub image height 

	private int frameWalk = 0; //for animating steps
	private int step = 2; //for animating steps, 1 is left, 2 is middle, 3 is right

	double[] maxCooldowns = new double[5]; // abilities will be set to this cooldown when used
	double[] cooldowns = new double[5]; // time needed to wait to use an ability again

	double[] castTime = new double[5]; // time before the selected ability is executed
	private boolean casting = false;
	// these two variables are for drawing a cast bar, so that the player may know for how long they are casting an ability for
	private double currentCast; // this is to help determine for how long the player is casting an ability
	private double maxCast = 1; // when a new ability is cast, this will be set to its cast time 

	private boolean parrying = false; // variable to parry attacks
	private double parryFrames = 0; // frames for parrying
	private double invincibleFrames = 0; // invisible frames
	private double ammoRechargeFrames = 0; //frames for ammo recharges

	private final Image arrowImage = new Image(getClass().getResourceAsStream("/files/arrow2_8bit.png")); // arrow image
	private final Image biggerArrow = new Image(getClass().getResourceAsStream("/files/bigger_arrow.png"));
	private final Image missileImage = new Image(getClass().getResourceAsStream("/files/magic_missile.png")); // orb will be a circle, arcane barrage will also be a circle
	@SuppressWarnings("unused")
	private final Image swordImage = new Image(getClass().getResourceAsStream("/files/8_bit_sword.png")); // thunderclap can be a circle, whirlwind can swing the sword around and parry doesnt require an image

	private final int pauseDuration = 1000/60; // pause time after attacks
	private boolean rolling = true; // used to indicate rolling attack
	private boolean charging = true; // used to indicate charging attack
	/**
	 * Default constructor
	 */
	public Player() {
		super();
	}

	public static void main(String[] args) {
		Player entity = new Player();
		double randomNumber = Math.random()*20;
		System.out.println("setting the xSpeed to " + randomNumber);
		entity.setXSpeed(randomNumber);
		System.out.println("entity xSpeed: " + entity.getXSpeed());
	}
	/**
	 * This constructor is used to gets the things needed to make a specific class for player
	 * <br>Then sets them to the player 
	 * @param playerClass
	 * The class of the player such as archer, mage and warrior
	 * @param image
	 * Image of the player
	 * @param x
	 * x coordinate of the player
	 * @param y
	 * y coordinate of the player
	 * @param xSpeed
	 * x speed of the player
	 * @param ySpeed
	 * y speed of the player
	 */
	public Player(String playerClass, Image image, double x, double y, double xSpeed, double ySpeed) {
		super();
		this.playerClass = playerClass;
		switch (playerClass) {
			case "ARCHER" -> {
				maxHealth = 150;
				health = maxHealth;
				healthRegen = 0.1;
				maxAmmo = 6;
				ammo = maxAmmo;
				maxCooldowns[0] = 0; //M1 (Steady Shot)
				castTime[0] = 10;
				maxCooldowns[1] = 90; //M2 (Volley)
				castTime[1] = 30;
				maxCooldowns[2] = 120; //Space (Roll)
				castTime[2] = 2;
				maxCooldowns[3] = 180; //Q (Rain of Arrows)
				castTime[3] = 30;
				maxCooldowns[4] = 180; //E (Piercing Shot)
				castTime[4] = 30;
				rolling = false;
				charging = false;
			}
			case "MAGE" -> {
				maxHealth = 100;
				health = maxHealth;
				healthRegen = 0.1;
				maxMana = 100;
				mana = maxMana;
				manaRegen = 0.5;
				maxCooldowns[0] = 10; //M1 (Magic Missile)
				castTime[0] = 20;
				maxCooldowns[1] = 120; //M2 (Arcane Orb)
				castTime[1] = 30;
				maxCooldowns[2] = 120; //Space (Blink)
				castTime[2] = 2;
				maxCooldowns[3] = 180; //Q (Arcane Barrage)
				castTime[3] = 2;
				maxCooldowns[4] = 120; //E (Wave of Force)
				castTime[4] = 2;
				rolling = false;
				charging = false;
			}
			case "WARRIOR" -> {
				maxHealth = 200;
				health = maxHealth;
				healthRegen = 0.2;
				maxCooldowns[0] = 15; //M1 (Sword Slash)
				castTime[0] = 6;
				maxCooldowns[1] = 30; //M2 (Thunderclap)
				castTime[1] = 10;
				maxCooldowns[2] = 120; //Space (Charge)
				castTime[2] = 2;
				maxCooldowns[3] = 120; //Q (Whirlwind)
				castTime[3] = 6;
				maxCooldowns[4] = 150; //E (Parry)
				castTime[4] = 2;
				rolling = false;
				charging = false;
			}
		}

		this.x = x;
		this.y = y;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;

		this.image = image;
		// Creates hitbox for the player
		this.hitbox.setWidth(48);
		this.hitbox.setHeight(48);
		subImageWidth = this.hitbox.getWidth();
		subImageHeight = this.hitbox.getHeight();
	}

	private int maxRechargeFrames = 60; // Creates an int for max recharge frames
	private boolean reloading = false; // creates the reloading boolean 
	/**
	 * Gets reloading boolean
	 * @return reloading
	 * Returns true or false whether an attack is reloading
	 */
	public boolean isReloading() {
		return reloading;
	}
	/**
	 * Sets reloading true or false.
	 * 
	 * @param reloading
	 *            reloading boolean.
	 */
	public void setReloading(boolean reloading) {
		this.reloading = reloading;
	}

	/**
	 * This method is called every frame by the updatePlayer thread in Main(). It updates the player's following things:
	 * <li>Health
	 * <li>Mana
	 * <li>Ammo
	 * <li>Coordinates
	 * <li>Checking for input and movement
	 * <li>Updating frames (invincibility, parrying, reloading)
	 */
	@Override
	public void update(Map map, GraphicsContext gc) {
		if (health <= 0) {
			Arcade.gameOver = true;
			Input.arcadeMode = false;
		}

		// Checks each type of frames
		
		// Checks each type of frames
		hitbox.setX(x);
		hitbox.setY(y);
		if (parryFrames > 0) parryFrames--;
		if (parryFrames <= 0) parrying = false;
		if (invincibleFrames > 0) invincibleFrames--;
		ammoRechargeFrames--;
		if (ammoRechargeFrames < 0) ammoRechargeFrames = 0;

		if (playerClass.equals("ARCHER")) {
			if (ammo == 0 && !reloading) {
				ammoRechargeFrames = maxRechargeFrames;
				reloading = true;
			}
			if (ammoRechargeFrames <= 0 && reloading) {
				reloading = false;
				ammo = maxAmmo;
			}
		}

		// Updates health and mana
		health+=healthRegen;
		if (health > maxHealth) health = maxHealth;
		if (playerClass.equals("MAGE")) {
			mana+=manaRegen;
			if (mana > maxMana) mana = maxMana;
		}

		// sets cast abilities
		moveCharacter(map, gc);
		for (int i=0; i<cooldowns.length; ++i) { //reduce cooldowns
			if (cooldowns[i] > 0) cooldowns[i]--;
		}
		if (Input.mouse1Pressed && cooldowns[0] <= 0 && !reloading && !isCasting() && !rolling && !charging) {
			Combat.cast(this, map, 1, castTime[0]);
		}
		if (Input.mouse2Pressed && cooldowns[1] <= 0 && !reloading && !isCasting() && !rolling && !charging) {
			Combat.cast(this, map, 2, castTime[1]);			
		}
		if (Input.buttonPressed[Input.keyMap.get("SPACE").ordinal()] && cooldowns[2] <= 0) {
			Combat.cast(this, map, 3, castTime[2]);
		}
		if (Input.buttonPressed[Input.keyMap.get("Q").ordinal()] && cooldowns[3] <= 0 && !reloading && !isCasting() && !rolling && !charging) {
			Combat.cast(this, map, 4, castTime[3]);
		}
		if (Input.buttonPressed[Input.keyMap.get("E").ordinal()] && cooldowns[4] <= 0 && !reloading && !isCasting() && !rolling && !charging) {
			Combat.cast(this, map, 5, castTime[4]);
		}
		if (Input.buttonPressed[Input.keyMap.get("RELOAD").ordinal()] && playerClass.equals("ARCHER") && !isCasting() && !rolling) {
			ammoRechargeFrames = maxRechargeFrames;
			reloading = true;
		}
	}

	/**
	 * This method allows the player to move across the map
	 * <br>In any direction the user chooses
	 * <br>It also allows the character to turn
	 * @param map
	 * The map used in the game, used to determine where the character moves 
	 */
	private void moveCharacter(Map map, GraphicsContext gc) {
		//to be deleted/changed //FIXME this is bad
		//-------------------------------------
		// Sets direction and speed to the player 
		if (Input.buttonPressed[Input.keyMap.get("RIGHT").ordinal()]
				|| Input.buttonPressed[Input.keyMap.get("LEFT").ordinal()]
						|| Input.buttonPressed[Input.keyMap.get("UP").ordinal()]
								|| Input.buttonPressed[Input.keyMap.get("DOWN").ordinal()]) frameWalk++;

		int frame = 4;
		if (frameWalk%(frame *2)==0) {
			step = 2;
		} else if (frameWalk%(frame *3)==0) {
			step = 1;
		} else if (frameWalk% frame ==0) {
			step = 3;
		}
		//-------------------------------------
		// TODO add collision stuff 

		boolean collidedUp = false;
		boolean collidedDown = false;
		boolean collidedLeft = false;
		boolean collidedRight = false;

		if (Input.buttonPressed[Input.keyMap.get("RIGHT").ordinal()] && !rolling && !charging) {
			if (x+xSpeed <= Input.resWidth-hitbox.getWidth()) { //  && !map.isEntityCollidingWithMap(this, gc)
				//if (!map.isEntityCollidingWithMap(this, gc, "RIGHT", xSpeed)) {
				x+=xSpeed;	
				//} else collidedUp = true;
				//	map.mapScroll(xSpeed, this);
			}
			direction = "RIGHT";
		}
		if (Input.buttonPressed[Input.keyMap.get("LEFT").ordinal()] && !rolling && !charging) {
			if (x-xSpeed >= 0) { //  && !map.isEntityCollidingWithMap(this, gc) 
				//if (!map.isEntityCollidingWithMap(this, gc, "LEFT", xSpeed)) {
				x-=xSpeed;
				//} else collidedDown = true;
				//	map.mapScroll(xSpeed, this);
			}
			direction = "LEFT";
		}
		if (Input.buttonPressed[Input.keyMap.get("UP").ordinal()] && !rolling && !charging) {
			if (y-ySpeed >= 0) { //  && !map.isEntityCollidingWithMap(this, gc)
				//if (!map.isEntityCollidingWithMap(this, gc, "UP", ySpeed)) {
				y-=ySpeed;
				//} else collidedLeft = true;
				//	map.mapScroll(ySpeed, this);
			}
			direction = "UP";
		}
		if (Input.buttonPressed[Input.keyMap.get("DOWN").ordinal()] && !rolling && !charging) {
			if (y+ySpeed <= Input.resHeight-hitbox.getHeight()) { //  && !map.isEntityCollidingWithMap(this, gc)
				//if (!map.isEntityCollidingWithMap(this, gc, "LEFT", xSpeed)) {
				y+=ySpeed;					
				//} else collidedRight = true;
				//	map.mapScroll(ySpeed, this);
			}
			direction = "DOWN";
		}	
		// updates hitbox coordinates  
		hitbox.setX(x);
		hitbox.setY(y);

		// Scrolls the map
		if (Input.buttonPressed[Input.keyMap.get("UP").ordinal()] && !collidedUp) map.mapScroll(0, -ySpeed, this);
		if (Input.buttonPressed[Input.keyMap.get("DOWN").ordinal()] && !collidedDown) map.mapScroll(0, ySpeed, this);
		if (Input.buttonPressed[Input.keyMap.get("LEFT").ordinal()] && !collidedLeft) map.mapScroll(-xSpeed, 0, this);
		if (Input.buttonPressed[Input.keyMap.get("RIGHT").ordinal()] && !collidedRight) map.mapScroll(xSpeed, 0, this);

	}

	/**
	 * Draws the player/sprite. 
	 * <br> It draws the player using different sub images comprising of 3 steps the player takes, in each direction.
	 * <br> This makes the player look like they're actually walking/running and makes them look 3D
	 * <br>This is drawn for every player class in the game, Archer, Mage, and Warrior.
	 * @param gc
	 *            The graphics context.
	 */
	public void draw(GraphicsContext gc) {
		// Creates shadow for character
		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(5.0);
		dropShadow.setOffsetX(3.0);
		dropShadow.setOffsetY(3.0);
		dropShadow.setColor(Color.color(0.4, 0.5, 0.5));

		ColorAdjust parryHue = new ColorAdjust(0, 0, -0.5, 0.2);
		gc.setEffect(dropShadow);
		if (isInvincible()) { 
			gc.setGlobalAlpha(0.5);
		}
		if (parrying) {
			gc.setEffect(parryHue);
		}
		// Creates player images in each direction the player is facing and image steps when the player moves
		double spriteWidth = image.getWidth();
		double spriteHeight = image.getHeight();
		int spritesPerRow = 4;
		int spritesPerColumn = 3;
		setSubImageWidth(Math.round(spriteWidth / spritesPerColumn));
		setSubImageHeight(Math.round(spriteHeight / spritesPerRow));
		if (direction.equals("DOWN"))
			if (step==1)
				gc.drawImage(image, 0, 0, subImageWidth, subImageHeight, hitbox.getX(), hitbox.getY(), subImageWidth, subImageHeight);
			else if (step==2)
				gc.drawImage(image, subImageWidth, 0, subImageWidth, subImageHeight, hitbox.getX(), hitbox.getY(), subImageWidth, subImageHeight);
			else if (step==3)
				gc.drawImage(image, subImageWidth*2, 0, subImageWidth, subImageHeight, hitbox.getX(), hitbox.getY(), subImageWidth, subImageHeight);

		if (direction.equals("UP"))
			if (step==1)
				gc.drawImage(image, 0, subImageHeight*3, subImageWidth, subImageHeight, hitbox.getX(), hitbox.getY(), subImageWidth, subImageHeight);
			else if (step==2)
				gc.drawImage(image, subImageWidth, subImageHeight*3, subImageWidth, subImageHeight, hitbox.getX(), hitbox.getY(), subImageWidth, subImageHeight);
			else if (step==3)
				gc.drawImage(image, subImageWidth*2, subImageHeight*3, subImageWidth, subImageHeight, hitbox.getX(), hitbox.getY(), subImageWidth, subImageHeight);

		if (direction.equals("LEFT"))
			if (step==1)
				gc.drawImage(image, 0, subImageHeight, subImageWidth, subImageHeight, hitbox.getX(), hitbox.getY(), subImageWidth, subImageHeight);	
			else if (step==2)
				gc.drawImage(image, subImageWidth, subImageHeight, subImageWidth, subImageHeight, hitbox.getX(), hitbox.getY(), subImageWidth, subImageHeight);
			else if (step==3)
				gc.drawImage(image, subImageWidth*2, subImageHeight, subImageWidth, subImageHeight, hitbox.getX(), hitbox.getY(), subImageWidth, subImageHeight);

		if (direction.equals("RIGHT"))
			if (step==1)
				gc.drawImage(image, 0, subImageHeight*2, subImageWidth, subImageHeight, hitbox.getX(), hitbox.getY(), subImageWidth, subImageHeight);	
			else if (step==2)
				gc.drawImage(image, subImageWidth, subImageHeight*2, subImageWidth, subImageHeight, hitbox.getX(), hitbox.getY(), subImageWidth, subImageHeight);
			else if (step==3)
				gc.drawImage(image, subImageWidth*2, subImageHeight*2, subImageWidth, subImageHeight, hitbox.getX(), hitbox.getY(), subImageWidth, subImageHeight);

		gc.setEffect(null);
		gc.setGlobalAlpha(1.0);

		//gc.fillRect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());

	}
	/**
	 * This cast ability is whenever the player left clicks the mouse
	 * <br>This method creates the first cast ability for the player
	 * <br>There is a different cast ability for each player class
	 * <li>Either Archer, Steady Shot
	 * <br> Shoots a projectile attack
	 * <li> Mage, Magic Missile
	 * <br> Shoots a projectile attack
	 * <li>Or Fighter, Sword Slash
	 * <br> Uses a melee attack
	 */
	public void castAbility1() {

		// Creates variables to create cast abilities such as damage, speed and radius
		double shotDamage = 10;
		double shotSpeed = 10;
		double missileDamage = 30;
		double missileSpeed = 10;
		double swordDamage = 20;
		double swordRadius = 100;
		// Checks which player class the user is using and makes the cast ability for the specific one
		if (playerClass.equals("ARCHER") && ammo > 0) { //Steady Shot(Projectile)
			double angle = Combat.calculateAngle(getCenterX(), getCenterY(), Input.mouseX, Input.mouseY);
			double xSpeed = -(Math.cos(angle-Math.PI)*shotSpeed);
			double ySpeed = Math.sin(angle-Math.PI)*shotSpeed;
			Projectile arrow = new Projectile(arrowImage, getCenterX(), getCenterY(), xSpeed, ySpeed, "PLAYER", shotDamage);
			Projectile.projectiles.add(arrow);
			cooldowns[0] = maxCooldowns[0];
			ammo--;
		} else if (playerClass.equals("MAGE")) { //Magic Missile(Projectile)
			double angle = Combat.calculateAngle(getCenterX(), getCenterY(), Input.mouseX, Input.mouseY);
			double xSpeed = -(Math.cos(angle-Math.PI)*missileSpeed);
			double ySpeed = Math.sin(angle-Math.PI)*missileSpeed;
			Projectile missile = new Projectile(missileImage, getCenterX(), getCenterY(), xSpeed, ySpeed, "PLAYER", missileDamage);
			Projectile.projectiles.add(missile);
			cooldowns[0] = maxCooldowns[0];
		} else if (playerClass.equals("WARRIOR")) { //Sword Slash(Melee attack)
			double angle = Combat.calculateAngle(getCenterX(), getCenterY(), Input.mouseX, Input.mouseY);
			Arc slash = new Arc(getCenterX(), getCenterY(), swordRadius, swordRadius, Math.toDegrees(angle)-45, 90);
			slash.setFill(Color.GRAY);
			Combat.melee(this, swordDamage, "PLAYER", slash, null, false);
			cooldowns[0] = maxCooldowns[0];
		}
	}
	/**
	 * This is whenever the player right clicks<p>
	 * This method creates the second cast ability for the player
	 * <li></li>Archer - Volley
	 * <li>shoots multiple arrows at once</li>
	 * Mage - Arcane Orb
	 * <li>shoots a colored ball</li>
	 * Warrior - ThunderClap
	 * <li>Creates an area damage attack infront of the warrior</li>
	 */
	public void castAbility2() {

		// Creates variables to create cast abilities such as damage, speed, radius and range
		double volleyDamage = 10;
		double volleySpeed = 10;

		double orbDamage = 30;
		double orbManaCost = 40;
		double orbSpeed = 10;

		double thunderclapDamage = 30;
		double thunderclapRadius = 80;
		double thunderclapRange = 80;

		// Checks which player class the user is using and makes the cast ability for the specific one
		if (playerClass.equals("ARCHER")) { //Volley(projectile)
			double angle1 = Combat.calculateAngle(getCenterX(), getCenterY(), Input.mouseX, Input.mouseY);
			double angle2 = Combat.calculateAngle(getCenterX(), getCenterY(), Input.mouseX, Input.mouseY) - Math.toRadians(10);
			double angle3 = Combat.calculateAngle(getCenterX(), getCenterY(), Input.mouseX, Input.mouseY) + Math.toRadians(10);
			double angle4 = Combat.calculateAngle(getCenterX(), getCenterY(), Input.mouseX, Input.mouseY) - Math.toRadians(5);
			double angle5 = Combat.calculateAngle(getCenterX(), getCenterY(), Input.mouseX, Input.mouseY) + Math.toRadians(5);

			double xSpeed1 = -(Math.cos(angle1-Math.PI)*volleySpeed);
			double ySpeed1 = Math.sin(angle1-Math.PI)*volleySpeed;
			double xSpeed2 = -(Math.cos(angle2-Math.PI)*volleySpeed);
			double ySpeed2 = Math.sin(angle2-Math.PI)*volleySpeed;
			double xSpeed3 = -(Math.cos(angle3-Math.PI)*volleySpeed);
			double ySpeed3 = Math.sin(angle3-Math.PI)*volleySpeed;
			double xSpeed4 = -(Math.cos(angle4-Math.PI)*volleySpeed);
			double ySpeed4 = Math.sin(angle4-Math.PI)*volleySpeed;
			double xSpeed5 = -(Math.cos(angle5-Math.PI)*volleySpeed);
			double ySpeed5 = Math.sin(angle5-Math.PI)*volleySpeed;

			Projectile arrow1 = new Projectile(arrowImage, getCenterX(), getCenterY(), xSpeed1, ySpeed1, "PLAYER", volleyDamage);
			Projectile arrow2 = new Projectile(arrowImage, getCenterX(), getCenterY(), xSpeed2, ySpeed2, "PLAYER", volleyDamage);
			Projectile arrow3 = new Projectile(arrowImage, getCenterX(), getCenterY(), xSpeed3, ySpeed3, "PLAYER", volleyDamage);
			Projectile arrow4 = new Projectile(arrowImage, getCenterX(), getCenterY(), xSpeed4, ySpeed4, "PLAYER", volleyDamage);
			Projectile arrow5 = new Projectile(arrowImage, getCenterX(), getCenterY(), xSpeed5, ySpeed5, "PLAYER", volleyDamage);

			Projectile.projectiles.add(arrow1);
			Projectile.projectiles.add(arrow2);
			Projectile.projectiles.add(arrow3);
			Projectile.projectiles.add(arrow4);
			Projectile.projectiles.add(arrow5);
			cooldowns[1] = maxCooldowns[1];
		} else if (playerClass.equals("MAGE") && mana >= orbManaCost) { //Arcane Orb(Projectile)
			double angle = Combat.calculateAngle(getCenterX(), getCenterY(), Input.mouseX, Input.mouseY);
			double xSpeed = -(Math.cos(angle-Math.PI)*orbSpeed);
			double ySpeed = Math.sin(angle-Math.PI)*orbSpeed;
			Projectile orb = new Projectile(getCenterX(), getCenterY(), 25, xSpeed, ySpeed, "PLAYER", orbDamage);
			Projectile.projectiles.add(orb);
			mana-=orbManaCost;
			cooldowns[1] = maxCooldowns[1];
		} else if (playerClass.equals("WARRIOR")) { //Thunderclap(AOE)
			double angle = Combat.calculateAngle(getCenterX(), getCenterY(), Input.mouseX, Input.mouseY);
			double clapX = Math.sin(angle+Math.PI/2)*thunderclapRange;
			double clapY = Math.cos(angle+Math.PI/2)*thunderclapRange;
			Circle thunderclap = new Circle(getCenterX() + clapX, getCenterY() + clapY, thunderclapRadius);
			thunderclap.setFill(Color.YELLOW);
			Combat.areaDamage(thunderclap, thunderclapDamage, this, "PLAYER", null);
			cooldowns[1] = maxCooldowns[1];
		}
	}

	/**
	 * This method creates the third cast ability for the player.
	 * <br>There is a different cast ability for each player class.
	 * <li>Archer - Roll
	 * <br>Quickly blink in the direction that you are currently moving towards.
	 * <li>Mage - Blink
	 * <br>Instantly blink
	 * <li>Warrior - Charge
	 * <br>Quickly charge in the direction of the mouse.
	 * @param map
	 * Map used in the game, used to help create ability
	 */
	public void castAbility3(Map map) {

		// Creates variables needed to create cast abilities such as distances and frames for each classes ability
		double rollDistance = 5;

		double rollIFrames = 10;
		double rollDuration = 10;

		double blinkDistance = 4*16;
		double blinkIFrames = 30;

		double chargeDistance = 15;
		double chargeDuration = 10;
		double chargeIFrames = 8;
		double chargeDamage = 40;

		// Checks which class the user is using and makes the cast ability for the specific one
		if (playerClass.equals("ARCHER")) { //Roll

			Thread roll = new Thread(() -> {
				boolean up = false;
				boolean down = false;
				boolean left = false;
				boolean right = false;

				if (Input.buttonPressed[Input.keyMap.get("UP").ordinal()]) up = true;
				if (Input.buttonPressed[Input.keyMap.get("DOWN").ordinal()]) down = true;
				if (Input.buttonPressed[Input.keyMap.get("LEFT").ordinal()]) left = true;
				if (Input.buttonPressed[Input.keyMap.get("RIGHT").ordinal()]) right = true;
				for (int i=0; i<rollDuration;) {
					rolling = true;
					if (up) {
						if (y-rollDistance >= 0) {
							y-=rollDistance;
							map.mapScroll(0, -rollDistance, player);
						}
						cooldowns[2] = maxCooldowns[2];
						invincibleFrames = rollIFrames;
					}
					if (down) {
						if (y+rollDistance <= Input.resHeight-hitbox.getHeight()) {
							y+=rollDistance;
							map.mapScroll(0, rollDistance, player);
						}
						cooldowns[2] = maxCooldowns[2];
						invincibleFrames = rollIFrames;
					}
					if (left) {
						if (x-rollDistance >= 0) {
							x-=rollDistance;
							map.mapScroll(-rollDistance, 0, player);
						}
						cooldowns[2] = maxCooldowns[2];
						invincibleFrames = rollIFrames;
					}
					if (right) {
						if (x+rollDistance <= Input.resWidth-hitbox.getWidth()) {
							x+=rollDistance;
							map.mapScroll(rollDistance, 0, player);
						}
						cooldowns[2] = maxCooldowns[2];
						invincibleFrames = rollIFrames;
					}
					if (!Input.pause) ++i;
					try { Thread.sleep(pauseDuration); } catch (InterruptedException ignored) {}
				}
				rolling = false;
			});
			roll.start();

		} else if (playerClass.equals("MAGE")) {
			if (Input.buttonPressed[Input.keyMap.get("UP").ordinal()] && y-blinkDistance > 0) {
				y-=blinkDistance;
				map.mapScroll(0, -blinkDistance, this);
				cooldowns[2] = maxCooldowns[2];
				invincibleFrames = blinkIFrames;
			}
			if (Input.buttonPressed[Input.keyMap.get("DOWN").ordinal()] && y+blinkDistance < Input.resHeight) {
				y+=blinkDistance;
				map.mapScroll(0, blinkDistance, this);
				cooldowns[2] = maxCooldowns[2];
				invincibleFrames = blinkIFrames;
			}
			if (Input.buttonPressed[Input.keyMap.get("LEFT").ordinal()] && x-blinkDistance > 0) {
				x-=blinkDistance;
				map.mapScroll(-blinkDistance, 0, this);
				cooldowns[2] = maxCooldowns[2];
				invincibleFrames = blinkIFrames;
			}
			if (Input.buttonPressed[Input.keyMap.get("RIGHT").ordinal()] && x+blinkDistance < Input.resWidth) {
				x+=blinkDistance;
				map.mapScroll(blinkDistance, 0, this);
				cooldowns[2] = maxCooldowns[2];
				invincibleFrames = blinkIFrames;
			}
		} else if (playerClass.equals("WARRIOR")) { //Charge
			Thread charge = new Thread(() -> {
				double angle = Combat.calculateAngle(getCenterX(), getCenterY(), Input.mouseX, Input.mouseY);
				charging = true;
				if (angle <= Math.PI/2) {
					direction = "RIGHT";
				} else if (angle <= Math.PI) {
					direction = "UP";
				} else if (angle <= 3*Math.PI/2) {
					direction = "LEFT";
				} else if (angle <= 2*Math.PI) {
					direction = "DOWN";
				}
				charge: {
					for(int i=0; i<chargeDuration;) {
						double xSpeed = -(Math.cos(angle-Math.PI)*chargeDistance);
						double ySpeed = Math.sin(angle-Math.PI)*chargeDistance;
						if (x+xSpeed <= Input.resWidth-hitbox.getWidth() &&
								x+xSpeed >= 0 &&
								y+ySpeed >= 0 &&
								y+ySpeed <= Input.resHeight-hitbox.getHeight()) {
							x+=xSpeed;
							y+=ySpeed;
							map.mapScroll(xSpeed, ySpeed, player);
						}
						hitbox.setX(x);
						hitbox.setY(y);
						for (int j=0; j<Enemy.enemyList.size(); ++j) {
							if (hitbox.intersects(Enemy.enemyList.get(j).getHitbox().getLayoutBounds())) {
								Enemy.enemyList.get(j).setHealth(Enemy.enemyList.get(j).getHealth()-chargeDamage);
								break charge;
							}
						}
						invincibleFrames = chargeIFrames;
						if (!Input.pause) ++i;
						try { Thread.sleep(pauseDuration); } catch (InterruptedException ignored) {}
					}
				}
				charging = false;
			});
			charge.start();
			cooldowns[2] = maxCooldowns[2];
		}

		// Updates hitbox location
		hitbox.setX(x);
		hitbox.setY(y);

	}

	/**
<<<<<<< HEAD

	 * This method creates the fourth cast ability for the player.
	 * <br>There is a different cast ability for each player class.
	 * <li>Either Archer, Rain of arrows
	 * Creates a big periodic AOE attack
	 * <li>Mage, Arcane Barrage
	 * Creates a big delayed AOE attack
	 * <li>Or Warrior, Whirlwind
	 * Creates a big area damage attack
=======
	 * This method creates the fourth cast ability for the player
	 * There is a different cast ability for each player class
	 * Either Archer, Rain of arrows
	 * Mage, Arcane Barrage
	 * Or Warrior, Whirlwind
>>>>>>> refs/remotes/origin/alex
	 */
	public void castAbility4() {

		// Creates variables needed to create cast ability such as damage, radius, and duration for each player class
		double rainDamage = 50;
		double rainRadius = 120; //lingering area of effect, each tick deals damage to enemies inside
		double rainTicks = 30;
		double rainDuration = rainTicks*5;

		double barrageDamage = 30;
		double barrageRadius = 120; //delayed area of effect (lands on the area you selected 0.5 seconds ago)
		double barrageManaCost = 40;
		double barrageDelay = 30;

		double whirlDamage = 50;
		double whirlRadius = 120; //from center of the character, instant

		// Checks which player class the user is using and makes the cast ability for the specific one
		if (playerClass.equals("ARCHER")) { //Rain of Arrows (AOE)
			Circle rain = new Circle(Input.mouseX, Input.mouseY, rainRadius);
			rain.setFill(Color.BROWN);
			Combat.periodicAOE(rain, rainDamage, this, "PLAYER", rainTicks, rainDuration, null);
			//Combat.areaDamage(rain, rainDamage, this, "PLAYER", null);			
			cooldowns[3] = maxCooldowns[3];
		} else if (playerClass.equals("MAGE") && mana >= barrageManaCost) { //Arcane Barrage (AOE)
			Circle barrage = new Circle(Input.mouseX, Input.mouseY, barrageRadius);
			barrage.setFill(Color.CORNFLOWERBLUE);
			Combat.delayedAOE(barrage, barrageDamage, this, "PLAYER", barrageDelay, null);
			mana-=barrageManaCost;
			cooldowns[3] = maxCooldowns[3];
		} else if (playerClass.equals("WARRIOR")) { //Whirlwind (AOE)
			Circle whirl = new Circle(getCenterX(), getCenterY(), whirlRadius); // circle comes from near the player
			whirl.setFill(Color.GRAY);
			Combat.areaDamage(whirl, whirlDamage, this, "PLAYER", null);
			cooldowns[3] = maxCooldowns[3];
		}
	}
	/**
	 * This method creates the fifth cast ability for the player.
	 * <br>There is a different cast ability for each player class.
	 * <li>Either Archer, Piercing Shot
	 * A giant projectile based attack
	 * <li>Mage, Wave of force
	 * A long range melee based attack
	 * <li>Or Warrior, Parry
	 * A parry, meaning reflects all projectile attacks
	 */
	public void castAbility5() {

		// Creates variables needed to create cast ability such as damage, radius, and speed for each player class
		double pierceDamage = 3; 
		double pierceSpeed = 8;
		double waveDamage = 40; //destroys enemy projectiles in a cone area
		double waveRadius = 180;
		double waveManaCost = 40;

		// Checks which player class the user is using and makes the cast ability for the specific one
		if (playerClass.equals("ARCHER")) { // Piercing Shot (Projectile)
			double angle = Combat.calculateAngle(getCenterX(), getCenterY(), Input.mouseX, Input.mouseY);
			double xSpeed = -(Math.cos(angle-Math.PI)*pierceSpeed);
			double ySpeed = Math.sin(angle-Math.PI)*pierceSpeed;
			Projectile bigShot = new Projectile(biggerArrow, getCenterX(), getCenterY(), xSpeed, ySpeed, "PLAYER", pierceDamage);
			bigShot.setDestroyOnHit(false);
			bigShot.setDestroysProjectiles(true);
			Projectile.projectiles.add(bigShot);
			cooldowns[4] = maxCooldowns[4];
		} else if (playerClass.equals("MAGE") && mana >= waveManaCost) { //Wave of Force (Melee Attack)
			double angle = Combat.calculateAngle(getCenterX(), getCenterY(), Input.mouseX, Input.mouseY);
			Arc wave = new Arc(getCenterX(), getCenterY(), waveRadius, waveRadius, Math.toDegrees(angle)-45, 90);
			wave.setFill(Color.AQUA);
			Combat.melee(this, waveDamage, "PLAYER", wave, null, true);
			mana-=waveManaCost;
			cooldowns[4] = maxCooldowns[4];
		} else if (playerClass.equals("WARRIOR")) { // Parry (Reflect Attacks)
			parryFrames = 90; 
			parrying = true;
			cooldowns[4] = maxCooldowns[4];
		}
	}
	/**
	 * Gets direction of player
	 * @return direction
	 * Returns the direction of the player
	 */
	public String getDirection() {
		return direction;
	}
	/**
	 * Sets the direction of the player.
	 * 
	 * @param direction
	 *            New direction.
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}
	/**
	 * Gets mana of the player
	 * @return mana
	 * Returns the mana of the player
	 */
	public double getMana() {
		return mana;
	}
	/**
	 * Gets max mana of the player
	 * @return maxMana
	 * Returns the max mana of the player
	 */
	public double getMaxMana() {
		return maxMana;
	}
	/**
	 * Sets the max mana of the player.
	 * 
	 * @param maxMana
	 *            New max mana value.
	 */
	public void setMaxMana(double maxMana) {
		this.maxMana = maxMana;
	}
	/**
	 * Gets max ammo of the player
	 * @return maxAmmo
	 * Returns the max ammo of the player
	 */
	public double getMaxAmmo() {
		return maxAmmo;
	}
	/**
	 * Sets the max ammo of the player.
	 * 
	 * @param maxAmmo
	 *            New max ammo value.
	 */
	public void setMaxAmmo(double maxAmmo) {
		this.maxAmmo = maxAmmo;
	}
	/**
	 * Sets the mana of the player.
	 * 
	 * @param mana
	 *            New mana value.
	 */
	public void setMana(double mana) {
		this.mana = mana;
	}
	/**
	 * Gets ammo of the player
	 * @return ammo
	 * Returns the ammo of the player
	 */
	public double getAmmo() {
		return ammo;
	}
	/**
	 * Sets the ammo of the player.
	 * 
	 * @param ammo
	 *            New ammo value.
	 */
	public void setAmmo(double ammo) {
		this.ammo = ammo;
	}
	/**
	 * Gets a double for the mana regeneration of the player
	 * @return manaRegen
	 * Returns the mana regeneration of the player
	 */
	public double getManaRegen() {
		return manaRegen;
	}
	/**
	 * Sets the mana regeneration of the player.
	 * 
	 * @param manaRegen
	 *            New mana regeneration value.
	 */
	public void setManaRegen(double manaRegen) {
		this.manaRegen = manaRegen;
	}
	/**
	 * Gets the sub image width for the player
	 * @return subImageWidth
	 * Returns the sub image width of the player
	 */
	public double getSubImageWidth() {
		return subImageWidth;
	}
	/**
	 * Sets the sub image width of the player.
	 * 
	 * @param subImageWidth
	 *            New sub image width.
	 */
	public void setSubImageWidth(long subImageWidth) {
		this.subImageWidth = subImageWidth;
	}
	/**
	 * Gets the sub image height for the player
	 * @return subImageHeight
	 * Returns the sub image height of the player
	 */
	public double getSubImageHeight() {
		return subImageHeight;
	}
	/**
	 * Sets the sub image height of the player.
	 * 
	 * @param subImageHeight
	 *            New sub image height.
	 */
	public void setSubImageHeight(long subImageHeight) {
		this.subImageHeight = subImageHeight;
	}
	/**
	 * A boolean method that checks if the invisibleFrames is greater then 
	 * @return
	 * Returns true or false whether invisibleFrames is greater then 0
	 */
	public boolean isInvincible() {
		return invincibleFrames > 0;
	}
	/**
	 * Gets the invisible frames for the player
	 * @return invisibleFrames
	 * Returns the invisible frames for the player
	 */
	public double getInvincibleFrames() {
		return invincibleFrames;
	}
	/**
	 * Sets the invisible frames for the player.
	 * 
	 * @param invincibleFrames
	 *            New invisible frames.
	 */
	public void setInvincibleFrames(double invincibleFrames) {
		this.invincibleFrames = invincibleFrames;
	}
	/**
	 * Gets the player class of the player
	 * @return playerClass
	 * Returns a string containing the player class of either Archer, Mage, or Warrior
	 */
	public String getPlayerClass() {
		return playerClass;
	}
	/**
	 * Sets the player class of the player.
	 * 
	 * @param playerClass
	 *            New/type of playerClass.
	 */
	public void setPlayerClass(String playerClass) {
		this.playerClass = playerClass;
	}
	/**
	 * Gets the max recharge frames of the player
	 * @return maxRechargeFrames
	 * Returns max recharge frames of the player
	 */
	public int getMaxRechargeFrames() {
		return maxRechargeFrames;
	}
	/**
	 * Sets the max recharge frames of the player.
	 * 
	 * @param maxRechargeFrames
	 *            New max recharge frames.
	 */
	public void setMaxRechargeFrames(int maxRechargeFrames) {
		this.maxRechargeFrames = maxRechargeFrames;
	}
	/**
	 * Holds boolean for whether an attack is being casted 
	 * @return
	 * Returns true or false whether the player is casting
	 */
	public boolean isCasting() {
		return casting;
	}
	/**
	 * Sets casting for the player.
	 * 
	 * @param casting
	 *            A boolean shows if the player is casting an attack.
	 */
	public void setCasting(boolean casting) {
		this.casting = casting;
	}
	/**
	 * Gets the current cast of the player
	 * @return currentCast
	 * Returns the current cast of the player
	 */
	public double getCurrentCast() {
		return currentCast;
	}
	/**
	 * Sets current cast ability of the player.
	 * 
	 * @param currentCast
	 *            New current cast.
	 */
	public void setCurrentCast(double currentCast) {
		this.currentCast = currentCast;
	}
	/**
	 * Gets the max cast of the player
	 * @return maxCast
	 * Returns the max cast of the player
	 */
	public double getMaxCast() {
		return maxCast;
	}
	/**
	 * Sets max cast of the player.
	 * 
	 * @param maxCast
	 *            New max cast.
	 */
	public void setMaxCast(double maxCast) {
		this.maxCast = maxCast;
	}
	/**
	 * Gets the ammo recharge frames for the player
	 * @return ammoRechargeFrames
	 * Returns the ammo recharge frames of the player
	 */
	public double getAmmoRechargeFrames() {
		return ammoRechargeFrames;
	}
	/**
	 * Sets ammo recharge frames of the player.
	 * 
	 * @param ammoRechargeFrames
	 *            New ammo recharge frames.
	 */
	public void setAmmoRechargeFrames(double ammoRechargeFrames) {
		this.ammoRechargeFrames = ammoRechargeFrames;
	}
	/**
	 * Holds boolean for whether an attack is being parried 
	 * @return parrying
	 * Returns true or false whether the player is parrying an attack
	 */
	public boolean isParrying() {
		return parrying;
	}
	/**
	 * Sets boolean parrying for the player.
	 * 
	 * @param parrying
	 *            New boolean parrying.
	 */
	public void setParrying(boolean parrying) {
		this.parrying = parrying;
	}
	/**
	 * Gets the parrying frames for the player
	 * @return parryFrames
	 * Returns the parrying frames of the player
	 */
	public double getParryFrames() {
		return parryFrames;
	}
	/**
	 * Sets parry frames for the player.
	 * 
	 * @param parryFrames
	 *            New parrying frames.
	 */
	public void setParryFrames(double parryFrames) {
		this.parryFrames = parryFrames;
	}
	/**
	 * Holds boolean for whether the player is rolling
	 * @return rolling
	 * Returns true or false for whether the player is using the roll attack
	 */
	public boolean isRolling() {
		return rolling;
	}
	/**
	 * Sets rolling boolean for the player.
	 * 
	 * @param rolling
	 *            New boolean rolling.
	 */
	public void setRolling(boolean rolling) {
		this.rolling = rolling;
	}
	/**
	 * Holds boolean for whether the player is charging
	 * @return charging
	 * Returns true or false for whether the player is using the charge attack
	 */
	public boolean isCharging() {
		return charging;
	}
	/**
	 * Sets charging boolean for the player.
	 * 
	 * @param charging
	 *            New boolean charging.
	 */
	public void setCharging(boolean charging) {
		this.charging = charging;
	}
}