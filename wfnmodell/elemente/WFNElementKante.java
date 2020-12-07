package wfnmodell.elemente;

import java.awt.Point;

import wfnmodell.schnittstellen.IWFNElementKante;
import wfnmodell.schnittstellen.IWFNElementOK;

/**
 * Klasse der Kanten,
 * instanzierte Objekte dieser Klasse stellen im Datenmodell eine gerichtete Kante dar.
 *
 */
public class WFNElementKante extends AWFNElement implements IWFNElementKante {
	
	/** Ausgangs-Element, von dem die Kante ausgeht */
	private IWFNElementOK von;
	/** Element, in dem die Kante endet */
	private IWFNElementOK zu;

	/**
	 * @param pnmlID wenn das Element importiert wurde, die entsprechende ID des Elements aus der pnml-Datei
	 * @param identifier die ID, welche das Element eindeutig unterscheidbar macht
	 * @param von Element, von dem die zu instanzierende Kante ausgeht
	 * @param zu Element, in dem die zu instanzierende Kante endet
	 */
	public WFNElementKante(String pnmlID, int identifier, IWFNElementOK von, IWFNElementOK zu) {
		super(pnmlID, identifier);
		this.von = von;
		this.zu = zu;
	}

	@Override
	public EWFNElement getTyp() {
		return EWFNElement.KANTE;
	}

	@Override
	public void setVon(IWFNElementOK elem) {
		this.von = elem;
	}

	@Override
	public IWFNElementOK getVon() {
		return this.von;
	}

	@Override
	public void setZu(IWFNElementOK elem) {
		this.zu = elem;
	}

	@Override
	public IWFNElementOK getZu() {
		return this.zu;
	}

	@Override
	public Point getMittelpunkt() {
		return new Point((von.getPosition().x + zu.getPosition().x) / 2,
						 (von.getPosition().y + zu.getPosition().y) / 2);
	}

}
