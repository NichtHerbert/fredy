package control;

import java.util.ArrayList;

import listeners.IElementSizeListener;
import wfnmodel.elements.EWfnElement;

/**
 * Klasse zur Verwaltung der Darstellungsgröße von Stellen, Transitionen und Pfeilspitzen.
 *
 */
public class ElementSizeManagement {
	
	/** Die aktuelle Elementgröße //??Maßeinheit?*/
	private int size;
	/** Die Liste derjenigen Horcher, die über eine Veränderung der Elementgröße informiert werden wollen.*/
	private ArrayList<IElementSizeListener> elementSizeListeners;
	
	public ElementSizeManagement() {
		size = EWfnElement.BASEFACTOR;
		elementSizeListeners = new ArrayList<>(2);
	}

	/**
	 * Vergrößert den Darstellungsfaktor der Elemente.
	 */
	public void bigger() {
		size += 4;
		fireElementSizeChanged();
	}

	/**
	 * Verkleinert den Darstellungsfaktor der Elemente.
	 */
	public void smaller() {
		if (size > 4) {
			size -=4;
			fireElementSizeChanged();
		}
	}
	
	/**
	 * Fügt der {@link #elementSizeListeners} den übergebenen Horcher hinzu.
	 * @param listener wird der {@link #elementSizeListeners} hinzugefügt
	 */
	void addElementSizeListener(IElementSizeListener listener) {
		elementSizeListeners.add(listener);
	}
	
	/**Entfernt den übergebenen Horcher von der {@link #elementSizeListeners}.
	 * @param listener wird von der {@link #elementSizeListeners} entfernt
	 */
	void removeElementSizeListener(IElementSizeListener listener) {
		if (elementSizeListeners.contains(listener)) 
			elementSizeListeners.remove(listener);
	}
	
	/**
	 * Informiert alle Horcher der {@link #elementSizeListeners} über eine Zoomfaktor-Änderung.
	 */
	private void fireElementSizeChanged() {
		for (IElementSizeListener listener : elementSizeListeners) 
			listener.elementSizeChanged(size);
	}

}
