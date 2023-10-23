package fr.eve.main.test;

import fr.eve.main.Brain;
import fr.eve.main.Detectable;

public class TestDetect {
	public static void main(String[] args) {
		new TestDetect().start(); 
	}
	public void start() {
		Brain b = new Brain();
		for (Detectable d : b.detect()) {
			System.out.println(d);
		}
	}
}
