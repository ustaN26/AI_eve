package fr.eve.main;

import java.util.ArrayList;
import java.util.List;

import fr.eve.main.Brain.Etats;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * Classe capteurs 
 * gere le minuteur et les capteurs
 */
public class Sensors implements Constantes{

	/** le minuteur : 2min30 */
	private long clock;

	/**
	 * re/initialise le minuteur à 2min30
	 */
	public void resetClock() { clock = System.currentTimeMillis()+150000; }

	/**
	 * dernier etat connu du capteur de touche
	 * @getter isTouch()
	 * @setter touchListener() 
	 * @return true, si touche
	 */
	private boolean touch;
	public boolean isTouch() { return touch; }
	private void touchListener(boolean b) {	touch = b; }

	/** buffer des valeur de distances lues par le capteur. 
	 * @getter getDistBuffer()
	 * @reset resetDistBuffer()
	 * */
	private final List<Float> distBuffer = new ArrayList<>();
	public List<Float> getDistBuffer() { return distBuffer;}
	public void resetDistBuffer() {	distBuffer.clear(); }
	/** etat de detection continue
	 * @setter setDetect()
	 * */
	private boolean detect = false;
	public void setDetect(boolean b) { detect = b; }
	
	/** derniere valeur detecté */
	private float lastUS;

	/**
	 * thread de la classe en boucle.
	 * @getter getThread()
	 */
	private Thread sensorTask;
	public Thread getThread() { return sensorTask; }

	/**
	 * detection via ultrason
	 * @return la distance lue
	 */
	public float getData() {
        SampleProvider sampleProvider=usSensor.getDistanceMode();
        float[] sample = new float[sampleProvider.sampleSize()];
        sampleProvider.fetchSample(sample, 0); 
        return sample[0];
    }
	
	/**
	 * constructeur
	 * @param brain le programme principale
	 * 
	 * initialise le minuteur et les etats des capteurs
	 * demarre le thread
	 */
	public Sensors(final Brain brain) {
		resetClock();
		touchListener(false);
		lastUS = getData();
		sensorTask = new Thread() {
			public void run() {
				while(true) {
					if(System.currentTimeMillis()>=clock)
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
	
	/**
	 * check l'etat actuel du capteur
	 * @return true, le capteur est actuellement presse
	 */
	protected boolean isPressed() {
		float[] sample = new float[1];
		touchSensor.getTouchMode().fetchSample(sample, 0);
		return sample[0] != 0.0f;
	}	
}

