package fr.eve.main.test;

import fr.eve.main.Sensors;
import fr.eve.main.tester.Tester;
import fr.eve.main.tester.TouchTest;
import fr.eve.main.tester.USTest;

public class TestAvancerJusqua {
	public static void main(String[] args) {
		/*Sensors sensors = new Sensors();
	    avancerjusqua(new USTest(30, sensors));//(0.18m))
		System.out.println("detect 30cm");*/
	}
	public static void avancerjusqua(Tester t) {
        while(!t.test()) {
        	try { Thread.sleep(100);
			} catch (InterruptedException ignored) {}
        }
    }
}
