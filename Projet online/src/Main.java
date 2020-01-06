import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

public class Main {

	public static void printMatrice(double[][] dist) {
		int V = dist.length;

		for (int i = 0; i < V; i++) {
			System.out.print(i + " : ");
			for (int j = 0; j < V; j++) {
				System.out.print(dist[i][j] + "\t");
			}
			System.out.print("\n");
		}
	}

	public static void main(String[] args) throws IOException {
		double ALPHA = 0.15;
		int ITER = 25;
		double TRESHOLD = 0.75;
		ArrayList<String> files = new ArrayList<>();

		BufferedWriter writer = new BufferedWriter(new FileWriter("src/centrality/id_node.txt"));

		try (Stream<Path> paths = Files.walk(Paths.get("src/livres"))) {
			paths.filter(Files::isRegularFile).forEach(f -> {
				files.add(f.toString());
				try {
					writer.write(files.size() - 1 + " : " + f.toString().split("\\\\")[2] + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		writer.close();

		long startTime = System.currentTimeMillis();
		Graph G = new Graph(TRESHOLD, files);
		long endTime = System.currentTimeMillis();
		System.out.println("Graph creation : That took " + (endTime - startTime) + " milliseconds");
		G.saveGraph("src/centrality/graph.txt");

		BufferedWriter writer2 = new BufferedWriter(new FileWriter("src/centrality/closeness.txt"));

		startTime = System.currentTimeMillis();
		Map<Integer, Double> closeness = Closeness.closeness(G.floydWarshalMat(Jaccard.jaccardMat(files)));
		endTime = System.currentTimeMillis();
		System.out.println("Closeness : That took " + (endTime - startTime) + " milliseconds");

		for (Integer i : closeness.keySet()) {
			writer2.write(i.intValue() + " " + closeness.get(i).doubleValue() + "\n");
		}
		writer2.close();

		startTime = System.currentTimeMillis();
		PageRank pageRank = new PageRank(G, ALPHA, ITER);
		pageRank.compute();
		endTime = System.currentTimeMillis();
		System.out.println("Page Rank : That took " + (endTime - startTime) + " milliseconds");

		double[] pagerank = pageRank.rank();

		writer2 = new BufferedWriter(new FileWriter("src/centrality/pagerank.txt"));

		int V = pagerank.length;
		for (int i = 0; i < V; i++) {
			writer2.write(i + " " + pagerank[i] + "\n");

		}
		writer2.close();
	}

}
