package fr.eve.main;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public interface Constantes {
	/** moteur pince */
	final EV3LargeRegulatedMotor mP = new EV3LargeRegulatedMotor(MotorPort.A);

	/** moteur roue gauche */
	final EV3LargeRegulatedMotor mG = new EV3LargeRegulatedMotor(MotorPort.B);
	/** moteur roue droite */
	final EV3LargeRegulatedMotor mD = new EV3LargeRegulatedMotor(MotorPort.C);
	/** vitesse max des moteurs */
	final float maxSpeed = 540f;

	/** capteur de touche */
	final EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S2);
	
	/** capteur d'ultrason */
	final EV3UltrasonicSensor usSensor = new EV3UltrasonicSensor(SensorPort.S1);

	/** valeur maximal que le capteur d'ultrason peut donner */
	final float maxUS_detect = 2.55f;
}
