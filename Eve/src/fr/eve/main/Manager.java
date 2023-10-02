package fr.eve.main;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public abstract class Manager {
	private final EV3LargeRegulatedMotor mP = new EV3LargeRegulatedMotor(MotorPort.A);
	private final EV3LargeRegulatedMotor mG = new EV3LargeRegulatedMotor(MotorPort.C);
	private final EV3LargeRegulatedMotor mD = new EV3LargeRegulatedMotor(MotorPort.B);
	private float speedG, speedD;
	private boolean dirG=true, dirD=true;
	
	private final EV3ColorSensor colorSensor = new EV3ColorSensor (SensorPort.S4);
	private final List<Integer> colorBuffer = new ArrayList<>();
	private int lastColor;
	public List<Integer> getColorBuffer() { return colorBuffer;}
	
	private final EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S1);//TODO check port
	private boolean touch;
	public boolean isTouch() { return touch;}
	
	private final EV3UltrasonicSensor usSensor = new EV3UltrasonicSensor(SensorPort.S2);//TODO check port
	private final List<Integer> distBuffer = new ArrayList<>();
	public List<Integer> getDistBuffer() { return distBuffer;}
	
	Thread flagTask, moveTask, brainTask;
	public Manager() {
		colorListener(colorSensor.getColorID());
		touchListener(false);
		Thread flagTask = new Thread() {
			public void run() {
				if(colorSensor.getColorID() != lastColor)
					colorListener(colorSensor.getColorID());
				if((touchSensor.getCurrentMode()==1)!=touch)
					touchListener((touchSensor.getCurrentMode()==1));
				//distBuffer.add(); TODO
			}
		};
		moveTask = new Thread() {
			private void init() {
				//mG.synchronizeWith(mD);// TODO
				mG.startSynchronization();
			}
			public void run() {
				mG.setSpeed(speedG);
				mD.setSpeed(speedD);
			};
		};
		brainTask = new Thread() {
			public void run() {
				
			}
		};
	}
	public void stard() {
		flagTask.start();
		moveTask.start();
		brainTask.start();
	}
	private void touchListener(boolean b) {
		touch = b;
	}
	private void colorListener(int color) {
		lastColor = color;
		colorBuffer.add(color);
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
