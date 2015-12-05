package master;

import gui.FenetrePrincipal;
import gui.MJInternalFrame;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.Vector;

public class Compte implements Serializable {

	private static int COMPTENUMERO = 1;
	private static final long serialVersionUID = 1L;
	private Vector<Connexion> _listConnexion; // Liste des connexions auquel le
												// compte a acc�s
	private String _nomCompte; // Nom Compte
	private FenetrePrincipal fp;

	/**
	 * Compte serializable, permet d'enregistrer sa session contenant les esclaves
	 * @param nomCompte	: le nom du compte
	 */
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
	
	/**
	 * Fonction appel�e lors de la connection d'un nouveau esclave
	 * @param so	                   : le socket de connection
	 * @return						   
	 * @throws ClassNotFoundException	
	 * @throws IOException
	 */
	public boolean ajouterSocket(Socket so) throws ClassNotFoundException,
			IOException {
		for (Connexion connexion : _listConnexion) {
			if (connexion.getSo() == so) {
				Server.log.
					enregistrerFichier("Connexion deja existente");
				return false;
			}
		}
		
		_listConnexion.addElement(new Connexion(so,this));
		fp.setJlist(_listConnexion);
		return true;
	}

	
	public FenetrePrincipal getFenetrePrincipale()
	{
		return fp;
	}
	
	public Vector<Connexion> getListConnexion() {
		return _listConnexion;
	}
	
	/**
	 * Methode appel� par une connection quand la connection est interrompu avec l'esclave
	 * Le compte supprime la connection et se charge d'envoyer les informations au mod�le
	 * @param co: la connection a supprimer
	 */
	public void removeConnection(Connexion co)
	{
		if(_listConnexion.contains(co)){
			_listConnexion.removeElement(co);
			try {
			Vector<MJInternalFrame> mjIF= fp.getFrameIndexConnection(co);		// Recupere l'ensemble des fenetres ouvertes pour la connection
			for(int i=mjIF.size()-1;i>=0;i--){
					MJInternalFrame mj=mjIF.elementAt(i);						// Parcours a l'envers pour pouvoir supprimer les elements
																				// Et en meme temps parcourir le vecteur
					fp.deletePublic(mj, -1);									// Supprime les fenetres avec le code -1
			}
			 co.get_so().shutdownInput();										// Ferme le socket
			 co.get_so().shutdownOutput();
			 co.getSo().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		fp.setJlist(_listConnexion);											// Met a jour la JList
	}
}