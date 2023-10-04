package fr.eve.main;

import lejos.hardware.BrickFinder;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;
import lejos.utility.Matrix;

public class Activators implements Constantes {
	private MovePilot movePilot;
	private float speedG, speedD;
	private boolean dirG, dirD, synch;
	private Thread moveTask;
	
	public Activators() {		
		moveTask = new Thread() {
			private void init() {
		        mG.synchronizeWith(new EV3LargeRegulatedMotor[] { mD }); // synchronise le moteur 1 avec le moteur 2(qui est un element d'un tableau de moteur)
		        mG.startSynchronization();    //demarre la synchronisation (des commandes ) des moteurs synchronisés avec le moteur 1
			}
			public void run() {
				if(synch) {
					mG.setSpeed(speedG);
					mD.setSpeed(speedD);
				}
				try { Thread.sleep(1);
				} catch (InterruptedException ignored) {}
			};
		};
	}
	public void move(Boolean dir) {
		picoMove(dir, mD.getMaxSpeed());
	}
	public void rotate(int angle) {
		
	}
    public void rotationRapide(int angle) {
    	mG.startSynchronization();
        mG.rotate(angle);
        mD.rotate(-angle);
        mG.endSynchronization();
        mG.waitComplete();
        mD.waitComplete();
    }
	public void picoMove(boolean avancer, float vit) {
		dirD=dirG=avancer;
		speedD=speedG=vit;
	}
    public void Avancer(int temps) { //temps en millisecondes 
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

    public void Reculer(int temps) {
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

    public void RotationDroite(int angle) {
        mG.rotate(angle);
    }

    public  void RotationGauche(int angle) {
        mD.rotate(angle);
    }
/*  public  void AvancerDiago() {
        RotationDroite(45);
        Avancer(5000);
    }*/
}
