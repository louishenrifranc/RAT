package send.specific.object;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import constante.Constante;
import loggingSystem.Log;

public class ReceivedSpecificObject {

	public static final String nomfichier = Paths.get("").toAbsolutePath()
			.toString()
			+ System.getProperty("file.separator")
			+ "FichierRecuperer"
			+ System.getProperty("file.separator")
			+ "fichier"
			+ Constante.nombredeFichierRecupere() + ".txt";

	private final static int FILE_SIZE = 83886080;

	public static int receivedFile(Integer code, ObjectInputStream is)
			throws IOException, ClassNotFoundException {
		

		File file = new File(nomfichier);
		

		byte[] content =(byte[]) is.readObject();
		Files.write(file.toPath(),content);

		// System.out.println("Nouveau fichier recu :"+code);
		return 1;
	}
}
