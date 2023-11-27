package fr.eve.main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import fr.eve.main.tester.DistanceTest;
import fr.eve.main.tester.IRTest;
import fr.eve.main.tester.Tester;
import fr.eve.main.tester.TouchTest;
import fr.eve.mainold.Deplacement;
import fr.eve.main.test.TestPremierPalet;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.utility.Delay;

public class Brain implements Constantes{
	private Activators activators;
	private static Sensors sensors;

	public Sensors getSensor() {
		return sensors;
	}
	private Thread brainThread;
	private Etats state;

	public Brain() {
		state = Etats.PremierPalet;
		activators = new Activators();
		sensors = new Sensors();
		BrickFinder.getDefault().getKey(Button.ENTER.getName()).addKeyListener(new KeyListener() {
			@Override // bouton  arret d'urgance
			public void keyReleased(Key k) {
				System.exit(0);
			}
			@Override
			public void keyPressed(Key k) {}
		});
		brainThread = new Thread() {
			public void run() {
				while(true) {
					switch(state) {
					case PremierPalet:
						premierPalet();
					case MarquerPalet:
						marquerPalet();
						break;
					case AcheminerPalet :
						acheminerPalet();
						break;
					case DetectPalet :
						detectPalet(detection360());
						break;
					default:
						break;
					}
					try { Thread.sleep(1);
					} catch (InterruptedException ignored) {}	
				}
			}
		};
		//brainThread.start();
		//x	while(true);
	}

	private static enum Etats {
		PremierPalet,
		MarquerPalet,
		DetectPalet,
		PrendrePalet,
		AcheminerPalet;
	}

	public static void avancerjusqua(Tester t) {
		while(!t.test()) {
			try { Thread.sleep(10);
			} catch (InterruptedException ignored) {}
		}
	}

	private void premierPalet() {
		activators.move(true);
		activators.ouverturePince(true);
		avancerjusqua(new TouchTest(sensors));
		activators.ouverturePince(false);
		activators.stop();
		activators.rotationRapide(45);
		activators.move(true);
		avancerjusqua(new DistanceTest(200, activators));
		activators.resetDist();
		activators.stop();
		activators.rotationRapide(-45);
		activators.resetDist();
		activators.move(true);
		activators.droitDevant();
		avancerjusqua(new IRTest(30,sensors));
		activators.resetDist();
		activators.stop();
	}
	protected void marquerPalet() {
		activators.move(true);
		TestPremierPalet.avancerjusqua(new IRTest(0.19f, sensors));//(0.18m)
		activators.ouverturePince(true);
		activators.move(false);
		TestPremierPalet.avancerjusqua(new DistanceTest(150,activators));
		activators.rotationRapide(180);
	}
	private void chercherPalet() {//ou attraper palet
		avancerjusqua(new DistanceTest((int)(sensors.getData()*100-20), activators));
		activators.ouverturePince(true);
		avancerjusqua(new TouchTest(sensors));
		activators.ouverturePince(false);
		activators.stop();
		acheminerPalet();
	}
	private void acheminerPalet() {
		activators.droitDevant();
		activators.move(true);//TODO voir si ya des obstacles droit devant à eventuellement esquiver

	}
	
	public float[] detection360() {
		float g=0;
		int nbVal=0;
		float[] valAngle=new float[360];
		mG.setAcceleration(520);
		mD.setAcceleration(520);
		mG.setSpeed(180); //125 pour 31700
		mD.setSpeed(180);
		mG.startSynchronization();
		mG.rotate((int) (360*2.16));
		mD.rotate((int) (-360*2.16));
		for(int i=0;i<360;i++) {
			for(int j=0;j<20;j++) {
				float t= sensors.getData();
				if(t<3) {
					g+=t;
					nbVal++;
				}
			}
			if(g==0) {
				g=3;
			}
			else {
				g=g/nbVal;
			}
			valAngle[i]=g;
			g=0;
			nbVal=0;
			Delay.usDelay(4200);
		}  
		mG.waitComplete();
		mD.waitComplete();
		mG.endSynchronization();
		return valAngle;
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

	public static void main(String[] args) {
		Brain a=new Brain();
		int i=a.detectPalet(a.detection360());
		System.out.println(i);
		Delay.msDelay(4000);
		System.exit(0);
	}
}






