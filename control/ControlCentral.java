package control;

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
public class ControlCentral implements 	ICentralConstants,
												ISelectionEditingListener,
												IEditModusListener {
	/**Das Datenmodell des WFN*/										
	private WfnModel wfnModel;
	/**Die Darstellung des WFN */
	private JPanelEditor jpEditor;
	/**Die Werkzeuge zum Bearbeiten des WFN*/
	private JtbWerkzeugleiste jtbToolbar;
	
	/** Die {@link ZoomManagement}*/
	private ZoomManagement zoom;
	/** Die {@link CoordinateManagement}*/
	private CoordinateManagement coordinateManagement;
	/** Die {@link SelectionManagement}*/
	private SelectionManagement<IWfnElement> selectionManagement;
	/** Die {@link MarkingManagement}*/
	private MarkingManagement markingManagement;
	/** Die {@link EditorMouseActions}*/
	private EditorMouseActions mouseActionsManagement;
	/** Die {@link FileManagement}*/
	private FileManagement fileManagement;
	/** Die {@link ElementSizeManagement} */
	private ElementSizeManagement elementSizeManagement;
	/** Die {@link CircleTest}*/
	private CircleTest circleTest;

	
	/**
	 * Im Konstruktor werden alle Verwaltungen gestartet, und die notwendigen Verknüpfungen vorgenommen,
	 * bzw. Horcher angemeldet.
	 * 
	 * @param wfnModel das Modell
	 * @param jpEditor die Darstellung
	 * @param jtbToolbar die Werkzeugleiste
	 */
	public ControlCentral(WfnModel wfnModel, JPanelEditor jpEditor, JtbWerkzeugleiste jtbToolbar) {
		this.wfnModel = wfnModel;
		this.jpEditor = jpEditor;
		this.jtbToolbar = jtbToolbar;
		
		zoom = new ZoomManagement();
		this.jtbToolbar.setZoomFaktorVerwaltung(zoom);
		zoom.addZoomListener(jpEditor);
		
		coordinateManagement = new CoordinateManagement(zoom);
		this.wfnModel.addChangingListener(coordinateManagement);
		
		selectionManagement = new SelectionManagement<>();
		selectionManagement.addSelectionChangingListener(jtbToolbar);
		selectionManagement.addSelectionChangingListener(jpEditor);
		
		fileManagement = new FileManagement(wfnModel, wfnModel, selectionManagement);
		jtbToolbar.setDateiVerwaltung(fileManagement);
		
		markingManagement = new MarkingManagement();
		wfnModel.addChangingListener(markingManagement);
		jtbToolbar.addTransitionsSchaltungsHorcher(markingManagement);
		markingManagement.addWfnStatusListener(jpEditor);
		markingManagement.addWfnStatusListener(jtbToolbar);
		
		mouseActionsManagement = new EditorMouseActions(wfnModel, 
											coordinateManagement, 
											selectionManagement, 
											markingManagement, 
											zoom);
		mouseActionsManagement.addRedrawListener(jpEditor);
		this.jpEditor.addMouseListener(mouseActionsManagement);
		this.jpEditor.addMouseMotionListener(mouseActionsManagement);
		this.jtbToolbar.addEditorModusHorcher(mouseActionsManagement);
		
		elementSizeManagement = new ElementSizeManagement();
		jtbToolbar.setElementGroessenVerwaltung(elementSizeManagement);
		elementSizeManagement.addElementSizeListener(jpEditor);
		elementSizeManagement.addElementSizeListener(coordinateManagement);
		
		circleTest = new CircleTest(selectionManagement);
		jtbToolbar.setKreisTestVerwaltung(circleTest);
		wfnModel.addChangingListener(circleTest);
		
		jtbToolbar.addAuswahlBearbeitetHorcher(this);
		jtbToolbar.addEditorModusHorcher(this);
		
		wfnModel.clear();
				
	}

	@Override
	public void editModusChanged(EWfnEditModus editorModus) {
		jpEditor.setCursor(editorModus.getCursor());
	}
	
	@Override
	public void elementsToDelete(ArrayList<? extends IWfnElement> selectedElements) {
		wfnModel.delete(selectedElements);
		selectionManagement.clearAndFire(NEW_SELECTION);
	}
	
	@Override
	public void elementToSetName(IWfnTransitionAndPlace element, String name) {
		wfnModel.setElementName(element, name);
	}

	/**
	 * Methode zum Beenden des gesamten Programms.
	 */
	public void exitProgram() {
		if (!wfnModel.isCurrentWfnSaved()) {
			Object[] buttons = { "Cancel",
								"Without Saving",
								"Save"};
			int n = JOptionPane.showOptionDialog(jpEditor, 
					"The file has not yet been saved! Do you really want to exit the program without saving?", 
					"Exit Program", 
					JOptionPane.YES_NO_CANCEL_OPTION, 
					JOptionPane.WARNING_MESSAGE, 
					null, 
					buttons, 
					buttons[2]);
			if (n == 1) System.exit(0);
			if (n == 2) {
				fileManagement.fileSaved(jtbToolbar);
				System.exit(0);
			}
		} else 
			System.exit(0);
		
	}
	
	
}
