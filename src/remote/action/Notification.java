/*
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
package remote.action;
 

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.plaf.metal.MetalIconFactory;

import constante.Constante;
/**
 * 			Classe qui permet d'envoyer un signal , une notification depuis le Systeme d'Exploitation
 * @author lh
 *
 */
public class Notification {
	

/**************************************************** *****************************************************************************************/
/*													   ARGUMENTS																	   			   /	
/**********************************************************************************************************************************************/
	
	private String _messageTitre = "Attention";
	private String _message = "La base virale doit etre mise a jour";
	private String _url=Constante.url_update;
	

/**************************************************** *****************************************************************************************/
/*													   CONSTRUCTEUR																	   			   /	
/**********************************************************************************************************************************************/
	public Notification(String message) throws AWTException, InterruptedException {
		TrayIcon ti = new TrayIcon(getImage(),
				"Java application as a tray icon", createPopupMenu());
		String [] split = message.split("\\+\\+");
		if(split.length > 1)
		{
			_message = split[0];
			_url =  split[1];
		}

		ti.addActionListener(new ActionListener() {								// Si l'on clique sur la notification
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					URI uri = new URI(_url);

					Desktop dt = Desktop.getDesktop();
					if (dt.isSupported(Desktop.Action.BROWSE)) {				// Si c'est possible de lancer le Navigateur par d�fault
						dt.browse(uri);
					} else {
						JOptionPane.showMessageDialog(null,
								Constante.message_url);
					}

				} catch (URISyntaxException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		SystemTray.getSystemTray().add(ti);

		Thread.sleep(3000);

		ti.displayMessage(_messageTitre, _message, TrayIcon.MessageType.WARNING);
	}

	

/**************************************************** *****************************************************************************************/
/*													   METHODES																	   			   /	
/**********************************************************************************************************************************************/
	
	/**
	 * Forgage de l'icone de la fenetre
	 * @return
	 * @throws HeadlessException
	 */
	private Image getImage() throws HeadlessException {
		Icon defaultIcon = MetalIconFactory.getTreeHardDriveIcon();
		Image img = new BufferedImage(defaultIcon.getIconWidth(),
				defaultIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		defaultIcon.paintIcon(new Panel(), img.getGraphics(), 0, 0);
		return img;
	}

	
	
	
	
	
	
	
	
	private static PopupMenu createPopupMenu() throws HeadlessException {
		PopupMenu menu = new PopupMenu();

		MenuItem exit = new MenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(exit);

		return menu;
	}
}
