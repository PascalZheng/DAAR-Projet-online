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

	public static void main(String[] args) throws IOException {
		double ALPHA = 0.15;
		int ITER = 30;
		double TRESHOLD = 0.75;
		int NB_LIVRE = 1664;

		String grapheFile = "src/centrality/graph.txt";
		String centralityIdNodeFile = "src/centrality/id_node.txt";
		String centralityClosenessFile = "src/centrality/closeness.txt";
		String centralityPageRankFile = "src/centrality/pagerank.txt";
		String suggestClosenessFile = "src/centrality/suggest_closeness.txt";
		String suggestPageRankFile = "src/centrality/suggest_pagerank.txt";
		String suggestJaccardFile = "src/centrality/suggest_jaccard.txt";

		String fileSources = "src/livres";
		String fileSourcesVrac = "/Vrac/livres";

		String folder = "livres";
		String folderPretraiter = "livres_pretraiter";

		ArrayList<String> files = new ArrayList<>();
		ArrayList<String> files_pretraiter = new ArrayList<>();
		List<Livre> livres = new ArrayList<>();

		try (Stream<Path> paths = Files.walk(Paths.get(fileSources))) {
			paths.filter(Files::isRegularFile).parallel().limit(NB_LIVRE).forEach(f -> {
				files.add(f.toString());
				files_pretraiter.add(f.toString().replace(folder, folderPretraiter));
			});
		}

		Livre.occurences(files, folder, folderPretraiter);

		livres = files_pretraiter.stream().map(f -> new Livre(f)).collect(Collectors.toList());


		BufferedWriter writer = new BufferedWriter(new FileWriter(centralityIdNodeFile));

		for (int id = 0; id < livres.size(); id++) {
			try {
				writer.write(id + " " + livres.get(id).getName().toString().split("\\\\")[2] + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		;

		writer.close();

		long startTime = System.currentTimeMillis();
		Graph G = new Graph(TRESHOLD, livres);
		long endTime = System.currentTimeMillis();
		System.out.println("Graph creation : That took " + (endTime - startTime) + " milliseconds");
		G.saveGraph(grapheFile);

		BufferedWriter writer2 = new BufferedWriter(new FileWriter(centralityClosenessFile));

		startTime = System.currentTimeMillis();
		Map<Integer, Double> closeness = Closeness.closeness(G.floydWarshalMat());
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

		writer2 = new BufferedWriter(new FileWriter(centralityPageRankFile));

		int V = pagerank.length;
		for (int i = 0; i < V; i++) {
			writer2.write(i + " " + pagerank[i] + "\n");

		}
		writer2.close();
		suggestionPageRank(G, pagerank, suggestPageRankFile);
		suggestionCloseness(G, closeness, suggestClosenessFile);
		suggestionJaccard(G, suggestJaccardFile);

	}

	public static void suggestionPageRank(Graph g, double[] pagerank, String filename) {
		ArrayList<ArrayList<Integer>> res = new ArrayList<>();

		g.getAdjArray().keySet().stream().forEach(n -> {
			res.add((ArrayList<Integer>) g.getNeighbor(n).stream().map(v -> {
				return new Paire(v, pagerank[v]);
			}).collect(Collectors.toList()).stream().sorted(Comparator.comparingDouble(Paire::getScore)).map(p -> {
				return p.getId();
			}).limit(5).collect(Collectors.toList()));
		});

		try {
			BufferedWriter writer2 = new BufferedWriter(new FileWriter(filename));
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

	public static void suggestionCloseness(Graph g, Map<Integer, Double> closeness, String filename) {

		ArrayList<ArrayList<Integer>> res = new ArrayList<>();

		g.getAdjArray().keySet().stream().forEach(n -> {
			res.add((ArrayList<Integer>) g.getNeighbor(n).stream().map(v -> {
				return new Paire(v, closeness.get(v));
			}).collect(Collectors.toList()).stream().sorted(Comparator.comparingDouble(Paire::getScore)).map(p -> {
				return p.getId();
			}).limit(5).collect(Collectors.toList()));
		});

		try {
			BufferedWriter writer2 = new BufferedWriter(new FileWriter(filename));
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

	public static void suggestionJaccard(Graph g, String filename) {

		ArrayList<ArrayList<Integer>> res = new ArrayList<>();

		g.getAdjArray().keySet().stream().forEach(n -> {
			res.add((ArrayList<Integer>) g.getNeighbor(n).stream().map(v -> {
				return new Paire(v, g.getJaccardMat()[n][v]);
			}).collect(Collectors.toList()).stream().sorted(Comparator.comparingDouble(Paire::getScore)).map(p -> {
				return p.getId();
			}).limit(5).collect(Collectors.toList()));
		});

		try {
			BufferedWriter writer2 = new BufferedWriter(new FileWriter(filename));
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
