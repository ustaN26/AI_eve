package fr.eve.main;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import fr.eve.main.Detectable.Type;

public class Brain implements Constantes{
	private Activators activators;
	private Sensors sensors;
	private Thread brainThread;
	private Etats state;

	public Brain() {
		state = Etats.AcheminerPallet;
		activators = new Activators();
		sensors = new Sensors();

		brainThread = new Thread() {
			public void run() {
<<<<<<< HEAD
				detectPalet();
=======
				switch(state) {
					case AcheminerPallet :
						break;
					case RecherchePallet :
						break;
				}
>>>>>>> a628be30373662a03620d4db60ac53666c65019b
				try { Thread.sleep(1);
				} catch (InterruptedException ignored) {}
			}
		};
		brainThread.start();
	}
	
<<<<<<< HEAD
	public void detectPalet() {
		sensors.getDistBuffer().clear();
		
=======
	public List<Detectable> detect() {
		List<Detectable> ret = new ArrayList<>();
		sensors.getDistBuffer().clear();
		activators.rotationRapide(800);
		List<Float> temp = new ArrayList<>(sensors.getDistBuffer());
		Map<Float,Float> dist_angle_temp = new HashMap<>();
		float anglePerVal = dist_angle_temp.size()/360;
		for(int i = 0;i<sensors.getDistBuffer().size();i++){
			dist_angle_temp.put(i*anglePerVal,temp.get(i));
		}
		final List<Map<Float,Float>> packets = new ArrayList<>();
		int last = 0;
		boolean newPacket = false;
		for(Entry<Float, Float> e : dist_angle_temp.entrySet()) {
			if (e.getValue()>maxUS_detect)
				newPacket=true;
			else if(Math.abs(last-e.getValue()) < maxUltrasonStep) {
				if(newPacket)
					packets.add(new HashMap<Float,Float>());//premiere detection
				packets.get(packets.size()).put(e.getKey(), e.getValue());
				newPacket=false;
			} else { //objet detecté sur un autre plan
				packets.add(new HashMap<Float,Float>());
				packets.get(packets.size()).put(e.getKey(), e.getValue());
				newPacket=false;
			}
		}
		for(Map<Float,Float> packet : packets) {
			Detectable det = whatIs(packet);
			if(det.getType()!=Type.ignored)
				ret.add(det);
		}
		return ret;
	}

	private Detectable whatIs(Map<Float,Float> packet) {
		Detectable ret = new Detectable();
		if(packet.size()<minNumbreOfValForDetect)return ret;
		Float coef = 0f,tempCoef;
		Float last = packet.get(0f);
		final Entry<Float, Float> min = min(packet);
		ret.setAD(min);
		packet.remove(0f);
		boolean decroissant = true;
		for(Float dist : packet.values()) {
				tempCoef=(dist/last);
				last=dist;
				if(coef!=0f) {
					if(tempCoef>(coef+intervalDetectionCoef) ||
							tempCoef<(coef-intervalDetectionCoef) ) {
						ret.setType(Type.palet);
						break;
					}
				}else coef=tempCoef;
		}
		if(ret.getType()!=Type.palet) return ret;
		for(Float dist : packet.values()) {
					if(dist>(min.getValue() + rayonPalet))
					ret.setType(Type.robot);
				if(decroissant){
					if(dist>last)
						decroissant=false;
				}else {
					if(dist<last)
						ret.setType(Type.robot);
				}
				last=dist;
			}
		return ret;
	}
	
	private Entry<Float,Float> min(Map<Float,Float> packet) {
		Entry<Float,Float> min = new AbstractMap.SimpleEntry<>(3f, 0f);//2.55 max);
		for(Entry<Float, Float> e : packet.entrySet()) {
			if(e.getValue()<min.getValue())min=e;
		}
		return min;
>>>>>>> a628be30373662a03620d4db60ac53666c65019b
	}
}









