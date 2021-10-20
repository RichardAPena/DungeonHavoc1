package main;

import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * <p>This game is called <b><i>Dungeon Havoc</i></b>. It is an action RPG beat 'em up style game.
 * <p>Players can choose from two modes, <b>Arcade Mode</b> and <b>Tutorial Mode</b>.
 * <p>When choosing Arcade Mode, players will be prompted to choose one of three classes: <i>Archer, Mage and Warrior</i>.
 * <li><i>Archers</i> are well-rounded fighters who rain arrows at their enemies from a distance.
 * <li><i>Mages</i> have formidable damage and mobility, but have poor defenses.
 * <li><i>Warriors</i> have great defence and offence, but they need to get close to their foes in order to do damage.</p>
 * <br>January 22, 2019 
 * <br>DungeonHavoc.java
 * @author Richard Pena
 * @author Alex Co
 * @author Mbarak Al-Amry
 *
 */
public class Main extends Application {

	int pauseDuration = 1000/60;

	public static void main(String[] args) {		
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Makes the window unable to be resized
		primaryStage.setWidth(Input.resWidth+6);
		primaryStage.setHeight(Input.resHeight+29);
		primaryStage.setResizable(false);

		Scanner in = new Scanner(getClass().getResourceAsStream("/files/filelist.txt"));

		while(in.hasNextLine())
			Sound.musicList.add(getClass().getResource(in.nextLine()).toURI().toString());
		in.close();

		//Setting up music------------------------------------------
		Sound.initializeMusicMap();
		Sound.playMusicLoop(Sound.musicMap.get("FIELD"));
		//Setting up music------------------------------------------

		//KEYMAPS---------------------------------------------------
		Input.initializeInputMap();
		//KEYMAPS---------------------------------------------------

		primaryStage.setTitle("Dungeon Havoc");
		Canvas canvas = new Canvas(Input.resWidth, Input.resHeight);

		// This line makes sure that the canvas has the focus so that the key presses are registered
		canvas.setFocusTraversable(true);
		final GraphicsContext gc = canvas.getGraphicsContext2D();

		//Maps----------------------------------------------
		Image arcadeImage = new Image("files/arcadeMap.png");
		Map arcadeMap = new Map(arcadeImage, 0, 0, "outside");
		Map.mapList.add(arcadeMap);
		//Maps----------------------------------------------

		Thread game = new Thread(new Runnable() {
			/**
			 * Repaints the canvas periodically.
			 */
			@Override
			public void run() {

				while (true) {
					draw(gc);
					try { Thread.sleep(pauseDuration); } catch (InterruptedException e) {e.printStackTrace();}
				}
			}
		});

		// Updates the player periodically.
		Thread updatePlayer = new Thread(() -> {
			while (true) {
				if (!Input.pause) {
					if (!Input.mainMenu && Input.arcadeMode && !Arcade.gameOver)
						Player.player.update(Map.mapList.get(Map.mapIndex), gc);
				}
				try { Thread.sleep(pauseDuration); } catch (InterruptedException e) {e.printStackTrace();}
			}
		});

		// Updates every enemy periodically
		Thread updateEnemies = new Thread(() -> {
			while (true) {
				if(!Input.pause) {
					if (!Input.mainMenu && Input.arcadeMode && !Arcade.gameOver) {
						for (int i=0; i<Enemy.enemyList.size(); i++) {
							if (Enemy.enemyList.get(i).getHealth() <= 0) {
								Enemy.enemyList.remove(i);
								Arcade.kills++;
								if (Enemy.enemyList.size() == 0) break;
								i=0; //reset
							}
							Enemy.enemyList.get(i).update(Map.mapList.get(Map.mapIndex), gc);
						}
						Arcade.update(Map.mapList.get(Map.mapIndex));
					}
				}
				try { Thread.sleep(pauseDuration); } catch (InterruptedException e) {e.printStackTrace();}
			}
		});

		// Updates all projectiles periodically
		Thread updateProjectiles = new Thread (() -> {
			while (true) {
				if(!Input.pause) {
					if (!Input.mainMenu && Input.arcadeMode && !Arcade.gameOver)
						for (int i=0; i<Projectile.projectiles.size(); ++i) {
							if (Projectile.projectiles.get(i).isDead()) {
								Projectile.projectiles.remove(i);
								i=0;
							}
						}
				}
				try { Thread.sleep(pauseDuration); } catch (InterruptedException e) {e.printStackTrace();}
			}
		});

		Scene scene = new Scene(new Group(canvas));
		primaryStage.setScene(scene);

		//Sets Event handlers----------------------------
		canvas.addEventFilter(KeyEvent.KEY_PRESSED, Input.keyPressedEventHandler);
		canvas.addEventFilter(KeyEvent.KEY_RELEASED, Input.keyReleasedEventHandler);
		canvas.addEventFilter(KeyEvent.KEY_PRESSED, Input.Pause);
		canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, Input.mouseClickedEventHandler);
		canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, Input.mousePressedEventHandler);
		canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, Input.mouseReleasedEventHandler);
		canvas.addEventFilter(MouseEvent.MOUSE_MOVED, Input.mouseMovedEventHandler);
		canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, Input.mouseDraggedEventHandler);
		canvas.setOnMouseExited(e -> Input.inWindow = false);
		canvas.setOnMouseEntered(e -> Input.inWindow = true);
		//Event handlers----------------------------

		canvas.setCursor(Cursor.NONE); // Makes the cursor transparent so that a crosshair may be drawn on top of it

		//Textbox code--------------------------------
		Stop[] stops1 = new Stop[] { new Stop(0, new Color(135.0 / 255.0, 224.0 / 255.0, 253.0 / 255.0, 127.0 / 255.0)), new Stop(1, new Color(5.0 / 255.0, 171.0 / 255.0, 224.0 / 255.0, 127.0 / 255.0))};
		Stop[] stops2 = new Stop[] { new Stop(0, new Color(254.0 / 255.0, 252.0 / 255.0, 234.0 / 255.0, 127.0 / 255.0)), new Stop(1, new Color(241.0 / 255.0, 218.0 / 255.0, 54.0 / 255.0, 127.0 / 255.0))};
		Stop[] stops3 = new Stop[] { new Stop(0, new Color(146.0 / 255.0, 45.0 / 255.0, 12.0 / 255.0, 1.0)), new Stop(1, new Color(206.0 / 255.0, 47.0 / 255.0, 16.0 / 255.0, 1.0))};
		LinearGradient lg1 = new LinearGradient(0, 540, 0, 540+180, false, CycleMethod.NO_CYCLE, stops2);
		LinearGradient lg2 = new LinearGradient(0, 540, 0, 540+180, false, CycleMethod.NO_CYCLE, stops1);
		LinearGradient lg3 = new LinearGradient(620, 700, 660, 720, false, CycleMethod.NO_CYCLE, stops3);

		Image zucc = new Image(getClass().getResourceAsStream("/files/dummy.png"));
		Textbox.getInstance().setForegroundPaint(lg2);
		Textbox.getInstance().setBackgroundPaint(lg1);
		Textbox.getInstance().setArrowPaint(lg3);
		Textbox.getInstance().setFont(new Font(20));
		Textbox.getInstance().setTextPaint(Color.rgb(69, 6, 26, 1.0));
		Textbox.getInstance().setPortraitImage(zucc);
		Textbox.getInstance().setMessage("Welcome to the Arcade Mode! You can press ESC to pause at any time. [Press Enter to dismiss]");
		//Textbox.getInstance().setMessage("Ham followed now ecstatic use speaking exercise may repeated. Himself he evident oh greatly my on inhabit general concern. It earnest amongst he showing females so improve in picture. Mrs can hundred its greater account. Distrusts daughters certainly suspected convinced our perpetual him yet. Words did noise taken right state are since. Expenses as material breeding insisted building to in. Continual so distrusts pronounce by unwilling listening. Thing do taste on we manor. Him had wound use found hoped. Of distrusts immediate enjoyment curiosity do. Marianne numerous saw thoughts the humoured. Ferrars all spirits his imagine effects amongst neither. It bachelor cheerful of mistaken. Tore has sons put upon wife use bred seen. Its dissimilar invitation ten has discretion unreserved. Had you him humoured jointure ask expenses learning. Blush on in jokes sense do do. Brother hundred he assured reached on up no. On am nearer missed lovers. To it mother extent temper figure better. She literature discovered increasing how diminution understood. Though and highly the enough county for man. Of it up he still court alone widow seems. Suspected he remainder rapturous my sweetness. All vanity regard sudden nor simple can. World mrs and vexed china since after often. Behind sooner dining so window excuse he summer. Breakfast met certainty and fulfilled propriety led. Waited get either are wooded little her. Contrasted unreserved as mr particular collecting it everything as indulgence. Seems ask meant merry could put. Age old begin had boy noisy table front whole given. ");
		//Textbox code--------------------------------

		// Starts game
		game.start();		
		updatePlayer.start();
		updateEnemies.start();
		updateProjectiles.start();
		primaryStage.show();
	}

	/**
	 * Draws/calls classes and methods needed to draw everything for the game.
	 * <br>Such as:
	 * <br>Main Menu.
	 * <br>Class Selection.
	 * <br>Map.
	 * <br>Arcade Mode.
	 * <br>Tutorial Mode.
	 * <br>GUI.
	 * <br>TextBoxes.
	 * <br>Enemies.
	 * <br>Players.
	 * <br>Attacks.
	 * <br>Game Over.
	 * @param gc
	 * 			The graphics context.
	 */
	public void draw(GraphicsContext gc) {
		Platform.runLater(() -> {
			// Draws main menu
			if (Input.mainMenu) {
				GUI.drawMenu(gc);
			}
			if (Input.classSelect) { // Draws class selection
				GUI.drawClassSelection(gc);
			}
			if (Input.tutMode) {
				GUI.drawTutorial(gc);
			}
			if (Arcade.gameOver) {
				Arcade.drawGameOverScreen(gc);
			}
			if (!Input.mainMenu && Input.arcadeMode) { // Checks mode

				gc.setFill(Color.AQUA); // Draws background
				gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

				Map.mapList.get(Map.mapIndex).drawMap(gc); // Draw map
				//Map.mapList.get(Map.mapIndex).drawMapGrid(gc); // Draw grid
				Player.player.draw(gc); // Draw player
				for (int i=0; i<Enemy.enemyList.size(); ++i) { // Draws all enemies
					Enemy.enemyList.get(i).draw(gc);
				}

				//Map.mapList.get(Map.mapIndex).isEntityCollidingWithMap(player, gc, player.getDirection(), player.getXSpeed()); // Check for collisions

				for (int i=0; i<Projectile.projectiles.size(); ++i) { // Draws all projectiles
					Projectile.projectiles.get(i).draw(gc);
				}
				Combat.drawShapes(gc);
				GUI.drawHUD(gc, Player.player);

				if (Textbox.printTextbox) { // Draws textbox
					Textbox.getInstance().drawTextBox(gc, 1280, 180);
				}
			}
			if (Input.pause) { // draws pause screen if the user pauses
				GUI.drawPauseScreen(gc);
			}
			if (Input.inWindow) GUI.drawCrosshair(gc); // Draws cross hair

		});
	}

	/**
	 * Called whenever the user <i>exits</i> the game, either through pressing the 'X' window button or pressing ALT+F4.
	 * <br>This is required otherwise the program will keep running in the background, and it will keep draining resources and playing sounds.
	 */
//	@Override
//	public void stop() {
//		System.exit(0);
//	}
}