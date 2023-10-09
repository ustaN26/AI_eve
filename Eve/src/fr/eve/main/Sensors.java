package fr.eve.main;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.ColorIdentifier;

public class Sensors implements Constantes{
	private final List<Integer> colorBuffer = new ArrayList<>();
	private int lastColor;
	public List<Integer> getColorBuffer() { return colorBuffer;}
	
	private boolean touch;
	public boolean isTouch() { return touch;}
	
	private final List<Integer> distBuffer = new ArrayList<>();
	public List<Integer> getDistBuffer() { return distBuffer;}
	
	Thread flagTask, brainTask;
	public Sensors() {
		colorListener(colorSensor.getColorID());
		touchListener(false);
		Thread flagTask = new Thread() {
			public void run() {
				if(colorSensor.getColorID() != lastColor)
					colorListener(colorSensor.getColorID());
				if((touchSensor.getCurrentMode()==1)!=touch)
					touchListener((touchSensor.getCurrentMode()==1));
				//distBuffer.add(); TODO
				try { Thread.sleep(1);
				} catch (InterruptedException ignored) {}
			}
		};
		flagTask.start();
	}
	private void touchListener(boolean b) {
		touch = b;
	}
	private void colorListener(int color) {
		lastColor = color;
		colorBuffer.add(color);
	}
	
	
}
