package fr.eve.main.tester;
import fr.eve.main.Sensors;
import fr.eve.main.Brain;
public class IRTest implements Tester {
	private float distDMD;
	private Sensors s;
	private Brain b;
	private boolean bigMur;
	
	public IRTest(float dist) {
		distDMD=dist;
		s = new Sensors();
		bigMur=s.getData()>distDMD;
	}
	
	

	@Override
	public boolean test() {
		b.avancerjusqua(bigMur);
		
		return (bigMur);
		//demander au sensor quelle distance c'est (distance detectée plus petite que demandée)
	}
	
	

}
