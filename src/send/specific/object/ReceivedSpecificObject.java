package send.specific.object;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
/**
 * Classe impl�mentant la m�thode statique appel� par les classes de controle de l'esclave et du maitre
 * Sauvegarde les fichiers recus dans le flux d'entr�e
 * @author lh
 *
 */
public class ReceivedSpecificObject {

	private final static int FILE_SIZE = 83886080;

	
	
	
	/**
	 * Appelle la fonction en dessous, est utilis� quand le dossier de sauvegarde n'est pas pr�cis�
	 * @param code			: pas utilis�
	 * @param is			: flux d'entr�e
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void receivedFile(Integer code, ObjectInputStream is)		
			throws IOException, ClassNotFoundException {
		receivedFileInDirectory(code,is,"");
	}
	
	
	
	/**
	 * Fonction qui r�cup�re le flux d'entr�e au moment o� le flux contient un fichier
	 * @param code 		: pas utilis� 
	 * @param is		: le flux d'entr�e
	 * @param path		: le chemin dans lequel on va sauvegard� le fichier
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static int receivedFileInDirectory(Integer code, ObjectInputStream is,String path)		
			throws IOException, ClassNotFoundException {
		
		String nomFichier = (String) is.readObject();
		String chemin = (path.isEmpty()) ?  Paths.get("").toAbsolutePath().toString()
				+ System.getProperty("file.separator") + "FichierRecuperer"
				+ System.getProperty("file.separator") : path;
		String nomComplet = chemin + nomFichier +".txt";
		File file = new File(nomComplet);
		byte[] content = (byte[]) is.readObject();
		Files.write(file.toPath(), content);

		// System.out.println("Nouveau fichier recu :"+code);
		return 1;
	}

}
