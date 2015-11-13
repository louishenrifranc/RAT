package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.jgoodies.forms.layout.FormLayout;

import master.Connexion;

/**
 * Classe Heritant de MJinternalFrame qui ouvre un fenetre ressemblant a un CMD
 * Permet d'envoyer des Commandes aux Esclaves et de recuperer la sortie dans la fenetre
 * @author lh
 *
 */
public class MCmdJInternalFrame extends MJInternalFrame {
	private JTextArea _jTextArea;
	private JTextField _jTextField;
	private JPanel _jpanelFille;

	public MCmdJInternalFrame(String title, int nframe,
			final Connexion connexion) {
		super(title, connexion, nframe);
		_jpanelFille = _jpanel;													// Creation de l'interface graphique
		_jTextArea = new JTextArea(15,30);
		_jTextField = new JTextField();
		_jTextArea.setOpaque(true);
		_jTextArea.setForeground(Color.green);
		_jTextArea.setBackground(Color.BLACK);
		_jTextField.setForeground(Color.green);
		_jTextField.setBackground(Color.BLACK);
		
		setContentPane(_jpanelFille);
		getContentPane().setLayout(new BorderLayout());							// Ajoute une barre de Scroll
		JScrollPane scrollpane=new JScrollPane(_jTextArea);					
		
		getContentPane().add(scrollpane, BorderLayout.NORTH);
		getContentPane().add(_jTextField, BorderLayout.SOUTH);
		
		_jTextField.addActionListener(new AbstractAction() {					// Action Listener quand j'appuie sur Enter

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String line = _jTextField.getText();
				connexion.sendCmdCommand(line, MCmdJInternalFrame.this);
				_jTextField.setText("");
				append(line+">\n");
			}
		});

	}

	
	/**
	 * Fonction qui ajoute a la "fenetre" de CMD une chaine de caractere
	 * @param s :  la chaine a ajouté
	 */
	public void append(String s) { // better implementation--uses					
		// StyleContext
		StyleContext sc = StyleContext.getDefaultStyleContext();
	
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
		        StyleConstants.Foreground, Color.green);
		int len = _jTextArea.getDocument().getLength(); 
		// getText().length();
		_jTextArea.setCaretPosition(len); 
//		_jTextArea.setCharacterAttributes(aset, false);
		_jTextArea.replaceSelection(s); 
		DefaultCaret caret = (DefaultCaret) _jTextArea.getCaret();
		  caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}

	/**
	 * Fonction obsolete
	 * @param string
	 * @throws BadLocationException
	 */
	public void add(String string) throws BadLocationException {
		StyledDocument sd = (StyledDocument) _jTextArea.getDocument();
		sd.insertString(sd.getLength(), string, null);
	}

}
