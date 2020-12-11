package verwaltung;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import gui.EWfnEditModus;
import gui.ICentralConstants;
import gui.JPanelEditor;
import gui.toolbar.JtbWerkzeugleiste;
import listeners.ISelectionEditingListener;
import listeners.IEditModusListener;
import wfnmodel.WfnModel;
import wfnmodel.interfaces.IWfnElement;
import wfnmodel.interfaces.IWfnTransitionAndPlace;

/**
 * Die Zentrale Klasse des Workflownetzeditors, in der alle Teile miteinander verschaltet werden.
 *
 */
public class ZentraleVerschraenkung implements 	ICentralConstants,
												ISelectionEditingListener,
												IEditModusListener {
	/**Das Datenmodell des WFN*/										
	private WfnModel wfnModell;
	/**Die Darstellung des WFN */
	private JPanelEditor jpEditor;
	/**Die Werkzeuge zum Bearbeiten des WFN*/
	private JtbWerkzeugleiste jtbVerwaltung;
	
	/** Die {@link ZoomManagement}*/
	private ZoomManagement zoom;
	/** Die {@link PositionsVerwaltung}*/
	private PositionsVerwaltung koordinatenVerwaltung;
	/** Die {@link AuswahlVerwaltung}*/
	private AuswahlVerwaltung<IWfnElement> auswahlVerwaltung;
	/** Die {@link MarkierungsVerwaltung}*/
	private MarkierungsVerwaltung markierungsVerwaltung;
	/** Die {@link MausVerwaltung}*/
	private MausVerwaltung mausVerwaltung;
	/** Die {@link FileManagement}*/
	private FileManagement dateiVerwaltung;
	/** Die {@link ElementGroessenVerwaltung} */
	private ElementGroessenVerwaltung eGVerwaltung;
	/** Die {@link KreisTestVerwaltung}*/
	private KreisTestVerwaltung kTVerwaltung;

	
	/**
	 * Im Konstruktor werden alle Verwaltungen gestartet, und die notwendigen Verknüpfungen vorgenommen,
	 * bzw. Horcher angemeldet.
	 * 
	 * @param wfnModell das Modell
	 * @param jpEditor die Darstellung
	 * @param jtbVerwaltung die Werkzeugleiste
	 */
	public ZentraleVerschraenkung(WfnModel wfnModell, JPanelEditor jpEditor, JtbWerkzeugleiste jtbVerwaltung) {
		this.wfnModell = wfnModell;
		this.jpEditor = jpEditor;
		this.jtbVerwaltung = jtbVerwaltung;
		
		zoom = new ZoomManagement();
		this.jtbVerwaltung.setZoomFaktorVerwaltung(zoom);
		zoom.addZoomListener(jpEditor);
		
		koordinatenVerwaltung = new PositionsVerwaltung(zoom);
		this.wfnModell.addChangingListener(koordinatenVerwaltung);
		
		auswahlVerwaltung = new AuswahlVerwaltung<>();
		auswahlVerwaltung.addAuswahlAenderungsHorcher(jtbVerwaltung);
		auswahlVerwaltung.addAuswahlAenderungsHorcher(jpEditor);
		
		dateiVerwaltung = new FileManagement(wfnModell, wfnModell, auswahlVerwaltung);
		jtbVerwaltung.setDateiVerwaltung(dateiVerwaltung);
		
		markierungsVerwaltung = new MarkierungsVerwaltung();
		wfnModell.addChangingListener(markierungsVerwaltung);
		jtbVerwaltung.addTransitionsSchaltungsHorcher(markierungsVerwaltung);
		markierungsVerwaltung.addModellStatusHorcher(jpEditor);
		markierungsVerwaltung.addModellStatusHorcher(jtbVerwaltung);
		
		mausVerwaltung = new MausVerwaltung(wfnModell, 
											koordinatenVerwaltung, 
											auswahlVerwaltung, 
											markierungsVerwaltung, 
											zoom);
		mausVerwaltung.addZeichnungBenoetigtHorcher(jpEditor);
		this.jpEditor.addMouseListener(mausVerwaltung);
		this.jpEditor.addMouseMotionListener(mausVerwaltung);
		this.jtbVerwaltung.addEditorModusHorcher(mausVerwaltung);
		
		eGVerwaltung = new ElementGroessenVerwaltung();
		jtbVerwaltung.setElementGroessenVerwaltung(eGVerwaltung);
		eGVerwaltung.addElementGroessenHorcher(jpEditor);
		eGVerwaltung.addElementGroessenHorcher(koordinatenVerwaltung);
		
		kTVerwaltung = new KreisTestVerwaltung(auswahlVerwaltung);
		jtbVerwaltung.setKreisTestVerwaltung(kTVerwaltung);
		wfnModell.addChangingListener(kTVerwaltung);
		
		jtbVerwaltung.addAuswahlBearbeitetHorcher(this);
		jtbVerwaltung.addEditorModusHorcher(this);
		
		wfnModell.clear();
				
	}

	@Override
	public void editModusChanged(EWfnEditModus editorModus) {
		jpEditor.setCursor(editorModus.getCursor());
	}
	
	@Override
	public void elementsToDelete(ArrayList<? extends IWfnElement> ausgewaehlteElemente) {
		wfnModell.delete(ausgewaehlteElemente);
		auswahlVerwaltung.clearAndFire(NEW_SELECTION);
	}
	
	@Override
	public void elementToSetName(IWfnTransitionAndPlace element, String name) {
		wfnModell.setElementName(element, name);
	}

	/**
	 * Methode zum Beenden des gesamten Programms.
	 */
	public void programmBeenden() {
		if (!wfnModell.isCurrentWfnSaved()) {
			Object[] buttons = { "Abbrechen",
								"Ohne Speichern",
								"Mit Speichern"};
			int n = JOptionPane.showOptionDialog(jpEditor, 
					"Die Datei wurde noch nicht gespeichert! Wollen Sie das Programm tatsächlich ohnen Speichern verlassen?", 
					"Programm beenden", 
					JOptionPane.YES_NO_CANCEL_OPTION, 
					JOptionPane.WARNING_MESSAGE, 
					null, 
					buttons, 
					buttons[2]);
			if (n == 1) System.exit(0);
			if (n == 2) {
				dateiVerwaltung.fileSaved(jtbVerwaltung);
				System.exit(0);
			}
		} else 
			System.exit(0);
		
	}
	
	
}
