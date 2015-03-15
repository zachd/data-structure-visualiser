import java.io.File;
import java.io.IOException;

public class Example {
	public static void main(String[] args) throws IOException {
		// Get the source directory of the project
		String directory = (new File(System.getProperty("user.dir") + "/../..")).getCanonicalPath();
		
		// Create a new Visualiser Object
		Visualiser visualiser = new Visualiser(directory, "output.json");
		
		// Two dummy arrays to test String and Int output
		String[] stringarray = {"ABC", "DEF", "GHI"};
		int[] intarray = {1, 2, 3, 4, 5, 6, 7, 8};
		int[][] twodarray = {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 0}};
		
		// Visualise both arrays
		visualiser.visualise(intarray);
		visualiser.visualise(stringarray);
		visualiser.visualise2d(twodarray);
		
		// Finsh writing and flush output
		visualiser.end();
	}
}
