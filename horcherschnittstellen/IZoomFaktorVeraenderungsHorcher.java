package horcherschnittstellen;

/**
 * Schnittstelle, um bei einer Veränderung des Zoomfaktors informiert werden zu können.
 *
 */
public interface IZoomFaktorVeraenderungsHorcher {
	
	/**
	 * Wird bei einer Änderung des Zoomfaktors aufgerufen.
	 * @param neuerZoomFaktor der neue Zoomfaktor
	 */
	void zoomFaktorGeaendert(double neuerZoomFaktor);
}
