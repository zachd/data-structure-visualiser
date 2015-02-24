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
