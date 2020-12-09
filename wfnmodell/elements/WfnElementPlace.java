package wfnmodell.elements;

import java.awt.Point;
import wfnmodell.schnittstellen.IWFNElementStelle;

/**
 * Instanzierte Objekte dieser Klasse stellen im Datenmodell Stellen dar.
 *
 */
public class WfnElementPlace extends AWfnElementButNoArc implements IWFNElementStelle {

	/** true, wenn die Stelle momentan eine Marke hat bzw. hasMarking ist */
	private boolean hasMarking;

	/**
	 * @param pnmlID wenn das Element importiert wurde, die entsprechende ID des Elements aus der pnml-Datei
	 * @param id die ID, welche das Element eindeutig unterscheidbar macht
	 * @param position die vorgesehenen Koordinaten des Elements
	 */	
	public WfnElementPlace(String pnmlID, int id, Point position) {
		super(pnmlID, id, position);
	}

	@Override
	public void setMarking(boolean marking) {
		this.hasMarking = marking;
	}

	@Override
	public boolean hasMarking() {
		return hasMarking;
	}

	@Override
	public EWfnElement getWfnElementType() {
		return EWfnElement.PLACE;
	}

}
