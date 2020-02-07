package scripts;

import java.io.BufferedWriter;
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
	
	static class Couple{
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
		String folderPretraiter = "src/livres_pretraiter";
		
		Map<String, Couple> allMots = new HashMap<>();


		try (Stream<Path> paths = Files.walk(Paths.get(folderPretraiter))) {
			paths.filter(Files::isRegularFile).parallel().forEach(f -> {
				try {
					Files.newBufferedReader(Paths.get(f.toString())).lines().forEach(line -> {
						String[] read = line.split(" ");
						Couple c = new Couple(f.toString().split("\\\\")[2], Double.valueOf(read[1]) );
						
						if(allMots.keySet().contains(read[0])) {
							if(allMots.get(read[0]).occu < c.occu) {
								allMots.put(read[0], c);
							}
						}else {
							allMots.put(read[0], c);
						}
						
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		
		try {
			BufferedWriter writer2 = new BufferedWriter(new FileWriter("src/centrality/allWords.txt"));
			String r = "";
			for (String s : allMots.keySet()) {
				r += s + " " + allMots.get(s).livre;
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
