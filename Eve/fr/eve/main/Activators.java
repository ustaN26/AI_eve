package fr.eve.main;

import lejos.hardware.motor.EV3LargeRegulatedMotor;

/**
 * Classe Activators 
 * gere tout ce qui est lié aux moteurs
 */
public class Activators implements Constantes {
	/** brain le programme principale */
	private Brain brain;
	
	/**
	 * Tache d'ouverture/fermeture de la pince asynchrone
	 */
	private Thread pinceTask;
	/**
	 * etat actuel de la pince
	 * @true = ouvert ; @false = fermé
	 * @see ordrePince
	 */
	private boolean etatPince = false;
	/**
	 * etat souhaité de la pince
	 * @true = ouvert ; @false = fermé
	 * @see etatPince
	 */
	private boolean ordrePince = false;
	
	/**
	 * angle entre l'orientation du robot et l'axe du terrain
	 * 0° pointe vers le but ennemi
	 * variable mise à jour lorsque le robot fait une rotation 
	 */
	private int boussole=0;

	/**
	 * constructeur
	 * @param brain le programme principale
	 * 
	 * creer le lien de synchronisation entre les moteurs et initialise leurs parametres
	 * 
	 * @exception arrete le programme si la synchronisation des moteurs echoue 5 fois
	 */
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
	
	/**
	 * arrete les moteurs de maniere synchronisé apres un move()
	 * @see move()
	 */
	public void stop() {
		mG.startSynchronization();
		mG.stop(true);
		mD.stop(true);
		mG.endSynchronization();
	}
	/**
	 * demarre les moteurs de maniere synchronisé
	 * @param direction, @true = avant, @false = arriere
	 * 
	 * @see stop()
	 */
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

	/**
	 * commande un déplacement sur une distance donnée, le temps de roulage est calculé avec empiriquement, minimisant l'erreur du aux accelerations au demarrage et à l'arret
	 * 
	 * @param direction, @true = avant, @false = arriere
	 * @param dist : la distance en cm
	 * 
	 * @return false si une esquive est necessaire
	 */
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
	/**
	 * Effectu une rotation avec les moteurs synchronisés et met à jour la bousolle
	 * Le facteur "2.16" sert à minimiser l'erreur du aux accelerations au demarrage et à l'arret
	 * @param angle : en degree 
	 */
	public void rotationRapide(int angle) {
		boussole=(boussole+angle)%360;
		mG.startSynchronization();
		mD.rotate((int)(angle*(-2.16)));
		mG.rotate((int)(angle*2.16));
		mG.endSynchronization();
		mD.waitComplete();
		mG.waitComplete();
		stop();
	}
	
	/**
	 * tourne le robot vers le but adverse
	 * @see boussole
	 */
	public void droitDevant() {
		rotationRapide(-boussole);
	}
	
	/*
	 * Manipule l'ouverture des pinces, ne permet pas de faire la même action deux fois d'affiler
	 * PinceOuverte(True) : ordonne d'ouvrir les pinces
	 * PinceOuverte(False) : ordonne de fermer les pinces 
	 * Aide au valeur du moteur : 
	 * Valeur dans les positifs forward => Fermé vers ouverte
	 * Valeur dans les négatifs backward => Ouverte vers fermé
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
}
