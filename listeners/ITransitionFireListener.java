package listeners;

import wfnmodel.interfaces.IWfnTransition;

/**
 * Schnittstelle, um das Schalten von Transitionen umzusetzen.
 *
 */
public interface ITransitionFireListener {
	
	/**
	 * Löscht alle Marken, aktivierten Transitionen und Kontakt-Transitionen,
	 * und markiert nur die Startstelle und die durch sie aktivierten Transitionen.
	 */
	void everythingBackToStart();
	
	/**
	 * Schaltet die übergebene Transition.
	 * @param transition die zu schaltende Transition
	 */
	void fireTransition(IWfnTransition transition);
}
