package wfnmodell.importexport;

import java.awt.Point;
import java.io.File;


/**
 * Schnittstelle zum Importieren in das Datenmodell.
 *
 */
public interface IWfnImport {
		
	/**
	 * Setzt die Referenz auf das Objekt der letzten geöffneten oder gespeicherten pnml-Datei.
	 * @param file zu setzende Referenz
	 */
	void setWfnFile(File file);
	
	/**
	 * Gibt eine Referenz auf die letzte geöffnete oder gespeicherte pnml-Datei.
	 * @return Referenz auf die letzte geöffnete oder gespeicherte pnml-Datei
	 */
	File getWfnFile();
	
	/**
	 * Gibt zurück, ob das WFN so schon gespeichert / exportiert wurde.
	 * @return true, wenn das Datenmodell in seiner aktuellen Form abgespeichert ist
	 */
	boolean isCurrentWfnSaved();
	
	/**
	 * Legt fest, ob das WFN so schon gespeichert / exportiert wurde.
	 * @param b der festzulegende boolsche Wert
	 */
	void setIsCurrentWfnSaved(boolean b);
	
	/**
	 * Leert das aktuelle Datenmodell, so dass es keine Elemente mehr hat.
	 */
	void clear();

	/**
	 * Fügt dem Datenmodell eine neue Stelle hinzu.
	 * @param pnmlID ID der Stelle laut pnml-Datei
	 * @param name Name der Stelle
	 * @param position Position der Stelle
	 * @param marking Markierung der Stelle
	 */
	void createPlace(String pnmlID, String name, Point position,  boolean marking);
	
	/**
	 * Fügt dem Datenmodell eine neue Transition hinzu.
	 * @param pnmlID ID der Transition laut pnml-Datei
	 * @param name Name der Transition
	 * @param position Position der Transition
	 */
	void createTransition(String pnmlID, String name, Point position);

	/**
	 * Fügt dem Datenmodell eine neue Kante hinzu.
	 * @param pnmlIDArc ID der Kante laut pnml-Datei
	 * @param pnmlIDSource pnml-Datei-ID des Ausgangselements der Kante
	 * @param pnmlIDTarget pnml-Datei-ID des Endelements der Kante
	 */
	void createArc(String pnmlIDArc, String pnmlIDSource, String pnmlIDTarget);

}
