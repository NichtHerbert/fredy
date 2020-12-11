package listeners;

import wfnmodel.WfnStatusInfo;

/**
 * Schnittstelle, um über Veränderungen am Datenmodell informiert werden zu können.
 *
 */
public interface IWfnNetListener {
	
	/**
	 * Wird aufgerufen, wenn eine Änderung am Modell eingetreten ist.
	 * @param statusInfo der aktuelle Zustand des WFN
	 */
	void netChangeOccurred(WfnStatusInfo statusInfo);
}
