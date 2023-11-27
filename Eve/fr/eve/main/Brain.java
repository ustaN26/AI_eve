package fr.eve.main;

import fr.eve.main.tester.DistanceTest;
import fr.eve.main.tester.IRTest;
import fr.eve.main.tester.Tester;
import fr.eve.main.tester.TouchTest;

import java.util.ArrayList;
import java.util.List;

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

	public Brain() {
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
		premierPalet();
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
		chercherPalet();
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
		activators.move(true);
	}
	
	public float[] detection360(){
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
				t += dists.get(i*nbVal+j);
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

	public static void main(String[] args) {
		Brain a=new Brain();
		int i=a.detectPalet(a.detection360());
		System.out.println(i);
		Delay.msDelay(4000);
		System.exit(0);
	}
}






