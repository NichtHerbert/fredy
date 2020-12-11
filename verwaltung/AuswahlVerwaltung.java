package verwaltung;

import java.util.ArrayList;

import listeners.ISelectionChangingListener;
import wfnmodel.interfaces.IWfnElement;

/**
 * Klasse zur Verwaltung der ausgewählten Elemente. Sie ist eine Liste, erweitert um Methoden, 
 * Änderungen an der Auswahlliste an interessierte Horcher weiterzuleiten.
 *
 * @param <E> alle Klassen, die die Schnittstelle {@link wfnmodel.interfaces.IWfnElement} implementiert haben.
 */
class AuswahlVerwaltung<E extends IWfnElement> extends ArrayList<E> {

	private static final long serialVersionUID = -6040342384882651111L;
	/**
	 * Liste der Horcher, die über eine Auswahländerung informiert werden möchten.
	 */
	private ArrayList<ISelectionChangingListener> auswahlVeraenderungsHorcherListe;
	
	AuswahlVerwaltung() {
		super();
		auswahlVeraenderungsHorcherListe = new ArrayList<>(3);
	}
	
	/**
	 * Fügt den übergebenen Horcher der {@link #auswahlVeraenderungsHorcherListe} hinzu.
	 * @param horcher wird {@link #auswahlVeraenderungsHorcherListe} hinzugefügt
	 */
	void addAuswahlAenderungsHorcher(ISelectionChangingListener horcher) {
		auswahlVeraenderungsHorcherListe.add(horcher);
	}

	/**
	 * Entfernt den übergebenen Horcher von der {@link #auswahlVeraenderungsHorcherListe}.
	 * @param horcher wird von {@link #auswahlVeraenderungsHorcherListe} entfernt
	 */
	void removeAuswahlAenderungsHorcher(ISelectionChangingListener horcher) {
		if (auswahlVeraenderungsHorcherListe.contains(horcher)) 
			auswahlVeraenderungsHorcherListe.add(horcher);
	}
	
	/**
	 * Informiert alle Horcher der Liste {@link #auswahlVeraenderungsHorcherListe} 
	 * über eine Auswahl-Änderung.
	 * @param auswahlArt NEW_SELECTION oder ARC_SELECTION
	 */
	void fireAuswahlAenderungEingetreten(int auswahlArt) {
		for (ISelectionChangingListener horcher : auswahlVeraenderungsHorcherListe)
			horcher.selectionChangeOccurred(auswahlArt, this);	
	}
	
	/**
	 * Leert die Liste und informiert alle Horcher.
	 * @param auswahlArt NEW_SELECTION oder ARC_SELECTION
	 */
	void clearAndFire(int auswahlArt) {
		clear();
		fireAuswahlAenderungEingetreten(auswahlArt);
	}
	
	/**
	 * Fügt der Auswahlliste ein Element hinzu und informiert alle Horcher.
	 * @param element das hinzuzufügende Element
	 * @param auswahlArt NEW_SELECTION oder ARC_SELECTION
	 */
	void addAndFire(E element, int auswahlArt) {
		if (element != null) 
			add(element);
		fireAuswahlAenderungEingetreten(auswahlArt);
	}
	
	/**
	 * Leert die Auswahlliste ,
	 * fügt das übergebene Element in die leere Liste ein,
	 * und informiert alle Horcher.
	 * @param element das hinzuzufügende Element
	 * @param auswahlArt NEW_SELECTION oder ARC_SELECTION
	 */
	void clearAndAddAndFire(E element, int auswahlArt) {
		clear();
		if (element != null) 
			add(element);
		fireAuswahlAenderungEingetreten(auswahlArt);
	}
	
	/**
	 * Leert die Auswahlliste,
	 * füllt sie mit den übergebenen Elementen,
	 * und informiert alle Horcher.
	 * @param elementListe Liste der hinzuzufügenden Elemente
	 * @param auswahlArt NEW_SELECTION oder ARC_SELECTION
	 */
	void clearAndAddALLAndFire(ArrayList<E> elementListe, int auswahlArt) {
		clear();
		if (elementListe != null) 
			addAll(elementListe);
		fireAuswahlAenderungEingetreten(auswahlArt);
	}
}
