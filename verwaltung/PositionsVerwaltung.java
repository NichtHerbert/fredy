package verwaltung;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

import horcherschnittstellen.IElementGroessenHorcher;
import horcherschnittstellen.IWFNVeraenderungsHorcher;
import wfnmodell.WfnStatusInfo;
import wfnmodell.elements.EWfnElement;
import wfnmodell.interfaces.IWfnElement;
import wfnmodell.interfaces.IWfnArc;
import wfnmodell.interfaces.IWfnTransitionAndPlace;

/**
 * Klasse, die Methoden bereitstellt, um herauszufinden, was sich an einer bestimmten Position des
 * Workflownetzes befindet.
 *
 */
class PositionsVerwaltung implements IWFNVeraenderungsHorcher, IElementGroessenHorcher {
	
	/**
	 * Die aktuelle {@link ZoomFaktorVerwaltung}.
	 */
	private ZoomFaktorVerwaltung zoom;
	
	/**
	 * Die aktuelle Elementgroesse.
	 */
	private int elementGroesse;
	
	/**
	 * Liste aller Stellen und Transitionen des WFN.
	 */
	private ArrayList<IWfnTransitionAndPlace> alleElementeOK;
	
	/**
	 * Liste aller Kanten des WFN.
	 */
	private ArrayList<IWfnArc> alleKanten;
	
	/**
	 * Die zuletzt untersuchte Position im WFN.
	 */
	private Point wasDaIstVorherigeKoordinate;
	
	/**
	 * Das Ergebnis der letzten Untersuchung. Was sich also an der Position {@link #wasDaIstVorherigeKoordinate} befindet.
	 */
	private IWfnElement wasDaIstVorherigesErgebnis;
	
	PositionsVerwaltung(ZoomFaktorVerwaltung zoom) {
		this.zoom = zoom;
		alleElementeOK = new ArrayList<>(1);
		alleKanten = new ArrayList<>(1);
		elementGroesse = EWfnElement.BASEFACTOR;
	}

	@Override
	public void modellAenderungEingetreten(WfnStatusInfo statusInfo) {
		alleElementeOK = statusInfo.getTransitionsAndPlaces();
		alleKanten = statusInfo.getArcs();
	}

	/**
	 * Ermittelt aus der übergebenen Position, ob und wenn ja, welches Element sich dort befindet.
	 * @param koordinate die zu überprüfende Position
	 * @return das Element, welches sich an der überprüften Position befindet, oder null, wenn dort kein Element ist
	 */
	IWfnElement getWasDaIst(Point koordinate) {
		IWfnElement ergebnis = null;
		koordinate = zoom.ohne(koordinate);
		if ((wasDaIstVorherigeKoordinate == null)
				|| (! koordinate.equals(wasDaIstVorherigeKoordinate))) {
			Iterator<IWfnTransitionAndPlace> itOK = alleElementeOK.iterator();
			while ((ergebnis == null)
					&& (itOK.hasNext())){ 
				IWfnTransitionAndPlace elem = itOK.next();
				if ((elem.getPosition().x >= koordinate.x - elementGroesse) 
						&& (elem.getPosition().x <= koordinate.x + elementGroesse)
						&& (elem.getPosition().y >= koordinate.y - elementGroesse) 
						&& (elem.getPosition().y <= koordinate.y + elementGroesse))
						ergebnis = elem;
			}
			if (ergebnis == null) {
				Iterator<IWfnArc> itKante = alleKanten.iterator();
				while ((ergebnis == null)
						&& (itKante.hasNext())){ 
					IWfnArc kante = itKante.next();
					if (istPunktNaheKante(koordinate, kante, EWfnElement.BASEFACTOR / 2))
						ergebnis = kante;
				}
			}
			wasDaIstVorherigeKoordinate = koordinate;
			wasDaIstVorherigesErgebnis = ergebnis;
		} else
			ergebnis = wasDaIstVorherigesErgebnis;
		return ergebnis;
	}
	

	/**
	 * Ermittelt aus den übergebenen Eckpunkten, ob sich in dem Rechteck zwischen diesen Elemente
	 * befinden, und wenn ja, welche.
	 * @param startMausPosition der eine Eckpunkt des zu überprüfenden Rechtecks
	 * @param jetztMausPosition der andere Eckpunkt des zu überprüfenden Rechtecks
	 * @return Liste der Elemente, welche sich innerhalb des Rechtecks befinden
	 */
	ArrayList<IWfnElement> getWasDaIst(Point startMausPosition, Point jetztMausPosition) {
		if (startMausPosition.x > jetztMausPosition.x) {
			int temp = startMausPosition.x;
			startMausPosition.x = jetztMausPosition.x;
			jetztMausPosition.x = temp;
		}
		if (startMausPosition.y > jetztMausPosition.y) {
			int temp = startMausPosition.y;
			startMausPosition.y = jetztMausPosition.y;
			jetztMausPosition.y = temp;
		}
		ArrayList<IWfnElement> ergebnis = new ArrayList<>();
		for (IWfnTransitionAndPlace elem : alleElementeOK)
			if ((elem.getPosition().x >= startMausPosition.x) 
					&& (elem.getPosition().x <= jetztMausPosition.x)
					&& (elem.getPosition().y >= startMausPosition.y) 
					&& (elem.getPosition().y <= jetztMausPosition.y))
				ergebnis.add(elem);
		for (IWfnArc kante : alleKanten) {
			Point mp = kante.getCenter();
			if ((mp.x >= startMausPosition.x) 
					&& (mp.x <= jetztMausPosition.x)
					&& (mp.y >= startMausPosition.y) 
					&& (mp.y <= jetztMausPosition.y))
				ergebnis.add(kante);
		}	
		return ergebnis;
	}
	
	
	/**
	 * Methode zum Erkennen, ob ein Punkt nahe an einer Kante liegt. 
	 * @param p der Punkt
	 * @param kante die Kante
	 * @param maxabstand maximaler Abstand, den p zur Kante haben darf
	 * @return true, falls der Abstand von p zur Kante kante kleinergleich maxabstand ist
	 */
	private static boolean istPunktNaheKante(Point p, IWfnArc kante, int maxabstand) {
		Point v, z;
		v = kante.getSource().getPosition();
		z = kante.getTarget().getPosition();
		if ((p.distance(v) + p.distance(z)) 
				<= (v.distance(z) * ( 1.005))) {
			return true;
		} else 
			return false;
	}

	@Override
	public void elementGroesseGeaendert(int neueGroesse) {
		elementGroesse = neueGroesse;
	}
	
}
