package listeners;

/**
 * Schnittstelle, um bei einer Veränderung des Zoomfaktors informiert werden zu können.
 *
 */
public interface IZoomListener {
	
	/**
	 * Wird bei einer Änderung des Zoomfaktors aufgerufen.
	 * @param neuerZoomFaktor der neue Zoomfaktor
	 */
	void zoomFactorChanged(double newFactor);
}
