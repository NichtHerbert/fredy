package wfnmodell.importexport;

import java.awt.Point;
import java.io.File;


/**
 * Schnittstelle zum Importieren in das Datenmodell.
 *
 */
public interface IWFNImport {
		
	/**
	 * Setzt die Referenz auf das Objekt der letzten geöffneten oder gespeicherten pnml-Datei.
	 * @param datei zu setzende Referenz
	 */
	void setWFNDatei(File datei);
	
	/**
	 * Gibt eine Referenz auf die letzte geöffnete oder gespeicherte pnml-Datei.
	 * @return Referenz auf die letzte geöffnete oder gespeicherte pnml-Datei
	 */
	File getWFNDatei();
	
	/**
	 * Gibt zurück, ob das WFN so schon gespeichert / exportiert wurde.
	 * @return true, wenn das Datenmodell in seiner aktuellen Form abgespeichert ist
	 */
	boolean istDasWFNSoSchonGespeichert();
	
	/**
	 * Legt fest, ob das WFN so schon gespeichert / exportiert wurde.
	 * @param b der festzulegende boolsche Wert
	 */
	void setIstDasWFNSoSchonGespeichert(boolean b);
	
	/**
	 * Leert das aktuelle Datenmodell, so dass es keine Elemente mehr hat.
	 */
	void clear();

	/**
	 * Fügt dem Datenmodell eine neue Stelle hinzu.
	 * @param pnmlID ID der Stelle laut pnml-Datei
	 * @param name Name der Stelle
	 * @param position Position der Stelle
	 * @param marke Markierung der Stelle
	 */
	void neueStelle(String pnmlID, String name, Point position,  boolean marke);
	
	/**
	 * Fügt dem Datenmodell eine neue Transition hinzu.
	 * @param pnmlID ID der Transition laut pnml-Datei
	 * @param name Name der Transition
	 * @param position Position der Transition
	 */
	void neueTransition(String pnmlID, String name, Point position);

	/**
	 * Fügt dem Datenmodell eine neue Kante hinzu.
	 * @param pnmlIDKante ID der Kante laut pnml-Datei
	 * @param pnmlIDSource pnml-Datei-ID des Ausgangselements der Kante
	 * @param pnmlIDTarget pnml-Datei-ID des Endelements der Kante
	 */
	void neueKante(String pnmlIDKante, String pnmlIDSource, String pnmlIDTarget);

}
