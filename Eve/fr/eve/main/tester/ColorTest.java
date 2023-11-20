package fr.eve.main.tester;

import fr.eve.main.Sensors;
import fr.eve.main.Sensors.Color;

public class ColorTest implements Tester{
	private Color arg;
	private Sensors s;
	public ColorTest(Color c,Sensors s) {
		arg = c;
		this.s = s;
	}

	@Override
	public boolean test() {
		System.out.println(s.getColorBuffer());
		return s.getLastColor()==arg;
	}
}
