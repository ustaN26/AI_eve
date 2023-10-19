package fr.eve.mainold;

import fr.eve.main.Activators;
import lejos.utility.Delay;

public class testDeplacementSynch {
	public static void main(String...strings) {
		Activators activators = new Activators();
		activators.synch(true);
		activators.move(true);
		Delay.msDelay(500);
		activators.rotate(15);
		Delay.msDelay(500);
		activators.rotate(-15);
		Delay.msDelay(500);
		activators.rotate(0);
		Delay.msDelay(500);
		activators.stop();
	}
}
