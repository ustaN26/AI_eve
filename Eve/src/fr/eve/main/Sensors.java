package fr.eve.main;

import java.util.ArrayList;
import java.util.List;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.ColorIdentifier;

public class Sensors implements Constantes{
<<<<<<< HEAD
	private final List<Integer> colorBuffer = new ArrayList<>();
	private int lastColor;
	public List<Integer> getColorBuffer() { return colorBuffer;}

	private boolean touch;
	public boolean isTouch() { return touch;}

	private final List<Integer> distBuffer = new ArrayList<>();
	public List<Integer> getDistBuffer() { return distBuffer;}

=======
	private final List<Color> colorBuffer = new ArrayList<>();
	private Color lastColor = Color.BLANC;
	public List<Color> getColorBuffer() { return colorBuffer;}
	
	private boolean touch;
	public boolean isTouch() { return touch;}
	
	private final List<Float> distBuffer = new ArrayList<>();
	public List<Float> getDistBuffer() { return distBuffer;}
	
>>>>>>> a628be30373662a03620d4db60ac53666c65019b
	Thread flagTask, brainTask;
	public Sensors() {
		touchListener(false);
		Thread flagTask = new Thread() {
			public void run() {
				if(Color.fromInt(colorSensor.getColorID()) != lastColor)
					colorListener(Color.fromInt(colorSensor.getColorID()));
				if((touchSensor.getCurrentMode()==1)!=touch)
					touchListener((touchSensor.getCurrentMode()==1));
				//distBuffer.add(); TODO
				try { Thread.sleep(1);
				} catch (InterruptedException ignored) {}
			}
		};
		flagTask.start();
	}
	private void touchListener(boolean b) {
		touch = b;
	}
	private void colorListener(Color color) {
		lastColor = color;
		colorBuffer.add(color);
	}
<<<<<<< HEAD

	public enum Color {
		BLUE(0), WHITE(1), BLACK(2), YELLOW(3), GREEN(4), RED(5), GREY(6);
		private int value;
		private Color (int value) {
			this.value = value;
		}
		public int toInt() {
			return value;
		}
		public static Color fromInt (int value) {
			switch(value) {
			case 0 : return BLUE;
			case 1 : return WHITE;
			case 2 : return BLACK;
			case 3 : return YELLOW;
			case 4 : return GREEN;
			case 5 : return RED;
			default : return GREY;
			}
		}
	}

=======
	public enum Color {

	    BLANC(0), JAUNE(1), ROUGE(2), BLEU(3),VERT(4),NOIR(5),GRIS(6) ;


	    private int value;

	    private Color(int value) {
	        this.value = value;
	    }

	    public int toInt() {
	        return value;   
	    }
	    
	    public static Color fromInt( int value ) {
	        switch(value) {
	            case 0: return BLANC;
	            case 1: return JAUNE;
	            case 2: return ROUGE;
	            case 3: return BLEU;
	            case 4: return VERT;
	            case 5: return NOIR;
	            default: return GRIS;
	        }
	    }
	}    
>>>>>>> a628be30373662a03620d4db60ac53666c65019b
}

