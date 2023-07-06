package application;

import java.io.File;

import javafx.scene.media.AudioClip;

/**
 * Classe che gestisce audio
 */
public class AudioManager {
	private static String systemPath = "/home/a/eclipse-workspace/Jbomber/src/resources/audio/"; 
	private static String soundTrackP = "soundtrack.mp3", gameStartP = "game-start.mp3", boomP = "boom.mp3", selectP = "select.mp3", gameOverP = "game-over.wav", damageTakenP = "damage-taken.mp3";
	private static AudioClip soundTrack = new AudioClip(new File(systemPath+soundTrackP).toURI().toString()),
						gameOver = new AudioClip(new File(systemPath+gameOverP).toURI().toString()),
						damageTaken = new AudioClip(new File(systemPath+damageTakenP).toURI().toString()),
						gameStart = new AudioClip(new File(systemPath+gameStartP).toURI().toString()),
						boom = new AudioClip(new File(systemPath+boomP).toURI().toString()),
						select = new AudioClip(new File(systemPath+selectP).toURI().toString());

	public AudioManager() {

	}

	public void playSoundtrack(boolean play) {
		if (play) {
			soundTrack.play();
		}else{
			soundTrack.stop();
		}
	}

	public void playGameOver() {
		gameOver.play();
	}

	public void playDamageTaken() {
		damageTaken.play();
	}

	public void playGameStart() {
		gameStart.play();
	}

	public void playBoom() {
		boom.play();
	}

	public void playSelect() {
		select.play();
	}

}