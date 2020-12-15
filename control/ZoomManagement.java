package control;

import java.awt.Point;
import java.util.ArrayList;

import listeners.IZoomListener;
import wfnmodel.elements.EWfnElement;

/**
 * Klasse zur Verwaltung des Zoomfaktors.
 *
 */
public class ZoomManagement {
	
	/** der aktuelle Zoomfaktor. */
	private double zoomFactor;
	/** Liste der Horcher, die über eine Veränderung von {@link #zoomFactor} informiert werden möchten.*/
	private ArrayList<IZoomListener> zoomListeners;
	/** Maximaler Wert zum Hineinzoomen. Orientiert sich an der Basiseinheit BASEFACTOR.*/
	private final double MAXZOOM = EWfnElement.BASEFACTOR / 50d;
	
	ZoomManagement() {
		zoomFactor = 1d;
		zoomListeners = new ArrayList<>(3);
	}
	
	/**
	 * Rechnet den Zoomfaktor aus dem übergebenen Point heraus.
	 * @param point Point aus dem der Zoomfaktor herausgerechnet werden soll
	 * @return ein neuer Point errechnet aus dem übergebenen Point ohne Zoomfaktor
	 */
	Point calculateOut(Point point) {
		return new Point(
				(int) (((point.x) / zoomFactor) + 0.5),
				(int) (((point.y) / zoomFactor) + 0.5));
	}
	
	/**
	 * Erhöht den Zoomfaktor.
	 */
	public void zoomIn() {
		zoomFactor *= 1.1;
		fireZoomFactorChanged();
	}
	
	/**
	 * Reduziert den Zoomfaktor, solange dieser größer ist als {@link #MAXZOOM}.
	 */
	public void zoomOut() {
		if ((zoomFactor * 0.9) > MAXZOOM) zoomFactor *= 0.9;
		else zoomFactor = MAXZOOM;
		fireZoomFactorChanged();
	}
	
	/**
	 * Fügt der {@link #zoomListeners} den übergebenen Horcher hinzu.
	 * @param listener wird der {@link #zoomListeners} hinzugefügt
	 */
	void addZoomListener(IZoomListener listener) {
		zoomListeners.add(listener);
	}
	
	/**Entfernt den übergebenen Horcher von der {@link #zoomListeners}.
	 * @param listener wird von der {@link #zoomListeners} entfernt
	 */
	void removeZoomListener(IZoomListener listener) {
		if (zoomListeners.contains(listener)) 
			zoomListeners.remove(listener);
	}
	
	/**
	 * Informiert alle Horcher der {@link #zoomListeners} über eine Zoomfaktor-Änderung.
	 */
	private void fireZoomFactorChanged() {
		for (IZoomListener listener : zoomListeners) 
			listener.zoomFactorChanged(zoomFactor);
	}
	
}
