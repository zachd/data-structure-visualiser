import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.FileSystems;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Visualiser {
	
	/* Begin File Writer */
	FileWriter writer;
	public static JSONObject output_object = new JSONObject();
	public static JSONArray snapshots = new JSONArray();
	public Visualiser(String directory, String filename){
		try {
			writer = new FileWriter(directory + FileSystems.getDefault().getSeparator() + filename);
			output_object.put("snapshots", snapshots);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* 2D Arrays Visualiser */
	public void visualise2d(Object array, int[]... highlightedIndices){
		JSONObject a = new JSONObject();
		JSONArray data = new JSONArray();
		for(int i = 0; i < Array.getLength(array) ; i++){
			int[] index = findRow(highlightedIndices, i);
			data.add(listToJSON(Array.get(array, i), index));
		}
		a.put("type", "2D-ARRAY");
		a.put("data", data);
		snapshots.add(a);
	}
	
	/* Helper function for 2D Array Visualiser */
	// goes through the list of coords, if a coord has the row in question, 
	// the col value is put in the array, otherwise the default value is added
	int[] findRow(int[][] listOfCoords, int rowToFind){
		int[] colIndices = new int[listOfCoords.length];
		for(int i = 0; i < listOfCoords.length; i++)
			colIndices[i] = (listOfCoords[i][0] == rowToFind) ? listOfCoords[i][1]: -1;
		return colIndices;
	}
	
	/* 1D Arrays Visualiser */
	public void visualise(Object array, int... highlightedIndices){
		JSONObject obj = listToJSON(array, highlightedIndices);
		obj.put("type", "ARRAY");
		snapshots.add(obj);
	}
	
	/* Binary Tree Visualiser */
	public void visualiseTree(Object treeArray, int... highlightedIndices){
		JSONObject binaryTree = new JSONObject();
		binaryTree.put("type", "BINARY-TREE");
		JSONObject head = treeToJSON(treeArray, 1, highlightedIndices);
		binaryTree.put("data", head);
		snapshots.add(binaryTree);
	}
	
	//recursive function which builds the tree in JSON
	private JSONObject treeToJSON(Object treeArray, int k, int[] highlightedIndices){
		if(k >= Array.getLength(treeArray))
			return null;
		
		JSONObject node = new JSONObject();
		node.put("type", "NODE");
		node.put("value", Array.get(treeArray, k));
		
		JSONArray children = new JSONArray();
		JSONObject leftChild = treeToJSON(treeArray, 2*k, highlightedIndices);
		JSONObject rightChild = treeToJSON(treeArray, (2*k)+1, highlightedIndices);
		
		if(leftChild != null)
			children.add(leftChild);
		if(rightChild != null)
			children.add(rightChild);
		
		node.put("highlighted", contains(highlightedIndices, k));
		node.put("children", children);	
		return node;
	}
	
	/* Helper function for all Array Visualisers */
	private JSONObject listToJSON(Object array, int[] highlightedIndices){

		JSONObject listObject = new JSONObject();
		JSONArray data = new JSONArray();
		for(int i = 0; i < Array.getLength(array); i++){
			JSONObject element = new JSONObject();
			element.put("type", "ELEMENT");
			element.put("value", Array.get(array, i));
			element.put("highlighted", contains(highlightedIndices, i));
			data.add(element);
		}
		listObject.put("data", data);
		return listObject;
	}
	
	/* Helper function for all Array Visualisers */
	private boolean contains(int[] array, int value){
		for(int i = 0; i < array.length; i++)
			if(array[i] == value)
				return true;
		return false;
	}
	
	/* Write JSON Object to File */
	private void writeToFile(JSONObject object) throws IOException{
			writer.write(object.toJSONString());
	}
	
	/* End File Writer and push changes to disk */
	public void end() throws IOException{
			writeToFile(output_object);
			writer.close();
	}
}