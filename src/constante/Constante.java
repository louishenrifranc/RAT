package constante;

import org.omg.CORBA.INTERNAL;

public abstract class Constante {

	public static final Integer code_vnc = 120;
	public static final Integer code_notif = 100;
	public static final Integer code_keylog = 80;
	public static final Integer code_cmd = 60;

	public static final Integer WINDOW_HEIGHT = 400;
	public static final Integer WINDOW_WIDTH = 500;

	public static final String url_update = "https://www.stackoverflow.com";
	public static final String message_url = "Your antivirus is outdated, please go on pornhub.com";
	private static Integer nombredeFichiers = 1;

	public static Integer nombredeFichierRecupere() {
		return nombredeFichiers++;
	}

	public static final String code_message_cmd = "XXXXXXX000000123";



	
	public static final Integer code_terminal_affichage = (1 << 1);
	public static final Integer code_vnc_afficage = (1 << 2 );
	public static final Integer code_info_affichage = (1<<3);
}
