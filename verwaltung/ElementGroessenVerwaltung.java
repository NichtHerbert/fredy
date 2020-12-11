package verwaltung;

import java.util.ArrayList;

import listeners.IElementSizeListener;
import wfnmodel.elements.EWfnElement;

/**
 * Klasse zur Verwaltung der Darstellungsgröße von Stellen, Transitionen und Pfeilspitzen.
 *
 */
public class ElementGroessenVerwaltung {
	
	/**
	 * Die aktuelle Elementgröße
	 */
	private int elementGroesse;
	/**
	 * Die Liste derjenigen Horcher, die über eine Veränderung der Elementgröße informiert werden wollen.
	 */
	private ArrayList<IElementSizeListener> eGHorcherListe;
	
	public ElementGroessenVerwaltung() {
		elementGroesse = EWfnElement.BASEFACTOR;
		eGHorcherListe = new ArrayList<>(2);
	}

	/**
	 * Vergrößert den Darstellungsfaktor der Elemente.
	 */
	public void elementGroesser() {
		elementGroesse += 4;
		fireElementGroesseGeaendert();
	}

	/**
	 * Verkleinert den Darstellungsfaktor der Elemente.
	 */
	public void elementKleiner() {
		if (elementGroesse > 4) {
			elementGroesse -=4;
			fireElementGroesseGeaendert();
		}
	}
	
	/**
	 * Fügt der {@link #eGHorcherListe} den übergebenen Horcher hinzu.
	 * @param horcher wird der {@link #eGHorcherListe} hinzugefügt
	 */
	void addElementGroessenHorcher(IElementSizeListener horcher) {
		eGHorcherListe.add(horcher);
	}
	
	/**Entfernt den übergebenen Horcher von der {@link #eGHorcherListe}.
	 * @param horcher wird von der {@link #eGHorcherListe} entfernt
	 */
	void removeZoomFaktorHorcher(IElementSizeListener horcher) {
		if (eGHorcherListe.contains(horcher)) 
			eGHorcherListe.remove(horcher);
	}
	
	/**
	 * Informiert alle Horcher der {@link #eGHorcherListe} über eine Zoomfaktor-Änderung.
	 */
	private void fireElementGroesseGeaendert() {
		for (IElementSizeListener horcher : eGHorcherListe) 
			horcher.elementSizeChanged(elementGroesse);
	}

}
