package tools;
import java.util.Comparator;

/**
 * Comparateur entre deux objets Paire
 * @author Thierno BAH, Pascal Zheng
 *
 */
public class PairComparator implements Comparator<Paire> {


	public int compare(Paire a, Paire b) {
		int res = 0;
		if ((a.getScore() - b.getScore()) > 0.0) {
			res = 1;
		} else if ((a.getScore() - b.getScore()) < 0.0) {
			res = -1;
		}
		return res;
	}

}
