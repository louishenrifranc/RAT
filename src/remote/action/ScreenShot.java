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

public class ScreenShot implements RemoteActions {

	private static byte[] size = null;

	@Override
	public Object executer(Robot robot) throws IOException {
		// TODO Auto-generated method stub
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		BufferedImage screenshot = robot.createScreenCapture(new Rectangle(
				dimension.width, dimension.height));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(screenshot, "png", baos);
		size = ByteBuffer.allocate(4).putInt(baos.size()).array();

		return baos.toByteArray();
		
	}

	public static byte[] getsize() {

		return size;
	}

}
