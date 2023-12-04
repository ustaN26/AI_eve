package fr.eve.main;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Activators implements Constantes {
	private Brain brain;
	private Thread pinceTask;
	private boolean etatPince = false, ordrePince = false;//true = open; false = close
	private int boussole=0;

	public Activators(Brain brain) {
		this.brain = brain;
		boolean ok = false;
		int essais = 0;
		while(!ok) {
			try {
				mG.synchronizeWith(new EV3LargeRegulatedMotor[] { mD }); // synchronise le moteur 1 avec le moteur 2(qui est un element d'un tableau de moteur)
				ok=true;
			}catch (Exception e) {
				essais++;
				if(essais>=5) {
					System.out.println("impossible de synchroniser les moteurs");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException ignored) {}
					System.exit(0);
				}
			}
		}
		mD.setAcceleration(720);
		mG.setAcceleration(720);
		mD.setSpeed(maxSpeed);
		mG.setSpeed(maxSpeed);
	}
	public void stop() {
		mG.startSynchronization();
		mG.stop(true);
		mD.stop(true);
		mG.endSynchronization();
	}
	public void move(boolean direction) {
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

	public boolean moveTo(boolean direction, int dist) {
		move(direction);
		long initDelay = System.currentTimeMillis();
		while(System.currentTimeMillis()-initDelay<dist*40){
			if(brain.getEsquiveInterrupt())
				return false;
		}// a 540 de vitesse le rapport distance et temps est : temps = distance*40
		stop();
		return true;
	}
	public void rotationRapide(int angle) {
		boussole=(boussole+angle)%360;
		mG.startSynchronization();
		if(angle>0){
			mD.rotate((int)(angle*(-2.16)));
			mG.rotate((int)(angle*2.16));
		}
		else{
			mD.rotate((int)(angle*(-2.16)));
			mG.rotate((int)(angle*2.16));
		}
		mG.endSynchronization();
		mD.waitComplete();
		mG.waitComplete();
		stop();
	}

	public void droitDevant() {
		rotationRapide(-boussole);
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
		pinceTask = new Thread("pinceTask") {
			public void run() {
				mP.setSpeed(mP.getMaxSpeed());
				if(etatPince!=ordrePince) {
					if (ordrePince) {
						mP.rotate(900);
						etatPince=true;
					}else{
						mP.rotate(-900);
						etatPince=false;
					} 
				}
			}
		};
		pinceTask.start();
	}
	
	public static void main(String[] args) {
		Activators a = new Activators(null);
		a.rotationRapide(-180);
	}
}
