package horcherschnittstellen;

import java.util.ArrayList;

import wfnmodell.schnittstellen.IWFNElement;
import wfnmodell.schnittstellen.IWFNElementOK;

/**
 * Schnittstelle um Änderungen an ausgewählten Elementen des Workflownetzes vorzunehmen.
 *
 */
public interface IAuswahlBearbeitetHorcher {
	
	/**
	 * Sorgt für das Löschen der übergebenen Elemente.
	 * @param ausgewaehlteElemente Liste der zu löschenden Elemente
	 */
	void auswahlSollGeloeschtWerden(ArrayList<? extends IWFNElement> ausgewaehlteElemente);
	
	/**
	 * Sorgt für die Namensänderung des übergebenen Elements
	 * @param element Element, dessen Name geändert werden soll
	 * @param name der zu setzende Name
	 */
	void elementSollNameAendern(IWFNElementOK element, String name);
}
