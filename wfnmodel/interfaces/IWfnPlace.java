package wfnmodel.interfaces;

/**
 * Schnittstelle für Elemente des WFN vom Typ Stelle.
 *
 */
public interface IWfnPlace extends IWfnTransitionAndPlace {
	
	/**
	 * Gibt an, ob die Stelle momentan eine Marke hat / markiert ist.
	 * @return true, wenn die Stelle momentan eine Marke hat
	 */
	boolean hasMarking();
	
	/**
	 * Legt fest, ob die Stelle momentan eine Marke hat / markiert ist.
	 * @param marking festzulegender boolscher Wert
	 */
	void setMarking(boolean marking);
}
