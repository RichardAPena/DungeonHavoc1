package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
/**
 * <p>This is the <b>Map</b> class
 * <p>This is where the <b>map</b> in the game is drawn and where all things are done on the map 
 * <br>such as <i>scrolling, transferring, and importing to the map</i>.
 * <br>January 22, 2019
 * <br>Map.java
 * @author Richard Pena
 * @author Alex Co
 * @author Mbarak Al-Amry
 */
public class Map {

	public static void main(String[] args) {
		Player player = new Player();
		player.setX(Input.resWidth/2+10);
		player.setY(Input.resHeight/2+10);
		Map map = new Map();
		map.setMapWidth(5000);
		map.setMapHeight(5000);
		map.setX(1000);
		map.setY(1000);
		map.updateScrollX(10, player);
		map.updateScrollY(10, player);
		System.out.println("x: " + player.getX());
		System.out.println("y: " + player.getY());
		System.out.println("mapx: " + map.getX());
		System.out.println("mapy: " + map.getY());
		System.out.println("scrollLeft " + map.scrollLeft);
		System.out.println("scrollRight " + map.scrollRight);
		System.out.println("scrollUp " + map.scrollUp);
		System.out.println("scrollDown " + map.scrollDown);
	}

	// Creates Map variables
	private String name; // Name
	private double x = 0; // x location
	private double y = 0; // y location
	private double mapWidth; // Map width
	private double mapHeight; // Map height
	private Image map; // Imsge of map

	// Scrolling variables
	private boolean scrollUp;
	private boolean scrollDown;
	private boolean scrollLeft;
	private boolean scrollRight;

	enum CollisionType {
		NONE, SOLID, WATER, MAP_TRANSFER
	}

	CollisionType[] tiles;

	static final int tileSize = 32;

	/**
	 * Gets the map width for the map.
	 * @return mapWidth
	 * Returns the mapWidth to create the map.
	 */
	public double getMapWidth() {
		return mapWidth;
	}
	
	/**
	 * Sets mapWidth for the map.
	 * @param mapWidth
	 * New map width.
	 */
	public void setMapWidth(double mapWidth) {
		this.mapWidth = mapWidth;
	}
	
	/**
	 * Gets the map height for the map.
	 * @return mapHeight
	 * Returns the mapHeight in order to create the map.
	 */
	public double getMapHeight() {
		return mapHeight;
	}
	
	/**
	 * Sets mapHeight for the map.
	 * @param mapHeight
	 * New map height.
	 */
	public void setMapHeight(double mapHeight) {
		this.mapHeight = mapHeight;
	}
	
	/**
	 * Gets the map image for the map.
	 * @return map
	 * Returns the map image.
	 */
	public Image getMap() {
		return map;
	}
	
	/**
	 * Sets map image for the map.
	 * @param map
	 * New map image.
	 */
	public void setMap(Image map) {
		this.map = map;
	}
	
	/**
	 * Sets x location of the map.
	 * @param x
	 * New x location.
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * Sets y location of the map.
	 * @param y
	 * New y location.
	 */
	public void setY(double y) {
		this.y = y;
	}
	// Creates list of maps
	static List<Map> mapList = Collections.synchronizedList(new ArrayList<>());
	static int mapIndex = 0;

	/**
	 * Default constructor.
	 */
	public Map() {
		super();
	}
	
	/**
	 * Creates constructor to set all map variables.
	 * @param image
	 * Image of the map.
	 * @param name
	 * name of the map.
	 */
	public Map(Image image, String name) {
		super();

		this.setName(name);
		this.map = image;
		this.x = 0;
		this.y = 0;
		this.mapWidth = image.getWidth();
		this.mapHeight = image.getHeight();
		this.tiles = new CollisionType[(int) (mapWidth / tileSize) * (int) (mapHeight / tileSize)];

		System.out.println(
				"# of tiles on map (tile size " + tileSize + "): " + (mapWidth / tileSize) * (mapHeight / tileSize));
		Arrays.fill(tiles, CollisionType.NONE);

		// x * Height + y
		// Height = mapHeight / tileSize
	}
	
	/**
	 * Creates constructor to set all map variables.
	 * @param image
	 * Image of the map.
	 * @param mapX
	 * X location of the map.
	 * @param mapY
	 * Y location of the map.
	 * @param name
	 * name of the map.
	 */
	public Map(Image image, double mapX, double mapY, String name) {
		super();
		map = image;
		this.x = mapX;
		this.y = mapY;
		mapWidth = image.getWidth();
		mapHeight = image.getHeight();

		this.tiles = new CollisionType[(int) (mapWidth / tileSize) * (int) (mapHeight / tileSize)];

		System.out.println(
				"# of tiles on map (tile size " + tileSize + "): " + (mapWidth / tileSize) * (mapHeight / tileSize));
		System.out.println("tiles array size: " + tiles.length);
		Arrays.fill(tiles, CollisionType.NONE);

		// y * Width + x
		// Width = mapWidth / tileSize
	}

	/**
	 * This method checks if an entity is colliding with the map.
	 * @param e
	 * The entity colliding with the map.
	 * @param gc
	 * The graphics context.
	 * @return
	 * The value of the boolean scrollDown.
	 */
	public boolean isEntityCollidingWithMap(Entity e, GraphicsContext gc) {
		return scrollDown;
	}
	
	//Rectangle tile = new Rectangle();
	/**
	 * This method checks if the entity is colliding with the map.
	 * @param e
	 * The entity colliding with the map.
	 * @param gc
	 * The graphics context.
	 * @param direction
	 * Shows direction the entity is colliding with the map.
	 * @param distance
	 * The distance needed to scroll.
	 * @return
	 * Returns true or false depending on if the entity is colliding with the map.
	 */
	public boolean isEntityCollidingWithMap(Entity e, GraphicsContext gc, String direction, double distance) {

		//	Rectangle tile = new Rectangle(e.getHitbox().getX(), e.getHitbox().getY(), tileSize, tileSize);
		Rectangle tile = new Rectangle(e.getHitbox().getX() + (tileSize / 2), e.getHitbox().getY() + (tileSize / 2), tileSize, tileSize);

		gc.fillRect(tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight());

		System.out.println("X:" + tile.getX() + " Y: " + tile.getY());
		//		System.out.println("Array index: " + (int) ((tile.getY() / tileSize) * (mapWidth / tileSize) + (tile.getX() / tileSize)) + " Type: " + tiles[(int) ((tile.getY() / tileSize) * (mapWidth / tileSize) + (tile.getX() / tileSize))]);

		System.out.println("Array index: " + (int) (((tile.getY() + this.y) / tileSize) * (mapWidth / tileSize) + ((tile.getX() + this.x) / tileSize)) + " Type: " + tiles[(int) (((tile.getY() + this.y) / tileSize) * (mapWidth / tileSize) + ((tile.getX() + this.x) / tileSize))]);

		switch (tiles[(int) (((tile.getY() + this.y) / tileSize) * (mapWidth / tileSize) + ((tile.getX() + this.x) / tileSize))]) {
			case SOLID:
			// To collide with a solid, set the Xspeed to -xSpeed or just don't update xpos/ypos if touching solid
			System.out.println("Colliding with solid....");

			break;
		case WATER:
			System.out.println("Colliding with water....");
			return true;
			default:
			break;
		}

		return false;
	}
	
	/**
	 * Draws <b>Map</b> used in the game.
	 * @param gc
	 * 			The graphics context.
	 */
	public void drawMap(GraphicsContext gc) {
		gc.drawImage(map, x, y, Input.resWidth, Input.resHeight, 0, 0, Input.resWidth, Input.resHeight);
	}
	
	/**
	 * Draws mini map used in the game.
	 * @param gc
	 * 			The graphics context.
	 * @param player
	 * Player object used in the game.
	 * @param map
	 * Map in the game.
	 */
	public void drawMiniMap(GraphicsContext gc, Player player, Map map) {
		// optional
	}
	// Creates grid variables
	private double xGrid = 0;
	private double yGrid = 0;

	/**
	 * Draws map grid.
	 * <br> Used to aim better through grid locations for the player.
	 * @param gc
	 * 			The graphics context.
	 */
	// Debugging purposes
	public void drawMapGrid(GraphicsContext gc) {
		gc.setFill(Color.ALICEBLUE);
		// Draw tile based on the coordinates of the mouse | the mouse doesn't snap properly after scrolling
		//		System.out.println("mx: " + x + "my: " + y );
		//		System.out.println("gx: " + xGrid + "gy: " + yGrid );
		//		xGrid is the same thing as negative map.x

		//gc.fillRect(Math.floor((Input.mouse) / tileSize) * tileSize, Math.floor((Input.mouseY) / tileSize) * tileSize, tileSize, tileSize);
		//gc.fillRect(Math.floor((Input.mouseX + (x / tileSize)) / tileSize) * tileSize + xGrid % tileSize, Math.floor((Input.mouseY + (y / tileSize)) / tileSize) * tileSize + yGrid % tileSize, tileSize, tileSize);

		//gc.fillRect(Math.floor(((Input.mouseX) + (x / tileSize)) / tileSize) * tileSize + xGrid % tileSize, Math.floor(((Input.mouseY) + (y / tileSize)) / tileSize) * tileSize + yGrid % tileSize, tileSize, tileSize);

		//		boolean halfX = x > mapWidth/2;
		//		boolean halfY = y > mapHeight/2;

		//                                                                                x%tileSize kind of works
		gc.fillRect(Math.floor(((Input.mouseX) + (x / tileSize)) / tileSize) * tileSize - (int)(x%mapWidth/tileSize),
				Math.floor(((Input.mouseY) + (y / tileSize)) / tileSize) * tileSize - (int)(y%mapHeight/tileSize), 
				tileSize, tileSize); // snaps perfectly UNTIL it reaches half of the map, then its one tile off
		//something something map width map height

		//tiles[936] = CollisionType.SOLID;
		tiles[937] = CollisionType.SOLID;
		//tiles[6] = CollisionType.SOLID;
		//tiles[890] = CollisionType.SOLID;

		// Draw collision type
		for (int i = 0; i < (mapWidth / tileSize); ++i) {
			for (int j = 0; j < (mapHeight / tileSize); ++j) {
				switch (tiles[(int) (j * (mapWidth / tileSize) + i)]) {
				case NONE:
					//gc.setStroke(Color.BLACK);
					gc.setFill(Color.SEAGREEN);
					break;
				case SOLID:
					//gc.setStroke(Color.RED);
					gc.setFill(Color.RED);
					break;
				case WATER:
					//gc.setStroke(Color.BLUE);
					gc.setFill(Color.BLUE);
					break;
				default:
					break;
				}
				// Fill in tile #
				//gc.strokeRect((i * tileSize+xGrid), j * tileSize+yGrid, tileSize, tileSize);

				gc.fillRect(i * tileSize+xGrid, j * tileSize+yGrid, tileSize, tileSize);

				//gc.setFont(new Font(10));
				gc.setFill(Color.BLACK);
				gc.fillText(String.valueOf((int)(j * (mapWidth / tileSize) + i)), (i * tileSize)+xGrid, (j * tileSize)+yGrid+tileSize);
			}
		}
	}
	
	/**
	 * This method <i>updates which way the player is scrolling for x coordinates</i> in the map.
	 * <br> This is done by checking if the player is moving left or right.
	 * <br> Then by using that sets the boolean scrollLeft or scrollRight, to true or false.
	 * @param scrollX
	 * The value used to determine if the player is scrolling right or left.
	 * @param player
	 * The player object used to determine which way to scroll. 
	 */
	private void updateScrollX(double scrollX, Player player) {
		scrollLeft = false;
		scrollRight = false;

		if (scrollX < 0) { // Player is moving left
			if (player.getX() <= Input.resWidth / 2 + scrollX && this.x <= mapWidth - Input.resWidth / 2
					&& this.x >= -scrollX) // left
			{ scrollLeft = true; }
		} else if (scrollX > 0) { // Player is moving right
			if (player.getX() >= Input.resWidth / 2 + scrollX
					&& this.x <= mapWidth - Input.resWidth - scrollX) // right
			{ scrollRight = true; }
		}
	}
	
	/**
	 * This method <i>updates which way the player is scrolling for y coordinates</i> in the map.
	 * <br> This is done by checking if the player is moving up or down.
	 * <br> Then by using that sets the boolean scrollUp or scrollDown, to true or false.
	 * @param scrollY
	 * The value used to determine if the player is scrolling up or down.
	 * @param player
	 * The player object used to determine which way to scroll.
	 */
	private void updateScrollY(double scrollY, Player player) {
		scrollUp = false;
		scrollDown = false;

		if (scrollY < 0) { // Player is moving up
			if (player.getY() <= Input.resHeight / 2 - scrollY 
					&& this.y >= -scrollY) // up
			{ scrollUp = true; }	
		} else if (scrollY > 0) { // Player is moving down
			if (player.getY() >= Input.resHeight / 2 + scrollY
					&& this.y <= mapHeight - Input.resHeight - scrollY) // down
			{ scrollDown = true; }		
		}
	}
	
	/**
	 * This method creates the <b>map scrolling</b> in the game.
	 * <br> It takes scrollX, scrollY and scroll directions, from the update methods.
	 * <br> to determine which way to scroll the grid/map and the player.
	 * <br> It also changes the attack coordinates so it is with the player.
	 * <br> and the enemy coordinates, so it follows the map.
	 * @param scrollX
	 * The variable used to determine if the map is being scrolled left or right.
	 * @param scrollY
	 * The variable used to determine if the map is being scrolled up or down.
	 * @param player
	 * The player object used to scroll the player with the map.
	 */
	public void mapScroll(double scrollX, double scrollY, Player player) {
		updateScrollX(scrollX, player);
		updateScrollY(scrollY, player);

		if (scrollY < 0 && scrollUp) {
			yGrid-=scrollY; // moves grid opposite way
			player.setY(player.getY() - scrollY); // moves the player the opposite way
			this.y += scrollY; // changes the Y of the map
			for (int i = 0; i < Enemy.enemyList.size(); ++i) {
				Enemy.enemyList.get(i).setY(Enemy.enemyList.get(i).getY() - scrollY);
			}
			for (int i = 0; i < Projectile.projectiles.size(); ++i) {
				if (i>=Projectile.projectiles.size()) break;
				try {
					Projectile.projectiles.get(i).setY(Projectile.projectiles.get(i).getY() - scrollY);
				} catch (Exception e) {}
			}
			for (int i=0; i<Combat.arcs.size(); ++i) {
				Combat.arcs.get(i).setCenterY(Combat.arcs.get(i).getCenterY() - scrollY);
			}
			for (int i=0; i<Combat.circles.size(); ++i) {
				Combat.circles.get(i).setCenterY(Combat.circles.get(i).getCenterY() - scrollY);
			}
			for (int i=0; i<Combat.periodicList.size(); ++i) {
				Combat.periodicList.get(i).setCenterY(Combat.periodicList.get(i).getCenterY() - scrollY);
			}
			for (int i=0; i<Combat.delayedList.size(); ++i) {
				Combat.delayedList.get(i).setCenterY(Combat.delayedList.get(i).getCenterY() - scrollY);
			}
		}
		if (scrollY > 0 && scrollDown) {
			yGrid-=scrollY; // moves grid opposite way
			player.setY(player.getY() - scrollY); // moves the player the opposite way
			this.y += scrollY; // changes the Y of the map
			for (int i = 0; i < Enemy.enemyList.size(); ++i) {
				Enemy.enemyList.get(i).setY(Enemy.enemyList.get(i).getY() - scrollY);
			}
			for (int i = 0; i < Projectile.projectiles.size(); ++i) {
				try {
					Projectile.projectiles.get(i).setY(Projectile.projectiles.get(i).getY() - scrollY); 
				} catch (Exception e) {}
			}
			for (int i=0; i<Combat.arcs.size(); ++i) {
				Combat.arcs.get(i).setCenterY(Combat.arcs.get(i).getCenterY()-scrollY);
			}
			for (int i=0; i<Combat.circles.size(); ++i) {
				Combat.circles.get(i).setCenterY(Combat.circles.get(i).getCenterY()-scrollY);
			}
			for (int i=0; i<Combat.periodicList.size(); ++i) {
				Combat.periodicList.get(i).setCenterY(Combat.periodicList.get(i).getCenterY()-scrollY);
			}
			for (int i=0; i<Combat.delayedList.size(); ++i) {
				Combat.delayedList.get(i).setCenterY(Combat.delayedList.get(i).getCenterY()-scrollY);
			}
		}
		if (scrollX < 0 && scrollLeft) {
			xGrid-=scrollX; // moves grid opposite way
			player.setX(player.getX() - scrollX); // moves the player the opposite way
			this.x += scrollX; // changes the X of the map
			for (int i = 0; i < Enemy.enemyList.size(); ++i) {
				Enemy.enemyList.get(i).setX(Enemy.enemyList.get(i).getX() - scrollX);
			}
			for (int i = 0; i < Projectile.projectiles.size(); ++i) {
				try {
					Projectile.projectiles.get(i).setX(Projectile.projectiles.get(i).getX() - scrollX);
				} catch (Exception e) {}
			}
			for (int i=0; i<Combat.arcs.size(); ++i) {
				Combat.arcs.get(i).setCenterX(Combat.arcs.get(i).getCenterX() - scrollX); //FIXME nullpointerexception
			}
			for (int i=0; i<Combat.circles.size(); ++i) {
				Combat.circles.get(i).setCenterX(Combat.circles.get(i).getCenterX() - scrollX);
			}
			for (int i=0; i<Combat.periodicList.size(); ++i) {
				Combat.periodicList.get(i).setCenterX(Combat.periodicList.get(i).getCenterX() - scrollX);
			}
			for (int i=0; i<Combat.delayedList.size(); ++i) {
				Combat.delayedList.get(i).setCenterX(Combat.delayedList.get(i).getCenterX() - scrollX);
			}
		}
		if (scrollX > 0 && scrollRight) {
			xGrid-=scrollX; // moves grid opposite way
			player.setX(player.getX() - scrollX); // moves the player the opposite way
			this.x += scrollX; // changes the X of the map
			for (int i = 0; i < Enemy.enemyList.size(); ++i) {
				Enemy.enemyList.get(i).setX(Enemy.enemyList.get(i).getX() - scrollX);
			}
			for (int i = 0; i < Projectile.projectiles.size(); ++i) {
				try {
					Projectile.projectiles.get(i).setX(Projectile.projectiles.get(i).getX() - scrollX);
				} catch (Exception e) {}
			}
			for (int i=0; i<Combat.arcs.size(); ++i) {
				Combat.arcs.get(i).setCenterX(Combat.arcs.get(i).getCenterX()-scrollX);
			}
			for (int i=0; i<Combat.circles.size(); ++i) {
				Combat.circles.get(i).setCenterX(Combat.circles.get(i).getCenterX()-scrollX);
			}
			for (int i=0; i<Combat.periodicList.size(); ++i) {
				Combat.periodicList.get(i).setCenterX(Combat.periodicList.get(i).getCenterX()-scrollX);
			}
			for (int i=0; i<Combat.delayedList.size(); ++i) {
				Combat.delayedList.get(i).setCenterX(Combat.delayedList.get(i).getCenterX()-scrollX);
			}
		}
		player.getHitbox().setX(player.getX());
		player.getHitbox().setY(player.getY());
	}

	/**
	 * Gets the x location.
	 * @return x
	 * Returns the x location.
	 */
	public double getX() {
		return this.x;
	}
	
	/**
	 * Sets x location.
	 * @param x
	 * New x location.
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Gets the y location.
	 * @return y
	 * Returns the y location.
	 */
	public double getY() {
		return this.y;
	}
	
	/**
	 * Sets y location.
	 * @param y
	 * New y location.
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Gets the name.
	 * @return name
	 * Returns the name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets name of the map.
	 * @param name
	 * New name.
	 */
	public void setName(String name) {
		this.name = name;
	}
}