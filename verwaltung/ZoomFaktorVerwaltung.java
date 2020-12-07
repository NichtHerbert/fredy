package verwaltung;

import java.awt.Point;
import java.util.ArrayList;

import horcherschnittstellen.IZoomFaktorVeraenderungsHorcher;
import wfnmodell.elemente.EWFNElement;

/**
 * Klasse zur Verwaltung des Zoomfaktors.
 *
 */
public class ZoomFaktorVerwaltung {
	
	/**
	 * der aktuelle Zoomfaktor.
	 */
	private double zoomFaktor;
	/**
	 * Liste der Horcher, die über eine Veränderung von {@link #zoomFaktor} informiert werden möchten.
	 */
	private ArrayList<IZoomFaktorVeraenderungsHorcher> zoomFaktorHorcherListe;
	/**
	 * Maximaler Wert zum Hineinzoomen. Orientiert sich an der Basiseinheit URGROESSE.
	 */
	private final double MAXZOOM = EWFNElement.URGROESSE / 50d;
	
	ZoomFaktorVerwaltung() {
		zoomFaktor = 1d;
		zoomFaktorHorcherListe = new ArrayList<>(3);
	}
	
	/**
	 * Rechnet den Zoomfaktor aus dem übergebenen Point heraus.
	 * @param point Point aus dem der Zoomfaktor herausgerechnet werden soll
	 * @return ein neuer Point errechnet aus dem übergebenen Point ohne Zoomfaktor
	 */
	Point ohne(Point point) {
		return new Point(
				(int) (((point.x) / zoomFaktor) + 0.5),
				(int) (((point.y) / zoomFaktor) + 0.5));
	}
	
	/**
	 * Erhöht den Zoomfaktor.
	 */
	public void zoomIn() {
		zoomFaktor *= 1.1;
		fireZoomFaktorGeaendert();
	}
	
	/**
	 * Reduziert den Zoomfaktor, solange dieser größer ist als {@link #MAXZOOM}.
	 */
	public void zoomOut() {
		if ((zoomFaktor * 0.9) > MAXZOOM) zoomFaktor *= 0.9;
		else zoomFaktor = MAXZOOM;
		fireZoomFaktorGeaendert();
	}
	
	/**
	 * Fügt der {@link #zoomFaktorHorcherListe} den übergebenen Horcher hinzu.
	 * @param horcher wird der {@link #zoomFaktorHorcherListe} hinzugefügt
	 */
	void addZoomFaktorHorcher(IZoomFaktorVeraenderungsHorcher horcher) {
		zoomFaktorHorcherListe.add(horcher);
	}
	
	/**Entfernt den übergebenen Horcher von der {@link #zoomFaktorHorcherListe}.
	 * @param horcher wird von der {@link #zoomFaktorHorcherListe} entfernt
	 */
	void removeZoomFaktorHorcher(IZoomFaktorVeraenderungsHorcher horcher) {
		if (zoomFaktorHorcherListe.contains(horcher)) 
			zoomFaktorHorcherListe.remove(horcher);
	}
	
	/**
	 * Informiert alle Horcher der {@link #zoomFaktorHorcherListe} über eine Zoomfaktor-Änderung.
	 */
	private void fireZoomFaktorGeaendert() {
		for (IZoomFaktorVeraenderungsHorcher horcher : zoomFaktorHorcherListe) 
			horcher.zoomFaktorGeaendert(zoomFaktor);
	}
	
}
