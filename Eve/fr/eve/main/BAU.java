package fr.eve.main;

import java.util.concurrent.atomic.AtomicBoolean;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;

public class BAU {
	public static void bau(final Brain brain) {
		BrickFinder.getDefault().getKey(Button.ESCAPE.getName()).addKeyListener(new KeyListener() {
			@Override // boutton arret d'urgance
			public void keyReleased(Key k) {
				System.exit(0);
			}
			@Override
			public void keyPressed(Key k) {}
		});
	}
}
