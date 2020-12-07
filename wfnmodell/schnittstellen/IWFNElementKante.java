package wfnmodell.schnittstellen;

import java.awt.Point;

/**
 * Schnittstelle für Elemente des WFN vom Typ Kante.
 *
 */
public interface IWFNElementKante extends IWFNElement {
	
	/**
	 * Legt das Element fest, von dem die Kante ausgeht.
	 * @param elem das Element, von dem die Kante ausgeht
	 */
	void setVon(IWFNElementOK elem);

	/**
	 * Gibt dasjenige Element zurück, von welchem die Kante ausgeht.
	 * @return das Element, von welchem die Kante ausgeht
	 */
	IWFNElementOK getVon();

	/**
	 * Legt das Element fest, in welchem die Kante endet
	 * @param elem das Element, in welchem die Kante endet
	 */
	void setZu(IWFNElementOK elem);

	/**
	 * Gibt dasjenige Element zurück, in welchem die Kante endet.
	 * @return das Element, in welchem die Kante endet
	 */
	IWFNElementOK getZu();
	
	/**
	 * Berechnet den Mittelpunkt zwischen Ausgangs- und Endelement der Kante.
	 * @return Point mit den Koordinaten des Mittelpunkts zwischen von und zu.
	 */
	Point getMittelpunkt();
}
