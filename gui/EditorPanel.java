package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JPanel;

import listeners.ISelectionChangingListener;
import listeners.IElementSizeListener;
import listeners.IWfnStatusListener;
import listeners.IRedrawListener;
import listeners.IZoomListener;
import wfnmodel.WfnStatusInfo;
import wfnmodel.elements.EWfnElement;
import wfnmodel.interfaces.IWfnArc;
import wfnmodel.interfaces.IWfnElement;
import wfnmodel.interfaces.IWfnPlace;
import wfnmodel.interfaces.IWfnTransition;
import wfnmodel.interfaces.IWfnTransitionAndPlace;

/**
 * Panel zur Darstellung des Workflownetzes.
 *
 */
public class EditorPanel extends JPanel implements IWfnStatusListener, 
													ISelectionChangingListener,
													IRedrawListener,
													IZoomListener,
													IElementSizeListener {

	private static final long serialVersionUID = -5582213274116886884L;
	
	/** Der letztübermittelte Zustand/Status des Workflownetzes.*/
	private WfnStatusInfo statusInfo;
	/** Liste der momentan ausgewählten WFN-Elemente. */
	private ArrayList<? extends IWfnElement> selectedElements;
	private int requiredShape;
	private Point shapePoint1, shapePoint2;
	private double zoomFactor;
	private int selectionType;
	/* Die Darstellungsgröße von Stellen, Transitionen und Pfeilspitzen*/
	private int elementSize;
	

	public EditorPanel() {
		
		selectedElements = null;
		selectionType = NEW_SELECTION;
		requiredShape = NOTHING;
		zoomFactor = 1d;
		statusInfo = new WfnStatusInfo(); 
		elementSize = EWfnElement.BASEFACTOR;

		setBackground(Color.WHITE);
	} 

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		zoomFactorCalculateIn(g2);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawSelection(g2);
		drawMarkingsAndEnabledTransitions(g2);
		drawAllElements(g2);
		drawShapeWhenMouseMovement(g2);
	}

	/**
	 * Zeichnet die in der Liste {@link #selectedElements} gespeicherten WFN-Elemente
	 * durch Aufruf der Methode {@link wfnmodel.elements.EWfnElement#zeichneAlsAusgewaehlt(Graphics2D, Point, Color)},
	 * wobei {@link #selectionType} Einfluss auf die Farbe hat.
	 * @param g2 Objekt auf dem die Zeichnung ausgeführt wird
	 */
	private void drawSelection(Graphics2D g2) {
		if (selectedElements == null || selectedElements.isEmpty()) return;
		switch (selectionType) {
		case NEW_SELECTION:
			for (IWfnElement elem : selectedElements) 
				if (elem.getWfnElementType() != EWfnElement.ARC) 
					elem.getWfnElementType().drawAsSelected(
							g2, 
							((IWfnTransitionAndPlace) elem).getPosition(), 
							EEditorColors.SELECTION, 
							elementSize);
				else 
					elem.getWfnElementType().drawAsSelected(
							g2, 
							((IWfnArc) elem).getSource().getWfnElementType(),
							((IWfnArc) elem).getSource().getPosition(),
							((IWfnArc) elem).getTarget().getPosition(),
							EEditorColors.SELECTION, 
							elementSize);
			break;
		case ARC_SELECTION:
			IWfnTransitionAndPlace arcSource = (IWfnTransitionAndPlace) selectedElements.get(0);
			arcSource.getWfnElementType().drawAsSelected(
					g2, 
					arcSource.getPosition(), 
					EEditorColors.ARC_SOURCE_SELECTED, 
					elementSize);
			break;
		default: break;
		}
	}

	/**
	 * Zeichnet, wenn {@link #isDrawingRequired} wahr ist, je nach {@link #requiredShape}
	 * ein Rechteck oder eine Linie zwischen {@link #shapePoint1} und {@link #shapePoint2}.
	 * @param g2 Objekt auf dem die Zeichnung ausgeführt wird
	 */
	private void drawShapeWhenMouseMovement(Graphics2D g2) {
		switch(requiredShape) {
		case RECTANGLE:
			int x, y, height, width;
			if (shapePoint1.x < shapePoint2.x) {
				x = shapePoint1.x;
				width = shapePoint2.x - shapePoint1.x;
			} else {
				x = shapePoint2.x;
				width = shapePoint1.x - shapePoint2.x;
			}
			if (shapePoint1.y < shapePoint2.y) {
				y = shapePoint1.y;
				height = shapePoint2.y - shapePoint1.y;
			} else {
				y = shapePoint2.y;
				height = shapePoint1.y - shapePoint2.y;
			}
			g2.drawRect(x,y,width,height);
			break;
		case LINE:
			g2.drawLine(shapePoint1.x, shapePoint1.y, 
						shapePoint2.x, shapePoint2.y);
			break;
		default: break;	
		}
		requiredShape = NOTHING;
	}

	/**
	 * Einberechnet {@link #zoomFactor} zur Skalierung sämtlicher Zeichenoperationen auf dem 
	 * Graphics2D-Object.
	 * @param g2 Objekt auf dem die Skalierung ausgeführt wird
	 */
	private void zoomFactorCalculateIn(Graphics2D g2) {
		AffineTransform at = AffineTransform.getScaleInstance(zoomFactor, zoomFactor);
        g2.transform(at);
	}

	/**
	 * Zeichnet sämtliche WFN-Elemente des aktuellen Datenmodels, die {@link #statusInfo} entnommen
	 * werden. Gegebenenfalls wird {@link #setPreferredSize(Dimension)} des Panels angepasst.
	 * @param g2 Objekt auf dem die Zeichnung ausgeführt wird
	 */
	private void drawAllElements(Graphics2D g2) {
		double maxX = 0d;
		double maxY = 0d;
		for (IWfnTransitionAndPlace elem : statusInfo.getTransitionsAndPlaces()) {
			Point position = elem.getPosition();
			EWfnElement type = elem.getWfnElementType();
			if ((position.x*zoomFactor) > maxX) maxX = position.x * zoomFactor;
			if ((position.y*zoomFactor) > maxY) maxY = position.y * zoomFactor;
			type.draw(g2, position, elementSize);
			drawName(elem.getName(), position, g2);
			for (IWfnTransitionAndPlace nextElem :  elem.getOutputElements())
				EWfnElement.ARC.draw(g2, type, position, nextElem.getPosition(), elementSize);
		}
		Dimension panelSize = new Dimension((int)maxX + (2 * elementSize) +10, (int)maxY + (2 * elementSize) +10);
		setPreferredSize(panelSize);
		revalidate();
	}

	/**
	 * Zeichnet, wenn laut {@link #statusInfo} das Netz alle Bedingungen für ein Workflownetz erfüllt,
	 * die Start- und die Endstelle, alle Marken, sowie alle aktivierten Transitionen.
	 * @param g2 Objekt, auf dem die Zeichnung ausgeführt wird
	 */
	private void drawMarkingsAndEnabledTransitions(Graphics2D g2) {
		if (!statusInfo.isWfn() 
				|| statusInfo.getStartPlace() == statusInfo.getEndPlace()) 
			return;
				 
		EWfnElement.PLACE.drawAsSelected(g2, statusInfo.getStartPlace().getPosition(), EEditorColors.STARTPLACE, elementSize);
		EWfnElement.PLACE.drawAsSelected(g2, statusInfo.getEndPlace().getPosition(), EEditorColors.ENDPLACE, elementSize);
		for (IWfnPlace place: statusInfo.getMarkings())
			g2.fillOval(place.getPosition().x - (elementSize/2), 
						place.getPosition().y - (elementSize/2), 
						elementSize, elementSize);
		if (statusInfo.getEnabledTransitions() != null)
			for (IWfnTransition transition : statusInfo.getEnabledTransitions())
				EWfnElement.TRANSITION.drawAsSelected(g2, transition.getPosition(), EEditorColors.ENABLED, elementSize); 
		if (statusInfo.getContactTransitions() != null)
			for (IWfnTransition transition : statusInfo.getContactTransitions())
				EWfnElement.TRANSITION.drawAsSelected(g2, transition.getPosition(), EEditorColors.CONTACT, elementSize);
	}

	/**
	 * Zeichnet Worte auf das Panel.
	 * @param name zu schreibende/zeichnende Worte
	 * @param position Position, unterhalb welcher der Wortanfang liegt
	 * @param g2 Objekt auf dem die Zeichnung ausgeführt wird
	  */
	private void drawName(String name, Point position, Graphics2D g2) {
		if (!name.equals("")) {
			g2.drawString(name, position.x, position.y + elementSize + EWfnElement.BASEFACTOR);
		}
	}
	
	/* 
	 * Bei Aufruf wird #statusInfo aktualisiert und die Liste #ausgewahlteElemente auf null gesetzt.
	 * Und repaint() aufgerufen.
	 * @see listeners.IWfnStatusListener#modellStatusAenderung(wfnmodel.WFNStatusInfo)
	 */
	@Override
	public void newWfnStatus(WfnStatusInfo statusInfo) {
		this.selectedElements = null;
		this.statusInfo = statusInfo;
		repaint();
	}

	@Override
	public void selectionChangeOccurred(int selectionType, ArrayList<? extends IWfnElement> selectedElements) {
		this.selectionType = selectionType;
		this.selectedElements = selectedElements;
		repaint();	
	}
	
	@Override
	public void redraw(int form, Point startMousePosition, Point nowMousePosition) {
		requiredShape = (startMousePosition == null || nowMousePosition == null)
				? NOTHING
				: form;
		shapePoint1 = startMousePosition;
		shapePoint2 = nowMousePosition;
		repaint();
	}

	@Override
	public void zoomFactorChanged(double newZoomFactor) {
		if (zoomFactor != newZoomFactor) {
			zoomFactor = newZoomFactor;
			repaint();
		}
	}

	@Override
	public void elementSizeChanged(int newSize) {
		if (elementSize != newSize) {
			elementSize = newSize;
			repaint();
		}
		
	}
	
}
