package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
/**
 * <p>This class contains the <b>GUI (graphical user interface).</b>
 * <br>This is where all the graphics in the game are created.
 * <li><i>Main menu</i>
 * <br> Creates images, buttons, and current option pointer, to draw main menu.
 * <p><br><li><i> Class Selection</i>
 * <br>Creates images, character portraits and option variables to create class selection screen.
 * <p><br><li><i>Pause Screen</i>
 * <br> Creates buttons for pause screen.
 * <p><br>The GUI also creates things on the map such as the abilities, cast times, health, mana, and ammo bars in the game.</p> 
 * <br>January 22, 2018
 * <br>GUI.java
 * @author Richard Pena
 * @author Alex Co
 * @author Mbarak Al-Amry
 */
public class GUI {

	// Creates the main menu, and pause rectangles and sets their location
	static Rectangle arcade = new Rectangle(540, 305, 200, 65); 
	static Rectangle tutorial = new Rectangle(540, 395, 200, 65);
	static Rectangle exit = new Rectangle(540, 490, 200, 65);
	static Rectangle pause = new Rectangle(1000,600, 200, 65);
	// Creates image variables that get the images used for main menu
	static Image image = new Image("/files/main_menu.png");
	static Image arcadeButton = new Image("/files/arcade.png");
	static Image arcadeH = new Image("/files/arcade_h.png");
	static Image tutorialButton = new Image("/files/tutorial.png");
	static Image tutorialH = new Image("/files/tutorial_h.png");
	static Image exitButton = new Image("/files/exit_game.png");
	static Image exitH = new Image("/files/exit_game_h.png");
	static Image pointer = new Image("files/pointer.png");

	/**
	 * <p>Draws main menu buttons and current option pointer.
	 * <br> Main menu options are chosen by the user. </p>
	 * @param gc
	 * 			The graphics context.
	 */
	public static void drawMenu(GraphicsContext gc) {
		gc.drawImage(image, 0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

		//-------------------------------------
		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(15.0);
		dropShadow.setOffsetX(7.0);
		dropShadow.setOffsetY(7.0);
		dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
		gc.setEffect(dropShadow);
		//-------------------------------------

		// Drawing menu buttons
		if (Input.menuOption == 1) {
			gc.drawImage(arcadeH, arcade.getX(), arcade.getY(), arcade.getWidth(), arcade.getHeight());
		} else gc.drawImage(arcadeButton, arcade.getX(), arcade.getY(), arcade.getWidth(), arcade.getHeight());
		if (Input.menuOption == 2) {
			gc.drawImage(tutorialH, tutorial.getX(), tutorial.getY(), tutorial.getWidth(), tutorial.getHeight());
		} else gc.drawImage(tutorialButton, tutorial.getX(), tutorial.getY(), tutorial.getWidth(), tutorial.getHeight());
		if (Input.menuOption == 3) {
			gc.drawImage(exitH, exit.getX(), exit.getY(), exit.getWidth(), exit.getHeight());
		} else gc.drawImage(exitButton, exit.getX(), exit.getY(), exit.getWidth(), exit.getHeight());

		gc.setEffect(null);

		// Drawing current option pointer
		gc.setStroke(Color.YELLOW);
		if(Input.menuOption == 1) {
			gc.drawImage(pointer, arcade.getX()+arcade.getWidth()+5, arcade.getY()+arcade.getHeight()/2-10, 25, 30);
		}
		else if(Input.menuOption == 2) {
			gc.drawImage(pointer, arcade.getX()+arcade.getWidth()+5, tutorial.getY()+tutorial.getHeight()/2-10, 25, 30);
		}
		else if(Input.menuOption == 3) {
			gc.drawImage(pointer, exit.getX()+exit.getWidth()+5, exit.getY()+exit.getHeight()/2-10, 25, 30);
		}
	}

	/**
	 * <p>Draws <i>pause screen</i>.
	 * <br>Creates faded background and text to explain it.
	 * <br>Main menu button is also drawn so the user can return to the main menu.</p>
	 * @param gc
	 * 			The graphics context.
	 */
	public static void drawPauseScreen(GraphicsContext gc) {		
		gc.setGlobalAlpha(0.5);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		gc.setGlobalAlpha(1.0);
		Font temp = gc.getFont();
		Font big = new Font(30);
		gc.setFont(big);
		gc.setFill(Color.RED);
		gc.fillRoundRect(pause.getX(), pause.getY(), pause.getWidth(), pause.getHeight(), 45.0, 45.0);
		gc.setFill(Color.BLACK);
		gc.fillText("PAUSED ",  gc.getCanvas().getWidth()/2-50, gc.getCanvas().getHeight()/2-80);
		gc.fillText("PRESS ESC AGAIN TO CONTINUE ", gc.getCanvas().getWidth()/2-160, (gc.getCanvas().getHeight()/2));
		gc.fillText("Main Menu", 1025, 640);
		gc.setFont(temp);
	}

	// Creates class select images
	static Image classSelect0 = new Image("/files/choose_class_0.png");
	static Image classSelect1 = new Image("/files/choose_class_1.png");
	static Image classSelect2 = new Image("/files/choose_class_2.png");
	static Image classSelect3 = new Image("/files/choose_class_3.png");
	static Image classSelect4 = new Image("/files/choose_class_4.png");
	// Creates class selection rectangles for each player class
	static Rectangle archer = new Rectangle(220, 193, 233, 394); 
	static Rectangle mage = new Rectangle(545, 193, 170, 394);
	static Rectangle warrior = new Rectangle(784, 193, 335, 394);
	static Rectangle returnButton = new Rectangle(526, 628, 225, 75);

	/**
	 * <p>Draws <i>class selection.</i>.
	 * <br> Archer, Mage, and Warrior are the options.
	 * <br>There is also a return statement created to go back to main menu.</p>
	 * @param gc
	 * 			The graphics context.
	 */
	public static void drawClassSelection(GraphicsContext gc) {
		gc.drawImage(classSelect0, 0, 0);
		if (archer.contains(Input.mouseX, Input.mouseY)) gc.drawImage(classSelect1, 0, 0);
		if (mage.contains(Input.mouseX, Input.mouseY)) gc.drawImage(classSelect2, 0, 0);
		if (warrior.contains(Input.mouseX, Input.mouseY)) gc.drawImage(classSelect3, 0, 0);
		if (returnButton.contains(Input.mouseX, Input.mouseY)) gc.drawImage(classSelect4, 0, 0);

	}

	static final Image moveTutorial = new Image("/files/move_tutorial.png");
	static final Image abilityTutorial = new Image("/files/ability_tutorial.png");
	static final Image archerTutorial = new Image("/files/archer_tutorial.png");
	static final Image mageTutorial = new Image("/files/mage_tutorial.png");
	static final Image warriorTutorial = new Image("/files/warrior_tutorial.png");
	static final Rectangle tutorialArrow = new Rectangle(1210,638,70,82);
	static int tutorialSlide = 1;

	/**
	 * </p>Draws <i>Tutorial mode.</i>.
	 * <br>Slides are drawn to explain the game in tutorial.
	 * <br>resets slideshow when the user is done reading how to play.</p>
	 * @param gc
	 * 			The graphics context.
	 */
	public static void drawTutorial(GraphicsContext gc) {
		if (tutorialSlide == 1) {
			gc.drawImage(moveTutorial, 0, 0);
		} else if (tutorialSlide == 2) {
			gc.drawImage(abilityTutorial, 0, 0);
		} else if (tutorialSlide == 3) {
			gc.drawImage(archerTutorial, 0, 0);
		} else if (tutorialSlide == 4) {
			gc.drawImage(mageTutorial, 0, 0);
		} else if (tutorialSlide == 5) {
			gc.drawImage(warriorTutorial, 0, 0);
		}
		else { // Reset slideshow
			Input.tutMode = false;
			Input.mainMenu = true;
			tutorialSlide = 1;
		}
	}

	//----------------------------
	// Creates health and health bars in the game
	private static final double HEALTH_X1 = 100;
	private static final double HEALTH_X2 = 120;
	private static final double HEALTH_Y = 40;
	private static final double HEALTH_WIDTH = 180;
	private static final double HEALTH_HEIGHT = 20;
	private static final double[] xHealthPoints = {HEALTH_X1, HEALTH_X2, HEALTH_X2+HEALTH_WIDTH, HEALTH_X1+HEALTH_WIDTH};
	private static final double[] Y_HEALTH_POINTS = {HEALTH_Y+HEALTH_HEIGHT, HEALTH_Y, HEALTH_Y, HEALTH_Y+HEALTH_HEIGHT};
	private static final int N_HEALTH_POINTS = 4;
	//-----------------------------
	// Creates ammo and ammo bars in the game
	private static final double AMMO_X1 = 80;
	private static final double AMMO_X2 = 100;
	private static final double AMMO_Y = 60;
	private static final double AMMO_WIDTH = 25;
	private static final double AMMO_HEIGHT = 20;
	private static final double[] xAmmoPoints = {AMMO_X1, AMMO_X2, AMMO_X2+AMMO_WIDTH, AMMO_X1+AMMO_WIDTH};
	private static final double[] Y_AMMO_POINTS = {AMMO_Y+AMMO_HEIGHT, AMMO_Y, AMMO_Y, AMMO_Y+AMMO_HEIGHT};
	private static final int N_AMMO_POINTS = 4;
	private static final double X_AMMO_GAP = 5;
	//---------------------------------------
	// Creates mana and mana bars in the game
	private static final double MANA_X1 = 80;
	private static final double MANA_X2 = 100;
	private static final double MANA_Y = 60;
	private static final double MANA_WIDTH = 180;
	private static final double MANA_HEIGHT = 20;
	private static final double[] xManaPoints = {MANA_X1, MANA_X2, MANA_X2+MANA_WIDTH, MANA_X1+MANA_WIDTH};
	private static final double[] Y_MANA_POINTS = {MANA_Y+MANA_HEIGHT, MANA_Y, MANA_Y, MANA_Y+MANA_HEIGHT};
	private static final int N_MANA_POINTS = 4;
	//---------------------------------------
	// Creates cast ability pictures in the game
	private static final double CAST_X1 = 60;
	private static final double CAST_X2 = 80;
	private static final double CAST_Y = 80;
	private static final double CAST_WIDTH = 180;
	private static final double CAST_HEIGHT = 10;
	private static final double[] xCastPoints = {CAST_X1, CAST_X2, CAST_X2+CAST_WIDTH, CAST_X1+CAST_WIDTH};
	private static final double[] Y_CAST_POINTS = {CAST_Y+CAST_HEIGHT, CAST_Y, CAST_Y, CAST_Y+CAST_HEIGHT};
	private static final int N_CAST_POINTS = 4;
	//---------------------------------------
	// Creates borders for the graphics in the game
	private static final double[] X_BORDER1_POINTS = {HEALTH_X1, HEALTH_X2, HEALTH_X2+HEALTH_WIDTH, HEALTH_X1+HEALTH_WIDTH};
	private static final double[] Y_BORDER1_POINTS = {HEALTH_Y+HEALTH_HEIGHT, HEALTH_Y, HEALTH_Y, HEALTH_Y+HEALTH_HEIGHT};
	private static final double[] X_BORDER2_POINTS = {MANA_X1, MANA_X2, MANA_X2+MANA_WIDTH, MANA_X1+MANA_WIDTH};
	private static final double[] Y_BORDER2_POINTS = {MANA_Y+MANA_HEIGHT, MANA_Y, MANA_Y, MANA_Y+MANA_HEIGHT};
	private static final double[] X_BORDER3_POINTS = {CAST_X1, CAST_X2, CAST_X2+CAST_WIDTH, CAST_X1+CAST_WIDTH};
	private static final double[] Y_BORDER3_POINTS = {CAST_Y+CAST_HEIGHT, CAST_Y, CAST_Y, CAST_Y+CAST_HEIGHT};
	private static final int N_BORDER_POINTS = 4;
	//---------------------------------------
	// Creates abilities in the game for each player class
	private static final double ABILITY_WIDTH = 50;
	private static final double ABILITY_HEIGHT = 50;

	private static final double[] ABILITY_X = {20, 90, 160, 230, 300};
	private static final double[] ABILITY_Y = {650, 650, 650, 650, 650};

	private static final double PORTRAIT_X = 30;
	private static final double PORTRAIT_Y = 30;
	private static final double PORTRAIT_WIDTH = 70;
	private static final double PORTRAIT_HEIGHT = 70;

	static final Image archerAbilities = new Image("files/archer_abilities.png");
	static final Image mageAbilities = new Image("files/mage_abilities.png");
	static final Image warriorAbilities = new Image("files/warrior_abilities.png");

	static final Image archerCirclePortrait = new Image("files/archer_circle_portrait.png");
	static final Image mageCirclePortrait = new Image("files/mage_circle_portrait.png");
	static final Image warriorCirclePortrait = new Image("files/warrior_circle_portrait.png");

	static final Color transparentBlack = new Color(0, 0, 0, (float) 0.5);

	/**
	 * <p>This method is used to update the x values of a player's health and mana bar, so that they may be.
	 * <br>drawn onto the screen HUD with accurate values.</p>
	 * @param player Player object to be evaluated.
	 */
	public static void updateElements(Player player) {

		if (player.getHealth() > 0) { // update health bar
			xHealthPoints[2] = HEALTH_X2+HEALTH_WIDTH*(player.getHealth()/player.getMaxHealth());
			xHealthPoints[3] = HEALTH_X1+HEALTH_WIDTH*(player.getHealth()/player.getMaxHealth());
		} else {
			xHealthPoints[2] = HEALTH_X2;
			xHealthPoints[3] = HEALTH_X1;
		}

		if (player.getMana() > 0) { // update mana bar
			xManaPoints[2] = MANA_X2+MANA_WIDTH*(player.getMana()/player.getMaxMana());
			xManaPoints[3] = MANA_X1+MANA_WIDTH*(player.getMana()/player.getMaxMana());
		} else {
			xManaPoints[2] = MANA_X2;
			xManaPoints[3] = MANA_X1;
		}

		if (player.getCurrentCast() > 0) { // update cast time bar
			xCastPoints[2] = CAST_X2+CAST_WIDTH*(player.getCurrentCast()/player.getMaxCast());
			xCastPoints[3] = CAST_X1+CAST_WIDTH*(player.getCurrentCast()/player.getMaxCast());
		} else {
			xCastPoints[2] = CAST_X2;
			xCastPoints[3] = CAST_X1;
		}
		if (player.isReloading()) { // reload
			xCastPoints[2] = CAST_X2+CAST_WIDTH*(player.getAmmoRechargeFrames()/player.getMaxRechargeFrames());
			xCastPoints[3] = CAST_X1+CAST_WIDTH*(player.getAmmoRechargeFrames()/player.getMaxRechargeFrames());
		}
	}
	/**
	 * <p>This method draws the HUD onto the screen while the Arcade mode is on.
	 * <br>The HUD displays the following information:
	 * <li>Character portrait
	 * <li>Health bar
	 * <li>Ammo bar (Archer only)
	 * <li>Mana bar (Mage only)
	 * <li>Cast time bar
	 * <li>Abilities
	 * @param gc Graphics Context.
	 * @param player Player object from which values will be taken from.
	 */
	public static void drawHUD(GraphicsContext gc, Player player) {
		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(5.0);
		dropShadow.setOffsetX(3.0);
		dropShadow.setOffsetY(3.0);
		dropShadow.setColor(Color.color(0.4, 0.5, 0.5)); 

		gc.setEffect(dropShadow);
		updateElements(player);

		gc.setFill(Color.DARKGRAY); // Draw borders
		gc.fillPolygon(X_BORDER1_POINTS, Y_BORDER1_POINTS, N_BORDER_POINTS);
		gc.fillPolygon(X_BORDER2_POINTS, Y_BORDER2_POINTS, N_BORDER_POINTS);
		gc.fillPolygon(X_BORDER3_POINTS, Y_BORDER3_POINTS, N_BORDER_POINTS); 

		gc.setFill(Color.GREEN); // Draw health bar
		gc.fillPolygon(xHealthPoints, Y_HEALTH_POINTS, N_HEALTH_POINTS);

		if (player.getPlayerClass().equals("ARCHER")) { // Draw ammo
			gc.setFill(Color.ORANGE);
			for (int i=0; i<player.getAmmo(); ++i) {

				gc.fillPolygon(xAmmoPoints, Y_AMMO_POINTS, N_AMMO_POINTS);
				for (int j=0; j<xAmmoPoints.length; ++j) {
					xAmmoPoints[j]+=(X_AMMO_GAP+AMMO_WIDTH);
				}				
			}
			xAmmoPoints[0] = AMMO_X1;
			xAmmoPoints[1] = AMMO_X2;
			xAmmoPoints[2] = AMMO_X2+AMMO_WIDTH;
			xAmmoPoints[3] = AMMO_X1+AMMO_WIDTH;	
		} else if (player.getPlayerClass().equals("MAGE")) { // Draw mana bar
			gc.setFill(Color.DEEPSKYBLUE);
			gc.fillPolygon(xManaPoints, Y_MANA_POINTS, N_MANA_POINTS);
		}

		gc.setFill(Color.RED); // Draw cast time bar
		gc.fillPolygon(xCastPoints, Y_CAST_POINTS, N_CAST_POINTS);

		gc.setFill(transparentBlack); // Draw ability icons
		switch (player.getPlayerClass()) {
			case "ARCHER" -> {
				gc.drawImage(archerAbilities, 0, 0, archerAbilities.getWidth() / 5, archerAbilities.getHeight(), ABILITY_X[0], ABILITY_Y[0], ABILITY_WIDTH, ABILITY_HEIGHT);
				gc.drawImage(archerAbilities, archerAbilities.getWidth() / 5, 0, archerAbilities.getWidth() / 5, archerAbilities.getHeight(), ABILITY_X[1], ABILITY_Y[1], ABILITY_WIDTH, ABILITY_HEIGHT);
				gc.drawImage(archerAbilities, archerAbilities.getWidth() / 5 * 2, 0, archerAbilities.getWidth() / 5, archerAbilities.getHeight(), ABILITY_X[2], ABILITY_Y[2], ABILITY_WIDTH, ABILITY_HEIGHT);
				gc.drawImage(archerAbilities, archerAbilities.getWidth() / 5 * 3, 0, archerAbilities.getWidth() / 5, archerAbilities.getHeight(), ABILITY_X[3], ABILITY_Y[3], ABILITY_WIDTH, ABILITY_HEIGHT);
				gc.drawImage(archerAbilities, archerAbilities.getWidth() / 5 * 4, 0, archerAbilities.getWidth() / 5, archerAbilities.getHeight(), ABILITY_X[4], ABILITY_Y[4], ABILITY_WIDTH, ABILITY_HEIGHT);
			}
			case "MAGE" -> {
				gc.drawImage(mageAbilities, 0, 0, mageAbilities.getWidth() / 5, mageAbilities.getHeight(), ABILITY_X[0], ABILITY_Y[0], ABILITY_WIDTH, ABILITY_HEIGHT);
				gc.drawImage(mageAbilities, mageAbilities.getWidth() / 5, 0, mageAbilities.getWidth() / 5, mageAbilities.getHeight(), ABILITY_X[1], ABILITY_Y[1], ABILITY_WIDTH, ABILITY_HEIGHT);
				gc.drawImage(mageAbilities, mageAbilities.getWidth() / 5 * 2, 0, mageAbilities.getWidth() / 5, mageAbilities.getHeight(), ABILITY_X[2], ABILITY_Y[2], ABILITY_WIDTH, ABILITY_HEIGHT);
				gc.drawImage(mageAbilities, mageAbilities.getWidth() / 5 * 3, 0, mageAbilities.getWidth() / 5, mageAbilities.getHeight(), ABILITY_X[3], ABILITY_Y[3], ABILITY_WIDTH, ABILITY_HEIGHT);
				gc.drawImage(mageAbilities, mageAbilities.getWidth() / 5 * 4, 0, mageAbilities.getWidth() / 5, mageAbilities.getHeight(), ABILITY_X[4], ABILITY_Y[4], ABILITY_WIDTH, ABILITY_HEIGHT);
			}
			case "WARRIOR" -> {
				gc.drawImage(warriorAbilities, 0, 0, warriorAbilities.getWidth() / 5, warriorAbilities.getHeight(), ABILITY_X[0], ABILITY_Y[0], ABILITY_WIDTH, ABILITY_HEIGHT);
				gc.drawImage(warriorAbilities, warriorAbilities.getWidth() / 5, 0, warriorAbilities.getWidth() / 5, warriorAbilities.getHeight(), ABILITY_X[1], ABILITY_Y[1], ABILITY_WIDTH, ABILITY_HEIGHT);
				gc.drawImage(warriorAbilities, warriorAbilities.getWidth() / 5 * 2, 0, warriorAbilities.getWidth() / 5, warriorAbilities.getHeight(), ABILITY_X[2], ABILITY_Y[2], ABILITY_WIDTH, ABILITY_HEIGHT);
				gc.drawImage(warriorAbilities, warriorAbilities.getWidth() / 5 * 3, 0, warriorAbilities.getWidth() / 5, warriorAbilities.getHeight(), ABILITY_X[3], ABILITY_Y[3], ABILITY_WIDTH, ABILITY_HEIGHT);
				gc.drawImage(warriorAbilities, warriorAbilities.getWidth() / 5 * 4, 0, warriorAbilities.getWidth() / 5, warriorAbilities.getHeight(), ABILITY_X[4], ABILITY_Y[4], ABILITY_WIDTH, ABILITY_HEIGHT);
			}
		}

		// These are used for cooldowns; When an ability is used, this will show a visual representation of when
		// the ability may be used again by drawing a transparent rectangle over the icon
		gc.fillRect(ABILITY_X[0], ABILITY_Y[0], ABILITY_WIDTH, (player.cooldowns[0]/player.maxCooldowns[0])*ABILITY_HEIGHT);
		gc.fillRect(ABILITY_X[1], ABILITY_Y[1], ABILITY_WIDTH, (player.cooldowns[1]/player.maxCooldowns[1])*ABILITY_HEIGHT);
		gc.fillRect(ABILITY_X[2], ABILITY_Y[2], ABILITY_WIDTH, (player.cooldowns[2]/player.maxCooldowns[2])*ABILITY_HEIGHT);
		gc.fillRect(ABILITY_X[3], ABILITY_Y[3], ABILITY_WIDTH, (player.cooldowns[3]/player.maxCooldowns[3])*ABILITY_HEIGHT);
		gc.fillRect(ABILITY_X[4], ABILITY_Y[4], ABILITY_WIDTH, (player.cooldowns[4]/player.maxCooldowns[4])*ABILITY_HEIGHT);

		// Draw circle portrait
		switch (player.getPlayerClass()) {
			case "ARCHER" -> gc.drawImage(archerCirclePortrait, PORTRAIT_X, PORTRAIT_Y, PORTRAIT_WIDTH, PORTRAIT_HEIGHT);
			case "MAGE" -> gc.drawImage(mageCirclePortrait, PORTRAIT_X, PORTRAIT_Y, PORTRAIT_WIDTH, PORTRAIT_HEIGHT);
			case "WARRIOR" -> gc.drawImage(warriorCirclePortrait, PORTRAIT_X, PORTRAIT_Y, PORTRAIT_WIDTH, PORTRAIT_HEIGHT);
		}

		gc.setEffect(null); 
	}

	static Image crosshair = new Image("files/crosshair.png"); // gets crosshair image and sets crosshair to it
	/**
	 * Draws the in game crosshair 
	 * @param gc
	 *            The graphics context.
	 */
	public static void drawCrosshair(GraphicsContext gc) {
		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(5.0);
		dropShadow.setOffsetX(3.0);
		dropShadow.setOffsetY(3.0);
		dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
		gc.setEffect(dropShadow);
		gc.drawImage(crosshair, Input.mouseX-crosshair.getWidth()/2, Input.mouseY-crosshair.getHeight()/2);
		gc.setEffect(null);
	}
}