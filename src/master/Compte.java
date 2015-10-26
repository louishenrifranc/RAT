package master;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.Vector;

public class Compte implements Serializable {

	private static final long serialVersionUID = 1L;
	private static int COMPTENUMERO = 1;
	private Vector<Connexion> listConnexion;
	private String nomCompte;

	public Compte(String nomCompte) {
		listConnexion = new Vector<>();
		nomCompte = nomCompte;
	}

	public boolean ajouterSocket(Socket so) throws ClassNotFoundException,
			IOException {
		for (Connexion connexion : listConnexion) {
			if (connexion.getSo() == so) {
				Server.log
						.enregistrerFichier("Nouvelle Connexion ajoutée au serveur");

				return false;
			}
		}
		listConnexion.addElement(new Connexion(so));
		return true;
	}

	public Vector<Connexion> getListConnexion() {
		return listConnexion;
	}
}