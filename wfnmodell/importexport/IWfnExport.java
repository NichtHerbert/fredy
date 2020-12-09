package wfnmodell.importexport;

import java.util.ArrayList;

/**
 * Schnittstelle zum Exportieren des vorhandenen Datenmodells.
 *
 */
public interface IWfnExport {
	
	/**
	 * Gibt eine auf den Export zugeschnittene Liste aller Elemente des Datenmodells zurück.
	 * @return Liste aller Elemente des Datenmodells
	 */
	ArrayList<PnmlElement> getAllElementsForExport();
	
	/**
	 * Legt fest, ob das WFN so schon gespeichert / exportiert wurde.
	 * @param b der festzulegende boolsche Wert
	 */
	void setIsCurrentWfnSaved(boolean b);
}
