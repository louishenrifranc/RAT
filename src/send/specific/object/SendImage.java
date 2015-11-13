package send.specific.object;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class SendImage {

	public static boolean sendImage(ObjectOutputStream oos, byte[] size,
			byte[] image) throws IOException {
		oos.writeObject(size);
		System.out.println("[debug] sendImage: Envoit tailleImage");
		oos.flush();
		oos.writeObject(image);
		System.out.println("[debug] sendImage: Envoit image");
		oos.flush();
		return true;
	}
}