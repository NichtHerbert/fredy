package wfnmodell.schnittstellen;

/**
 * Schnittstelle für Elemente des WFN vom Typ Stelle.
 *
 */
public interface IWFNElementStelle extends IWFNElementOK {
	
	/**
	 * Gibt an, ob die Stelle momentan eine Marke hat / markiert ist.
	 * @return true, wenn die Stelle momentan eine Marke hat
	 */
	boolean hatMarke();
	
	/**
	 * Legt fest, ob die Stelle momentan eine Marke hat / markiert ist.
	 * @param marke festzulegender boolscher Wert
	 */
	void setMarke(boolean marke);
}
