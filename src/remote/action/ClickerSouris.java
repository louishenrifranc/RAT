package remote.action;

import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.io.IOException;
/**
 * 
 * @author Clement Collet & Louis Henri Franc & Mohammed Boukhari
 *
 */
public class ClickerSouris implements ActionVNC {

	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private final int Bouton;
	private final int click;

	public ClickerSouris(int bouton, int click) {
		super();
		Bouton = bouton;
		this.click = click;
	}

	public ClickerSouris(MouseEvent e) {
		this(e.getModifiers(), e.getClickCount());
	}

	@Override
	public Object executer(Robot robot) throws IOException {
		// TODO Auto-generated method stub
		for (int i = 0; i < click; i++) {
			robot.mousePress(Bouton);
			robot.mouseRelease(Bouton);
		}
		return null;
	}

}
