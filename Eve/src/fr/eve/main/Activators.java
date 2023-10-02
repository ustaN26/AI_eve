package fr.eve.main;

public class Activators implements Constantes {
	private float speedG, speedD;
	private boolean dirG, dirD;
	private Thread moveTask;
	
	public Activators() {
		moveTask = new Thread() {
			private void init() {
				//mG.synchronizeWith(mD);// TODO
				mG.startSynchronization();
			}
			public void run() {
				mG.setSpeed(speedG);
				mD.setSpeed(speedD);
				try { Thread.sleep(1);
				} catch (InterruptedException ignored) {}
			};
		};
	}
	public void move(Boolean dir) {
		picoMove(dir, mD.getMaxSpeed());
	}
	public void rotate(int angle) {
		//TODO
	}
	public void hardRotate(int angle) {
		//TODO
	}
	public void picoMove(boolean avancer, float vit) {
		dirD=dirG=avancer;
		speedD=speedG=vit;
	}
}
