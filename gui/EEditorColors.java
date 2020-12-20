package gui;

import java.awt.Color;

/**
 * Dient zur zentralen Organisation, welche Farben welchen Zuständen zugeordnet sein sollen.  
 *
 */
public enum EEditorColors {
	
	;
	
	public final static Color SELECTION = Color.LIGHT_GRAY;
	public final static Color ARC_SOURCE_SELECTED = Color.YELLOW;
	public final static Color ARC_TARGET_SUGGESTED = Color.blue;
	public final static Color ENDPLACE = Color.GREEN.darker();
	public final static Color STARTPLACE = Color.GREEN.darker();
	public final static Color ENABLED = Color.BLUE.darker();
	public final static Color CONTACT = Color.RED.darker();
}
