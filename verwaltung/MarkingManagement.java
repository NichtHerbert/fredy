package verwaltung;

import java.util.ArrayList;

import listeners.ITransitionFireListener;
import listeners.IWfnStatusListener;
import listeners.IWfnNetListener;
import wfnmodel.WfnStatusInfo;
import wfnmodel.elements.EWfnElement;
import wfnmodel.interfaces.IWfnElement;
import wfnmodel.interfaces.IWfnPlace;
import wfnmodel.interfaces.IWfnTransition;
import wfnmodel.interfaces.IWfnTransitionAndPlace;

/**
 * Klasse, die die markierten Stellen, die aktivierten Transitionen und das Schalten
 * von Transitionen verwaltet.
 *
 */
class MarkingManagement implements IWfnNetListener,
									   ITransitionFireListener {
	/** Der letztübermittelte Zustand/Status des WFN*/
	private WfnStatusInfo statusInfo;
	/** Liste der markierten Stellen */
	private ArrayList<IWfnPlace> placesWithMarking;
	/** Liste der aktivierten Transitionen*/
	private ArrayList<IWfnTransition> enabledTransitions;
	/** Liste der Transitionen mit Kontakt*/
	private ArrayList<IWfnTransition> contactTransitions;
	/** Liste der Horcher, die über eine Veränderung des Modell-Status informiert werden möchten.*/ 
	private ArrayList<IWfnStatusListener> wfnStatusListeners;
	/** Satz, mit dem dem Benutzer ein Deadlock mitgeteilt wird. */ 
	private final String deadlock = "DEADLOCK!";
	/** Satz, mit dem dem Benutzer das Erreichen der Endposition mitgeteilt wird. */
	private final String reachedEnd = "Regular end marking reached!";
	
	MarkingManagement() {
		placesWithMarking = new ArrayList<>();
		enabledTransitions = new ArrayList<>();
		contactTransitions = new ArrayList<>();
		wfnStatusListeners = new ArrayList<>(2);
	}
	
	/**
	 * Wenn das übergebene Element eine aktivierte Transition ist, wird diese geschaltet,
	 * und Marken werden bewegt.
	 * @param element die zu schaltende Transition
	 */
	void fire(IWfnElement element) {
		if ((element != null)
				&& (statusInfo.isWfn())
				&& (element.getWfnElementType() == EWfnElement.TRANSITION)
				&& (enabledTransitions.contains(element))) {
			removeMarkingOfInputPlaces(element);
			for (IWfnTransitionAndPlace placeForwards : ((IWfnTransitionAndPlace) element).getOutputElements()) {
				((IWfnPlace) placeForwards).setMarking(true);
				placesWithMarking.add((IWfnPlace) placeForwards);
				for (IWfnTransitionAndPlace transitionForwards : ((IWfnTransitionAndPlace) placeForwards).getOutputElements()) 
					setNewMarkingInput(transitionForwards);
				for (IWfnTransitionAndPlace transitionSidewards : ((IWfnTransitionAndPlace) placeForwards).getInputElements())
					if ((enabledTransitions.contains(transitionSidewards))
							&& (!placeForwards.getOutputElements().contains(transitionSidewards))){
						enabledTransitions.remove(transitionSidewards);
						contactTransitions.add((IWfnTransition) transitionSidewards);
					}
			}
			updateStatusInfo();
			fireNewWfnStatus();
		}
	}

	/**
	 * Überprüft, ob bei einer Transition die Bedingungen für eine Aktivierung erfüllt sind,
	 * und wenn ja, ob Kontakt besteht.
	 * @param transition zu überprüfende Transition
	 */
	private void setNewMarkingInput(IWfnTransitionAndPlace transition) {
		boolean isEnabled = true;
		for (IWfnTransitionAndPlace placeBackwards : transition.getInputElements())
			if (! ((IWfnPlace) placeBackwards).hasMarking())
				isEnabled = false;
		if (isEnabled) {
			boolean hasContact = false;
			for (IWfnTransitionAndPlace placeForwards : transition.getOutputElements())
				if ((((IWfnPlace) placeForwards).hasMarking())
						&& ((placeForwards.getOutputElements()).contains(transition))==false)
					hasContact = true;
			if (hasContact) 
				contactTransitions.add((IWfnTransition) transition);
			else
				enabledTransitions.add((IWfnTransition) transition);
		}
	}

	/**
	 * Entfernt die Marken der Eingangsstellen einer Transition und überprüft die Auswirkungen
	 * für benachbarte Elemente.
	 * @param element Transition, deren Eingansstellen unmarkiert werden sollen
	 */
	private void removeMarkingOfInputPlaces(IWfnElement element) {
		for (IWfnTransitionAndPlace placeBackwards : ((IWfnTransitionAndPlace) element).getInputElements()) {
			((IWfnPlace) placeBackwards).setMarking(false);
			placesWithMarking.remove(placeBackwards);
			for (IWfnTransitionAndPlace placeBackwardOutTransitions : ((IWfnTransitionAndPlace) placeBackwards).getOutputElements()) {
				if (enabledTransitions.contains(placeBackwardOutTransitions))
					enabledTransitions.remove(placeBackwardOutTransitions);
				else
					if (contactTransitions.contains(placeBackwardOutTransitions))
						contactTransitions.remove(placeBackwardOutTransitions);
			}
			for (IWfnTransitionAndPlace placeBackwardInTransitions : ((IWfnTransitionAndPlace) placeBackwards).getInputElements())
				if (contactTransitions.contains(placeBackwardInTransitions)) {
					boolean hasContact = false;
					for (IWfnTransitionAndPlace placeBackwardInTransitionOutPlace : placeBackwardInTransitions.getOutputElements())
						if (((IWfnPlace)placeBackwardInTransitionOutPlace).hasMarking())
							hasContact = true;
					if (!hasContact) {
						enabledTransitions.add((IWfnTransition) placeBackwardInTransitions);
						contactTransitions.remove(placeBackwardInTransitions);
					}
				}
		}
	}
	
	@Override
	public void netChangeOccurred(WfnStatusInfo statusInfo) {
		this.statusInfo = statusInfo;
		if (statusInfo.isWfn()) {
			allMarkingsBackToStart();
			updateStatusInfo();
		}
		fireNewWfnStatus();
	}
	
	/**
	 * Fügt den übergebenen Horcher der {@link #wfnStatusListeners} hinzu.
	 * @param listener wird {@link #wfnStatusListeners} hinzugefügt
	 */
	void addWfnStatusListener(IWfnStatusListener listener) {
		wfnStatusListeners.add(listener);
	}
	
	/**
	 * Entfernt den übergebenen Horcher von der {@link #wfnStatusListeners}.
	 * @param listener wird von der {@link #wfnStatusListeners} entfernt
	 */
	void removeWfnStatusListener(IWfnStatusListener listener) {
		if (wfnStatusListeners.contains(listener)) 
			wfnStatusListeners.remove(listener);
	}

	/**
	 * Übersendet allen Horchern der {@link #wfnStatusListeners} das aktuelle {@link #statusInfo}.
	 */
	private void fireNewWfnStatus() {
		for (IWfnStatusListener listener : wfnStatusListeners)
			listener.newWfnStatus(statusInfo);	
	}
	
	/**
	 * Löscht alle Markierungen, aktivierten und Kontakt-Transitionen,
	 * und markiert nur die Startstelle, sowie die dadurch aktivierten Transitionen.
	 */
	private void allMarkingsBackToStart() {
		placesWithMarking.clear();
		enabledTransitions.clear();
		contactTransitions.clear();
		for (IWfnTransitionAndPlace elem : statusInfo.getTransitionsAndPlaces()) 
			if (elem.getWfnElementType() == EWfnElement.PLACE) 
				((IWfnPlace)elem).setMarking(false);
		statusInfo.getStartPlace().setMarking(true);
		placesWithMarking.add(statusInfo.getStartPlace());
		for (IWfnTransitionAndPlace folgeTransition : statusInfo.getStartPlace().getOutputElements())
			setNewMarkingInput(folgeTransition);
		while (statusInfo.getNotWfnExplanatoryStatements().contains(reachedEnd))
			statusInfo.removeNotWfnExplanatoryStatements(reachedEnd);
		while (statusInfo.getNotWfnExplanatoryStatements().contains(deadlock))
			statusInfo.removeNotWfnExplanatoryStatements(deadlock);
	}
	
	/**
	 * Aktualisiert {@link #statusInfo} mit den aktuellen Listen:
	 * {@link #placesWithMarking}
	 * {@link #enabledTransitions}
	 * {@link #contactTransitions}
	 */
	private void updateStatusInfo() {
		statusInfo.setMarkings(placesWithMarking);
		if ((enabledTransitions.size() == 0)
				&& (placesWithMarking.size() > 0)) {
			if ((placesWithMarking.size() == 1)
					&& (placesWithMarking.contains(statusInfo.getEndPlace()))
					&& (statusInfo.getStartPlace() != statusInfo.getEndPlace())
					&& (!statusInfo.getNotWfnExplanatoryStatements().contains(reachedEnd)))
				statusInfo.addNotWfnExplanatoryStatements(reachedEnd);
			else
				if ((!statusInfo.getNotWfnExplanatoryStatements().contains(deadlock))
						&& (statusInfo.getStartPlace() != statusInfo.getEndPlace()))
					statusInfo.addNotWfnExplanatoryStatements(deadlock);
		}
		statusInfo.setEnabledTransitions(enabledTransitions);
		statusInfo.setContactTransitions(contactTransitions);
	}

	@Override
	public void everythingBackToStart() {
		allMarkingsBackToStart();
		updateStatusInfo();
		fireNewWfnStatus();
	}

	@Override
	public void fireTransition(IWfnTransition transition) {
		fire(transition);
	}
}
