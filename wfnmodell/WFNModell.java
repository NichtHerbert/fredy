package wfnmodell;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import horcherschnittstellen.IWFNVeraenderungsHorcher;
import wfnmodell.elements.EWfnElement;
import wfnmodell.elements.WfnElementPlace;
import wfnmodell.elements.WfnElementTransition;
import wfnmodell.importexport.IWFNExport;
import wfnmodell.importexport.IWFNImport;
import wfnmodell.importexport.TempPNMLElement;
import wfnmodell.interfaces.IWfnElement;
import wfnmodell.interfaces.IWfnArc;
import wfnmodell.interfaces.IWfnTransitionAndPlace;
import wfnmodell.interfaces.IWfnPlace;
import wfnmodell.interfaces.IWfnModelChanging;
import wfnmodell.interfaces.IWfnModelPresentation;

/**
 * Das Datenmodell des Workflownetzes.
 *
 */
public class WFNModell implements 	IWfnModelChanging, 
									IWfnModelPresentation, 
									IWFNImport, 
									IWFNExport {
	
	/**
	 * Die aktuelle {@link IDManagement}.
	 */
	private IDManagement idVerwaltung;
	/**
	 * Liste aller Transitionen und Stellen des aktuellen Datenmodells.
	 */
	private ArrayList<IWfnTransitionAndPlace> elementListeOK;
	/**
	 * Liste der Horcher, die über eine Modell-Änderung informiert werden möchten.
	 */
	private ArrayList<IWFNVeraenderungsHorcher> wfnVeraenderungsHorcherListe;
	/**
	 * Die aktuelle {@link StartEndManagement}.
	 */
	private StartEndManagement startEndStellenVerwaltung;
	/**
	 * Die aktuelle {@link ConnectionManagement}.
	 */
	private ConnectionManagement zusammenhangsVerwaltung;
	/**
	 * Die aktuelle {@link ArcManagement}.
	 */
	private ArcManagement kantenVerwaltung;
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
		idVerwaltung = new IDManagement();
		elementListeOK = new ArrayList<IWfnTransitionAndPlace>();
		zusammenhangsVerwaltung = new ConnectionManagement();
		startEndStellenVerwaltung = new StartEndManagement(zusammenhangsVerwaltung);
		kantenVerwaltung = new ArcManagement(idVerwaltung, startEndStellenVerwaltung,
				zusammenhangsVerwaltung);
		istGespeichert = false;
	}
	
	@Override
	public void addChangingListener(IWFNVeraenderungsHorcher horcher) {
		wfnVeraenderungsHorcherListe.add(horcher);
	}
	
	@Override
	public void removeChangingListener(IWFNVeraenderungsHorcher horcher) {
		if (wfnVeraenderungsHorcherListe.contains(horcher)) 
			wfnVeraenderungsHorcherListe.remove(horcher);
	}
	
	/**
	 * Erstellt ein neues {@link WfnStatusInfo} und sendet es an alle Horcher der Liste {@link #wfnVeraenderungsHorcherListe}.
	 */
	private void fireModellAenderungEingetreten() {
		WfnStatusInfo statusInfo = WfnStatusInfo.getInfo(elementListeOK, kantenVerwaltung.getAllArcs(), startEndStellenVerwaltung);
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
	public void delete(ArrayList<? extends IWfnElement> elementListe) {
		for (IWfnElement element : elementListe)
			loescheOhneEventFire(element);
		fireModellAenderungEingetreten();
	}
	
	@Override
	public void delete(IWfnElement element) {
		loescheOhneEventFire(element);
		fireModellAenderungEingetreten();
	}
	
	/**
	 * Private Methode zur Löschung eines Elements aus dem Datenmodell, ohne eine Modelländerung bekannt zu machen.
	 * Handelt es sich bei dem Element um eine Stelle oder Transition, werden auch alle Kanten, die in diesem Element
	 * enden oder beginnen, gelöscht.
	 * @param element das zu löschende Element
	 */
	private void loescheOhneEventFire(IWfnElement element) {
		if (element.getWfnElementType() != EWfnElement.ARC) {
			IWfnTransitionAndPlace elementOK = (IWfnTransitionAndPlace)element; 
			if (elementListeOK.contains(elementOK)) {
				for (IWfnTransitionAndPlace nachbar : elementOK.getInputElements()) {
					kantenVerwaltung.deleteArc(nachbar, elementOK);
				}
				for (IWfnTransitionAndPlace nachbar : elementOK.getOutputElements()) {
					kantenVerwaltung.deleteArc(elementOK, nachbar);
				}
				idVerwaltung.passBack(elementOK.getID());
				if (elementOK.getWfnElementType() == EWfnElement.PLACE) 
					startEndStellenVerwaltung.remove((WfnElementPlace) elementOK);
			    elementListeOK.remove(elementOK);
			}
		} else 
			kantenVerwaltung.deleteArc((IWfnArc) element);
	}
	
	@Override
	public void createPlace(Point position) {
		neueStelle("", "", position, false);
	}

	@Override
	public void neueStelle(String pnmlID, String name, Point position, boolean marke) {
		idVerwaltung.pnmlIDMonitoring(pnmlID);
		final int neueID = idVerwaltung.get();
		WfnElementPlace stelle = new WfnElementPlace(pnmlID, neueID, position);
		if (!name.equals("")) stelle.setName(name);
		if (marke) stelle.setMarking(marke);
		elementListeOK.add(stelle);
		startEndStellenVerwaltung.add(stelle);
		fireModellAenderungEingetreten();
	}

	@Override
	public void createTransition(Point position) {
		neueTransition("", "", position);
	}

	@Override
	public void neueTransition(String pnmlID, String name, Point position) {
		idVerwaltung.pnmlIDMonitoring(pnmlID);
		final int neueID = idVerwaltung.get();
		WfnElementTransition transition = new WfnElementTransition(pnmlID, neueID, position);
		if ( !name.equals("")) transition.setName(name);
		elementListeOK.add(transition);
		fireModellAenderungEingetreten();
	}
	
	@Override
	public void neueKante(String pnmlIDKante, String pnmlIDSource, String pnmlIDTarget) {
		boolean sourceNichtGefunden = true;
		boolean targetNichtGefunden = true;
		Iterator<IWfnTransitionAndPlace> it = elementListeOK.iterator();
		IWfnTransitionAndPlace von = null;
		IWfnTransitionAndPlace zu = null;
		while ((it.hasNext()) && (sourceNichtGefunden || targetNichtGefunden)) {
			IWfnTransitionAndPlace elem = it.next();
			String pnmlID = elem.getPnmlID();
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
	public void createArc(IWfnTransitionAndPlace von, IWfnTransitionAndPlace zu) {
		neueKante("", von, zu);
	}
	
	/**
	 * Übergibt das Erschaffen der neuen Kante der {@link #kantenVerwaltung}, und löst Informationsweitergabe
	 * zur Modelländerung aus.
	 * @param pnmlID ID aus der pnml-Datei oder ""
	 * @param von Element, von dem die neue Kante ausgeht
	 * @param zu Element, in dem die neue Kante endet
	 */
	private void neueKante(String pnmlID, IWfnTransitionAndPlace von, IWfnTransitionAndPlace zu) {
		kantenVerwaltung.createArc(pnmlID, von, zu);
		fireModellAenderungEingetreten();
	}
	
	@Override
	public void setElementName(IWfnTransitionAndPlace element, String name) {
		element.setName(name);
		fireModellAenderungEingetreten();
	}
	
	@Override
	public ArrayList<IWfnTransitionAndPlace> getAllElementsForDrawing() {
		return elementListeOK;
	}
	
	/**
	 * Gibt alle Ellemente, also auch Kanten, zurück.
	 * @return Liste aller Stellen, Transitionen und Kanten des aktuellen Datenmodells.
	 */
	private ArrayList<? extends IWfnElement> getAlleElemente() {
		ArrayList<IWfnElement> ergebnis = new ArrayList<IWfnElement>(elementListeOK);
		ergebnis.addAll(kantenVerwaltung.getAllArcs());
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
		ArrayList<? extends IWfnElement> alleElemente = getAlleElemente();
		ArrayList<TempPNMLElement> ergebnis = new ArrayList<>(alleElemente.size());
		for (IWfnElement elem : alleElemente) {
			String pnmlID = elem.getPnmlID();
			if (pnmlID.equals("")) pnmlID = idVerwaltung.convertIDintoPnmlID(elem.getID());
			String marke = "";
			EWfnElement typ = elem.getWfnElementType();
			TempPNMLElement temp;
			switch (typ) {
    		case PLACE: 	if (((IWfnPlace) elem).hasMarking()) marke = "1"; 
    						else marke = "0";	
    		case TRANSITION:String name = ((IWfnTransitionAndPlace) elem).getName();
    						String x = String.valueOf((((IWfnTransitionAndPlace) elem).getPosition()).x);
    						String y = String.valueOf((((IWfnTransitionAndPlace) elem).getPosition()).y);
    						temp = new TempPNMLElement(typ,pnmlID,name,x,y,marke);
    						ergebnis.add(temp);
    						break;
    		case ARC:		String vonPNMLID = (((IWfnArc) elem).getSource()).getPnmlID();
    						String zuPNMLID = (((IWfnArc) elem).getTarget()).getPnmlID();
    						if (vonPNMLID.equals("")) vonPNMLID = 
    							idVerwaltung.convertIDintoPnmlID((((IWfnArc) elem).getSource()).getID());
    						if (zuPNMLID.equals("")) zuPNMLID = 
        						idVerwaltung.convertIDintoPnmlID((((IWfnArc) elem).getTarget()).getID());
    							ergebnis.add(new TempPNMLElement(typ,pnmlID,vonPNMLID,zuPNMLID));
    						break;
			}
		}
		return ergebnis;
	}

	
}
