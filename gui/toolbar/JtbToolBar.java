package gui.toolbar;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JToolBar;

import control.CircleTest;
import control.ElementSizeManagement;
import control.FileManagement;
import control.ZoomManagement;
import listeners.ISelectionEditingListener;
import listeners.ISelectionChangingListener;
import listeners.IEditModusListener;
import listeners.ITransitionFireListener;
import listeners.IWfnStatusListener;
import wfnmodel.WfnStatusInfo;
import wfnmodel.interfaces.IWfnElement;

/**
 * Die Toolbar des WFN-Editors. Sie startet alle Steuerungs- und Informationspanels und leitet 
 * notwendige Nachrichten an das jeweilige Panel weiter. 
 */
public class JtbToolBar extends JToolBar implements 	ISelectionChangingListener,
															IWfnStatusListener {

	private static final long serialVersionUID = 145715004147453701L;
	
	/** Panel zur Anzeige des Dateinamens und zur Steuerung von Öffnen, Speichern und Leeren
	 * des Datenmodels. */
	private JPanelFileOperations jpFileOperations;
	/** Panel zur Anzeige des gegenwärtigen Zustands des Workflownetzes. */
	private JPanelNetInfo jpNetInfo;
	/** Panel zum Setzen des #{@link gui.EWfnEditModus} und zum Löschen und Namen-Ändern 
	 * von einzelnen bzw. ausgewählten Elementen.*/
	private JPanelElementHandling jpElementHandling;
	/** Panel zuständig zur Anzeige der Steuerung des Schaltens von Transitionen und des Zurücksetzens
	 * aller Marken auf die Ausgangsposition.  */
	private JPanelFireTransition jpFireTransition;
	/** Panel zur Steuerung des Zooms. */
	private JPanelZoom jpZoom;
	/** Panel zur Steuerung der Elementgröße.*/
	private JPanelElementSize jpElementSize;

	
	/**
	 * Initialisiert die Toolbar mit allen Panels im Fenster rechtsseitig und vertikal übereinander.
	 */
	public JtbToolBar() {
		
		setOrientation(VERTICAL);
		
		jpFileOperations = new JPanelFileOperations();
		jpNetInfo = new JPanelNetInfo();
		jpElementHandling = new JPanelElementHandling();
		jpFireTransition = new JPanelFireTransition();
		jpZoom = new JPanelZoom();
		jpElementSize = new JPanelElementSize();
		
		add(jpFileOperations);
		addSeparator();
		add(jpNetInfo);
		addSeparator();
		add(jpElementHandling);

		jpNetInfo.revalidate();
		addSeparator();
		add(jpFireTransition);
		addSeparator();
		add(jpElementSize);
		addSeparator();
		add(jpZoom);
	}
	
	@Override
	public void selectionChangeOccurred(int selectionType, ArrayList<? extends IWfnElement> selectedElements) {
		jpElementHandling.selectionChangeOccurred(selectionType, selectedElements);
	}

	/**
	 * Weiterleitung an gleichnamige Methode von {@link #jpElementHandling}.
	 * @param listener wird an {@link #jpElementHandling} weitergeleitet
	 */
	public void addSelectionEditingListener(ISelectionEditingListener listener) {
		jpElementHandling.addSelectionEditingListener(listener);
	}

	/**
	 * Weiterleitung an gleichnamige Methode von {@link #jpElementHandling}.
	 * @param listener wird an {@link #jpElementHandling} weitergeleitet
	 */
	public void addEditorModusListener(IEditModusListener listener) {
		jpElementHandling.addEditorModusListener(listener);
	}
	
	/**
	 * Weiterleitung an gleichnamige Methode von {@link #jpFireTransition}.
	 * @param listener wird an {@link #jpFireTransition} weitergeleitet
	 */
	public void addTransitionFireListener(ITransitionFireListener listener) {
		jpFireTransition.addTransitionFireListener(listener);
	}

	
	@Override
	public void newWfnStatus(WfnStatusInfo statusInfo) {
		jpFileOperations.newWfnStatus(statusInfo);
		jpNetInfo.newWfnStatus(statusInfo);
		jpFireTransition.newWfnStatus(statusInfo);
		if (( !statusInfo.isWfn()) 
				|| (statusInfo.getStartPlace() == statusInfo.getEndPlace())) 
			setComponentWithChildrenEnabled(jpFireTransition, false);
		else
			setComponentWithChildrenEnabled(jpFireTransition, true);
	}
	
	/**
	 * Weiterleitung an gleichnamige Methode von {@link #jpZoom}.
	 * @param zoomManagement wird an {@link #jpZoom} weitergeleitet
	 */
	public void setZoomManagement(ZoomManagement zoomManagement) {
		jpZoom.setZoomManagement(zoomManagement);
	}

	/**
	 * Weiterleitung an gleichnamige Methode von {@link #jpFileOperations}.
	 * @param fileManagement wird an {@link #jpFileOperations} weitergeleitet
	 */
	public void setFileManagement(FileManagement fileManagement) {
		jpFileOperations.setFileManagement(fileManagement);
	}
	
	/**
	 * Weiterleitung an gleichnamige Methode von {@link #jpElementSize}.
	 * @param elementSizeManagement wird an {@link #jpElementSize} weitergeleitet
	 */
	public void setElementSizeManagement(ElementSizeManagement elementSizeManagement) {
		jpElementSize.setElementSizeManagement(elementSizeManagement);
	}
	
	/**
	 * Weiterleitung an gleichnamige Methode von {@link #jpNetInfo}.
	 * @param eGVerwaltung wird an {@link #jpNetInfo} weitergeleitet
	 */
	public void setCircleTest(CircleTest circleTest) {
		jpNetInfo.setCircleTest(circleTest);
	}
	
	
	/**
	 * Setzt {@link #setEnabled(boolean)} eines Panels und von allen 
	 * auf dem Panel sitzenden Komponenten.
	 * Anmerkung: Dies gilt nur für die erste Ebene. Haben die "Kind"-Komponenten wiederum 
	 * "Kind"-Elemente so werden diese nicht beachtet.
	 * @param component Panel, dessen {@link #setEnabled(boolean)} gesetzt werden soll
	 * @param b boolscher Wert auf den das Panel gesetzt werden soll.
	 */
	static void setComponentWithChildrenEnabled(JComponent component, boolean b) {
		for (Component child : component.getComponents())
			child.setEnabled(b);
		component.setEnabled(b);
	}
}
