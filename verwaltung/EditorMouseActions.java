package verwaltung;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.event.MouseInputAdapter;

import gui.EWfnEditModus;
import gui.ICentralConstants;
import listeners.IEditModusListener;
import listeners.IRedrawListener;
import wfnmodel.elements.EWfnElement;
import wfnmodel.interfaces.IWfnElement;
import wfnmodel.interfaces.IWfnModelChanging;
import wfnmodel.interfaces.IWfnTransitionAndPlace;

/**
 * Klasse zur Verwaltung der möglichen Mausaktionen im {@link gui.JPanelEditor}.
 *
 */
class EditorMouseActions extends MouseInputAdapter implements IEditModusListener,
														  ICentralConstants {

	/**Schnittstelle um Veränderungen am Datenmodell vorzunehmen*/
	private IWfnModelChanging wfnModel;
	/**Die aktuelle {@link ZoomManagement}. */
	private ZoomManagement zoom;
	/** Liste der ausgewählten Elemente */
	private SelectionManagement<IWfnElement> selection;
	/**Die aktuelle {@link CoordinateManagement} */
	private CoordinateManagement coordinates;
	/**Die aktuelle {@link MarkingManagement} */
	private MarkingManagement markingManagement;
	
	/**(Für den Editormodus SELECT) Gibt an ob es ein {@link #startElement} gibt.*/
	private boolean startElementExists;
	/**(Für den Editormodus SELECT) Dasjenige ausgewählte Element, über dem sich der Mauszeiger befand,
	 * als eine Maustaste gedrückt wurde.*/
	private IWfnElement startElement;
	/**(Für den Editormodus SELECT) Die Mausposition, wenn die Maustaste gedrückt wurde.*/
	private Point startMousePosition;
	/** (Für den Editormodus SELECT) Zum Speichern (bei einer Auswahl größer 1) 
	 * der Abstandsverhältnisse der ausgewählten Elemente zum {@link #startElement}. */
	private HashMap<IWfnElement, Point> selectionDistances;
	/**(Für den Editormodus SELECT) Zum Speichern (bei einer Auswahl größer 1) 
	 * der größten x und der größten y Verschiebung der ausgewählten Elemente zum {@link #startElement}
	 * (so dass man die Auswahl nicht über den Rand hinaus schieben kann).*/
	private int xLowerThenStartElemX, yLowerThenStartElemY;
	/** (Für den Editormus ADD_ARC) Gibt an, ob schon ein Kanteausgangselement gewählt wurde.*/
	private boolean newArcSourceExists;
	/**(Für den Editormodus ADD_ARC) Das für die hinzuzufügende Kante gewählte Ausgangselement.*/
	private IWfnTransitionAndPlace newArcSource;
	/**Der aktuelle Editormodus */
	private EWfnEditModus editModus;
	/**Liste der Horcher, die informiert werden möchten, wenn die Zeichnung einer Linie oder eines Rechtecks benötigt wird*/
	private ArrayList<IRedrawListener> redrawListeners;

	/**
	 * Instanziert eine EditorMouseActions und stellt den Editormodus auf SELECT. 
	 * @param wfnModel Schnittstelle um Veränderungen am Datenmodell vorzunehmen
	 * @param coordinates Die aktuelle {@link CoordinateManagement} 
	 * @param selection Die aktuelle {@link SelectionManagement}
	 * @param markingManagement Die aktuelle {@link MarkingManagement}
	 * @param zoom Die aktuelle {@link ZoomManagement}
	 */
	EditorMouseActions(IWfnModelChanging wfnModel,
						CoordinateManagement coordinates,
						SelectionManagement<IWfnElement> selectionManagement,
						MarkingManagement markingManagement,
						ZoomManagement zoom) {
		this.wfnModel = wfnModel;
		this.coordinates = coordinates;
		this.selection = selectionManagement;
		this.markingManagement = markingManagement;
		this.zoom = zoom;
		startElementExists = false;
		newArcSourceExists = false;
		selectionDistances = new HashMap<>(5);
		xLowerThenStartElemX = 0;
		yLowerThenStartElemY = 0;
		editModus = EWfnEditModus.SELECT;
		redrawListeners = new ArrayList<>(1);	
	}
	
	/**
	 * Fügt den übergebenen Horcher der {@link #redrawListeners} hinzu.
	 * @param listener wird der {@link #redrawListeners} hinzugefügt
	 */
	void addRedrawListener(IRedrawListener listener) {
		redrawListeners.add(listener);
	}
	
	/**
	 * Entfernt den übergebenen Horcher von der {@link #redrawListeners}.
	 * @param listener wird von der {@link #redrawListeners} enfernt
	 */
	void removeRedrawListener(IRedrawListener listener) {
		if (redrawListeners.contains(listener))
			redrawListeners.remove(listener);
	}
	
	/**
	 * Informiert alle Horcher der {@link #redrawListeners} über eine benötigte Zeichnung.
	 * @param form LINE oder RECTANGLE
	 * @param startMousePosition Eckpunkt/Startpunkt der Zeichnung
	 * @param angle2 Eckpunkt/Endpunkt der Zeichnung
	 */
	private void fireRedraw(int form, Point angle1, Point angle2) {
		for (IRedrawListener listener : redrawListeners)
			listener.redraw(form, angle1, angle2);
	}

	/* (non-Javadoc)
	 * @see listeners.IEditModusListener#editorModusGeaendert(gui.EWfnEditModus)
	 * Falls durch die Modus-Änderung nötig, wird die derzeitige Auswahl geleert oder die Kanten-Auswahl abgebrochen
	 * und jeweils ein neu-zeichnen ausgelöst.
	 */
	@Override
	public void editModusChanged(EWfnEditModus newModus) {
		if (editModus == newModus) return;
		switch (editModus) {
			case SELECT:
				selection.clearAndAddAndFire(null, NEW_SELECTION);
				break;
			case ADD_ARC:
				if (!newArcSourceExists) break;
				fireRedraw(NOTHING, null, null);
				newArcSourceExists = false;
				newArcSource = null;
				break;
			default:break;
		}
		editModus = newModus;
	}
	
	/* (non-Javadoc)
	 * Falls im Editormodus SELECT eine Maustaste gedrückt wird
	 * und sich an dieser Stelle ein Element befindet,
	 * wird die selection aktualisiert, 
	 * und gegebenenfalls die Abstände der ausgewählten Elemente zueinander.
	 * Falls im Editormodus SELECT eine Maustaste gedrückt wird
	 * und es befindet sich an dieser Stelle kein Element,
	 * wird die Mausposition in #startMousePosition gespeichert,
	 * für das mögliche Zeichnen eines Rechtecks bei einer Mausbewegung.
	 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (editModus != EWfnEditModus.SELECT) return; 
		IWfnElement elem = coordinates.getElementAt(e.getPoint());
		if (elem == null) {
			startMousePosition = zoom.calculateOut(e.getPoint());
			return;
		}
		
		startElementExists = true;
		startElement = elem;
		xLowerThenStartElemX = 0;
		yLowerThenStartElemY = 0;
		if (!selection.contains(startElement)) {
			if (!e.isControlDown()) 
				selection.clear();
			selection.addAndFire(startElement, NEW_SELECTION);
		}
		if (selection.size() <= 1) return;
		
		int x, y;
		if (startElement.getWfnElementType() != EWfnElement.ARC ) {
			x = ((IWfnTransitionAndPlace)startElement).getPosition().x;
			y = ((IWfnTransitionAndPlace)startElement).getPosition().y;
		} else {
			Point p = zoom.calculateOut(e.getPoint());
			x = p.x;
			y = p.y;
		}
		selectionDistances.clear();
		for (IWfnElement selected : selection) {
			if ((selected.getWfnElementType() != EWfnElement.ARC)
					&& (selected != startElement)) {
				Point distanceToStartElem = new Point(
						((IWfnTransitionAndPlace)selected).getPosition().x - x,
						((IWfnTransitionAndPlace)selected).getPosition().y - y);
				if (distanceToStartElem.x < xLowerThenStartElemX) 
					xLowerThenStartElemX = distanceToStartElem.x;
				if (distanceToStartElem.y < yLowerThenStartElemY)
					yLowerThenStartElemY = distanceToStartElem.y;
				selectionDistances.put(selected, distanceToStartElem);
			}
		}
		xLowerThenStartElemX *= -1;
		yLowerThenStartElemY *= -1;
	}
	

	/* (non-Javadoc)
	 * 
	 * Falls im Editormodus SELECT die Maus mit gedrückter Maustaste bewegt wird, 
	 * und es ausgewählte Elemente gibt,
	 * werden diese Elemente der Mausbewegung entsprechend verschoben,
	 * aber in ihrer Entfernung zum Element, über welchem sich die Maus befindet (dem #startElement), 
	 * bleiben die Elemente gleich (gespeichert in #selectionDistances).
	 * 
	 * Falls im Editormodus SELECT die Maus mit gedrückter Maustaste bewegt wird, 
	 * und es gibt keine ausgewählten Elemente, 
	 * dann wird ein Rechteck gezeichnet zwischen der jetzigen Mausposition
	 * und der Position, an der die Maustaste gedrückt wurde. (#startMousePosition)
	 * 
	 * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		if (editModus != EWfnEditModus.SELECT) return;
		if (!startElementExists) {
			fireRedraw(RECTANGLE, startMousePosition, zoom.calculateOut(e.getPoint()));
			return;
		}
		final Point mousePosWithoutZoom = zoom.calculateOut(e.getPoint());
		if  ((startElement.getWfnElementType() == EWfnElement.ARC)
				|| (mousePosWithoutZoom.x < (xLowerThenStartElemX + EWfnElement.BASEFACTOR))
				|| (mousePosWithoutZoom.y < (yLowerThenStartElemY + EWfnElement.BASEFACTOR))) 
			return;
		((IWfnTransitionAndPlace)startElement).setPosition(mousePosWithoutZoom);
		if (selection.size() > 1) 
			for (IWfnElement selectedElem : selection) 
				if ((selectedElem.getWfnElementType() != EWfnElement.ARC)
						&& (selectedElem != startElement)) {
					Point position = ((IWfnTransitionAndPlace)selectedElem).getPosition();
					position.setLocation(mousePosWithoutZoom.getX() + selectionDistances.get(selectedElem).x,
							mousePosWithoutZoom.getY() + selectionDistances.get(selectedElem).y);
				}
		selection.fireSelectionChangeOccurred(NEW_SELECTION);
	}

	/* (non-Javadoc)
	 * Falls im Editormodus ADD_ARC die Maus bewegt wird,
	 * und schon eine Kante oder Transition als Kantenausgangselement ausgewählt wurde,
	 * wird eine Linie vom #newArcSource zur Cursorposition gezeichnet. 
	 * @see java.awt.event.MouseAdapter#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		if ((editModus == EWfnEditModus.ADD_ARC) 
				&& (newArcSourceExists)) {
			fireRedraw(
					LINE,
					newArcSource.getPosition(),
					zoom.calculateOut(e.getPoint()));
		}
	}

	/* (non-Javadoc)
	 * 
	 * Falls die linke Maustaste losgelassen wird im Editormodus SELECT,
	 *   wird die Auswahl aktualisiert.
	 * Falls die linke Maustaste losgelassen wird im Editormodus ADD_PLACE,
	 * 	 dann wird via Datenmodell eine neue Stelle instanziert. 
	 * Falls die linke Maustaste losgelassen wird im Editormodus ADD_TRANSITION,
	 * 	 dann wird via Datenmodell eine neue Transition instanziert.
	 * Falls die linke Maustaste losgelassen wird im Editormodus ADD_ARC,
	 * 	 und es befindet sich an der Mausposition ein Element,
	 * 	 so wird dieses ein Kantenausgangselement,
	 *   oder falls ein solches schon gibt, 
	 *   und der Elementtyp des neuen Elements anders ist als der des Kantenausgangselements,
	 *   so wird via Datenmodell eine neue Kante instanziert.  	 
	 * 
	 * Falls die rechte Maustaste losgelassen wird,
	 *   und es befindet sich an dieser Stelle eine aktivierte Transition,
	 *   so wird diese geschaltet.
	 *    
	 * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON1
				&& e.getButton() != MouseEvent.BUTTON3)
			return;
		if (e.getButton() == MouseEvent.BUTTON3) {
			markingManagement.fire(coordinates.getElementAt( e.getPoint() ));
			return;
		}
		switch (editModus) {
		case SELECT:
			if (!startElementExists) {
				selection.clearAndAddALLAndFire(
						coordinates.getElementsIn(
								startMousePosition, 
								zoom.calculateOut(e.getPoint())),
						NEW_SELECTION);
				return;
			}
			IWfnElement newlySelected = coordinates.getElementAt(e.getPoint());
			if (newlySelected != null) {
				if (!selection.contains(newlySelected)) {
					if (!e.isControlDown()) selection.clear();
					selection.addAndFire(newlySelected, NEW_SELECTION);
				}
			} else 
				if (selection.size() != 0) 
					selection.clearAndAddAndFire(null, NEW_SELECTION);
			startElementExists = false;
			break;
		case ADD_PLACE:
			wfnModel.createPlace(zoom.calculateOut(e.getPoint()));
			break;
		case ADD_TRANSITION:
			wfnModel.createTransition(zoom.calculateOut(e.getPoint()));
			break;
		case ADD_ARC:
			IWfnElement element = coordinates.getElementAt(e.getPoint());
			if ((element == null)
					|| (element.getWfnElementType() == EWfnElement.ARC)) 
				return;
			if (!newArcSourceExists) {
				newArcSource = (IWfnTransitionAndPlace) element;
				newArcSourceExists = true;
				selection.clearAndAddAndFire(element, ARC_SELECTION);
				return;
			} 
			if (element == newArcSource) {
				newArcSource = null;
				newArcSourceExists = false;
				selection.clearAndFire(ARC_SELECTION);
				return;
			} 
			if (element.getWfnElementType() != newArcSource.getWfnElementType()) {
				wfnModel.createArc(newArcSource, (IWfnTransitionAndPlace) element);
				newArcSourceExists = false;
				newArcSource = null;
			}
			break;	 
		}
	}


}	
