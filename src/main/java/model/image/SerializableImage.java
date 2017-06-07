package model.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by Piotr Staśkiewicz on 09.01.2017.
 */
public class SerializableImage implements Serializable {

	private byte[] imageInByte;

	public void imageToByteArray(BufferedImage image) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		baos.flush();
		imageInByte = baos.toByteArray();
		baos.close();
	}

	public BufferedImage byteArrayToImage() throws IOException {
		InputStream in = new ByteArrayInputStream(imageInByte);
		return ImageIO.read(in);
	}

}
