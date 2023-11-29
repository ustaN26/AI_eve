package fr.eve.main.tester;

import fr.eve.main.Activators;

public class DistanceTest implements Tester {
	private int dist;
	private Activators a;
	
	public DistanceTest(int dist, Activators a) {
		a.resetDist();
		this.dist = dist;
		this.a = a;
	}

	@Override
	public boolean test() {
		return a.reached(dist);
	}
}
