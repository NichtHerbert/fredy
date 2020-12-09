package wfnmodell;

import java.util.ArrayList;

import wfnmodell.schnittstellen.IWFNElementStelle;
import wfnmodell.elements.EWfnElement;
import wfnmodell.elements.WfnElementPlace;
import wfnmodell.schnittstellen.IWFNElementOK;

/**
 * Klasse zur Verwaltung der möglichen Start- und der möglichen Endstellen eines Workflownetzes.
 *
 */
class StartEndManagement {
	/**
	 * Liste der möglichen Startstellen.
	 */
	private ArrayList<IWFNElementStelle> startPlaces;
	/**
	 * Liste der möglichen Endstellen.
	 */
	private ArrayList<IWFNElementStelle> endPlaces;
	/**
	 * Die aktuelle {@link ConnectionManagement}.
	 */
	private ConnectionManagement connectionManagement;
	
	StartEndManagement(ConnectionManagement connectionManagement) {
		startPlaces = new ArrayList<>();
		endPlaces = new ArrayList<>();
		this.connectionManagement = connectionManagement;
	}

	/**
	 * Überprüft, ob durch die neue Kante Stellen aus den Listen {@link #startPlaces} und 
	 * {@link #endPlaces} gelöscht werden müssen. Wenn dies der Fall ist, wird auch die 
	 * {@link #connectionManagement} informiert.
	 * @param origin Element, von dem die neue Kante ausgeht
	 * @param ending Element, in dem die neue Kante endet
	 */
	void infoCreatedArc(IWFNElementOK origin, IWFNElementOK ending) {
		if (origin.getWfnElementType() == EWfnElement.PLACE) { 
			if (endPlaces.contains(origin)) {
				endPlaces.remove(origin);
				connectionManagement.setLostEndPath(origin);
			}
		} else {
			if (startPlaces.contains(ending)) {
				startPlaces.remove(ending);
				connectionManagement.setLostStartPath(ending);
			}
		}
	}
	
	/**
	 * Überprüft, ob durch die gelöschte Kante eine Stelle der Liste {@link #startPlaces} oder der
	 * Liste {@link #endPlaces} hinzugefügt werden muss. Wenn dies der Fall ist, wird auch die 
	 * {@link #connectionManagement} informiert.
	 * @param origin Element, von dem die gelöschte Kante ausging
	 * @param ending Element, in dem die gelöschte Kante endete
	 */
	void infoDeletedArc(IWFNElementOK origin, IWFNElementOK ending) {
		if (origin.getWfnElementType() == EWfnElement.PLACE) {
			if (!origin.hasOutgoingArcs()) {
				endPlaces.add((WfnElementPlace) origin);
				connectionManagement.setHasEndPath(origin);
			}
		} else {
			if (!ending.hasIncomingArcs()) {
				startPlaces.add((WfnElementPlace) ending);
				connectionManagement.setHasStartPath(ending);
			}
		}
	}
	
	@Override
	public String toString() {
		return "StartEndStellenVerwaltung: Anzahl Startst. = " + startPlaces.size() + " ,"
				+ "Anzahl Endst. = " + endPlaces.size() +"\n"
				+ " [startStellen=" + startPlaces +"\n"
				+ " endStellen=" + endPlaces + "]";
	}

	/**
	 * Fügt die übergebene Stelle, von der angenommen wird, dass sie neu ist und deswegen noch keine Kanten
	 * anliegen hat, den Listen {@link #startPlaces} und {@link #endPlaces} hinzu und informiert
	 * die {@link #connectionManagement}.
	 * @param place neues WFN-Element Stelle
	 */
	void add(WfnElementPlace place) {
		startPlaces.add(place);
		endPlaces.add(place);
		connectionManagement.setHasStartPath(place);
		connectionManagement.setHasEndPath(place);
	}

	/**
	 * Entfernt die übergebene WFN-Stelle aus den Listen {@link #endPlaces} und {@link #startPlaces}.
	 * @param place zu entfernende WFN-Stelle
	 */
	void remove(WfnElementPlace place) {
		if (startPlaces.contains(place))
			startPlaces.remove(place);
		if (endPlaces.contains(place))
			endPlaces.remove(place);
	}
	
	/**
	 * Gibt die Anzahl der möglichen Startstellen zurück.
	 * @return die Länge der Liste {@link #startPlaces}
	 */
	int getStartPlacesNumber() {
		return startPlaces.size();
	}

	/**
	 * Gibt die Anzahl der möglichen Endstellen zurück.
	 * @return die Länge der Liste {@link #endPlaces}
	 */
	int getEndPlacesNumber() {
		return endPlaces.size();
	}
	
	/**
	 * Gibt zurück, ob es nur eine mögliche Startstelle gibt.
	 * @return true, wenn es genau eine mögliche Startstelle gibt, sonst false
	 */
	boolean hasUniqueStart() {
		return (startPlaces.size() == 1);
	}

	/**
	 * Gibt zurück, ob es nur eine mögliche Endstelle gibt.
	 * @return true, wenn es genau eine mögliche Endstelle gibt, sonst false
	 */
	boolean hasUniqueEnd() {
		return (endPlaces.size() == 1);
	}

	/**
	 * @return true, wenn es nur eine Start- und eine Endstelle gibt, sonst false
	 */
	boolean hasUniqueStartAndEnd() {
		return hasUniqueStart() && hasUniqueEnd();	
	}

	/**
	 * Gibt es eine eindeutige Startstelle, wird eine Referenz auf sie zurückgegeben.
	 * @return die Startstelle, oder null, wenn es keine eindeutige gibt
	 */
	IWFNElementStelle getUniqueStart() {
		if (hasUniqueStart())
			return startPlaces.get(0);
		else
			return null;
	}

	/**
	 * Gibt es eine eindeutige Endstelle, wird eine Referenz auf sie zurückgegeben.
	 * @return die Endstelle, oder null, wenn es keine eindeutige gibt
	 */
	IWFNElementStelle getUniqueEnd() {
		if (hasUniqueEnd())
			return endPlaces.get(0);
		else
			return null;
	}

	/**
	 * Gibt eine Liste der möglichen Startstellen zurück.
	 * @return {@link #startPlaces}
	 */
	ArrayList<IWFNElementStelle> getStartPlaces() {
		return startPlaces;
	}

	/**
	 * Gibt eine Liste der möglichen Endstellen zurück.
	 * @return {@link #endPlaces}
	 */
	ArrayList<IWFNElementStelle> getEndPlaces() {
		return endPlaces;
	}
	
	
}
