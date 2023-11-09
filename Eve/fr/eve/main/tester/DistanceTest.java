package fr.eve.main.tester;

import fr.eve.main.Activators;

public class DistanceTest implements Tester{
	private int dist;
	private Activators a;
	
	public DistanceTest(int dist, Activators a) {
		this.dist = dist;
		this.a = a;
	}

	@Override
	public boolean test() {
		boolean r = a.reached(dist);//a ajuster
		System.out.println(a.dist()+"  "+a.mG.getTachoCount());
		return r;
	}

}
