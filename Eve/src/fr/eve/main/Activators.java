package fr.eve.main;

public class Activators implements Constantes {
	private float speedG, speedD;
	private boolean dirG, dirD;
	private Thread moveTask;
	private boolean pinceOuverte= false;
	
	public Activators() {
		moveTask = new Thread() {
			private void init() {
				//mG.synchronizeWith(mD);// TODO
				mG.startSynchronization();
			}
			public void run() {
				mG.setSpeed(speedG);
				mD.setSpeed(speedD);
				try { Thread.sleep(1);
				} catch (InterruptedException ignored) {}
			};
		};
	}
	public void move(Boolean dir) {
		picoMove(dir, mD.getMaxSpeed());
	}
	public void monven(Boolean dir) {

	}
	public void rotate(int angle) {
		//TODO
	}
	public void hardRotate(int angle) {
		//TODO
		mD.stop();
		mG.stop();
	}
	public void picoMove(boolean avancer, float vit) {
		dirD=dirG=avancer;
		speedD=speedG=vit;
	}
	public void stop() {
		speedD=speedG=0;
	}
	
	/*
	 * Manipule l'ouverture des pinces, ne permet pas de faire la même action deux d'affiler
	 * PinceOuverte(True) : Ouvre les pinces
	 * PinceOuverte(False) : Ferme les pinces 
	 * Aide au valeur du moter : 
	 * Valeur dans les positifs ça va forward => Fermé vers ouverte
	 * Valeur dans les négatifs ca va backward => Ouverte vers fermé
	 */
	public void pinceOuverte(boolean b) {
		if (b==true && pinceOuverte==false) {
			mP.rotate(900);
			pinceOuverte=false;
		}
		if (b==false && pinceOuverte==true) {
			mP.rotate(-900);
			pinceOuverte=true;
		}
	}
}
