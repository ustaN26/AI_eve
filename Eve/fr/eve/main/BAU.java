package fr.eve.main;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;

/**
 * Class Bouton d'Arret d'Urgence.
 * arrete le programme lorsque ESCAPE est appuye
 */
public class BAU {
	public static void bau() {
		BrickFinder.getDefault().getKey(Button.ESCAPE.getName()).addKeyListener(new KeyListener() {
			@Override // boutton arret d'urgence
			public void keyReleased(Key k) {
				System.exit(0);
			}
			@Override
			public void keyPressed(Key k) {}
		});
	}
}
