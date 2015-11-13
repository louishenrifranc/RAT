package send.specific.object;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import javax.imageio.ImageIO;

public class SendSpecificObject {


	

	
	public static boolean sendTxt(String nomFichier,ObjectOutputStream oos) throws IOException
	{
		Path f = Paths.get(nomFichier);
		Integer number = new Random().nextInt(2000) + 200;
		byte[] content = Files.readAllBytes(f);
		oos.writeObject(number);
		oos.flush();
		oos.writeObject(content);
		oos.flush();
		// System.out.println("[debug] Nouveau fichier Envoye de code :"+number);
		return true;
	}
	
	
}
