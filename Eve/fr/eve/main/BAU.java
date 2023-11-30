package fr.eve.main;

import java.util.concurrent.atomic.AtomicBoolean;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;

public class BAU {
	public static void bau(final Brain brain) {
		BrickFinder.getDefault().getGraphicsLCD().clear();
		System.out.println("press enter to pause");
		System.out.println("press escape to stop");
		BrickFinder.getDefault().getKey(Button.ENTER.getName()).addKeyListener(new KeyListener() {
			@Override // boutton pause
			public void keyReleased(Key k) {
				pause(brain);
				BrickFinder.getDefault().getGraphicsLCD().clear();
				System.out.println("press enter to pause");
				System.out.println("press escape to stop");
			}
			@Override
			public void keyPressed(Key k) {}
		});
		BrickFinder.getDefault().getKey(Button.ESCAPE.getName()).addKeyListener(new KeyListener() {
			@Override // boutton arret d'urgance
			public void keyReleased(Key k) {
				System.exit(0);
			}
			@Override
			public void keyPressed(Key k) {}
		});
	}

	protected static void pause(Brain brain) {
		brain.getThread().suspend();
		long timePause = System.currentTimeMillis();
		brain.getSensor().getThread().suspend();
		final AtomicBoolean resume = new AtomicBoolean(false);
		System.out.println("enter = resume / escape = stop");
		BrickFinder.getDefault().getKey(Button.ENTER.getName()).addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(Key k) {
				resume.set(true);
			}
			@Override
			public void keyPressed(Key k) {}
		});
		BrickFinder.getDefault().getKey(Button.ESCAPE.getName()).addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(Key k) {
				System.exit(0);
			}
			@Override
			public void keyPressed(Key k) {}
		});
		while(!resume.get()) {
			try { Thread.sleep(1);
			} catch (InterruptedException ignored) {}
		}
		brain.getSensor().addPauseTime(System.currentTimeMillis()-timePause);
		brain.getSensor().getThread().resume();
		brain.getThread().resume();
	}
}
