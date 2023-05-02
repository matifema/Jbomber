package util.timer;

import com.google.common.eventbus.Subscribe;

import application.Level;

public class EventListener {
	
	private Level level;
	
	public EventListener() {
	}
    @Subscribe
    public void gameOverEvent(String event) {
    }
}