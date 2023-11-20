package fr.eve.main;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;


public interface Constantes {
	final EV3LargeRegulatedMotor mP = new EV3LargeRegulatedMotor(MotorPort.A);
	
	final EV3LargeRegulatedMotor mG = new EV3LargeRegulatedMotor(MotorPort.C);
	final EV3LargeRegulatedMotor mD = new EV3LargeRegulatedMotor(MotorPort.B);
	final float maxSpeed = 600;
	
	final EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S2);
	final EV3UltrasonicSensor usSensor = new EV3UltrasonicSensor(SensorPort.S1);
	final SampleProvider sp = usSensor.getDistanceMode();
	final float [] sample = new float[sp.sampleSize()];
	final EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);
	
	final float maxUltrasonStep = 0.05f;
	final float intervalDetectionCoef = 0.5f;
	final float rayonPalet = 0.03f;
	final int minNumbreOfValForDetect = 3;
	final float maxUS_detect = 2.55f;
}
