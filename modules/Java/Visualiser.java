import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Visualiser {
	FileWriter writer;
	public Visualiser(String directory){
		try {
			writer = new FileWriter(directory);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//handles 2D arrays
	public void visualise(int[][] array){
		JSONObject a = new JSONObject();
		JSONArray data = new JSONArray();
		for(int i = 0; i < array.length ; i++){
			data.add(listToJSON(array[i]));
		}
		a.put("data", data);
		writeToFile(a);
	}
	
	//handles 1D arrays
	public void visualise(int[] list){
		JSONObject l = listToJSON(list);
		writeToFile(l);
	}
	
	//handles array implementations of binary trees
	public void visualiseTree(int[] treeArray){
		JSONObject binaryTree = new JSONObject();
		binaryTree.put("type", "BINARY-TREE");
		JSONObject head = treeToJSON(treeArray, 1);
		binaryTree.put("head", head);
		writeToFile(binaryTree);
	}
	
	//recursive function which builds the tree in JSON
	private JSONObject treeToJSON(int[] treeArray, int k){
		if(k >= treeArray.length){
			return null;
		}
		JSONObject node = new JSONObject();
		node.put("type", "NODE");
		node.put("value", treeArray[k]);
		
		JSONArray children = new JSONArray();
		JSONObject leftChild = treeToJSON(treeArray, 2*k);
		JSONObject rightChild = treeToJSON(treeArray, (2*k)+1);
		
		if(leftChild != null)
			children.add(leftChild);
		if(rightChild != null)
			children.add(rightChild);
		
		node.put("children", children);	
		return node;
	}
	
	//takes a 1D array and returns it as a json object
	private JSONObject listToJSON(int[] list){
		JSONObject listObject = new JSONObject();
		JSONArray data = new JSONArray();
		
		for(int i = 0; i < list.length; i++){
			JSONObject element = new JSONObject();
			element.put("type", "ELEMENT");
			element.put("value", list[i]);
			data.add(element);
		}
		listObject.put("data", data);
		return listObject;
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
