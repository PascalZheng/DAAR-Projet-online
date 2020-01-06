

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Closeness {

	/*public static double[][] floydWarshall(double[][] jaccardMat) {
//		System.out.println("floyd done");
		int V = jaccardMat.length;

		double[][] res = new double[V][V];
		
		for (int i = 0; i < V; i++)
			for (int j = 0; j < V; j++)
				res[i][j] = jaccardMat[i][j];

		for (int k = 0; k < V; k++) {
			for (int i = 0; i < V; i++) {
				for (int j = 0; j < V; j++) {
					if (res[i][k] + res[k][j] < res[i][j])
						res[i][j] = res[i][k] + res[k][j];
				}
			}
		}
//		System.out.println("floyd done");
		return res;

	}*/
	
	public static Map<Integer, Double> closeness(double[][] floydWarshallMat ){
//		System.out.println("closeness start");
		int V = floydWarshallMat.length;
		Map<Integer, Double> res = new HashMap<Integer, Double>();
		double s = 0.0;
		for(int i=0;i<V;i++) {
			s = (Arrays.stream(floydWarshallMat[i]).sum());
			if(s == 0) {
				res.put(i, 0.0);
			}else {
				res.put(i, V/s);
			}
		}
		
//		System.out.println("closeness done");
		return res;
	}

}
