package fr.eve.main.test;
import fr.eve.main.Activators;
import fr.eve.main.BAU;
import fr.eve.main.Constantes;
import fr.eve.main.Sensors;
import fr.eve.main.Brain.Etats;
import fr.eve.main.tester.Tester;
import fr.eve.main.tester.TouchTest;
import fr.eve.main.tester.USTest;
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
		activators = new Activators(null);
		sensors = new Sensors(null);
		BAU.bau();
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
	    avancerjusqua(new USTest(20, sensors));//(0.18m))
		activators.stop();
		activators.ouverturePince(true);
		if(!activators.moveTo(false,20))
			return;
		activators.ouverturePince(false);
		System.out.println("j'ai finie");
	}
}
