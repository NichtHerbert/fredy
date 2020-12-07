package wfnmodell.importexport;

import java.awt.Point;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import wfnmodell.elemente.EWFNElement;

/**
 * Oberklasse zum Importieren einer unbestimmten Menge von Elementen in das Datenmodell.
 *
 */
public class ImportVerwaltung {
	/**
	 * Die Datei, aus der importiert werden soll.
	 */
	private File pnmlDatei;
	/**
	 * Import-Schnittstelle zum Datenmodell.
	 */
	private IWFNImport wfnModell;
	

	/**
	 * Konstruktor zur Instanzierung eines ImportVerwaltung-Objekts, 
	 * welches einen Konkreten Import-Vorgang umsetzen soll.
	 * @param pnml die Datei, aus der importiert werden soll
	 * @param wfnModell die Import-Schnittstelle zum Datenmodell
	 */
	public ImportVerwaltung(File pnml, IWFNImport wfnModell) {
		this.pnmlDatei = pnml;
		this.wfnModell = wfnModell;
	}
	
	/**
	 * Führt den konkreten Importvorgang mit Hilfe des Parsers {@link PNMLParserAngepasst} durch.
	 * Dabei werden zuerst alle Stellen und Transitionen in das Datenmodell übertragen, und erst dann die Kanten,
	 * um sicher zu gehen, dass die Elemente, die durch die Kante verbunden werden, schon existieren.
	 * @return die Anzahl der importierten Elemente, oder -1 wenn es ein Problem beim Öffnen der Datei gab
	 */
	public int startImport(){
		int ergebnis = -1;
		Point position;
		HashMap<String,TempPNMLElement> pnmlElemente;
		if (pnmlDatei.exists()) {
            PNMLParserAngepasst pnmlParser = new PNMLParserAngepasst(pnmlDatei);
            pnmlParser.initParser();
            pnmlParser.parse();
            pnmlElemente = pnmlParser.getPNMLImport();
            if (pnmlElemente != null) {
            	wfnModell.clear();
            	wfnModell.setWFNDatei(pnmlDatei);
            	Set<String> keySet = pnmlElemente.keySet();
            	ergebnis = keySet.size();
            	HashSet<String> kantenKeys = new HashSet<String>();
            	Iterator<String> it = keySet.iterator();
            	while (it.hasNext()) {
            		TempPNMLElement elem = pnmlElemente.get(it.next());
            		switch (elem.getTyp()) {
            		case STELLE: 	position = getPositionToPoint(elem.getX(), elem.getY()); 
            						boolean marke = getMarkierungToBoolean(elem.getMarkiert());
            						wfnModell.neueStelle(elem.getPNMLID(), elem.getName(), 
            						position , marke);
            						break;
            		case TRANSITION:position = getPositionToPoint(elem.getX(), elem.getY());
            						wfnModell.neueTransition(elem.getPNMLID(), elem.getName(), 
        							position);
            						break;
            		case KANTE: kantenKeys.add(elem.getPNMLID());
            						break;
            		}
            	}
            	it = kantenKeys.iterator();
            	while (it.hasNext()) {
            		TempPNMLElement elem = pnmlElemente.get(it.next());
            		EWFNElement source = (pnmlElemente.get(elem.getPnmlIDSource())).getTyp();
            		EWFNElement target = (pnmlElemente.get(elem.getPnmlIDTarget())).getTyp();
            		if ((source != target) 
            			&& (source != EWFNElement.KANTE) 
            			&& (target != EWFNElement.KANTE)) {          			
            				wfnModell.neueKante( elem.getPNMLID(), elem.getPnmlIDSource(), 
            					elem.getPnmlIDTarget() );
            		} else {
            			ergebnis--;
            		}
            	}			
            }
        } 
		return ergebnis;
	}
	
	/**
	 * Wandelt zwei Strings in einen Point um.
	 * @param x der zu parsende X-Wert 
	 * @param y der zu parsende Y-Wert
	 * @return Point mit den geparsten Werten
	 */
	private static Point getPositionToPoint(String x, String y) {
		return new Point(Integer.parseInt(x), Integer.parseInt(y));
	}
	
	/**
	 * Zur Umrechnung des Markierungswerts der pnml-Datei in einen booleschen Wert.
	 * @param markierung der umzurechnende Wert
	 * @return true, falls markierung == "1"
	 */
	private static boolean getMarkierungToBoolean(String markierung) {
		return (Integer.parseInt(markierung) == 1) ? true : false ;
	}

}
