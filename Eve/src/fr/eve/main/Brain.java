package fr.eve.main;

public class Brain {
	private Activators activators;
	private Sensors sensors;
	private Thread brainThread;
	
	public Brain() {
		activators = new Activators();
		sensors = new Sensors();
		
		brainThread = new Thread() {
			public void run() {
				detectPalet();
				try { Thread.sleep(1);
				} catch (InterruptedException ignored) {}
			}
		};
		brainThread.start();
	}
	
	public void detectPalet() {
		sensors.getDistBuffer().clear();
		
	}
}
