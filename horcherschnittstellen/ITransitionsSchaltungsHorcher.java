package horcherschnittstellen;

import wfnmodell.interfaces.IWfnTransition;

/**
 * Schnittstelle, um das Schalten von Transitionen umzusetzen.
 *
 */
public interface ITransitionsSchaltungsHorcher {
	
	/**
	 * Löscht alle Marken, aktivierten Transitionen und Kontakt-Transitionen,
	 * und markiert nur die Startstelle und die durch sie aktivierten Transitionen.
	 */
	void allesZurueckAufStart();
	
	/**
	 * Schaltet die übergebene Transition.
	 * @param transition die zu schaltende Transition
	 */
	void schalteTransition(IWfnTransition transition);
}
