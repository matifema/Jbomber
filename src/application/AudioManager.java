package application;

import java.io.File;

import javafx.scene.media.AudioClip;

/**
 * Audio Managment class.
 */
public class AudioManager {
	private static String systemPath = "/home/a/eclipse-workspace/Jbomber/src/resources/audio/"; 
	private static String soundTrackP = "soundtrack.mp3", gameStartP = "game-start.mp3", boomP = "boom.mp3", selectP = "select.mp3", gameOverP = "game-over.wav", damageTakenP = "damage-taken.mp3";
	
	private static AudioClip	soundTrack 	= new AudioClip(new File(systemPath+soundTrackP).toURI().toString()),
								gameOver 	= new AudioClip(new File(systemPath+gameOverP).toURI().toString()),
								damageTaken = new AudioClip(new File(systemPath+damageTakenP).toURI().toString()),
								gameStart 	= new AudioClip(new File(systemPath+gameStartP).toURI().toString()),
								boom 		= new AudioClip(new File(systemPath+boomP).toURI().toString()),
								select 		= new AudioClip(new File(systemPath+selectP).toURI().toString());

	public AudioManager() {}

	/**
	 * Plays soundtrack, 
	 * boolean play == true -> start
	 * 		   play == false -> stop
	 * @param play
	 */
	public void playSoundtrack(boolean play) {
		if (play) {
			soundTrack.play();
		}else{
			soundTrack.stop();
		}
	}

	/**
	 * Plays Game Over sound
	 */
	public void playGameOver() {
		gameOver.play();
	}

	/**
	 * Plays damage taken sound
	 */
	public void playDamageTaken() {
		damageTaken.play();
	}

	/**
	 * Plays game start sound
	 */
	public void playGameStart() {
		gameStart.play();
	}

	/**
	 * Plays explosion sound
	 */
	public void playBoom() {
		boom.play();
	}

	/**
	 * Plays select sound
	 */
	public void playSelect() {
		select.play();
	}

}