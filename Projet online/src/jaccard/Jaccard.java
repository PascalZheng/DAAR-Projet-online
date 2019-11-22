package jaccard;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Jaccard {
	public static double distanceJaccard(Map<String, Long> D1, Map<String, Long> D2) {
		double sommeEnHaut = 0.0;
		double sommeEnBas = 0.0;

		sommeEnHaut = D1.keySet().stream().collect(Collectors.toList()).stream().map(
				k -> D2.get(k) != null ? Math.max(D1.get(k), D2.get(k)) - Math.min(D1.get(k), D2.get(k)) : D1.get(k))
				.collect(Collectors.toList()).stream().mapToLong(Long::longValue).sum();

		sommeEnHaut += D2.keySet().stream().filter(k -> !D1.containsKey(k)).collect(Collectors.toList()).stream()
				.map(k -> D2.get(k)).collect(Collectors.toList()).stream().mapToLong(Long::longValue).sum();

		sommeEnBas = D1.keySet().stream().collect(Collectors.toList()).stream()
				.map(k -> D2.get(k) != null ? Math.max(D1.get(k), D2.get(k)) : D1.get(k)).collect(Collectors.toList())
				.stream().mapToLong(Long::longValue).sum();

		sommeEnBas += D2.keySet().stream().filter(k -> !D1.containsKey(k)).collect(Collectors.toList()).stream()
				.map(k -> D2.get(k)).collect(Collectors.toList()).stream().mapToLong(Long::longValue).sum();

		return (sommeEnBas == 0 ? 1.0 : sommeEnHaut / sommeEnBas);
	}

	public static Map<String, Long> liste(String filename) throws IOException {
		return Files.lines(Paths.get(filename), Charset.forName("ISO_8859_1")).map(String::toLowerCase)
				.map(line -> line.split("[\\s,:;!?.]+")).flatMap(Arrays::stream).filter(s -> s.matches("[a-zA-z-']+"))
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}

	public static Map<String, Double> closenessCentrality(ArrayList<String> files) throws IOException {
		double ctr = 0.0;
		Map<String, Double> res = new HashMap<String, Double>();
		for (String f1 : files) {
			ctr = 0.0;
			for (String f2 : files) {
				if (!f1.equals(f2)) {
					ctr += distanceJaccard(liste(f1), liste(f2));
				}
			}
			res.put(f1, 1.0 / ctr);
		}
		return res;
	}

	public static void main(String[] args) throws IOException {
		ArrayList<String> files = new ArrayList<>();
		files.add("src/files/s.txt");
		files.add("src/files/u.txt");
		files.add("src/files/v.txt");
		files.add("src/files/w.txt");
		
		Map<String, Double> a = closenessCentrality(files);
		for(String k  :  a.keySet()) {
			System.out.println(k+" : "+a.get(k));
		}

//		System.out.println(distanceJaccard(
//				liste("\\Users\\Thierno\\Documents\\Git\\DAAR-Projet-online\\Projet online\\src\\files\\w.txt"),
//				liste("\\Users\\Thierno\\Documents\\Git\\DAAR-Projet-online\\Projet online\\src\\files\\s.txt")));

	}
}
