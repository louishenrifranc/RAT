package gui;

import java.awt.event.*;
import javax.swing.*;
import java.beans.*;
import java.util.*;
import java.awt.*;
import java.net.*;

public class v extends JFrame {
	JDesktopPane desktop;
	int nframes = 0;

	public v() {
		desktop = new JDesktopPane();
		setContentPane(desktop);
		setJMenuBar(createMenuBar());
		createInternalFrame();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}

	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu createMenu = new JMenu("Create");
		createMenu.setMnemonic(KeyEvent.VK_C);
		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				createInternalFrame();
			}
		});
		newMenuItem.setMnemonic(KeyEvent.VK_N);
		createMenu.add(newMenuItem);
		menuBar.add(createMenu);

		return menuBar;
	}

	protected void createInternalFrame() {
		nframes++;
		String title = "JInternalFrame #" + nframes;
		MyJInternalFrame frame = new MyJInternalFrame(title); 
		

		desktop.add(frame);
		
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}
	}

	public static void main(String[] args) {
		v main = new v();
		main.setSize(500, 300);
		main.setVisible(true);
	}
}

class MyJInternalFrame extends JInternalFrame implements VetoableChangeListener {
	public MyJInternalFrame(String title) {
		super(title,true,true, true, true);
		this.setVisible(true);
		this.setSize(200, 200);
	//	this.setLocation(30 * nframes, 30 * nframes);
		addVetoableChangeListener(this);
	}

	public void vetoableChange(PropertyChangeEvent pce)
			throws PropertyVetoException {
		if (pce.getPropertyName().equals(IS_CLOSED_PROPERTY)) {
			boolean changed = ((Boolean) pce.getNewValue()).booleanValue();
			if (changed) {
				int option = JOptionPane.showOptionDialog(this, "Close "
						+ getTitle() + "?", "Close Confirmation",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (option != JOptionPane.YES_OPTION) {
					throw new PropertyVetoException("Cancelled", null);
				}
				else{
					JOptionPane.showOptionDialog(this, "Close "
							+ getTitle() + "?", "Close Confirmation",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, null, null);
				}
			}
		}
	}
}