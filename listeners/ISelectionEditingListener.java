package listeners;

import java.util.ArrayList;

import wfnmodel.interfaces.IWfnElement;
import wfnmodel.interfaces.IWfnTransitionAndPlace;

/**
 * Schnittstelle um Änderungen an ausgewählten Elementen des Workflownetzes vorzunehmen.
 *
 */
public interface ISelectionEditingListener {
	
	/**
	 * Sorgt für das Löschen der übergebenen Elemente.
	 * @param selectedElements Liste der zu löschenden Elemente
	 */
	void elementsToDelete(ArrayList<? extends IWfnElement> selectedElements);
	
	/**
	 * Sorgt für die Namensänderung des übergebenen Elements
	 * @param element Element, dessen Name geändert werden soll
	 * @param name der zu setzende Name
	 */
	void elementToSetName(IWfnTransitionAndPlace element, String name);
}
