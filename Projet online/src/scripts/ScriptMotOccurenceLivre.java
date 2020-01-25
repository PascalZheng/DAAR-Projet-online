package scripts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import algorithme.Livre;


public class ScriptMotOccurenceLivre {
	
	public static void main(String[] args) throws IOException {
		

		String fileSourcesVrac = "/Vrac/livres";
		
		String folder = "livres";
		String folderPretraiter = "livres_pretraiter";

		ArrayList<String> files = new ArrayList<>();
		ArrayList<String> files_pretraiter = new ArrayList<>();


		try (Stream<Path> paths = Files.walk(Paths.get(fileSourcesVrac))) {
			paths.filter(Files::isRegularFile).parallel().forEach(f -> {
				files.add(f.toString());
				files_pretraiter.add(f.toString().replace(folder, folderPretraiter));
			});
		}

		Livre.occurences(files, folder, folderPretraiter);
	}

}
