import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Graph {
	// Graph avec distance de Jaccard use la liste d'adjence pour le rpz
	
	private double edgeTreshold ;
	private Map<Integer, HashSet<Integer>> adjArray;
	
	public Graph(double edgeThreshold,ArrayList<String> files) throws IOException {
		this.edgeTreshold = edgeThreshold;
		
		int n = files.size();
		adjArray = new HashMap<>();
		for(int i=0; i<n; i++) {
			adjArray.put(i, new HashSet<Integer>());
		}
		
		for(int i=0; i<n; i++) {
			for(int j=0;j<n;j++) {
				if(i!=j) {
					if(Jaccard.distanceJaccard(files.get(i),files.get(j)) <= this.edgeTreshold) {
						adjArray.get(i).add(j);
						adjArray.get(j).add(i);
					}
				}
			}
		}
	}
	
	public HashSet<Integer> getNeighbor(Integer i){
		return adjArray.get(i);
	}
	
	public int getDegreeNode(Integer i){
		return adjArray.get(i).size();
	}
	
	public int size() {
		return adjArray.size();
	}

	

}
