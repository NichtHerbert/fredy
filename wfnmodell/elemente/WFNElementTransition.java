package wfnmodell.elemente;

import java.awt.Point;

import wfnmodell.schnittstellen.IWFNElementTransition;

/**
 * Instanzierte Objekte dieser Klasse stellen im Datenmodell des WFN Transitionen dar.
 *
 */
public class WFNElementTransition extends AWFNElementOK
		implements IWFNElementTransition {

	/**
	 * @param pnmlID wenn das Element importiert wurde, die entsprechende ID des Elements aus der pnml-Datei
	 * @param id die ID, welche das Element eindeutig unterscheidbar macht
	 * @param position die vorgesehenen Koordinaten des Elements
	 */
	public WFNElementTransition(String pnmlID, int id, Point position) {
		super(pnmlID, id, position);
	}

	@Override
	public EWFNElement getTyp() {
		return EWFNElement.TRANSITION;
	}

}
