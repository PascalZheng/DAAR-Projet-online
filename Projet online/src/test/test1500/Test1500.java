package test.test1500;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import algorithme.Closeness;
import algorithme.Graph;
import algorithme.Main;
import algorithme.PageRank;
import tools.Livre;
/**
 * Mesure de temps pour un graphe de taille 1500
 * @author Thierno
 *
 */
public class Test1500 {

	public static void main(String[] args) throws IOException {
		double ALPHA = 0.15;
		int ITER = 30;
		double TRESHOLD = 0.75;
		
		int NB_LIVRE = 1500;
		
		String test = "src/test/test"+NB_LIVRE+"/";
		System.out.println(test);
		String testResultFile = test+ "result.txt";
		String grapheFile = test+ "graph.txt";
		String centralityIdNodeFile = test+ "id_node.txt";
		String centralityClosenessFile = test+ "closeness.txt";
		String centralityPageRankFile = test+ "pagerank.txt";
		String suggestClosenessFile = test+ "suggest_closeness.txt";
		String suggestPageRankFile = test+ "suggest_pagerank.txt";
		String suggestJaccardFile = test+ "suggest_jaccard.txt";
		
		
		String fileSourcesVrac = "/Vrac/livres";
		
		String folder = "livres";
		String folderPretraiter = "livres_pretraiter";

		
		ArrayList<String> files = new ArrayList<>();
		ArrayList<String> files_pretraiter = new ArrayList<>();
		
		List<Livre> livres = new ArrayList<>();
		
		try (Stream<Path> paths = Files.walk(Paths.get(fileSourcesVrac))) {
			paths.filter(Files::isRegularFile).parallel().limit(NB_LIVRE).forEach(f -> {
				files.add(f.toString());
				files_pretraiter.add(f.toString().replace(folder, folderPretraiter));
			});
		}
		
		livres = files_pretraiter.stream().map(f -> new Livre(f)).collect(Collectors.toList());


		BufferedWriter writer = new BufferedWriter(new FileWriter(centralityIdNodeFile));

		for (int id = 0; id < livres.size(); id++) {
			try {
				writer.write(id + " " + livres.get(id).getName().toString().split("/")[3] + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		;

		writer.close();

		long startTime = System.currentTimeMillis();
		Graph G = new Graph(TRESHOLD, livres);
		long endTime = System.currentTimeMillis();
		
		long totalTime = endTime - startTime;
		String grapheCreationTime = "Graph creation : That took " + totalTime + " milliseconds";
		System.out.println(grapheCreationTime);
		G.saveGraph(grapheFile);

		BufferedWriter writer2 = new BufferedWriter(new FileWriter(centralityClosenessFile));

		startTime = System.currentTimeMillis();
		Map<Integer, Double> closeness = Closeness.closeness(G.floydWarshalMat());
		endTime = System.currentTimeMillis();
		
		long totalTime1 = endTime - startTime;
		String closenessTime = "Closeness : That took " + totalTime1 + " milliseconds";
		System.out.println(closenessTime);

		for (Integer i : closeness.keySet()) {
			writer2.write(i.intValue() + " " + closeness.get(i).doubleValue() + "\n");
		}
		writer2.close();

		startTime = System.currentTimeMillis();
		PageRank pageRank = new PageRank(G, ALPHA, ITER);
		pageRank.compute();
		endTime = System.currentTimeMillis();
		
		long totalTime2 = endTime - startTime;
		String pagerankTime = "Page Rank : That took " + totalTime2 + " milliseconds";
		System.out.println(pagerankTime);

		double[] pagerank = pageRank.rank();

		writer2 = new BufferedWriter(new FileWriter(centralityPageRankFile));

		int V = pagerank.length;
		for (int i = 0; i < V; i++) {
			writer2.write(i + " " + pagerank[i] + "\n");
		}
		writer2.close();
		
		writer2 = new BufferedWriter(new FileWriter(testResultFile));
		writer2.write(grapheCreationTime + "\n");
		writer2.write(closenessTime + "\n");
		writer2.write(pagerankTime + "\n");
		writer2.close();
		
		Main.suggestionPageRank(G, pagerank, suggestPageRankFile);
		Main.suggestionCloseness(G, closeness, suggestClosenessFile);
		Main.suggestionJaccard(G, suggestJaccardFile);


	}

}
