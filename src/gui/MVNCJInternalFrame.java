package gui;

import master.Connexion;
import master.Connexion.Affichage;

public class MVNCJInternalFrame extends MJInternalFrame{
	private Affichage affichage;
	
	public MVNCJInternalFrame(String title, Connexion connexion, int nframe) {
		super(title, connexion, nframe);
		affichage=new Affichage();
		getContentPane().add(affichage);
		affichage.setVisible(true);
	}
}
