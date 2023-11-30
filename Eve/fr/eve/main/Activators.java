package fr.eve.main;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.utility.Delay;

public class Activators implements Constantes {
	private Thread pinceTask;
	private boolean etatPince = false, ordrePince = false;//true = open; false = close
	private int boussole=0;

	public Activators() {
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
	}
	public void stop() {
		mG.startSynchronization();
		mG.stop(true);
		mD.stop(true);
		mG.endSynchronization();
	}
	public void move(boolean direction) {
		mD.setAcceleration(720);//TODO
		mG.setAcceleration(720);//TODO
		mD.setSpeed(maxSpeed);
		mG.setSpeed(maxSpeed);
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

	public void moveTo(boolean direction, int dist) {
		mD.setAcceleration(720);//TODO
		mG.setAcceleration(720);//TODO
		mD.setSpeed(maxSpeed);
		mG.setSpeed(maxSpeed);
		mG.startSynchronization();
		if(direction) {
			mD.forward();
			mG.forward();
		}else {
			mD.backward();
			mG.backward();
		}
		mG.endSynchronization();
		Delay.msDelay(dist*40);// a 540 de vitesse le rapport distance et temps est : temps = distance*40
	/*	mG.startSynchronization();
		mG.close();
		mD.close();
		mG.endSynchronization();*/
		
	}
	public void rotationRapide(int angle) {
		boussole=(boussole+angle)%360;
		mG.setAcceleration(720);
		mD.setAcceleration(720);
		mD.setSpeed(maxSpeed);
		mG.setSpeed(maxSpeed);
		mG.startSynchronization();
		mG.rotate((int)(angle*2.16));
		mD.rotate((int)(-angle*2.16));
		mG.endSynchronization();
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
		System.out.println(dist+" <= "+distanceParcourue);
		return dist<=distanceParcourue;
	}
	private void calcDistParcourue() {
		int g = Math.abs(mG.getTachoCount());
		distanceParcourue+=(((Math.PI*57/360)*g));// (2*PI*r/360) *degres parcourus
		mG.resetTachoCount();
		mD.resetTachoCount();
	}
	public void resetDist() {
		distanceParcourue=0;
	}
	
	public static void main(String[] args) {
		Activators a = new Activators();
		a.moveTo(true, 60);//2400 pour 60 cm donc x40
	}
	
}
