package scripts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ScriptRename {

	public static void main(String[] args) {
		
		String allFilesVrac = "/Vrac/livres";
		try (Stream<Path> paths = Files.walk(Paths.get(allFilesVrac))) {
			paths.filter(Files::isRegularFile).forEach(f -> {
				String new_name = f.getFileName().toString().replaceAll("\\s+", "_");
				f.toFile().renameTo(new File(allFilesVrac+"/"+new_name));

			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
