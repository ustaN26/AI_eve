package fr.eve.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Brain implements Constantes{
	private Activators activators;
	private Sensors sensors;
	private Thread brainThread;
	
	public Brain() {
		activators = new Activators();
		sensors = new Sensors();
		
		brainThread = new Thread() {
			public void run() {
				
				try { Thread.sleep(1);
				} catch (InterruptedException ignored) {}
			}
		};
		brainThread.start();
	}
	
	private Map<Detectable,Integer> detect() {
		Map<Detectable,Integer> ret = new HashMap<>();
		sensors.getDistBuffer().clear();
		activators.rotationRapide(360);
		List<Integer> temp = sensors.getDistBuffer();
		List<List<Integer>> packets = new ArrayList<>();
		int last = 0;
		boolean newPacket = true;
		packets.add(new ArrayList<Integer>());
		for (int i : temp)
			if (i==1000)//TODO infinie
				newPacket=true;
			else if(Math.abs(last-i) < maxUltrasonStep) {
				if(newPacket)
					packets.add(new ArrayList<Integer>());
				packets.get(packets.size()).add(i);
				newPacket=false;
			} else { //objet detecté sur un autre plan
				packets.add(new ArrayList<Integer>());
				packets.get(packets.size()).add(i);
				newPacket=false;
			}
		for(List<Integer> packet : packets) {
			Detectable det = whatIs(packet);
			if(det!=Detectable.ignored)
				ret.put(det,min(packet));
		}
		return ret;
	}

	private Detectable whatIs(List<Integer> packet) {
		Detectable ret = Detectable.mur;
		float coef = 0,tempCoef;
		int last = 1;
		if(packet.size()<minNumbreOfValForDetect)
			return Detectable.ignored;
		for (int i: packet) { //test linear/mur
			tempCoef = i/last;
			last=i;
			if(coef!=0) {
				if(tempCoef>(coef+intervalDetectionCoef) ||
						tempCoef<(coef-intervalDetectionCoef) ) {
					ret = Detectable.pallet;
					break;
				}
			}else coef = tempCoef;
		}
		for (int i: packet) {//test pallet
			
			/*if(pas pallet) {
				ret = Detectable.robot;
				break;
			}*/
		}
		return ret;
	}
	private Integer min(List<Integer> packet) {
		int min = 1000;
		for (int i: packet) {
			if(i<min) min = i;
		}
		return null;
	}
}









