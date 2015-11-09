package loggingSystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 		Classe permet de gérer les différents flux de messages de fonctionnement du keylogger
 * 		@author lh
 *
 */

public class Log {
/**********************************************************************************************************************************************/
/*													   ARGUMENT																	   			   /	
/**********************************************************************************************************************************************/
	private static Path cheminactuel = Paths.get("");

	private static final String nomfichier = cheminactuel.toAbsolutePath()
			.toString()
			+ System.getProperty("file.separator")
			+ "Log"
			+ System.getProperty("file.separator") + "log.txt";
	private static PrintWriter pw;
	private static File file;

/**********************************************************************************************************************************************/
/*													   CONSTRUCTEUR																	   			   /	
/**********************************************************************************************************************************************/
	public Log() {
		super();
		file = new File(nomfichier);

		try {
			pw = new PrintWriter(new BufferedWriter(new FileWriter(nomfichier,
					true)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

/**********************************************************************************************************************************************/
/*													   METHODES																	   			   /	
/**********************************************************************************************************************************************/
	public void enregistrerFichier(String string) {
		synchronized (file) {
			pw.println(string);
			pw.flush();
		}
	}

	public void fermerFichier() {
		pw.flush();
		pw.close();
	}

}
