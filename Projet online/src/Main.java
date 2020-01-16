import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
		int ITER = 30;
		double TRESHOLD = 0.75;
		int NB_LIVRE = 1664;
		String fileSources = "src/livres";
		String fileSourcesVrac = "/Vrac/livres";
		
		ArrayList<String> files = new ArrayList<>();
		ArrayList<String> files_pretraiter = new ArrayList<>();
		List<Livre> livres = new ArrayList<>();

		BufferedWriter writer = new BufferedWriter(new FileWriter("src/centrality/id_node.txt"));

		try (Stream<Path> paths = Files.walk(Paths.get(fileSourcesVrac))) {
			paths.filter(Files::isRegularFile).limit(NB_LIVRE).forEach(f -> {
				files.add(f.toString());
				files_pretraiter.add(f.toString().replace("livres", "livres_pretraiter"));
				try {
					writer.write(files.size() - 1 + " " + f.toString().split("/")[3] + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}

		writer.close();

		Livre.occurences(files, "livres_pretraiter");

		livres = files_pretraiter.stream().parallel().map(f -> new Livre(f)).collect(Collectors.toList());

		long startTime = System.currentTimeMillis();
		Graph G = new Graph(TRESHOLD, livres);
		long endTime = System.currentTimeMillis();
		System.out.println("Graph creation : That took " + (endTime - startTime) + " milliseconds");
		G.saveGraph("src/centrality/graph.txt");

		BufferedWriter writer2 = new BufferedWriter(new FileWriter("src/centrality/closeness.txt"));

		startTime = System.currentTimeMillis();
		Map<Integer, Double> closeness = Closeness.closeness(G.floydWarshalMat(Jaccard.jaccardMat(livres)));
		endTime = System.currentTimeMillis();
		System.out.println("Closeness : That took " + (endTime - startTime) + " milliseconds");

		for (Integer i : closeness.keySet()) {
			writer2.write(i.intValue() + " " + closeness.get(i).doubleValue() + "\n");
		}
		writer2.close();

		////
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
		suggestionPageRank(G, pagerank);
		///
		suggestionCloseness(G, closeness);

	}

	public static void suggestionPageRank(Graph g, double[] pagerank) {
		ArrayList<ArrayList<Integer>> res = new ArrayList<>();

		g.getAdjArray().keySet().stream().forEach(n -> {
			res.add((ArrayList<Integer>) g.getNeighbor(n).stream().map(v -> {
				return new Paire(v, pagerank[v]);
			}).collect(Collectors.toList()).stream().sorted(Comparator.comparingDouble(Paire::getScore).reversed())
					.limit(5).map(p -> {
						return p.getId();
					}).collect(Collectors.toList()));
		});

		try {
			BufferedWriter writer2 = new BufferedWriter(new FileWriter("src/centrality/suggest_pagerank.txt"));
			String r = "";
			for (int i = 0; i < res.size(); i++) {
				r = String.valueOf(i);
				for (Integer v : res.get(i)) {
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

	public static void suggestionCloseness(Graph g, Map<Integer, Double> closeness) {

		ArrayList<ArrayList<Integer>> res = new ArrayList<>();

		g.getAdjArray().keySet().stream().forEach(n -> {
			res.add((ArrayList<Integer>) g.getNeighbor(n).stream().map(v -> {
				return new Paire(v, closeness.get(v));
			}).collect(Collectors.toList()).stream().sorted(Comparator.comparingDouble(Paire::getScore).reversed())
					.limit(5).map(p -> {
						return p.getId();
					}).collect(Collectors.toList()));
		});

		try {
			BufferedWriter writer2 = new BufferedWriter(new FileWriter("src/centrality/suggest_closeness.txt"));
			String r = "";
			for (int i = 0; i < res.size(); i++) {
				r = String.valueOf(i);
				for (Integer v : res.get(i)) {
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
