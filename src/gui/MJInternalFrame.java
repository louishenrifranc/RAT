package gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import constante.Constante;
import master.Connexion;

public class MJInternalFrame extends JInternalFrame implements
		VetoableChangeListener {
	protected JPanel _jpanel;
	public MJInternalFrame(String title,Connexion connexion,int nframe) {
		super(title, true, true, true, true);
		addVetoableChangeListener(this);
		_jpanel=new JPanel();
	}

	@Override
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
					FenetrePrincipal.deletePublic(this,Constante.code_terminal_affichage);
					
				}
			}
		}
	}

}
