
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Closeness {
	public static Map<Integer, Double> closeness(double[][] floydWarshallMat) {
		int n = floydWarshallMat.length;
		Map<Integer, Double> res = new HashMap<Integer, Double>();

		IntStream.range(0, n).forEach(i -> {
			double s = (Arrays.stream(floydWarshallMat[i]).parallel().sum());
			if (s == 0) {
				res.put(i, 0.0);
			} else {
				res.put(i, n / s);
			}
		});

		return res;
	}
}
