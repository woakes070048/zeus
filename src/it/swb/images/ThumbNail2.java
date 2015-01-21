package it.swb.images;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

public class ThumbNail2 {
	
//	public static void main(String[] args) throws Exception {
//		new ThumbNail2().createThumbnail("C:/personal/a.jpg",
//				"C:/personal/thumb.jpg", 50, 30);
//	}
	
	

	public void createThumbnail(String imgFilePath, String thumbPath,
			int thumbWidth, int thumbHeight) throws Exception {

		Image image = Toolkit.getDefaultToolkit().getImage(imgFilePath);
		MediaTracker mediaTracker = new MediaTracker(new Container());
		mediaTracker.addImage(image, 0);
		mediaTracker.waitForID(0);
		double thumbRatio = (double) thumbWidth / (double) thumbHeight;
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		double imageRatio = (double) imageWidth / (double) imageHeight;
		if (thumbRatio < imageRatio) {
			thumbHeight = (int) (thumbWidth / imageRatio);
		} else {
			thumbWidth = (int) (thumbHeight * imageRatio);
		}
		BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = thumbImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
		BufferedOutputStream out;
		try	{
			out = new BufferedOutputStream(
				new FileOutputStream(thumbPath));
		}	catch(FileNotFoundException e){
			int x = thumbPath.lastIndexOf("\\");
			String cartella = thumbPath.substring(0,x);
			new File(cartella).mkdirs();
			
			out = new BufferedOutputStream(
					new FileOutputStream(thumbPath));
		}
		ImageIO.write(thumbImage, "jpeg", out);
//		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
//		int quality = 100;
//		param.setQuality((float) quality / 100.0f, false);
//		encoder.setJPEGEncodeParam(param);
//		encoder.encode(thumbImage);
		out.close();
	}
}