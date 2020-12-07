package wfnmodell.importexport;

import java.util.ArrayList;

/**
 * Schnittstelle zum Exportieren des vorhandenen Datenmodells.
 *
 */
public interface IWFNExport {
	
	/**
	 * Gibt eine auf den Export zugeschnittene Liste aller Elemente des Datenmodells zurück.
	 * @return Liste aller Elemente des Datenmodells
	 */
	ArrayList<TempPNMLElement> getAlleElementeFuerExport();
	
	/**
	 * Legt fest, ob das WFN so schon gespeichert / exportiert wurde.
	 * @param b der festzulegende boolsche Wert
	 */
	void setIstDasWFNSoSchonGespeichert(boolean b);
}
