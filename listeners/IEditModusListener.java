package listeners;

import gui.EWfnEditModus;

/**
 * Schnittstelle, um über Änderungen des Editormodus informiert zu werden.
 *
 */
public interface IEditModusListener {
	
	/**
	 * Wird bei Änderung des Editormodus aufgerufen.
	 * @param newModus der neue Editormodus
	 */
	void editModusChanged(EWfnEditModus newModus);
}
