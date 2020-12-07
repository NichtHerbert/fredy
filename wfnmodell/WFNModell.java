package wfnmodell;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import horcherschnittstellen.IWFNVeraenderungsHorcher;
import wfnmodell.elemente.EWFNElement;
import wfnmodell.elemente.WFNElementStelle;
import wfnmodell.elemente.WFNElementTransition;
import wfnmodell.importexport.IWFNExport;
import wfnmodell.importexport.IWFNImport;
import wfnmodell.importexport.TempPNMLElement;
import wfnmodell.schnittstellen.IWFNElement;
import wfnmodell.schnittstellen.IWFNElementKante;
import wfnmodell.schnittstellen.IWFNElementStelle;
import wfnmodell.schnittstellen.IWFNElementOK;
import wfnmodell.schnittstellen.IWFNModellVeraendern;
import wfnmodell.schnittstellen.IWFNModellWiedergeben;

/**
 * Das Datenmodell des Workflownetzes.
 *
 */
public class WFNModell implements 	IWFNModellVeraendern, 
									IWFNModellWiedergeben, 
									IWFNImport, 
									IWFNExport {
	
	/**
	 * Die aktuelle {@link IdentifierVerwaltung}.
	 */
	private IdentifierVerwaltung idVerwaltung;
	/**
	 * Liste aller Transitionen und Stellen des aktuellen Datenmodells.
	 */
	private ArrayList<IWFNElementOK> elementListeOK;
	/**
	 * Liste der Horcher, die über eine Modell-Änderung informiert werden möchten.
	 */
	private ArrayList<IWFNVeraenderungsHorcher> wfnVeraenderungsHorcherListe;
	/**
	 * Die aktuelle {@link StartEndStellenVerwaltung}.
	 */
	private StartEndStellenVerwaltung startEndStellenVerwaltung;
	/**
	 * Die aktuelle {@link ZusammenhangsVerwaltung}.
	 */
	private ZusammenhangsVerwaltung zusammenhangsVerwaltung;
	/**
	 * Die aktuelle {@link KantenVerwaltung}.
	 */
	private KantenVerwaltung kantenVerwaltung;
	/**
	 * Referenz auf die letzte importierte oder exportierte pnml-Datei.
	 */
	private File letzteWFNDatei;
	/**
	 * true, solange es seit dem letzten Öffnen oder Speichern des Datenmodells zu keiner Änderung des Models kam
	 */
	private boolean istGespeichert;

	public WFNModell() {
		newInit();
		wfnVeraenderungsHorcherListe = new ArrayList<IWFNVeraenderungsHorcher>();
	}

	/**
	 * Trennt Initialisierung und Konstruktor, so kann das Datenmodell durch newInit() auf einfachste
	 * Art zurückgesetzt werden.
	 */
	private void newInit() {
		idVerwaltung = new IdentifierVerwaltung();
		elementListeOK = new ArrayList<IWFNElementOK>();
		zusammenhangsVerwaltung = new ZusammenhangsVerwaltung();
		startEndStellenVerwaltung = new StartEndStellenVerwaltung(zusammenhangsVerwaltung);
		kantenVerwaltung = new KantenVerwaltung(idVerwaltung, startEndStellenVerwaltung,
				zusammenhangsVerwaltung);
		istGespeichert = false;
	}
	
	@Override
	public void addVeraenderungsHorcher(IWFNVeraenderungsHorcher horcher) {
		wfnVeraenderungsHorcherListe.add(horcher);
	}
	
	@Override
	public void removeVeraenderungsHorcher(IWFNVeraenderungsHorcher horcher) {
		if (wfnVeraenderungsHorcherListe.contains(horcher)) 
			wfnVeraenderungsHorcherListe.remove(horcher);
	}
	
	/**
	 * Erstellt ein neues {@link WFNStatusInfo} und sendet es an alle Horcher der Liste {@link #wfnVeraenderungsHorcherListe}.
	 */
	private void fireModellAenderungEingetreten() {
		WFNStatusInfo statusInfo = WFNStatusInfo.getInfo(elementListeOK, kantenVerwaltung.getAlleKanten(), startEndStellenVerwaltung);
		for (IWFNVeraenderungsHorcher horcher : wfnVeraenderungsHorcherListe)
			horcher.modellAenderungEingetreten(statusInfo);
		istGespeichert = false;
	}
	
	@Override
	public void setWFNDatei(File datei) {
		letzteWFNDatei = datei;
	}
	
	@Override
	public File getWFNDatei() {
		return letzteWFNDatei;
	}


	@Override
	public void clear() {
		newInit();
		fireModellAenderungEingetreten();
	}
	
	@Override
	public void loesche(ArrayList<? extends IWFNElement> elementListe) {
		for (IWFNElement element : elementListe)
			loescheOhneEventFire(element);
		fireModellAenderungEingetreten();
	}
	
	@Override
	public void loesche(IWFNElement element) {
		loescheOhneEventFire(element);
		fireModellAenderungEingetreten();
	}
	
	/**
	 * Private Methode zur Löschung eines Elements aus dem Datenmodell, ohne eine Modelländerung bekannt zu machen.
	 * Handelt es sich bei dem Element um eine Stelle oder Transition, werden auch alle Kanten, die in diesem Element
	 * enden oder beginnen, gelöscht.
	 * @param element das zu löschende Element
	 */
	private void loescheOhneEventFire(IWFNElement element) {
		if (element.getTyp() != EWFNElement.KANTE) {
			IWFNElementOK elementOK = (IWFNElementOK)element; 
			if (elementListeOK.contains(elementOK)) {
				for (IWFNElementOK nachbar : elementOK.getKantenVon()) {
					kantenVerwaltung.loescheKante(nachbar, elementOK);
				}
				for (IWFNElementOK nachbar : elementOK.getKantenZu()) {
					kantenVerwaltung.loescheKante(elementOK, nachbar);
				}
				idVerwaltung.idWiederFrei(elementOK.getID());
				if (elementOK.getTyp() == EWFNElement.STELLE) 
					startEndStellenVerwaltung.remove((WFNElementStelle) elementOK);
			    elementListeOK.remove(elementOK);
			}
		} else 
			kantenVerwaltung.loescheKante((IWFNElementKante) element);
	}
	
	@Override
	public void neueStelle(Point position) {
		neueStelle("", "", position, false);
	}

	@Override
	public void neueStelle(String pnmlID, String name, Point position, boolean marke) {
		idVerwaltung.pnmlIDUeberwachung(pnmlID);
		final int neueID = idVerwaltung.getNextFreeIdentifier();
		WFNElementStelle stelle = new WFNElementStelle(pnmlID, neueID, position);
		if (!name.equals("")) stelle.setName(name);
		if (marke) stelle.setMarke(marke);
		elementListeOK.add(stelle);
		startEndStellenVerwaltung.add(stelle);
		fireModellAenderungEingetreten();
	}

	@Override
	public void neueTransition(Point position) {
		neueTransition("", "", position);
	}

	@Override
	public void neueTransition(String pnmlID, String name, Point position) {
		idVerwaltung.pnmlIDUeberwachung(pnmlID);
		final int neueID = idVerwaltung.getNextFreeIdentifier();
		WFNElementTransition transition = new WFNElementTransition(pnmlID, neueID, position);
		if ( !name.equals("")) transition.setName(name);
		elementListeOK.add(transition);
		fireModellAenderungEingetreten();
	}
	
	@Override
	public void neueKante(String pnmlIDKante, String pnmlIDSource, String pnmlIDTarget) {
		boolean sourceNichtGefunden = true;
		boolean targetNichtGefunden = true;
		Iterator<IWFNElementOK> it = elementListeOK.iterator();
		IWFNElementOK von = null;
		IWFNElementOK zu = null;
		while ((it.hasNext()) && (sourceNichtGefunden || targetNichtGefunden)) {
			IWFNElementOK elem = it.next();
			String pnmlID = elem.getPNMLID();
			if (pnmlID.equals(pnmlIDSource)) {
				von = elem;
				sourceNichtGefunden = false;
			} else 
				if (pnmlID.equals(pnmlIDTarget)) {
					zu = elem;
					targetNichtGefunden = false;
				}
		}
		if (( !sourceNichtGefunden) && ( !targetNichtGefunden)) 
			neueKante(pnmlIDKante, von, zu);
	}
	
	@Override
	public void neueKante(IWFNElementOK von, IWFNElementOK zu) {
		neueKante("", von, zu);
	}
	
	/**
	 * Übergibt das Erschaffen der neuen Kante der {@link #kantenVerwaltung}, und löst Informationsweitergabe
	 * zur Modelländerung aus.
	 * @param pnmlID ID aus der pnml-Datei oder ""
	 * @param von Element, von dem die neue Kante ausgeht
	 * @param zu Element, in dem die neue Kante endet
	 */
	private void neueKante(String pnmlID, IWFNElementOK von, IWFNElementOK zu) {
		kantenVerwaltung.neueKante(pnmlID, von, zu);
		fireModellAenderungEingetreten();
	}
	
	@Override
	public void setElementName(IWFNElementOK element, String name) {
		element.setName(name);
		fireModellAenderungEingetreten();
	}
	
	@Override
	public ArrayList<IWFNElementOK> getAlleElementeZumZeichnen() {
		return elementListeOK;
	}
	
	/**
	 * Gibt alle Ellemente, also auch Kanten, zurück.
	 * @return Liste aller Stellen, Transitionen und Kanten des aktuellen Datenmodells.
	 */
	private ArrayList<? extends IWFNElement> getAlleElemente() {
		ArrayList<IWFNElement> ergebnis = new ArrayList<IWFNElement>(elementListeOK);
		ergebnis.addAll(kantenVerwaltung.getAlleKanten());
		return ergebnis;
	}

	@Override
	public boolean istDasWFNSoSchonGespeichert() {
		return istGespeichert;
	}
	
	@Override
	public void setIstDasWFNSoSchonGespeichert(boolean b) {
		istGespeichert = b;
	}

	@Override
	public String toString() {
		String ergebnis = "";
		if (letzteWFNDatei != null) 
			ergebnis = "Geöffnete Datei = " + letzteWFNDatei.getName() + "\n";
		ergebnis += idVerwaltung.toString() + "\n"
 					+ zusammenhangsVerwaltung.toString() + "\n"
					+ startEndStellenVerwaltung.toString() + "\n"
					+ kantenVerwaltung.toString() + "\n"
					+ (getAlleElemente()).toString();
		return ergebnis;
	}

	@Override
	public ArrayList<TempPNMLElement> getAlleElementeFuerExport() {
		ArrayList<? extends IWFNElement> alleElemente = getAlleElemente();
		ArrayList<TempPNMLElement> ergebnis = new ArrayList<>(alleElemente.size());
		for (IWFNElement elem : alleElemente) {
			String pnmlID = elem.getPNMLID();
			if (pnmlID.equals("")) pnmlID = idVerwaltung.idZuPNMLID(elem.getID());
			String marke = "";
			EWFNElement typ = elem.getTyp();
			TempPNMLElement temp;
			switch (typ) {
    		case STELLE: 	if (((IWFNElementStelle) elem).hatMarke()) marke = "1"; 
    						else marke = "0";	
    		case TRANSITION:String name = ((IWFNElementOK) elem).getName();
    						String x = String.valueOf((((IWFNElementOK) elem).getPosition()).x);
    						String y = String.valueOf((((IWFNElementOK) elem).getPosition()).y);
    						temp = new TempPNMLElement(typ,pnmlID,name,x,y,marke);
    						ergebnis.add(temp);
    						break;
    		case KANTE:		String vonPNMLID = (((IWFNElementKante) elem).getVon()).getPNMLID();
    						String zuPNMLID = (((IWFNElementKante) elem).getZu()).getPNMLID();
    						if (vonPNMLID.equals("")) vonPNMLID = 
    							idVerwaltung.idZuPNMLID((((IWFNElementKante) elem).getVon()).getID());
    						if (zuPNMLID.equals("")) zuPNMLID = 
        						idVerwaltung.idZuPNMLID((((IWFNElementKante) elem).getZu()).getID());
    							ergebnis.add(new TempPNMLElement(typ,pnmlID,vonPNMLID,zuPNMLID));
    						break;
			}
		}
		return ergebnis;
	}

	
}
