package wfnmodell.schnittstellen;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Schnittstelle zum Verändern des Datenmodells des WFN.
 *
 */
public interface IWFNModellVeraendern {

	/**
	 * Instanziert eine neue Stelle.
	 * @param position Position, an welcher die neue Stelle liegen soll
	 */
	void neueStelle(Point position);

	/**
	 * Instanziert eine neue Transition.
	 * @param position Position, an welcher die neue Transition liegen soll
	 */
	void neueTransition(Point position);

	/**
	 * Instanziert eine neue Kante.
	 * @param kantenAusgangsElement Element, von welchem die neue Kante ausgeht
	 * @param kantenZielElement Element, in welchem die neue Kante endet
	 */
	void neueKante(IWFNElementOK kantenAusgangsElement, IWFNElementOK kantenZielElement);
	
	/**
	 * Löscht das übergebene Element.
	 * @param elem das zu löschende Element
	 */
	void loesche(IWFNElement elem);
	
	/**
	 * Löscht die übergebenen Elemente.
	 * @param elementListe Liste der zu löschende Elemente
	 */
	void loesche(ArrayList<? extends IWFNElement> elementListe);
	
	/**
	 * Legt den Namen des mitgegebenen Elements fest.
	 * @param element Element, dessen Name geändert werden soll.
	 * @param name zu setzender Name
	 */
	void setElementName(IWFNElementOK element, String name);
}
