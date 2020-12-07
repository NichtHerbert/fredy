package horcherschnittstellen;

import java.util.ArrayList;

import wfnmodell.schnittstellen.IWFNElement;

/**
 * Schnittstelle, um über eine Veränderung der Auswahlliste informiert werden zu können,
 *
 */
public interface IAuswahlVeraenderungsHorcher {
	
	final int NEUE_AUSWAHL = 0;
	final int KANTEN_AUSWAHL = 1;
	
	/**
	 * Wird aufgerufen, wenn es zu einer Veränderung der Auswahlliste kam.
	 * @param auswahlArt NEUE_AUSWAHL oder KANTEN_AUSWAHL
	 * @param ausgewaehlteElemente Liste der aktuell ausgewählten Elemente
	 */
	void auswahlAenderungEingetreten(int auswahlArt, ArrayList<? extends IWFNElement> ausgewaehlteElemente);
}
