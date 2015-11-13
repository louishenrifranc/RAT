package master;

import gui.FenetrePrincipal;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.Vector;

public class Compte implements Serializable {

	private static final long serialVersionUID = 1L;
	private static int COMPTENUMERO = 1;
	private Vector<Connexion> _listConnexion; // Liste des connexions auquel le
												// compte a accès
	private String _nomCompte; // Nom Compte
	private FenetrePrincipal fp;

	public Compte(String nomCompte) {
		_listConnexion = new Vector<>();
		_nomCompte = nomCompte;

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					fp = new FenetrePrincipal(); // Demarre la GUI

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public boolean ajouterSocket(Socket so) throws ClassNotFoundException,
			IOException {
		for (Connexion connexion : _listConnexion) {
			if (connexion.getSo() == so) {
				Server.log.
					enregistrerFichier("Connexion deja existente");
				return false;
			}
		}
		_listConnexion.addElement(new Connexion(so));
		fp.setJlist(_listConnexion);
		return true;
	}

	public Vector<Connexion> getListConnexion() {
		return _listConnexion;
	}
}