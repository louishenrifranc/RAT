package send.specific.object;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class sendInputStream implements Runnable {

	private final InputStream inputStream;
	private String finale;

	public sendInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	private BufferedReader getBufferedReader(InputStream is) {
		return new BufferedReader(new InputStreamReader(is));
	}

	@Override
	public void run() {
		BufferedReader br = getBufferedReader(inputStream);
		String ligne = "";
		finale = "";
		try {
			while ((ligne = br.readLine()) != null) {
				finale += ligne + "\n";
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFinale() {
		return this.finale;
	}
}
