package scripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import algorithme.Livre;

public class ScriptMotOccurenceLivre {

	static class Couple {
		private double occu;
		private String livre;

		public Couple(String m, double occu) {
			this.livre = m;
			this.occu = occu;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof Couple)) {
				return false;
			}
			Couple c = (Couple) o;
			return c.livre.equals(this.livre);
		}
	}

	public static void main(String[] args) throws IOException {
		String fileSourcesVrac = "/Vrac/livres";
		String folder = "livres";
		String folderPretraiter = "livres_pretraiter";

		File file = new File("src/centrality/id_node.txt");

		BufferedReader br = new BufferedReader(new FileReader(file));
		ArrayList<String> files_pretraiter = new ArrayList<>();
		String st;
		while ((st = br.readLine()) != null) {
			String[] line = st.split(" ");
			files_pretraiter.add((fileSourcesVrac + "/" + line[1]).replace(folder, folderPretraiter));
		}

		Map<String, Couple> allMots = new HashMap<>();

		files_pretraiter.stream().forEach(f -> {
			try {
				Files.newBufferedReader(Paths.get(f)).lines().forEach(line -> {
					String[] read = line.split(" ");
					if(!read[0].contains("--") && read[0].length()>3 && Double.valueOf(read[1])>60.0){
						Couple c = new Couple(f.split("/")[3], Double.valueOf(read[1]));
						if (allMots.keySet().contains(read[0])) {
							if (allMots.get(read[0]).occu < c.occu) {
								allMots.put(read[0], c);
							}
						} else {
							allMots.put(read[0], c);
						}
					}
					
					

				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		try {
			BufferedWriter writer2 = new BufferedWriter(new FileWriter("src/centrality/all_words.txt"));
			String r = "";
			for (String s : allMots.keySet()) {
				r += s + " " + allMots.get(s).livre + " " + allMots.get(s).occu;
				r += "\n";
				writer2.write(r);
				r = "";
			}
			writer2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
