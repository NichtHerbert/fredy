package verwaltung;

import java.util.ArrayList;

import horcherschnittstellen.ITransitionsSchaltungsHorcher;
import horcherschnittstellen.IWFNModellStatusHorcher;
import horcherschnittstellen.IWFNVeraenderungsHorcher;
import wfnmodell.WFNStatusInfo;
import wfnmodell.elemente.EWFNElement;
import wfnmodell.schnittstellen.IWFNElement;
import wfnmodell.schnittstellen.IWFNElementStelle;
import wfnmodell.schnittstellen.IWFNElementTransition;
import wfnmodell.schnittstellen.IWFNElementOK;

/**
 * Klasse, die die markierten Stellen, die aktivierten Transitionen und das Schalten
 * von Transitionen verwaltet.
 *
 */
class MarkierungsVerwaltung implements IWFNVeraenderungsHorcher,
									   ITransitionsSchaltungsHorcher {
	/** Der letztübermittelte Zustand/Status des WFN*/
	private WFNStatusInfo statusInfo;
	/** Liste der markierten Stellen */
	private ArrayList<IWFNElementStelle> markierungsListe;
	/** Liste der aktivierten Transitionen*/
	private ArrayList<IWFNElementTransition> aktivierteTransitionen;
	/** Liste der Transitionen mit Kontakt*/
	private ArrayList<IWFNElementTransition> kontaktTransitionen;
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
	void schalteWennElementTransition(IWFNElement element) {
		if ((element != null)
				&& (statusInfo.istWFN())
				&& (element.getTyp() == EWFNElement.TRANSITION)
				&& (aktivierteTransitionen.contains(element))) {
			entferneMarkeDerEingangsstellen(element);
			for (IWFNElementOK stelleDanach : ((IWFNElementOK) element).getKantenZu()) {
				((IWFNElementStelle) stelleDanach).setMarke(true);
				markierungsListe.add((IWFNElementStelle) stelleDanach);
				for (IWFNElementOK transitionDanach : ((IWFNElementOK) stelleDanach).getKantenZu()) 
					transitionHatNeueMarkierteEingangsstelle(transitionDanach);
				for (IWFNElementOK transitionVorStelleDanach : ((IWFNElementOK) stelleDanach).getKantenVon())
					if ((aktivierteTransitionen.contains(transitionVorStelleDanach))
							&& (!stelleDanach.getKantenZu().contains(transitionVorStelleDanach))){
						aktivierteTransitionen.remove(transitionVorStelleDanach);
						kontaktTransitionen.add((IWFNElementTransition) transitionVorStelleDanach);
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
	private void transitionHatNeueMarkierteEingangsstelle(IWFNElementOK transition) {
		boolean istAktiviert = true;
		for (IWFNElementOK stelleVorTransition : transition.getKantenVon())
			if (! ((IWFNElementStelle) stelleVorTransition).hatMarke())
				istAktiviert = false;
		if (istAktiviert) {
			boolean hatKontakt = false;
			for (IWFNElementOK stelleNachTransition : transition.getKantenZu())
				if ((((IWFNElementStelle) stelleNachTransition).hatMarke())
						&& ((stelleNachTransition.getKantenZu()).contains(transition))==false)
					hatKontakt = true;
			if (hatKontakt) 
				kontaktTransitionen.add((IWFNElementTransition) transition);
			else
				aktivierteTransitionen.add((IWFNElementTransition) transition);
		}
	}

	/**
	 * Entfernt die Marken der Eingangsstellen einer Transition und überprüft die Auswirkungen
	 * für benachbarte Elemente.
	 * @param element Transition, deren Eingansstellen unmarkiert werden sollen
	 */
	private void entferneMarkeDerEingangsstellen(IWFNElement element) {
		for (IWFNElementOK stelleDavor : ((IWFNElementOK) element).getKantenVon()) {
			((IWFNElementStelle) stelleDavor).setMarke(false);
			markierungsListe.remove(stelleDavor);
			for (IWFNElementOK transitionDanach : ((IWFNElementOK) stelleDavor).getKantenZu()) {
				if (aktivierteTransitionen.contains(transitionDanach))
					aktivierteTransitionen.remove(transitionDanach);
				else
					if (kontaktTransitionen.contains(transitionDanach))
						kontaktTransitionen.remove(transitionDanach);
			}
			for (IWFNElementOK transitionDavor : ((IWFNElementOK) stelleDavor).getKantenVon())
				if (kontaktTransitionen.contains(transitionDavor)) {
					boolean kontakt = false;
					for (IWFNElementOK stelleNachTransitionDavor : transitionDavor.getKantenZu())
						if (((IWFNElementStelle)stelleNachTransitionDavor).hatMarke())
							kontakt = true;
					if (!kontakt) {
						aktivierteTransitionen.add((IWFNElementTransition) transitionDavor);
						kontaktTransitionen.remove(transitionDavor);
					}
				}
		}
	}
	
	@Override
	public void modellAenderungEingetreten(WFNStatusInfo statusInfo) {
		this.statusInfo = statusInfo;
		if (statusInfo.istWFN()) {
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
		for (IWFNElementOK elem : statusInfo.getAlleElementeOK()) 
			if (elem.getTyp() == EWFNElement.STELLE) 
				((IWFNElementStelle)elem).setMarke(false);
		statusInfo.getStartStelle().setMarke(true);
		markierungsListe.add(statusInfo.getStartStelle());
		for (IWFNElementOK folgeTransition : statusInfo.getStartStelle().getKantenZu())
			transitionHatNeueMarkierteEingangsstelle(folgeTransition);
		while (statusInfo.getGruendeFuerKeinWFN().contains(ende_erreicht))
			statusInfo.removeGruendeFuerKeinWFN(ende_erreicht);
		while (statusInfo.getGruendeFuerKeinWFN().contains(deadlock))
			statusInfo.removeGruendeFuerKeinWFN(deadlock);
	}
	
	/**
	 * Aktualisiert {@link #statusInfo} mit den aktuellen Listen:
	 * {@link #markierungsListe}
	 * {@link #aktivierteTransitionen}
	 * {@link #kontaktTransitionen}
	 */
	private void statusInfoAktualisieren() {
		statusInfo.setMarkierungsListe(markierungsListe);
		if ((aktivierteTransitionen.size() == 0)
				&& (markierungsListe.size() > 0)) {
			if ((markierungsListe.size() == 1)
					&& (markierungsListe.contains(statusInfo.getEndStelle()))
					&& (statusInfo.getStartStelle() != statusInfo.getEndStelle())
					&& (!statusInfo.getGruendeFuerKeinWFN().contains(ende_erreicht)))
				statusInfo.addGruendeFuerKeinWFN(ende_erreicht);
			else
				if ((!statusInfo.getGruendeFuerKeinWFN().contains(deadlock))
						&& (statusInfo.getStartStelle() != statusInfo.getEndStelle()))
					statusInfo.addGruendeFuerKeinWFN(deadlock);
		}
		statusInfo.setAktivierteTransitionen(aktivierteTransitionen);
		statusInfo.setKontaktTransitionen(kontaktTransitionen);
	}

	@Override
	public void allesZurueckAufStart() {
		markenAlleZurueckAufStart();
		statusInfoAktualisieren();
		fireModellStatusAenderung();
	}

	@Override
	public void schalteTransition(IWFNElementTransition transition) {
		schalteWennElementTransition(transition);
	}
}
