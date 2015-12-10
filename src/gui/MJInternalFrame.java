package gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import master.Connexion;
import constante.Constante;

/**
 * Classe Héritant de JinternalFrame a laquel on a rajouté un Listener qui permet de demander verification
 * lorsqu'on ferme la fenetre.
 * @author Clement Collet & Louis Henri Franc & Mohammed Boukhari
 */

public class MJInternalFrame extends JInternalFrame implements
		VetoableChangeListener {
	protected Connexion _connexion;
	protected JPanel _jpanel;
	public MJInternalFrame(String title,Connexion connexion,int nframe) {
		super(title, true, true, true, true);
		addVetoableChangeListener(this);
		_jpanel=new JPanel();
		_connexion=connexion;
	}

	@Override
	/*
	 * Méthode appelé par le Listener quand la fenetre veut etre supprimé
	 * 
	 */
	public void vetoableChange(PropertyChangeEvent pce)
			throws PropertyVetoException {
		// TODO Auto-generated method stub
		if (pce.getPropertyName().equals(IS_CLOSED_PROPERTY)) {
			boolean changed = ((Boolean) pce.getNewValue()).booleanValue();
			if (changed) {
				int option = JOptionPane.showOptionDialog(this, "Fermer "
						+ getTitle() + "?", "Fermer la fenetre",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (option != JOptionPane.YES_OPTION) {
					throw new PropertyVetoException("Annuler", null);
				}
				else{
					if(this instanceof MTerminalJInternalFrame)
						_connexion.getCompte().getFenetrePrincipale().deletePublic(this,Constante.code_terminal_affichage);
					else if(this instanceof MVNCJInternalFrame)
						_connexion.getCompte().getFenetrePrincipale().deletePublic(this,Constante.code_vnc_afficage);
					else if(this instanceof MInfoJInternalFrame)
						_connexion.getCompte().getFenetrePrincipale().deletePublic(this,Constante.code_info_affichage);
					else if(this instanceof MotherJInternalFrame){
						_connexion.getCompte().getFenetrePrincipale().deletePublic(this, Constante.code_troll);
					}
					else if(this instanceof MFileInternalFrame){
						_connexion.getCompte().getFenetrePrincipale().deletePublic(this,Constante.code_envoyer);
					}
				}	
			}
		}
	}

}
