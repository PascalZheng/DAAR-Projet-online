
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Jaccard {

	public static double distanceJaccard(String f1, String f2) throws IOException {
		Map<String, Double> D1 = liste(f1);
		Map<String, Double> D2 = liste(f2);

		double sommeEnHaut = 0.0;
		double sommeEnBas = 0.0;

		sommeEnHaut = D1.keySet().parallelStream().collect(Collectors.toList()).stream().map(
				k -> D2.get(k) != null ? Math.max(D1.get(k), D2.get(k)) - Math.min(D1.get(k), D2.get(k)) : D1.get(k))
				.collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).sum();

		sommeEnHaut += D2.keySet().parallelStream().filter(k -> !D1.containsKey(k)).collect(Collectors.toList())
				.stream().map(k -> D2.get(k)).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue)
				.sum();

		sommeEnBas = D1.keySet().parallelStream().collect(Collectors.toList()).stream()
				.map(k -> D2.get(k) != null ? Math.max(D1.get(k), D2.get(k)) : D1.get(k)).collect(Collectors.toList())
				.stream().mapToDouble(Double::doubleValue).sum();

		sommeEnBas += D2.keySet().parallelStream().filter(k -> !D1.containsKey(k)).collect(Collectors.toList()).stream()
				.map(k -> D2.get(k)).collect(Collectors.toList()).stream().mapToDouble(Double::doubleValue).sum();

		return (sommeEnBas == 0 ? 1.0 : sommeEnHaut / sommeEnBas);
	}

	// le filter double le temps d'execution
	public static Map<String, Double> liste(String filename) throws IOException {
		return Stream.of(Files.lines(Paths.get(filename), Charset.forName("ISO_8859_1"))).flatMap(s -> s).parallel()
				.map(String::toLowerCase).map(line -> line.split(" "))
				.collect(Collectors.toMap(p -> (String) p[0], p -> Double.parseDouble((String) p[1])));

	}

	public static void occurences(ArrayList<String> files) {
		files.stream().parallel().forEach(file -> {
			try {
				Map<String, Long> res = Stream.of(Files.lines(Paths.get(file), Charset.forName("ISO_8859_1")))
						.flatMap(s -> s).parallel().map(String::toLowerCase).map(line -> line.split("[\\s,:;!?.]+"))
						.flatMap(Arrays::stream).filter(s -> s.matches("[a-zA-Z-']+"))
						.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

				BufferedWriter writer2 = new BufferedWriter(
						new FileWriter(file.replace("livres", "livres_pretraiter")));
				for (String mot : res.keySet()) {
					writer2.write(mot + " " + res.get(mot).doubleValue() + "\n");
				}
				writer2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

	public static double[][] jaccardMat(ArrayList<String> files) throws IOException {
		double[][] res = new double[files.size()][files.size()];
		int n = res.length;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j) {
					res[i][j] = 0.0;
				} else {
					res[i][j] = distanceJaccard(files.get(i), files.get(j));
				}
			}
		}
		return res;
	}

}
