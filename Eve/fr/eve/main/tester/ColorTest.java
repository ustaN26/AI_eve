package fr.eve.main.tester;

import fr.eve.main.Sensors;
import fr.eve.main.Sensors.Color;

public class ColorTest implements Tester{
	private Color arg;
	public ColorTest(Color c) {
		arg = c;
	}

	@Override
	public boolean test() {
		return Sensors.getLastColor()==arg;
	}
}
