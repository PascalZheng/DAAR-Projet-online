

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Jaccard {
	
	public static double distanceJaccard(String f1, String f2) throws IOException {
		Map<String, Long> D1 = liste(f1);
		Map<String, Long> D2 = liste(f2);
		
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

	// le filter double le temps d'execution
	public static Map<String, Long> liste(String filename) throws IOException {
		return Files.lines(Paths.get(filename), Charset.forName("ISO_8859_1")).map(String::toLowerCase)
				.map(line -> line.split("[\\s,:;!?.]+")).flatMap(Arrays::stream).filter(s -> s.matches("[a-zA-Z-']+")) 
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}
	
	
	public static double[][] jaccardMat(ArrayList<String> files) throws IOException{
//		System.out.println("jaccard Mat start");
		double[][] res = new double[files.size()][files.size()];
		int n = res.length;
		
		for(int i=0; i<n; i++) {
//			System.out.println(i);
			for(int j=0;j<n;j++) {
				if(i==j) {
					res[i][j] = 0.0;
				}
				else {
					res[i][j] = distanceJaccard(files.get(i),files.get(j));
				}
				
			}
		}
//		System.out.println("jaccard Mat done");
		return res;
	}
	
	
}
