package fr.eve.main;

import java.util.ArrayList;
import java.util.List;

public class Sensors implements Constantes{	
	private boolean touch = false;
	public boolean isTouch() { return touch;}
	
	private final List<Float> distBuffer = new ArrayList<>();
	public List<Float> getDistBuffer() { return distBuffer;}
	
	Thread flagTask, brainTask;
	public Sensors() {
		touchListener(false);
		Thread flagTask = new Thread() {
			public void run() {
				while(true) {
					if(isPressed()!=touch)
						touchListener(isPressed());
		            try { Thread.sleep(1);
					} catch (InterruptedException ignored) {}
				}
			}
		};
		flagTask.start();
	}
	protected boolean isPressed() {
		float[] sample = new float[1];
		touchSensor.getTouchMode().fetchSample(sample, 0);
		return sample[0] != 0.0f;
	}
	private void touchListener(boolean b) {
		touch = b;
	}
}

