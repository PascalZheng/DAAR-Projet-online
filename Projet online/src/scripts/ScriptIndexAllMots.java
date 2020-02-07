package scripts;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ScriptIndexAllMots {
	
public static void main(String[] args) throws IOException {
		
		String fileSourcesVrac = "/Vrac/livres_pretraiter";
		

		try (Stream<Path> paths = Files.walk(Paths.get(fileSourcesVrac))) {
			paths.filter(Files::isRegularFile).forEach(f -> {
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter("/Vrac/livres_1664/" + f.getFileName()));
					Files.newBufferedReader(Paths.get(f.toString().replace("livres_pretraiter", "livres"))).lines().forEach(line -> {
						try {
							writer.write(line+"\n");
							
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			});
		}

	}

}
