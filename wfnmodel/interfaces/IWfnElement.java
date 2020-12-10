package wfnmodel.interfaces;

import wfnmodel.elements.EWfnElement;

/**
 * Schnittstelle für Methoden, die jedes Element des Workflownetzes besitzt. 
 *
 */
public interface IWfnElement {

	/**
	 * Gibt den Element-Typ des Elements zurück.
	 * @return je nach Typ ARC, TRANSITION oder PLACE
	 */
	EWfnElement getWfnElementType();

	/**
	 * Gibt die PNML-ID des Elements zurück.
	 * @return Wenn das Element importiert wurde, die entsprechende ID des Elements der pnml-Datei, sonst "".
	 */
	String getPnmlID();

	/**
	 * Gibt die eindeutig-unterscheidbare ID des Elements zurück.
	 * @return die eindeutige Id des Elements
	 */
	int getID();
	
	/**
	 * Gibt an, ob das Element momentan schon als Parameter einer rekursiven Methode aufgerufen wurde, auf deren Rückgabewert 
	 * noch gewartet wird.
	 * @return true, wenn gerade mit dem Element eine rekursiver Methodenaufruf am Laufen ist
	 */
	boolean getRecursiveMethodFlag();
	
	/**
	 * Setzt den boolschen Wert, ob das Element momentan Teil eines rekursiven Methodenaufrufs ist.
	 * @param flag der zu setzende Wert
	 */
	void setRecursiveMethodFlag(boolean flag);
}
