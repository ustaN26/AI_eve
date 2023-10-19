package fr.eve.main;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.utility.Delay;

public class Activators implements Constantes {
	private float speedG, speedD;
	private boolean dirG, dirD, synch;
	private Thread moveTask;
	
	public Activators() {
	    mG.synchronizeWith(new EV3LargeRegulatedMotor[] { mD }); // synchronise le moteur 1 avec le moteur 2(qui est un element d'un tableau de moteur)
        moveTask = new Thread("moveTask") {
			public void run() {
				if(synch) {
					mG.startSynchronization();    //demarre la synchronisation (des commandes ) des moteurs synchronisés avec le moteur 1
			        mG.setSpeed(speedG);
					if(dirG) mG.forward();
					else mG.backward();
					mD.setSpeed(speedD);
					if(dirD) mD.forward();
					else mD.backward();
					mG.endSynchronization();
				}
				try { Thread.sleep(1);
				} catch (InterruptedException ignored) {}
			};
		};
		moveTask.start();
	}
	public void synch(boolean s) {
		this.synch=s;
	}
	public void stop() {
		picoMove(dirD, 0);
	}
	public void move(boolean dir) {
		picoMove(dir, maxSpeed);
	}
	public void rotate(float angle) {
		if(angle==0)
			speedD=speedG=maxSpeed;
		else if(angle>0) {
			speedD = maxSpeed * Math.abs(angle)*(3.6f*5.7f/14)/100;
			speedG = maxSpeed;
		}
		else{
			speedG = maxSpeed * Math.abs(angle)*(3.6f*5.7f/14)/100;
			speedD = maxSpeed;
		}
			
	}
    public void rotationRapide(int angle) {//TODO vrai angle
    	mG.startSynchronization();
        mG.rotate(angle);
        mD.rotate(-angle);
        mG.waitComplete();
        mD.waitComplete();
        mG.endSynchronization();
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
