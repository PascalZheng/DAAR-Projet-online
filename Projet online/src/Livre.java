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

public class Livre {

	private String name;
	private Map<String, Double> occurencesMots;

	public Livre(String name) {
		this.name = name;
		try {
			this.occurencesMots = liste(name);
		} catch (IOException e) {
			System.out.println("erreur pour charger le fichier pretraiter : " + name);
			e.printStackTrace();
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Double> getOccurencesMots() {
		return occurencesMots;
	}

	public void setOccurencesMots(Map<String, Double> occurencesMots) {
		this.occurencesMots = occurencesMots;
	}

	public static Map<String, Double> liste(String filename) throws IOException {
		return Stream.of(Files.lines(Paths.get(filename), Charset.forName("ISO_8859_1")))
				.flatMap(s -> s).parallel()
				.map(String::toLowerCase).parallel()
				.map(line -> line.split(" ")).parallel()
				.collect(Collectors.toMap(p -> (String) p[0], p -> Double.parseDouble((String) p[1])));

	}

	public static void occurences(ArrayList<String> files, String folder) {
		files.stream().parallel()
		.forEach(file -> {
			try {
				Map<String, Long> res = Stream.of(Files.lines(Paths.get(file), Charset.forName("ISO_8859_1")))
						.flatMap(s -> s).parallel()
						.map(String::toLowerCase).parallel()
						.map(line -> line.split("[\\s,:;!?.]+")).parallel()
						.flatMap(Arrays::stream).parallel()
						.filter(s -> s.matches("[a-zA-Z-']+")).parallel()
						.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

				BufferedWriter writer2 = new BufferedWriter(new FileWriter(file.replace("livres", folder)));
				for (String mot : res.keySet()) {
					writer2.write(mot + " " + res.get(mot).doubleValue() + "\n");
				}
				writer2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

}
