package listeners;

import java.awt.Point;

/**
 * Schnittstelle, um über benötigte Zeichnungen informiert werden zu können.
 *
 */
public interface IRedrawListener {
	final int LINE = 10;
	final int RECTANGLE = 11;
	final int NOTHING = 12;
	
	/**
	 * Wird aufgerufen, wenn eine Zeichnung benötigt wird.
	 * @param form LINE, RECTANGLE oder NOTHING
	 * @param startMousePosition der eine Eckpunkt/Startpunkt der Zeichnung
	 * @param nowMousePosition der ander Eckpunkt/Startpunkt der Zeichnung
	 */
	void redraw(int form, Point startMousePosition, Point nowMousePosition);

}
