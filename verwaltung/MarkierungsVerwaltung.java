package verwaltung;

import java.util.ArrayList;

import horcherschnittstellen.ITransitionsSchaltungsHorcher;
import horcherschnittstellen.IWFNModellStatusHorcher;
import horcherschnittstellen.IWFNVeraenderungsHorcher;
import wfnmodell.WfnStatusInfo;
import wfnmodell.elements.EWfnElement;
import wfnmodell.interfaces.IWfnElement;
import wfnmodell.interfaces.IWfnTransitionAndPlace;
import wfnmodell.interfaces.IWfnPlace;
import wfnmodell.interfaces.IWfnTransition;

/**
 * Klasse, die die markierten Stellen, die aktivierten Transitionen und das Schalten
 * von Transitionen verwaltet.
 *
 */
class MarkierungsVerwaltung implements IWFNVeraenderungsHorcher,
									   ITransitionsSchaltungsHorcher {
	/** Der letztübermittelte Zustand/Status des WFN*/
	private WfnStatusInfo statusInfo;
	/** Liste der markierten Stellen */
	private ArrayList<IWfnPlace> markierungsListe;
	/** Liste der aktivierten Transitionen*/
	private ArrayList<IWfnTransition> aktivierteTransitionen;
	/** Liste der Transitionen mit Kontakt*/
	private ArrayList<IWfnTransition> kontaktTransitionen;
	/** Liste der Horcher, die über eine Veränderung des Modell-Status informiert werden möchten.*/ 
	private ArrayList<IWFNModellStatusHorcher> wfnModellStatusHorcherListe;
	/** Satz, mit dem dem Benutzer ein Deadlock mitgeteilt wird. */ 
	private final String deadlock = "DEADLOCK!";
	/** Satz, mit dem dem Benutzer das Erreichen der Endposition mitgeteilt wird. */
	private final String ende_erreicht = "Reguläre Endmarkierung erreicht!";
	
	MarkierungsVerwaltung() {
		markierungsListe = new ArrayList<>();
		aktivierteTransitionen = new ArrayList<>();
		kontaktTransitionen = new ArrayList<>();
		wfnModellStatusHorcherListe = new ArrayList<>(2);
	}
	
	/**
	 * Wenn das übergebene Element eine aktivierte Transition ist, wird diese geschaltet,
	 * und Marken werden bewegt.
	 * @param element die zu schaltende Transition
	 */
	void schalteWennElementTransition(IWfnElement element) {
		if ((element != null)
				&& (statusInfo.isWfn())
				&& (element.getWfnElementType() == EWfnElement.TRANSITION)
				&& (aktivierteTransitionen.contains(element))) {
			entferneMarkeDerEingangsstellen(element);
			for (IWfnTransitionAndPlace stelleDanach : ((IWfnTransitionAndPlace) element).getOutputElements()) {
				((IWfnPlace) stelleDanach).setMarking(true);
				markierungsListe.add((IWfnPlace) stelleDanach);
				for (IWfnTransitionAndPlace transitionDanach : ((IWfnTransitionAndPlace) stelleDanach).getOutputElements()) 
					transitionHatNeueMarkierteEingangsstelle(transitionDanach);
				for (IWfnTransitionAndPlace transitionVorStelleDanach : ((IWfnTransitionAndPlace) stelleDanach).getInputElements())
					if ((aktivierteTransitionen.contains(transitionVorStelleDanach))
							&& (!stelleDanach.getOutputElements().contains(transitionVorStelleDanach))){
						aktivierteTransitionen.remove(transitionVorStelleDanach);
						kontaktTransitionen.add((IWfnTransition) transitionVorStelleDanach);
					}
			}
			statusInfoAktualisieren();
			fireModellStatusAenderung();
		}
	}

	/**
	 * Überprüft, ob bei einer Transition die Bedingungen für eine Aktivierung erfüllt sind,
	 * und wenn ja, ob Kontakt besteht.
	 * @param transition zu überprüfende Transition
	 */
	private void transitionHatNeueMarkierteEingangsstelle(IWfnTransitionAndPlace transition) {
		boolean istAktiviert = true;
		for (IWfnTransitionAndPlace stelleVorTransition : transition.getInputElements())
			if (! ((IWfnPlace) stelleVorTransition).hasMarking())
				istAktiviert = false;
		if (istAktiviert) {
			boolean hatKontakt = false;
			for (IWfnTransitionAndPlace stelleNachTransition : transition.getOutputElements())
				if ((((IWfnPlace) stelleNachTransition).hasMarking())
						&& ((stelleNachTransition.getOutputElements()).contains(transition))==false)
					hatKontakt = true;
			if (hatKontakt) 
				kontaktTransitionen.add((IWfnTransition) transition);
			else
				aktivierteTransitionen.add((IWfnTransition) transition);
		}
	}

	/**
	 * Entfernt die Marken der Eingangsstellen einer Transition und überprüft die Auswirkungen
	 * für benachbarte Elemente.
	 * @param element Transition, deren Eingansstellen unmarkiert werden sollen
	 */
	private void entferneMarkeDerEingangsstellen(IWfnElement element) {
		for (IWfnTransitionAndPlace stelleDavor : ((IWfnTransitionAndPlace) element).getInputElements()) {
			((IWfnPlace) stelleDavor).setMarking(false);
			markierungsListe.remove(stelleDavor);
			for (IWfnTransitionAndPlace transitionDanach : ((IWfnTransitionAndPlace) stelleDavor).getOutputElements()) {
				if (aktivierteTransitionen.contains(transitionDanach))
					aktivierteTransitionen.remove(transitionDanach);
				else
					if (kontaktTransitionen.contains(transitionDanach))
						kontaktTransitionen.remove(transitionDanach);
			}
			for (IWfnTransitionAndPlace transitionDavor : ((IWfnTransitionAndPlace) stelleDavor).getInputElements())
				if (kontaktTransitionen.contains(transitionDavor)) {
					boolean kontakt = false;
					for (IWfnTransitionAndPlace stelleNachTransitionDavor : transitionDavor.getOutputElements())
						if (((IWfnPlace)stelleNachTransitionDavor).hasMarking())
							kontakt = true;
					if (!kontakt) {
						aktivierteTransitionen.add((IWfnTransition) transitionDavor);
						kontaktTransitionen.remove(transitionDavor);
					}
				}
		}
	}
	
	@Override
	public void modellAenderungEingetreten(WfnStatusInfo statusInfo) {
		this.statusInfo = statusInfo;
		if (statusInfo.isWfn()) {
			markenAlleZurueckAufStart();
			statusInfoAktualisieren();
		}
		fireModellStatusAenderung();
	}
	
	/**
	 * Fügt den übergebenen Horcher der {@link #wfnModellStatusHorcherListe} hinzu.
	 * @param horcher wird {@link #wfnModellStatusHorcherListe} hinzugefügt
	 */
	void addModellStatusHorcher(IWFNModellStatusHorcher horcher) {
		wfnModellStatusHorcherListe.add(horcher);
	}
	
	/**
	 * Entfernt den übergebenen Horcher von der {@link #wfnModellStatusHorcherListe}.
	 * @param horcher wird von der {@link #wfnModellStatusHorcherListe} entfernt
	 */
	void removeModellStatusHorcher(IWFNModellStatusHorcher horcher) {
		if (wfnModellStatusHorcherListe.contains(horcher)) 
			wfnModellStatusHorcherListe.remove(horcher);
	}

	/**
	 * Übersendet allen Horchern der {@link #wfnModellStatusHorcherListe} das aktuelle {@link #statusInfo}.
	 */
	private void fireModellStatusAenderung() {
		for (IWFNModellStatusHorcher horcher : wfnModellStatusHorcherListe)
			horcher.modellStatusAenderung(statusInfo);	
	}
	
	/**
	 * Löscht alle Markierungen, aktivierten und Kontakt-Transitionen,
	 * und markiert nur die Startstelle, sowie die dadurch aktivierten Transitionen.
	 */
	private void markenAlleZurueckAufStart() {
		markierungsListe.clear();
		aktivierteTransitionen.clear();
		kontaktTransitionen.clear();
		for (IWfnTransitionAndPlace elem : statusInfo.getTransitionsAndPlaces()) 
			if (elem.getWfnElementType() == EWfnElement.PLACE) 
				((IWfnPlace)elem).setMarking(false);
		statusInfo.getStartPlace().setMarking(true);
		markierungsListe.add(statusInfo.getStartPlace());
		for (IWfnTransitionAndPlace folgeTransition : statusInfo.getStartPlace().getOutputElements())
			transitionHatNeueMarkierteEingangsstelle(folgeTransition);
		while (statusInfo.getNotWfnExplanatoryStatements().contains(ende_erreicht))
			statusInfo.removeNotWfnExplanatoryStatements(ende_erreicht);
		while (statusInfo.getNotWfnExplanatoryStatements().contains(deadlock))
			statusInfo.removeNotWfnExplanatoryStatements(deadlock);
	}
	
	/**
	 * Aktualisiert {@link #statusInfo} mit den aktuellen Listen:
	 * {@link #markierungsListe}
	 * {@link #aktivierteTransitionen}
	 * {@link #kontaktTransitionen}
	 */
	private void statusInfoAktualisieren() {
		statusInfo.setMarkings(markierungsListe);
		if ((aktivierteTransitionen.size() == 0)
				&& (markierungsListe.size() > 0)) {
			if ((markierungsListe.size() == 1)
					&& (markierungsListe.contains(statusInfo.getEndPlace()))
					&& (statusInfo.getStartPlace() != statusInfo.getEndPlace())
					&& (!statusInfo.getNotWfnExplanatoryStatements().contains(ende_erreicht)))
				statusInfo.addNotWfnExplanatoryStatements(ende_erreicht);
			else
				if ((!statusInfo.getNotWfnExplanatoryStatements().contains(deadlock))
						&& (statusInfo.getStartPlace() != statusInfo.getEndPlace()))
					statusInfo.addNotWfnExplanatoryStatements(deadlock);
		}
		statusInfo.setEnabledTransitions(aktivierteTransitionen);
		statusInfo.setContactTransitions(kontaktTransitionen);
	}

	@Override
	public void allesZurueckAufStart() {
		markenAlleZurueckAufStart();
		statusInfoAktualisieren();
		fireModellStatusAenderung();
	}

	@Override
	public void schalteTransition(IWfnTransition transition) {
		schalteWennElementTransition(transition);
	}
}
