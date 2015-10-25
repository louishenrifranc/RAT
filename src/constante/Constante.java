package constante;

public class Constante {
	
	
	public static final Integer code_vnc = 120;
	public static final Integer code_notif = 100;
	public static final Integer code_keylog = 80;
	public static final Integer code_cmd = 60;
	
	public static final Integer WINDOW_HEIGHT = 400;
	public static final Integer WINDOW_WIDTH = 500;
	
	public static final String url_update="https://www.stackoverflow.com";
	public static final String message_url="Your antivirus is outdated, please go on pornhub.com";
	public static Integer nombredeFichiers = 1;
	public static Integer nombredeFichierRecupere(){
		return nombredeFichiers++;
	}

}
