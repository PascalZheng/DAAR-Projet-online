
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Jaccard {

	public static double distanceJaccard(Livre D1, Livre D2) throws IOException {

		double sommeEnHaut = 0.0;
		double sommeEnBas = 0.0;

		sommeEnHaut = D1.getOccurencesMots().keySet().parallelStream()
				.collect(Collectors.toList()).stream().parallel()
				.map(k -> D2.getOccurencesMots().get(k) != null
						? Math.max(D1.getOccurencesMots().get(k), D2.getOccurencesMots().get(k))
								- Math.min(D1.getOccurencesMots().get(k), D2.getOccurencesMots().get(k))
						: D1.getOccurencesMots().get(k))
				.collect(Collectors.toList()).stream().parallel()
				.mapToDouble(Double::doubleValue).sum();

		sommeEnHaut += D2.getOccurencesMots().keySet().parallelStream()
				.filter(k -> !D1.getOccurencesMots().containsKey(k))
				.collect(Collectors.toList()).stream().parallel()
				.map(k -> D2.getOccurencesMots().get(k)).collect(Collectors.toList()).stream().parallel()
				.mapToDouble(Double::doubleValue).sum();

		sommeEnBas = D1.getOccurencesMots().keySet().parallelStream()
				.collect(Collectors.toList()).stream().parallel()
				.map(k -> D2.getOccurencesMots().get(k) != null
						? Math.max(D1.getOccurencesMots().get(k), D2.getOccurencesMots().get(k))
						: D1.getOccurencesMots().get(k))
				.collect(Collectors.toList()).stream().parallel()
				.mapToDouble(Double::doubleValue).sum();

		sommeEnBas += D2.getOccurencesMots().keySet().parallelStream()
				.filter(k -> !D1.getOccurencesMots().containsKey(k))
				.collect(Collectors.toList()).stream()
				.parallel().map(k -> D2.getOccurencesMots().get(k))
				.collect(Collectors.toList()).stream().parallel()
				.mapToDouble(Double::doubleValue).sum();

		return (sommeEnBas == 0 ? 1.0 : sommeEnHaut / sommeEnBas);
	}

	public static double[][] jaccardMat(List<Livre> files) /*throws IOException*/ {
		double[][] res = new double[files.size()][files.size()];
		int n = res.length;
		IntStream.range(0, n).parallel().forEach(i -> {
			IntStream.range(0, n).parallel().forEach(j -> {
				if (i == j) {
					res[i][j] = 0.0;
				} else {
					try {
						res[i][j] = distanceJaccard(files.get(i), files.get(j));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		});

		/*for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j) {
					res[i][j] = 0.0;
				} else {
					res[i][j] = distanceJaccard(files.get(i), files.get(j));
				}
			}
		}*/
		
		return res;
	}

}
