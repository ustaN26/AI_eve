package fr.eve.main.tester;

import fr.eve.main.Sensors;

public class TouchTest implements Tester{
	private Sensors s;
	public TouchTest(Sensors s) {
		this.s=s;
	}
	@Override
	public boolean test() {	
		return s.isTouch();
	}
}
