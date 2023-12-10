package fr.eve.main.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.eve.main.Constantes;

public class TestDetectPalet implements Constantes{
	public static void main(String[] args) {
		System.out.println(detectPalet(detection360()));
	}
	public static float[] detection360(){
		List<Float> tabVal = new ArrayList<>();
		final List<Float> distBuffer = new ArrayList<>();
		final AtomicBoolean stop = new AtomicBoolean(false);
		Thread detect = new Thread() {
			public void run() {
				while(!stop.get())
					distBuffer.add(TestUS.getData());
			};
		};
		detect.start();
		TestRotation.rotationRapide(180);
		stop.set(true);
		List<Float> dists = distBuffer;
		int nbVal=dists.size()/360;
		for(int i=0;i<180;i++){
			float t = 0;
			for(int j = i*nbVal;j<(i+1)*nbVal;j++) {
				t += dists.get(j);
			}
			t/=nbVal;
			tabVal.add(t);
		}
		float[] floats = new float[tabVal.size()];
		for (int i = 0; i < floats.length; i++)
			floats[i] = tabVal.get(i);
		return floats;
	}

	public static int detectPalet(float[] dist) {
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
}
