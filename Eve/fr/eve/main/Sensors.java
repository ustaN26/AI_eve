package fr.eve.main;

import java.util.ArrayList;
import java.util.List;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Sensors implements Constantes{
	private boolean touch;
	public boolean isTouch() { return touch;}
	
	private final List<Float> distBuffer = new ArrayList<>();
	public List<Float> getDistBuffer() { return distBuffer;}
	private boolean detect = false;
	
	public float getData() {
        SampleProvider sampleProvider=usSensor.getDistanceMode();
        float[] sample=new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0); 
        return sample[0];
    }
	
	public Sensors() {
		touchListener(false);
		Thread flagTask = new Thread() {
			public void run() {
				while(true) {
					if(isPressed()!=touch)
						touchListener(isPressed());
					if(detect)
						distBuffer.add(getData());
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

	public void resetDistBuffer() {
		distBuffer.clear();
	}

	public void setDetect(boolean b) {
		detect = b;
	}
}

