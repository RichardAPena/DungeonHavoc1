package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javafx.scene.media.*;
import javafx.scene.media.MediaPlayer;
/**
 * <p>This is the <b>Sound</b> class.
 * <p>This class is used for <b>sound/music</b> in the game.
 * <br> This is done by creating a music and sound list.
 * <br> and placing the music and sounds in different parts of the game.
 * <br>such as sounds for attacks, music for main menu, music for game over and sounds when fighting.</p>
 * <br>January 22, 2019
 * <br>Sound.java
 * @author Richard Pena
 * @author Alex Co
 * @author Mbarak Al-Amry
 *
 */
public class Sound {

	// Creates list of music for the game
	static ArrayList<String> musicList = new ArrayList<>();
	static ConcurrentHashMap<String, Media> musicMap = new ConcurrentHashMap<>();

	static MediaPlayer music; // creates MediaPlayer for sounds
	// Creates a list of sounds for the game
	static List<Media> soundList = Collections.synchronizedList(new ArrayList<>());

	static int soundsPlayed = 0; // keeps track of sounds played
	static MediaPlayer soundPlayer; // creates MediaPlayer for sounds
	static int pauseDuration = 1000/60; // pause time after sound being played

	// Creates a thread to play the sounds in the game
	static Thread sounds = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				if (soundList.size() > soundsPlayed) {
					soundPlayer = new MediaPlayer(soundList.get(soundsPlayed));
					soundPlayer.play();
					soundsPlayed++;
				}
				try { Thread.sleep(pauseDuration); } catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	});
	// Sets music to different parts of the game
	/**
	 * This method initializes the music used in the map.
	 * <br>Different music is used for different parts.
	 */
	public static void initializeMusicMap() {
		Media title = new Media(Sound.musicList.get(0));
		Sound.musicMap.put("TITLE", title);
		Media field = new Media(Sound.musicList.get(1));
		Sound.musicMap.put("FIELD", field);
		Media boss = new Media(Sound.musicList.get(2));
		Sound.musicMap.put("BOSS", boss);
		Media menuSound1 = new Media(Sound.musicList.get(3));
		Sound.musicMap.put("MenuSound1", menuSound1);
		Media menuSound2 = new Media(Sound.musicList.get(4));
		Sound.musicMap.put("MenuSound2", menuSound2);
		sounds.start();
	}
	/**
	 * This method plays sound 
	 * @param sound
	 * Sound that is being played
	 */
	public static void playSound(Media sound) {
		soundList.add(sound);
	}
	/**
	 * This method plays music in a loop
	 * @param sound
	 * Music being played
	 */
	public static void playMusicLoop(Media sound) {
		music = new MediaPlayer(sound);
		music.setCycleCount(MediaPlayer.INDEFINITE);
		music.play();				
	}

}
