package wfnmodell.elemente;

import wfnmodell.schnittstellen.IWFNElement;

/**
 * Abstrakte Klasse, in der all diejenigen Methoden und Attribute versammelt sind, die 
 * Kanten, Stellen und Transitionen gemeinsam haben. 
 */
abstract class AWFNElement implements IWFNElement {
	/**
	 * Wenn das Element importiert wurde, die entsprechende ID des Elements aus der pnml-Datei, sonst "". 
	 */
	private final String pnmlID;
	/**
	 * ID, die das Element eindeutig unterscheidbar macht.
	 */
	private final int identifier;
	/**
	 * boolsches Attribut zur Verwendung in rekursiven Methodenaufrufen
	 */
	private boolean hatRekursiveMethodeAufgerufen;
	
	
	/**
	 * @param pnmlID wenn das Element importiert wurde, die entsprechende ID des Elements aus der pnml-Datei
	 * @param identifier die ID, welche das Element eindeutig unterscheidbar macht
	 */
	public AWFNElement(String pnmlID, int identifier) {
		this.pnmlID = pnmlID;
		this.identifier = identifier;
		hatRekursiveMethodeAufgerufen = false;
	}
	

	@Override
	public EWFNElement getTyp() {
		// muss in den konkreten Klassen überschrieben werden
		return null;
	}

	@Override
	public String getPNMLID() {
		return pnmlID;
	}

	@Override
	public int getID() {
		return identifier;
	}
	
	@Override
	public boolean hatRekursiveMethodeAufgerufen() {
		return hatRekursiveMethodeAufgerufen;
	}

	@Override
	public void setHatRekursiveMethodeAufgerufen(boolean hatRekursiveMethodeAufgerufen) {
		this.hatRekursiveMethodeAufgerufen = hatRekursiveMethodeAufgerufen;
	}
	
}
