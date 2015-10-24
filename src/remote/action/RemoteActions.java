package remote.action;

import java.awt.Robot;
import java.io.IOException;
import java.io.Serializable;

public interface RemoteActions extends Serializable {

	Object executer(Robot robot) throws IOException;

}
