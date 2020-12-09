package wfnmodell.elements;

import wfnmodell.interfaces.IWfnElement;

/**
 * Abstrakte Klasse, in der all diejenigen Methoden und Attribute versammelt sind, die 
 * Kanten, Stellen und Transitionen gemeinsam haben. 
 */
abstract class AWfnElement implements IWfnElement {
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
	private boolean recursiveMethodFlag;
	
	
	/**
	 * @param pnmlID wenn das Element importiert wurde, die entsprechende ID des Elements aus der pnml-Datei
	 * @param identifier die ID, welche das Element eindeutig unterscheidbar macht
	 */
	public AWfnElement(String pnmlID, int identifier) {
		this.pnmlID = pnmlID;
		this.identifier = identifier;
		recursiveMethodFlag = false;
	}
	

	@Override
	public EWfnElement getWfnElementType() {
		// muss in den konkreten Klassen überschrieben werden
		return null;
	}

	@Override
	public String getPnmlID() {
		return pnmlID;
	}

	@Override
	public int getID() {
		return identifier;
	}
	
	@Override
	public boolean getRecursiveMethodFlag() {
		return recursiveMethodFlag;
	}

	@Override
	public void setRecursiveMethodFlag(boolean flag) {
		this.recursiveMethodFlag = flag;
	}
	
}
