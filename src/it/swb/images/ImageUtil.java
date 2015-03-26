package it.swb.images;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

import it.swb.log.Log;

public class ImageUtil {
	
	
	public static boolean creaThumb(int altezza, int larghezza, String sorgente, String destinazione){
		boolean ok = true;
		try {
			Thumbnails.of(sorgente).size(larghezza, altezza).toFile(destinazione);
			//Log.debug("Thumb "+altezza+"x"+larghezza+" creata: "+destinazione);
		} 
		catch (FileNotFoundException ex) {
			ok = false;
			Log.error("Immagine "+sorgente+" non trovata. Non è stato possibile creare la miniatura."); 
		}
		catch (Exception e) {
			ok = false;
			Log.info(e); 
			//e.printStackTrace();
		}
		return ok;
	}

	public static boolean creaThumbPiccola(String sorgente, String destinazione) throws IOException{
		int larghezza = 150;
		int altezza = 250;
		boolean ok = true;

		if (!(new File(destinazione)).exists())		//se la thumb esiste già non viene fatta
			{
				Thumbnails.of(sorgente).size(larghezza, altezza).toFile(destinazione);
				//Log.debug("Thumb piccola creata: "+destinazione);
			}
			else {
				Thumbnails.of(sorgente).size(larghezza, altezza).toFile(destinazione);
				//Log.debug("La Thumb piccola esisteva gia', è stata ricreata: "+destinazione);
			}
			ok = true;
		return ok;
	}
	
	public static boolean creaThumbMedia(String sorgente, String destinazione) throws IOException{
		int larghezza = 250;
		int altezza = 450;
		boolean ok = false;
			if (!(new File(destinazione)).exists())		//se la thumb esiste già non viene fatta
				{
				Thumbnails.of(sorgente).size(larghezza, altezza).toFile(destinazione);
				//Log.debug("Thumb media creata: "+destinazione);
				}
			else {
				Thumbnails.of(sorgente).size(larghezza, altezza).toFile(destinazione);
				//Log.debug("La Thumb media esisteva gia', è stata ricreata: "+destinazione);
			}
			ok = true;
		return ok;
	}
	
	public static boolean creaThumbPiccola(String sorgente, String destinazione, boolean ricrea){
		int larghezza = 150;
		int altezza = 250;
		boolean ok = true;
		try {
			if (!(new File(destinazione)).exists() || ((new File(destinazione)).exists() && ricrea)){		//Se non esiste oppure esiste ma ricrea è true
				Thumbnails.of(sorgente).size(larghezza, altezza).toFile(destinazione);
			}
		} catch (Exception e) {
			Log.info(e); 
			e.printStackTrace();
			ok = false;
		}
		return ok;
	}
	
	public static boolean creaThumbMedia(String sorgente, String destinazione, boolean ricrea){
		int larghezza = 250;
		int altezza = 450;
		boolean ok = true;
		try {
			if (!(new File(destinazione)).exists() || ((new File(destinazione)).exists() && ricrea)){		//Se non esiste oppure esiste ma ricrea è true
				Thumbnails.of(sorgente).size(larghezza, altezza).toFile(destinazione);
				//Log.debug("Thumb media creata: "+destinazione);
			}
		} catch (Exception e) {
			Log.info(e); 
			e.printStackTrace();
			ok = false;
		}
		return ok;
	}
	
	public static void creaMiniThumb(String sorgente, String destinazione){
		int larghezza = 50;
		int altezza = 100;
		try {
			if (!(new File(destinazione)).exists())		//se la thumb esiste già non viene fatta
				Thumbnails.of(sorgente).size(larghezza, altezza).toFile(destinazione);
				//new ThumbNail2().createThumbnail(sorgente,destinazione,larghezza,altezza);
		} catch (Exception e) {
			Log.info(e); e.printStackTrace();
		}
	}
}
