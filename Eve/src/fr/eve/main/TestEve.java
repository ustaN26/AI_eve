package fr.eve.main;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.ColorIdentifier;
import lejos.hardware.BrickFinder;
import lejos.hardware.Sound;
import lejos.hardware.BrickFinder;
import lejos.hardware.motor.EV3LargeRegulatedMotor;


public class TestEve implements Constantes {

	public TestEve() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// dans les positifs ça va forward => Ouvre
		// dans les négatifs ca va backward => Ferme
		mP.rotate(900);
		//BrickFinder.getDefault().getGraphicsLCD().drawString(Float.toString(mP.getPosition()),0,0,0);
		

	}

}
