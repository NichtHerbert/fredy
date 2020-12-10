package wfnmodel.importexport;

import java.io.File;
import java.util.ArrayList;

import wfnmodel.elements.EWfnElement;

/**
 * Oberklasse zum Exportieren des Datenmodells in eine *.pnml-Datei.
 *
 */
public class Export {
	
	/**
	 * Führt den konkreten Exportvorgang durch.
	 */
	public static void execute(File pnmlFile, IWfnExport expModel) {
		ArrayList<PnmlElement> pnmlElements = expModel.getAllElementsForExport();
		if (pnmlElements.size() > 0) {
			PNMLWriter pnmlWriter = new PNMLWriter(pnmlFile);
            pnmlWriter.startXMLDocument();
            // TODO: One for loop and switch case OR is there a predetermined order of element types?
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
