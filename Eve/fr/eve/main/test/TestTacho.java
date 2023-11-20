package fr.eve.main.test;

import java.io.FileOutputStream;
import fr.eve.main.Activators;
import fr.eve.main.Constantes;
import fr.eve.main.tester.DistanceTest;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;

public class TestTacho implements Constantes{
	private Activators activators;
	public static FileOutputStream file;
	//private Sensors sensors;
	
	public static void main(String[] args) {
		new TestTacho().test();
	}
	public void test() {
		activators = new Activators();
		//sensors = new Sensors();
		//EVE.start();
		BrickFinder.getDefault().getKey(Button.ENTER.getName()).addKeyListener(new KeyListener() {
			@Override // boutton arret d'urgance
			public void keyReleased(Key k) {
				System.exit(0);
			}
			@Override
			public void keyPressed(Key k) {}
		});
		System.out.println("start");
		test2();
		System.exit(0);
	}
	public void test2() {
		activators.resetDist();
		mG.setAcceleration(720);
		mD.setAcceleration(720);
		activators.move(true);
        TestPremierPalet.avancerjusqua(new DistanceTest(2400, activators));
        activators.stop();
        System.out.println("STOPPP");
	}
	/*public void test1() {
		int time = 0;
		mG.resetTachoCount();
		while(time<=100) {
			try {
				time++;
				mG.rotate(5);
				System.out.println(mG.getTachoCount());	
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}*/
}
