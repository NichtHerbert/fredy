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
class KantenVerwaltung {
	/**
	 * Die aktuelle {@link Identifier}.
	 */
	private Identifier idVerwaltung;
	
	/**
	 * Die aktuelle {@link StartEndStellenVerwaltung}.
	 */
	private StartEndStellenVerwaltung startEndStellenVerwaltung;
	
	/**
	 * Die aktuelle {@link ZusammenhangsVerwaltung}.
	 */
	private ZusammenhangsVerwaltung zusammenhangsVerwaltung;
	
	/**
	 * Liste aller Kanten des aktuellen Datenmodells.
	 */
	private ArrayList<WFNElementKante> kantenListe;

	KantenVerwaltung(Identifier idVerwaltung, StartEndStellenVerwaltung startEndStellenVerwaltung,
			ZusammenhangsVerwaltung zusammenhangsVerwaltung) {
		this.idVerwaltung = idVerwaltung;
		this.startEndStellenVerwaltung = startEndStellenVerwaltung;
		this.zusammenhangsVerwaltung = zusammenhangsVerwaltung;
		kantenListe = new ArrayList<WFNElementKante>();
	}

	/**
	 * Instanziert ein neues Objekt vom Typ {@link wfnmodell.elemente.WFNElementKante} mit einer 
	 * neuen ID und den übergebenen Parametern, fügt es der {@link #kantenListe} hinzu, informiert alle 
	 * anderen Verwaltungen des Datenmodells und informiert natürlich auch die Stelle und die Transition,
	 * zwischen denen sie verläuft.
	 * @param pnmlID die ID aus der pnml-Datei oder ""
	 * @param von das Element von dem die Kante ausgeht
	 * @param zu das Element in dem die Kante endet
	 */
	void neueKante(String pnmlID, IWFNElementOK von, IWFNElementOK zu) {
		if ((von.getTyp() != zu.getTyp()) 
				&& ( !istDaSchonEineKante(von,zu))) {
			idVerwaltung.pnmlIDMonitoring(pnmlID);
			int id = idVerwaltung.get();
			WFNElementKante neueKante = new WFNElementKante(pnmlID,id,von,zu);
			kantenListe.add(neueKante);
			von.addKanteZu(zu);
			zu.addKanteVon(von);
			startEndStellenVerwaltung.infoNeueKante(von,zu);
			zusammenhangsVerwaltung.infoNeueKante(von,zu);
		}
	}
	
	/**
	 * Zur Überprüfung ob sich zwischen zwei Elementen schon eine gerichtete Kante mit gleicher 
	 * Richtung befindet.
	 * @param von Element, welches als Kanten-Ausgangspunkt überprüft wird
	 * @param zu Element, welches als Kanten-Endpunkt überprüft wird
	 * @return true, wenn da schon eine Kante ist, sonst false
	 */
	boolean istDaSchonEineKante(IWFNElementOK von, IWFNElementOK zu) {
		boolean ergebnis = false;
		ArrayList<IWFNElementOK> kantenVon = von.getKantenZu();
		if (kantenVon != null) {
			ArrayList<IWFNElementOK> kantenZu = zu.getKantenVon();
			if (kantenZu != null) {
				int i = 0;
				while ((i< kantenVon.size()) && (ergebnis == false)) {
					if (kantenZu.contains(kantenVon.get(i))) ergebnis = true;
					i++;
				}
			}
		}
		return ergebnis;
	}

	/**
	 * Gibt eine Liste aller Kanten zurück.
	 * @return {@link #kantenListe}
	 */
	ArrayList<IWFNElementKante> getAlleKanten() {
		return new ArrayList<IWFNElementKante>(kantenListe);
	}

	@Override
	public String toString() {
		return "KantenVerwaltung [kantenListe=" + kantenListe + "]";
	}

	/**
	 * Ermittelt die fehlenden Parameter um dann {@link #loescheKante(IWFNElementKante, IWFNElementOK, IWFNElementOK)} aufzurufen.
	 * @param kante Referenz auf das zu löschende Kantenobjekt
	 */
	void loescheKante(IWFNElementKante kante) {
		loescheKante(kante, kante.getVon(), kante.getZu());
	}
	
	/**
	 * Ermittelt die fehlenden Parameter um dann {@link #loescheKante(IWFNElementKante, IWFNElementOK, IWFNElementOK)} aufzurufen.
	 * @param von Element, von dem die zu löschende Kante ausgeht
	 * @param zu Element, in welchem die zu löschende Kante endet
	 */
	void loescheKante(IWFNElementOK von, IWFNElementOK zu) {
		loescheKante(getKante(von,zu),von,zu);
	}
	
	/**
	 * Methode zum Löschen einer Kante: Löscht die Kante von der Liste {@link #kantenListe}, informiert alle 
	 * anderen Verwaltungen des Datenmodells und informiert natürlich auch die Stelle und die Transition,
	 * zwischen denen sie verlief.
	 * @param kante die zu löschende Kante
	 * @param von Element, von dem die zu löschende Kante ausgeht
	 * @param zu Element, in welchem die zu löschende Kante endet
	 */
	void loescheKante(IWFNElementKante kante, IWFNElementOK von, IWFNElementOK zu) {
		if (kantenListe.contains(kante)) {
			kantenListe.remove(kante);
			von.removeKanteZu(zu);
			zu.removeKanteVon(von);
			idVerwaltung.passBack(kante.getID());
			zusammenhangsVerwaltung.infoGeloeschteKante(von, zu);
			startEndStellenVerwaltung.infoGeloeschteKante(von, zu);
		}
	}
	
	/**
	 * Gibt die Referenz auf ein Kante zurück, die zwischen den übergebenen Elementen verläuft.
	 * @param von Element von dem die gesuchte Kante ausgeht
	 * @param zu Element in dem die gesuchte Kante endet
	 * @return die Kante zwischen den beiden übergebenen Elementen oder null
	 */
	private IWFNElementKante getKante(IWFNElementOK von, IWFNElementOK zu) {
		Iterator<WFNElementKante> it = kantenListe.iterator();
		IWFNElementKante ergebnis;
		while (it.hasNext()) {
			ergebnis = it.next();
			if ((ergebnis.getVon() == von)
				&& (ergebnis.getZu() == zu))  
					return ergebnis;
		}
		return null;
	}

	
	
}
