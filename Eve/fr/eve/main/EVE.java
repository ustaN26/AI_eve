package fr.eve.main;

import java.util.concurrent.atomic.AtomicBoolean;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;

public class EVE {
	public static void main(String[] args) {
		start();
		new EVE().run(); 
	}
	public void run() {
		new Brain();
	}
	public static void start() {
		final AtomicBoolean s = new AtomicBoolean(false);
		System.out.println("press enter to start!");
		BrickFinder.getDefault().getKey(Button.ENTER.getName()).addKeyListener(new KeyListener() {
			@Override // boutton arret d'urgance
			public void keyReleased(Key k) {
				s.set(true);
			}
			@Override
			public void keyPressed(Key k) {}
		});
		while(!s.get()) {;}
		System.out.println("GO GO GO!!!");
	}
}
