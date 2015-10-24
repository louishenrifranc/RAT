package send.specific.object;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

import loggingSystem.Log;

public class ReceivedSpecificObject {

	public static int NOMBRE_DE_FICHIER = 1;
	public static final String nomfichier = Paths.get("").toAbsolutePath()
			.toString()
			+ System.getProperty("file.separator")
			+ "FichierRecuperer"
			+ System.getProperty("file.separator")
			+ "fichier"
			+ (NOMBRE_DE_FICHIER) + ".txt";

	private final static int FILE_SIZE = 83886080;

	public static int receivedFile(Integer code, ObjectInputStream is)
			throws IOException {
		byte[] array = new byte[FILE_SIZE];
		int count;

		File file = new File(nomfichier);
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
		}

		while ((count = is.read(array)) > 0) {
			os.write(array, 0, count);
		}
		os.close();

		// System.out.println("Nouveau fichier recu :"+code);
		return NOMBRE_DE_FICHIER++;
	}
}
