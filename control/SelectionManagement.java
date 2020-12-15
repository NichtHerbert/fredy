package control;

import java.util.ArrayList;

import listeners.ISelectionChangingListener;
import wfnmodel.interfaces.IWfnElement;

/**
 * Klasse zur Verwaltung der ausgewählten Elemente. Sie ist eine Liste, erweitert um Methoden, 
 * Änderungen an der Auswahlliste an interessierte Horcher weiterzuleiten.
 *
 * @param <E> alle Klassen, die die Schnittstelle {@link wfnmodel.interfaces.IWfnElement} implementiert haben.
 */
class SelectionManagement<E extends IWfnElement> extends ArrayList<E> {

	private static final long serialVersionUID = -6040342384882651111L;
	
	/** Liste der Horcher, die über eine Auswahländerung informiert werden möchten.*/
	private ArrayList<ISelectionChangingListener> selectionChangingListeners;
	
	SelectionManagement() {
		super();
		selectionChangingListeners = new ArrayList<>(3);
	}
	
	/**
	 * Fügt den übergebenen Horcher der {@link #selectionChangingListeners} hinzu.
	 * @param listener wird {@link #selectionChangingListeners} hinzugefügt
	 */
	void addSelectionChangingListener(ISelectionChangingListener listener) {
		selectionChangingListeners.add(listener);
	}

	/**
	 * Entfernt den übergebenen Horcher von der {@link #selectionChangingListeners}.
	 * @param listener wird von {@link #selectionChangingListeners} entfernt
	 */
	void removeSelectionChangingListener(ISelectionChangingListener listener) {
		if (selectionChangingListeners.contains(listener)) 
			selectionChangingListeners.remove(listener);
	}
	
	/**
	 * Informiert alle Horcher der Liste {@link #selectionChangingListeners} 
	 * über eine Auswahl-Änderung.
	 * @param selectionType NEW_SELECTION oder ARC_SELECTION
	 */
	void fireSelectionChangeOccurred(int selectionType) {
		for (ISelectionChangingListener listener : selectionChangingListeners)
			listener.selectionChangeOccurred(selectionType, this);	
	}
	
	/**
	 * Leert die Liste und informiert alle Horcher.
	 * @param selectionType NEW_SELECTION oder ARC_SELECTION
	 */
	void clearAndFire(int selectionType) {
		clear();
		fireSelectionChangeOccurred(selectionType);
	}
	
	/**
	 * Fügt der Auswahlliste ein Element hinzu und informiert alle Horcher.
	 * @param element das hinzuzufügende Element
	 * @param selectionType NEW_SELECTION oder ARC_SELECTION
	 */
	void addAndFire(E element, int selectionType) {
		if (element != null) 
			add(element);
		fireSelectionChangeOccurred(selectionType);
	}
	
	/**
	 * Leert die Auswahlliste ,
	 * fügt das übergebene Element in die leere Liste ein,
	 * und informiert alle Horcher.
	 * @param element das hinzuzufügende Element
	 * @param selectionType NEW_SELECTION oder ARC_SELECTION
	 */
	void clearAndAddAndFire(E element, int selectionType) {
		clear();
		addAndFire(element, selectionType);
	}
	
	/**
	 * Leert die Auswahlliste,
	 * füllt sie mit den übergebenen Elementen,
	 * und informiert alle Horcher.
	 * @param elements Liste der hinzuzufügenden Elemente
	 * @param selectionType NEW_SELECTION oder ARC_SELECTION
	 */
	void clearAndAddALLAndFire(ArrayList<E> elements, int selectionType) {
		clear();
		if (elements != null) 
			addAll(elements);
		fireSelectionChangeOccurred(selectionType);
	}
}
