package fr.eve.main;

import java.util.ArrayList;
import java.util.List;

import fr.eve.main.Brain.Etats;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Sensors implements Constantes{
	private long clock;//2min30
	public void resetClock() { clock = System.currentTimeMillis()+150000; }
	private long pauseTime=0;
	public void addPauseTime(long l) {
		pauseTime+=l;
	}
	
	private boolean touch;
	public boolean isTouch() { return touch;}
	
	private final List<Float> distBuffer = new ArrayList<>();
	public List<Float> getDistBuffer() { return distBuffer;}
	private boolean detect = false;
	private float lastUS;
	private Thread sensorTask;
	public Thread getThread() { return sensorTask; }
	
	
	public float getData() {
        SampleProvider sampleProvider=usSensor.getDistanceMode();
        float[] sample=new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0); 
        return sample[0];
    }
	
	public Sensors(final Brain brain) {
		resetClock();
		touchListener(false);
		lastUS = getData();
		sensorTask = new Thread() {
			public void run() {
				while(true) {
					if(System.currentTimeMillis()-pauseTime>=clock)
						brain.endGame();
					if(isPressed()!=touch)
						touchListener(isPressed());
					float ir = getData();
					if(brain.getState()!=Brain.Etats.detectionDuPalet && lastUS-ir>0.1 && ir<0.20)
						brain.esquive();
					if(detect)
						distBuffer.add(ir);
					lastUS = ir;
				}
			}
		};
		sensorTask.start();
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

