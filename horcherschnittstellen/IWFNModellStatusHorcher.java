package horcherschnittstellen;

import wfnmodell.WFNStatusInfo;

/**
 * Schnittstelle, um über Veränderungen am Modell oder an den Markierungen /aktivierten Transitionen
 * informiert werden zu können.
 *
 */
public interface IWFNModellStatusHorcher {
	
	/**Wird aufgerufen, wenn es zu einer Änderung am Modell und oder den Markierungen der Stellen kam. 
	 * @param statusInfo der aktuelle Zustand des WFN inklusive aller Infos der {@link verwaltung.MarkierungsVerwaltung}.
	 */
	void modellStatusAenderung(WFNStatusInfo statusInfo);
}
