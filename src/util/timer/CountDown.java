package util.timer;

import javafx.concurrent.Task;
import javafx.scene.text.Text;

public class CountDown extends Task<Void> {
	private final int seconds;
	private final Text text;
	private final String baseText;
	
	
	public CountDown(int sec, Text txt, String bt) {
		this.seconds = sec;
		this.text = txt;
		this.baseText = bt;
	}

	@Override
	protected Void call() throws Exception {
		for (int i = seconds; i >= 0; i--) {
            Thread.sleep(1000); // attesa di un secondo
            updateMessage(Integer.toString(i)); // aggiorna il messaggio con il tempo rimanente
        }
		return null;
	}
	
    @Override
    protected void updateMessage(String message) {
        super.updateMessage(message);
        text.setText(this.baseText + message); // imposta il testo della Label con il tempo rimanente
    }
}
