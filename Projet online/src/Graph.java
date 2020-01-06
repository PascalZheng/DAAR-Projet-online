import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Graph {
	// Graph avec distance de Jaccard use la liste d'adjence pour le rpz

	private double edgeTreshold;
	private Map<Integer, HashSet<Integer>> adjArray;

	public Graph(double edgeThreshold, ArrayList<String> files) throws IOException {
		this.edgeTreshold = edgeThreshold;

		int n = files.size();
		adjArray = new HashMap<>();
		for (int i = 0; i < n; i++) {
			adjArray.put(i, new HashSet<Integer>());
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i != j) {
					if (Jaccard.distanceJaccard(files.get(i), files.get(j)) <= this.edgeTreshold) {
						adjArray.get(i).add(j);
						adjArray.get(j).add(i);
					}
				}
			}
		}
	}

	public double[][] floydWarshalMat(double[][] jaccardMat) {
		int n = this.adjArray.keySet().size();
		int[][] paths = new int[n][n];
		double[][] dist = new double[n][n];

		for (int i = 0; i < paths.length; i++)
			for (int j = 0; j < paths.length; j++)
				paths[i][j] = i;

		for (int i = 0; i < paths.length; i++) {
			for (int j = 0; j < paths.length; j++) {
				if (i == j) {
					dist[i][i] = 0.0;
					continue;
				}
				if (getNeighbor(i).contains(j)) {
					dist[i][j] = jaccardMat[i][j];

				} else {
					dist[i][j] = Double.POSITIVE_INFINITY;
				}
				paths[i][j] = j;
			}
		}

		for (int k = 0; k < paths.length; k++) {
			for (int i = 0; i < paths.length; i++) {
				for (int j = 0; j < paths.length; j++) {
					if (dist[i][j] > dist[i][k] + dist[k][j]) {
						dist[i][j] = dist[i][k] + dist[k][j];
						paths[i][j] = paths[i][k];

					}
				}
			}
		}

		return dist;
	}

	public HashSet<Integer> getNeighbor(Integer i) {
		return adjArray.get(i);
	}

	public Map<Integer, HashSet<Integer>> getAdjArray() {
		return adjArray;
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
			for (Integer i : adjArray.keySet()) {
				writer2.write(i.toString());
				adjArray.get(i).stream().forEach(v -> {
					try {
						writer2.write(" " + v);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				writer2.write("\n");
			}

			writer2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
