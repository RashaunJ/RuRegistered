package parse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
 
public class CourseWriter {
	static String baseUrl = "https://sis.rutgers.edu/soc/multi.json?subjects=";

    
    public static ArrayList<Course> create(String major,String semester, String campus) throws IOException, JSONException{
    	try{
    	ArrayList<Course> in = new ArrayList<Course>();
    	Gson document = new Gson();

    	//URL stream to grab json text from Rutgers
    	URL url = new URL(baseUrl+major+"&semester="+semester+"&campus="+campus+"&level=U");
    	BufferedReader urlStream = new BufferedReader(
    	new InputStreamReader(url.openStream()));

    	StringBuilder jsonText = new StringBuilder();
    	//Holds the full JSON text

    	String curr = null;
    	while ((curr = urlStream.readLine()) != null){	
    	jsonText.append(curr);
    	}
    	urlStream.close();
    	
    	InputStream jsonStream = new ByteArrayInputStream(jsonText.toString().getBytes());
    	//Json source file
    	JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));//Converts String to type InputStream

    	JsonParser parser = new JsonParser();
    	JsonArray userarray= parser.parse(reader).getAsJsonArray();


    	for(JsonElement singleClass: userarray){
    		Course singleCourse = document.fromJson(singleClass, Course.class);

    		in.add(singleCourse);

    	}
    	return in;
    	}
    	catch (FileNotFoundException e){
    		System.out.print("File not found");
    		return null;	
    		}
    	}
    
}
