package master;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

import loggingSystem.Log;
/**
 * Classe Main
 * @author lh
 *
 */
public class Server extends Thread {
	
	private static Compte compteActuel;
	public static Log log;
	private static final int PORT = 443;
	public static void main(String args[]) {
		Server server = new Server();
		server.run();
	}
	private Vector<Socket> listSocket;
	
	private ServerSocket ss;
	
	public Server() {
		log = new Log();
		initialiserCompte();
		listSocket = new Vector<Socket>();

	}

	public void initialiserCompte() {
		String compte;
		Scanner sc = new Scanner(System.in);
		compte = "buboub";
		log.enregistrerFichier("********************** Nouveau Compte :"
				+ compte);
		compteActuel = new Compte(compte); /*
											 * DeserializerCompte.charger(compte)
											 */

	}

	@Override
	public void run() {
		Socket s = null;
		try {
			ss = new ServerSocket(PORT);
			if (ss.isClosed())
				return;
		//	Thread.sleep(1000);
			while (true) {
				s = ss.accept();
				validerConnexion(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean validerConnexion(Socket s) throws ClassNotFoundException,
			IOException {
		if (compteActuel.ajouterSocket(s))
			listSocket.addElement(s);
		return true;
	}

}
