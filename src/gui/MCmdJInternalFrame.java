package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.jgoodies.forms.layout.FormLayout;

import master.Connexion;

public class MCmdJInternalFrame extends MJInternalFrame {
	private JTextPane _jTextArea;
	private JTextField _jTextField;
	private JPanel _jpanelFille;

	public MCmdJInternalFrame(String title, int nframe,
			final Connexion connexion) {
		super(title, connexion, nframe);
		_jpanelFille = _jpanel;
		_jTextArea = new JTextPane();
		_jTextField = new JTextField();
		setContentPane(_jpanelFille);
		getContentPane().setLayout(new BorderLayout());
		JScrollPane scrollpane=new JScrollPane(_jTextArea);					// Ameliorer le JScrollPane
		
		getContentPane().add(scrollpane, BorderLayout.NORTH);
		getContentPane().add(_jTextField, BorderLayout.SOUTH);
		pack();
		_jTextField.addActionListener(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String line = _jTextField.getText();
				connexion.sendCmdCommand(line, MCmdJInternalFrame.this);
				_jTextField.setText("");
				append(line);
			}
		});

	}

	public void append(String s) { // better implementation--uses
		// StyleContext
		StyleContext sc = StyleContext.getDefaultStyleContext();
	
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
		        StyleConstants.Foreground, Color.green);
		int len = _jTextArea.getDocument().getLength(); // same value as
		// getText().length();
		_jTextArea.setCaretPosition(len); // place caret at the end (with no selection)
//		_jTextArea.setCharacterAttributes(aset, false);
		_jTextArea.replaceSelection(s); // there is no selection, so inserts at caret
	}

	public void add(String string) throws BadLocationException {
		StyledDocument sd = (StyledDocument) _jTextArea.getDocument();
		sd.insertString(sd.getLength(), string, null);
	}

}
