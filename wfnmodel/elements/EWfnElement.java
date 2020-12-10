package wfnmodel.elements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

/**
 * Enum zur Typunterscheidung der Elemente eines Workflownetzes.
 * Desweiteren werden den Typen des Enums ihre Zeichen-Methoden mitgegeben.
 *
 */
public enum EWfnElement {
	PLACE() {

		@Override
		public void draw(Graphics2D g2, Point pos, int elemSize) {
			g2.drawOval(pos.x - elemSize, pos.y - elemSize, elemSize * 2, elemSize * 2);
		}

		@Override
		public void drawAsSelected(Graphics2D g2, Point pos, Color fillColor, int elemSize) {
 		   	Color currentColor = g2.getColor();
			g2.setColor(fillColor);
			g2.fillOval(pos.x - elemSize, pos.y - elemSize, elemSize * 2, elemSize * 2);
			g2.setColor(currentColor);
		}
		
	},
	TRANSITION() {

		@Override
		public void draw(Graphics2D g2, Point pos, int elemSize) {
			g2.drawRect(pos.x - elemSize, pos.y - elemSize, elemSize * 2, elemSize * 2);
		}
		
		@Override
		public void drawAsSelected(Graphics2D g2, Point pos, Color fillColor, int elemSize) {
 			Color currentColor = g2.getColor();
			g2.setColor(fillColor);
			g2.fillRect(pos.x - elemSize, pos.y - elemSize, elemSize * 2, elemSize * 2);
			g2.setColor(currentColor);
		}
		
	},
	ARC() {
		BasicStroke thickLine = new BasicStroke(BASEFACTOR/2);
		Stroke currentLine;
		Color currentColor;

		@Override
		public void draw(Graphics2D g2, EWfnElement type, Point sourcePos, Point targetPos, int elemSize) {
			Point sourceBorder = getShift(type, sourcePos, targetPos, elemSize);
			Point targetBorder = getShift(getOpposedElementType(type), targetPos, sourcePos, elemSize);
			drawArrowLine(g2, sourceBorder.x, sourceBorder.y, targetBorder.x, targetBorder.y, elemSize, elemSize / 2);
		}
		
		@Override
		public void drawAsSelected(Graphics2D g2, EWfnElement type, Point sourcePos, Point targetPos, Color fillColor, int elemSize) {
			Point sourceBorder = getShift(type, sourcePos, targetPos, elemSize);
			Point targetBorder = getShift(getOpposedElementType(type), targetPos, sourcePos, elemSize);
			currentLine = g2.getStroke();
			currentColor = g2.getColor();
			g2.setStroke(thickLine);
			g2.setColor(fillColor);
			g2.drawLine(sourceBorder.x, sourceBorder.y, targetBorder.x, targetBorder.y);
			g2.setStroke(currentLine);
			g2.setColor(currentColor);
		}

	};

	/**
	 * Basisgröße und Maßeinheit bei der Darstellung des WFN.
	 */
	public final static int BASEFACTOR = 15;
	

	/**
	 * Zeichnet je nach Typ, auf der sie aufgerufen wird, eine Stelle oder eine Transition.
	 * Im Falle des Aufrufs von einer Kante passiert nichts. 
	 * @param g2 das Graphics2D-Object auf dem gezeichnet wird
	 * @param position Position, an welcher die Zeichnung erfolgen soll
	 */
	public void draw(Graphics2D g2, Point position, int elementSize) {
		// wird überschrieben für die Typen PLACE und TRANSITION.
		// im Fall von Kante passiert nichts.
	}

	/**
	 * Zeichnet, falls sie von einer Kante aufgerufen wird, eine Kante.
	 * Im Falle des Aufrufs von einer Stelle oder einer Transition passiert nichts.
	 * @param g2 das Graphics2D-Object auf dem gezeichnet wird
	 * @param type Typ des Elements, von dem die Kante ausgeht
	 * @param sourcePos Position des Elements, von dem die Kante ausgeht
	 * @param targetPos Position des Elements, in dem die Kante endet
	 */
	public void draw(Graphics2D g2, EWfnElement type, Point sourcePos, Point targetPos, int elementSize) {
		// wird überschrieben für den Typ ARC.
		// im Fall von PLACE und TRANSITION passiert nichts.
	}
	
	/**
	 * Zeichnet je nach Typ, auf der sie aufgerufen wird, eine mit Farbe gefüllte
	 * Stelle oder Transition.
	 * Im Falle des Aufrufs von einer Kante passiert nichts. 
	 * @param g2 das Graphics2D-Object auf dem gezeichnet wird
	 * @param position Position, an welcher die Zeichnung erfolgen soll
	 * @param fillColor Farbe, mit der die Füllung vorgenommen werden soll
	 */
	public void drawAsSelected(Graphics2D g2, Point position, Color fillColor, int elementSize) {
		// wird überschrieben für die Typen PLACE und TRANSITION.
		// im Fall von Kante passiert nichts.
	}

	/**
	 * Zeichnet eine ausgewählte Kante.
	 * Im Falle des Aufrufs von einer Stelle oder einer Transition passiert nichts.
	 * @param g2 g2 das Graphics2D-Object auf dem gezeichnet wird
	 * @param type Typ des Elements, von dem die Kante ausgeht
	 * @param sourcePos Position des Elements, von dem die Kante verläuft
	 * @param targetPos Position des Elements, zu dem die Kante verläuft
	 * @param fillColor Farbe, mit welcher die Füllung vorgenommen werden soll
	 */
	public void drawAsSelected(Graphics2D g2, EWfnElement type, Point sourcePos, Point targetPos, Color fillColor, int elementSize) {
		// wird überschrieben für den Typ ARC.
		// im Fall von PLACE und TRANSITION passiert nichts.
	}
	
	/**
	 * Gibt den anderen Elementtyp zurück, der keine Kante ist.
	 * @param type Ausgangstyp
	 * @return der andere Elementtyp, der keine Kante ist
	 */
	private static EWfnElement getOpposedElementType(EWfnElement type) {
		return (type == PLACE) ? TRANSITION : PLACE;
	}

	
	
	/**
	 * Methode zur Berechnung des Punktes auf dem Rand der Stelle oder Transition,
	 * von wo aus die Kante gezeichnet werden soll.
	 * @param type Typ des Elements, dessen Verschiebung zum Rand berechnet werden soll
	 * @param sourcePos Position des Elements, von welchem die Verschiebung berechnet werden soll
	 * @param targetPos Position des Elements, zu welchem die Kante verlaufen soll
	 * @return die Verschiebung zum Mittelpunkt ("der Position") des Elements, von wo aus die Kante gezeichnet werden soll
	 */
	private static Point getShift(EWfnElement type, Point sourcePos, Point targetPos, int elemSize) {
		int deltaX = Math.abs(targetPos.x - sourcePos.x);
		int deltaY = Math.abs(targetPos.y - sourcePos.y);
		double distance = sourcePos.distance(targetPos);

		double xResult = deltaX * elemSize / distance;
		double yResult = deltaY * elemSize / distance;

		Point shift = sourcePos;
		switch (type) {
		case PLACE:
			if (sourcePos.x < targetPos.x) {
				// zu befindet sich rechts von von
				if (sourcePos.y < targetPos.y) {
					// zu befindet sich unter von
					shift = new Point(sourcePos.x + ((int) xResult), sourcePos.y + ((int) yResult));
				} else {
					// zu befindet sich über oder auf gleicher Höhe von von
					shift = new Point(sourcePos.x + ((int) xResult), sourcePos.y - ((int) yResult));
				}
			} else {
				// zu befindet sich links oder auf gleicher Höhe von von
				if (sourcePos.y < targetPos.y) {
					// zu befindet sich unter von
					shift = new Point(sourcePos.x - ((int) xResult), sourcePos.y + ((int) yResult));
				} else {
					// zu befindet sich über oder auf gleicher Höhe von von
					shift = new Point(sourcePos.x - ((int) xResult), sourcePos.y - ((int) yResult));
				}
			}
			break;
		case TRANSITION:
			if (sourcePos.x < targetPos.x) {
				// zu befindet sich rechts von von
				if (sourcePos.y < targetPos.y) {
					// zu befindet sich unter von
					if (deltaX <= deltaY) {
						// schneidet auf der Unterseite rechts oder im Eckpunkt
						shift = new Point(sourcePos.x + ((int) xResult), sourcePos.y + elemSize); 
					} else {
						// schneidet auf der rechten Seite unten
						shift = new Point(sourcePos.x + elemSize, sourcePos.y + ((int) yResult));
					}
				} else {
					// zu befindet sich über oder auf gleicher Höhe von von
					if (deltaX <= deltaY) {
						// schneidet auf der Oberseite rechts oder im Eckpunkt
						shift = new Point(sourcePos.x + ((int) xResult), sourcePos.y - elemSize);
					} else {
						// schneidet auf der rechten Seite oben
						shift = new Point(sourcePos.x + elemSize, sourcePos.y - ((int) yResult));
					}
				}
			} else {
				// zu befindet sich links oder auf gleicher Höhe von von
				if (sourcePos.y < targetPos.y) {
					// zu befindet sich unter von
					if (deltaX <= deltaY) {
						// schneidet auf der Unterseite links oder im Eckpunkt
						shift = new Point(sourcePos.x - ((int) xResult), sourcePos.y + elemSize); //
					} else {
						// schneidet auf der linken Seite unten
						shift = new Point(sourcePos.x - elemSize, sourcePos.y + ((int) yResult));
					}
				} else {
					// zu befindet sich über oder auf gleicher Höhe von von
					if (deltaX <= deltaY) {
						// schneidet auf der Oberseite links oder im Eckpunkt
						shift = new Point(sourcePos.x - ((int) xResult), sourcePos.y - elemSize); //
					} else {
						// schneidet auf der rechten Seite
						shift = new Point(sourcePos.x - elemSize, sourcePos.y - ((int) yResult));
					}
				}
			}
			break;
			default:
				break;
		}
		return shift;
	}
	



	/**
	 * Die Methode zeichnet einen Pfeil zwischen zwei Punkten, und die Methode ist in ihrer ganzen Klarheit
	 * und Effizienz entliehen beim User phibao237, auf stackoverflow am 14.12.2017.
	 * https://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java#2027641
	 * @param g the graphics component.
	 * @param x1 x-position of first point.
	 * @param y1 y-position of first point.
	 * @param x2 x-position of second point.
	 * @param y2 y-position of second point.
	 * @param d  the width of the arrow.
	 * @param h  the height of the arrow.
	 */
	private static void drawArrowLine(Graphics2D g, int x1, int y1, int x2, int y2, int d, int h) {
	    int dx = x2 - x1, dy = y2 - y1;
	    double D = Math.sqrt(dx*dx + dy*dy);
	    double xm = D - d, xn = xm, ym = h, yn = -h, x;
	    double sin = dy / D, cos = dx / D;
	
	    x = xm*cos - ym*sin + x1;
	    ym = xm*sin + ym*cos + y1;
	    xm = x;
	
	    x = xn*cos - yn*sin + x1;
	    yn = xn*sin + yn*cos + y1;
	    xn = x;
	
	    int[] xpoints = {x2, (int) xm, (int) xn};
	    int[] ypoints = {y2, (int) ym, (int) yn};
	
	    g.drawLine(x1, y1, x2, y2);
	    g.fillPolygon(xpoints, ypoints, 3);

	}


}
