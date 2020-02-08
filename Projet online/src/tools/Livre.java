package tools;
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

/**
 * Classe qui permet de stocker un livre sous forme d'index
 * @author Thierno BAH, Pascal Zheng
 *
 */
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

	/**
	 * Transforme un fichier en index
	 * @param filename nom du fichier a transformer en index
	 * @return l'index sous forme de HashMap ou les clés sont les mots et les valeurs le nombre d'occurence des mots
	 * @throws IOException
	 */
	public static Map<String, Double> liste(String filename) throws IOException {
		return Stream.of(Files.lines(Paths.get(filename), Charset.forName("ISO_8859_1"))).flatMap(s -> s).parallel()
				.map(String::toLowerCase).parallel().map(line -> line.split(" ")).parallel()
				.collect(Collectors.toMap(p -> (String) p[0], p -> Double.parseDouble((String) p[1])));

	}

	/**
	 * Fonctions qui permets de créer un index d'un livre du dossier folderSrc et qui le mets dans le dossier folderDest 
	 * @param files noms des fichiers dont l'index doit être crée
	 * @param folderSrc dossier qui contient les fichiers
	 * @param folderDest dossier où mettre les index créer
	 */
	public static void occurences(ArrayList<String> files, String folderSrc,String folderDest) {
		files.stream().parallel().forEach(file -> {
			try {
				Map<String, Long> res = Stream.of(Files.lines(Paths.get(file), Charset.forName("ISO_8859_1")))
						.flatMap(s -> s).parallel().map(String::toLowerCase).parallel()
						.map(line -> line.split("[\\s,:;!?.]+")).parallel().flatMap(Arrays::stream).parallel()
						.filter(s -> s.matches("[a-zA-Z-']+")).parallel()
						.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

				BufferedWriter writer2 = new BufferedWriter(new FileWriter(file.replace(folderSrc, folderDest)));
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
