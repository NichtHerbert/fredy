package verwaltung;

import javax.swing.JComponent;

/**
 * Schnittstelle der möglichen Datei-Operationen.
 *
 */
public interface IDateiVerwaltung {
	
	/**
	 * Leert das vorhandene Datenmodell.
	 */
	void dateiNeu();

	/**
	 * Lässt eine *.pnml-Datei auswählen und öffnet sie.
	 * @param ausloeser notwendig um den Öffnen-Dialog zu verankern
	 */
	void dateiOeffnen(JComponent ausloeser);

	/**
	 * Speichert das aktuelle Datenmodell unter dem aktuellen Dateinamen.
	 * Falls es diesen nicht gibt, wird {@link #dateiSpeichernUnter(JComponent)}
	 * aufgerufen.
	 * @param ausloeser notwendig um den potentiellen Speichern-Dialog zu verankern
	 * @return true, wenn gespeichert wurde und der Dateiname ausgewählt wurde
	 */
	boolean dateiSpeichern(JComponent ausloeser);

	/**
	 * Speichert das aktuelle Datenmodell unter einem vom Benutzer bestimmten Namen und Ort.
	 * @param ausloeser notwendig um den SpeichernUnterDialog zu verankern.
	 * @return true, wenn gespeichert wurde und der Dateiname ausgewählt wurde
	 */
	boolean dateiSpeichernUnter(JComponent ausloeser);
	
	/**
	 * Gibt den Namen der zuletzt geöffneten oder gespeicherten Datei zurück
	 * @return Name der zuletzt geöffneten oder gespeicherten Datei oder null, falls es solch eine nicht gibt
	 */
	String getDateiName();

}
