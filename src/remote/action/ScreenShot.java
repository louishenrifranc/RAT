package remote.action;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
/**
 * Classe qui redéfinit la classe de l'interface RemoteActions. Utilise le robot pour creer une capture d'ecran.  
 * @author lh
 *
 */
public class ScreenShot implements RemoteActions {
	
/**********************************************************************************************************************************************/
/*													   ARGUMENT																	   			   /	
/**********************************************************************************************************************************************/
	private static byte[] size = null;

	
	
	
	
/**********************************************************************************************************************************************/
/*													   CONSTRUCTEUR																	   			   /	
/**********************************************************************************************************************************************/
	@Override
	public Object executer(Robot robot) throws IOException {
		// TODO Auto-generated method stub
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		BufferedImage screenshot = robot.createScreenCapture(new Rectangle(
				dimension.width, dimension.height));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(screenshot, "png", baos);
		size = ByteBuffer.allocate(6).putInt(baos.size()).array();
					// Alloue 4 octets : 2,147,483,647 comme taille max
					// Y met dedans la taille du ByteArrayOutputStream
					// Renvoit le tableau de bytes contenant la taille
		System.out.println("[debug] ScreenShot: commande executer");
		return baos.toByteArray();
	}

	
	
/**********************************************************************************************************************************************/
/*													   METHODES																	   			   /	
/**********************************************************************************************************************************************/
	public static byte[] getsize() {

		return size;
	}

}
