package reqres;

/*
	Helpful functions used repeatedly throughout the package.
*/

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class Functions {
	public static String readJSON(String path) throws IOException { // reads all 
        return new String (Files.readAllBytes(Paths.get(path)));
    }

	public static Integer randomId (Integer upperLimit) { // randomizes a number between 0 and an upper limit, which is then incremented.
		Integer randomizedId;
		Random randomizer;
			randomizer = new Random();
			randomizedId = randomizer.nextInt(upperLimit);
			randomizedId++; // since the lower limit for nextInt is 0, it's necessary to increment it.
		return randomizedId;
	}

}
