package jaccarudo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Jaccard {
	public double distanceJaccard(HashMap<String, Integer> D1, HashMap<String, Integer> D2) {
		double sommeEnHaut = 0.0;
		double sommeEnBas = 0.0;
		for (String m : D1.keySet()) {
			if (D2.keySet().contains(m)) {
				int max = Math.max(D1.get(m), D2.get(m));
				int min = Math.min(D1.get(m), D2.get(m));
				sommeEnHaut += max - min;
				sommeEnHaut += max;
			}
		}
		return sommeEnHaut / sommeEnBas;
	}

	public static HashMap<String, Integer> liste(String filename) {
		HashMap<String, Integer> res = new HashMap<>();
		BufferedReader lecteur = null;
		try {
			lecteur = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String ligne = null;
		try {
			ligne = lecteur.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (ligne != null) {
			
		}
		return res;
	}
}
