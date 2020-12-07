package wfnmodell.elemente;

import java.awt.Point;
import java.util.ArrayList;
import wfnmodell.schnittstellen.IWFNElementOK;

/**
 * Abstrakte Klasse, in der all diejenigen Methoden und Attribute versammelt sind, die 
 * Stellen und Transitionen gemeinsam haben. 
 */
abstract class AWFNElementOK extends AWFNElement implements IWFNElementOK {

	/** Der Name des Elements */
	private String name;
	/** Die Koordinaten des Elements */
	private Point position;
	/** Liste von Elementen mit ausgehenden Kanten, die in diesem Element enden. */ 
	protected ArrayList<IWFNElementOK> kantenVon;
	/** Liste von Elementen mit eingehenden Kanten, die von diesem Element ausgehen. */
	protected ArrayList<IWFNElementOK> kantenZu;
	/** true, wenn es zu diesem Element mindestens einen gerichteten Pfad von mindestens einer möglichen Startstelle gibt. */  
	protected boolean pfadVomStart;
	/** true, wenn von diesem Element mindestens einen gerichteten Pfad zu mindestens einer möglichen Endstelle gibt. */
	protected boolean pfadZumEnde;
	
	/**
	 * @param identifier die ID, welche das Element eindeutig unterscheidbar macht
	 * @param position die vorgesehenen Koordinaten des Elements
	 */
	public AWFNElementOK(int identifier, Point position) {
		this("", identifier, position);
	}
	
	/**
	 * @param pnmlID wenn das Element importiert wurde, die entsprechende ID des Elements aus der pnml-Datei
	 * @param identifier die ID, welche das Element eindeutig unterscheidbar macht
	 * @param position die vorgesehenen Koordinaten des Elements
	 */
	public AWFNElementOK(String pnmlID, int identifier, Point position) {
		super(pnmlID, identifier);
		this.position = position;
		name = "";
		kantenVon = new ArrayList<IWFNElementOK>(2);
		kantenZu = new ArrayList<IWFNElementOK>(2);
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setPosition(Point point) {
		this.position = point;
	}

	@Override
	public Point getPosition() {
		return this.position;
	}
	
	@Override
	public boolean hatEingehendeKanten() {
		return (this.kantenVon.size() > 0) ? true : false;
	}
	
	@Override
	public boolean hatAusgehendeKanten() {
		return (this.kantenZu.size() > 0) ? true : false;
	}

	@Override
	public boolean addKanteVon(IWFNElementOK elem) {
		boolean elemTypUngleichThis;
		if (this.getTyp() != elem.getTyp()) {
			this.kantenVon.add(elem);
			elemTypUngleichThis = true;
		} else
			elemTypUngleichThis = false;
		return elemTypUngleichThis;
	}
	
	@Override
	public void removeKanteVon(IWFNElementOK elem) {
		if (kantenVon.contains(elem)) 
			kantenVon.remove(elem);
	}

	@Override
	public ArrayList<IWFNElementOK> getKantenVon() {
		return new ArrayList<IWFNElementOK>(kantenVon);
	}

	@Override
	public boolean addKanteZu(IWFNElementOK elem) {
		boolean elemTypUngleichThis;
		if (this.getTyp() != elem.getTyp()) {
			this.kantenZu.add(elem);
			elemTypUngleichThis = true;
		} else
			elemTypUngleichThis = false;
		return elemTypUngleichThis;
	}
	
	@Override
	public void removeKanteZu(IWFNElementOK elem) {
		if (kantenZu.contains(elem)) 
			kantenZu.remove(elem);
	}

	@Override
	public ArrayList<IWFNElementOK> getKantenZu() {
		return new ArrayList<IWFNElementOK>(kantenZu);
	}

	@Override
	public boolean istAufPfadVomStart() {
		return pfadVomStart;
	}

	@Override
	public boolean istAufPfadZumEnde() {
		return pfadZumEnde;
	}
	
	@Override
	public void setPfadVomStart(boolean pfadVomStart) {
		this.pfadVomStart = pfadVomStart;
	}
	
	@Override
	public void setPfadZumEnde(boolean pfadZumEnde) {
		this.pfadZumEnde = pfadZumEnde;
	}

	@Override
	public String toString() {
		return (name.equals(""))
				? getTyp() + " " + getID()
				: name;
	}
	
	
}
