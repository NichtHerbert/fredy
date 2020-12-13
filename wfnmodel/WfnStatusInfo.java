package wfnmodel;

import java.util.ArrayList;

import wfnmodel.elements.EWfnElement;
import wfnmodel.interfaces.IWfnArc;
import wfnmodel.interfaces.IWfnPlace;
import wfnmodel.interfaces.IWfnTransition;
import wfnmodel.interfaces.IWfnTransitionAndPlace;

/**
 * Klasse, die alle wesentlichen Informationen über den aktuellen Zustand des Workflownetzes in sich trägt 
 * bzw. tragen kann, und daher prädestiniert ist zur Informations-Weitergabe.
 */
public class WfnStatusInfo {
	
	/**
	 * true, wenn der gegenwärtige Zustand des Netzes die Bedingungen für ein WFN erfüllt.
	 */
	private boolean isWfn;
	/**
	 * true, wenn es nur eine Start- und eine Endstelle gibt.
	 */
	private boolean	hasOneStartOneEnd;
	/**
	 * Die Startstelle, wenn es nur eine gibt, sonst null.
	 */
	private IWfnPlace start;
	/**
	 * Die Endstelle, wenn es nur eine gibt, sonst null.
	 */
	private IWfnPlace end; 
	/**
	 * Liste der Gründe, warum das Netz die Bedingungen für ein WFN nicht erfüllt,
	 * oder dass es sich hierbei um ein WFN handelt.
	 */
	private ArrayList<String> notWfnExplanatoryStatements;
	/**
	 * Liste aller Transitionen und Stellen des aktuellen Workflownetzes.
	 */
	private ArrayList<IWfnTransitionAndPlace> transitionsAndPlaces;
	/**
	 * Liste aller Kanten des aktuellen Workflownetzes;
	 */
	private ArrayList<IWfnArc> arcs;
	/**
	 * Liste aller Stellen, die eine Marke haben.
	 */
	private ArrayList<IWfnPlace> markings;
    /**
     * Liste aller aktivierten Transitionen.
     */
    private ArrayList<IWfnTransition> enabledTransitions;
    /**
     * Liste aller Transitionen, bei denen Kontakt besteht.
     */
    private ArrayList<IWfnTransition> contactTransitions;
	
	public WfnStatusInfo() {
//		isWfn = false;
//		hasOneStartOneEnd = false;
//		start = null;
//		end = null;
		notWfnExplanatoryStatements = new ArrayList<>(1);
		transitionsAndPlaces = new ArrayList<>(1);
		arcs = new ArrayList<>(1);
		markings = new ArrayList<>();
		enabledTransitions = new ArrayList<>();
		contactTransitions = new ArrayList<>();
		
	}
	

	public WfnStatusInfo(boolean isWfn, 
						boolean hasOneStartOneEnd, 
						IWfnPlace start, 
						IWfnPlace end, 
						ArrayList<String> notWfnExplanatoryStatements,
						ArrayList<IWfnTransitionAndPlace> transitionsAndPlaces, 
						ArrayList<IWfnArc> arcs) {
		this.isWfn = isWfn;
		this.hasOneStartOneEnd = hasOneStartOneEnd;
		this.start = start;
		this.end = end;
		this.notWfnExplanatoryStatements = notWfnExplanatoryStatements;
		this.transitionsAndPlaces = transitionsAndPlaces;
		this.arcs = arcs;
	}
	
	/**
	 * Gibt zurück, ob es sich um ein WFN handelt.
	 * @return true, falls Netz Bedingungen für Workflownetz erfüllt
	 */
	public boolean isWfn() {
		return isWfn;
	}
	
	/**
	 * Gibt zurück, ob das WFN nur eine Start- und eine Endstelle hat.
	 * @return true, falls das WFN nur eine Start- und eine Endstelle hat
	 */
	public boolean hasOneStartOneEnd() {
		return hasOneStartOneEnd;
	}
	
	/**
	 * Gibt die Startstelle des WFN zurück.
	 * @return Startstelle, oder null, falls es keine eindeutige gibt
	 */
	public IWfnPlace getStartPlace() {
		return start;
	}

	/**
	 * Gibt die Endstelle des WFN zurück.
	 * @return Endstelle, oder null, falls es keine eindeutige gibt
	 */
	public IWfnPlace getEndPlace() {
		return end;
	}
	
	/**
	 * Gibt die Begründung, warum es sich um kein WFN handelt als Liste zurück.
	 * @return Liste der Gründe {@link #notWfnExplanatoryStatements}
	 */
	public ArrayList<String> getNotWfnExplanatoryStatements(){
		return new ArrayList<>(notWfnExplanatoryStatements);
	}
	
	/**
	 * Fügt eine Begründung hinzu, warum es sich um kein WFN handelt.
	 * @param statement zu {@link #notWfnExplanatoryStatements} hinzuzufügender Grund
	 */
	public void addNotWfnExplanatoryStatements(String statement){
		notWfnExplanatoryStatements.add(statement);
	}
	
	/**
	 * Entfernt eine Begründung, warum es sich um kein WFN handelt.
	 * @param statement von {@link #notWfnExplanatoryStatements} zu entfernender Grund
	 */
	public void removeNotWfnExplanatoryStatements(String statement){
		notWfnExplanatoryStatements.remove(statement);
	}

	/**
	 * Gibt eine Liste mit Referenzen auf alle Stellen und Transition des aktuellen WFN zurück.
	 * @return Liste aller Stellen und Transitionen des WFN {@link #transitionsAndPlaces}
	 */
	public ArrayList<IWfnTransitionAndPlace> getTransitionsAndPlaces() {
		return new ArrayList<>(transitionsAndPlaces);
	}
	
	/**
	 * Gibt eine Liste mit allen Kanten des WFN zurück.
	 * @return Liste aller Kanten des WFN
	 */
	public ArrayList<IWfnArc> getArcs() {
		return  new ArrayList<>(arcs);
	}
	
	/**
	 * Gibt eine Liste aller Stellen mit einer Marke zurück.
	 * @return Liste aller Stellen mit Marke {@link #markings}
	 */
	public ArrayList<IWfnPlace> getMarkings() {
		return markings;
	}
	
	/**
	 * Setzt die Liste aller Stellen mit Marke.
	 * @param markings zu setzende Liste für {@link #markings}
	 */
	public void setMarkings(ArrayList<IWfnPlace> markings) {
		this.markings = new ArrayList<>(markings);
	}

	/**
	 * Gibt eine Liste aller aktivierten Transitionen zurück.
	 * @return Liste aller aktivierten Transitionen {@link #enabledTransitions}
	 */
	public ArrayList<IWfnTransition> getEnabledTransitions() {
		return enabledTransitions;
	}

	/**
	 * Setzt die Liste aller aktivierten Transitionen.
	 * @param enabledTransitions zu setzende Liste für {@link #enabledTransitions}
	 */
	public void setEnabledTransitions(ArrayList<IWfnTransition> enabledTransitions) {
		this.enabledTransitions = new ArrayList<>(enabledTransitions);
	}
	
	/**
	 * Gibt die Liste aller Transitionen mit Kontakt zurück.
	 * @return Liste aller Transitionen mit Kontakt {@link #contactTransitions}
	 */
	public ArrayList<IWfnTransition> getContactTransitions() {
		return contactTransitions;
	}

	/**
	 * Setzt die Liste aller Transitionen mit Kontakt.
	 * @param contactTransitions zu setzenede Liste für {@link #contactTransitions}
	 */
	public void setContactTransitions(ArrayList<IWfnTransition> contactTransitions) {
		this.contactTransitions = new ArrayList<>(contactTransitions);
	}

	// is tostring in use?? because it should be if else, or am i wrong?
	@Override
	public String toString() {
		return "Oha, here is someone using WFNStatusInfo.toString()";
//		String ergebnis = "";
//		if (istWFN) 
//			ergebnis = "Das ist ein Workflow - Netz.\n";
//		try {
//			for (String grund : gruendeFuerKeinWFN)
//				ergebnis += grund + "\n";
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return ergebnis;
	}
	
	/**
	 * Statische Methode, die aus den übergebenen Parametern alle nötigen Informationen über den
	 * Zustand des WFN zieht, und diese Informationen einem neu instanzierten Objekt dieser Klasse übergibt.
	 * Anmerkung: mit Ausnahme derjenigen Informationen, die nur die {@link verwaltung.MarkingManagement} stellen
	 * kann (also: Stellen mit Marken, aktivierte Transitionen, Transitionen mit Kontakt).
	 * @param transitionsAndPlaces Liste aller Stellen und Transitionen des aktuellen Datenmodells.
	 * @param arcs Liste aller Kanten des aktuellen Datenmodells 
	 * @param startEndControll Referenz auf die aktuelle {@link StartEndManagement} des Datenmodells
	 * @return ein neu instanziertes und mit allen notwendigen Informationen versehenes Objekt dieser Klasse
	 */
	public static WfnStatusInfo getInfo(ArrayList<IWfnTransitionAndPlace> transitionsAndPlaces, 
										ArrayList<IWfnArc> arcs, 
										StartEndManagement startEndControll) {
		IWfnPlace startPlace = null, endPlace = null;
		ArrayList<String> statements = new ArrayList<>(5);
		boolean isWfn = false;
		if (transitionsAndPlaces.size() == 0) {
			statements.add("");
			statements.add("-- no elements --");
		} else {
			boolean isNotConnected = false;
			boolean isNotConnectedAltTest = false;
			boolean hasOnlyTransitions = true;
			boolean isNotWfn = false;
			for (IWfnTransitionAndPlace element : transitionsAndPlaces) {
				boolean hasStartPath = element.hasStartPath();
				boolean hasEndPath = element.hasEndPath(); 
				if ((hasStartPath)
						^ (hasEndPath))
					isNotWfn = true;
				else if ((!hasStartPath)
							|| (!hasEndPath))
						isNotConnected = true;
				if ((!element.hasOutgoingArcs())
						&& (!element.hasIncomingArcs()))
					isNotConnectedAltTest = true;
				if ((hasOnlyTransitions)
						&& (element.getWfnElementType() != EWfnElement.TRANSITION))
					hasOnlyTransitions = false;	
			}
			if ((isNotConnected) 
					|| ( !startEndControll.hasUniqueStartAndEnd())
					|| (isNotWfn)) {
				statements.add("Kein Workflow-Netz!");
				if (!hasOnlyTransitions) {
					if (isNotConnectedAltTest) {
						statements.add("Nicht alle Elemente verbunden.");
					} else {
						if ((!startEndControll.hasUniqueStart())
								|| (!startEndControll.hasUniqueEnd())) {
							if (!startEndControll.hasUniqueStart()) {
								if (startEndControll.getStartPlacesNumber() == 0) 
									statements.add("keine Anfangsstelle");
								else
									statements.add("zu viele Anfangsstellen");
							}
							if (!startEndControll.hasUniqueEnd()) {
								if (startEndControll.getEndPlacesNumber() == 0) 
									statements.add("keine Endstelle");
								else
									statements.add("zu viele Endstellen");
							}
						} else {
							statements.add("Nicht alle Elemente auf Pfad");
							statements.add("von Anfangs- zu Endstelle.");
						}
					}
				}
			} else {
				isWfn = true;
				statements.add("Dies ist ein Workflow-Netz.");
				startPlace = startEndControll.getUniqueStart();
				endPlace = startEndControll.getUniqueEnd();
			}
		}
		return new WfnStatusInfo(isWfn,
								startEndControll.hasUniqueStartAndEnd(),
								startPlace,
								endPlace,
								statements,
								new ArrayList<>(transitionsAndPlaces),
								arcs);
	}


	
}
