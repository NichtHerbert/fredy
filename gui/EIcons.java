package gui;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import wfnmodel.elements.EWfnElement;

/**
 * Dient der zentralen Organisation der im Programm verwendeten Icons,
 * die so via ENUMBEZEICHNUNG.getIcon() eingebunden werden können.
 * 
 * As things are now all icons in this program are from flaticon.com 
 * and can (legally) be used if there is a link inside the program 
 * (in the "about" section, one for each author) and in the description 
 * of the program, when it will be published.
 * To create this link, use getAuthorHtmlLink(). 
 * Original links are commented out at the end of the file.
 */
public enum EIcons {
	NEW					("/icons/blank-file.png", "Dave Gandy"),
	OPEN				("/icons/open-folder.png", "Dave Gandy"),
	SAVE				("/icons/floppy-disk.png", "Freepik"),
	SAVE_AS				("/icons/floppy-diskette-with-pen.png", "Freepik"),
	ZOOM_IN				("/icons/zoom-in.png", "Dave Gandy"),
	ZOOM_OUT			("/icons/zoom-out.png", "Dave Gandy"),
	PLACE				("/icons/circle.png", "Freepik"),
	TRANSITION			("/icons/square.png", "Vitaly Gorbachev"),
	ARC					("/icons/diagonal-line.png", "Freepik"),
	MINUS				("/icons/minus-symbol.png", "Dave Gandy"),
	PLUS				("/icons/plus-symbol.png", "Dave Gandy"),
	PLAY				("/icons/arrowhead-pointing-to-the-right.png", "Dave Gandy"),
	BACK_TO_START		("/icons/refresh-arrow.png", "Dave Gandy"),
	QUESTIONMARK		("/icons/question-mark.png", "Dave Gandy"),
	TRASH				("/icons/garbage.png", "Freepik"),
	CURSOR				("/icons/cursor.png", "Freepik"),
	CURSOR_ADD_PLACE	("/icons/cursor-circle.png", "Freepik"),
	CURSOR_ADD_TRANSITION("/icons/cursor-square.png", "Darius Dan"),
	CROSSHAIR			("/icons/crosshair.png", "Freepik"),
	CROSSHAIR_SMALL		("/icons/crosshair-small.png", "Pixel perfect"),
	CROSSHAIR_CIRCLE	("/icons/crosshair-circle.png", "Pixel perfect")
	;
	
	/** festgesetzte Größe der Icons, die sich an der Basiseinheit BASEFACTOR orientiert.*/
	private final int iconSize = EWfnElement.BASEFACTOR * 2;
	/** Das ImageIcon des jeweiligen Enum-Typs */
	private ImageIcon icon;
	/** The name of the author of the icon */
	private String iconAuthor;
	
	/**
	 * Baut für jede Enum-Einheit ein eigenes ImageIcon {@link #icon}.
	 * @param iconPath relativer Pfad zur Bilddatei, aus der das ImageIcon gebaut wird
	 * @param iconAuthor
	 */
	EIcons(String iconPath, String iconAuthor) {
		this.iconAuthor = iconAuthor;
		icon = createImageIcon(iconPath, toString());
	}
	
	public String getAuthorHtmlLink() {
		final String authorKebapCase = iconAuthor.toLowerCase().replaceAll("\\s", "-");
		final String htmlLink = "<html><div>Icons made by "
				+ "<a href=\"https://www.flaticon.com/authors/" + authorKebapCase
				+ "\" title=\"" + iconAuthor + "\">" + iconAuthor
				+ "</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a></div></html>";
		return htmlLink;
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
					   iconSize, iconSize, java.awt.Image.SCALE_SMOOTH ) ;  	   
			return new ImageIcon( newimg, description );
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	public static String getAllAuthorHtmlLinks() {
		ArrayList<String> links = new ArrayList<>(5);
		for (EIcons icon: EIcons.values())
			if (!links.contains(icon.getAuthorHtmlLink()))
				links.add(icon.getAuthorHtmlLink());
		return String.join("\n", links);
		
	}
}
// Original links:
//<div>Icons made by <a href="https://www.flaticon.com/authors/dave-gandy" title="Dave Gandy">Dave Gandy</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
//<div>Icons made by <a href="https://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
//<div>Icons made by <a href="https://www.flaticon.com/authors/darius-dan" title="Darius Dan">Darius Dan</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
//<div>Icons made by <a href="https://www.flaticon.com/authors/pixel-perfect" title="Pixel perfect">Pixel perfect</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
//<div>Icons made by <a href="https://www.flaticon.com/authors/vitaly-gorbachev" title="Vitaly Gorbachev">Vitaly Gorbachev</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>

