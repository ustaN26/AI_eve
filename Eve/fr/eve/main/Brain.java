package fr.eve.main;

import fr.eve.main.tester.DistanceTest;
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
	public Activators getActivator() { return activators; }
	private static Sensors sensors;
	public Sensors getSensor() { return sensors; }
	private Thread brainThread;
	public Thread getThread() {	return brainThread;}
	private Etats state;
	public Etats getState() { return state; }

	public Brain() {
		activators = new Activators();
		sensors = new Sensors(this);
		state = Etats.premierPalet;
		waitForRestart();
		while(getState()!=Etats.end) {
			try {
				Thread.sleep(1000000);//>2m30 pour pas encombrer le proco
			} catch (InterruptedException ignored) {};
		}
	}

	private void init() {
		state = Etats.premierPalet;
		brainThread = resetBrainThread();
		brainThread.start();
	}

	private Thread resetBrainThread() {
		return new Thread() {
			public void run() {
				switch(state) {
				case premierPalet:
					premierPalet();
				case marquerPalet:
					marquerPalet();
				case acheminerPalet :
					acheminerPalet();
				case detectionDuPalet:
					detectionDuPalet();
				case allerChercherPalet :
					allerChercherPalet();
				default:
					break;
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

	public void avancerjusqua(Tester t) {
		activators.move(true);
		while(!t.test()) {
			try { Thread.sleep(1);
			} catch (InterruptedException ignored) {}
		}
		activators.stop();
	}

	private void premierPalet() {
		activators.ouverturePince(true);
		avancerjusqua(new TouchTest(sensors));
		activators.ouverturePince(false);
		activators.rotationRapide(45);
		activators.stop();
		activators.moveTo(true, 20);
		activators.stop();
		activators.rotationRapide(-90);
		activators.stop();
		avancerjusqua(new USTest(25,sensors));
		state = Etats.marquerPalet;
	}
	protected void marquerPalet() {
		activators.move(true);
		TestPremierPalet.avancerjusqua(new USTest(19, sensors));//(0.18m)
		activators.ouverturePince(true);
		activators.move(false);
		activators.resetDist();
		activators.moveTo(true, 15);
		activators.ouverturePince(false);
		activators.stop();
		state = Etats.detectionDuPalet;
	}
	private void detectionDuPalet() {
		activators.rotationRapide(90);
		activators.rotationRapide(detectPalet(detection360())-180);
		state = Etats.allerChercherPalet;
	}

	private void allerChercherPalet() {//ou attraper palet
		activators.move(true);
		activators.moveTo(true,(int)(sensors.getData()*100-20));
		activators.ouverturePince(true);
		activators.stop();
		avancerjusqua(new TouchTest(sensors));
		activators.ouverturePince(false);
		activators.stop();
		state = Etats.acheminerPalet;
	}
	private void acheminerPalet() {
		activators.droitDevant();
		activators.move(true);
		TestPremierPalet.avancerjusqua(new USTest(30, sensors));
		activators.stop();
		state = Etats.marquerPalet;
	}

	public float[] detection360(){//TODO mettre a jour
		List<Float> tabVal = new ArrayList<>();
		sensors.resetDistBuffer();
		sensors.setDetect(true);
		activators.rotationRapide(360);
		sensors.setDetect(false);
		List<Float> dists = sensors.getDistBuffer();
		int nbVal=dists.size()/360;
		for(int i=0;i<360;i++){
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
		try {
			brainThread.suspend();
			brainThread.stop();
			brainThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int rot = 45; 
		activators.rotationRapide(rot);
		if(sensors.getData()>0.25) {
			activators.rotationRapide(-2*rot);
			rot = -rot;
		}
		activators.move(true);
		avancerjusqua(new DistanceTest(200, activators));
		activators.resetDist();
		activators.stop();
		activators.rotationRapide(-rot);
		activators.resetDist();
		brainThread = resetBrainThread();
		brainThread.start();
	}

	public void endGame() {
		try {
			brainThread.suspend();
			brainThread.stop();
			brainThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		BAU.bau(this);
		init();
	}

	public static void main(String[] args) {
		Brain b = new Brain();
	}

	
}



  


