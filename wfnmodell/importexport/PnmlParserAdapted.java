package wfnmodell.importexport;

import java.io.File;
import java.util.HashMap;

import wfnmodell.elements.EWfnElement;

/**
 * Erweiterung der vorgegebenen Klasse {@link PNMLParser}.
 *
 */
public class PnmlParserAdapted extends PNMLParser {
	
	/**
	 * HashMap zur Ablage der geparsten Elemente als Objekte der Klasse {@link PnmlElement},
	 * als Key dienen die IDs aus der pnml-Datei.
	 */
	private HashMap<String,PnmlElement> pnmlImport;

	public PnmlParserAdapted(File pnml) {
		super(pnml);
		pnmlImport = new HashMap<>();
	}
	
	/**
	 * Gibt alle aus einer pnml-Datei erparsten Elemente in Form von Objekten der Klasse {@link PnmlElement} zurück.
	 * Sie wird vorzugsweise erst ausgeführt, wenn das Parsen abgeschlossen ist!
	 * @return HashMap aller importierten Elemente
	 */
	public HashMap<String,PnmlElement> getPnmlImport() {
		return pnmlImport;
	}
	
	@Override
    public void newTransition(final String id) {
        pnmlImport.put(id, new PnmlElement(EWfnElement.TRANSITION, id));
    }

    @Override
    public void newPlace(final String id) {
    	pnmlImport.put(id, new PnmlElement(EWfnElement.PLACE, id));
    }

    @Override
    public void newArc(final String id, final String source, final String target) {
    	PnmlElement elem = new PnmlElement(EWfnElement.ARC, id);
    	elem.setPnmlIDSource(source);
    	elem.setPnmlIDTarget(target);
    	pnmlImport.put(id, elem);
    }

    @Override
    public void setPosition(final String id, final String x, final String y) {
        (pnmlImport.get(id)).setPosition(x,y);
    }

    @Override
    public void setName(final String id, final String name) {
    	(pnmlImport.get(id)).setName(name);
    }

    @Override
    public void setMarking(final String id, final String marking) {
    	(pnmlImport.get(id)).setMarking(marking);
    }
}