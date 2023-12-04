package fr.eve.main.test;
import fr.eve.main.Activators;
import fr.eve.main.Constantes;
import fr.eve.main.Sensors;
import fr.eve.main.tester.DistanceTest;
import fr.eve.main.tester.Tester;
import fr.eve.main.tester.TouchTest;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.utility.Delay;

public class TestPremierPalet implements Constantes{
	private Activators activators;
	private Sensors sensors;
	public static void main(String[] args) {
		new TestPremierPalet().test(); 
	}
	public void test() {
		activators = new Activators();
		sensors = new Sensors(null);
		BrickFinder.getDefault().getKey(Button.ENTER.getName()).addKeyListener(new KeyListener() {
			@Override // boutton arret d'urgance
			public void keyReleased(Key k) {
				System.exit(0);
			}
			@Override
			public void keyPressed(Key k) {}
		});
		premierPalet();    	
	}
    public static void avancerjusqua(Tester t) {
        while(!t.test()) {
        	try { Thread.sleep(100);
			} catch (InterruptedException ignored) {}
        }
    }
	private void premierPalet() {
		activators.move(true);
	    activators.ouverturePince(true);
	    avancerjusqua(new TouchTest(sensors));
	    activators.ouverturePince(false);
	    activators.rotationRapide(45);
	    activators.moveTo(true,20);
	    activators.rotationRapide(-45);
	    activators.droitDevant();
	    activators.stop();
	}
	private void marquerPalet() {
        activators.ouverturePince(true);
        Delay.msDelay(1000);
        activators.move(false);
        activators.rotationRapide(180);
	}
}
