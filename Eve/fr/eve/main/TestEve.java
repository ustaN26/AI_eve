package fr.eve.main;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.utility.Delay;


public class TestEve implements Constantes {

	public void rotationRapide(int angle) {//TODO vrai angle
		mG.setAcceleration(720);
		mD.setAcceleration(720);
		mG.synchronizeWith(new EV3LargeRegulatedMotor[] { mD }); // synchronise le moteur 1 avec le moteur 2(qui est un element d'un tableau de moteur)
        mG.startSynchronization();
		mG.rotate((int)(angle*2.16));
		mD.rotate((int)(-angle*2.16));
		mG.endSynchronization();
		mG.waitComplete();
		mD.waitComplete();

	}

	public static void main(String[] args) {
		new TestEve().rotationRapide(90); 


	}


}
