package gui.toolbar;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JToolBar;

import listeners.ISelectionEditingListener;
import listeners.ISelectionChangingListener;
import listeners.IEditModusListener;
import listeners.ITransitionFireListener;
import listeners.IWfnStatusListener;
import verwaltung.FileManagement;
import verwaltung.ElementSizeManagement;
import verwaltung.CircleTest;
import verwaltung.ZoomManagement;
import wfnmodel.WfnStatusInfo;
import wfnmodel.interfaces.IWfnElement;

/**
 * Die Toolbar des WFN-Editors. Sie startet alle Steuerungs- und Informationspanels und leitet 
 * notwendige Nachrichten an das jeweilige Panel weiter. 
 */
public class JtbWerkzeugleiste extends JToolBar implements 	ISelectionChangingListener,
															IWfnStatusListener {

	private static final long serialVersionUID = 145715004147453701L;
	
	/**
	 * Panel zur Anzeige des Dateinamens und zur Steuerung von Öffnen, Speichern und Leeren
	 * des Datenmodels.
	 */
	private JPanelDateiOperationen jpDateiOperationen;
	
	/**
	 * Panel zur Anzeige des gegenwärtigen Zustands des Workflownetzes.
	 */
	private JPanelNetzInfo jpNetzInfo;

	/**
	 * Panel zum Setzen des #{@link gui.EWfnEditModus} und zum Löschen und Namen-Ändern 
	 * von einzelnen bzw. ausgewählten Elementen.
	 */
	private JPanelElementBearbeitung jpElementBearbeitung;
	
	/**
	 * Panel zuständig zur Anzeige der Steuerung des Schaltens von Transitionen und des Zurücksetzens
	 * aller Marken auf die Ausgangsposition. 
	 */
	private JPanelSchalten jpSchalten;
	
	/**
	 * Panel zur Steuerung des Zooms.
	 */
	private JPanelZoom jpZoom;
	
	/**
	 * Panel zur Steuerung der Elementgröße.
	 */
	private JPanelElementGroesse jpEG;

	
	/**
	 * Initialisiert die Toolbar mit allen Panels im Fenster rechtsseitig und vertikal übereinander.
	 */
	public JtbWerkzeugleiste() {
		
		setOrientation(VERTICAL);
		
		jpDateiOperationen = new JPanelDateiOperationen();
		jpNetzInfo = new JPanelNetzInfo();
		jpElementBearbeitung = new JPanelElementBearbeitung();
		jpSchalten = new JPanelSchalten();
		jpZoom = new JPanelZoom();
		jpEG = new JPanelElementGroesse();
		
		add(jpDateiOperationen);
		addSeparator();
		add(jpNetzInfo);
		addSeparator();
		add(jpElementBearbeitung);

		jpNetzInfo.revalidate();
		addSeparator();
		add(jpSchalten);
		addSeparator();
		add(jpEG);
		addSeparator();
		add(jpZoom);
	}
	
	@Override
	public void selectionChangeOccurred(int auswahlArt, ArrayList<? extends IWfnElement> ausgewaehlteElemente) {
		jpElementBearbeitung.selectionChangeOccurred(auswahlArt, ausgewaehlteElemente);
	}

	/**
	 * Weiterleitung an gleichnamige Methode von {@link #jpElementBearbeitung}.
	 * @param horcher wird an {@link #jpElementBearbeitung} weitergeleitet
	 */
	public void addAuswahlBearbeitetHorcher(ISelectionEditingListener horcher) {
		jpElementBearbeitung.addAuswahlBearbeitetHorcher(horcher);
	}

	/**
	 * Weiterleitung an gleichnamige Methode von {@link #jpElementBearbeitung}.
	 * @param horcher wird an {@link #jpElementBearbeitung} weitergeleitet
	 */
	public void addEditorModusHorcher(IEditModusListener horcher) {
		jpElementBearbeitung.addEditorModusHorcher(horcher);
	}
	
	/**
	 * Weiterleitung an gleichnamige Methode von {@link #jpSchalten}.
	 * @param horcher wird an {@link #jpSchalten} weitergeleitet
	 */
	public void addTransitionsSchaltungsHorcher(ITransitionFireListener horcher) {
		jpSchalten.addTransitionsSchaltungsHorcher(horcher);
	}

	
	@Override
	public void newWfnStatus(WfnStatusInfo statusInfo) {
		jpDateiOperationen.newWfnStatus(statusInfo);
		jpNetzInfo.newWfnStatus(statusInfo);
		jpSchalten.newWfnStatus(statusInfo);
		if (( !statusInfo.isWfn()) 
				|| (statusInfo.getStartPlace() == statusInfo.getEndPlace())) 
			setKomponenteMitKindernEnabled(jpSchalten, false);
		else
			setKomponenteMitKindernEnabled(jpSchalten, true);
	}
	
	/**
	 * Weiterleitung an gleichnamige Methode von {@link #jpZoom}.
	 * @param zoomVerwaltung wird an {@link #jpZoom} weitergeleitet
	 */
	public void setZoomFaktorVerwaltung(ZoomManagement zoomVerwaltung) {
		jpZoom.setZoomFaktorVerwaltung(zoomVerwaltung);
	}

	/**
	 * Weiterleitung an gleichnamige Methode von {@link #jpDateiOperationen}.
	 * @param dateiVerwaltung wird an {@link #jpDateiOperationen} weitergeleitet
	 */
	public void setDateiVerwaltung(FileManagement dateiVerwaltung) {
		jpDateiOperationen.setDateiVerwaltung(dateiVerwaltung);
	}
	
	/**
	 * Weiterleitung an gleichnamige Methode von {@link #jpEG}.
	 * @param eGVerwaltung wird an {@link #jpEG} weitergeleitet
	 */
	public void setElementGroessenVerwaltung(ElementSizeManagement eGVerwaltung) {
		jpEG.setElementGroessenVerwaltung(eGVerwaltung);
	}
	
	/**
	 * Weiterleitung an gleichnamige Methode von {@link #jpNetzInfo}.
	 * @param eGVerwaltung wird an {@link #jpNetzInfo} weitergeleitet
	 */
	public void setKreisTestVerwaltung(CircleTest kTVerwaltung) {
		jpNetzInfo.setKreisTestVerwaltung(kTVerwaltung);
	}
	
	
	/**
	 * Setzt {@link #setEnabled(boolean)} eines Panels und von allen 
	 * auf dem Panel sitzenden Komponenten.
	 * Anmerkung: Dies gilt nur für die erste Ebene. Haben die "Kind"-Komponenten wiederum 
	 * "Kind"-Elemente so werden diese nicht beachtet.
	 * @param komponente Panel, dessen {@link #setEnabled(boolean)} gesetzt werden soll
	 * @param b boolscher Wert auf den das Panel gesetzt werden soll.
	 */
	static void setKomponenteMitKindernEnabled(JComponent komponente, boolean b) {
		for (Component kind : komponente.getComponents())
			kind.setEnabled(b);
		komponente.setEnabled(b);
	}
}
