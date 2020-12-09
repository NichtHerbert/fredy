package wfnmodell.elements;

import java.awt.Point;

import wfnmodell.schnittstellen.IWFNElementKante;
import wfnmodell.schnittstellen.IWFNElementOK;

/**
 * Klasse der Kanten,
 * instanzierte Objekte dieser Klasse stellen im Datenmodell eine gerichtete Kante dar.
 *
 */
public class WfnElementArc extends AWfnElement implements IWFNElementKante {
	
	/** Ausgangs-Element, source dem die Kante ausgeht */
	private IWFNElementOK source;
	/** Element, in dem die Kante endet */
	private IWFNElementOK target;

	/**
	 * @param pnmlID wenn das Element importiert wurde, die entsprechende ID des Elements aus der pnml-Datei
	 * @param id die ID, welche das Element eindeutig unterscheidbar macht
	 * @param source Element, source dem die target instanzierende Kante ausgeht
	 * @param target Element, in dem die target instanzierende Kante endet
	 */
	public WfnElementArc(String pnmlID, int id, IWFNElementOK source, IWFNElementOK target) {
		super(pnmlID, id);
		this.source = source;
		this.target = target;
	}

	@Override
	public EWfnElement getWfnElementType() {
		return EWfnElement.ARC;
	}

	@Override
	public void setSource(IWFNElementOK elem) {
		this.source = elem;
	}

	@Override
	public IWFNElementOK getSource() {
		return this.source;
	}

	@Override
	public void setTarget(IWFNElementOK elem) {
		this.target = elem;
	}

	@Override
	public IWFNElementOK getTarget() {
		return this.target;
	}

	@Override
	public Point getCenter() {
		return new Point((source.getPosition().x + target.getPosition().x) / 2,
						 (source.getPosition().y + target.getPosition().y) / 2);
	}

}
