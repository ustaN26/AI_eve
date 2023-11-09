package fr.eve.main;

public class PremierPalet implements Constantes {
	private Activators a;
	private float rotation;
	private int distance;
	private int vitesse; 

	public PremierPalet() {
		vitesse=mG.getSpeed();
		distance=60;
		rotation=45;
	}
	public void setDistance(int dist){
		distance=dist;
	}
	public void avancer() {
		a.Avancer(distance/vitesse*1000);
	}
	public void ouverturePince() {
		a.ouverturePince(true);
	}
	public void fermeturePince() {
		a.ouverturePince(false);
	}
	public void rotation() {
		a.rotate(rotation);
		rotation=-20;
	}
	public void avancerAvecPinces() {
   //     a.avancerAvecPinces(distance/vitesse*1000);
    }
	 public void avancerJusqua () {
	//	 a.avancerJusqua();
	 }

   
	public static void main(String[] args) {
		final PremierPalet p= new PremierPalet();
		
		 // Thread pour ouvrir les pinces
        Thread ouvrirPincesThread = new Thread(new Runnable() {
            @Override
            public void run() {
                p.ouverturePince();
            }
        });

        // Thread pour avancer avec les pinces ouvertes
        Thread avancerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                p.avancerAvecPinces();
            }
        });

        // Démarrer les deux threads simultanément
        ouvrirPincesThread.start();
        avancerThread.start();

        // Attendre que les threads se terminent
        try {
            ouvrirPincesThread.join();
            avancerThread.join();
        } 
        catch (InterruptedException e) {
            e.printStackTrace();
        }
		p.fermeturePince();
		p.setDistance(120);
		p.rotation();
		p.avancer();
		p.rotation();
		p.avancerJusqua();
		p.ouverturePince();
		
		
	}
	

}
