package application;

import java.io.File;

import javafx.scene.media.AudioClip;

public class AudioManager{
	private String soundTrackPath = "/home/a/eclipse-workspace/Jbomber/src/resources/audio/soundtrack.mp3",
				   gameStartPath = "/home/a/eclipse-workspace/Jbomber/src/resources/audio/game-start.mp3",
				   boomPath = "/home/a/eclipse-workspace/Jbomber/src/resources/audio/boom.mp3",
				   menuPath = "/home/a/eclipse-workspace/Jbomber/src/resources/audio/menu.mp3",
				   selectPath = "/home/a/eclipse-workspace/Jbomber/src/resources/audio/select.mp3",
				   gameOverPath = "/home/a/eclipse-workspace/Jbomber/src/resources/audio/game-over.wav",
				   damageTakenPath = "/home/a/eclipse-workspace/Jbomber/src/resources/audio/damage-taken.mp3";
	
	public AudioManager() {}
	
	
	public void playSoundtrack(boolean playing) {
		AudioClip soundTrack = new AudioClip(new File(soundTrackPath).toURI().toString());
		soundTrack.play();
		
		if(!playing) {
			soundTrack.stop();
		}
	}
	
	public void playGameOver() {
		AudioClip gameOver = new AudioClip(new File(gameOverPath).toURI().toString());
		gameOver.play();
	}
	
	public void playDamageTaken() {
		AudioClip damageTaken = new AudioClip(new File(damageTakenPath).toURI().toString());
		damageTaken.play();
	}
	
	public void playGameStart() {
		AudioClip gameStart = new AudioClip(new File(gameStartPath).toURI().toString());
		gameStart.play();
	}
	
	public void playBoom() {
		AudioClip boom = new AudioClip(new File(boomPath).toURI().toString());
		boom.play();
	}
	
	public void playMenu() {
		AudioClip menu = new AudioClip(new File(menuPath).toURI().toString());
		menu.play();
	}
	
	public void playSelect() {
		AudioClip select = new AudioClip(new File(selectPath).toURI().toString());
		select.play();
	}

}