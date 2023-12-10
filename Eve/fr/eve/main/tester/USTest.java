package fr.eve.main.tester;

import fr.eve.main.Sensors;

public class USTest implements Tester {
	private float distDMD;
	private Sensors s;
	
	/**
	 * constructeur
	 * @param dist : distance a atteindre
	 * @param s : objet capteurs
	 */
	public USTest(float dist, Sensors s) {
		distDMD=dist;
		this.s = s;
	}
	
	/**
	 * Test de distance
	 * @return true, if successful
	 */
	@Override
	public boolean test() {
		return (s.getData()*100<distDMD);
	}
}
