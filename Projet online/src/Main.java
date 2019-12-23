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
		ArrayList<String> files = new ArrayList<>();
		
		try (Stream<Path> paths = Files.walk(Paths.get("src/livres"))) {
		    paths
		        .filter(Files::isRegularFile)
		        .forEach(f -> files.add(f.toString()));
		} 


		long startTime = System.currentTimeMillis();

		Map<Integer, Double> res = Closeness.closeness(Closeness.floydWarshall(Jaccard.jaccardMat(files)));

		long endTime = System.currentTimeMillis();

		System.out.println("That took " + (endTime - startTime) + " milliseconds");

		System.out.println(res);

	}

}
