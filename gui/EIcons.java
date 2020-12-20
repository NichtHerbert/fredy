package gui;

import java.awt.Image;

import javax.swing.ImageIcon;

import wfnmodel.elements.EWfnElement;

/**
 * Dient der zentralen Organisation der im Programm verwendeten Icons,
 * die so via ENUMBEZEICHNUNG.getIcon() eingebunden werden können.
 *
 */
public enum EIcons {
	NEU					("/icons/neu.png"),
	OEFFNEN				("/icons/oeffnen.png"),
	SPEICHERN			("/icons/speichern.png"),
	SPEICHERN_UNTER		("/icons/speichern_unter.png"),
	ZOOM_IN				("/icons/zoom_in.png"),
	ZOOM_OUT			("/icons/zoom_out.png"),
	STELLE				("/icons/stelle.png"),
	TRANSITION			("/icons/transition.png"),
	KANTE				("/icons/kante.png"),
	MINUS				("/icons/minus.png"),
	PLUS				("/icons/plus.png"),
	PLAY				("/icons/play.png"),
	AUF_START			("/icons/auf_start.png"),
	FRAGEZEICHEN		("/icons/fragezeichen.png"),
	MUELLEIMER			("/icons/muelleimer.png"),
	CURSOR				("/icons/cursor.png"),
	CURSOR_STELLE		("/icons/cursor_stelle.png"),
	CURSOR_TRANSITION	("/icons/cursor_transition.png"),
	CURSOR_KANTE		("/icons/cursor_fadenkreuz.png")
	;
	
	/**
	 * festgesetzte Größe der Icons, die sich an der Basiseinheit BASEFACTOR orientiert.
	 */
	private final int iconGroesse = EWfnElement.BASEFACTOR * 2;
	
	/**
	 * Das ImageIcon des jeweiligen Enum-Typs
	 */
	private ImageIcon icon;
	
	/**
	 * Baut für jede Enum-Einheit ein eigenes ImageIcon {@link #icon}.
	 * @param iconpfad relativer Pfad zur Bilddatei, aus der das ImageIcon gebaut wird
	 */
	EIcons(String iconpfad) {
		icon = createImageIcon(iconpfad, toString());
	}
	
	/**
	 * Gibt das ImageIcon der jeweiligen Enum-Einheit zurück. 
	 * @return das Attribut {@link #icon} der jeweiligen Enum-Einheit
	 */
	public ImageIcon getIcon() {
		return icon;
	}
	

	/**
	 * Die vorliegende Methode ist Oracle's Java Tutorial "How to use Icons" entnommen:
	 * https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html
	 * Sie baut ein ImageIcon aus einer Bilddatei.
	 * @param path Pfad zur Bilddatei
	 * @param description Beschreibung der Bilddatei
	 * @return ein ImageIcon oder null, wenn der Pfad ungültig war
	 */
	private ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			ImageIcon icon = new ImageIcon(imgURL, description);
			Image img = icon.getImage() ;  
			   Image newimg = img.getScaledInstance( 
					   iconGroesse, iconGroesse, java.awt.Image.SCALE_SMOOTH ) ;  	   
			return new ImageIcon( newimg, description );
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}
