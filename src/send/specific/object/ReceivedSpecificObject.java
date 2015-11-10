package send.specific.object;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import constante.Constante;

public class ReceivedSpecificObject {

	private final static int FILE_SIZE = 83886080;

	public static int receivedFile(Integer code, ObjectInputStream is)		// Gerer la reception des IMAGES !!!!!!!!!!
			throws IOException, ClassNotFoundException {
		String nomfichier = Paths.get("").toAbsolutePath().toString()
				+ System.getProperty("file.separator") + "FichierRecuperer"
				+ System.getProperty("file.separator") + "fichier"
				+ Constante.nombredeFichierRecupere() + ".txt";
		File file = new File(nomfichier);
		System.out.println("LA");
		byte[] content = (byte[]) is.readObject();
		Files.write(file.toPath(), content);

		// System.out.println("Nouveau fichier recu :"+code);
		return 1;
	}
}
