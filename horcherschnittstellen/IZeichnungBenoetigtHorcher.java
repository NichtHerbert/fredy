package horcherschnittstellen;

import java.awt.Point;

/**
 * Schnittstelle, um über benötigte Zeichnungen informiert werden zu können.
 *
 */
public interface IZeichnungBenoetigtHorcher {
	final int LINIE = 10;
	final int RECHTECK = 11;
	final int NICHTS = 12;
	
	/**
	 * Wird aufgerufen, wenn eine Zeichnung benötigt wird.
	 * @param form LINIE, RECHTECK oder NICHTS
	 * @param startMausPosition der eine Eckpunkt/Startpunkt der Zeichnung
	 * @param jetztMausPosition der ander Eckpunkt/Startpunkt der Zeichnung
	 */
	void zeichnungBenoetigt(int form, Point startMausPosition, Point jetztMausPosition);

}
