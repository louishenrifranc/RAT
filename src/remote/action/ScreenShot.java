package remote.action;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;

import client.Esclave;
/**
 * Classe qui redéfinit la classe de l'interface RemoteActions. Utilise le robot pour creer une capture d'ecran.  
 * @author Clement Collet & Louis Henri Franc & Mohammed Boukhari
 *
 */
public class ScreenShot implements ActionVNC {
	
/**********************************************************************************************************************************************/
/*													   ARGUMENT																	   			   /	
/**********************************************************************************************************************************************/
	private static byte[] size = null;

	
	
	
	
/**********************************************************************************************************************************************/
/*													   METHODES																	   			   /	
/**********************************************************************************************************************************************/
	public static byte[] getsize() {

		return size;
	}

	
	
/**********************************************************************************************************************************************/
/*													   CONSTRUCTEUR																	   			   /	
/**********************************************************************************************************************************************/

	public BufferedImage executer(Robot robot) throws IOException {
		// TODO Auto-generated method stub
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		BufferedImage screenshot = robot.createScreenCapture(new Rectangle(
				dimension.width, dimension.height));		
        BufferedImage img=robot.createScreenCapture(new Rectangle(dimension.width, dimension.height));
	/**	ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(screenshot, "jpg", baos);
		size = ByteBuffer.allocate(6).putInt(baos.size()).array();
					// Alloue 4 octets : 2,147,483,647 comme taille max
					// Y met dedans la taille du ByteArrayOutputStream
					// Renvoit le tableau de bytes contenant la taille
		
		File outputfile = new File(Paths.get("").toAbsolutePath()
		.toString() + File.separator + "saved.png");
		System.out.println("[debug] receivedImage :Nouvelle image sauvegarde");
			
		ImageIO.write(screenshot, "png", outputfile);

		System.out.println("[debug] ScreenShot: commande executer");
	**/
		return img;
	
	}

}
