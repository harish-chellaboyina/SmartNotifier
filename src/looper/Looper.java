package looper;

import java.util.Timer;
import java.util.TimerTask;

import interfaces.TaskRunner;

public class Looper {

	public void startLoop(int interval, TaskRunner tr) {
		Timer t = new Timer();
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				tr.execute();
			}
		}, 60000 * interval, 60000 * interval);
	}

}
