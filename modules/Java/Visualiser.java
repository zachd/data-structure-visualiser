import java.awt.List;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Visualiser {
	FileWriter writer;
	public static final int DEFAULT_HIGHTLIGHTED_INDEX = -1;
	public Visualiser(String directory){
		try {
			writer = new FileWriter(directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//handles 2D arrays
	public void visualise(int[][] array, int[]... highlightedIndices){
		JSONObject a = new JSONObject();
		JSONArray data = new JSONArray();
		for(int i = 0; i < array.length ; i++){
			int[] index = findRow(highlightedIndices, i);
			data.add(listToJSON(array[i], index));
		}
		a.put("data", data);
		writeToFile(a);
	}
	
	public void visualise(int[][] array){
		int[] defaultCoord = {DEFAULT_HIGHTLIGHTED_INDEX,DEFAULT_HIGHTLIGHTED_INDEX};
		int[][] list = {defaultCoord};
		visualise(array, list);
	}
	
	//goes through the list of coords, if a coord has the row in question, the col value is put in the array,
	//otherwise the default value is added
	int[] findRow(int[][] listOfCoords, int rowToFind){
		int[] colIndices = new int[listOfCoords.length];
		for(int i = 0; i < listOfCoords.length; i++)
			colIndices[i] = (listOfCoords[i][0] == rowToFind) ? listOfCoords[i][1]: DEFAULT_HIGHTLIGHTED_INDEX;
		return colIndices;
	}
	
	//handles 1D arrays
	public void visualise(int[] list, int... highlightedIndices){
		JSONObject l = listToJSON(list, highlightedIndices);
		writeToFile(l);
	}
	
	public void visualise(int[] list){
		visualise(list, DEFAULT_HIGHTLIGHTED_INDEX);
	}
	
	//handles array implementations of binary trees
	public void visualiseTree(int[] treeArray, int... highlightedIndices){
		JSONObject binaryTree = new JSONObject();
		binaryTree.put("type", "BINARY-TREE");
		JSONObject head = treeToJSON(treeArray, 1, highlightedIndices);
		binaryTree.put("head", head);
		writeToFile(binaryTree);
	}
	
	public void visualiseTree(int[] treeArray){
		visualiseTree(treeArray, DEFAULT_HIGHTLIGHTED_INDEX);
	}
	
	//recursive function which builds the tree in JSON
	private JSONObject treeToJSON(int[] treeArray, int k, int[] highlightedIndices){
		if(k >= treeArray.length){
			return null;
		}
		JSONObject node = new JSONObject();
		node.put("type", "NODE");
		node.put("value", treeArray[k]);
		
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
	
	//takes a 1D array and returns it as a json object
	private JSONObject listToJSON(int[] list, int[] highlightedIndices){
		JSONObject listObject = new JSONObject();
		JSONArray data = new JSONArray();
		for(int i = 0; i < list.length; i++){
			JSONObject element = new JSONObject();
			boolean highlighted = contains(highlightedIndices, i);
			element.put("type", "ELEMENT");
			element.put("value", list[i]);
			element.put("highlighted", highlighted);
			data.add(element);
		}
		listObject.put("data", data);
		return listObject;
	}
	//built in functions were doing weird things so i wrote my own
	private boolean contains(int[] array, int value){
		for(int i = 0; i < array.length; i++){
			if(array[i] == value){
				return true;
			}
		}
		return false;
	}
	
	//takes a JSON object and writes it to a visualiser's file
	private void writeToFile(JSONObject object){
		try {
			writer.write(object.toJSONString());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//closes the visualiser's file writer
	public void endVisualisation(){
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
