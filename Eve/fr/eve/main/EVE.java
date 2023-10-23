package fr.eve.main;

import lejos.hardware.BrickFinder;

public class EVE {
	public static void main(String[] args) {
		new EVE().start(); 
	}
	public void start() {
		while(BrickFinder.getDefault().getKeys().waitForAnyPress(1000)==0);
		new Brain();
	}
}
