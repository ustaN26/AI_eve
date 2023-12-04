package fr.eve.main;

import fr.eve.main.tester.USTest;
import fr.eve.main.tester.Tester;
import fr.eve.main.tester.TouchTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.eve.main.test.TestPremierPalet;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;


@SuppressWarnings("deprecation")
public class Brain implements Constantes{
	private Activators activators;
	public Activators getActivators() { return activators; }
	private Sensors sensors;
	public Sensors getSensors() { return sensors; }
	private Thread brainThread;
	public Thread getThread() {	return brainThread;}
	private Etats state;
	public Etats getState() { return state; }
	private boolean esquiveInterupt = false;
	public boolean getEsquiveInterrupt(){ return esquiveInterupt;}

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

	private void init() {
		state = Etats.premierPalet;
		sensors.resetClock();
		brainThread = resetBrainThread();
		brainThread.start();
	}

	private Thread resetBrainThread() {
		return new Thread() {
			public void run() {
				//activators.moveTo(true,100);
				//if(esquiveInterupt) return;
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

	public static enum Etats {
		premierPalet,
		marquerPalet,
		allerChercherPalet,
		detectionDuPalet,
		acheminerPalet,
		end;
	}

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

	private void premierPalet() {
		activators.ouverturePince(true);
		if(!avancerjusqua(new TouchTest(sensors)))
			return;
		activators.ouverturePince(false);
		activators.rotationRapide(45);
		if(!activators.moveTo(true, 20))
			return;
		activators.rotationRapide(-50);
		System.out.println("fini premierpalet");
		state = Etats.acheminerPalet;
	}
	
	private void marquerPalet() {
		System.out.println("je marque");
		if(!avancerjusqua(new USTest(20, sensors)))//(0.18m))
			return;
		activators.ouverturePince(true);
		if(!activators.moveTo(false,20))
			return;
		activators.ouverturePince(false);
		System.out.println("j'ai fermé");
		state = Etats.detectionDuPalet;
	}
	private void detectionDuPalet() {
		System.out.println("je cherche");
		activators.rotationRapide(90);
		activators.rotationRapide(detectPalet(detection360())-180);
		state = Etats.allerChercherPalet;
	}

	private void allerChercherPalet() {
		System.out.println("je vais le chercher");
		activators.ouverturePince(true);
		if(!avancerjusqua(new TouchTest(sensors)))
			return;
		activators.ouverturePince(false);
		state = Etats.acheminerPalet;
	}
	private void acheminerPalet() {
		System.out.println("je ramène le palet");
		activators.droitDevant();
		if(!avancerjusqua(new USTest(30, sensors)))
			return;
		state = Etats.marquerPalet;
	}

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
		BAU.bau(this);
		init();
	}

	public static void main(String[] args) {
		while(true){
			try{
				Brain b = new Brain();
			} catch(IllegalArgumentException ignored){}
		}
	}	
}



  


