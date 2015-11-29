package gui;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.nio.file.Paths;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;

import master.Connexion;

/**
 * Classe Héritant de MJinternalFrame qui ouvre un fenetre de Renseignement
 * @author lh
 */
public class MInfoJInternalFrame extends MJInternalFrame {
	private JTextPane _jtextPane;
	private Icon _icon;
	public MInfoJInternalFrame(String title, Connexion connexion, int nframe) {
		super(title, connexion, nframe);
		// TODO Auto-generated constructor stub
		setFrameIcon(Toolkit.getDefaultToolkit().getImage(Paths.get("")
				.toAbsolutePath().toString()+File.separator+"ressources"+File.separator+"mask1.png"));
		
		_jtextPane = new JTextPane();
		_jtextPane.setContentType("text/html");
		_jtextPane.setText("<p><b>Username</b> :" + connexion.get_user_name()
				+ "</p> <p><b> IP </b>:" + connexion.get_ip()
				+ "<p><b>OS Arch</b> :" + connexion.get_os_arch() + "</p>"
				+ "<p><b>Version de l'os</b> :" + connexion.get_os_version()
				+ "</p>" + "<p><b>Pays</b> :" + connexion.get_pays()
				);
		getContentPane().add(_jtextPane);
		_jtextPane.setForeground(Color.green);
		_jtextPane.setBackground(new Color(0, 255, 51));
		_jtextPane.setEditable(false);
		setFrameIcon(new ImageIcon(
				FenetrePrincipal.class
						.getResource("/javax/swing/plaf/metal/icons/Question.gif")));
	}
	private void setFrameIcon(Image image) {
		// TODO Auto-generated method stub
		
	}
}
