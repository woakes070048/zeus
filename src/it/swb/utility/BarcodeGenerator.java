package it.swb.utility;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

public class BarcodeGenerator {
	
	public static void main(String[] args){
		generaBarcode("282256");
	}

	   public static void generaBarcode(String idOrdine) {
	        try {
	            //Create the barcode bean
	            Code39Bean bean = new Code39Bean();
	            
	            final int dpi = 100;
	            
	            //Configure the barcode generator
	            bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar 
	                                                             //width exactly one pixel
	            bean.setWideFactor(3);
	            bean.doQuietZone(false);
	            
	            bean.setFontName("Calibri");
	            bean.setFontSize(6);
	            
	            //Open output file
	            File outputFile = new File("D:\\zeus\\barcodes\\"+idOrdine+".jpg");
	            OutputStream out = new FileOutputStream(outputFile);
	            
	            try {
	                //Set up the canvas provider for monochrome JPEG output 
	                BitmapCanvasProvider canvas = new BitmapCanvasProvider(
	                        out, "image/jpeg", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
	            
	                //Generate the barcode
	                bean.generateBarcode(canvas, "282855");
	            
	                //Signal end of generation
	                canvas.finish();
	                
	            } finally {
	                out.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
