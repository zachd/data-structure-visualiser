import java.io.File;
import java.io.IOException;

public class Example {
	public static void main(String[] args) throws IOException {
		// Get the source directory of the project
		String directory = (new File(System.getProperty("user.dir") + "/../..")).getCanonicalPath();
		
		// Create a new Visualiser Object
		Visualiser visualiser = new Visualiser(directory);
		
		// Two dummy arrays to test String and Int output
		String[] stringarray = {"ABC", "DEF", "GHI"};
		int[] intarray = {1, 2, 3, 4, 5, 6, 7, 8};

		// Visualise both arrays
		visualiser.visualise(intarray);
		visualiser.visualise(stringarray);
		
		// Finsh writing and flush output
		visualiser.flush();
	}
}
