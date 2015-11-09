package send.specific.object;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class SendImage {

	public static boolean sendImage(ObjectOutputStream oos, byte[] size,
			byte[] image) throws IOException {
		oos.writeObject(size);
		oos.flush();
		oos.writeObject(image);
		oos.flush();
		oos.reset();
		return false;
	}
}
