package wfnmodell.schnittstellen;

import java.util.ArrayList;

import horcherschnittstellen.IWFNVeraenderungsHorcher;

/**
 * Schnittstelle, zum Wiedergeben / Darstellen des Datenmodells des WFN.
 *
 */
public interface IWFNModellWiedergeben {
	
	/**
	 * Gibt alle Stellen und Transitionen des aktuellen Datenmodells zurück.
	 * @return eine Liste aller Stellen und Transitionen des aktuellen Datenmodells
	 */
	public ArrayList<IWFNElementOK> getAlleElementeZumZeichnen();
	
	/**
	 * Fügt der Menge derjenigen, die über eine Veränderung des Datenmodells informiert werden,
	 * einen weiteren Horcher hinzu.
	 * @param horcher der hinzuzufügende Horcher
	 */
	public void addVeraenderungsHorcher(IWFNVeraenderungsHorcher horcher);

	/**
	 * Entfernt aus der Menge derjenigen, die über eine Veränderung des Datenmodells informiert werden,
	 * den mitgegebenen Horcher.
	 * @param horcher der zu entfernende Horcher
	 */
	public void removeVeraenderungsHorcher(IWFNVeraenderungsHorcher horcher);
	
}
