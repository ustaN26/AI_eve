package fr.eve.main;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class Activators implements Constantes {
	private Thread pinceTask;
	private boolean etatPince = false, ordrePince = false;//true = open; false = close
	private int boussole=0;

	public Activators() {
		boolean ok = true;
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
	}
	public void stop() {
		mG.startSynchronization();
		mG.stop();
		mD.stop();
		mG.endSynchronization();
		//mG.close();
		//mD.close(); //TODO c'etait utilisé dans avancer(temps)
	}
	public void move(boolean direction) {
		mD.setAcceleration(720);//TODO
		mG.setAcceleration(720);//TODO
		mD.setSpeed(720);
		mG.setSpeed(720);
		mG.startSynchronization();
		if(direction) {
			mD.forward();
			mG.forward();
		}else {
			mD.backward();
			mG.backward();
		}
		mG.endSynchronization();
	}

	public void rotationRapide(int angle) {
		boussole=(boussole+angle)%360;
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

	private int distanceParcourue = 0;
	public boolean reached(int dist) {
		calcDistParcourue();
		return dist<=distanceParcourue;
	}
	private void calcDistParcourue() {
		int g = Math.abs(mG.getTachoCount());
		int d = Math.abs(mD.getTachoCount()); 
		distanceParcourue+=(((Math.PI*57/360)*(g+d)/2));// (2*PI*r/360) *degres parcourus
		mG.resetTachoCount();
		mD.resetTachoCount();
	}
	public void resetDist() {
		distanceParcourue=0;
	}
}
