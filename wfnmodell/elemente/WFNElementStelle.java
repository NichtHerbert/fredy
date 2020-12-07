package wfnmodell.elemente;

import java.awt.Point;
import wfnmodell.schnittstellen.IWFNElementStelle;

/**
 * Instanzierte Objekte dieser Klasse stellen im Datenmodell Stellen dar.
 *
 */
public class WFNElementStelle extends AWFNElementOK implements IWFNElementStelle {

	/** true, wenn die Stelle momentan eine Marke hat bzw. markiert ist */
	private boolean markiert;

	/**
	 * @param pnmlID wenn das Element importiert wurde, die entsprechende ID des Elements aus der pnml-Datei
	 * @param id die ID, welche das Element eindeutig unterscheidbar macht
	 * @param position die vorgesehenen Koordinaten des Elements
	 */	
	public WFNElementStelle(String pnmlID, int id, Point position) {
		super(pnmlID, id, position);
	}

	@Override
	public void setMarke(boolean marke) {
		this.markiert = marke;
	}

	@Override
	public boolean hatMarke() {
		return markiert;
	}

	@Override
	public EWFNElement getTyp() {
		return EWFNElement.STELLE;
	}

}
