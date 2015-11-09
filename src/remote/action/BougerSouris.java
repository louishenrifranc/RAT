package remote.action;

import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class BougerSouris implements RemoteActions {

	/**********************************************************************************************************************************************/
	/*													   ARGUMENT																	   			   /	
	/**********************************************************************************************************************************************/
	private static final long serialVersionUID = 1L;
	private final int pos_x;
	private final int pos_y;

	public BougerSouris(int pos_x, int pos_y) {
		super();
		this.pos_x = pos_x;
		this.pos_y = pos_y;
	}

	public BougerSouris(MouseEvent e) {
		this((int) e.getPoint().getX(), (int) e.getPoint().getY());
	}

	@Override
	public Object executer(Robot robot) throws IOException {
		robot.mouseMove(pos_x, pos_y);

		return null;
	}

}
