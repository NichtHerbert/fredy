package wfnmodel.interfaces;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Schnittstelle zum Verändern des Datenmodells des WFN.
 *
 */
public interface IWfnModelChanging {

	/**
	 * Instanziert eine neue Stelle.
	 * @param position Position, an welcher die neue Stelle liegen soll
	 */
	void createPlace(Point position);

	/**
	 * Instanziert eine neue Transition.
	 * @param position Position, an welcher die neue Transition liegen soll
	 */
	void createTransition(Point position);

	/**
	 * Instanziert eine neue Kante.
	 * @param kantenAusgangsElement Element, von welchem die neue Kante ausgeht
	 * @param kantenZielElement Element, in welchem die neue Kante endet
	 */
	void createArc(IWfnTransitionAndPlace source, IWfnTransitionAndPlace target);
	
	/**
	 * Löscht das übergebene Element.
	 * @param elem das zu löschende Element
	 */
	void delete(IWfnElement element);
	
	/**
	 * Löscht die übergebenen Elemente.
	 * @param elementListe Liste der zu löschende Elemente
	 */
	void delete(ArrayList<? extends IWfnElement> elements);
	
	/**
	 * Legt den Namen des mitgegebenen Elements fest.
	 * @param element Element, dessen Name geändert werden soll.
	 * @param name zu setzender Name
	 */
	void setElementName(IWfnTransitionAndPlace element, String name);
}
