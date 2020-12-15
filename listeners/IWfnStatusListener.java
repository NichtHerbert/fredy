package listeners;

import wfnmodel.WfnStatusInfo;

/**
 * Schnittstelle, um über Veränderungen am Modell oder an den Markierungen /aktivierten Transitionen
 * informiert werden zu können.
 *
 */
public interface IWfnStatusListener {
	
	/**Wird aufgerufen, wenn es zu einer Änderung am Modell und oder den Markierungen der Stellen kam. 
	 * @param statusInfo der aktuelle Zustand des WFN inklusive aller Infos der {@link control.MarkingManagement}.
	 */
	void newWfnStatus(WfnStatusInfo statusInfo);
}
