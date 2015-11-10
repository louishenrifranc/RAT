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

	public static boolean sendFile(String nomFIchier, ObjectOutputStream oos)
			throws IOException, InterruptedException {
		String[] split=nomFIchier.split("\\.");
		if(split[1].equals("txt")){
			sendTxt(nomFIchier,oos);
			// System.out.println("Nouveau fichier texte envoyer");
		}
		else if(split[1].equals("png"))
		{
			BufferedImage image = ImageIO.read(new File(nomFIchier)); 
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			byte[] size = ByteBuffer.allocate(4).putInt(baos.size()).array();
			sendImage(oos,size,baos.toByteArray());
		}
				
		return true;
	}
	
	
	public static boolean sendImage(ObjectOutputStream oos, byte[] size,
			byte[] image) throws IOException {
		oos.writeObject(size);
		oos.flush();
		oos.writeObject(image);
		oos.flush();
		oos.reset();
		return false;
	}
	
	private static boolean sendTxt(String nomFichier,ObjectOutputStream oos) throws IOException
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
