package wfnmodell.importexport;

import java.awt.Point;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import wfnmodell.elements.EWfnElement;

/**
 * Oberklasse zum Importieren einer unbestimmten Menge von Elementen in das Datenmodell.
 *
 */
public class ImportManagement {
	/**
	 * Die Datei, aus der importiert werden soll.
	 */
	private File pnmlFile;
	/**
	 * Import-Schnittstelle zum Datenmodell.
	 */
	private IWfnImport wfnModel;
	

	/**
	 * Konstruktor zur Instanzierung eines ImportManagement-Objekts, 
	 * welches einen Konkreten Import-Vorgang umsetzen soll.
	 * @param pnml die Datei, aus der importiert werden soll
	 * @param wfnModel die Import-Schnittstelle zum Datenmodell
	 */
	public ImportManagement(File pnml, IWfnImport wfnModel) {
		this.pnmlFile = pnml;
		this.wfnModel = wfnModel;
	}
	
	/**
	 * Führt den konkreten Importvorgang mit Hilfe des Parsers {@link PnmlParserAdapted} durch.
	 * Dabei werden zuerst alle Stellen und Transitionen in das Datenmodell übertragen, und erst dann die Kanten,
	 * um sicher zu gehen, dass die Elemente, die durch die Kante verbunden werden, schon existieren.
	 * @return die Anzahl der importierten Elemente, oder -1 wenn es ein Problem beim Öffnen der Datei gab
	 */
	public int startImport(){
		int result = -1;
		Point position;
		HashMap<String,PnmlElement> pnmlElements;
		if (pnmlFile.exists()) {
            PnmlParserAdapted pnmlParser = new PnmlParserAdapted(pnmlFile);
            pnmlParser.initParser();
            pnmlParser.parse();
            pnmlElements = pnmlParser.getPnmlImport();
            if (pnmlElements != null) {
            	wfnModel.clear();
            	wfnModel.setWfnFile(pnmlFile);
            	Set<String> keySet = pnmlElements.keySet();
            	result = keySet.size();
            	HashSet<String> arcPnmlIDs = new HashSet<String>();
            	Iterator<String> it = keySet.iterator();
            	while (it.hasNext()) {
            		PnmlElement elem = pnmlElements.get(it.next());
            		switch (elem.getType()) {
            		case PLACE: 	position = convertPnmlPosition(elem.getX(), elem.getY()); 
            						boolean marke = convertPnmlMarking(elem.getMarking());
            						wfnModel.createPlace(elem.getPNMLID(), elem.getName(), 
            						position , marke);
            						break;
            		case TRANSITION:position = convertPnmlPosition(elem.getX(), elem.getY());
            						wfnModel.createTransition(elem.getPNMLID(), elem.getName(), 
        							position);
            						break;
            		case ARC: 		arcPnmlIDs.add(elem.getPNMLID());
            						break;
            		}
            	}
            	it = arcPnmlIDs.iterator();
            	while (it.hasNext()) {
            		PnmlElement elem = pnmlElements.get(it.next());
            		EWfnElement source = (pnmlElements.get(elem.getPnmlIDSource())).getType();
            		EWfnElement target = (pnmlElements.get(elem.getPnmlIDTarget())).getType();
            		if ((source != target) 
            			&& (source != EWfnElement.ARC) 
            			&& (target != EWfnElement.ARC)) {          			
            				wfnModel.createArc( elem.getPNMLID(), elem.getPnmlIDSource(), 
            					elem.getPnmlIDTarget() );
            		} else {
            			result--;
            		}
            	}			
            }
        } 
		return result;
	}
	
	/**
	 * Wandelt zwei Strings in einen Point um.
	 * @param x der zu parsende X-Wert 
	 * @param y der zu parsende Y-Wert
	 * @return Point mit den geparsten Werten
	 */
	private static Point convertPnmlPosition(String x, String y) {
		return new Point(Integer.parseInt(x), Integer.parseInt(y));
	}
	
	/**
	 * Zur Umrechnung des Markierungswerts der pnml-Datei in einen booleschen Wert.
	 * @param marking der umzurechnende Wert
	 * @return true, falls markierung == "1"
	 */
	private static boolean convertPnmlMarking(String marking) {
		return (Integer.parseInt(marking) == 1);
	}

}
