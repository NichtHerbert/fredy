package wfnmodell.interfaces;

import java.awt.Point;

/**
 * Schnittstelle für Elemente des WFN vom Typ Kante.
 *
 */
public interface IWfnArc extends IWfnElement {
	
	/**
	 * Legt das Element fest, von dem die Kante ausgeht.
	 * @param elem das Element, von dem die Kante ausgeht
	 */
	void setSource(IWfnTransitionAndPlace elem);

	/**
	 * Gibt dasjenige Element zurück, von welchem die Kante ausgeht.
	 * @return das Element, von welchem die Kante ausgeht
	 */
	IWfnTransitionAndPlace getSource();

	/**
	 * Legt das Element fest, in welchem die Kante endet
	 * @param elem das Element, in welchem die Kante endet
	 */
	void setTarget(IWfnTransitionAndPlace elem);

	/**
	 * Gibt dasjenige Element zurück, in welchem die Kante endet.
	 * @return das Element, in welchem die Kante endet
	 */
	IWfnTransitionAndPlace getTarget();
	
	/**
	 * Berechnet den Mittelpunkt zwischen Ausgangs- und Endelement der Kante.
	 * @return Point mit den Koordinaten des Mittelpunkts zwischen von und zu.
	 */
	Point getCenter();
}
