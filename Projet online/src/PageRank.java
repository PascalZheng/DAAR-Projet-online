import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PageRank {
	private Graph G;
	private int n;
	private double alpha;
	private int iter;
	
	private double[] I;
	private double[] P;
	private double[]tmpP;
	
	public PageRank(Graph G, double alpha, int iter){
		this.G = G;
		this.alpha = alpha;
		this.iter = iter;
		this.n = G.size();
		this.I = new double[n];
		this.P = new double[n];
		this.tmpP = new double[n];
		IntStream.range(0, G.size()).forEach(i -> {I[i] = 1/n;P[i] = 1/n;tmpP[i] = 1/n;});
	}
	
	public void adjVectProd(Graph G, double[] T, double[] P) {
		IntStream.range(0, G.size()).forEach(i -> {P[i] = 0.0;});
		IntStream.range(0, G.size()).forEach(i -> {G.getNeighbor(i).forEach(v-> P[v] += (T[i]/G.getDegreeNode(i)));});
	}
	
	public void normalize() {
		 double t = Arrays.stream(P).sum();
		 IntStream.range(0, P.length).forEach(i -> {P[i] += (1-t)/P.length; tmpP[i] = P[i];});
	}
	
	
	public void compute(){
		IntStream.range(0, this.iter).forEach(i -> {
			this.adjVectProd(this.G, this.tmpP, this.P);
			IntStream.range(0, P.length).forEach(j -> { this.P[j] = (1-this.alpha)*P[j] + this.alpha*this.I[j]; });	
			this.normalize();
		});
	}
	
	public double[] rank() {
		return this.P;
	}
	
	public static void main(String[] args) throws IOException {
		double ALPHA = 0.15;
		int ITER = 20;
		double TRESHOLD = 0.75;
		
		ArrayList<String> files = new ArrayList<>();

		try (Stream<Path> paths = Files.walk(Paths.get("src/livres"))) {
			paths.filter(Files::isRegularFile).forEach(f -> {
				files.add(f.toString());
				
			});
		}
		
		long startTime = System.currentTimeMillis();
		
		Graph G = new Graph(TRESHOLD, files);
		G.saveGraph("src/centrality/graph.txt");
		
		PageRank pageRank = new PageRank(G, ALPHA, ITER);
		
		pageRank.compute();
		
		double[] res = pageRank.rank();
		long endTime = System.currentTimeMillis();
		
		
		System.out.println("Page Rank : That took " + (endTime - startTime) + " milliseconds");
		
		BufferedWriter writer2 = new BufferedWriter(new FileWriter("src/centrality/pagerank.txt"));
		
		int V = res.length;
		for (int i = 0; i < V; i++) {
			writer2.write(i + " "+res[i]+"\n");
			
		}
		writer2.close();
	}

}
