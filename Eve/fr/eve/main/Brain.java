package fr.eve.main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import fr.eve.main.tester.DistanceTest;
import fr.eve.main.tester.IRTest;
import fr.eve.main.tester.Tester;
import fr.eve.main.tester.TouchTest;
import fr.eve.main.test.TestPremierPalet;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.utility.Delay;

public class Brain implements Constantes{
	private Activators activators;
	private Sensors sensors;

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
			@Override // boutton arret d'urgance
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
						AcheminerPalet();
						break;
					case DetectPalet :
						detecterPalet();
						break;
					default:
						break;
					}
					try { Thread.sleep(1);
					} catch (InterruptedException ignored) {}	
				}
			}
		};
		brainThread.start();
		while(true);
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
		activators.move(false);
		activators.rotationRapide(45);
		activators.move(true);
		avancerjusqua(new DistanceTest(200, activators));
		activators.resetDist();
		activators.move(false);
		activators.rotationRapide(-45);
		activators.resetDist();
		activators.move(true);
		activators.droitDevant();
		//TODO avancer jusqua mur
		activators.resetDist();
		activators.stop();
	}
	protected void marquerPalet() {
		TestPremierPalet.avancerjusqua(new IRTest(2, sensors));//on verra pour la distance
		activators.ouverturePince(true);
		activators.move(false);
		TestPremierPalet.avancerjusqua(new DistanceTest(150,activators));
		activators.rotationRapide(180);
	}
	private void AcheminerPalet() {

	}
	protected void detecterPalet() {
	}

	public void detection360() throws IOException{
		String s="";
		FileOutputStream f=new FileOutputStream("valeur.txt");
		mG.setSpeed(130); //125 pour 31700
		mD.setSpeed(130);
		activators.synch(true);
		mG.forward();
		mD.backward();
		activators.synch(false);
		float g=0;
		int nbVal=0;
		for(int i=0;i<360;i++) {
			for(int j=0;j<20;j++) {
				float t= sensors.getData();
				if(t<3) 
					g+=t;
				nbVal++;
			}
			if(g==0) {
				g=3;
			}
			else {
				g=g/nbVal;
			}
			s =""+ g;
			f.write((s+"\n").getBytes());
			g=0;
			nbVal=0;
			Delay.usDelay(12000);
		}
		activators.synch(true);
		mG.stop();
		mD.stop();
		activators.synch(false);
		f.flush();
		f.close();
	}

	public float[] tabValeur() throws IOException {
		float[] valeur= new float[360];
		FileInputStream f=new FileInputStream("valeur.txt");
		for(int i=0;i<360;i++) {
			valeur[i]=f.read();
		}
		f.close();
		return valeur;
	}

	public int detectPalet(float[] dist) { 
		/*programme qui va détecter les écarts 
		 * entre la valeur de distance demandée et la valeur 
		 * détectée afin de detecter la presence d'un palet
		 */
		int compteur=0,i=0,j=0;
		int indiceDebut=0;
		float a,b;
		for ( i=0; i<dist.length;i++) {
			if (dist[i]-dist[i+1]<0.2 && dist[i+1]<dist[i]) {
				a=dist[i]-dist[i+1];
				indiceDebut=i+1;

				for (j=i; j<dist.length;j++)
					if (dist[j]-dist[j+1]<0.2 && dist[j+1]>dist[j]) {
						b=dist[j]-dist[j+1];
						if (a==b && (i-j<50) && (i-j>10))
							for (int k = i; k<j; k++) {
								if (dist[k]>dist[i+1]+0.07 && dist[k]<=dist[j])
									compteur++;
							}
					}
			}
		}
		if (compteur>30 && compteur<50)
			return (i+j)/2;
		else return -1;
	}
}









