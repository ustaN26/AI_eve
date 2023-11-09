package fr.eve.main;

import java.util.List;
import java.util.Map.Entry;

public class Detectable {
	private Type type;
	private float distance;
	private float angle;

	public Detectable(){
		this.type = Type.ignored;
	}
	public void setAD(Entry<Float, Float> min) {
		angle = min.getKey();
		distance = min.getValue();
	}
	public void setType(Type type) {
		this.type = type;
	}
	public float getDistance() {
		return distance;
	}
	public float getAngle() {
		return angle;
	}
	public Type getType() {
		return type;
	}
	public String toString() {
		return type+" "+angle+"° "+distance*100+"cm";
	}
	public enum Type {
		mur, 
		palet,
		robot,
		ignored
	}
	public static Detectable getClosestPalet(List<Detectable> detect) {
		float minDist = 1000;
		Detectable r = null;
		for(Detectable d : detect) {
			if(d.type == Type.palet && d.distance<minDist) {
				r = d;
				minDist = d.distance;
			}
		}
		return r;
	}
}


