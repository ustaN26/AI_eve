package fr.eve.main.tester;
import fr.eve.main.Sensors;
public class USTest implements Tester {
	private float distDMD;
	private Sensors s;
	
	public USTest(float dist, Sensors s2) {
		distDMD=dist;
		s = s2;
	}
	
	@Override
	public boolean test() {
		return (s.getData()*100<distDMD);
		//demander au sensor us quelle distance c'est (distance detectée plus petite que demandée)
	}
}
