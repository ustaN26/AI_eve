package fr.eve.main.test;

import fr.eve.main.BAU;
import fr.eve.main.Constantes;
import lejos.robotics.SampleProvider;

public class TestUS implements Constantes{
	public static void main(String[] args) {
		TestUS tus = new TestUS();
		BAU.bau();
		while(true) {
			System.out.println(tus.getData());
			try {
				Thread.sleep(200);
			} catch (InterruptedException ignored) {}
		}
	}
	public static float getData() {
        SampleProvider sampleProvider=usSensor.getDistanceMode();
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0); 
        return sample[0];
    }
}
