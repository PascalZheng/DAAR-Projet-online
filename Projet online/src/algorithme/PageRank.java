package algorithme;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Algorithme qui calcul le score de pagerank de chaque noeud du graphe
 * @author Thierno
 *
 */
public class PageRank {
	private Graph G;
	private int n;
	private double alpha;
	private int iter;

	private double[] I;
	private double[] P;
	private double[] tmpP;

	public PageRank(Graph G, double alpha, int iter) {
		this.G = G;
		this.alpha = alpha;
		this.iter = iter;
		this.n = G.size();

		this.I = new double[n];
		this.P = new double[n];
		this.tmpP = new double[n];

		IntStream.range(0, G.size()).parallel().forEach(i -> {
			I[i] = 1 / n;
			P[i] = 1 / n;
			tmpP[i] = 1 / n;
		});
	}

	public void adjVectProd(Graph G, double[] T, double[] P) {
		IntStream.range(0, G.size()).forEach(i -> {
			P[i] = 0.0;
		});
		IntStream.range(0, G.size()).forEach(i -> {
			G.getNeighbor(i).forEach(v -> P[v] += (T[i] / G.getDegreeNode(i)));
		});
	}

	public void normalize() {
		double t = Arrays.stream(P).sum();
		IntStream.range(0, P.length).forEach(i -> {
			P[i] += (1 - t) / P.length;
			tmpP[i] = P[i];
		});
	}

	public void compute() {
		IntStream.range(0, this.iter).forEach(i -> {
			this.adjVectProd(this.G, this.tmpP, this.P);
			IntStream.range(0, P.length).forEach(j -> {
				this.P[j] = (1 - this.alpha) * P[j] + this.alpha * this.I[j];
			});
			this.normalize();
		});
	}

	public double[] rank() {
		return this.P;
	}

}
