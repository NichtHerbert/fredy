package control;

import javax.swing.JComponent;

/**
 * Schnittstelle der möglichen Datei-Operationen.
 *
 */
public interface IFileManagement {
	
	/**
	 * Leert das vorhandene Datenmodell.
	 */
	void clear();

	/**
	 * Lässt eine *.pnml-Datei auswählen und öffnet sie.
	 * @param ausloeser notwendig um den Öffnen-Dialog zu verankern
	 */
	void open(JComponent trigger);

	/**
	 * Speichert das aktuelle Datenmodell unter dem aktuellen Dateinamen.
	 * Falls es diesen nicht gibt, wird {@link #saveAs(JComponent)}
	 * aufgerufen.
	 * @param ausloeser notwendig um den potentiellen Speichern-Dialog zu verankern
	 * @return true, wenn gespeichert wurde und der Dateiname ausgewählt wurde
	 */
	boolean save(JComponent trigger);

	/**
	 * Speichert das aktuelle Datenmodell unter einem vom Benutzer bestimmten Namen und Ort.
	 * @param ausloeser notwendig um den SpeichernUnterDialog zu verankern.
	 * @return true, wenn gespeichert wurde und der Dateiname ausgewählt wurde
	 */
	boolean saveAs(JComponent trigger);
	
	/**
	 * Gibt den Namen der zuletzt geöffneten oder gespeicherten Datei zurück
	 * @return Name der zuletzt geöffneten oder gespeicherten Datei oder null, falls es solch eine nicht gibt
	 */
	String getFileName();

}
