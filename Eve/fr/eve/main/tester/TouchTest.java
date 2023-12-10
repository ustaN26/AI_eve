package fr.eve.main.tester;

import fr.eve.main.Sensors;

public class TouchTest implements Tester{
	
	/** objet capteurs */
	private Sensors s;
	
	/**
	 * constructeur
	 * @param s : objet capteurs
	 */
	public TouchTest(Sensors s) {
		this.s=s;
	}
	
	/**
	 * Test de touche
	 * @return true, if successful
	 */
	@Override
	public boolean test() {	
		return s.isTouch();
	}
}
