package verwaltung;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	IWfnElement getElementAt(Point coordWithZoomFactor) {
		final Point coord = zoom.calculateOut(coordWithZoomFactor);
		if (lastCoordinate != null && coord.equals(lastCoordinate)) 
			return lastResult;
		
		IWfnElement result = transitionsAndPlaces.parallelStream()
			.filter(elem -> ((elem.getPosition().x >= coord.x - elementSize) 
					&& (elem.getPosition().x <= coord.x + elementSize)
					&& (elem.getPosition().y >= coord.y - elementSize) 
					&& (elem.getPosition().y <= coord.y + elementSize)))
			.findFirst().orElse(null);
		result = (result != null) ? result : arcs.parallelStream()
			.filter(arc -> isCoordCloseToArc(coord, arc, EWfnElement.BASEFACTOR / 2))
			.findFirst().orElse(null);

		lastCoordinate = coord;
		lastResult = result;
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
		return Stream.concat(transitionsAndPlaces.parallelStream(), arcs.parallelStream())
				.filter(elem -> {
					final Point coord = (elem.getWfnElementType() == EWfnElement.ARC)
							? ((IWfnArc) elem).getCenter()
							: ((IWfnTransitionAndPlace) elem).getPosition();
					return ((Math.abs(angle1.x - angle2.x) >= Math.abs(angle1.x - coord.x) + Math.abs(angle2.x - coord.x))
						&& (Math.abs(angle1.y - angle2.y) >= Math.abs(angle1.y - coord.y) + Math.abs(angle2.y - coord.y)));
				})
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	
	/**
	 * Methode zum Erkennen, ob ein Punkt nahe an einer Kante liegt. 
	 * @param coord der Punkt
	 * @param arc die Kante
	 * @param maxDistance maximaler Abstand, den p zur Kante haben darf // currently not in use
	 * @return true, falls der Abstand von p zur Kante kante kleinergleich maxabstand ist
	 */
	private static boolean isCoordCloseToArc(Point coord, IWfnArc arc, int maxDistance) {
		final Point sourceCoord, targetCoord;
		sourceCoord = arc.getSource().getPosition();
		targetCoord = arc.getTarget().getPosition();
		if ((coord.distance(sourceCoord) + coord.distance(targetCoord)) 
				<= (sourceCoord.distance(targetCoord) * ( 1.005))) 
			return true;
		
		return false;
	}
	
}
