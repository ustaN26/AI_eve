package fr.eve.main.test;

import java.io.FileOutputStream;

import fr.eve.main.Constantes;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class TestDeplacement implements Constantes{
	public static FileOutputStream file;
	//private Sensors sensors;
	public TestDeplacement() {
		mG.synchronizeWith(new EV3LargeRegulatedMotor[] { mD }); // synchronise le moteur 1 avec le moteur 2(qui est un element d'un tableau de moteur)
	}
	public static void main(String[] args) {
		TestDeplacement td = new TestDeplacement();
		td.moveTo(50,true);
		td.moveTo(30,false);
		/*td.move();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException ignored) {}
		td.stop();*/
	}
	private void moveTo(int dist,boolean direction) {
		move(direction);
		long initDelay = System.currentTimeMillis();
		while(System.currentTimeMillis()-initDelay<dist*40){}
		stop();
	}
	private void move(boolean direction) {
		mG.startSynchronization();
		if(direction) {
			mD.forward();
			mG.forward();
		}else{
			mD.backward();
			mG.backward();
		}
		mG.endSynchronization();
	}
	private void stop() {
		mG.startSynchronization();
		mG.stop(true);
		mD.stop(true);
		mG.endSynchronization();
	}
}
