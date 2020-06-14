package me.cepera.snake.sounds;

import java.util.HashMap;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundSystem {

	private static HashMap<String, Media> mediaCache = new HashMap<>();
	
	public static MediaPlayer loadSoundPlayer(String name) {
		Media sound = mediaCache.get(name);
		if(sound == null) {
			sound = new Media(SoundSystem.class.getResource("/assets/sounds/"+name).toExternalForm());
			mediaCache.put(name, sound);
		}
		return new MediaPlayer(sound);
	}
	
	public void clearCache() {
		mediaCache.clear();
	}
	
}
