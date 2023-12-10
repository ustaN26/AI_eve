package fr.eve.main;

import fr.eve.main.tester.USTest;
import fr.eve.main.tester.Tester;
import fr.eve.main.tester.TouchTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;

/**
 * Classe Brain 
 * le 'cerveau' du robot, contient le thread principale qui gere les etats, les demandes d'esquive et les taches complexes
 */
@SuppressWarnings("deprecation")
public class Brain implements Constantes{
	/** object de gestion des moteurs
	 * @getter getActivators()
	 * */
	private Activators activators;
	public Activators getActivators() { return activators; }
	
	/** object de gestion des capteurs
	 * @getter getSensors()
	 * */
	private Sensors sensors;
	public Sensors getSensors() { return sensors; }
	
	/**
	 * thread de la classe en boucle.
	 * @getter getThread()
	 */
	private Thread brainThread;
	public Thread getThread() {	return brainThread;}
	
	/**
	 * Etat de la partie.
	 * @getter getState()
	 * @see Etats
	 */
	private Etats state;
	public Etats getState() { return state; }
	
	/**
	 * drapeau levé lors de la detection d'un obstacle
	 * permet d'arreter le thread principale pour demarrer l'esquive
	 */
	private boolean esquiveInterupt = false;
	public boolean getEsquiveInterrupt(){ return esquiveInterupt;}

	
	/**
	 * constructeur
	 * initialise les objects de gestions des moteurs et capteurs
	 * puis attend l'ordre de demarrage
	 * une fois demarré, le programme se pause et laisser gerer les threads jusqu'a la fin de partie.
	 */
	public Brain() {
		activators = new Activators(this);
		sensors = new Sensors(this);
		waitForRestart();
		while(getState()!=Etats.end) {
			try {
				Thread.sleep(1000000);//>2m30 pour pas encombrer le proco
			} catch (InterruptedException ignored) {};
		}
	}
	/**
	 * initialise l'etat de la partie  (premierPalet), l'orloge et le thread Principale
	 */
	private void init() {
		state = Etats.premierPalet;
		sensors.resetClock();
		brainThread = resetBrainThread();
		brainThread.start();
	}
	
	/**
	 * cree un nouveau thread principal qui s'interromp si une esquive est necessaire.
	 * utilise un switch pour lancer la bonne fonction selon l'etat de la partie  
	 * 
	 * @return le thread resultant
	 */
	private Thread resetBrainThread() {
		return new Thread() {
			public void run() {
				while(!esquiveInterupt){
					switch(state) {
					case premierPalet:
						premierPalet();
						break;
					case marquerPalet:
						marquerPalet();
						break;
					case detectionDuPalet:
						detectionDuPalet();
						break;
					case allerChercherPalet :
						allerChercherPalet();
						break;
					case acheminerPalet :
						acheminerPalet();
						break;
					default:
						break;
					}
				}
			}
		};
	}

	/**
	 * liste des etats possible de la partie 
	 */
	public static enum Etats {
		premierPalet,
		marquerPalet,
		allerChercherPalet,
		detectionDuPalet,
		acheminerPalet,
		end;
	}
	/**
	 * avance de maniere asynchrone jusqu'a ce qu'un test soit validé ou qu'une esquive soit necessaire
	 * @param un objet Tester
	 */
	public boolean avancerjusqua(Tester t) {
		activators.move(true);
		while(!t.test()) {
			if(esquiveInterupt) return false;
			try { Thread.sleep(1);
			} catch (InterruptedException ignored) {}
		}
		activators.stop();
		return true;
	}
	/**
	 * dans l'etat premierPalet
	 * demarre en ouvrant les pinces juqu'a toucher un palet puis referme les pinces
	 * tourne de 45° avance de 20cm puis se remet dans l'axe du but ennemi
	 * change l'état en acheminerPalet
	 */
	private void premierPalet() {
		activators.ouverturePince(true);
		if(!avancerjusqua(new TouchTest(sensors)))
			return;
		activators.ouverturePince(false);
		activators.rotationRapide(45);
		if(!activators.moveTo(true, 20))
			return;
		activators.rotationRapide(-45);
		System.out.println("fini premierpalet");
		state = Etats.acheminerPalet;
	}
	/**
	 * dans l'etat marquerPalet
	 * ouvre les pinces, recule de 40cm, referme les pinces
	 * change l'état en detectionDuPalet
	 */
	private void marquerPalet() {
		System.out.println("je marque");
		activators.ouverturePince(true);
		if(!activators.moveTo(false,40))
			return;
		activators.ouverturePince(false);
		System.out.println("j'ai fermé");
		state = Etats.detectionDuPalet;
	}
	
	/**
	 * dans l'etat detectionDuPalet
	 * tourne de 90° puis se tourne vers l'angle donné par detectPalet
	 * change l'état en allerChercherPalet
	 */
	private void detectionDuPalet() {
		System.out.println("je cherche");
		activators.rotationRapide(90);
		activators.rotationRapide(detectPalet(detection360())-180);
		state = Etats.allerChercherPalet;
	}
	
	/**
	 * dans l'etat allerChercherPalet
	 * ouvre les pinces, avance jusqu'a toucher le palet ciblé puis referme les pinces
	 * change l'état en acheminerPalet
	 */
	private void allerChercherPalet() {
		System.out.println("je vais le chercher");
		activators.ouverturePince(true);
		if(!avancerjusqua(new TouchTest(sensors)))
			return;
		activators.ouverturePince(false);
		state = Etats.acheminerPalet;
	}
	/**
	 * dans l'etat acheminerPalet
	 * se tourne vers le but ennemi puis avance jusqu'a etre à 20cm du mur du fond
	 * change l'état en marquerPalet
	 */
	private void acheminerPalet() {
		System.out.println("je ramène le palet");
		activators.droitDevant();
		if(!avancerjusqua(new USTest(20, sensors)))
			return;
		state = Etats.marquerPalet;
	}

	/**
	 * enregistre toutes les données lues du capteur de distance tout en effectuant une rotation de 180°
	 * calcule ensuite la moyenne de distances lues pour degree
	 * @return le tableau des distances moyennes lues par degree 
	 */
	public float[] detection360(){
		List<Float> tabVal = new ArrayList<>();
		sensors.resetDistBuffer();
		sensors.setDetect(true);
		activators.rotationRapide(180);
		sensors.setDetect(false);
		List<Float> dists = sensors.getDistBuffer();
		int nbVal=dists.size()/360;
		for(int i=0;i<180;i++){
			float t = 0;
			for(int j = i*nbVal;j<(i+1)*nbVal;j++) {
				t += dists.get(j);
			}
			t/=nbVal;
			tabVal.add(t);
		}
		sensors.resetDistBuffer();
		float[] floats = new float[tabVal.size()];
		for (int i = 0; i < floats.length; i++)
			floats[i] = tabVal.get(i);
		return floats;
	}

	/**
	 * il parcours tout le tableau, au premier écart d'augmentation de valeurs de plus de 0.05m il note un premier indice, à l'écart suivant de diminution de valeur de plus de 0.05m il note un seconde indice. L'écart entre les indices est vérifier pour ne pas avoir d'angle. Puis un compteur est fait pour voir si toutes les valeurs entre les deux indices sont bien inférieur au deux, si 80% de ces valeurs sont inférieur, alors c'est un palet. Et enfin la moyenne des deux indices est faite pour avoir le milieu du palet.
	 * @param dist, un tableau de 180 distances 
	 * @return un angle qui cible un palet
	 */
	public int detectPalet(float[] dist) {
		int indiceDebut=0, indiceFin=0, anglePalet=0; 
		float distancePalet=3;
		for (int i=0; i<dist.length-1; i++) {
			if ( Math.abs(dist[i]-dist[i+1])>0.05 && dist[i+1]<dist[i] ) {
				indiceDebut=i+1;
			}
			if (Math.abs(dist[i]-dist[i+1])>0.05 && dist[i+1]>dist[i]){
				indiceFin=i;
			}
			if (indiceDebut<indiceFin && indiceDebut>=indiceFin-50) {
				int compteur=0;
				for (int k=indiceDebut; k<indiceFin; k++){
					if (dist[k]<dist[indiceDebut] && dist[k]<dist[indiceFin])
						compteur++;
				}
				if (compteur> (indiceDebut-indiceFin)*0.8 && dist[(indiceDebut+indiceFin)/2]<distancePalet){
					anglePalet=(indiceDebut+indiceFin)/2;
					distancePalet=dist[(indiceDebut+indiceFin)/2];
				}
			}
		}
		return anglePalet;
	}

	/**
	 * appelé par le thread capteur losqu'un obstacle est à esquivé
	 * attend que le thread principale s'arrete en levant le drapeau esquiveInterupt
	 * tourne vers la droite(45*), regarde si un obstacle est à moins de 15cm
	 * si oui se tourne vers la gauche(-45°)
	 * avance de 20cm, se retourne dans son axe d'origine
	 * puis reinitialise le thread principale (sans changer l'etat du jeu)
	 */
	public void esquive() {
		System.out.println("esquive!!!!");
		esquiveInterupt = true;
		activators.stop();
		try {
			brainThread.join();
			System.out.println("brain killed");	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		esquiveInterupt = false;
		int rot = 45; 
		activators.rotationRapide(rot);
		if(sensors.getData()<0.15) {
			activators.rotationRapide(-2*rot);
			rot*= -1;
		}
		activators.moveTo(true, 20);
		activators.rotationRapide(-1*rot);
		System.out.println("esquive finie");	
		brainThread = resetBrainThread();
		brainThread.start();
		System.out.println("brain restarted");
	}

	/**
	 * appelé par le thread capteur losque le temps de l'orloge est dépassé
	 * attend que le thread principale s'arrete en levant le drapeau esquiveInterupt
	 * puis attend l'ordre de redemarrage de la partie ou d'arret du programme
	 */
	public void endGame() {
		esquiveInterupt = true;
		state = Etats.end;
		try {
			brainThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		esquiveInterupt = false;
		System.out.println("temps écouler");
		waitForRestart();//shutdown or continue
	}
	
	/**
	 * attend l'ordre de redemarrage de la partie (bouton entré) ou d'arret du programme (bouton echap)
	 * si le programme continue, active le bouton d'arret d'urgence et initialise les parametres de debut de partie
	 */
	public void waitForRestart() {
		final AtomicBoolean restart = new AtomicBoolean(false);
		System.out.println("enter = restart / escape = stop");
		BrickFinder.getDefault().getKey(Button.ENTER.getName()).addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(Key k) {
				restart.set(true);
				System.out.println("start");
			}
			@Override
			public void keyPressed(Key k) {}
		});
		BrickFinder.getDefault().getKey(Button.ESCAPE.getName()).addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(Key k) {
				System.exit(0);
			}
			@Override
			public void keyPressed(Key k) {}
		});
		while(!restart.get()) {
			try { Thread.sleep(1);
			} catch (InterruptedException ignored) {}
		}
		BrickFinder.getDefault().getKey(Button.ENTER.getName()).addKeyListener(new KeyListener() {
			//listener vide pour surrpimer celui present
			@Override
			public void keyReleased(Key k) {}
			@Override
			public void keyPressed(Key k) {}
		});
		BAU.bau();
		init();
	}
	/**
	 * essais de demmarer Brain en ignorant les IllegalArgumentException qui sont levé aléatoirement lors du demmarage du programme
	 * @param args
	 */
	public static void main(String[] args) {
		while(true){
			try{
				Brain b = new Brain();
			} catch(IllegalArgumentException ignored){}
		}
	}	
}



  


