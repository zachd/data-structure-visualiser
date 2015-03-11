import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.FileSystems;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Visualiser {
	
	/* Begin File Writer */
	FileWriter writer;
	public static final int DEFAULT_HIGHTLIGHTED_INDEX = -1;
	public static JSONObject output_object = new JSONObject();
	public static JSONArray snapshots = new JSONArray();
	public Visualiser(String directory){
		try {
			writer = new FileWriter(directory + FileSystems.getDefault().getSeparator() + "output.json");
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
	
	public void visualise2d(Object array){
		int[] defaultCoord = {DEFAULT_HIGHTLIGHTED_INDEX, DEFAULT_HIGHTLIGHTED_INDEX};
		int[][] list = {defaultCoord};
		visualise2d(array, list);
	}
	
	/* Helper function for 2D Array Visualiser */
	// goes through the list of coords, if a coord has the row in question, 
	// the col value is put in the array, otherwise the default value is added
	int[] findRow(int[][] listOfCoords, int rowToFind){
		int[] colIndices = new int[listOfCoords.length];
		for(int i = 0; i < listOfCoords.length; i++)
			colIndices[i] = (listOfCoords[i][0] == rowToFind) ? listOfCoords[i][1]: DEFAULT_HIGHTLIGHTED_INDEX;
		return colIndices;
	}
	
	/* 1D Arrays Visualiser */
	public void visualise(Object array, int... highlightedIndices){
		JSONObject obj = listToJSON(array, highlightedIndices);
		obj.put("type", "ARRAY");
		snapshots.add(obj);
	}
	
	public void visualise(Object array){
		visualise(array, DEFAULT_HIGHTLIGHTED_INDEX);
	}
	
	/* Binary Tree Visualiser */
	public void visualiseTree(Object treeArray, int... highlightedIndices){
		JSONObject binaryTree = new JSONObject();
		binaryTree.put("type", "BINARY-TREE");
		JSONObject head = treeToJSON(treeArray, 1, highlightedIndices);
		binaryTree.put("data", head);
		snapshots.add(binaryTree);
	}
	
	public void visualiseTree(int[] treeArray){
		visualiseTree(treeArray, DEFAULT_HIGHTLIGHTED_INDEX);
	}
	
	//recursive function which builds the tree in JSON
	private JSONObject treeToJSON(Object treeArray, int k, int[] highlightedIndices){
		if(k >= Array.getLength(treeArray)){
			return null;
		}
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
		
		boolean highlighted = contains(highlightedIndices, k);
		node.put("children", children);	
		node.put("highlighted", highlighted);
		return node;
	}
	
	/* Helper function for all Array Visualisers */
	private JSONObject listToJSON(Object array, int[] highlightedIndices){

		JSONObject listObject = new JSONObject();
		JSONArray data = new JSONArray();
		for(int i = 0; i < Array.getLength(array); i++){
			JSONObject element = new JSONObject();
			boolean highlighted = contains(highlightedIndices, i);
			element.put("type", "ELEMENT");
			element.put("value", Array.get(array, i));
			element.put("highlighted", highlighted);
			data.add(element);
		}
		listObject.put("data", data);
		return listObject;
	}
	
	/* Helper function for all Array Visualisers */
	private boolean contains(int[] array, int value){
		for(int i = 0; i < array.length; i++){
			if(array[i] == value){
				return true;
			}
		}
		return false;
	}
	
	/* Write JSON Object to File */
	private void writeToFile(JSONObject object){
		try {
			writer.write(object.toJSONString());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* End File Writer and push changes to disk */
	public void flush(){
		try {
			writeToFile(output_object);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
