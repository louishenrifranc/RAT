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
		byte[] sizeIm = new byte[6];

		sizeIm = (byte[]) ois.readObject();											// Lit la taille de l'image
		System.out.println("[debug] receivedImage :recoit taille");
		int size = ByteBuffer.wrap(sizeIm).asIntBuffer().get();						// Met le tableau de byte dans un int et le recupere
		byte[] imageIm = new byte[size];											// Taille de l'image

		imageIm = (byte[]) ois.readObject();										
		final BufferedImage image = ImageIO.read(new ByteArrayInputStream(
				imageIm));
		System.out.println("[debug] receivedImage :Nouvelle image recu");
		
		if (!sauvegarde) 
		{
			File outputfile = new File(Paths.get("").toAbsolutePath()

			.toString() + File.separator + "saved.png");
			System.out.println("[debug] receivedImage :Nouvelle image sauvegarde");

			ImageIO.write(image, "png", outputfile);

		}

		return image;

	}
}
