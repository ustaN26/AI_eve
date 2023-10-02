package fr.eve.main;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public interface Constantes {
	final EV3LargeRegulatedMotor mP = new EV3LargeRegulatedMotor(MotorPort.A);
	
	final EV3LargeRegulatedMotor mG = new EV3LargeRegulatedMotor(MotorPort.C);
	final EV3LargeRegulatedMotor mD = new EV3LargeRegulatedMotor(MotorPort.B);
	
	final EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S2);
	final EV3UltrasonicSensor usSensor = new EV3UltrasonicSensor(SensorPort.S1);
	final EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);
}
