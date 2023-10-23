package fr.eve.main.test;

import java.util.List;

import fr.eve.main.Brain;
import fr.eve.main.Detectable;
import lejos.utility.Delay;

public class TestDetect {
	public static void main(String[] args) {
		new TestDetect().start(); 
	}
	public void start() {
		Brain b = new Brain();
		List<Detectable> ds = b.detect();
		for (Detectable d : ds) {
			System.out.println(d);
		}
		Delay.msDelay(5000);
	}
}
