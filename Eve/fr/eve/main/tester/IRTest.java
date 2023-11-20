package fr.eve.main.tester;
import fr.eve.main.Sensors;
public class IRTest implements Tester {
	private float distDMD;
	private Sensors s;
	
	public IRTest(float dist, Sensors s2) {
		distDMD=dist;
		s = s2;
	}
	
	

	@Override
	public boolean test() {
		return (s.getData()>distDMD);
		//demander au sensor quelle distance c'est (distance detectée plus petite que demandée)
	}

}
