package Camera.jrpicam.examples;

import Camera.jrpicam.RPiCamera;
import Camera.jrpicam.enums.AWB;
import Camera.jrpicam.enums.DRC;
import Camera.jrpicam.enums.Encoding;
import Camera.jrpicam.exceptions.FailedToRunRaspistillException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * ShootBufferedStill is an example of how to take and buffer a still image using JRPiCam.
 * 
 * @author Andrew Dillon
 */
public class ShootBufferedStill {
	
	public static void main(String[] args) throws FailedToRunRaspistillException {
		RPiCamera piCamera = null;
		// Attempt to create an instance of RPiCamera, will fail if raspistill is not properly installed
		String saveDir = "/home/pi/Desktop";
		piCamera = new RPiCamera(saveDir);
		// Take a still image, buffer, and save it
		if (piCamera != null)
			shootBufferedStill(piCamera);
	}
	
	public static void shootBufferedStill(RPiCamera piCamera) {
		piCamera.setAWB(AWB.AUTO)       // Change Automatic White Balance setting to automatic
			.setDRC(DRC.OFF)            // Turn off Dynamic Range Compression
			.setContrast(100)           // Set maximum contrast
			.setSharpness(100)          // Set maximum sharpness
			.setQuality(100)            // Set maximum quality
			.setTimeout(1000)           // Wait 1 second to take the image
			.turnOnPreview()            // Turn on image preview
			.setEncoding(Encoding.PNG); // Change encoding of images to PNG
		// Take a 650x650 still image, buffer it, and save it as "/home/pi/Desktop/A Cool Picture.png"
		try {
			BufferedImage buffImg = piCamera.takeBufferedStill(650, 650); // Take image and store in BufferedImage
			File saveFile = new File("/home/pi/Desktop/A Cool Picture.png"); // Create file to save image to
			ImageIO.write(buffImg, "png", saveFile); // Save image to file
			System.out.println("New PNG image saved to:\n\t" + saveFile.getAbsolutePath()); // Print out location of image
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
