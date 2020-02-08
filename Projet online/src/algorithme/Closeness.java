package algorithme;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Algorithme qui calcul le score de closeness de chaque noeuds d'un graphe a partir de la matrice des plus court chemins
 * @author Thierno BAH, Pascal Zheng
 *
 */
public class Closeness {
	public static Map<Integer, Double> closeness(double[][] floydWarshallMat) {
		int n = floydWarshallMat.length;
		Map<Integer, Double> res = new HashMap<Integer, Double>();

		IntStream.range(0, n).forEach(i -> {
			double s = (Arrays.stream(floydWarshallMat[i]).parallel().sum());
			if (s == 0) {
				res.put(i, 0.0);
			} else {
				res.put(i, n / s);
			}
		});

		return res;
	}
}
