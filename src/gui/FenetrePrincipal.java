package gui;

import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.print.attribute.standard.Sides;
import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.RepaintManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JDesktopPane;
import javax.swing.JMenuBar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.locks.AbstractQueuedLongSynchronizer.ConditionObject;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ActionMapUIResource;

import org.omg.PortableInterceptor.INACTIVE;

import constante.Constante;
import master.Connexion;
import master.Connexion.Affichage;
import master.Server;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.awt.Toolkit;
import java.awt.SystemColor;

public class FenetrePrincipal {

	private JFrame frame;
	private Vector<Connexion> Index_To_Connexion;  				// Passer d'un index dans la liste a la connection
	private static Vector<Vector<MJInternalFrame>> frames ;			// InternalFrame pour toutes les connexions
	private static Vector<MJInternalFrame> actualframes;				// InternalFrame actuellement a l'ecran
	private static Vector<Integer> fenetres ;							// Pour chaque connection quelle fenetre a été choisie
	
	private static JList<String> list;
	private static JDesktopPane desktopPane ;
	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the application.
	 */
	public FenetrePrincipal() {
		initialize();
		this.frame.setVisible(true);
		
		
	}

	/**
	 * Initialise la fenetre Principale
	 * Ajoute les ActionListener
	 * 
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Paths.get("")
				.toAbsolutePath().toString()+File.separator+"ressources"+File.separator+"mask1.png"));
		
		frame.setBounds(100, 100, 766, 513);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		actualframes =new Vector<>();													// Allocation pour les vector
		frames = new Vector<Vector<MJInternalFrame>>();
		Index_To_Connexion = new Vector<>();
		fenetres =new Vector<>(0);														// Initialise les valeurs à -1
		
		list = new JList();																// Jlist
		list.setBackground(new Color(0, 153, 51));
		list.setForeground(new Color(0, 0, 128));
		
		JToolBar toolBar = new JToolBar();												// Tool Bar
		toolBar.setBackground(new Color(0, 102, 153));
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton btnNewButton = new JButton("Notification");
		btnNewButton.setIcon(new ImageIcon(Paths.get("")
				.toAbsolutePath().toString()+File.separator+"ressources"+File.separator+"turn20.png"));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNewButton.setForeground(Color.BLUE);
		btnNewButton.setBackground(new Color(0, 255, 51));
		
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) 
			{
				int index = getSelectedIndex();
				if(index != -1)
				{
				Connexion connexion = Index_To_Connexion.get(index); 					// Recupere la connexion
				connexion.sendNotification(Constante.message_url,Constante.url_update);	// Envoit une demande de notification
				}
			}
		});
		
		toolBar.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Keylogger");						// Recuperer le fichier de keylog
		btnNewButton_1.setIcon(new ImageIcon(Paths.get("")
				.toAbsolutePath().toString()+File.separator+"ressources"+File.separator+"computer207.png"));
		btnNewButton_1.setFont(new Font("Tahoma", Font.ITALIC, 11));
		btnNewButton_1.setForeground(Color.BLUE);
		btnNewButton_1.setBackground(new Color(0, 255, 51));
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = getSelectedIndex();
				if(index != -1)
				{
					Connexion connexion = Index_To_Connexion.get(index);
					connexion.sendKeylog();
				}
			}
		});
		toolBar.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Terminal");								
		btnNewButton_2.setIcon(new ImageIcon(Paths.get("")
				.toAbsolutePath().toString()+File.separator+"ressources"+File.separator+"logotype192.png"));
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNewButton_2.setBackground(new Color(0, 255, 51));
		btnNewButton_2.setForeground(Color.BLUE);
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clicked(Constante.code_terminal_affichage);															// 1 correspond au code pour lancer une JInFrame de CMD
			}
		});
		toolBar.add(btnNewButton_2);
		
		desktopPane = new JDesktopPane();											// Espace de travail
		desktopPane.setBackground(new Color(0, 0, 128));
		
		frame.getContentPane().add(desktopPane, BorderLayout.CENTER);
		
		
		
	
		JButton btnNewButton_3 = new JButton("VNC");
		btnNewButton_3.setIcon(new ImageIcon(Paths.get("")
				.toAbsolutePath().toString()+File.separator+"ressources"+File.separator+"screen54.png"));
		btnNewButton_3.setFont(new Font("Tahoma", Font.ITALIC, 11));
		btnNewButton_3.setForeground(Color.BLUE);
		btnNewButton_3.setBackground(new Color(0, 255, 51));
		btnNewButton_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clicked(Constante.code_vnc_afficage);
			}
		});
		toolBar.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Informations");
		btnNewButton_4.setIcon(new ImageIcon(Paths.get("")
				.toAbsolutePath().toString()+File.separator+"ressources"+File.separator+"business133.png"));
		btnNewButton_4.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNewButton_4.setForeground(Color.BLUE);
		btnNewButton_4.setBackground(new Color(0, 255, 51));
		btnNewButton_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clicked(Constante.code_info_affichage);
			}
		});
		toolBar.add(btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton("\r\n");
		btnNewButton_5.setIcon(new ImageIcon(Paths.get("")
				.toAbsolutePath().toString()+File.separator+"ressources"+File.separator+"emoticon120.png"));
		btnNewButton_5.setBackground(Color.GREEN);
		toolBar.add(btnNewButton_5);
		btnNewButton_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clicked(Constante.code_troll);															// 1 correspond au code pour lancer une JInFrame de CMD
			}
		});
	
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.EAST);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(0, 102, 102));
		frame.getContentPane().add(panel_1, BorderLayout.WEST);
		
	    JScrollPane scrollPane = new JScrollPane(list);
		panel_1.add(scrollPane);
		
		 ListSelectionListener listSelectionListener = new ListSelectionListener() {	// Listener des que l'on change de connexions
		      public void valueChanged(ListSelectionEvent listSelectionEvent) {
		    	  list.setEnabled(false);
		    	  changeWorkspace();
		      }
		 };
		 list.addListSelectionListener(listSelectionListener);
		
	
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		for(int i=0;i<20;i++)
			frames.add(new Vector<MJInternalFrame>());

		
	}
	
	
	/**
	 * Recupere l'index de l'element choisi dans la liste des connexions choisis
	 * @return
	 */
	private static int getSelectedIndex()
	{
		return list.getSelectedIndex();
	}

	
	/**
	 * Modifie la liste actuelle des connexions possibles
	 * @param connexions
	 */
	public void setJlist(Vector<Connexion> connexions)
	{
		Index_To_Connexion.clear();
		DefaultListModel<String> lists=new DefaultListModel<>();
		int i=0;
		
		for(Connexion connexion:  connexions)
		{
			
			Index_To_Connexion.add(i, connexion);
			fenetres.insertElementAt(i, 0);
			lists.add(i++,connexion.get_ip().split("/")[1]);
				
		}
		list.setModel(lists);
	}
	
	
	
	
	/**
	 * Est appele par les MouseClicked avec un code particulier
	 * Verifie si une nouvelle fenetre doit etre afficher
	 *
	 * @param keycode
	 */
	private void clicked(int keycode)
	{
		int index = getSelectedIndex();											// Recupere la connexion actuellement en traitement
		if(index != -1 )
		{	
			if( ( (fenetres.get(index).intValue() & ( 1 << keycode )) == 0) )		// Si une fenetre similaire n'existe pas deja
			{
				Connexion connexion =Index_To_Connexion.get(index);			// Pointeur sur la connexion

				if(keycode == Constante.code_terminal_affichage)													// SI une connexion est selectionne
				{
					MCmdJInternalFrame mcmdJF = new MCmdJInternalFrame(connexion.get_user_name()+" term",
										actualframes.size(), connexion);		// Nouvelle fenetre
					
					actualframes.addElement(mcmdJF);							// Ajoute la fenetre a la liste de la page actuelle
					frames.get(index).add(mcmdJF);								// Ajoute la fenetre a la liste des fenetres pour cette connections
					desktopPane.add(mcmdJF);
					mcmdJF.setBounds(100, 100, 200, 200);
					mcmdJF.setSize(400,290);
					mcmdJF.setLocation(30*actualframes.size(),30*actualframes.size());

					try {
				         mcmdJF.setSelected(true);
				      } catch (java.beans.PropertyVetoException e) {}
					mcmdJF.setVisible(true);
					
				}
				else if(keycode == Constante.code_troll)
				{	
					MotherJInternalFrame moJF= new MotherJInternalFrame("Panneau de commande pour "+connexion.get_user_name(), connexion,actualframes.size());
					actualframes.add(moJF);
					frames.get(index).add(moJF);
					desktopPane.add(moJF);
					moJF.setBounds(100, 100, 200, 200);
					moJF.setSize(300,300);
					moJF.setLocation(30*actualframes.size(),30*actualframes.size());
					try {
						moJF.setSelected(true);
				      } catch (java.beans.PropertyVetoException e) {}
					moJF.setVisible(true);
				}
				else if(keycode == Constante.code_vnc_afficage)
				{
			/*		MVNCJInternalFrame mvncJF = new MVNCJInternalFrame(connexion.get_user_name()+" vnc", connexion,actualframes.size());
					actualframes.add(mvncJF);
					frames.get(index).add(mvncJF);
					desktopPane.add(mvncJF);
					mvncJF.setBounds(100, 100, 200, 200);
					mvncJF.setSize(300,300);
					mvncJF.setLocation(30*actualframes.size(),30*actualframes.size());
					try {
						mvncJF.setSelected(true);
				      } catch (java.beans.PropertyVetoException e) {}
					mvncJF.setVisible(true);
		*/
					Affichage affichage=new Affichage();
					affichage.setVisible(true);
				}
				else if(keycode == Constante.code_info_affichage)
				{
					MInfoJInternalFrame minfJF = new MInfoJInternalFrame(connexion.get_user_name()+" info", connexion,actualframes.size());
					actualframes.add(minfJF );
					frames.get(index).add(minfJF );
					desktopPane.add(minfJF );
					minfJF .setBounds(100, 100, 200, 200);
					minfJF .setSize(100,150);
					minfJF .setLocation(30*actualframes.size(),30*actualframes.size());
					try {
						minfJF .setSelected(true);
				      } catch (java.beans.PropertyVetoException e) {}
					minfJF .setVisible(true);
				}
				fenetres.set(index,fenetres.get(index)+ (1 << keycode));		// Indique que l'on ne peut plus creer de nouvelles fenetres pour ce type maintenant
			}
		}
	}
	
	
	
	
	/**
	 * Methode public appelé depuis le listener de fermeture des JInternalFrame
	 * Appelle la methode privee de la fenetre principale
	 * @param mjiFrame
	 */
	public static void deletePublic(MJInternalFrame mjiFrame,int keycode)
	{
		deletePrivee(mjiFrame,keycode);
	}
	
	
	
	
	
	
	/**
	 * Supprime la JInternalFrame fermé par l'utilisateur
	 * @param mjiFrame
	 */
	private static void deletePrivee(MJInternalFrame mjiFrame,int keycode)
	{
		if(actualframes.contains(mjiFrame))											// Supprime de la liste des fenetres affichés a l'ecrans
		{
			actualframes.removeElement(mjiFrame);
			mjiFrame.setVisible(false);												// Au cas ou, mais en principe inutil
			desktopPane.remove(mjiFrame);
		}
		int index = getSelectedIndex();
		if(index != -1)
		{
			if(!frames.isEmpty() && frames.size() >= index && frames.get(index).contains(mjiFrame) )
			{
				frames.get(index).removeElement(mjiFrame);							// Supprime de la liste des fenetres de la connexion actuelle affichée
			}
			if(!fenetres.isEmpty())
			{
				fenetres.set(index, fenetres.get(index)- (1 << keycode));			// Enleve le code qui empechait d'en creer une nouvelle
			}
			

		}
		
	}
	
	
	
	
	/**
	 * Modifie l'espace d'affichage des fenetres pour n'afficher que celle de la connexion que l'on a selectionne
	 */
	private void changeWorkspace()
	{
		for(MJInternalFrame frame : actualframes)
		{
			frame.setVisible(false);							// Masque les frames de l'ancien espace de travail
		}
		int index = getSelectedIndex();							// Recupere le nouvel index
		actualframes.clear();
		
		for(int i=0;i<frames.get(index).size();i++)
		{
			if( index  == -1 || frames.get(index).isEmpty()) break;	// Si pas d'indice selectionné ou pas de fenetres active pour la connexion
			frames.get(index).get(i).setVisible(true);			// Affiche les nouvelles fenetres
			actualframes.add(frames.get(index).get(i));			// Les ajoute a la liste pour le nouveau espace de travail
		}
		list.setEnabled(true);									// Permet de remodifier la liste
	}
	
	
	
	
	/**
	 * Change le fond de couleur des qu'un nouveau fichier est receptionné
	 * @throws InterruptedException
	 */
	public static void setBackgroundReceivingFile() throws InterruptedException
	{
		int i=3;
		while(i-- >0)
		{
		desktopPane.setBackground(Color.GREEN);
		
		Thread nouveauFichier = new Thread("Nwe"+i) {								// Lance le thread d'envoi
			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
		};
		nouveauFichier.start();
		nouveauFichier.join(500);
	//	nouveauFichier.stop();
		desktopPane.setBackground(new Color(0, 0, 128));
		}
	}
}
