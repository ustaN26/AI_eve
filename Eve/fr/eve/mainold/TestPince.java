package fr.eve.mainold;

import lejos.hardware.BrickFinder;
import lejos.hardware.motor.EV3LargeRegulatedMotor;

public class TestPince {

	public static void main(String[] args) {
		/* private static */
		float MIN_ANGLE_PINCE = 0;
		float MAX_ANGLE_PINCE = 0;
		final EV3LargeRegulatedMotor motorPince = new EV3LargeRegulatedMotor(BrickFinder.getDefault().getPort("A"));
		motorPince.setStallThreshold(1, 100);
		motorPince.setSpeed(motorPince.getMaxSpeed());
		while(motorPince.isMoving() || !motorPince.isStalled())motorPince.backward();
		MIN_ANGLE_PINCE = motorPince.getPosition();
		while(!motorPince.isStalled())motorPince.forward();
		MAX_ANGLE_PINCE = motorPince.getPosition();
		while(!motorPince.isStalled())motorPince.backward();
		MIN_ANGLE_PINCE = motorPince.getPosition();

		BrickFinder.getDefault().getGraphicsLCD().drawString(""+MIN_ANGLE_PINCE, 0, 0, 0);
		BrickFinder.getDefault().getGraphicsLCD().drawString(""+MAX_ANGLE_PINCE, 0, 40, 0);
	}
}
