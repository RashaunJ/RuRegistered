package parse;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Majors {

	/**
	 * @param args
	 * @throws IOException 
	 */

	public static ArrayList<String> URLParse(ArrayList<String> incoming) throws IOException{
		URL baseUrl = new URL("http://www.acs.rutgers.edu/pls/sc_p/sc_display.select_major");
		BufferedReader in = new BufferedReader(
		new InputStreamReader(baseUrl.openStream()));
		String input;
		while(in.readLine()!=null){
			input = in.readLine();//Bad code practice. Fix so that it doesn't read blindly.
			if(input.contains("<option value")){
				input = input.trim();
				input=input.replaceAll("<(.|\n)*?>","");//Remove html tagging
				incoming.add(input);
			}
		}
		return incoming;
	}
	/**
	 * Reads from local txt. Temp solution until I properly implement POST responses
	 * @param incoming
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<String> localParse(int ID) throws IOException{
    	FileReader fileReader;
	    if(ID == 0){
	    	fileReader = new FileReader("NB.txt");
	    }
	    else if(ID ==1){
	    	fileReader = new FileReader("NW.txt");
	    }
	    else{
	    	fileReader = new FileReader("CA.txt");
	    }

        BufferedReader bufferedReader = new BufferedReader(fileReader);
        ArrayList<String> incoming = new ArrayList<String>();
	    
		String input;

		while((input = bufferedReader.readLine()) != null){
			if(input.contains("<OPTION VALUE")){
				input = input.trim();
				input=input.replaceAll("<(.|\n)*?>","");//Remove html tagging
				incoming.add(input);
			}
		}
		bufferedReader.close();
		return incoming;
	}

}
