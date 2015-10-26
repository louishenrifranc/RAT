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

public class Notification {
	private String messageTitre = "Attention";
	private String message = "La base virale doit etre mise a jour";

	public Notification() throws AWTException, InterruptedException {
		TrayIcon ti = new TrayIcon(getImage(),
				"Java application as a tray icon", createPopupMenu());

		ti.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					URI uri = new URI(Constante.url_update);

					Desktop dt = Desktop.getDesktop();
					if (dt.isSupported(Desktop.Action.BROWSE)) {
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

		ti.displayMessage(messageTitre, message, TrayIcon.MessageType.WARNING);
	}

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
