package fr.eve.main.test;

import java.io.FileOutputStream;

import fr.eve.main.Constantes;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class TestRotation implements Constantes {
	public static FileOutputStream file;
	//private Sensors sensors;
	public TestRotation() {
		mG.synchronizeWith(new EV3LargeRegulatedMotor[] { mD }); // synchronise le moteur 1 avec le moteur 2(qui est un element d'un tableau de moteur)
	}
	public static void main(String[] args) {
		TestRotation tr = new TestRotation();
		tr.rotationRapide(90);
		try {
			Thread.sleep(200);
		} catch (InterruptedException ignored) {}
		tr.rotationRapide(-90);
		try {
			Thread.sleep(200);
		} catch (InterruptedException ignored) {}
		tr.rotationRapide(-400);
		try {
			Thread.sleep(200);
		} catch (InterruptedException ignored) {}
		tr.rotationRapide(20);
		try {
			Thread.sleep(200);
		} catch (InterruptedException ignored) {}
		tr.rotationRapide(45);
		
	}
	public static void rotationRapide(int angle) {
		mG.startSynchronization();
		mD.rotate((int)(angle*(-2.16)));
		mG.rotate((int)(angle*2.16));
		mG.endSynchronization();
		mD.waitComplete();
		mG.waitComplete();
		mG.startSynchronization();
		mG.stop(true);
		mD.stop(true);
		mG.endSynchronization();
	}
}
