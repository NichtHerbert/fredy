package wfnmodel.elements;

import java.awt.Point;

import wfnmodel.interfaces.IWfnTransition;

/**
 * Instanzierte Objekte dieser Klasse stellen im Datenmodell des WFN Transitionen dar.
 *
 */
public class WfnElementTransition extends AWfnElementButNoArc
		implements IWfnTransition {

	/**
	 * @param pnmlID wenn das Element importiert wurde, die entsprechende ID des Elements aus der pnml-Datei
	 * @param id die ID, welche das Element eindeutig unterscheidbar macht
	 * @param position die vorgesehenen Koordinaten des Elements
	 */
	public WfnElementTransition(String pnmlID, int id, Point position) {
		super(pnmlID, id, position);
	}

	@Override
	public EWfnElement getWfnElementType() {
		return EWfnElement.TRANSITION;
	}

}
