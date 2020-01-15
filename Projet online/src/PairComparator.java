import java.util.Comparator;

public class PairComparator implements Comparator<Paire> {

	// Used for sorting in ascending order of
	// roll number
	public int compare(Paire a, Paire b) {
		int res = 0;
		if ((a.getScore() - b.getScore()) > 0.0) {
			res = -1;
		} else if ((a.getScore() - b.getScore()) < 0.0) {
			res = 1;
		}
		return res;
	}

}
