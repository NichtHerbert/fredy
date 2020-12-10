package horcherschnittstellen;

import gui.EWfnEditModus;

/**
 * Schnittstelle, um über Änderungen des Editormodus informiert zu werden.
 *
 */
public interface IEditorModusHorcher {
	
	/**
	 * Wird bei Änderung des Editormodus aufgerufen.
	 * @param neuerModus der neue Editormodus
	 */
	void editorModusGeaendert(EWfnEditModus neuerModus);
}
