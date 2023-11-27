package fr.eve.mainold;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class Deplacement {
	private static final EV3LargeRegulatedMotor mG = new EV3LargeRegulatedMotor(MotorPort.C);
	private static final EV3LargeRegulatedMotor mD = new EV3LargeRegulatedMotor(MotorPort.B);

	public static void Avancer(int temps) {
		mG.synchronizeWith(new EV3LargeRegulatedMotor[] { mD });

		mG.startSynchronization();
		mG.forward();
		mD.forward();
		mG.endSynchronization();

		Delay.msDelay(temps);

		mG.startSynchronization();
		mG.close();
		mD.close();
		mG.endSynchronization();
	}

	public static void Reculer(int temps) {
		mG.synchronizeWith(new EV3LargeRegulatedMotor[] { mD });
		mG.startSynchronization();
		mG.backward();
		mD.backward();
		mG.endSynchronization();
		Delay.msDelay(temps);
		mG.startSynchronization();
		mG.close();
		mD.close();
		mG.endSynchronization();
	}

	public static void RotationDroite(int angle) {
		mG.rotate(angle);
		;
	}

	public static void RotationGauche(int angle) {
		mD.rotate(angle);
		;
	}

	public static void AvancerDiago() {
		RotationDroite(45);
		Avancer(5000);
	}

	public static void rotationRapide(int angle) {
		mG.startSynchronization();
		mG.rotate((int) (angle*2.16));
		mD.rotate((int) (-angle*2.16));
		mG.endSynchronization();
		mG.waitComplete();
		mD.waitComplete();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		rotationRapide(800);
	}

}
