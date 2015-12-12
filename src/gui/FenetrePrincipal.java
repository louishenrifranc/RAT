package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import master.Connexion;
import master.Connexion.Affichage;
import constante.Constante;
/**
 * GUI pour le Maitre, gère une liste de connexions, gère les cliques sur les boutons et se charge de les envoyer au controle (connexion)
 * Correspond au modèle dans le schéma MVC
 * @author Clement Collet & Louis Henri Franc & Mohammed Boukhari
 *
 */
public class FenetrePrincipal {

	private Vector<MJInternalFrame> actualframes;				// InternalFrame actuellement a l'ecran
	private JDesktopPane desktopPane ;
	private Vector<Integer> fenetres ;							// Pour chaque connection quelle fenetre a été choisie
	private JFrame frame;
	private Vector<Vector<MJInternalFrame>> frames ;			// InternalFrame pour toutes les connexions
	private Vector<Connexion> Index_To_Connexion;  				// Passer d'un index dans la liste a la connection									
	private  JList<String> list;
	private Boolean modifiable;
	/**
	 * Lance l'application.
	 */
	private JToolBar toolBar;
	

	/**
	 * Crée l'application.
	 */
	public FenetrePrincipal() {
		initialize();
		this.frame.setVisible(true);
		modifiable=true;		
	}

	/**
	 * Affiche un label/ bouton dès que un nouveau fichier est recu
	 * @throws InterruptedException
	 */
	public void Addinformation(String info) throws InterruptedException
	{
		
		final JButton button = new JButton(info);
		button.setFont(new Font("Courrier", Font.BOLD, 11));
		button.setForeground(Color.RED);
		button.setBackground(Color.YELLOW);
		button.setVisible(true);
		button.setEnabled(false);
		toolBar.add(button);
		Thread information = new Thread() {								// Lance le thread d'envoi
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					button.setVisible(false);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				}
		};
		information.start();			
		
	}
	
	
	/**
	 * Modifie les fenetres actuellement affichées pour que si l'on change de connexion, cela change aussi le workspace
	 */
	private synchronized void changeWorkspace()
	{
		
		for(MJInternalFrame frame : actualframes)
		{
			frame.setVisible(false);							// Masque les frames de l'ancien espace de travail
		}
		int index = getSelectedIndex();							// Recupere le nouvel index
		actualframes.clear();
		
		if(index != -1)
		{
			Connexion connexion = Index_To_Connexion.elementAt(index);
			if(connexion.get_os_name().contains("Win") || connexion.get_os_name().contains("win"))
			{
				frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Paths.get("")
						.toAbsolutePath().toString()+File.separator+"ressources"+File.separator+"WW.png"));
			}
			else
			{
				frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Paths.get("")
						.toAbsolutePath().toString()+File.separator+"ressources"+File.separator+"logo27.png"));
			}
		}
		for(int i=0;i<frames.get((index == -1 ) ? 0 : index).size();i++)
		{
			if( index  == -1 || frames.get(index).isEmpty()) break;	// Si pas d'indice selectionné ou pas de fenetres active pour la connexion
			frames.get(index).get(i).setVisible(true);			// Affiche les nouvelles fenetres
			actualframes.add(frames.get(index).get(i));			// Les ajoute a la liste pour le nouveau espace de travail
		}
		list.setEnabled(true);									// Permet de remodifier la liste
	}
	
	

	
	/**
	 * Est appele par les MouseClicked avec un code particulier
	 * Ouvre une nouvelle fenetre suivant le code, verifie qu'elle n'est pas déja ouverte et que l'on a bien séléctionné une connexion
	 * @param keycode : code indiquant quelle type de fenetre doit etre ouvert
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
					MTerminalJInternalFrame mcmdJF = new MTerminalJInternalFrame(connexion.get_user_name()+" term",
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
				else if(keycode == Constante.code_envoyer)
				{
						MFileInternalFrame mFJF =new MFileInternalFrame("Envoyer Fichier"+ connexion.get_user_name(), connexion,actualframes.size());
						actualframes.add(mFJF);
						frames.get(index).add(mFJF);
						desktopPane.add(mFJF);
						mFJF.setBounds(100, 100, 200, 200);
						mFJF.setSize(300,300);
						mFJF.setLocation(30*actualframes.size(),30*actualframes.size());
						try {
							mFJF.setSelected(true);
					      } catch (java.beans.PropertyVetoException e) {}
						mFJF.setVisible(true);
				}
				else if(keycode == Constante.code_vnc_afficage)
				{
					connexion.sendVNCcommand();
				//	Affichage affichage=new Affichage();
				//	affichage.setVisible(true);
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
			else{
				try {
					Addinformation("FENETRE DEJA EXISTENTE");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * Supprime la JInternalFrame fermé par l'utilisateur
	 * @param mjiFrame
	 */
	private  void deletePrivee(MJInternalFrame mjiFrame,int keycode)
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
			if(!fenetres.isEmpty() && keycode != -1)
			{
				fenetres.set(index, fenetres.get(index)- (1 << keycode));			// Enleve le code qui empechait d'en creer une nouvelle
			}
			

		}
		
	}
	
	
	/**
	 * Methode public appelé depuis le listener de fermeture des JInternalFrame
	 * Appelle la methode privee de la fenetre principale
	 * @param mjiFrame
	 */
	public void deletePublic(MJInternalFrame mjiFrame,int keycode)
	{
		deletePrivee(mjiFrame,keycode);
	}
	
	
	
	
	/**
	 * Recupere la liste des MJInternalFrame ouverte pour la connection co
	 * @param co : la connection en question
	 * @return
	 */
	public Vector<MJInternalFrame> getFrameIndexConnection(Connexion co)
	{
		Vector<MJInternalFrame> x=frames.get(Index_To_Connexion.indexOf(co));
		return x;
	}	
	
	/**
	 * Recupere l'index de l'element choisi dans la liste des connexions choisis
	 * @return
	 */
	private int getSelectedIndex()
	{
		return list.getSelectedIndex();
	}
	
	/**
	 * Initialise la fenetre Principale
	 * Ajoute les ActionListener
	 * 
	 */
	private synchronized void initialize() {
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
		
		list = new JList<String>();																// Jlist
		list.setBackground(new Color(0, 153, 51));
		list.setForeground(new Color(0, 0, 128));
		
		toolBar = new JToolBar();												// Tool Bar
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
	
		JButton btnNewButton_6 = new JButton("Envoyer");
		btnNewButton_6.setIcon(new ImageIcon(Paths.get("")
				.toAbsolutePath().toString()+File.separator+"ressources"+File.separator+"tray33.png"));
		btnNewButton_6.setBackground(Color.GREEN);
		toolBar.add(btnNewButton_6);
		btnNewButton_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				clicked(Constante.code_envoyer);															// 1 correspond au code pour lancer une JInFrame de CMD
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
		      public void valueChanged(ListSelectionEvent listSelectionEvent) {			// Lance un Thread dans le cas de la fermeture d'une conection
		    	  																		// En effet il faut attendre la fin de la mise a jour de la Jlist
		    	  																		// Avant de vouloir changer de Workspace
		    	  list.setEnabled(false);
		    	  Thread attendre=new Thread(){
		    		  public void run()
		    		  {
		    			  while(!modifiable){
		    				  try {
								Thread.sleep(100);
								this.join();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    			  }
		 		    	 if(modifiable) changeWorkspace();
		    		  }
		    	  }; 
		    	  attendre.start();
		      }
		 };
		 list.addListSelectionListener(listSelectionListener);
		
	
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		for(int i=0;i<20;i++)
			frames.add(new Vector<MJInternalFrame>());
		
	}
	
	/**
	 * Modifie la liste actuelle des connexions possibles
	 * @param connexions : la liste des connexions a mettre dans la Jlist
	 */
	public synchronized void setJlist(Vector<Connexion> connexions)
	{
		modifiable=false;
		Index_To_Connexion.clear();
		DefaultListModel<String> lists=new DefaultListModel<>();
		int i=0;
		list.setSelectedIndex(-1);
		for(Connexion connexion:  connexions)
		{
			Index_To_Connexion.add(i, connexion);
			fenetres.insertElementAt(i, 0);
			lists.add(i++,connexion.get_ip().split("/")[1]);
				
		}
		list.setSelectedIndex(0);
		modifiable=true;
		list.setModel(lists);	
	}
}
