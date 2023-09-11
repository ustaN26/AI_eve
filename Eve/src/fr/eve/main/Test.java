package fr.eve.main;
import lejos.*;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Motor.B.forward();

		Motor.C.forward();
		Delay.msDelay(5000);
		Motor.B.stop();
		Motor.C.stop();
	}

}
