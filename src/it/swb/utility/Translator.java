package it.swb.utility;

	
import java.io.*;
import java.net.*;

import org.primefaces.json.JSONObject;
import org.primefaces.json.JSONTokener;
public class Translator{
	
    public static void main(String[] args) throws IOException{
    	
        String text = "TORTA di BOMBONIERE CON STRUMENTI MUSICALI, 18 FETTE + 1";
        
        System.out.println("ITALIANO: "+text);
        System.out.println("INGLESE: "+translate("en", text));
        System.out.println("FRANCESE: "+translate("fr", text));
        System.out.println("SPAGNOLO: "+translate("es", text));
        System.out.println("TEDESCO: "+translate("de", text));
        
    }
		
    public static String translate(String toLanguage, String text) {
    	String translation = "";
    	
    	String fromLanguage = "it";
    	
    	try {
	        String urlString = "http://mymemory.translated.net/api/get?" +
	        		"de=zeldabomboniere@gmail.com" + //email valida, per avere 10000 parole al giorno
	        		"&mt=1" + //machine translations turned ON
	        		"&q="+ URLEncoder.encode(text, "UTF-8") +  //testo da tradurre
	        		"&langpair=" + fromLanguage + "|"+toLanguage; //lingue da cui e verso cui tradurre
    		
	        URL url = new URL(urlString);
	
	        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
	
	        String line = null;
	
	        String json = null;
	        while ((line = reader.readLine()) != null) {
	                json = line;
	        }
	
	        reader.close();
	
	        JSONTokener tokener = new JSONTokener(json);
	
	        JSONObject response = (JSONObject) tokener.nextValue();
	        
	        //System.out.println(response);
	
	        translation = response.get("responseData").toString();
	        
	        int x = translation.indexOf("translatedText")+17;
	        int y = translation.indexOf("}")-1;
	        
	        translation =  translation.substring(x,y);
	        
    	} catch ( Exception e){
    		e.printStackTrace();
    	}
    	return translation;
    }
    
}