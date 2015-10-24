package remote.action;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

public class ScreenShot implements RemoteActions {

	@Override
	public Object executer(Robot robot) throws IOException {
		// TODO Auto-generated method stub
		BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit
				.getDefaultToolkit().getScreenSize()));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", baos);
		byte[] size = ByteBuffer.allocate(4).putInt(baos.size()).array();

		return baos.toByteArray();

	}

}
