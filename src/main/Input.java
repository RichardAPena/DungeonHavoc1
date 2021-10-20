package main;

import java.util.concurrent.ConcurrentHashMap;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
/**
 * <p>This is the <b>Input class</b> where all types of input, moving, clicking, and pressing are placed.
 * <p>This class makes all the controls needed for the game.
 * <br>It also makes the options, such as ones in the main menu in the game.
 * <br>This is done by creating event handlers that account all input methods.</p>
 * <br>January 22, 2019
 * <br>Input.java
 * @author Richard Pena
 * @author Alex Co
 * @author Mbarak Al-Amry
 */
public class Input {

	// Creates all the variables needed for input and event handlers, such as mouse and KeyCode
	public static double mouseX = -1;
	public static double mouseY = -1;

	static ConcurrentHashMap<String, KeyCode> keyMap = new ConcurrentHashMap<>();
	static ConcurrentHashMap<String, MouseButton> mouseMap = new ConcurrentHashMap<>();
	static boolean inWindow = false;

	static int resWidth = 1280;
	static int resHeight = 720;

	static boolean[] buttonPressed = new boolean[255]; 
	static boolean mouse1Pressed = false;
	static boolean mouse2Pressed = false;
	static boolean pause = false;
	//---------------------------------------
	// Creates menu and class selection options
	static int menuOption = 0;
	static int classOption = 0;
	//---------------------------------------
	/**
	 * This method initializes the keyMap and mouseMap, so that Key Events and Mouse Events may be read using the keyMap and mouseMap.
	 */
	public static void initializeInputMap() {
		keyMap.put("ESCAPE", KeyCode.ESCAPE);
		keyMap.put("UP", KeyCode.W);
		keyMap.put("LEFT", KeyCode.A);
		keyMap.put("DOWN", KeyCode.S);
		keyMap.put("RIGHT", KeyCode.D);
		keyMap.put("SPACE", KeyCode.SPACE);
		keyMap.put("RUN", KeyCode.SHIFT);
		mouseMap.put("MOUSE1", MouseButton.PRIMARY);
		mouseMap.put("MOUSE2", MouseButton.SECONDARY);
		keyMap.put("Q", KeyCode.Q);
		keyMap.put("E", KeyCode.E);
		keyMap.put("RELOAD", KeyCode.R);
		keyMap.put("ENTER", KeyCode.ENTER);
		keyMap.put("ESCAPE", KeyCode.ESCAPE);
	}

	//Creates main menu options used to make main menu
	static boolean mainMenu = true;
	static boolean arcadeMode = false;
	static boolean tutMode = false;
	static boolean exitGame = false;
	static boolean classSelect = false;
	//---------------------------------

	/**
	 * Creates event handler for key pressed.
	 */
	public static EventHandler<KeyEvent> keyPressedEventHandler = event -> Input.buttonPressed[event.getCode().ordinal()] = true;
	
	/**
	 * Creates event handler for key released.
	 */
	public static EventHandler<KeyEvent> keyReleasedEventHandler = event -> Input.buttonPressed[event.getCode().ordinal()] = false;
	
	/**
	 * Creates event handler for pause.
	 * <br>Meaning pause the game when escape (ESC) is pressed.
	 */
	public static EventHandler<KeyEvent> Pause = event -> {
		if (event.getCode() == keyMap.get("ESCAPE") && !mainMenu && !classSelect && !Arcade.gameOver) {
			pause = !pause;
		}
	};
	
	/**
	 * Creates event handler for mouse clicked.
	 * <br>Checks if it was clicked to choose an option and checks which option was chose.
	 */
	public static EventHandler<MouseEvent> mouseClickedEventHandler = event -> {
		Input.mouseX = event.getX();
		Input.mouseY = event.getY();
		// Checks which Main menu option was chose
		if (mainMenu && event.getButton() == Input.mouseMap.get("MOUSE1")) {
			if(GUI.arcade.contains(Input.mouseX, Input.mouseY) && mainMenu) {
				Sound.playSound(Sound.musicMap.get("MenuSound2"));
				mainMenu = false;
				classSelect = true;
				return;
			} else if(GUI.tutorial.contains(Input.mouseX, Input.mouseY) && mainMenu) {
				Sound.playSound(Sound.musicMap.get("MenuSound2"));
				mainMenu = false;
				tutMode = true;
				return;
			} else if(GUI.exit.contains(Input.mouseX, Input.mouseY) && mainMenu) {
				System.exit(0);
				mainMenu = false;
				exitGame = true;
				return;
			}
		}
		// Creates pausing for the game
		if (pause) {
			if (event.getButton() == Input.mouseMap.get("MOUSE1")) {
				if(GUI.pause.contains(Input.mouseX, Input.mouseY)) {
					Player.player = null;
					mainMenu = true;
					arcadeMode = false;
					tutMode = false;
					pause = false;
					Arcade.reset();
					return;
				}
			}
		}
		// Allows you to go to the next slide in Tutorial
		if (tutMode && !mainMenu) {
			if (GUI.tutorialArrow.contains(mouseX, mouseY)) {
				GUI.tutorialSlide++;
				Sound.playSound(Sound.musicMap.get("MenuSound2"));

			}
		}
		// Checks which Class selection option was chose
		if (classSelect && event.getButton() == Input.mouseMap.get("MOUSE1")) {

			if (GUI.archer.contains(mouseX, mouseY)) {
				Sound.playSound(Sound.musicMap.get("MenuSound2"));
				Image archerSpritesheet = new Image("files/archer_sprite.png");
				Player.player = new Player("ARCHER", archerSpritesheet, resWidth/2, resHeight/2, 4, 4);
				Map.mapList.get(Map.mapIndex).setX(Map.mapList.get(Map.mapIndex).getMapWidth()/2);
				Map.mapList.get(Map.mapIndex).setY(Map.mapList.get(Map.mapIndex).getMapHeight()/2);
				classSelect = false;
				arcadeMode = true;
			}
			if (GUI.mage.contains(mouseX, mouseY)) {
				Sound.playSound(Sound.musicMap.get("MenuSound2"));
				Image mageSpritesheet = new Image("files/mage_sprite.png"); //
				Player.player = new Player("MAGE", mageSpritesheet, resWidth/2, resHeight/2, 4, 4);
				Map.mapList.get(Map.mapIndex).setX(Map.mapList.get(Map.mapIndex).getMapWidth()/2);
				Map.mapList.get(Map.mapIndex).setY(Map.mapList.get(Map.mapIndex).getMapHeight()/2);
				classSelect = false;
				arcadeMode = true;
			}
			if (GUI.warrior.contains(mouseX, mouseY)) {
				Sound.playSound(Sound.musicMap.get("MenuSound2"));
				Image warriorSpritesheet = new Image("files/warrior_sprite.png");
				Player.player = new Player("WARRIOR", warriorSpritesheet, resWidth/2, resHeight/2, 4, 4);
				Map.mapList.get(Map.mapIndex).setX(Map.mapList.get(Map.mapIndex).getMapWidth()/2);
				Map.mapList.get(Map.mapIndex).setY(Map.mapList.get(Map.mapIndex).getMapHeight()/2);
				classSelect = false;
				arcadeMode = true;
			}
			if (GUI.returnButton.contains(mouseX, mouseY)) {
				Sound.playSound(Sound.musicMap.get("MenuSound2"));
				classSelect = false;
				mainMenu = true;
			}

		}
		if (Arcade.gameOver && Arcade.toMainMenu.contains(mouseX, mouseY)) {// Checks for GameOver
			Arcade.gameOver = false;
			Arcade.reset();
			mainMenu = true;
		}
	};
	
	/**
	 * Creates event handler for mouse pressed.
	 *<br> Checks which part of the mouse left or right was clicked.
	 */
	public static EventHandler<MouseEvent> mousePressedEventHandler = event -> {
		Input.mouseX = event.getX();
		Input.mouseY = event.getY();
		if (!mainMenu && event.getButton() == Input.mouseMap.get("MOUSE1"))
			Input.mouse1Pressed = true;
		if (!mainMenu && event.getButton() == Input.mouseMap.get("MOUSE2"))
			Input.mouse2Pressed = true;
	};
	
	/**
	 * Creates event handler for mouse released.
	 * <br>Checks which part of the mouse left or right was released.
	 */
	public static EventHandler<MouseEvent> mouseReleasedEventHandler = event -> {
		Input.mouseX = event.getX();
		Input.mouseY = event.getY();
		if (!mainMenu && event.getButton() == Input.mouseMap.get("MOUSE1"))
			Input.mouse1Pressed = false;
		if (!mainMenu && event.getButton() == Input.mouseMap.get("MOUSE2"))
			Input.mouse2Pressed = false;
	};
	
	/**
	 * Creates mouse moved event handler.
	 * <br>Checks where the mouse is moved and if it's to one of the menu or class selection options.
	 * <br>To then play sound after it.
	 */
	public static EventHandler<MouseEvent> mouseMovedEventHandler = event -> {
		Input.mouseX = event.getX();
		Input.mouseY = event.getY();

		// Checks where the mouse is moved, sets the options to there and plays sound
		if (GUI.arcade.contains(Input.mouseX, Input.mouseY) && mainMenu) {
			if (menuOption!=1) {
				Sound.playSound(Sound.musicMap.get("MenuSound1"));
			}
			menuOption = 1;
		} else if (GUI.tutorial.contains(Input.mouseX, Input.mouseY) && mainMenu) {
			if (menuOption!=2) {
				Sound.playSound(Sound.musicMap.get("MenuSound1"));
			}
			menuOption = 2;
		} else if (GUI.exit.contains(Input.mouseX, Input.mouseY) && mainMenu) {
			if (menuOption!=3) {
				Sound.playSound(Sound.musicMap.get("MenuSound1"));
			}
			menuOption = 3;
		}

		if (GUI.archer.contains(mouseX, mouseY) && classSelect) {
			if (classOption!=1) {
				Sound.playSound(Sound.musicMap.get("MenuSound1"));
			}
			classOption = 1;
		}
		else if (GUI.mage.contains(mouseX, mouseY) && classSelect) {
			if (classOption!=2) {
				Sound.playSound(Sound.musicMap.get("MenuSound1"));
			}
			classOption = 2;
		}
		else if (GUI.warrior.contains(mouseX, mouseY) && classSelect) {
			if (classOption!=3) {
				Sound.playSound(Sound.musicMap.get("MenuSound1"));
			}
			classOption = 3;
		}
		else if (GUI.returnButton.contains(mouseX, mouseY) && classSelect) {
			if (classOption!=4) {
				Sound.playSound(Sound.musicMap.get("MenuSound1"));
			}
			classOption = 4;
		}
		else {
			classOption = 0;
		}
	};
	
	/**
	 * Creates mouse drag event handler.
	 * <br>This method allows the player to hold down a mouse key and aim simultaneously.
	 */
	public static EventHandler<MouseEvent> mouseDraggedEventHandler = event -> {
		Input.mouseX = event.getX();
		Input.mouseY = event.getY();
	};
}