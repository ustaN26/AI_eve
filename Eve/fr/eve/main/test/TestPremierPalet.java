package fr.eve.main.test;

import fr.eve.main.Activators;
import fr.eve.main.Constantes;
import fr.eve.main.EVE;
import fr.eve.main.Sensors;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.utility.Delay;

public class TestPremierPalet implements Constantes{
	private Activators activators;
	private Sensors sensors;
	public static void main(String[] args) {
		new TestPremierPalet().test(); 
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
        activators.ouverturePince(true);
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        activators.ouverturePince(false);
		//premierPalet();    	
	}
    private void avancerjusqua(boolean test) {
        while(test) {
        	try { Thread.sleep(1);
			} catch (InterruptedException ignored) {}
        }
    }
	private void premierPalet() {
		activators.synch(true);
		activators.move(true);
        activators.ouverturePince(true);
        avancerjusqua(sensors.isTouch());
        activators.ouverturePince(false);
        activators.rotate(45);
        avancerjusqua(activators.reached(20));
        activators.resetDist();
        activators.rotate(-45);
        avancerjusqua(activators.reached(20));
        activators.resetDist();
        activators.rotate(0);
        avancerjusqua(activators.reached(110));
        activators.resetDist();
        activators.stop();
        marquerPalet();
	}
	private void marquerPalet() {
        activators.ouverturePince(true);
        Delay.msDelay(1000);
        activators.move(false);
        while(activators.reached(10)) {
        	try { Thread.sleep(1);
			} catch (InterruptedException ignored) {}
        }
        activators.resetDist();
        activators.rotationRapide(180);
	}
}
