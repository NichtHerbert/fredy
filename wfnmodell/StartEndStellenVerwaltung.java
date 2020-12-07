package wfnmodell;

import java.util.ArrayList;

import wfnmodell.elemente.EWFNElement;
import wfnmodell.elemente.WFNElementStelle;
import wfnmodell.schnittstellen.IWFNElementStelle;
import wfnmodell.schnittstellen.IWFNElementOK;

/**
 * Klasse zur Verwaltung der möglichen Start- und der möglichen Endstellen eines Workflownetzes.
 *
 */
class StartEndStellenVerwaltung {
	/**
	 * Liste der möglichen Startstellen.
	 */
	private ArrayList<IWFNElementStelle> startStellen;
	/**
	 * Liste der möglichen Endstellen.
	 */
	private ArrayList<IWFNElementStelle> endStellen;
	/**
	 * Die aktuelle {@link ZusammenhangsVerwaltung}.
	 */
	private ZusammenhangsVerwaltung zusammenhangsVerwaltung;
	
	StartEndStellenVerwaltung(ZusammenhangsVerwaltung zusammenhangsVerwaltung) {
		startStellen = new ArrayList<>();
		endStellen = new ArrayList<>();
		this.zusammenhangsVerwaltung = zusammenhangsVerwaltung;
	}

	/**
	 * Überprüft, ob durch die neue Kante Stellen aus den Listen {@link #startStellen} und 
	 * {@link #endStellen} gelöscht werden müssen. Wenn dies der Fall ist, wird auch die 
	 * {@link #zusammenhangsVerwaltung} informiert.
	 * @param von Element, von dem die neue Kante ausgeht
	 * @param zu Element, in dem die neue Kante endet
	 */
	void infoNeueKante(IWFNElementOK von, IWFNElementOK zu) {
		if (von.getTyp() == EWFNElement.STELLE) { 
			if (endStellen.contains(von)) {
				endStellen.remove(von);
				zusammenhangsVerwaltung.setHatEinenPfadZumEndeVerloren(von);
			}
		} else {
			if (startStellen.contains(zu)) {
				startStellen.remove(zu);
				zusammenhangsVerwaltung.setHatEinenPfadVomStartVerloren(zu);
			}
		}
	}
	
	/**
	 * Überprüft, ob durch die gelöschte Kante eine Stelle der Liste {@link #startStellen} oder der
	 * Liste {@link #endStellen} hinzugefügt werden muss. Wenn dies der Fall ist, wird auch die 
	 * {@link #zusammenhangsVerwaltung} informiert.
	 * @param von Element, von dem die gelöschte Kante ausging
	 * @param zu Element, in dem die gelöschte Kante endete
	 */
	void infoGeloeschteKante(IWFNElementOK von, IWFNElementOK zu) {
		if (von.getTyp() == EWFNElement.STELLE) {
			if (!von.hatAusgehendeKanten()) {
				endStellen.add((WFNElementStelle) von);
				zusammenhangsVerwaltung.setHatJetztPfadZumEnde(von);
			}
		} else {
			if (!zu.hatEingehendeKanten()) {
				startStellen.add((WFNElementStelle) zu);
				zusammenhangsVerwaltung.setHatJetztPfadVomStart(zu);
			}
		}
	}
	
	@Override
	public String toString() {
		return "StartEndStellenVerwaltung: Anzahl Startst. = " + startStellen.size() + " ,"
				+ "Anzahl Endst. = " + endStellen.size() +"\n"
				+ " [startStellen=" + startStellen +"\n"
				+ " endStellen=" + endStellen + "]";
	}

	/**
	 * @return true, wenn es nur eine Start- und eine Endstelle gibt, sonst false
	 */
	boolean hatNurEineStartUndEineEndStelle() {
		return ((startStellen.size()==1) && (endStellen.size()==1)) ? true : false;	
	}

	/**
	 * Fügt die übergebene Stelle, von der angenommen wird, dass sie neu ist und deswegen noch keine Kanten
	 * anliegen hat, den Listen {@link #startStellen} und {@link #endStellen} hinzu und informiert
	 * die {@link #zusammenhangsVerwaltung}.
	 * @param stelle neues WFN-Element Stelle
	 */
	void add(WFNElementStelle stelle) {
		startStellen.add(stelle);
		endStellen.add(stelle);
		zusammenhangsVerwaltung.setHatJetztPfadVomStart(stelle);
		zusammenhangsVerwaltung.setHatJetztPfadZumEnde(stelle);
	}

	/**
	 * Entfernt die übergebene WFN-Stelle aus den Listen {@link #endStellen} und {@link #startStellen}.
	 * @param stelle zu entfernende WFN-Stelle
	 */
	void remove(WFNElementStelle stelle) {
		if (startStellen.contains(stelle))
			startStellen.remove(stelle);
		if (endStellen.contains(stelle))
			endStellen.remove(stelle);
	}
	
	/**
	 * Gibt die Anzahl der möglichen Startstellen zurück.
	 * @return die Länge der Liste {@link #startStellen}
	 */
	int getStartStellenAnzahl() {
		return startStellen.size();
	}

	/**
	 * Gibt die Anzahl der möglichen Endstellen zurück.
	 * @return die Länge der Liste {@link #endStellen}
	 */
	int getEndStellenAnzahl() {
		return endStellen.size();
	}
	
	/**
	 * Gibt zurück, ob es nur eine mögliche Startstelle gibt.
	 * @return true, wenn es genau eine mögliche Startstelle gibt, sonst false
	 */
	boolean hatNurEineStartStelle() {
		return (startStellen.size() == 1) ? true : false;
	}

	/**
	 * Gibt zurück, ob es nur eine mögliche Endstelle gibt.
	 * @return true, wenn es genau eine mögliche Endstelle gibt, sonst false
	 */
	boolean hatNurEineEndStelle() {
		return (endStellen.size() == 1) ? true : false;
	}
	
	/**
	 * Gibt es eine eindeutige Startstelle, wird eine Referenz auf sie zurückgegeben.
	 * @return die Startstelle, oder null, wenn es keine eindeutige gibt
	 */
	IWFNElementStelle getStartStelle() {
		if (hatNurEineStartStelle())
			return startStellen.get(0);
		else
			return null;
	}

	/**
	 * Gibt es eine eindeutige Endstelle, wird eine Referenz auf sie zurückgegeben.
	 * @return die Endstelle, oder null, wenn es keine eindeutige gibt
	 */
	IWFNElementStelle getEndStelle() {
		if (hatNurEineEndStelle())
			return endStellen.get(0);
		else
			return null;
	}

	/**
	 * Gibt eine Liste der möglichen Startstellen zurück.
	 * @return {@link #startStellen}
	 */
	ArrayList<IWFNElementStelle> getStartStellen() {
		return startStellen;
	}

	/**
	 * Gibt eine Liste der möglichen Endstellen zurück.
	 * @return {@link #endStellen}
	 */
	ArrayList<IWFNElementStelle> getEndStellen() {
		return endStellen;
	}
	
	
}
