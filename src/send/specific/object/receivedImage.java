package send.specific.object;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class receivedImage {
	public static BufferedImage receivedImage(ObjectInputStream ois,
			boolean sauvegarde) throws ClassNotFoundException, IOException {
		byte[] sizeIm = new byte[4];

		sizeIm = (byte[]) ois.readObject();
		System.out.println("recoit taille");
		int size = ByteBuffer.wrap(sizeIm).asIntBuffer().get();
		byte[] imageIm = new byte[size];

		imageIm = (byte[]) ois.readObject();
		final BufferedImage image = ImageIO.read(new ByteArrayInputStream(
				imageIm));
		System.out.println("Nouvelle image enregistre");
		
		if (sauvegarde) {
			File outputfile = new File(Paths.get("").toAbsolutePath()
			
			.toString() + File.separator + "saved.png");
			ImageIO.write(image, "png", outputfile);

		}

		return image;

	}
}
