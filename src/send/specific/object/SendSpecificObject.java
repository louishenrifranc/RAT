package send.specific.object;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class SendSpecificObject {

	public static boolean sendFile(String nomFIchier, ObjectOutputStream oos)
			throws IOException, InterruptedException {
		String[] split=nomFIchier.split("\\.");
		if(split[1].equals("txt")){
			sendTxt(nomFIchier,oos);
			// System.out.println("Nouveau fichier texte envoyer");
		}
		else
		{
			
		}
		
		return true;
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
