package listeners;

/**
 * Schnittstelle, um über eine Veränderung der Elementgröße informiert zu werden.
 *
 */
public interface IElementSizeListener {
	
	
	/**
	 * Wird bei einer Änderung der Elementgröße aufgerufen.
	 * @param newSize die neue Elementgröße ???in welcher Maßeinheit?
	 */
	void elementSizeChanged(int newSize);
}
