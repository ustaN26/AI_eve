package fr.eve.main.test;

import fr.eve.main.Activators;
import fr.eve.main.Sensors;
import fr.eve.main.Sensors.Color;

public class TestColo {

	public TestColo() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String... strings) {
		Sensors s = new Sensors();
		Activators a = new Activators();
		a.Avancer(10);
		for (Color c : s.getColorBuffer()) {
			System.out.println(c.name());
		}
	}
 
}
