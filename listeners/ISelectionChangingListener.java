package listeners;

import java.util.ArrayList;

import wfnmodel.interfaces.IWfnElement;

/**
 * Schnittstelle, um über eine Veränderung der Auswahlliste informiert werden zu können,
 *
 */
public interface ISelectionChangingListener {
	
	final int NEW_SELECTION = 0;
	final int ARC_SELECTION = 1;
	
	/**
	 * Wird aufgerufen, wenn es zu einer Veränderung der Auswahlliste kam.
	 * @param auswahlArt NEW_SELECTION oder ARC_SELECTION
	 * @param ausgewaehlteElemente Liste der aktuell ausgewählten Elemente
	 */
	void selectionChangeOccurred(int auswahlArt, ArrayList<? extends IWfnElement> ausgewaehlteElemente);
}
