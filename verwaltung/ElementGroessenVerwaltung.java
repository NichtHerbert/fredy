package verwaltung;

import java.util.ArrayList;

import horcherschnittstellen.IElementGroessenHorcher;
import wfnmodell.elemente.EWFNElement;

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
	private ArrayList<IElementGroessenHorcher> eGHorcherListe;
	
	public ElementGroessenVerwaltung() {
		elementGroesse = EWFNElement.URGROESSE;
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
	void addElementGroessenHorcher(IElementGroessenHorcher horcher) {
		eGHorcherListe.add(horcher);
	}
	
	/**Entfernt den übergebenen Horcher von der {@link #eGHorcherListe}.
	 * @param horcher wird von der {@link #eGHorcherListe} entfernt
	 */
	void removeZoomFaktorHorcher(IElementGroessenHorcher horcher) {
		if (eGHorcherListe.contains(horcher)) 
			eGHorcherListe.remove(horcher);
	}
	
	/**
	 * Informiert alle Horcher der {@link #eGHorcherListe} über eine Zoomfaktor-Änderung.
	 */
	private void fireElementGroesseGeaendert() {
		for (IElementGroessenHorcher horcher : eGHorcherListe) 
			horcher.elementGroesseGeaendert(elementGroesse);
	}

}
