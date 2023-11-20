package fr.eve.main.test;

import fr.eve.main.Activators;
import fr.eve.main.Constantes;
import fr.eve.main.EVE;
import fr.eve.main.Sensors;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;

public class TestPince implements Constantes{
	private Activators activators;
	private Sensors sensors;
	
	public static void main(String[] args) {
		new TestPince().test(); 
	}
	public void test() {
		activators = new Activators();
		sensors = new Sensors();
		EVE.start();
		BrickFinder.getDefault().getKey(Button.ENTER.getName()).addKeyListener(new KeyListener() {
			@Override // boutton arret d'urgance
			public void keyReleased(Key k) {
				System.exit(0);
			}
			@Override
			public void keyPressed(Key k) {}
		});
		mP.rotate(-900);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mP.rotate(900);
		//activators.ouverturePince(false);
		//premierPalet(); 
	}

}
