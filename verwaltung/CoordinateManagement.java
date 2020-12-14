package verwaltung;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import listeners.IElementSizeListener;
import listeners.IWfnNetListener;
import wfnmodel.WfnStatusInfo;
import wfnmodel.elements.EWfnElement;
import wfnmodel.interfaces.IWfnArc;
import wfnmodel.interfaces.IWfnElement;
import wfnmodel.interfaces.IWfnTransitionAndPlace;

/**
 * Klasse, die Methoden bereitstellt, um herauszufinden, was sich an einer bestimmten Position des
 * Workflownetzes befindet.
 *
 */
class CoordinateManagement implements IWfnNetListener, IElementSizeListener {
	
	/** Die aktuelle {@link ZoomManagement}.*/
	private ZoomManagement zoom;
	/** Die aktuelle Elementgroesse.*/
	private int elementSize;
	/** Liste aller Stellen und Transitionen des WFN.*/
	private ArrayList<IWfnTransitionAndPlace> transitionsAndPlaces;
	/** Liste aller Kanten des WFN. */
	private ArrayList<IWfnArc> arcs;
	/** Die zuletzt untersuchte Position im WFN. */
	private Point lastCoordinate;
	/** Das Ergebnis der letzten Untersuchung. Was sich also an der Position {@link #lastCoordinate} befindet. */
	private IWfnElement lastResult;
	
	CoordinateManagement(ZoomManagement zoom) {
		this.zoom = zoom;
		transitionsAndPlaces = new ArrayList<>(1);
		arcs = new ArrayList<>(1);
		elementSize = EWfnElement.BASEFACTOR;
	}

	@Override
	public void netChangeOccurred(WfnStatusInfo statusInfo) {
		transitionsAndPlaces = statusInfo.getTransitionsAndPlaces();
		arcs = statusInfo.getArcs();
	}

	@Override
	public void elementSizeChanged(int newSize) {
		elementSize = newSize;
	}

	/**
	 * Ermittelt aus der übergebenen Position, ob und wenn ja, welches Element sich dort befindet.
	 * @param coord die zu überprüfende Position
	 * @return das Element, welches sich an der überprüften Position befindet, oder null, wenn dort kein Element ist
	 */
	IWfnElement getElementAt(Point coord) {
		IWfnElement result = null;
		coord = zoom.calculateOut(coord);
		if ((lastCoordinate == null)
				|| (! coord.equals(lastCoordinate))) {
			Iterator<IWfnTransitionAndPlace> itTransitionAndPlaces = transitionsAndPlaces.iterator();
			while ((result == null)
					&& (itTransitionAndPlaces.hasNext())){ 
				IWfnTransitionAndPlace elem = itTransitionAndPlaces.next();
				if ((elem.getPosition().x >= coord.x - elementSize) 
						&& (elem.getPosition().x <= coord.x + elementSize)
						&& (elem.getPosition().y >= coord.y - elementSize) 
						&& (elem.getPosition().y <= coord.y + elementSize))
					result = elem;
			}
			if (result == null) {
				Iterator<IWfnArc> itArc = arcs.iterator();
				while ((result == null)
						&& (itArc.hasNext())){ 
					IWfnArc arc = itArc.next();
					if (isCoordCloseToArc(coord, arc, EWfnElement.BASEFACTOR / 2))
						result = arc;
				}
			}
			lastCoordinate = coord;
			lastResult = result;
		} else
			result = lastResult;
		return result;
	}
	

	/**
	 * Ermittelt aus den übergebenen Eckpunkten, ob sich in dem Rechteck zwischen diesen Elemente
	 * befinden, und wenn ja, welche.
	 * @param angle1 der eine Eckpunkt des zu überprüfenden Rechtecks
	 * @param angle2 der andere Eckpunkt des zu überprüfenden Rechtecks
	 * @return Liste der Elemente, welche sich innerhalb des Rechtecks befinden
	 */
	ArrayList<IWfnElement> getElementsIn(Point angle1, Point angle2) {
		if (angle1.x > angle2.x) {
			int temp = angle1.x;
			angle1.x = angle2.x;
			angle2.x = temp;
		}
		if (angle1.y > angle2.y) {
			int temp = angle1.y;
			angle1.y = angle2.y;
			angle2.y = temp;
		}
		ArrayList<IWfnElement> result = new ArrayList<>();
		for (IWfnTransitionAndPlace elem : transitionsAndPlaces)
			if ((elem.getPosition().x >= angle1.x) 
					&& (elem.getPosition().x <= angle2.x)
					&& (elem.getPosition().y >= angle1.y) 
					&& (elem.getPosition().y <= angle2.y))
				result.add(elem);
		for (IWfnArc arc : arcs) {
			Point arcCenter = arc.getCenter();
			if ((arcCenter.x >= angle1.x) 
					&& (arcCenter.x <= angle2.x)
					&& (arcCenter.y >= angle1.y) 
					&& (arcCenter.y <= angle2.y))
				result.add(arc);
		}	
		return result;
	}
	
	
	/**
	 * Methode zum Erkennen, ob ein Punkt nahe an einer Kante liegt. 
	 * @param coord der Punkt
	 * @param arc die Kante
	 * @param maxDistance maximaler Abstand, den p zur Kante haben darf // currently not in use
	 * @return true, falls der Abstand von p zur Kante kante kleinergleich maxabstand ist
	 */
	private static boolean isCoordCloseToArc(Point coord, IWfnArc arc, int maxDistance) {
		Point sourceCoord, targetCoord;
		sourceCoord = arc.getSource().getPosition();
		targetCoord = arc.getTarget().getPosition();
		if ((coord.distance(sourceCoord) + coord.distance(targetCoord)) 
				<= (sourceCoord.distance(targetCoord) * ( 1.005))) {
			return true;
		} else 
			return false;
	}
	
}
