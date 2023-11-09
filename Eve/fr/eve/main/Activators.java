package fr.eve.main;

import fr.eve.main.Sensors.Color;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.utility.Delay;

public class Activators implements Constantes {
	private float speedG=0, speedD=0;
	private boolean dirG=true, dirD=true, synch=true;
	private Thread moveTask, pinceTask;
	private boolean etatPince = false,ordrePince = false;//true = open; false = close
	private int lastTachoG=0, lastTachoD=0;
	private int boussole=0;
	
	public Activators() {
	    mG.synchronizeWith(new EV3LargeRegulatedMotor[] { mD }); // synchronise le moteur 1 avec le moteur 2(qui est un element d'un tableau de moteur)
        moveTask = new Thread("moveTask") {
			public void run() {
				while(true) {
					if(synch) {
						mG.startSynchronization();    //demarre la synchronisation (des commandes ) des moteurs synchronisés avec le moteur 1
				        mG.setSpeed(speedG);
						mD.setSpeed(speedD);
						if(dirG) mG.forward();
						else mG.backward();
						if(dirD) mD.forward();
						else mD.backward();
						mG.endSynchronization();
						calcDistParcourue();
					}
					try { Thread.sleep(1);
					} catch (InterruptedException ignored) {}
				}
			};
		};
		pinceTask = new Thread("pinceTask") {
			public void run() {
				while(true) {
					if(etatPince!=ordrePince) {
						if (ordrePince) {
							mP.rotate(-900);
							etatPince=true;
						}else{
							mP.rotate(-900);
							etatPince=false;
						} 
					}
					try { Thread.sleep(1);
					} catch (InterruptedException ignored) {}
				}
			};
		};
		moveTask.start();
		pinceTask.start();
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
	
    public void rotationRapide(int angle) {//TODO vrai angle
    	boussole=angle;
    	mG.setAcceleration(720);
		mD.setAcceleration(720);
    	mG.startSynchronization();
    	mG.rotate((int)(angle*2.16));
		mD.rotate((int)(-angle*2.16));
        mG.endSynchronization();
        mG.waitComplete();
        mD.waitComplete();
    }
    
    public void droitDevant() {
    	rotationRapide(-boussole);
    }
    
    
	public void picoMove(boolean avancer, float vit) {
		dirD=dirG=avancer;
		speedD=speedG=vit;
	}
	/*
	 * Manipule l'ouverture des pinces, ne permet pas de faire la même action deux d'affiler
	 * PinceOuverte(True) : Ouvre les pinces
	 * PinceOuverte(False) : Ferme les pinces 
	 * Aide au valeur du moter : 
	 * Valeur dans les positifs ça va forward => Fermé vers ouverte
	 * Valeur dans les négatifs ca va backward => Ouverte vers fermé
	 */
	public void ouverturePince(boolean b) {
		ordrePince = b;
	}
    public void Avancer(int temps) {//temps en millisecondes 
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

    public void avancerJusquaCouleur(Color c) {
    	//Faire pour les autres trucs qu'une couleur (quand on en aura besoin on rajoute)
    	while (Sensors.getLastColor()!=c) {
    		Avancer(100000);
    	}
    }
    
	private int distanceParcourue = 0;
	public boolean reached(int dist) {
		return dist>=distanceParcourue;
	}
	private void calcDistParcourue() {
	    int g = mG.getTachoCount()-lastTachoG;
	    int d = mD.getTachoCount()-lastTachoD;
	    distanceParcourue+=(Math.PI*6/360)*(g+d)/2;// (2*PI*r/360) *degres parcourus 
	}
	public void resetDist() {
		distanceParcourue=0;
	}
}
