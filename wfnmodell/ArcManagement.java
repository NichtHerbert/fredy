package wfnmodell;

import java.util.ArrayList;
import java.util.Iterator;

import wfnmodell.elements.WfnElementArc;
import wfnmodell.interfaces.IWfnArc;
import wfnmodell.interfaces.IWfnTransitionAndPlace;

/**
 * Klasse speziell zur Verwaltung der Kanten und ihrer Abhängigkeiten.
 *
 */
class ArcManagement {
	/**
	 * Die aktuelle {@link IDManagement}.
	 */
	private IDManagement identifier;
	
	/**
	 * Die aktuelle {@link StartEndManagement}.
	 */
	private StartEndManagement startEndManagement;
	
	/**
	 * Die aktuelle {@link ConnectionManagement}.
	 */
	private ConnectionManagement connectionManagement;
	
	/**
	 * Liste aller Kanten des aktuellen Datenmodells.
	 */
	private ArrayList<WfnElementArc> arcs;

	ArcManagement(IDManagement idManagement, StartEndManagement startEndManagement,
			ConnectionManagement connectionManagement) {
		this.identifier = idManagement;
		this.startEndManagement = startEndManagement;
		this.connectionManagement = connectionManagement;
		arcs = new ArrayList<WfnElementArc>();
	}

	/**
	 * Instanziert ein neues Objekt vom Typ {@link wfnmodell.elements.WfnElementArc} mit einer 
	 * neuen ID und den übergebenen Parametern, fügt es der {@link #arcs} hinzu, informiert alle 
	 * anderen Verwaltungen des Datenmodells und informiert natürlich auch die Stelle und die Transition,
	 * zwischen denen sie verläuft.
	 * @param pnmlID die ID aus der pnml-Datei oder ""
	 * @param origin das Element von dem die Kante ausgeht
	 * @param ending das Element in dem die Kante endet
	 */
	void createArc(String pnmlID, IWfnTransitionAndPlace origin, IWfnTransitionAndPlace ending) {
		if ((origin.getWfnElementType() != ending.getWfnElementType()) 
				&& ( !existsArcAlready(origin, ending))) {
			identifier.pnmlIDMonitoring(pnmlID);
			int id = identifier.get();
			WfnElementArc createdArc = new WfnElementArc(pnmlID,id,origin,ending);
			arcs.add(createdArc);
			origin.addOutputElements(ending);
			ending.addInputElement(origin);
			startEndManagement.infoCreatedArc(origin,ending);
			connectionManagement.infoCreatedArc(origin,ending);
		}
	}
	
	/**
	 * Zur Überprüfung ob sich zwischen zwei Elementen schon eine gerichtete Kante mit gleicher 
	 * Richtung befindet.
	 * @param origin Element, welches als Kanten-Ausgangspunkt überprüft wird
	 * @param ending Element, welches als Kanten-Endpunkt überprüft wird
	 * @return true, wenn da schon eine Kante ist, sonst false
	 */
	boolean existsArcAlready(IWfnTransitionAndPlace origin, IWfnTransitionAndPlace ending) {
		boolean result = false;
		ArrayList<IWfnTransitionAndPlace> allArcsStartingInOrigin = origin.getOutputElements();
		if (allArcsStartingInOrigin != null) {
			ArrayList<IWfnTransitionAndPlace> allArcsEndingInEnding = ending.getInputElements();
			if (allArcsEndingInEnding != null) {
				int i = 0;
				while ((i< allArcsStartingInOrigin.size()) && (result == false)) {
					if (allArcsEndingInEnding.contains(allArcsStartingInOrigin.get(i))) result = true;
					i++;
				}
			}
		}
		return result;
	}

	/**
	 * Gibt eine Liste aller Kanten zurück.
	 * @return {@link #arcs}
	 */
	ArrayList<IWfnArc> getAllArcs() {
		return new ArrayList<IWfnArc>(arcs);
	}

	@Override
	public String toString() {
		return "KantenVerwaltung [kantenListe=" + arcs + "]";
	}

	/**
	 * Ermittelt die fehlenden Parameter um dann {@link #deleteArc(IWfnArc, IWfnTransitionAndPlace, IWfnTransitionAndPlace)} aufzurufen.
	 * @param arc Referenz auf das zu löschende Kantenobjekt
	 */
	void deleteArc(IWfnArc arc) {
		deleteArc(arc, arc.getSource(), arc.getTarget());
	}
	
	/**
	 * Ermittelt die fehlenden Parameter um dann {@link #deleteArc(IWfnArc, IWfnTransitionAndPlace, IWfnTransitionAndPlace)} aufzurufen.
	 * @param origin Element, von dem die zu löschende Kante ausgeht
	 * @param ending Element, in welchem die zu löschende Kante endet
	 */
	void deleteArc(IWfnTransitionAndPlace origin, IWfnTransitionAndPlace ending) {
		deleteArc(getArc(origin,ending), origin, ending);
	}
	
	/**
	 * Methode zum Löschen einer Kante: Löscht die Kante von der Liste {@link #arcs}, informiert alle 
	 * anderen Verwaltungen des Datenmodells und informiert natürlich auch die Stelle und die Transition,
	 * zwischen denen sie verlief.
	 * @param arc die zu löschende Kante
	 * @param origin Element, von dem die zu löschende Kante ausgeht
	 * @param ending Element, in welchem die zu löschende Kante endet
	 */
	void deleteArc(IWfnArc arc, IWfnTransitionAndPlace origin, IWfnTransitionAndPlace ending) {
		if (arcs.contains(arc)) {
			arcs.remove(arc);
			origin.removeOutputElements(ending);
			ending.removeInputElement(origin);
			identifier.passBack(arc.getID());
			connectionManagement.infoDeletedArc(origin, ending);
			startEndManagement.infoDeletedArc(origin, ending);
		}
	}
	
	/**
	 * Gibt die Referenz auf ein Kante zurück, die zwischen den übergebenen Elementen verläuft.
	 * @param from Element von dem die gesuchte Kante ausgeht
	 * @param to Element in dem die gesuchte Kante endet
	 * @return die Kante zwischen den beiden übergebenen Elementen oder null
	 */
	private IWfnArc getArc(IWfnTransitionAndPlace from, IWfnTransitionAndPlace to) {
		Iterator<WfnElementArc> it = arcs.iterator();
		IWfnArc result;
		while (it.hasNext()) {
			result = it.next();
			if ((result.getSource() == from)
				&& (result.getTarget() == to))  
					return result;
		}
		return null;
	}

	
	
}
