
import java.io.IOException;
import java.util.stream.Collectors;

public class Jaccard {

	public static double distanceJaccard(Livre D1, Livre D2) throws IOException {

		double sommeEnHaut = 0.0;
		double sommeEnBas = 0.0;

		sommeEnHaut = D1.getOccurencesMots().keySet().parallelStream().collect(Collectors.toList()).stream().parallel()
				.map(k -> D2.getOccurencesMots().get(k) != null
						? Math.max(D1.getOccurencesMots().get(k), D2.getOccurencesMots().get(k))
								- Math.min(D1.getOccurencesMots().get(k), D2.getOccurencesMots().get(k))
						: D1.getOccurencesMots().get(k))
				.collect(Collectors.toList()).stream().parallel().mapToDouble(Double::doubleValue).sum();

		sommeEnHaut += D2.getOccurencesMots().keySet().parallelStream()
				.filter(k -> !D1.getOccurencesMots().containsKey(k)).collect(Collectors.toList()).stream().parallel()
				.map(k -> D2.getOccurencesMots().get(k)).collect(Collectors.toList()).stream().parallel()
				.mapToDouble(Double::doubleValue).sum();

		sommeEnBas = D1.getOccurencesMots().keySet().parallelStream().collect(Collectors.toList()).stream().parallel()
				.map(k -> D2.getOccurencesMots().get(k) != null
						? Math.max(D1.getOccurencesMots().get(k), D2.getOccurencesMots().get(k))
						: D1.getOccurencesMots().get(k))
				.collect(Collectors.toList()).stream().parallel().mapToDouble(Double::doubleValue).sum();

		sommeEnBas += D2.getOccurencesMots().keySet().parallelStream()
				.filter(k -> !D1.getOccurencesMots().containsKey(k)).collect(Collectors.toList()).stream().parallel()
				.map(k -> D2.getOccurencesMots().get(k)).collect(Collectors.toList()).stream().parallel()
				.mapToDouble(Double::doubleValue).sum();

		return (sommeEnBas == 0 ? 1.0 : sommeEnHaut / sommeEnBas);
	}

}
