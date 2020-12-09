package wfnmodell.interfaces;

import java.util.ArrayList;

import horcherschnittstellen.IWFNVeraenderungsHorcher;

/**
 * Schnittstelle, zum Wiedergeben / Darstellen des Datenmodells des WFN.
 *
 */
public interface IWfnModelPresentation {
	
	/**
	 * Gibt alle Stellen und Transitionen des aktuellen Datenmodells zurück.
	 * @return eine Liste aller Stellen und Transitionen des aktuellen Datenmodells
	 */
	public ArrayList<IWfnTransitionAndPlace> getAllElementsForDrawing();
	
	/**
	 * Fügt der Menge derjenigen, die über eine Veränderung des Datenmodells informiert werden,
	 * einen weiteren Horcher hinzu.
	 * @param listener der hinzuzufügende Horcher
	 */
	public void addChangingListener(IWFNVeraenderungsHorcher listener);

	/**
	 * Entfernt aus der Menge derjenigen, die über eine Veränderung des Datenmodells informiert werden,
	 * den mitgegebenen Horcher.
	 * @param listener der zu entfernende Horcher
	 */
	public void removeChangingListener(IWFNVeraenderungsHorcher listener);
	
}
