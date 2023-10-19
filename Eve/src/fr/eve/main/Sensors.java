package fr.eve.main;

import java.util.ArrayList;
import java.util.List;

public class Sensors implements Constantes{
	private final List<Integer> colorBuffer = new ArrayList<>();
	private int lastColor;
	public List<Integer> getColorBuffer() { return colorBuffer;}
	
	private boolean touch;
	public boolean isTouch() { return touch;}
	
	private final List<Float> distBuffer = new ArrayList<>();
	public List<Float> getDistBuffer() { return distBuffer;}
	
	Thread flagTask;
	public Sensors() {
		colorListener(colorSensor.getColorID());
		touchListener(false);
		Thread flagTask = new Thread() {
			public void run() {
				if(colorSensor.getColorID() != lastColor)
					colorListener(colorSensor.getColorID());
				if((touchSensor.getCurrentMode()==1)!=touch)
					touchListener((touchSensor.getCurrentMode()==1));
				//distBuffer.add(); TODO
				try { Thread.sleep(1);
				} catch (InterruptedException ignored) {}
			}
		};
		flagTask.start();
	}
	private void touchListener(boolean b) {
		touch = b;
	}
	private void colorListener(int color) {
		lastColor = color;
		colorBuffer.add(color);
	}
}
