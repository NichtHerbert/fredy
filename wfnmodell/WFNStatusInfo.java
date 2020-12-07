package wfnmodell;

import java.util.ArrayList;
import wfnmodell.schnittstellen.IWFNElementStelle;
import wfnmodell.schnittstellen.IWFNElementTransition;
import wfnmodell.elemente.EWFNElement;
import wfnmodell.schnittstellen.IWFNElementKante;
import wfnmodell.schnittstellen.IWFNElementOK;

/**
 * Klasse, die alle wesentlichen Informationen über den aktuellen Zustand des Workflownetzes in sich trägt 
 * bzw. tragen kann, und daher prädestiniert ist zur Informations-Weitergabe.
 */
public class WFNStatusInfo {
	
	/**
	 * true, wenn der gegenwärtige Zustand des Netzes die Bedingungen für ein WFN erfüllt.
	 */
	private boolean istWFN;
	/**
	 * true, wenn es nur eine Start- und eine Endstelle gibt.
	 */
	private boolean	hatNurEineStartUndEineEndStelle;
	/**
	 * Die Startstelle, wenn es nur eine gibt, sonst null.
	 */
	private IWFNElementStelle startStelle;
	/**
	 * Die Endstelle, wenn es nur eine gibt, sonst null.
	 */
	private IWFNElementStelle endStelle; 
	/**
	 * Liste der Gründe, warum das Netz die Bedingungen für ein WFN nicht erfüllt,
	 * oder dass es sich hierbei um ein WFN handelt.
	 */
	private ArrayList<String> gruendeFuerKeinWFN;
	/**
	 * Liste aller Transitionen und Stellen des aktuellen Workflownetzes.
	 */
	private ArrayList<IWFNElementOK> elementListeOK;
	/**
	 * Liste aller Kanten des aktuellen Workflownetzes;
	 */
	private ArrayList<IWFNElementKante> kantenListe;
	/**
	 * Liste aller Stellen, die eine Marke haben.
	 */
	private ArrayList<IWFNElementStelle> markierungsListe;
    /**
     * Liste aller aktivierten Transitionen.
     */
    private ArrayList<IWFNElementTransition> aktivierteTransitionen;
    /**
     * Liste aller Transitionen, bei denen Kontakt besteht.
     */
    private ArrayList<IWFNElementTransition> kontaktTransitionen;
	
	public WFNStatusInfo() {
		istWFN = false;
		hatNurEineStartUndEineEndStelle = false;
		gruendeFuerKeinWFN = new ArrayList<>(1);
		elementListeOK = new ArrayList<>(1);
		kantenListe = new ArrayList<>(1);
		markierungsListe = new ArrayList<>();
		aktivierteTransitionen = new ArrayList<>();
		kontaktTransitionen = new ArrayList<>();
		startStelle = null;
		endStelle = null;
	}
	

	public WFNStatusInfo(boolean istWFN, 
						boolean hatNurEineStartUndEineEndStelle, 
						IWFNElementStelle startStelle, 
						IWFNElementStelle endStelle, 
						ArrayList<String> gruende,
						ArrayList<IWFNElementOK> elementListeOK, 
						ArrayList<IWFNElementKante> kantenListe) {
		this.istWFN = istWFN;
		this.hatNurEineStartUndEineEndStelle = hatNurEineStartUndEineEndStelle;
		this.startStelle = startStelle;
		this.endStelle = endStelle;
		gruendeFuerKeinWFN = gruende;
		this.elementListeOK = elementListeOK;
		this.kantenListe = kantenListe;
	}
	
	/**
	 * Gibt zurück, ob es sich um ein WFN handelt.
	 * @return true, falls Netz Bedingungen für Workflownetz erfüllt
	 */
	public boolean istWFN() {
		return istWFN;
	}
	
	/**
	 * Gibt zurück, ob das WFN nur eine Start- und eine Endstelle hat.
	 * @return true, falls das WFN nur eine Start- und eine Endstelle hat
	 */
	public boolean hatNurEineStartUndEineEndStelle() {
		return hatNurEineStartUndEineEndStelle;
	}
	
	/**
	 * Gibt die Startstelle des WFN zurück.
	 * @return Startstelle, oder null, falls es keine eindeutige gibt
	 */
	public IWFNElementStelle getStartStelle() {
		return startStelle;
	}

	/**
	 * Gibt die Endstelle des WFN zurück.
	 * @return Endstelle, oder null, falls es keine eindeutige gibt
	 */
	public IWFNElementStelle getEndStelle() {
		return endStelle;
	}
	
	/**
	 * Gibt die Begründung, warum es sich um kein WFN handelt als Liste zurück.
	 * @return Liste der Gründe {@link #gruendeFuerKeinWFN}
	 */
	public ArrayList<String> getGruendeFuerKeinWFN(){
		return new ArrayList<>(gruendeFuerKeinWFN);
	}
	
	/**
	 * Fügt eine Begründung hinzu, warum es sich um kein WFN handelt.
	 * @param grund zu {@link #gruendeFuerKeinWFN} hinzuzufügender Grund
	 */
	public void addGruendeFuerKeinWFN(String grund){
		gruendeFuerKeinWFN.add(grund);
	}
	
	/**
	 * Entfernt eine Begründung, warum es sich um kein WFN handelt.
	 * @param grund von {@link #gruendeFuerKeinWFN} zu entfernender Grund
	 */
	public void removeGruendeFuerKeinWFN(String grund){
		gruendeFuerKeinWFN.remove(grund);
	}

	/**
	 * Gibt eine Liste mit Referenzen auf alle Stellen und Transition des aktuellen WFN zurück.
	 * @return Liste aller Stellen und Transitionen des WFN {@link #elementListeOK}
	 */
	public ArrayList<IWFNElementOK> getAlleElementeOK() {
		return new ArrayList<>(elementListeOK);
	}
	
	/**
	 * Gibt eine Liste mit allen Kanten des WFN zurück.
	 * @return Liste aller Kanten des WFN
	 */
	public ArrayList<IWFNElementKante> getAlleKanten() {
		return  new ArrayList<>(kantenListe);
	}
	
	/**
	 * Gibt eine Liste aller Stellen mit einer Marke zurück.
	 * @return Liste aller Stellen mit Marke {@link #markierungsListe}
	 */
	public ArrayList<IWFNElementStelle> getMarkierungsListe() {
		return markierungsListe;
	}
	
	/**
	 * Setzt die Liste aller Stellen mit Marke.
	 * @param markierungsListe zu setzende Liste für {@link #markierungsListe}
	 */
	public void setMarkierungsListe(ArrayList<IWFNElementStelle> markierungsListe) {
		this.markierungsListe = new ArrayList<>(markierungsListe);
	}

	/**
	 * Gibt eine Liste aller aktivierten Transitionen zurück.
	 * @return Liste aller aktivierten Transitionen {@link #aktivierteTransitionen}
	 */
	public ArrayList<IWFNElementTransition> getAktivierteTransitionen() {
		return aktivierteTransitionen;
	}

	/**
	 * Setzt die Liste aller aktivierten Transitionen.
	 * @param aktivierteTransitionen zu setzende Liste für {@link #aktivierteTransitionen}
	 */
	public void setAktivierteTransitionen(ArrayList<IWFNElementTransition> aktivierteTransitionen) {
		this.aktivierteTransitionen = new ArrayList<>(aktivierteTransitionen);
	}
	
	/**
	 * Gibt die Liste aller Transitionen mit Kontakt zurück.
	 * @return Liste aller Transitionen mit Kontakt {@link #kontaktTransitionen}
	 */
	public ArrayList<IWFNElementTransition> getKontaktTransitionen() {
		return kontaktTransitionen;
	}

	/**
	 * Setzt die Liste aller Transitionen mit Kontakt.
	 * @param kontaktTransitionen zu setzenede Liste für {@link #kontaktTransitionen}
	 */
	public void setKontaktTransitionen(ArrayList<IWFNElementTransition> kontaktTransitionen) {
		this.kontaktTransitionen = new ArrayList<>(kontaktTransitionen);
	}


	@Override
	public String toString() {
		String ergebnis = "";
		if (istWFN) 
			ergebnis = "Das ist ein Workflow - Netz.\n";
		try {
			for (String grund : gruendeFuerKeinWFN)
				ergebnis += grund + "\n";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ergebnis;
	}
	
	/**
	 * Statische Methode, die aus den übergebenen Parametern alle nötigen Informationen über den
	 * Zustand des WFN zieht, und diese Informationen einem neu instanzierten Objekt dieser Klasse übergibt.
	 * Anmerkung: mit Ausnahme derjenigen Informationen, die nur die {@link verwaltung.MarkierungsVerwaltung} stellen
	 * kann (also: Stellen mit Marken, aktivierte Transitionen, Transitionen mit Kontakt).
	 * @param elementListeOK Liste aller Stellen und Transitionen des aktuellen Datenmodells.
	 * @param kantenListe Liste aller Kanten des aktuellen Datenmodells 
	 * @param startEndStellenVerwaltung Referenz auf die aktuelle {@link StartEndStellenVerwaltung} des Datenmodells
	 * @return ein neu instanziertes und mit allen notwendigen Informationen versehenes Objekt dieser Klasse
	 */
	public static WFNStatusInfo getInfo(ArrayList<IWFNElementOK> elementListeOK, 
										ArrayList<IWFNElementKante> kantenListe, 
										StartEndStellenVerwaltung startEndStellenVerwaltung) {
		IWFNElementStelle startStelle = null, endStelle = null;
		ArrayList<String> gruende = new ArrayList<>(5);
		boolean istWFN = false;
		if (elementListeOK.size() == 0) {
			gruende.add("");
			gruende.add("-- keine Elemente --");
		} else {
			boolean istUnzusammenhaengend = false;
			boolean istUnzusammenhaengendAlternativTest = false;
			boolean hatNurTransitionen = true;
			boolean keinWFN = false;
			for (IWFNElementOK element : elementListeOK) {
				boolean hatPfadvomStart = element.istAufPfadVomStart();
				boolean hatPfadzumEnde = element.istAufPfadZumEnde(); 
				if ((hatPfadvomStart)
						^ (hatPfadzumEnde))
					keinWFN = true;
				else if ((!hatPfadvomStart)
							|| (!hatPfadzumEnde))
						istUnzusammenhaengend = true;
				if ((!element.hatAusgehendeKanten())
						&& (!element.hatEingehendeKanten()))
					istUnzusammenhaengendAlternativTest = true;
				if ((hatNurTransitionen)
						&& (element.getTyp() != EWFNElement.TRANSITION))
					hatNurTransitionen = false;	
			}
			if ((istUnzusammenhaengend) 
					|| ( !startEndStellenVerwaltung.hatNurEineStartUndEineEndStelle())
					|| (keinWFN)) {
				gruende.add("Kein Workflow-Netz!");
				if (!hatNurTransitionen) {
					if (istUnzusammenhaengendAlternativTest) {
						gruende.add("Nicht alle Elemente verbunden.");
					} else {
						if ((!startEndStellenVerwaltung.hatNurEineStartStelle())
								|| (!startEndStellenVerwaltung.hatNurEineEndStelle())) {
							if (!startEndStellenVerwaltung.hatNurEineStartStelle()) {
								if (startEndStellenVerwaltung.getStartStellenAnzahl() == 0) 
									gruende.add("keine Anfangsstelle");
								else
									gruende.add("zu viele Anfangsstellen");
							}
							if (!startEndStellenVerwaltung.hatNurEineEndStelle()) {
								if (startEndStellenVerwaltung.getEndStellenAnzahl() == 0) 
									gruende.add("keine Endstelle");
								else
									gruende.add("zu viele Endstellen");
							}
						} else {
							gruende.add("Nicht alle Elemente auf Pfad");
							gruende.add("von Anfangs- zu Endstelle.");
						}
					}
				}
			} else {
				istWFN = true;
				gruende.add("Dies ist ein Workflow-Netz.");
				startStelle = startEndStellenVerwaltung.getStartStelle();
				endStelle = startEndStellenVerwaltung.getEndStelle();
			}
		}
		return new WFNStatusInfo(istWFN,
								startEndStellenVerwaltung.hatNurEineStartUndEineEndStelle(),
								startStelle,
								endStelle,
								gruende,
								new ArrayList<>(elementListeOK),
								kantenListe);
	}


	
}
