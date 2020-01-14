

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Closeness {
	
	public static Map<Integer, Double> closeness(double[][] floydWarshallMat ){
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
		
		return res;
	}

}
