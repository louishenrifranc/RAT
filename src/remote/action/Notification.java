package remote.action;

import java.awt.AWTException;
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

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.plaf.metal.MetalIconFactory;

public class Notification {
	public Notification() throws AWTException, InterruptedException{
		TrayIcon ti=new TrayIcon(getImage(),"Java application as a tray icon", createPopupMenu());
	
	
	
	ti.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "Hey, you activated me!");
         }
      });
	
      SystemTray.getSystemTray().add(ti);

      Thread.sleep(3000);

      ti.displayMessage("Attention", "Please click here", 
            TrayIcon.MessageType.WARNING);
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
	         public void actionPerformed(ActionEvent e) {
	            System.exit(0);
	         }
	      });
	      menu.add(exit);

	      return menu;
	   }
}


