package wfnmodell.elements;

import java.awt.Point;
import java.util.ArrayList;
import wfnmodell.schnittstellen.IWFNElementOK;

/**
 * Abstrakte Klasse, in der all diejenigen Methoden und Attribute versammelt sind, die 
 * Stellen und Transitionen gemeinsam haben. 
 */
abstract class AWfnElementButNoArc extends AWfnElement implements IWFNElementOK {

	/** Der Name des Elements */
	private String name;
	/** Die Koordinaten des Elements */
	private Point position;
	/** Liste von Elementen mit ausgehenden Kanten, die in diesem Element enden. */ 
	protected ArrayList<IWFNElementOK> inputElements;
	/** Liste von Elementen mit eingehenden Kanten, die von diesem Element ausgehen. */
	protected ArrayList<IWFNElementOK> outputElements;
	/** true, wenn es zu diesem Element mindestens einen gerichteten Pfad von mindestens einer möglichen Startstelle gibt. */  
	protected boolean hasStartPath;
	/** true, wenn von diesem Element mindestens einen gerichteten Pfad zu mindestens einer möglichen Endstelle gibt. */
	protected boolean hasEndPath;
	
	/**
	 * @param id die ID, welche das Element eindeutig unterscheidbar macht
	 * @param position die vorgesehenen Koordinaten des Elements
	 */
	public AWfnElementButNoArc(int id, Point position) {
		this("", id, position);
	}
	
	/**
	 * @param pnmlID wenn das Element importiert wurde, die entsprechende ID des Elements aus der pnml-Datei
	 * @param id die ID, welche das Element eindeutig unterscheidbar macht
	 * @param position die vorgesehenen Koordinaten des Elements
	 */
	public AWfnElementButNoArc(String pnmlID, int id, Point position) {
		super(pnmlID, id);
		this.position = position;
		name = "";
		inputElements = new ArrayList<IWFNElementOK>(2);
		outputElements = new ArrayList<IWFNElementOK>(2);
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
	public boolean hasIncomingArcs() {
		return (this.inputElements.size() > 0);
	}
	
	@Override
	public boolean hasOutgoingArcs() {
		return (this.outputElements.size() > 0);
	}

	@Override
	public boolean addInputElement(IWFNElementOK source) {
		if (this.getWfnElementType() != source.getWfnElementType()) {
			this.inputElements.add(source);
			return true;
		}
		return false;
	}
	
	@Override
	public void removeInputElement(IWFNElementOK source) {
		if (inputElements.contains(source)) 
			inputElements.remove(source);
	}

	@Override
	public ArrayList<IWFNElementOK> getInputElements() {
		return new ArrayList<IWFNElementOK>(inputElements);
	}

	@Override
	public boolean addOutputElements(IWFNElementOK target) {
		if (this.getWfnElementType() != target.getWfnElementType()) {
			this.outputElements.add(target);
			return true;
		} 
		return false;
	}
	
	@Override
	public void removeOutputElements(IWFNElementOK target) {
		if (outputElements.contains(target)) 
			outputElements.remove(target);
	}

	@Override
	public ArrayList<IWFNElementOK> getOutputElements() {
		return new ArrayList<IWFNElementOK>(outputElements);
	}

	@Override
	public boolean hasStartPath() {
		return hasStartPath;
	}

	@Override
	public boolean hasEndPath() {
		return hasEndPath;
	}
	
	@Override
	public void setHasStartPath(boolean hasStartPath) {
		this.hasStartPath = hasStartPath;
	}
	
	@Override
	public void setHasEndPath(boolean hasEndPath) {
		this.hasEndPath = hasEndPath;
	}

	@Override
	public String toString() {
		return (name.equals(""))
				? getWfnElementType() + " " + getID()
				: name;
	}
	
	
}
