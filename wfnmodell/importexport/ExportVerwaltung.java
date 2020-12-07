package wfnmodell.importexport;

import java.io.File;
import java.util.ArrayList;

import wfnmodell.elemente.EWFNElement;

/**
 * Oberklasse zum Exportieren des Datenmodells in eine *.pnml-Datei.
 *
 */
public class ExportVerwaltung {
	/**
	 * Die Datei, in welches das Datenmodell exportiert werden soll.
	 */
	private File pnmlDatei;
	/**
	 * Die Export-Schnittstelle zum Datenmodell.
	 */
	private IWFNExport expModell;
	

	/**
	 * Konstruktor zur Instanzierung eines ExportVerwaltung-Objekts, 
	 * welches einen Konkreten Export-Vorgang umsetzen soll.
	 * @param pnmlDatei die Datei, in die exportiert werden soll
	 * @param expModell die Export-Schnittstelle zum Datenmodell
	 */
	public ExportVerwaltung(File pnmlDatei, IWFNExport expModell) {
		this.pnmlDatei = pnmlDatei;
		this.expModell = expModell;
	}
	
	/**
	 * Führt den konkreten Exportvorgang durch.
	 */
	public void startExport() {
		ArrayList<TempPNMLElement> alleElemente = expModell.getAlleElementeFuerExport();
		if (alleElemente.size() > 0) {
			PNMLWriter pnmlWriter = new PNMLWriter(pnmlDatei);
            pnmlWriter.startXMLDocument();
			for (TempPNMLElement elem : alleElemente) 
				if (elem.getTyp() == EWFNElement.STELLE) { 
					pnmlWriter.addPlace(elem.getPNMLID(), elem.getName(), elem.getX(), elem.getY(), elem.getMarkiert());
				}
			for (TempPNMLElement elem : alleElemente) 
				if (elem.getTyp() == EWFNElement.TRANSITION) 
					pnmlWriter.addTransition(elem.getPNMLID(), elem.getName(), elem.getX(), elem.getY());
			for (TempPNMLElement elem : alleElemente) 
				if (elem.getTyp() == EWFNElement.KANTE) 
					pnmlWriter.addArc(elem.getPNMLID(), elem.getPnmlIDSource(), elem.getPnmlIDTarget());
			pnmlWriter.finishXMLDocument();
		}
	}

}
