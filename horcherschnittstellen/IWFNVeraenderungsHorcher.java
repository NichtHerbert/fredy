package horcherschnittstellen;

import wfnmodell.WFNStatusInfo;

/**
 * Schnittstelle, um über Veränderungen am Datenmodell informiert werden zu können.
 *
 */
public interface IWFNVeraenderungsHorcher {
	
	/**
	 * Wird aufgerufen, wenn eine Änderung am Modell eingetreten ist.
	 * @param statusInfo der aktuelle Zustand des WFN
	 */
	void modellAenderungEingetreten(WFNStatusInfo statusInfo);
}
