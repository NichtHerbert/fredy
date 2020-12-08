package wfnmodell;

import java.util.ArrayList;
import java.util.Iterator;

import wfnmodell.elemente.WFNElementKante;
import wfnmodell.schnittstellen.IWFNElementKante;
import wfnmodell.schnittstellen.IWFNElementOK;

/**
 * Klasse speziell zur Verwaltung der Kanten und ihrer Abhängigkeiten.
 *
 */
class ArcManagement {
	/**
	 * Die aktuelle {@link Identifier}.
	 */
	private Identifier identifier;
	
	/**
	 * Die aktuelle {@link StartEndStellenVerwaltung}.
	 */
	private StartEndStellenVerwaltung startEndManagement;
	
	/**
	 * Die aktuelle {@link ZusammenhangsVerwaltung}.
	 */
	private ZusammenhangsVerwaltung connectionManagement;
	
	/**
	 * Liste aller Kanten des aktuellen Datenmodells.
	 */
	private ArrayList<WFNElementKante> arcs;

	ArcManagement(Identifier idManagement, StartEndStellenVerwaltung startEndManagement,
			ZusammenhangsVerwaltung connectionManagement) {
		this.identifier = idManagement;
		this.startEndManagement = startEndManagement;
		this.connectionManagement = connectionManagement;
		arcs = new ArrayList<WFNElementKante>();
	}

	/**
	 * Instanziert ein neues Objekt vom Typ {@link wfnmodell.elemente.WFNElementKante} mit einer 
	 * neuen ID und den übergebenen Parametern, fügt es der {@link #arcs} hinzu, informiert alle 
	 * anderen Verwaltungen des Datenmodells und informiert natürlich auch die Stelle und die Transition,
	 * zwischen denen sie verläuft.
	 * @param pnmlID die ID aus der pnml-Datei oder ""
	 * @param origin das Element von dem die Kante ausgeht
	 * @param ending das Element in dem die Kante endet
	 */
	void createArc(String pnmlID, IWFNElementOK origin, IWFNElementOK ending) {
		if ((origin.getTyp() != ending.getTyp()) 
				&& ( !existsArcAlready(origin, ending))) {
			identifier.pnmlIDMonitoring(pnmlID);
			int id = identifier.get();
			WFNElementKante createdArc = new WFNElementKante(pnmlID,id,origin,ending);
			arcs.add(createdArc);
			origin.addKanteZu(ending);
			ending.addKanteVon(origin);
			startEndManagement.infoNeueKante(origin,ending);
			connectionManagement.infoNeueKante(origin,ending);
		}
	}
	
	/**
	 * Zur Überprüfung ob sich zwischen zwei Elementen schon eine gerichtete Kante mit gleicher 
	 * Richtung befindet.
	 * @param origin Element, welches als Kanten-Ausgangspunkt überprüft wird
	 * @param ending Element, welches als Kanten-Endpunkt überprüft wird
	 * @return true, wenn da schon eine Kante ist, sonst false
	 */
	boolean existsArcAlready(IWFNElementOK origin, IWFNElementOK ending) {
		boolean result = false;
		ArrayList<IWFNElementOK> allArcsStartingInOrigin = origin.getKantenZu();
		if (allArcsStartingInOrigin != null) {
			ArrayList<IWFNElementOK> allArcsEndingInEnding = ending.getKantenVon();
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
	ArrayList<IWFNElementKante> getAllArcs() {
		return new ArrayList<IWFNElementKante>(arcs);
	}

	@Override
	public String toString() {
		return "KantenVerwaltung [kantenListe=" + arcs + "]";
	}

	/**
	 * Ermittelt die fehlenden Parameter um dann {@link #deleteArc(IWFNElementKante, IWFNElementOK, IWFNElementOK)} aufzurufen.
	 * @param arc Referenz auf das zu löschende Kantenobjekt
	 */
	void deleteArc(IWFNElementKante arc) {
		deleteArc(arc, arc.getVon(), arc.getZu());
	}
	
	/**
	 * Ermittelt die fehlenden Parameter um dann {@link #deleteArc(IWFNElementKante, IWFNElementOK, IWFNElementOK)} aufzurufen.
	 * @param origin Element, von dem die zu löschende Kante ausgeht
	 * @param ending Element, in welchem die zu löschende Kante endet
	 */
	void deleteArc(IWFNElementOK origin, IWFNElementOK ending) {
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
	void deleteArc(IWFNElementKante arc, IWFNElementOK origin, IWFNElementOK ending) {
		if (arcs.contains(arc)) {
			arcs.remove(arc);
			origin.removeKanteZu(ending);
			ending.removeKanteVon(origin);
			identifier.passBack(arc.getID());
			connectionManagement.infoGeloeschteKante(origin, ending);
			startEndManagement.infoGeloeschteKante(origin, ending);
		}
	}
	
	/**
	 * Gibt die Referenz auf ein Kante zurück, die zwischen den übergebenen Elementen verläuft.
	 * @param from Element von dem die gesuchte Kante ausgeht
	 * @param to Element in dem die gesuchte Kante endet
	 * @return die Kante zwischen den beiden übergebenen Elementen oder null
	 */
	private IWFNElementKante getArc(IWFNElementOK from, IWFNElementOK to) {
		Iterator<WFNElementKante> it = arcs.iterator();
		IWFNElementKante result;
		while (it.hasNext()) {
			result = it.next();
			if ((result.getVon() == from)
				&& (result.getZu() == to))  
					return result;
		}
		return null;
	}

	
	
}
