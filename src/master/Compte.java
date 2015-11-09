package master;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.Vector;

public class Compte implements Serializable {

	private static final long serialVersionUID = 1L;			
	private static int COMPTENUMERO = 1;					
	private Vector<Connexion> _listConnexion;			// Liste des connexions auquel le compte a accès
	private String _nomCompte;							// Nom Compte

	
	public Compte(String nomCompte) {
		_listConnexion = new Vector<>();
		_nomCompte = nomCompte;
	}

	public boolean ajouterSocket(Socket so) throws ClassNotFoundException,
			IOException {
		for (Connexion connexion : _listConnexion) {
			if (connexion.getSo() == so) {
				Server.log
						.enregistrerFichier("Connexion deja existente");

				return false;
			}
		}
		_listConnexion.addElement(new Connexion(so));
		return true;
	}

	public Vector<Connexion> getListConnexion() {
		return _listConnexion;
	}
}