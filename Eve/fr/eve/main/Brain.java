package fr.eve.main;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import fr.eve.main.Detectable.Type;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;

public class Brain implements Constantes{
	private Activators activators;
	private Sensors sensors;

	public Sensors getSensor() {
		return sensors;
	}
	private Thread brainThread;
	private Etats state;

	public Brain() {
		state = Etats.PremierPalet;
		activators = new Activators();
		sensors = new Sensors();
		BrickFinder.getDefault().getKey(Button.ENTER.getName()).addKeyListener(new KeyListener() {
			@Override // boutton arret d'urgance
			public void keyReleased(Key k) {
				System.exit(0);
			}
			@Override
			public void keyPressed(Key k) {}
		});
		brainThread = new Thread() {
			public void run() {
				while(true) {
					switch(state) {
						case PremierPalet:
							premierPalet();
						case MarquerPalet:
							marquerPalet();
							break;
						case AcheminerPalet :
							AcheminerPalet();
							break;
						case DetectPalet :
							detecterPalet();
							break;
					default:
						break;
					}
					try { Thread.sleep(1);
					} catch (InterruptedException ignored) {}	
				}
			}
		};
		brainThread.start();
		while(true);
	}

	public List<Detectable> detect() {
		List<Detectable> ret = new ArrayList<>();
		sensors.getDistBuffer().clear();
		activators.rotationRapide(360);
		List<Float> temp = new ArrayList<>(sensors.getDistBuffer());
		Map<Float,Float> dist_angle_temp = new HashMap<>();
		float anglePerVal = dist_angle_temp.size()/360;
		for(int i = 0;i<sensors.getDistBuffer().size();i++){
			System.out.println("data "+i+" , dist "+temp.get(i));
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
	}
	private static enum Etats {
		PremierPalet,
		MarquerPalet,
		DetectPalet,
		PrendrePalet,
		AcheminerPalet;
	}

    private void avancerjusqua(boolean test) {
        while(test) {
        	try { Thread.sleep(1);
			} catch (InterruptedException ignored) {}
        }
    }
	private void premierPalet() {
		activators.move(true);
    activators.ouverturePince(true);
    avancerjusqua(sensors.isTouch());
    activators.ouverturePince(false);
    activators.rotationRapide(45);
    avancerjusqua(activators.reached(20));
    activators.resetDist();
    activators.rotationRapide(-45);
    avancerjusqua(activators.reached(20));
    activators.resetDist();
    activators.droitDevant();
    avancerjusqua(activators.reached(110));
    activators.resetDist();
    activators.stop();
	}
	protected void marquerPalet() {
		
	}
	private void AcheminerPalet() {
        
	}
	protected void detecterPalet() {
		Detectable palet = Detectable.getClosestPalet(detect());
		
	}
}









