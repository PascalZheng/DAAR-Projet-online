package algorithme;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import tools.Livre;

/**
 * Classe qui permet de creer un graphe géométrique en utilisant comme distance la distance de Jaccard
 * @author Thierno BAH, Pascal ZHENG
 *
 */
public class Graph {
	private double edgeTreshold;
	private Map<Integer, HashSet<Integer>> adjArray;
	private double[][] jaccardMat;

	public Graph(double edgeThreshold, List<Livre> files) throws IOException {
		this.edgeTreshold = edgeThreshold;
		int n = files.size();
		adjArray = new HashMap<>();
		jaccardMat = new double[files.size()][files.size()];

		IntStream.range(0, n).forEach(i -> {
			adjArray.put(i, new HashSet<Integer>());
		});

		IntStream.range(0, n).parallel().forEach(i -> {
			IntStream.range(0, n).parallel().forEach(j -> {
				double distJacc = 1.0;
				try {
					if (i == j) {
						distJacc = 0.0;
					} else {
						distJacc = Jaccard.distanceJaccard(files.get(i), files.get(j));
					}
					jaccardMat[i][j] = distJacc;
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (distJacc <= this.edgeTreshold && i != j) {
					adjArray.get(i).add(j);
					adjArray.get(j).add(i);
				}
			});
		});
	}

	public double[][] floydWarshalMat() {
		int n = this.adjArray.keySet().size();
		int[][] paths = new int[n][n];
		double[][] dist = new double[n][n];

		IntStream.range(0, n).forEach(i -> {
			IntStream.range(0, n).forEach(j -> {
				paths[i][j] = i;
			});
		});

		IntStream.range(0, n).parallel().forEach(i -> {
			IntStream.range(0, n).parallel().forEach(j -> {
				if (i == j) {
					dist[i][i] = 0.0;
				} else {
					if (getNeighbor(i).contains(j)) {
						dist[i][j] = jaccardMat[i][j];
					} else {
						dist[i][j] = 1.0;
					}
					paths[i][j] = j;
				}

			});
		});

		IntStream.range(0, n).parallel().forEach(k -> {
			IntStream.range(0, n).parallel().forEach(i -> {
				IntStream.range(0, n).parallel().forEach(j -> {
					if (dist[i][j] > dist[i][k] + dist[k][j]) {
						dist[i][j] = dist[i][k] + dist[k][j];
						paths[i][j] = paths[i][k];
					}

				});

			});
		});

		return dist;
	}

	public HashSet<Integer> getNeighbor(Integer i) {
		return adjArray.get(i);
	}

	public Map<Integer, HashSet<Integer>> getAdjArray() {
		return adjArray;
	}

	public double[][] getJaccardMat() {
		return jaccardMat;
	}

	public void setJaccardMat(double[][] jaccardMat) {
		this.jaccardMat = jaccardMat;
	}

	public int getDegreeNode(Integer i) {
		return adjArray.get(i).size();
	}

	public int size() {
		return adjArray.size();
	}

	public void saveGraph(String filename) {
		try {
			BufferedWriter writer2 = new BufferedWriter(new FileWriter(filename));
			String r = "";
			for (Integer i : adjArray.keySet()) {
				r = i.toString();
				for (Integer v : adjArray.get(i)) {
					r += " " + v.toString();
				}
				r += "\n";
				writer2.write(r);
				r = "";
			}
			writer2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
