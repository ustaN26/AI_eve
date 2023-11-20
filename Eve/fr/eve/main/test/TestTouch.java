package fr.eve.main.test;

import java.io.FileOutputStream;
import fr.eve.main.Activators;
import fr.eve.main.Constantes;
import fr.eve.main.Sensors;
import fr.eve.main.tester.DistanceTest;
import fr.eve.main.tester.TouchTest;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;

public class TestTouch implements Constantes{
	private Activators activators;
	public static FileOutputStream file;
	//private Sensors sensors;
	
	public static void main(String[] args) {
		new TestTouch().test();
	}
	public void test() {
		Sensors sensors = new Sensors();
		BrickFinder.getDefault().getKey(Button.ENTER.getName()).addKeyListener(new KeyListener() {
			@Override // boutton arret d'urgance
			public void keyReleased(Key k) {
				System.exit(0);
			}
			@Override
			public void keyPressed(Key k) {}
		});
		System.out.println("start");
	    TestPremierPalet.avancerjusqua(new TouchTest(sensors));
	    System.out.println("TOUCHED");
	    try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
	
}
