package horcherschnittstellen;

/**
 * Schnittstelle, um über eine Veränderung der Elementgröße informiert zu werden.
 *
 */
public interface IElementGroessenHorcher {
	
	
	/**
	 * Wird bei einer Änderung der Elementgröße aufgerufen.
	 * @param neueGroesse die neue Elementgröße
	 */
	void elementGroesseGeaendert(int neueGroesse);
}
