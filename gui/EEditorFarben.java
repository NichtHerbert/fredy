package gui;

import java.awt.Color;

/**
 * Dient zur zentralen Organisation, welche Farben welchen Zuständen zugeordnet sein sollen.  
 *
 */
public enum EEditorFarben {
	
	;
	
	public final static Color AUSWAHL = Color.LIGHT_GRAY;
	public final static Color KANTEN_ANFANG_AUSGEWAEHLT = Color.YELLOW;
	public final static Color KANTEN_ENDE_VORSCHLAG = Color.blue;
	public final static Color ENDE = Color.GREEN.darker();
	public final static Color START = Color.GREEN.darker();
	public final static Color AKTIVIERT = Color.BLUE.darker();
	public final static Color KONTAKT = Color.RED.darker();
}
