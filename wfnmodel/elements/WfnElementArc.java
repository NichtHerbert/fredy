package wfnmodel.elements;

import java.awt.Point;

import wfnmodel.interfaces.IWfnArc;
import wfnmodel.interfaces.IWfnTransitionAndPlace;

/**
 * Klasse der Kanten,
 * instanzierte Objekte dieser Klasse stellen im Datenmodell eine gerichtete Kante dar.
 *
 */
public class WfnElementArc extends AWfnElement implements IWfnArc {
	
	/** Ausgangs-Element, source dem die Kante ausgeht */
	private IWfnTransitionAndPlace source;
	/** Element, in dem die Kante endet */
	private IWfnTransitionAndPlace target;

	/**
	 * @param pnmlID wenn das Element importiert wurde, die entsprechende ID des Elements aus der pnml-Datei
	 * @param id die ID, welche das Element eindeutig unterscheidbar macht
	 * @param source Element, source dem die target instanzierende Kante ausgeht
	 * @param target Element, in dem die target instanzierende Kante endet
	 */
	public WfnElementArc(String pnmlID, int id, IWfnTransitionAndPlace source, IWfnTransitionAndPlace target) {
		super(pnmlID, id);
		this.source = source;
		this.target = target;
	}

	@Override
	public EWfnElement getWfnElementType() {
		return EWfnElement.ARC;
	}

	@Override
	public void setSource(IWfnTransitionAndPlace elem) {
		this.source = elem;
	}

	@Override
	public IWfnTransitionAndPlace getSource() {
		return this.source;
	}

	@Override
	public void setTarget(IWfnTransitionAndPlace elem) {
		this.target = elem;
	}

	@Override
	public IWfnTransitionAndPlace getTarget() {
		return this.target;
	}

	@Override
	public Point getCenter() {
		return new Point((source.getPosition().x + target.getPosition().x) / 2,
						 (source.getPosition().y + target.getPosition().y) / 2);
	}

}
