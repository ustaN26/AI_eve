package fr.eve.main;

import java.util.ArrayList;
import java.util.List;

public class Sensors implements Constantes{
	private final List<Color> colorBuffer = new ArrayList<>();
	private static Color lastColor = Color.WHITE; //J'ai mis en static pour referencer direct
	public List<Color> getColorBuffer() { return colorBuffer;}
	
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
					if(Color.fromInt(colorSensor.getColorID()) != lastColor)
						colorListener(Color.fromInt(colorSensor.getColorID()));
					if((touchSensor.getCurrentMode()==1)!=touch)
						touchListener((touchSensor.getCurrentMode()==1));
		            sp.fetchSample(sample,0);
					distBuffer.add(sample[0]);
					try { Thread.sleep(1);
					} catch (InterruptedException ignored) {}
				}
			}
		};
		flagTask.start();
	}
	private void touchListener(boolean b) {
		touch = b;
	}
	private void colorListener(Color color) {
		lastColor = color;
		colorBuffer.add(color);
	}
	public static Color getLastColor() {
		return lastColor;
	}
	
	public enum Color {
	    BLUE(0), WHITE(1), BLACK(2), YELLOW(3), GREEN(4), RED(5), GREY(6);

	    private int value;

	    private Color(int value) {
	        this.value = value;
	    }

	    public int toInt() {
	        return value;
	    }

	    public static Color fromInt(int value) {
	        for (Color color : Color.values()) {
	            if (color.toInt() == value) {
	                return color;
	            }
	        }
	        return GREY;
	    }
	}
}

