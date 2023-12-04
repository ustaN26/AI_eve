package fr.eve.main.test;

import fr.eve.main.BAU;
import fr.eve.main.Constantes;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;

public class TestPince implements Constantes{	
	public static void main(String[] args) {
		BAU.bau();
		//new TestPince().test();
		try {
			new TestPince().testThread();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public void test() {
		mP.rotate(-900);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mP.rotate(900);
	}
	private Thread pinceTask;
	private boolean ordrePince = false, etatPince = true;
	private void testThread() throws InterruptedException {
		ordrePince = false;
		resetPinceTask();
		pinceTask.start();
		pinceTask.join();
		ordrePince = true;
		resetPinceTask();
		pinceTask.start();
		pinceTask.join();
		ordrePince = false;
		resetPinceTask();
		pinceTask.start();
		pinceTask.join();
	}
	public Thread resetPinceTask() {
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
		return pinceTask;
	}
}
