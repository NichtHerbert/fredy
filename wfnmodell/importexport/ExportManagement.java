package wfnmodell.importexport;

import java.io.File;
import java.util.ArrayList;

import wfnmodell.elements.EWfnElement;

/**
 * Oberklasse zum Exportieren des Datenmodells in eine *.pnml-Datei.
 *
 */
public class ExportManagement {
	/**
	 * Die Datei, in welches das Datenmodell exportiert werden soll.
	 */
	private File pnmlFile;
	/**
	 * Die Export-Schnittstelle zum Datenmodell.
	 */
	private IWfnExport expModel;
	

	/**
	 * Konstruktor zur Instanzierung eines ExportManagement-Objekts, 
	 * welches einen Konkreten Export-Vorgang umsetzen soll.
	 * @param pnmlFile die Datei, in die exportiert werden soll
	 * @param expModel die Export-Schnittstelle zum Datenmodell
	 */
	public ExportManagement(File pnmlFile, IWfnExport expModel) {
		this.pnmlFile = pnmlFile;
		this.expModel = expModel;
	}
	
	/**
	 * Führt den konkreten Exportvorgang durch.
	 */
	public void startExport() {
		ArrayList<PnmlElement> pnmlElements = expModel.getAllElementsForExport();
		if (pnmlElements.size() > 0) {
			PNMLWriter pnmlWriter = new PNMLWriter(pnmlFile);
            pnmlWriter.startXMLDocument();
            // TODO: One for loop and switch case
			for (PnmlElement elem : pnmlElements) 
				if (elem.getType() == EWfnElement.PLACE) { 
					pnmlWriter.addPlace(elem.getPNMLID(), elem.getName(), elem.getX(), elem.getY(), elem.getMarking());
				}
			for (PnmlElement elem : pnmlElements) 
				if (elem.getType() == EWfnElement.TRANSITION) 
					pnmlWriter.addTransition(elem.getPNMLID(), elem.getName(), elem.getX(), elem.getY());
			for (PnmlElement elem : pnmlElements) 
				if (elem.getType() == EWfnElement.ARC) 
					pnmlWriter.addArc(elem.getPNMLID(), elem.getPnmlIDSource(), elem.getPnmlIDTarget());
			pnmlWriter.finishXMLDocument();
		}
	}

}
