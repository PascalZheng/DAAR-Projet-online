package jaccarudo;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Jaccard {
	public static double distanceJaccard(Map<String, Long> D1, Map<String, Long> D2) {
		double sommeEnHaut = 0.0;
		double sommeEnBas = 0.0;
		sommeEnHaut = D1.keySet().stream().filter(D2.keySet()::contains).collect(Collectors.toList()).stream()
				.map(k -> Math.max(D1.get(k), D2.get(k)) - Math.min(D1.get(k), D2.get(k))).collect(Collectors.toList())
				.stream().mapToLong(Long::longValue).sum();
		sommeEnBas = D1.keySet().stream().filter(D2.keySet()::contains).collect(Collectors.toList()).stream()
				.map(k -> Math.max(D1.get(k), D2.get(k))).collect(Collectors.toList()).stream()
				.mapToLong(Long::longValue).sum();
		return (sommeEnBas == 0 ? Long.MAX_VALUE : sommeEnHaut / sommeEnBas);
	}

	public static Map<String, Long> liste(String filename) throws IOException {
		return Files.lines(Paths.get(filename), Charset.forName("ISO_8859_1")).map(String::toLowerCase)
				.map(line -> line.split("[\\s,:;!?.]+")).flatMap(Arrays::stream).filter(s -> s.matches("[a-zA-z-']+"))
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}

	public static Map<String, Double> closenessCentrality(ArrayList<String> files) throws IOException{
		double ctr = 0.0;
		Map<String, Double> res = new HashMap<String, Double>();
		for(String f1 : files) {
			ctr = 0.0;
			for(String f2 : files) {
				if(!f1.equals(f2)) {
					ctr+=distanceJaccard(liste(f1),liste(f2));
				}
			}
			res.put(f1, 1.0/ctr);
		}
		return res;
	}
	
	public static void main(String[] args) throws IOException {
		ArrayList<String> files = new ArrayList<>();
		files.add("/users/nfs/Etu5/3408625/Bureau/S3/DAAR/DAAR-Projet-online/Projet online/src/jaccarudo/test1");
		files.add("/users/nfs/Etu5/3408625/Bureau/S3/DAAR/DAAR-Projet-online/Projet online/src/jaccarudo/test2");
		files.add("/users/nfs/Etu5/3408625/Bureau/S3/DAAR/DAAR-Projet-online/Projet online/src/jaccarudo/test3");
		files.add("/users/nfs/Etu5/3408625/Bureau/S3/DAAR/DAAR-Projet-online/Projet online/src/jaccarudo/test4");
		files.add("/users/nfs/Etu5/3408625/Bureau/S3/DAAR/DAAR-Projet-online/Projet online/src/jaccarudo/test5");
		Map<String, Double> a = closenessCentrality(files);
		for(String k  :  a.keySet()) {
			System.out.println(k+" : "+a.get(k));
		}
		
	}
}
