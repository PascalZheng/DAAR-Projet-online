

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
		Map<String, Long> D1 = liste(f1);
		Map<String, Long> D2 = liste(f2);
		
		double sommeEnHaut = 0.0;
		double sommeEnBas = 0.0;

		sommeEnHaut = D1.keySet().stream().collect(Collectors.toList()).stream().parallel().map(
				k -> D2.get(k) != null ? Math.max(D1.get(k), D2.get(k)) - Math.min(D1.get(k), D2.get(k)) : D1.get(k))
				.collect(Collectors.toList()).stream().mapToLong(Long::longValue).sum();

		sommeEnHaut += D2.keySet().stream().filter(k -> !D1.containsKey(k)).collect(Collectors.toList()).stream().parallel()
				.map(k -> D2.get(k)).collect(Collectors.toList()).stream().mapToLong(Long::longValue).sum();

		sommeEnBas = D1.keySet().stream().collect(Collectors.toList()).stream().parallel()
				.map(k -> D2.get(k) != null ? Math.max(D1.get(k), D2.get(k)) : D1.get(k)).collect(Collectors.toList())
				.stream().mapToLong(Long::longValue).sum();

		sommeEnBas += D2.keySet().stream().filter(k -> !D1.containsKey(k)).collect(Collectors.toList()).stream().parallel()
				.map(k -> D2.get(k)).collect(Collectors.toList()).stream().mapToLong(Long::longValue).sum();

		return (sommeEnBas == 0 ? 1.0 : sommeEnHaut / sommeEnBas);
	}

	// le filter double le temps d'execution
	public static Map<String, Long> liste(String filename) throws IOException {
		return Stream.of(Files.lines(Paths.get(filename), Charset.forName("ISO_8859_1"))).flatMap(s->s).map(String::toLowerCase).parallel().map(line -> line.split("[\\s,:;!?.]+")).flatMap(Arrays::stream).filter(s -> s.matches("[a-zA-Z-']+")) 
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
