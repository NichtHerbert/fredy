package gui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import wfnmodel.elements.EWfnElement;

/**
 * Zur Unterscheidung 4 möglicher Modi, deren momentan ausgewählter Modus im Falle einer Mausaktion
 * im {@link EditorPanel} zur Bestimmung, was denn getan werden soll, behilflich sein kann.
 * Desweiteren wird für jeden Modus ein eigener Cursor zur Verfügung gestellt. 
 */
public enum EWfnEditModus {
	SELECT(EIcons.CURSOR.getIcon()),
	ADD_PLACE(EIcons.CURSOR_ADD_PLACE.getIcon()),
	ADD_TRANSITION(EIcons.CURSOR_ADD_TRANSITION.getIcon()),
	ADD_ARC(EIcons.CROSSHAIR.getIcon())
	;
	
	/**
	 * Der Cursor des jeweiligen Modus.
	 */
	private Cursor cursor;
	
	/**
	 * Baut aus dem übergebenen ImageIcon für jeden Modus einen eigenen Cursor.
	 * Der Modus SELECT behält den DEFAULT_CURSER. 
	 * @param icon dient als Cursor-Vorlage für den jeweiligen Modus.
	 */
	EWfnEditModus(ImageIcon icon) {
		if (icon == EIcons.CURSOR.getIcon())
			cursor = new Cursor(Cursor.DEFAULT_CURSOR);
		else
			cursor = Toolkit.getDefaultToolkit().createCustomCursor(
				icon.getImage(), new Point(EWfnElement.BASEFACTOR, EWfnElement.BASEFACTOR), toString());
	}
	
	/**
	 * Gibt den Cursor des jeweiligen Modus zurück.
	 * @return das Attribut {@link #cursor} des jeweiligen Modus
	 */
	public Cursor getCursor() {
		return cursor;
	}
}
