package gui;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JTextPane;

import master.Connexion;

public class MInfoJInternalFrame extends MJInternalFrame{
	private JTextPane _jtextPane;
	public MInfoJInternalFrame(String title, Connexion connexion, int nframe) {
		super(title, connexion, nframe);
		// TODO Auto-generated constructor stub
		_jtextPane =  new JTextPane();
		_jtextPane.setContentType("text/html");
		_jtextPane.setText("<p><b>Username</b> :"+connexion.get_user_name()+"</p> <p><b> IP </b>:"+connexion.get_ip());
		getContentPane().add(_jtextPane);
		_jtextPane.setForeground(Color.BLUE);
		_jtextPane.setBackground(Color.ORANGE);
		setFrameIcon(new ImageIcon(FenetrePrincipal.class.getResource("/javax/swing/plaf/metal/icons/Question.gif")));
	}

}
